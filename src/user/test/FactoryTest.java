package user.test;

public class FactoryTest implements PerformTest {

	public FactoryTest() {
		start(10, 100000000);
	}

	@Override
	public void test1() {
		t1(new String("zxc"));
	}

	@Override
	public void test2() {
		t2(new String("zxc"));
	}

	@Override
	public void test3() {
		t3(new String("zxc"));
	}

	@Override
	public void test4() {

	}

	@Override
	public void test5() {

	}

	public String t1(String k) {
		String key = k;
		switch (key) {
			case "asd":
				return "asd";
			case "zxc":
				return "zxc";
			case "qwe":
				return "qwe";
			default:
				return "";
		}
	}

	public String t2(String k) {
		String key = k;
		if (key.equals("asd")) return "asd";
		if (key.equals("zxc")) return "zxc";
		if (key.equals("qwe")) return "qwe";
		return "";
	}

	public String t3(String k) {
		String key = k;
		if (key.compareTo("asd") == 0) return "asd";
		if (key.compareTo("zxc") == 0) return "zxc";
		if (key.compareTo("qwe") == 0) return "qwe";
		return "";
	}

	public static void main(String[] args) {
		new FactoryTest();
	}
}
