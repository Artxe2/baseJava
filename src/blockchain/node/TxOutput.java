package blockchain.node;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;

public class TxOutput implements Serializable {
	public String id;// 코인의 해시코드
	public PublicKey reciepient;// 코인 주인
	BigDecimal value;// 코인 갯수
	public String parentTxId;// 코인이 발행된 트랜잭션의 해시코드

	public TxOutput(PublicKey reciepient, BigDecimal value, String parentTxId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTxId = parentTxId;
		StringBuilder sb = new StringBuilder(HashUtil.getStringFromKey(reciepient));
		sb.append(value);
		sb.append(parentTxId);
		this.id = HashUtil.toSHA3_256(sb.toString());
	}

	// 해당 키가 이 트랜잭션 아웃풋의 주인인지 확인
	public boolean isMine(PublicKey key) {
		return (reciepient.equals(key));
	}
}
