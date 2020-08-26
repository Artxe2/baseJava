public class Main {

	static java.util.List<Integer> list;

	static void num() {
		int a = 1;
		for (int i : list) {
			while (i > a) {
				a *= 10;
				list.add(i-- % a / (a / 10));
			}
		}
	}

	static void rec(int n) {
		int s = list.size();
		for (int i = 0; i < list.size(); i++) list.set(i, list.get(i) * n);
		for (int i = list.size(); i > 0; i--) {
			int t;
			if ((t = list.get(i - 1)) > 9) {
				list.set(i - 1, t % 10);
				list.set(i, t % 10 / 10);
			}
		}
		if (n > 1) rec(--n);
	}

	static String fac(int n) {
		list = new java.util.ArrayList<>();
		list.add(1);
		rec(n);
		StringBuilder r = new StringBuilder();
		java.util.Collections.reverse(list);
		for (int i : list) r.append(i);
		return r.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(fac(4));
	}
}