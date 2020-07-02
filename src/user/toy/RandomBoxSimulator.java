package user.toy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import user.toy.RandomVO.rdBox_Origin;

public class RandomBoxSimulator implements ActionListener, ItemListener{
	{JFrame.setDefaultLookAndFeelDecorated(true);}
	private int SS_stack;
	private int S_stack;
	private int A_stack;
	private int B_stack;
	private int C_stack;
	private int SS_max;
	private int S_max;
	private int A_max;
	private int B_max;
	private int C_max;
	public int SS_count2;
	public int S_count2;
	public int A_count2;
	public int B_count2;
	public int C_count2;
	private int SS_max2;
	private int S_max2;
	private int A_max2;
	private int B_max2;
	private int C_max2;
	/***************************/
	public double SS_pro;
	public double S_pro;
	public double A_pro;
	public double B_pro;
	public double C_pro;
	public double SS_table[];
	public double S_table[];
	public double A_table[];
	public double B_table[];
	public double C_table[];
	public int SS_count;
	public int S_count;
	public int A_count;
	public int B_count;
	public int C_count;
	public int SS_over;
	public int S_over;
	public int A_over;
	public int B_over;
	public int C_over;
	public double SS_now;
	public double S_now;
	public double A_now;
	public double B_now;
	public double C_now;
	/***************************/
	private AdaptiveProCalc apc = AdaptiveProCalc.getInstance();
	private Random random = new Random();
	private double ranNum = 0.0;
	private RandomVO rvo = RandomVO.getRvo();
	private rdBox_Origin o = null;
	private rdBox_Origin[] ro = rvo.ro;
	private String[] setList = {"rdBox 0 -SS 0.5%-","rdBox 1 -SS 1%-","rdBox 2 -SS 1.5%-","rdBox 3 -SS 3%-","rdBox 4 -SS 15%-","rdBox 4 -SS 20%-"};
	private String[] setList2 = {"?","?","?"};
	private String box = setList[0];
	private static final RandomBoxSimulator INSTANCE = new RandomBoxSimulator();
	private int hundred = 10000000;
	/***************************/
	private JFrame jFrame_rdbxSim = new JFrame();
	private 	JPanel jPanel_frame = new JPanel();
	private 		JPanel jPanel_boxT = new JPanel();
	private 			JTextArea jTextArea_display = new JTextArea();
	private 			JPanel jPanel_boundary = new JPanel();
	private 		JPanel jPanel_boxB = new JPanel();
	private   			JPanel jPanel_box = new JPanel();
	private   				JPanel jPanel_boxCenter = new JPanel();
	private   					JTextArea jTextArea_box = new JTextArea();
	private   					JComboBox<String> jcbBox_list = new JComboBox<>(setList);
	private   				JPanel jPanel_boxEast = new JPanel();
	private   					JButton jButton_box = new JButton("DrawingLots");
	private   					JButton jButton_boxx = new JButton(String.format("%,d", hundred) + " Lots");
	private   			JPanel jPanel_box2 = new JPanel();
	private   				JPanel jPanel_boxCenter2 = new JPanel();
	private   					JTextArea jTextArea_box2 = new JTextArea();
	private   					JComboBox<String> jcbBox_list2 = new JComboBox<>(setList2);
	private   				JPanel jPanel_boxEast2 = new JPanel();
	private   					JButton jButton_box2 = new JButton("?");
	private   					JButton jButton_boxx2 = new JButton("?");
	private 			JPanel jPanel_box3 = new JPanel();
	private 				JButton jButton_reset = new JButton("reset");
	private 				JButton jButton_exit = new JButton("Exit");

	private RandomBoxSimulator() {
		addListener();
		proCalc();
		changeField();
		reset();
		initDisplay();
	}
	public static RandomBoxSimulator getInstance() {
		return INSTANCE;
	}
	private void hundredLots() {
		for (int i = 0; i < hundred; i++) {
			drawingLots();
		}
	}
	private void setMax2() {
		if (SS_pro > ranNum) {
			SS_count2 = 0;
			S_count2++;
			A_count2++;
			B_count2++;
			C_count2++;
		} else if (SS_pro + S_pro > ranNum) {
			SS_count2++;
			S_count2 = 0;
			A_count2++;
			B_count2++;
			C_count2++;
		} else if (SS_pro + S_pro + A_pro > ranNum) {
			SS_count2++;
			S_count2++;
			A_count2 = 0;
			B_count2++;
			C_count2++;
		} else if (SS_pro + S_pro + A_pro + B_pro > ranNum) {
			SS_count2++;
			S_count2++;
			A_count2++;
			B_count2 = 0;
			C_count2++;
		} else if (SS_pro + S_pro + A_pro + B_pro + C_pro > ranNum) {
			SS_count2++;
			S_count2++;
			A_count2++;
			B_count2++;
			C_count2 = 0;
		}
		if (SS_count2 >= SS_max2) {
			SS_max2 = SS_count2 + 1;
		}
		if (S_count2 >= S_max2) {
			S_max2 = S_count2 + 1;
		}
		if (A_count2 >= A_max2) {
			A_max2 = A_count2 + 1;
		}
		if (B_count2 >= B_max2) {
			B_max2 = B_count2 + 1;
		}
		if (C_count2 >= C_max2) {
			C_max2 = C_count2 + 1;
		}
	}
 	private void drawingLots() {
		ranNum = random.nextInt(100) + random.nextDouble();
		if (SS_now > ranNum) {
			SS_count = 0;
			if (S_count + 2 < S_table.length) {
				S_count++;
			} else {
				S_over++;
			}
			if (A_count + 2 < A_table.length) {
				A_count++;
			} else {
				A_over++;
			}
			if (B_count + 2 < B_table.length) {
				B_count++;
			} else {
				B_over++;
			}
			if (C_count + 2 < C_table.length) {
				C_count++;
			} else {
				C_over++;
			}
			SS_stack++;
		} else if (SS_now + S_now > ranNum){
			if (SS_count + 2 < SS_table.length) {
				SS_count++;
			} else {
				SS_over++;
			}
			S_count = 0;
			if (A_count + 2 < A_table.length) {
				A_count++;
			} else {
				A_over++;
			}
			if (B_count + 2 < B_table.length) {
				B_count++;
			} else {
				B_over++;
			}
			if (C_count + 2 < C_table.length) {
				C_count++;
			} else {
				C_over++;
			}
			S_stack++;
		} else if (SS_now + S_now + A_now > ranNum ) {
			if (SS_count + 2 < SS_table.length) {
				SS_count++;
			} else {
				SS_over++;
			}
			if (S_count + 2 < S_table.length) {
				S_count++;
			} else {
				S_over++;
			}
			A_count = 0;
			if (B_count + 2 < B_table.length) {
				B_count++;
			} else {
				B_over++;
			}
			if (C_count + 2 < C_table.length) {
				C_count++;
			} else {
				C_over++;
			}
			A_stack++;
		} else if (SS_now + S_now + A_now + B_now > ranNum) {
			if (SS_count + 2 < SS_table.length) {
				SS_count++;
			} else {
				SS_over++;
			}
			if (S_count + 2 < S_table.length) {
				S_count++;
			} else {
				S_over++;
			}
			if (A_count + 2 < A_table.length) {
				A_count++;
			} else {
				A_over++;
			}
			B_count = 0;
			if (C_count + 2 < C_table.length) {
				C_count++;
			} else {
				C_over++;
			}
			B_stack++;
		} else if (SS_now + S_now + A_now + B_now + C_now > ranNum) {
			if (SS_count + 2 < SS_table.length) {
				SS_count++;
			} else {
				SS_over++;
			}
			if (S_count + 2 < S_table.length) {
				S_count++;
			} else {
				S_over++;
			}
			if (A_count + 2 < A_table.length) {
				A_count++;
			} else {
				A_over++;
			}
			if (B_count + 2 < B_table.length) {
				B_count++;
			} else {
				B_over++;
			}
			C_count = 0;
			C_stack++;
		}
		while (SS_over > 0 && SS_count + 2 < SS_table.length) {
			SS_over--;
			SS_count++;
		}
		while (S_over > 0 && S_count + 2 < S_table.length) {
			S_over--;
			S_count++;
		}
		while (A_over > 0 && A_count + 2 < A_table.length) {
			A_over--;
			A_count++;
		}
		while (B_over > 0 && B_count + 2 < B_table.length) {
			B_over--;
			B_count++;
		}
		while (C_over > 0 && C_count + 2 < C_table.length) {
			C_over--;
			C_count++;
		}
		setNowNMax();
		setMax2();
	}
 	private void proTableSync(String box) {
		if(box.equals(setList[0])) {
			if (o != ro[0]) {
				o = ro[0];
				changeField();
				setNowNMax();
			}
		} else if (box.equals(setList[1])) {
			if (o != ro[1]) {
				o = ro[1];
				changeField();
				setNowNMax();
			}
		} else if (box.equals(setList[2])) {
			if (o != ro[2]) {
				o = ro[2];
				changeField();
				setNowNMax();
			}
		} else if (box.equals(setList[3])) {
			if (o != ro[3]) {
				o = ro[3];
				changeField();
				setNowNMax();
			}
		} else if (box.equals(setList[4])) {
			if (o != ro[4]) {
				o = ro[4];
				changeField();
				setNowNMax();
			}
		} else if (box.equals(setList[5])) {
			if (o != ro[5]) {
				o = ro[5];
				changeField();
				setNowNMax();
			}
		}
 	}
 	private void changeField() {
		SS_pro = o.SS_pro;
		S_pro = o.S_pro;
		A_pro = o.A_pro;
		B_pro = o.B_pro;
		C_pro = o.C_pro;
		SS_table = o.SS_table;
		S_table = o.S_table;
		A_table = o.A_table;
		B_table = o.B_table;
		C_table = o.C_table;
		SS_count = o.SS_count;
		S_count = o.S_count;
		A_count = o.A_count;
		B_count = o.B_count;
		C_count = o.C_count;
		SS_over = o.SS_over;
		S_over = o.S_over;
		A_over = o.A_over;
		B_over = o.B_over;
		C_over = o.C_over;
		SS_now = o.SS_now;
		S_now = o.S_now;
		A_now = o.A_now;
		B_now = o.B_now;
		C_now = o.C_now;
 	}
	private void reset() {
		SS_count = 0;
		S_count = 0;
		A_count = 0;
		B_count = 0;
		C_count = 0;
		SS_over = 0;
		S_over = 0;
		A_over = 0;
		B_over = 0;
		C_over = 0;
		SS_stack = 0;
  		S_stack = 0;
  		A_stack = 0;
  		B_stack = 0;
  		C_stack = 0;
  		SS_max = 0;
  		S_max = 0;
  		A_max = 0;
  		B_max = 0;
  		C_max = 0;
		SS_count2 = 0;
		S_count2 = 0;
		A_count2 = 0;
		B_count2 = 0;
		C_count2 = 0;
  		SS_max2 = 0;
  		S_max2 = 0;
  		A_max2 = 0;
  		B_max2 = 0;
  		C_max2 = 0;
  		SS_now = SS_table[SS_count + 1];
  		S_now = S_table[S_count + 1] * (100 - SS_now) / (S_table[S_count + 1] + A_table[A_count + 1] + B_table[B_count + 1] + C_table[C_count + 1]);
  		A_now = A_table[A_count + 1] * (100 - SS_now - S_now) / (A_table[A_count + 1] + B_table[B_count + 1] + C_table[C_count + 1]);
  		B_now = B_table[B_count + 1] * (100 - SS_now - S_now - A_now) / (B_table[B_count + 1] + C_table[C_count + 1]);
  		C_now = C_table[C_count + 1] * (100 - SS_now - S_now - A_now - B_now) / C_table[C_count + 1];
		reFresh();
	}
	private void setNowNMax() {
		SS_now = SS_table[SS_count + 1];
		S_now = S_table[S_count + 1] * (100 - SS_now) / (S_table[S_count + 1] + A_table[A_count + 1] + B_table[B_count + 1] + C_table[C_count + 1]);
		A_now = A_table[A_count + 1] * (100 - SS_now - S_now) / (A_table[A_count + 1] + B_table[B_count + 1] + C_table[C_count + 1]);
		B_now = B_table[B_count + 1] * (100 - SS_now - S_now - A_now) / (B_table[B_count + 1] + C_table[C_count + 1]);
		C_now = C_table[C_count + 1] * (100 - SS_now - S_now - A_now - B_now) / C_table[C_count + 1];
		if (SS_count + SS_over >= SS_max) {
			SS_max = SS_count + SS_over + 1;
		}
		if (S_count + S_over >= S_max) {
			S_max = S_count + S_over + 1;
		}
		if (A_count + A_over >= A_max) {
			A_max = A_count + A_over + 1;
		}
		if (B_count + B_over >= B_max) {
			B_max = B_count + B_over + 1;
		}
		if (C_count + C_over >= C_max) {
			C_max = C_count + C_over + 1;
		}
	}
	private void reFresh() {
		jTextArea_display.setText(
				String.format("\tTotal : " + "%,d ea", (SS_stack + S_stack + A_stack + B_stack + C_stack) ) +
				String.format("\n\t SS(%3.1f%%) : " + "%,d ea    /    max : (" + SS_max + " / " + SS_max2 + ")    /    %3.3f%%", SS_pro, SS_stack, SS_now) +
				String.format("\n\t S(%3.1f%%) : " + "%,d ea    /    max : (" + S_max + " / " + S_max2 + ")    /    %3.3f%%", S_pro, S_stack, S_now) +
				String.format("\n\t A(%3.1f%%) : " + "%,d ea    /    max : (" + A_max + " / " + A_max2 + ")    /    %3.3f%%", A_pro, A_stack, A_now) +
				String.format("\n\t B(%3.1f%%) : " + "%,d ea    /    max : (" + B_max + " / " + B_max2 + ")    /    %3.3f%%", B_pro, B_stack, B_now) +
				String.format("\n\t C(%3.1f%%) : " + "%,d ea    /    max : (" + C_max + " / " + C_max2 + ")    /    %3.3f%%", C_pro, C_stack, C_now) +
				"\n");
		jTextArea_box.setText(String.format("SS : %3.1f%% S : %3.1f%% A : %3.1f%% B : %3.1f%% C : %3.1f%%", SS_pro, S_pro, A_pro, B_pro, C_pro));
	}
	private void proCalc() {
		for (rdBox_Origin i : ro) {
			o = i;
			if (o.SS_pro != 0) {
				o.SS_table = apc.exProCalc(o.SS_pro);
			} else {
				o.SS_table = new double[2];
			}
			if (o.S_pro != 0) {
				o.S_table = apc.exProCalc(o.S_pro);
			} else {
				o.S_table = new double[2];
			}
			if (o.A_pro != 0) {
				o.A_table = apc.exProCalc(o.A_pro);
			} else {
				o.A_table = new double[2];
			}
			if (o.B_pro != 0) {
				o.B_table = apc.exProCalc(o.B_pro);
			} else {
				o.B_table = new double[2];
			}
			if (o.C_pro != 0) {
				o.C_table = apc.exProCalc(o.C_pro);
			} else {
				o.C_table = new double[2];
			}
		}
		o = ro[0];
	}
	private void addListener() {
		jcbBox_list.addItemListener(this);
		jcbBox_list2.addItemListener(this);
		jButton_boxx.addActionListener(this);
		jButton_boxx2.addActionListener(this);
		jButton_box.addActionListener(this);
		jButton_box2.addActionListener(this);
		jButton_reset.addActionListener(this);
		jButton_exit.addActionListener(this);
	}
	private void initDisplay() {
		jFrame_rdbxSim.setDefaultCloseOperation(jFrame_rdbxSim.EXIT_ON_CLOSE);
		jFrame_rdbxSim.setTitle("RandomBoxSimulator 2");
		jFrame_rdbxSim.setSize(500,300);
		jFrame_rdbxSim.setResizable(false);
		jFrame_rdbxSim.setLocationRelativeTo(null);
			jFrame_rdbxSim.add(jPanel_frame);
				jPanel_frame.setLayout(new GridLayout(2, 1));
				jPanel_frame.add(jPanel_boxT);
					jPanel_boxT.setLayout(new BorderLayout());
					jPanel_boxT.add(jTextArea_display);
						jTextArea_display.setEditable(false);
					jPanel_boxT.add("South",jPanel_boundary);
						jPanel_boundary.setBackground(new Color(75, 125, 200));
				jPanel_frame.add(jPanel_boxB);
					jPanel_boxB.setLayout(new GridLayout(3,1));
					jPanel_boxB.add(jPanel_box);
						jPanel_box.setLayout(new BorderLayout());
						jPanel_box.add("Center",jPanel_boxCenter);
							jPanel_boxCenter.setLayout(new BorderLayout());
							jPanel_boxCenter.add("Center",jTextArea_box);
							jTextArea_box.setEditable(false);
							jPanel_boxCenter.add("South",jcbBox_list);
						jPanel_box.add("East",jPanel_boxEast);
							jPanel_boxEast.setLayout(new GridLayout(2,1));
							jPanel_boxEast.add(jButton_box);
							jPanel_boxEast.add(jButton_boxx);
					jPanel_boxB.add(jPanel_box2);
						jPanel_box2.setLayout(new BorderLayout());
						jPanel_box2.add("Center",jPanel_boxCenter2);
							jPanel_boxCenter2.setLayout(new BorderLayout());
							jPanel_boxCenter2.setBackground(Color.GRAY);
							jPanel_boxCenter2.add("Center",jTextArea_box2);
							jTextArea_box2.setEditable(false);
							jPanel_boxCenter2.add("South",jcbBox_list2);
						jPanel_box2.add("East",jPanel_boxEast2);
							jPanel_boxEast2.setLayout(new GridLayout(2,1));
							jPanel_boxEast2.add(jButton_box2);
							jPanel_boxEast2.add(jButton_boxx2);
					jPanel_boxB.add(jPanel_box3);
						jPanel_box3.setLayout(new FlowLayout(2));
						jPanel_box3.add(jButton_reset);
						jPanel_box3.add(jButton_exit);
		jFrame_rdbxSim.setVisible(true);
	}

	public static void main(String[] args) {
		RandomBoxSimulator start = RandomBoxSimulator.INSTANCE;
		System.out.println(INSTANCE);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object ao = ae.getSource();
		if (ao == jButton_boxx) {
			proTableSync(box);
			hundredLots();
			reFresh();
		} else if (ao == jButton_boxx2) {

		} else if (ao == jButton_box) {
			proTableSync(box);
			drawingLots();
			reFresh();
		} else if (ao == jButton_box2) {

		} else	if (ao == jButton_reset) {
			reset();
		} else
		if(ao == jButton_exit) {
			System.exit(0);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object io = ie.getSource();
		if (io == jcbBox_list) {
			box = ie.getItem().toString();
			proTableSync(box);
			reset();
		} else if (io == jcbBox_list2) {

		}
	}
}
