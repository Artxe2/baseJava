package user.test;

public class ParamTest implements PerformTest {

	public ParamTest() {
		start(10, 100);
	}
	@Override
	public void test1() {
		param1("bye");
	}
	public void param1(String i) {
		String a = "Hello";
		int n = 0;
		while (n < 100) {
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			a += i;
			n++;
		}
	}
	@Override
	public void test2() {
		param2("bye");
	}
	public void param2(String i) {
		String j = i;
		String a = "Hello";
		int n = 0;
		while (n < 100) {
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			n++;
		}
	}
	@Override
	public void test3() {
		String j = "bye";
		String a = "Hello";
		int n = 0;
		while (n < 100) {
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			a += j;
			n++;
		}
	}
	@Override
	public void test4() {

	}
	@Override
	public void test5() {

	}

	public static void main(String[] args) {
		new ParamTest();
	}
}
