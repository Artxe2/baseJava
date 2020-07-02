package user.test;

public class LoopTest implements PerformTest {

	public LoopTest() {
		start(10, 100000);
	}
	@Override
	public void test1() {
		String[] sa = new String[1000];
		for (int i = 0; i < sa.length; i++) {
			sa[i] = "Hello";
		}
	}
	@Override
	public void test2() {
		String[] sa = new String[1000];
		int sal = sa.length;
		for (int i = 0; i < sal; i++) {
			sa[i] = "Hello";
		}
	}
	@Override
	public void test3() {
		String[] sa = new String[1000];
		for (String s : sa) {
			//s = "Hello";
		}
	}
	@Override
	public void test4() {
		String[] sa = new String[1000];
		int i = 0;
		while (i < sa.length) {
			sa[i] = "Hello";
			i++;
		}
	}
	@Override
	public void test5() {
		String[] sa = new String[1000];
		int i = 0;
		int sal = sa.length;
		while (i < sal) {
			sa[i] = "Hello";
			i++;
		}
	}

	public static void main(String[] args) {
		new LoopTest();
	}
}
