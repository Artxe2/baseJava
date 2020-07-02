package user.io.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static final int SERVER_PORT = 4000;
	private static String sIP = "localhost";
	private static ServerSocket ssTCP = null;
	protected static Socket hsTCP = null;
	protected static List<ServerTh> aList = new ArrayList<>();// All <Users>

	private Server() {
		serverStart();
	}

	public static void main(String[] args) {
		new Server();
	}

	private void serverStart() {
		try {
			ssTCP = new ServerSocket();
			ssTCP.bind(new InetSocketAddress(sIP, SERVER_PORT));
			System.out.println("IO TCP Server Start, Port: " + SERVER_PORT);
			while (true) {
				hsTCP = ssTCP.accept();// Blocking Point
				new Thread(new ServerTh(this)).start();
				System.out.println("Connected by: " + hsTCP.getInetAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ssTCP != null) ssTCP.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Server End");
		}
	}
}