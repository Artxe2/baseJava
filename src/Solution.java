import com.google.gson.Gson;

class Solution {
	public int solution(int[] m) {
		int f = m.length;
		int[][] d = { new int[f], new int[f], new int[f] };
		d[0][0] = m[0];
		d[1][1] = m[0];
		d[2][2] = m[0];
		return 0;
	}

	public static void main(String[] args) {
		Solution s = new Solution();
		int r = s.solution(new Gson().fromJson("[1, 2, 3, 1]", int[].class));
		System.out.println("\n" + new Gson().toJson(r));
	}
}