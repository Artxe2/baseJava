package user.nio.tcp;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerHandleRun implements Runnable {

	private Server server = null;

	ServerHandleRun(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		boolean isLoop = true;
		while (isLoop) {
			SocketChannel hscTCP = null;
			ByteBuffer bb = null;
			synchronized (server) {
				if (!server.requests.isEmpty()) {
					hscTCP = server.requesters.poll();
					bb = server.requests.poll();
				} else {
					try {
						Thread.sleep(0, 1);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
					}
				}
			}
			if (bb != null) {
				server.analysisRequest(hscTCP, bb);
			}
		}
	}
}
