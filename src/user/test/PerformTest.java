package user.test;

public interface PerformTest {
	public void test1();
	public void test2();
	public void test3();
	public void test4();
	public void test5();
	default void start(int inn, int jnn) {
		int in = inn;
		int jn = jnn;
		long start = System.currentTimeMillis();
			try { Thread.sleep(400); } catch (Exception e) {}
		start = System.currentTimeMillis();
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < jn; j++) {
				test1();
			}
			System.out.print(i);
		}
		System.out.println("   Test1 End");
		long test1 = System.currentTimeMillis() - start;
			try { Thread.sleep(400); } catch (Exception e) {}
		start = System.currentTimeMillis();
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < jn; j++) {
				test2();
			}
			System.out.print(i);
		}
		System.out.println("   Test2 End");
		long test2 = System.currentTimeMillis() - start;
			try { Thread.sleep(400); } catch (Exception e) {}
		start = System.currentTimeMillis();
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < jn; j++) {
				test3();
			}
			System.out.print(i);
		}
		System.out.println("   Test3 End");
		long test3 = System.currentTimeMillis() - start;
			try { Thread.sleep(400); } catch (Exception e) {}
		start = System.currentTimeMillis();
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < jn; j++) {
				test4();
			}
			System.out.print(i);
		}
		System.out.println("   Test4 End");
		long test4 = System.currentTimeMillis() - start;
			try { Thread.sleep(400); } catch (Exception e) {}
		start = System.currentTimeMillis();
		for (int i = 0; i < in; i++) {
			for (int j = 0; j < jn; j++) {
				test5();
			}
			System.out.print(i);
		}
		System.out.println("   Test5 End");
		long test5 = System.currentTimeMillis() - start;
		System.out.println("test 1 : " + test1 + "ms\n" + "test 2 : " + test2 + "ms\n" + "test 3 : " + test3 + "ms\n" + "test 4 : " + test4 + "ms\n" + "test 5 : " + test5 + "ms\n");
	}
}
