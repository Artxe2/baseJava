package user.io.tcp;

import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class ServerTh implements Runnable {

	private Socket hsTCP = null;
	private List<ServerTh> aList = null;// All <Users>
	private ObjectOutputStream sOOS = null;
	private ObjectInputStream sOIS = null;
	private String userName = "";

	protected ServerTh(Server server) {
		this.hsTCP = server.hsTCP;
		this.aList = server.aList;
	}

	@Override
	public void run() {
		try {
			sOOS = new ObjectOutputStream(new BufferedOutputStream(hsTCP.getOutputStream()));
			sOIS = new ObjectInputStream(hsTCP.getInputStream());
			boolean isLoop = true;
			System.out.println("ServerTh Start");
			while (isLoop) {
				try {
					String request = (String) sOIS.readObject();// Blocking Point
					//					long start = System.nanoTime();//
					isLoop = analysisRequest(request);
					//					long end = System.nanoTime();//
					//					System.out.println((end - start) / 1000000.0 + "ms - read");//
				} catch (Exception e) {
					isLoop = false;
				} finally {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			logOut();

			try {
				if (sOIS != null) sOIS.close();
				if (sOOS != null) sOOS.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			System.out.println("ServerTh End");
		}
	}

	private boolean analysisRequest(String request) {
		StringTokenizer st = new StringTokenizer(request, PVO.Sharp);
		if (st.hasMoreTokens()) {
			String protocol = st.nextToken();
			if (protocol.compareTo(PVO.A) == 0) {
				methodA(st);
			} else if (protocol.compareTo(PVO.B) == 0) {
				methodB(st);
			} else if (protocol.compareTo(PVO.LOG_IN) == 0) {
				logIn(st);
			} else if (protocol.compareTo(PVO.LOG_OUT) == 0) {
				return false;
			} else {
				System.out.println("Undefined Protocol...\"" + protocol + "\"");
			}
		} else {
			System.out.println("Not Included  Protocol...\"" + request + "\"");
		}
		return true;
	}

	private void broadCast(List<ServerTh> list, String msg) {
		try {
			for (ServerTh a : list) {
				a.sOOS.writeObject(msg);
				a.sOOS.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void broadCast(String msg) {
		try {
			sOOS.writeObject(msg);
			sOOS.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void logIn(StringTokenizer st) {
		userName = st.nextToken();
		for (ServerTh s : aList) {
			StringBuilder msg = new StringBuilder();
			msg.append(PVO.LOG_IN);
			msg.append(PVO.Sharp);
			msg.append(s.userName);
			msg.append(PVO.Sharp);
			msg.append(" - Welcome");
			broadCast(msg.toString());
		}
		StringBuilder msg = new StringBuilder();
		msg.append(PVO.LOG_IN);
		msg.append(PVO.Sharp);
		msg.append(userName);
		msg.append(PVO.Sharp);
		msg.append(" - Hi");
		broadCast(aList, msg.toString());
		aList.add(this);
	}

	private void logOut() {
		aList.remove(this);
		StringBuilder msg = new StringBuilder();
		msg.append(PVO.LOG_OUT);
		msg.append(PVO.Sharp);
		msg.append(userName);
		msg.append(PVO.Sharp);
		msg.append(" - Bye");
		broadCast(aList, msg.toString());
	}

	private void methodA(StringTokenizer st) {
		StringBuilder msg = new StringBuilder();
		msg.append(PVO.MESSAGE);
		while (st.hasMoreTokens()) {
			msg.append(PVO.Sharp);
			msg.append(st.nextElement());
		}
		broadCast(msg.toString());
	}

	private void methodB(StringTokenizer st) {
		StringBuilder msg = new StringBuilder();
		msg.append(PVO.MESSAGE);
		while (st.hasMoreTokens()) {
			msg.append(PVO.Sharp);
			msg.append(st.nextElement());
		}
		broadCast(aList, msg.toString());
	}
}
