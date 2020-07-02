package user.test;

public class StringExtendTest2 implements PerformTest {

	public StringExtendTest2() {
		start(10, 1000000);
	}
	class C {
		static final String b = "qwe";
		String getB() {
			return "qwe";
		}
	}
	C A = new C();
	static final String a = "asdf";
	static final String[] z = {"abc", "def", "ghi", "jkl", "mmo"};
	String b = "asdf";
	@Override
	public void test1() {
		String s = a + C.b + a + C.b;
	}
	@Override
	public void test2() {
		String s = z[0] + z[1] + z[2] + z[3];
	}
	@Override
	public void test3() {
		String s = a + a + a + a;
	}
	@Override
	public void test4() {
		String s = A.getB() + A.getB() + A.getB() + A.getB();
	}
	@Override
	public void test5() {
		String s = getA(16) + A.b + getA(16) + A.b;
	}

	public String getA2() {
		return "asdf";
	}

	public String getA(int i) {
		switch (i) {
		case 0: return "asdf";
		case 1: return "asdfa";
		case 2: return "asdfb";
		case 3: return "asdfc";
		case 4: return "asdfd";
		case 5: return "asdfd";
		case 6: return "asdfd";
		case 7: return "asdfd";
		case 8: return "asdfd";
		case 9: return "asdfd";
		case 10: return "asdfd";
		case 11: return "asdfd";
		case 12: return "asdfd";
		case 13: return "asdfd";
		case 14: return "asdfd";
		case 15: return "asdfd";
		case 16: return "asdf";

		default: return "-1";
		}
	}

	public static void main(String[] args) {
		new StringExtendTest2();
	}
}
