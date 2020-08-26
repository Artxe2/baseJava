
public class Test {
	public static void main(String[] args) {
		int i = 1000;
		int j = 1500;
		String s = Integer.toHexString((int) Math.round(16.0 / j * i) - 1);
		System.out.println(s);
	}
}
