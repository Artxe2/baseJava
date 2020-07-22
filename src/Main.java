interface If {
	int asd(int a);
}

public class Main {

	Main() {

	}

	public static void main(String[] args) {
		If i = (a) -> { return a * 4; };
		int r = i.asd(4);
		System.out.println(r);
		new Thread(() ->  {System.out.println("run");}).start();
	}
}