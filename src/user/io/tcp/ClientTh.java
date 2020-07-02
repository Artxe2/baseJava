package user.io.tcp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;

public class ClientTh implements Runnable {

	private static Client client = null;
	private static ObjectOutputStream cOOS = null;
	private static ObjectInputStream cOIS = null;

	protected ClientTh(Client client) {
		this.client = client;
		this.cOOS = client.cOOS;
		try {
			cOIS = new ObjectInputStream(client.csTCP.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public void run() {
		try {
			boolean isLoop = true;
			System.out.println("ClientTh Start");
			while (isLoop) {
				try {
					String response = (String) cOIS.readObject();// Blocking Point
					analysisResponse(response);
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
				if (cOIS != null) cOIS.close();
				if (cOOS != null) cOOS.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			System.out.println("ClientTh End");
		}
	}

	private void analysisResponse(String response) {
		StringTokenizer st = new StringTokenizer(response, PVO.Sharp);
		if (st.hasMoreTokens()) {
			String protocol = st.nextToken();
			if (protocol.compareTo(PVO.MESSAGE) == 0) {
				message(st);
			} else if (protocol.compareTo(PVO.LOG_IN) == 0) {
				logIn(st);
			} else if (protocol.compareTo(PVO.LOG_OUT) == 0) {
				logOut(st);
			} else {
				System.out.println("Undefined Protocol...\"" + protocol + "\"");
			}
		} else {
			System.out.println("Not Included  Protocol...\"" + response + "\"");
		}
	}

	private void message(StringTokenizer st) {
		client.showMessage(st.nextToken() + ": " + st.nextToken());
	}

	private void logIn(StringTokenizer st) {
		String userName = st.nextToken();
		client.showMessage(userName + st.nextToken());
		client.userList.add(userName);
		client.refreshUserList();
	}

	private void logOut(StringTokenizer st) {
		String userName = st.nextToken();
		client.showMessage(userName + st.nextToken());
		client.userList.remove(userName);
		client.refreshUserList();
	}
}
