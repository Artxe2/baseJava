package user.nio.tcp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private static final int R_R_AP = Runtime.getRuntime().availableProcessors();
	private static final int SERVER_PORT = 8000;
	private static final String sIP = "localhost";
	private static Selector selector = null;
	private static final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	private static final StringBuilder msg = new StringBuilder();
	private static final List<SocketChannel> aList = new ArrayList<>();// All <User>
	private static final List<String> nList = new ArrayList<>();// All <UserName>
	private static final Map<SocketChannel, String> aMap = new HashMap<>();// All <User, UserName>
	protected static final ByteBufferPool bbp = new ByteBufferPool(10000, 1024);
	protected static final Queue<ByteBuffer> requests = new LinkedList<>();
	protected static final Queue<SocketChannel> requesters = new LinkedList<>();
	private static final ExecutorService es = Executors.newFixedThreadPool(R_R_AP);

	private Server() {
		serverStart();
	}

	public static void main(String[] args) {
		new Server();
	}

	private void serverStart() {
		try {
			for (int i = 0; i < R_R_AP; i++) {
				es.submit(new ServerThWork(this));
			}
			selector = Selector.open();
			ServerSocketChannel sscTCP = ServerSocketChannel.open();
			sscTCP.configureBlocking(false);
			sscTCP.bind(new InetSocketAddress(sIP, SERVER_PORT));
			sscTCP.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("NIO TCP Server Start: " + sscTCP.socket().getLocalSocketAddress());
			boolean isLoop = true;
			while (isLoop) {
				selector.select();
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					if (key.isAcceptable()) {
						accept(key);
					} else if (key.isReadable()) {
						read(key);
					}
					keys.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			es.shutdown();
			System.out.println("Server End");
		}
	}

	private void accept(SelectionKey key) throws Exception {
		ServerSocketChannel sscTCP = (ServerSocketChannel) key.channel();
		SocketChannel hscTCP = sscTCP.accept();
		hscTCP.configureBlocking(false);
		hscTCP.register(selector, SelectionKey.OP_READ);
	}

	protected void read(SelectionKey key) {
		// long start = System.nanoTime();//
		SocketChannel hscTCP = null;
		try {
			hscTCP = (SocketChannel) key.channel();
			ByteBuffer bb = bbp.burrowBuffer();
			hscTCP.read(bb);
			bb.flip();
			requesters.offer(hscTCP);
			requests.offer(bb);
		} catch (Exception e) {
			key.cancel();
			logOut(hscTCP);
			try {
				hscTCP.close();
			} catch (Exception ex) {
				e.printStackTrace();
			} finally {
			}
			e.printStackTrace();
		} finally {
		}
	}

	protected void analysisRequest(SocketChannel hscTCP, ByteBuffer request) {
		StringTokenizer st = null;
		try {
			st = new StringTokenizer(decoder.decode(request).toString(), Pvo.Sharp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bbp.returnBuffer(request);
		}
		if (st.hasMoreTokens()) {
			String protocol = st.nextToken();
			if (protocol.compareTo(Pvo.A) == 0) {
				methodA(hscTCP, st);
			} else if (protocol.compareTo(Pvo.B) == 0) {
				methodB(st);
			} else if (protocol.compareTo(Pvo.LOG_IN) == 0) {
				logIn(hscTCP, st);
			} else if (protocol.compareTo(Pvo.LOG_OUT) == 0) {
				logOut(hscTCP);
			} else {
				System.out.println("Undefined Protocol...\"" + request + "\"");
			}
		} else {
			System.out.println("Not Included  Protocol...\"" + request + "\"");
		}
	}

	private void broadCast(SocketChannel hscTCP, String msg) {
		ByteBuffer bb = bbp.burrowBuffer();
		bb.put(msg.getBytes());
		bb.flip();
		try {
			hscTCP.write(bb);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bbp.returnBuffer(bb);
		}
	}

	private void broadCast(List<SocketChannel> list, String msg) {
		// ByteBuffer bb = ByteBuffer.allocateDirect(1024);
		ByteBuffer bb = bbp.burrowBuffer();
		bb.put(msg.getBytes());
		try {
			for (SocketChannel hscTCP : list) {
				bb.flip();
				hscTCP.write(bb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bbp.returnBuffer(bb);
		}
	}

	private void logIn(SocketChannel hscTCP, StringTokenizer st) {
		String userName = st.nextToken();
		for (String s : nList) {
			msg.append(Pvo.LOG_IN);
			msg.append(Pvo.Sharp);
			msg.append(s);
			msg.append(Pvo.Sharp);
			msg.append(" - Welcome");
			broadCast(hscTCP, msg.toString());
			msg.setLength(0);
			try {
				Thread.sleep(0, 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		nList.add(userName);
		msg.append(Pvo.LOG_IN);
		msg.append(Pvo.Sharp);
		msg.append(userName);
		msg.append(Pvo.Sharp);
		msg.append(" - Hi");
		broadCast(aList, msg.toString());
		msg.setLength(0);
		aList.add(hscTCP);
		aMap.put(hscTCP, userName);
	}

	protected void logOut(SocketChannel hscTCP) {
		aList.remove(hscTCP);
		msg.append(Pvo.LOG_OUT);
		msg.append(Pvo.Sharp);
		msg.append(aMap.get(hscTCP));
		aMap.remove(hscTCP);
		msg.append(Pvo.Sharp);
		msg.append(" - Bye");
		broadCast(aList, msg.toString());
		msg.setLength(0);
	}

	private void methodA(SocketChannel hscTCP, StringTokenizer st) {
		msg.append(Pvo.MESSAGE);
		while (st.hasMoreTokens()) {
			msg.append(Pvo.Sharp);
			msg.append(st.nextElement());
		}
		broadCast(hscTCP, msg.toString());
		msg.setLength(0);
	}

	private void methodB(StringTokenizer st) {
		msg.append(Pvo.MESSAGE);
		while (st.hasMoreTokens()) {
			msg.append(Pvo.Sharp);
			msg.append(st.nextElement());
		}
		broadCast(aList, msg.toString());
		msg.setLength(0);
	}
}