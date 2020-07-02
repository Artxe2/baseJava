package user.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListTest implements PerformTest {

	public ListTest() {
		start(10, 200);
	}
	@Override
	public void test1() {
		Map<Integer,Integer> m = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			m.put(i, i);
		}
		int ms = m.size();
		for (int i = 0; i < ms; i++) {
			int j = m.get(i);
		}
	}
	@Override
	public void test2() {
		Map<Integer,Integer> m = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			m.put(i, i);
		}
		m.forEach((k, i) -> {int j = i;});
	}
	@Override
	public void test3() {
		List<Integer> l = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			l.add(i);
		}
		for (int i : l) {
			int j = i;
		}
	}
	@Override
	public void test4() {
		List<Integer> l = new LinkedList<>();
		for (int i = 0; i < 10000; i++) {
			l.add(i);
		}
		for (int i : l) {
			int j = i;
		}
	}
	@Override
	public void test5() {
		Map<Integer, Integer> m = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			m.put(i, i);
		}
		for (int i : m.keySet()) {
			int j = m.get(i);
		}
	}

	public static void main(String[] args) {
		new ListTest();
	}
}
