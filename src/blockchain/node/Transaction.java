package blockchain.node;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String txId;// 이 트랜잭션의 해시코드
	public PublicKey sender;// 보내는 이
	public String senderName;
	public PublicKey recipient;// 받는 이
	public String recipientName;
	public BigDecimal value;// 거래되는 코인 값
	public byte[] signature;// 보내는 이의 싸인
	public List<String> inputs = new ArrayList<>();// 트랜잭션 생성시 입력된 TxOId들
	public List<String> outputs = new ArrayList<>();// 다음 트랜잭션에게 전해줄 기록들
	public long timeStamp;// 트랜잭션 생성 시간 (같은 내용의 트랜잭션이어도 다른 해시코드를 갖게 됨)

	public Transaction(PublicKey sender, PublicKey recipient, BigDecimal value, List<String> inputs) {
		timeStamp = System.currentTimeMillis();
		this.sender = sender;
		this.recipient = recipient;
		this.value = value;
		this.inputs = inputs;
		txId = calcHash();
	}

	// 트랜잭션이 갖고있는 데이터를 전부 합해서 해쉬코드 생성
	public String calcHash() {
		StringBuilder sb = new StringBuilder(String.valueOf(timeStamp));
		sb.append(HashUtil.getStringFromKey(sender));
		sb.append(HashUtil.getStringFromKey(recipient));
		sb.append(value);
		return HashUtil.toSHA3_256(sb.toString());
	}

	// 개인키로 이 트랜잭션의 데이터에 싸인
	public Transaction generateSignature(PrivateKey key) {
		signature = HashUtil.applyECDSASig(key, txId);
		return this;
	}
}
