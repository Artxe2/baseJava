package user.nio.tcp;

import java.nio.ByteBuffer;

public class ClientThWork implements Runnable {

	private ClientTh clientTh = null;

	ClientThWork(ClientTh clientTh) {
		this.clientTh = clientTh;
	}

	@Override
	public void run() {
		boolean isLoop = true;
		ByteBuffer bb = null;
		while (isLoop) {
			if (!clientTh.responses.isEmpty()) {
				try {
					clientTh.analysisResponse(clientTh.responses.poll());
				} catch (Exception e) {
					isLoop = false;
					clientTh.stopLoop();
					e.printStackTrace();
				} finally {
				}
			} else {
				try {
					Thread.sleep(0, 1);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}

		}
	}
}
