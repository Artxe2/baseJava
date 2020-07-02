package user.test;

import java.util.StringTokenizer;

public class StringSplitTest implements PerformTest {
	String t = "";
	int r = 1000;
	int i = 0;
	public StringSplitTest() {
		while(i < r) {
			t = t + "#" +  i;
			i++;
		}
		i = 0;
		start(10, 10);
	}
	@Override
	public void test1() {
		while(i < r) {
			StringTokenizer st = new StringTokenizer(t,"#");
			while(st.hasMoreTokens() ) {
				String S = st.nextToken();
			}
			i++;
		}
		i = 0;
	}
	@Override
	public void test2() {
		while(i < r) {
			String[] sa = t.split("#");
			for (String s : sa) {
				String S = s;
			}
			i++;
		}
		i = 0;
	}
	@Override
	public void test3() {
		while(i < r) {
			String[]sa = tokenize(t, '#');
			for (String s : sa) {
				String S = s;
			}
			i++;
		}
		i = 0;
	}
	@Override
	public void test4() {

	}
	@Override
	public void test5() {

	}
    public String[] tokenize(String string, char delimiter) {
        String[] temp = new String[(string.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = string.indexOf(delimiter);

        while( j >= 0) {
            temp[wordCount++] = string.substring(i, j);
            i = j + 1;
            j = string.indexOf(delimiter, i);
        }

        temp[wordCount++] = string.substring(i);
        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);
        return result;
    }

	public static void main(String[] args) {
		new StringSplitTest();
	}
}
