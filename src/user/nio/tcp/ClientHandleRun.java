package user.nio.tcp;

public class ClientHandleRun implements Runnable {

	private ClientTh clientTh = null;

	ClientHandleRun(ClientTh clientTh) {
		this.clientTh = clientTh;
	}

	@Override
	public void run() {
		boolean isLoop = true;
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
