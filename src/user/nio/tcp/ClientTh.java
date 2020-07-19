package user.nio.tcp;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import javax.swing.JComboBox;

public class ClientTh implements Runnable {

	private static Client client = null;
	private static final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	protected static final Queue<ByteBuffer> responses = new LinkedList<>();
	//	protected static final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static boolean isLoop = true;

	protected ClientTh(Client client) {
		this.client = client;
	}

	protected void stopLoop() {
		isLoop = false;
	}

	@Override
	public void run() {
		try {
			new Thread(new ClientThWork(this)).start();
			System.out.println("ClientTh Start");
			while (isLoop) {
				try {
					client.selector.select();
					Iterator<SelectionKey> keys = client.selector.selectedKeys().iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						if (key.isReadable()) {
							read(key);
						}
						keys.remove();
					}
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
				if (client.selector != null) client.selector.close();
				if (client.cscTCP != null) client.cscTCP.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			System.out.println("ClientTh End");
		}
	}

	private void read(SelectionKey key) {
		SocketChannel cscTCP = (SocketChannel) key.channel();
		ByteBuffer bb = client.bbp.burrowBuffer();
		try {
			cscTCP.read(bb);
			bb.flip();
			responses.offer(bb);
		} catch (Exception e) {
			try {
				cscTCP.close();
			} catch (Exception ex) {
				e.printStackTrace();
			} finally {
			}
			e.printStackTrace();
		} finally {
		}
	}

	protected void analysisResponse(ByteBuffer response) {
		StringTokenizer st = null;
		try {
			st = new StringTokenizer(decoder.decode(response).toString(), Pvo.Sharp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.bbp.returnBuffer(response);
		}
		if (st.hasMoreTokens()) {
			String protocol = st.nextToken();
			if (protocol.compareTo(Pvo.MESSAGE) == 0) {
				message(st);
			} else if (protocol.compareTo(Pvo.LOG_IN) == 0) {
				logIn(st);
			} else if (protocol.compareTo(Pvo.LOG_OUT) == 0) {
				logOut(st);
			} else {
				System.out.println("Undefined Protocol...\"" + response + "\"");
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
		client.userList.add(userName);
		client.remove(client.jcb2);
		client.jcb2 = new JComboBox<>(client.userList.toArray(new String[client.userList.size()]));
		client.add("North", client.jcb2);
		client.setVisible(true);
		client.showMessage(userName + st.nextToken());
	}

	private void logOut(StringTokenizer st) {
		String userName = st.nextToken();
		client.userList.remove(userName);
		client.remove(client.jcb2);
		client.jcb2 = new JComboBox<>(client.userList.toArray(new String[client.userList.size()]));
		client.add("North", client.jcb2);
		client.setVisible(true);
		client.showMessage(userName + st.nextToken());
	}
}
