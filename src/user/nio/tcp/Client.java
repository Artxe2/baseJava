package user.nio.tcp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {
	private static final int SERVER_PORT = 4000;
	private static final String SERVER_IP = "localhost";
	protected static Selector selector = null;
	protected static SocketChannel cscTCP = null;
	private static final StringBuilder msg = new StringBuilder();
	protected static String userName = "";
	protected static final List<String> userList = new ArrayList<>();
	private static final JTextArea jta = new JTextArea();
	private static final JScrollPane jsp = new JScrollPane(jta);
	private static final JPanel jpS = new JPanel();
	private static final JTextField jtf = new JTextField();
	private static final JComboBox<String> jcb = new JComboBox<>(new String[] {
			"A(to Me)", "B(to All)"
	});
	protected static JComboBox<String> jcb2 = new JComboBox<>(userList.toArray(new String[userList.size()]));
	protected static final ByteBufferPool bbp = new ByteBufferPool(1000, 1024);

	private Client() {
		clientStart();
	}

	public static void main(String[] args) {
		new Client();
	}

	private void clientStart() {
		try {
			selector = Selector.open();
			cscTCP = SocketChannel.open(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			cscTCP.configureBlocking(false);
			cscTCP.register(selector, SelectionKey.OP_READ);
			System.out.println("Nio Tcp Client Start, Port: " + SERVER_PORT);
			initDisplay();
			addListener();
			new Thread(new ClientTh(this)).start();
		} catch (Exception e) {
			try {
				cscTCP.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
			}
			e.printStackTrace();
		} finally {
			System.out.println("Client End");
		}
	}

	private void addListener() {
		jtf.addActionListener(this);
	}

	private void initDisplay() {
		setTitle("Nio Tcp Client");
		setSize(600, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add("North", jcb2);
		add("Center", jsp);
		jta.setEditable(false);
		add("South", jpS);
		jpS.setLayout(new BorderLayout());
		jpS.add("West", jcb);
		jpS.add("Center", jtf);
		setVisible(true);
		while (userName.compareTo("") == 0) {
			userName = JOptionPane.showInputDialog("Input your name");
		}
		doLogIn(userName);
		setTitle("NIO TCP Client - " + userName);
	}

	protected void showMessage(String msg) {
		jta.append(msg + "\n");
		jsp.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}

	protected void sendMessage(String input) {
		if (input.compareTo("") != 0) {
			ByteBuffer bb = bbp.burrowBuffer();
			int s = jcb.getSelectedIndex();
			if (s == 0) {
				msg.append(Pvo.A);
			} else if (s == 1) {
				msg.append(Pvo.B);
			}
			msg.append(Pvo.Sharp);
			msg.append(userName);
			msg.append(Pvo.Sharp);
			msg.append(input);
			bb.put(msg.toString().getBytes());
			bb.flip();
			try {
				cscTCP.write(bb);
				msg.setLength(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bbp.returnBuffer(bb);
			}
		}
	}

	protected void refreshUserList() {
		remove(jcb2);
		jcb2 = new JComboBox<>(userList.toArray(new String[userList.size()]));
		add("North", jcb2);
		setVisible(true);
	}

	protected void doLogIn(String name) {
		ByteBuffer bb = bbp.burrowBuffer();
		userList.add(name);
		refreshUserList();
		msg.append(Pvo.LOG_IN);
		msg.append(Pvo.Sharp);
		msg.append(name);
		bb.put(msg.toString().getBytes());
		bb.flip();
		try {
			cscTCP.write(bb);
			msg.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bbp.returnBuffer(bb);
		}
	}

	protected void doLogOut() {
		ByteBuffer bb = bbp.burrowBuffer();
		bb.put(Pvo.LOG_OUT.getBytes());
		bb.flip();
		try {
			cscTCP.write(bb);
			msg.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bbp.returnBuffer(bb);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(jtf)) {
			sendMessage(jtf.getText());
			jtf.setText("");
		}
	}
}
