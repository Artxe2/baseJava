package blockchain.node;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BcNodeThread implements Runnable {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BcNodeThread.class);
	private ObjectOutputStream cOos;
	private ObjectInputStream cOis;
	private BcNode node;
	private Socket csTCP;

	protected BcNodeThread(BcNode node, Socket csTCP) {
		this.node = node;
		this.csTCP = csTCP;
	}

	@Override
	public void run() {
		try {
			cOos = new ObjectOutputStream(csTCP.getOutputStream());
			cOis = new ObjectInputStream(csTCP.getInputStream());
			// p2p네트워크를 위한 자신의 서버소켓을 생성
			final String myIp = (String) cOis.readObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						node.openPeer(myIp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			boolean isLoop = true;
			logger.info("BcNodeThread Start");
			while (isLoop) {
				try {
					// 새로 네트워크에 접속한 노드에게 ip를 전달받아서
					String response = (String) cOis.readObject();
					logger.info("response: " + response);
					Socket p2pTCP = new Socket();
					// 대상의 서버소켓에 접속
					p2pTCP.connect(new InetSocketAddress(response, BcNode.P2P_PORT));
					new Thread(new BcNodeP2PThread(node, p2pTCP)).start();
					BcNode.jta.append("P2P Accepted by: " + p2pTCP.getInetAddress() + "\n");
				} catch (Exception e) {
					isLoop = false;
					e.printStackTrace();
				} finally {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cOis != null)
					cOis.close();
				if (cOos != null)
					cOos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			logger.info("ClientTh End");
		}
	}
}
