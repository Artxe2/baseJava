package user.toy;

import java.util.ArrayList;
import java.util.List;

public class CalcLogic {

	double getText(String s) {
		char[] ca = s.toCharArray();
		List<String> l = new ArrayList<>();
		List<Integer> m = new ArrayList<>();
		String st = "";
		for (char c : ca) {
			if(c == 46 || (c >= 48 && c <= 57) ) {
				st = st + c;
			} else {
				if (!st.equals("")) {
					l.add(st);
					m.add(0);
				}
				st = "";
				l.add(String.valueOf(c) );
				m.add(0);
			}
		}
		if (!st.equals("") ) {
			l.add(st);
			m.add(0);
		}

		int z = l.size();
		int f = 0;
		int n = 0;
		for (int i = 0; i < z; i++) {
			String op = l.get(i);
			f = f / 3 * 3;
			if(op.equals("+") || op.equals("-") ) {
				f++;
				m.set(i, f);
			} else if(op.equals("*") || op.equals("/") ) {
				f += 2;
				m.set(i, f);
			} else if(op.equals("(") ) {
				f += 3;
				l.remove(i);
				m.remove(i);
				i--;
				z--;
			} else if(op.equals("A") ) {
				f += 3;
			} else if(op.equals(")") ) {
				f -= 3;
				l.remove(i);
				m.remove(i);
				i--;
				z--;
			} else if(op.equals("a") ) {
				f -= 3;
			}
			if (f > n) {
				n = f;
			}
		}

		return calculating(l, m, n);
	}
	double calculating(List<String> l, List<Integer> m, int n){
		int z = l.size();
		for (int i = 0; i < z; ) {
			if (m.get(i) == n) {
				String s = l.get(i);
				double a = Double.parseDouble(l.get(i - 1) );
				double b = Double.parseDouble(l.get(i + 1) );
				if (s.equals("+") ) {
					l.set(i, a + b + "");
				} else if (s.equals("-") ) {
					l.set(i, a - b + "");
				} else if (s.equals("*") ) {
					l.set(i, a * b + "");
				} else if (s.equals("/") ) {
					l.set(i, a / b + "");
				}
				m.set(i, 0);
				m.remove(i + 1);
				l.remove(i + 1);
				z--;
				m.remove(i - 1);
				l.remove(i - 1);
				i--;
				z--;
			} else {
				i++;
			}

			for (String x : l) {
				System.out.print(x + " ");
			}
			System.out.println();

			try {
				if (l.get(i-1).equals("A") ) {
					System.out.println(l.size() );
					if (l.get(i + 2).equals("a") ) {
						l.remove(i + 2);
						z--;
						l.remove(i);
						z--;
						l.set(i, Math.abs(Double.parseDouble(l.get(i) ) ) + "");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (l.size() == 1) {
			return Double.parseDouble(l.get(0) );
		} else {
			return calculating(l, m, n - 1);
		}
	}

	public static void main(String[] args) {
		CalcLogic mc = new CalcLogic();
//		String s = "2+4*8+(2+4*4)/2";
//		String s = "0-1-1-1-1";
		String s = "1+A0-1-1-1-1a";
		System.out.println(mc.getText(s) );
	}
}
