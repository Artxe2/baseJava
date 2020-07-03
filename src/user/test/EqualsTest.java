package user.test;

public class EqualsTest implements PerformTest {

	private class A {
		public String a = "Hello";

		public String a() {
			return "Hello";
		}
	}

	A a = new A();
	String s = "Hello";

	public EqualsTest() {
		start(10, Integer.MAX_VALUE / 10);
	}

	@Override
	public void test1() {
	}

	@Override
	public void test2() {
	}

	@Override
	public void test3() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) if (a.a.contentEquals("good")) ifAction();
	}

	@Override
	public void test4() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) if (a.a.equals("good")) ifAction();
	}

	@Override
	public void test5() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) if (a.a.compareTo("good") == 0) ifAction();
	}

	public void ifAction() {

	}

	public static void main(String[] args) {
		new EqualsTest();
	}
}
