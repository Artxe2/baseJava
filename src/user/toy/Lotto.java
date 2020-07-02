package user.toy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Lotto extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1;// ?
	private JPanel jp_display = new JPanel();
	private JTextArea jta_display = new JTextArea();
	private JPanel jp_btns = new JPanel();
	private JButton jbtn_getNum = new JButton("번호 뽑기");
	private JButton jbtn_exit = new JButton("종료");
	private Random r = new Random();
	private int lots[] = new int[7];
	int lotsLength=lots.length;
	private String lotS[] = new String[lotsLength];
	private int digits;
	private int delay = 77;
	private int roof = 7;
	private int count;
	private ranNumThread rnT;
	private StringBuilder sb = new StringBuilder("");
	private boolean threadOn;
	private Lotto() {
		addListener();
		reset();
		refresh(0);
		initDisplay();
	}
	private class ranNumThread extends Thread{
		public void run() {
			try {
				for (int z = 0; z < 5; z++) {
					while (digits<lotsLength && threadOn){
						int DuplicateCheck;
						do {
							DuplicateCheck=0;
							lots[digits]=r.nextInt(45)+1;
							for (int i=0 ; i<=digits; i++) {
								if (lots[digits]==lots[i]) {
									DuplicateCheck++;
								}
							}
						} while (DuplicateCheck>1 && threadOn);
						refresh(z);
						count++;
						sleep(delay);
						if (count>=roof && threadOn) {	
							lotS[digits] += String.valueOf(lots[digits]);
							if (lotS[digits].length() == 1) {
								lotS[digits] = 0 + lotS[digits];
							}
							digits++;
							count=0;
							lotsArray();
						}
					}
					refresh(z);
					sbSet(z);
					reset();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			jta_display.append(" 입니다.");
			sb.setLength(0);
			threadOn = false;
			jbtn_getNum.setEnabled(true);
		}
	}
	private void lotsArray() {
		String temp;
		for (int i=0; i+1 < lotsLength; i++ ) {
			for (int j=0; j+1 < lotsLength; j++ ) {
				if (lotS[i].length() > 0 && lotS[j].length() > 0 && Integer.parseInt(lotS[i]) < Integer.parseInt(lotS[j]) ) {
					temp = lotS[i];
					lotS[i] = lotS[j];
					lotS[j] = temp;
				}
			}
		}
	}
	public void refresh(int z) {
		jta_display.setText(String.format("\n\t    첫 번째 숫자 : %2S \n"
				+ "\t    두 번째 숫자 : %2d \n"
				+ "\t    세 번째 숫자 : %2d \n"
				+ "\t    네 번째 숫자 : %2d \n"
				+ "\t다섯 번째 숫자 : %2d \n"
				+ "\t여섯 번째 숫자 : %2d \n"
				+ "\t     보너스 숫자 : %2d \n"
				, lots[0], lots[1], lots[2], lots[3], lots[4], lots[5], lots[6]) );
		jta_display.append(sb.toString());
		switch (z) {
		case 0:
			jta_display.append("\n              첫 번째");
			break;
		case 1:
			jta_display.append(" 입니다.\n              두 번째");
			break;
		case 2:
			jta_display.append(" 입니다.\n              세 번째");
			break;
		case 3:
			jta_display.append(" 입니다.\n              네 번째");
			break;
		case 4:
			jta_display.append(" 입니다.\n          다섯 번째");
			break;
		default:
			break;
		}
		jta_display.append(" 당첨번호는 "+lotS[0]+" "+lotS[1]+" "+lotS[2]+" "+lotS[3]+" "+lotS[4]+" "+lotS[5]+" + "+lotS[6]);
	}
	public void sbSet(int z) {
		switch (z) {
		case 0:
			sb.append("\n              첫 번째");
			break;
		case 1:
			sb.append(" 입니다.\n              두 번째");
			break;
		case 2:
			sb.append(" 입니다.\n              세 번째");
			break;
		case 3:
			sb.append(" 입니다.\n              네 번째");
			break;
		case 4:
			sb.append(" 입니다.\n          다섯 번째");
			break;
		default:
			break;
		}
		sb.append(" 당첨번호는 "+lotS[0]+" "+lotS[1]+" "+lotS[2]+" "+lotS[3]+" "+lotS[4]+" "+lotS[5]+" + "+lotS[6]);
	}
	public void reset() {
		for (int i = 0; i < lotsLength; i++) {
			lotS[i] = "";
		}
		for (int i = 0; i < lotsLength; i++) {
			lots[i] = 0;
		}
		digits=0;
	}
	public void addListener() {
		jbtn_getNum.addActionListener(this);
		jbtn_exit.addActionListener(this);
	}
	public void initDisplay() {
		this.setTitle("로또번호생성기 v1.0");
		this.add("Center", jp_display);
			jp_display.setLayout(new BorderLayout());
			jp_display.add(jta_display);
		this.add("South", jp_btns);
			jp_btns.setLayout(new FlowLayout(2));
			jp_btns.add(jbtn_getNum);
			jp_btns.add(jbtn_exit);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(350, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public void RanNumGen() {
		rnT = null;
		rnT = new ranNumThread();
		rnT.setDaemon(true);
		threadOn = true;
		rnT.start();
	}
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Lotto();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbtn_getNum) {
			jbtn_getNum.setEnabled(false);
			RanNumGen();
		}else if (e.getSource() == jbtn_exit) {
			System.exit(0);
		}
		
	}

}
