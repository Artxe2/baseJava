package blockchain.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class BcNetThread implements Runnable {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BcNetThread.class);
	public static final int P2P_PORT = 11111;

	private Socket hsTCP = null;
	protected ObjectOutputStream sOos = null;
	protected ObjectInputStream sOis = null;

	protected BcNetThread(Socket hsTCP) {
		this.hsTCP = hsTCP;
	}

	@Override
	public void run() {
		BlockChainNetwork.tList.add(this);
		try {
			sOos = new ObjectOutputStream(hsTCP.getOutputStream());
			sOis = new ObjectInputStream(hsTCP.getInputStream());

			// 자신의 주소를 얻기 위해 자신의 아이피를 전송
			String myAddr = hsTCP.getInetAddress().getHostAddress();
			sOos.writeObject(myAddr);
			sOos.flush();

			// 네트워크에 접속시 네트워크에 접속되어있는 모든 노드들에게 자신의 ip 전송
			logger.info("myAddr: " + myAddr);
			broadCast(BlockChainNetwork.tList, myAddr);
			boolean isLoop = true;
			logger.info("BcNetThread Start");
			while (isLoop) {
				try {
					// 아무 일도 일어나지 않는다.
					String request = (String) sOis.readObject();
					System.out.println("절대 오지 않는 메시지: " + request);
				} catch (Exception e) {
					isLoop = false;
					e.printStackTrace();
				} finally {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			System.out.println("remove: " + BlockChainNetwork.tList.remove(this));
			logger.info(hsTCP.getInetAddress() + " has disconnected\n");

			try {
				if (sOis != null)
					sOis.close();
				if (sOos != null)
					sOos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			logger.info("BcNetTh End");
		}
	}

	private void broadCast(List<BcNetThread> list, String msg) {
		logger.info("broadCast to: " + list.size());
		try {
			for (BcNetThread t : list) {
				if (!this.equals(t)) {
					t.sOos.writeObject(msg);
					t.sOos.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
