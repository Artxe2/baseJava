package blockchain.node;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Wallet implements Serializable {

	public PrivateKey privateKey = null;
	public PublicKey publicKey = null;

	public Map<String, Float> utxos = new HashMap<>();// 지갑에 속해있는 코인들

	public Wallet() {
		// Wallet 생성시 타원곡선 알고리즘을 통해 private key와 public key를 생성
		try {
			Security.addProvider(new BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			keyGen.initialize(new ECGenParameterSpec("prime192v1"), SecureRandom.getInstance("SHA1PRNG"));
			KeyPair kp = keyGen.generateKeyPair();
			privateKey = kp.getPrivate();
			publicKey = kp.getPublic();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
