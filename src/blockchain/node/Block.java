package blockchain.node;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Block.class);
	public int chainCount;// 채굴도중 이 블록이 이미 채굴되었는지 알기위한 체인의 길이
	public String hash = "";// 이 블록의 해쉬코드
	public String previousHash;// 이전 블록의 해쉬코드
	public String merkleRoot;// 머클루트의 해쉬코드
	public List<Transaction> txs = new ArrayList<>();// 이 블록에 기록된 트랜잭션 목록
	public long timeStamp;// 블록 생성 시간
	public long difficulty = 1000;
	public int nonce;// 채굴을 위한 더미데이터
	public PublicKey miner;// 채굴 보상을 받을 놈

	public Block(Block pBlock) {
		timeStamp = System.currentTimeMillis();
		if (pBlock == null) {
			if (BcNode.chain.size() == 0) {
				previousHash = "0";
				difficulty = HashUtil.getDifficulty(difficulty, BcNode.guideTime, timeStamp, timeStamp);
			} else {
				throw new RuntimeException();
			}
		} else {
			previousHash = pBlock.hash;
			chainCount = BcNode.chain.size();
			difficulty = HashUtil.getDifficulty(pBlock.difficulty, BcNode.guideTime, pBlock.timeStamp, timeStamp);
		}
	}

	// 블록이 갖고있는 데이터를 전부 합해서 해쉬코드 생성
	public String calcHash(int nonce) {
		StringBuilder sb = new StringBuilder(chainCount);
		sb.append(previousHash);
		sb.append(timeStamp);
		sb.append(difficulty);
		// 파라미터가 0일시는 이 블록의 해시코드를, 파라미터가 0이 아닐 경우는 해당 nonce값의 채굴여부를 검사할 해시코드를 반환
		if (nonce == 0) {
			sb.append(this.nonce);
		} else {
			sb.append(nonce);
		}
		sb.append(merkleRoot);
		return HashUtil.toSHA3_256(sb.toString());
	}

	// 블록 채굴하기
	public int mineBlock(PublicKey miner) {
		if (this.miner == null) {
			// 채굴 성공시 보상을 받는 트랜잭션
			Transaction rewardToMiner = ChainUtil.getReward(miner);
			// 트랜잭션을 검증하기위한 머클트리 기록, 기록시 트랜잭션 리스트에 채굴보상을 받는 부분을 추가했을 때의 머클루트 계산
			merkleRoot = HashUtil.getMerkleRoot(txs, rewardToMiner);
			// 채굴에 성공하거나 다른 놈이 채굴할 때까지 채굴을 시도
			int temp = 0;
			while (BcNode.isMiningable && !HashUtil.verifyMining(difficulty, calcHash(temp))
					&& BcNode.chain.size() == chainCount) {
				temp = (int) (Math.random() * Integer.MAX_VALUE);
			}
			// 채굴 성공한 놈이 본인일시 본인의 보상을 트랜잭션 목록에 추가
			if (BcNode.isMiningable && HashUtil.verifyMining(difficulty, calcHash(temp))) {
				this.miner = miner;
				nonce = temp;
				hash = calcHash(0);
				txs.add(0, rewardToMiner);
				logger.info("Block is mined! " + hash);
				logger.info("Mined nonce: " + nonce);
				return nonce;
			} else {
				logger.error("Mining canceled");
				return -1;
			}
		} else {
			return -1;
		}
	}

	// 블록에 저장된 트랜잭션들을 처리
	// 블록에 트랜잭션을 추가할 때 미리 트랜잭션의 유효성을 검사해야하며 유효하지 않은 트랜잭션을
	// 포함한 블록은 채굴에 성공해도 유효한 블록으로 처리되지 못함.
	public boolean processTxs(boolean isLoad) {
		for (int i = 0; i < txs.size(); i++) {
			if (!ChainUtil.processTransaction(this, txs.get(i), isLoad)) {
				BcNode.jta.append("errorTx: " + txs.get(i));
				for (i--; i >= 0; i--) {
					ChainUtil.rollbackTransaction(txs.get(i));
				}
				return false;
			}
		}
		return true;
	}
}
