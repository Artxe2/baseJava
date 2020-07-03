package user.test;

public class EqualsTest2 implements PerformTest {

	private class A {
		public String a = "Hel";
		public String a() {
			return "Hel";
		}
	}
	A a = new A();
	public EqualsTest2() {
		start(10, 100000000);
	}
	String zxc = "zxc";
	@Override
	public void test1() {
		t1(zxc);
	}

	@Override
	public void test2() {
		t2(zxc);
	}

	@Override
	public void test3() {
		t3(zxc);
	}

	@Override
	public void test4() {
	}

	@Override
	public void test5() {

	}

	public void t1(String k) {
//		String key = k;
		int a;
		switch (k) {
			case "asd":ifAction(); break;
			case "zxc":ifAction(); break;
			case "qwe":ifAction(); break;
			default:
				ifAction();
		}
	}

	public void t2(String k) {
//		String key = k;
		int a;
		if ("asd".equals(k)) {
			ifAction();
		}
		if ("zxc".equals(k)) {
			ifAction();
		}
		if ("qwe".equals(k)) {
			ifAction();
		}
		ifAction();
	}

	public void t3(String k) {
//		String key = k;
		int a;
		if ("asd".compareTo(k) == 0) {
			ifAction();
		}
		if ("zxc".compareTo(k) == 0) {
			ifAction();
		}
		if ("qwe".compareTo(k) == 0) {
			ifAction();
		}
		ifAction();
	}

	public void ifAction() {

	}

	public static void main(String[] args) {
		new EqualsTest2();
	}
}
