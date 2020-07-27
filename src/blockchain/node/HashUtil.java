package blockchain.node;

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.GsonBuilder;

public final class HashUtil implements Serializable {
	// 입력받은 값을 SHA3-256방식으로 암호화하여 64자의 해쉬코드를 생성
	public static final String toSHA3_256(String input) {
		return Hex.toHexString(new SHA3.Digest256().digest(input.getBytes()));
	}

	// 프라이빗 키로 데이터에 싸인
	public static final byte[] applyECDSASig(PrivateKey key, String data) {
		try {
			Signature ecdsa = Signature.getInstance("ECDSA", "BC");
			ecdsa.initSign(key);
			ecdsa.update(data.getBytes());
			return ecdsa.sign();
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException();
		}
		return new byte[0];
	}

	// 프라이빗키로 싸인한 데이터가 맞는지 퍼블릭키로 검증
	public static final boolean verifyECDSASig(PublicKey key, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(key);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException();
		}
		return false;
	}

	// Object로 jsonView형식의 스트링을 생성
	public static String toPrettyJson(Object o) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}

	// 설정된 자릿수의 "000"꼴의 스트링을 생성
	//	public static String getDifficultyString(int difficulty) {
	//		return new String(new char[difficulty]).replace('\0', '0');
	//	}

	// Key를 64진법으로 문자코드에 영향을 받지않는 일련의 스트링으로 인코딩
	public static final String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	// 머클 트리방식으로 머클 루트의 해쉬코드를 생성
	public static String getMerkleRoot(List<Transaction> txs, Transaction rewardToMiner) {
		//트랜잭션 아이디 목록 만들기
		List<String> hashs = new ArrayList<>();
		for (Transaction tx : txs) {
			hashs.add(tx.txId);
		}
		// 채굴보상을 트랜잭션 아이디 목록에 추가
		if (rewardToMiner != null) {
			rewardToMiner.generateSignature(BcNode.god.privateKey);
			rewardToMiner.txId = rewardToMiner.calcHash();
			hashs.add(0, rewardToMiner.txId);
		}
		if (hashs.size() > 0) {
			int i = 2;
			while (i < hashs.size()) {
				i *= 2;
			}
			if (i != hashs.size()) {
				for (int j = hashs.size(); j < i; j++) {
					hashs.add(hashs.get(j - 1));
				}
			}
		}
		if (hashs.size() > 1) {
			for (String s : linkingMerkleTree(hashs)) {
				hashs.add(s);
			}
		}
		return hashs.get(hashs.size() - 1);
	}

	private static List<String> linkingMerkleTree(List<String> hashs) {
		List<String> hashss = new ArrayList<>();
		for (int i = 0; i < hashs.size();) {
			hashss.add(toSHA3_256(hashs.get(i++) + hashs.get(i++)));
		}
		if (hashss.size() > 1) {
			for (String s : linkingMerkleTree(hashss)) {
				hashss.add(s);
			}
		}
		return hashss;
	}

	// 머클트리 검증
	public static boolean verifyMerkleRoot(String merkleRoot, List<Transaction> txs) {
		return merkleRoot.compareTo(getMerkleRoot(txs, null)) == 0;
	}

	// 머클 패쓰 검증
	public static boolean verifyMerklePath() {
		return true;
	}

	// 기준 시간과 블록을 생성하는데 걸린 시간의 갭을 비교하여 이전 난이도를 기준으로 0.5 ~ 1.5배까지 난이도를 조정
	public static long getDifficulty(long previousDifficulty, long guideTime, long previousTime, long currentTime) {
		double difficulty = (double) (currentTime - previousTime) / guideTime;
		if (difficulty < 1.0 / 1.5) {
			return (long) (previousDifficulty * 1.5);
		}
		if (difficulty > 2) {
			long result = (long) (previousDifficulty / 2.0);
			return result > BcNode.difficulty ? result : BcNode.difficulty;
		}
		long result = (long) (previousDifficulty / difficulty);
		return result > BcNode.difficulty ? result : BcNode.difficulty;
	}

	// 난이도에 따른 채굴 검증
	public static boolean verifyMining(long difficulty, String hash) {
		String target = Long.toHexString(difficulty);
		int index = target.length() - 1;
		int maxDifficulty = Integer.parseInt(new String(new char[target.length()]).replace('\0', 'F'), 16);
		target = new String(new char[index]).replace('\0', '0')
				+ Integer.toHexString((int) Math.round((maxDifficulty - difficulty) / (maxDifficulty) / 16.0));
		return target.substring(0, index).equals(hash.substring(0, index))
				&& target.charAt(index) >= hash.charAt(index);
	}
}
