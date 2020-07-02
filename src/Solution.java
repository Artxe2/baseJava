import java.util.LinkedList;
import java.util.Queue;

class Solution {
	Solution(){
		int[] r = solution(new int[] {1,2,3,2,3});
		for (int i : r) {
			System.out.println(i);
		}
	}
    public int[] solution(int[] p) {
    	Queue<Integer> q = new LinkedList<>();
    	for (int i : p) {
    		q.offer(i);
    	}
        int[] r = new int[p.length];
        boolean[] b = new boolean[p.length];
        while(true) {
        	int x = q.poll();
        	if (q.isEmpty()) {
        		break;
        	} else {
        		q.offer(1);
        	}
        }
        for (int i = 0; i < p.length; i++) {
        	for (int j = 0; j < i; j++) {
        		if (!b[j]) {
        			r[j]++;
        			if (p[j] > p[i]) {
        				b[j] = true;
        			}
        		}
        	}
        }
        return r;
    }
    public static void main(String[] args) {
		new Solution();
	}
}