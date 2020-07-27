package blockchain.node;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChainUtil implements Serializable {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ChainUtil.class);
	protected static ObjectOutputStream oos;

	public static Transaction sendFunds(PublicKey sender, PublicKey recipient, BigDecimal value) {
		logger.info("sendFunds(sender, recipient, value: " + value + ")");
		// 본인에게 보내려고 할 경우
		if (recipient.equals(sender)) {
			logger.error("본인에게는 송금할 수 없습니다. 거래가 취소되었습니다.");
			return null;
		}
		List<String> inputs = ChainUtil.getInputsForTx(sender, value);
		if (inputs == null) {
			logger.error("거래에 필요한 금액을 보유하고있지 않습니다. 거래가 취소되었습니다.");
			return null;
		}
		Transaction newTx = new Transaction(sender, recipient, value, inputs);// 인풋 리스트로 수취인에게 코인을 보낸다는 트랜잭션을 생성
		return newTx;
	}

	public static List<String> getInputsForTx(PublicKey sender, BigDecimal value) {
		logger.info("getInputsForTx(sender, value: " + value + ")");
		Map<String, TxOutput> oMap = BcNode.uTxOs.get(sender);
		if (oMap != null) {
			List<String> inputs = new ArrayList<>();
			BigDecimal total = new BigDecimal(0);
			TxOutput o;
			for (Map.Entry<String, TxOutput> e : oMap.entrySet()) {
				inputs.add(e.getValue().id);
				total = total.add(e.getValue().value);
				if (total.floatValue() >= value.floatValue()) {
					return inputs;
				}
			}
		}
		return null;
	}

	// 보내는 이의 개인키로 싸인한 데이터가 맞는지 검증
	public static boolean verifySignature(Transaction tx) {
		logger.info("verifySignature(tx)");
		// 유효한 싸인이 아닐 경우
		if (!HashUtil.verifyECDSASig(tx.sender, tx.txId, tx.signature)) {
			logger.error("거래의 싸인이 유효하지 않습니다.");
			return false;
		}
		return true;
	}

	// 거래에 사용되는 코인의 양이 충분한지 검증
	public static boolean verifyBalance(Transaction tx) {
		logger.info("verifyBalance(tx.value: " + tx.value + ")");
		// 입력값이 거래값보다 적을 경우
		if (measureOutputsValue(tx.sender, tx.inputs).compareTo(tx.value) < 0) {
			logger.error("보유한 금액이 거래에 필요한 금액보다 적습니다." + tx.value);
			return false;
		}
		// 거래금액이 설정된 최소거래값보다 적을 경우
		if (tx.value.compareTo(BcNode.minTx) < 0) {
			logger.error("거래 금액이 최소 거래단위보다 적습니다.");
			return false;
		}
		return true;
	}

	// 트랜잭션을 블록에 추가하기 전 utxos의 코인이 충분한지 검증
	public static BigDecimal measureOutputsValue(PublicKey sender, List<String> outputs) {
		logger.info("measureOutputsValue(sender, outputs.size: " + (outputs == null ? "null" : outputs.size()) + ")");
		Map<String, TxOutput> oMap = BcNode.uTxOs.get(sender);
		BigDecimal total = new BigDecimal("0");
		if (oMap != null) {
			TxOutput o;
			if (outputs != null) {
				for (String id : outputs) {
					if ((o = oMap.get(id)) != null) {
						total = total.add(new BigDecimal(String.valueOf(o.value)));
					}
				}
			} else {
				// 보유중인 거래 가능한 코인 합계 검색
				for (Map.Entry<String, TxOutput> e : oMap.entrySet()) {
					total = total.add(new BigDecimal(String.valueOf(e.getValue().value)));
				}
			}
		} else {
			logger.info("oMap == null");
		}
		return total;
	}

	// 블록의 트랜잭션들을 실행하기 전 utxis의 코인이 충분한지 검증
	public static BigDecimal measureInputsValue(PublicKey sender, List<String> inputs) {
		logger.info("measureInputsValue(sender, inputs.size: " + (inputs == null ? "null" : inputs.size()) + ")");
		Map<String, TxOutput> iMap = BcNode.uTxIs.get(sender);
		BigDecimal total = new BigDecimal("0");
		if (iMap != null) {
			TxOutput o;
			if (inputs != null) {
				for (String i : inputs) {
					if ((o = iMap.get(i)) != null) {
						total = total.add(new BigDecimal(String.valueOf(o.value)));
					}
				}
			} else {
				// 보유중인 거래 예정인 코인 합계 검색
				for (Map.Entry<String, TxOutput> e : iMap.entrySet()) {
					total = total.add(new BigDecimal(String.valueOf(e.getValue().value)));
				}
			}
		} else {
			logger.info("iMap == null");
		}
		return total;
	}

	// atxos에서 주어진 코인목록의 값을 합을 반환
	public static BigDecimal measureTxOsValues(List<String> txOIds) {
		logger.info("measureTxOsValues(txOIds.size(): " + txOIds.size() + ")");
		BigDecimal total = new BigDecimal("0");
		for (String id : txOIds) {
			total.add(new BigDecimal(String.valueOf(BcNode.aTxOs.get(id).value)));
		}
		return total;
	}

	// 체인의 utxos에 txo를 추가
	public static void putUTxOs(TxOutput o) {
		logger.info("putUTxOs(" + o + ")");
		Map<String, TxOutput> oMap = BcNode.uTxOs.get(o.reciepient);
		if (oMap == null) {
			oMap = new HashMap<>();
			BcNode.uTxOs.put(o.reciepient, oMap);
		}
		oMap.put(o.id, o);
		BcNode.aTxOs.put(o.id, o);

	}

	// 거래예정으로 묶인 txo를 utxos에서 제거하고 utxis에 추가
	public static void putUTxIs(TxOutput o) {
		logger.info("putUTxIs(" + o + ")");
		Map<String, TxOutput> iMap = BcNode.uTxIs.get(o.reciepient);
		if (iMap == null) {
			iMap = new HashMap<>();
			BcNode.uTxIs.put(o.reciepient, iMap);
		}
		// utxos에 해당하는 코인이 있을 경우 제거.
		BcNode.uTxOs.get(o.reciepient).remove(o.id);
		iMap.put(o.id, o);
	}

	// 채굴 보상 트랜잭션 생성
	public static Transaction getReward(PublicKey miner) {
		return new Transaction(BcNode.god.publicKey, miner, BcNode.rewardValue, new ArrayList<>());
	}

	public static boolean isTransactionValid(Transaction tx) {
		logger.info("isTransactionValid(tx.value: " + (tx == null ? "null" : tx.value) + ")");
		if (tx == null) {
			logger.error("유효하지 않은 거래입니다.");
			return false;
		}
		// 이미 체인에 연결된 트랜잭션일 경우
		if (BcNode.processedTxs.containsKey(tx.txId)) {
			logger.error("이미 체인에 기록된 거래입니다.");
			BcNode.unprocessedTxs.remove(tx.txId);// 이미 처리된 트랜잭션은 미처리 목록에서 삭제
			return false;
		}
		// 보내는 놈이 승인한 트랜잭션이 아닐 경우
		if (!verifySignature(tx)) {
			logger.error("거래의 싸인이 유효하지 않습니다.");
			return false;
		}
		// 보내는 놈이 거래에 충분한 코인을 보유하고있지 않을 경우
		if (!verifyBalance(tx)) {
			logger.error("트랜잭션에 충분한 금액을 보유하고 있지 않습니다.");
			return false;
		}
		// 중복 거래 방지를 위해 거래에 사용되는 코인들을 utxis에 저장
		for (String id : tx.inputs) {
			putUTxIs(BcNode.aTxOs.get(id));
		}
		// 유효한 트랜잭션을 트랜잭션 대기열에 추가.
		BcNode.unprocessedTxs.put(tx.txId, tx);

		BcNode.jta.append(tx.senderName + "(이)가 " + tx.recipientName + "에게 " + tx.value + "코인을 전송\n");

		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(BcNode.tx_list_path))));
			oos.writeObject(BcNode.unprocessedTxs);
		} catch (Exception e) {
			logger.error("oos: " + oos);
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BcNode.reFreshBalance();
		return true;
	}

	// 블록에 트랜잭션 추가
	// aTxIs에 input txo 추가
	public static boolean addTransaction(Block b, Transaction tx) {
		logger.info("addTransaction(" + b.chainCount + ", tx.value: " + tx.value + ")");
		// 이미 채굴된 블록일 경우
		if (b.miner != null) {
			logger.error("이미 채굴된 블록에는 트랜잭션을 추가할 수 없습니다");
			return false;
		}
		if (BcNode.processedTxs.containsKey(tx.txId)) {
			BcNode.unprocessedTxs.remove(tx.txId);
			logger.info("이미 처리된 트랜잭션입니다.");
			return false;
		}
		b.txs.add(tx);// 블록에 트랜잭션 추가
		return true;
	}

	// 트랜잭션 처리. loadBlock시 isLoad = true;
	// isLoad시 utxIs에 input txo 추가 (or addTransaction에서 추가)
	// uTxOs에 output txo 추가
	// aTxOs에 output txo 추가
	// uTxIs에서 input txo 제거
	public static boolean processTransaction(Block b, Transaction tx, boolean isLoad) {
		//		BcNode.jta.append("new prssTx: " + tx + "\n");
		logger.info("processTransaction(" + b.chainCount + ", tx.value: " + tx.value + ", isLoad: " + isLoad + ")");
		if (b.miner == null) {
			logger.info("miner == null");
			return false;
		}
		// loadBlock시 addTransaction을 하지 않으므로 inputs들을 aTxIs에 준비
		if (isLoad) {
			for (int i = 0; i < tx.inputs.size(); i++) {
				TxOutput o = BcNode.aTxOs.get(tx.inputs.get(i));
				try {
					// input으로 지정된 코인이 유효한 코인이 아닐 경우 에러
					if (o == null) {
						logger.info("(o == null)");
						throw new NullPointerException();
					}
					putUTxIs(o);
				} catch (Exception e) {
					e.printStackTrace();
					// 에러 발생시 uTxIs에 넣었던 input들을 다시 uTxOs로 되돌린다.
					for (i--; i >= 0; i--) {
						o = BcNode.aTxOs.remove(tx.inputs.get(i));
						putUTxOs(o);
					}
					return false;
				}
			}
			// inputs의 값이 충분하지 않을 경우
			if (!tx.sender.equals(BcNode.god.publicKey)
					&& measureInputsValue(tx.sender, tx.inputs).compareTo(tx.value) < 0) {
				logger.info(
						"(!tx.sender.equals(BcNode.god.publicKey) && measureInputsValue(tx.sender, tx.inputs) < tx.value)");
				logger.info("sender: " + BcNode.keyName.get(tx.sender));
				logger.info(measureInputsValue(tx.sender, tx.inputs) + " < " + tx.value);
				return false;
			}
		}
		tx.outputs.clear();// 아웃풋 전부 삭제(재 생성할 예정)
		BigDecimal leftOver = new BigDecimal(0);
		if (!tx.sender.equals(BcNode.god.publicKey)) {
			leftOver = new BigDecimal(String.valueOf(measureInputsValue(tx.sender, tx.inputs)))
					.subtract(new BigDecimal(String.valueOf(tx.value)));
		}
		// 새로 생성된 txo들을 utxos에 저장
		TxOutput o;
		BigDecimal fees = new BigDecimal(String.valueOf(tx.value))
				.multiply(new BigDecimal(String.valueOf(BcNode.fees)));
		BigDecimal value = new BigDecimal(String.valueOf(tx.value)).subtract(new BigDecimal(String.valueOf(fees)));
		o = new TxOutput(tx.recipient, tx.value.subtract(fees), tx.txId);// 받는 사람에게 주어지는 몫
		putUTxOs(o);
		tx.outputs.add(o.id);
		logger.info("거래금액: " + o.value + "코인");
		o = new TxOutput(b.miner, fees, tx.txId);// 채굴한 사람에게 주어지는 수수료
		putUTxOs(o);
		tx.outputs.add(o.id);
		logger.info("수수료: " + o.value + "코인");
		if (leftOver.compareTo(new BigDecimal(0)) > 0) {
			o = new TxOutput(tx.sender, leftOver, tx.txId);// 주는 사람이 돌려받는 잔액
			putUTxOs(o);
			tx.outputs.add(o.id);
			logger.info("잔액: " + o.value + "코인");
		}
		// 거래에 사용된 txo들을 전부 삭제
		for (String i : tx.inputs) {
			try {
				BcNode.uTxIs.get(tx.sender).remove(i);
				logger.info("psTx...remove: " + i);
			} catch (Exception e) {
				// System.out.println("debug #ChainUtil#inputs.get(i) = null");
			}
		}
		// 미 처리 목록에서 트랜잭션을 제거하고 처리된 트랜잭션 목록에 추가
		BcNode.unprocessedTxs.remove(tx.txId);
		BcNode.processedTxs.put(tx.txId, tx);
		return true;
	}

	public static void rollbackTransaction(Transaction tx) {
		logger.info("rollbackTransaction(tx.value: " + tx.value + ")");
		// 잘못 생성된 txo들 전부 삭제
		for (String id : tx.outputs) {
			TxOutput o = BcNode.aTxOs.remove(id);
			if (BcNode.uTxOs.get(o.reciepient) != null) {
				BcNode.uTxOs.get(o.reciepient).remove(id);
			}
			if (BcNode.uTxIs.get(o.reciepient) != null) {
				BcNode.uTxIs.get(o.reciepient).remove(id);
			}
			//			BcNode.aTxOs.remove(id);
		}
		// 잘못 삭제된 txo들 전부 생성
		for (String id : tx.inputs) {
			putUTxOs(BcNode.aTxOs.get(id));
		}
		// 미 처리 거래목록에 추가
		BcNode.unprocessedTxs.put(tx.txId, tx);
	}

	public static int showMeTheMoney7(PublicKey myKey) {
		logger.info("showMeTheMoney7(myKey)");
		BigDecimal outs = ChainUtil.measureOutputsValue(myKey, null).add(ChainUtil.measureInputsValue(myKey, null));
		for (Map.Entry<String, Transaction> e : BcNode.unprocessedTxs.entrySet()) {
			outs.subtract(e.getValue().value);
		}
		return outs.multiply(new BigDecimal(10000)).intValue();
	}

	public static String measureWallet(PublicKey owner) {
		logger.info("measureWallet(owner)");
		BigDecimal outs = ChainUtil.measureOutputsValue(owner, null);
		return outs + "(" + outs.add(ChainUtil.measureInputsValue(owner, null)) + ")";
	}
}
