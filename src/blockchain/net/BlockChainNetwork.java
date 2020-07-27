package blockchain.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BlockChainNetwork {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BlockChainNetwork.class);
	private static final int SERVER_PORT = 9999;
	private ServerSocket ssTCP = null;
	protected Socket hsTCP = null;
	protected static final List<BcNetThread> tList = new ArrayList<>();// All <Users>\
	protected static final JTextArea jta = new JTextArea();
	private static final JScrollPane jsp = new JScrollPane(jta);

	private BlockChainNetwork() {
		serverStart();
	}

	public static BlockChainNetwork getInstance() {
		return LazyHolder.instance;
	}

	private static class LazyHolder {
		private static final BlockChainNetwork instance = new BlockChainNetwork();
	}

	public static void main(String[] args) {
		new BlockChainNetwork();
	}

	private void serverStart() {
		try {
			System.out.print("Input server ip: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String ip = br.readLine();
			ssTCP = new ServerSocket();
			ssTCP.bind(new InetSocketAddress(ip, SERVER_PORT));
			logger.info("BlockChainNetwork Start, Port: " + SERVER_PORT);
			while (true) {
				hsTCP = ssTCP.accept();// Blocking Point
				new Thread(new BcNetThread(hsTCP)).start();
				logger.info("Connected by: " + hsTCP.getInetAddress() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ssTCP != null)
					ssTCP.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("Server End");
		}
	}
}