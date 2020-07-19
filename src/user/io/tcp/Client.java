package user.io.tcp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
	protected static Socket csTCP = new Socket();
	protected static ObjectOutputStream cOOS = null;
	private static StringBuilder msg = new StringBuilder();
	protected static String userName = "";
	protected static List<String> userList = new ArrayList<>();
	private static JTextArea jta = new JTextArea();
	private static JScrollPane jsp = new JScrollPane(jta);
	private static JPanel jpS = new JPanel();
	private static JTextField jtf = new JTextField();
	private static JComboBox<String> jcb = new JComboBox<>(new String[] {
			"A(to Me)", "B(to All)"
	});
	protected static JComboBox<String> jcb2 = new JComboBox<>(userList.toArray(new String[userList.size()]));

	private Client() {
		clientStart();
	}

	public static void main(String[] args) {
		new Client();
	}

	private void clientStart() {
		try {
			csTCP.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));// Time Out Point
			System.out.println("Io Tcp Client Start, Port: " + SERVER_PORT);
			cOOS = new ObjectOutputStream(new BufferedOutputStream(csTCP.getOutputStream()));
			initDisplay();
			addListener();
			new Thread(new ClientTh(this)).start();
		} catch (Exception e) {
			try {
				csTCP.close();
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
		setTitle("IO TCP Client");
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
		setTitle("IO TCP Client - " + userName);
	}

	protected void showMessage(String msg) {
		jta.append(msg + "\n");
		jsp.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}

	protected void sendMessage(String input) {
		if (input.compareTo("") != 0) {
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
			try {
				cOOS.writeObject(msg.toString());
				cOOS.flush();
				msg.setLength(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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
		userList.add(name);
		refreshUserList();
		try {
			msg.append(Pvo.LOG_IN);
			msg.append(Pvo.Sharp);
			msg.append(name);
			cOOS.writeObject(msg.toString());
			cOOS.flush();
			msg.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	protected void doLogOut() {
		try {
			cOOS.writeObject(Pvo.LOG_OUT);
			cOOS.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
