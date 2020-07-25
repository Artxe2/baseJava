public class Main {

	public Main() {
		int num = 9;
		for (int i = 1; i <= 9; i++) {
			System.out.println(num + " x " + i + " = " + (i * num));
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}