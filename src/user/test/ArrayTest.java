package user.test;

public class ArrayTest implements PerformTest {

	public ArrayTest() {
		start(10, 100000);
	}
	@Override
	public void test1() {
		int[] a = new int[100];
		for (int i = 0; i < 100;) {
			if (a[50] == 0) {
				i++;
			}
		}
	}
	int[] a;
	@Override
	public void test2() {
		a = new int[100];
		int b = a[50];
		for (int i = 0; i < 100;) {
			if (b == 0) {
				i++;
			}
		}
	}
	@Override
	public void test3() {
		int[] a = new int[100];
		int b = a[50];
		for (int i = 0; i < 100;) {
			if (b == 0) {
				i++;
			}
		}
	}
	@Override
	public void test4() {

	}
	@Override
	public void test5() {

	}

	public static void main(String[] args) {
		new ArrayTest();
	}
}
