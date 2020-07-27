package blockchain.node;

import java.io.Serializable;
import java.security.PublicKey;

public class KeyName implements Serializable {
	private PublicKey myKey;
	private String name;

	public KeyName() {

	}

	public KeyName(PublicKey myKey, String name) {
		this.myKey = myKey;
		this.name = name;
	}

	public PublicKey getMyKey() {
		return myKey;
	}

	public void setMyKey(PublicKey myKey) {
		this.myKey = myKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
