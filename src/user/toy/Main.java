package user.toy;

public class Main {


	public class A {
		public void m1() {
			System.out.println("m1");
		}
		public void m2() {
			System.out.println("m2");
		}
		public void m3() {
			System.out.println("m3");
		}
		public void m4() {
			System.out.println("m4");
		}
	}

	public class B extends A {
		@Override
		public void m4() {
			// TODO Auto-generated method stub
			System.out.println("Im B");
			fly();
		}
		public void fly() {
			System.out.println("I can't fly...");
		}
	}

	Main() {
		A a = new B();
		a.m1();
		a.m2();
		a.m3();
		a.m4();
	}

	public static void main(String[] args) {
		new Main();
	}
}