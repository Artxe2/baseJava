class Solution {
    int answer, dLength, wLength, verify, start;

    int solution(int n, int[] weak, int[] dist) {
        dLength = dist.length;
        wLength = weak.length;
        if (wLength == 1) {
            return 1;
        }
        answer = dLength + 1;
        verify = dLength;
        weak = java.util.Arrays.copyOf(weak, wLength * 2);
        for (; start < wLength; start++) {
            weak[start + wLength] = weak[start] + n;
        }
        permutation(weak, dist, new boolean[dLength], new int[dLength], 0);
        return answer > dLength ? -1 : answer;
    }

    void verifyCheck(int[] weak, int[] dist) {
        int index = 0, check = 0, end;
        while (index < verify) {
            end = weak[start + check] + dist[index++];
            while (weak[start + check] <= end) {
                check++;
                if (check == wLength) {
                    if (answer > index) {
                        answer = index;
                        verify = answer - 1;
                    }
                    return;
                }
            }
        }
    }

    void permutation(int[] weak, int[] dist, boolean[] used, int[] array, int index) {
        if (index < verify) {
            int next = index + 1;
            for (int i = 0; i < dLength; i++) {
                if (!used[i]) {
                    array[index] = dist[i];
                    used[i] = true;
                    permutation(weak, dist, used, array, next);
                    used[i] = false;
                    if (index >= verify) {
                        return;
                    }
                }
            }
        } else {
            for (start = 0; start < wLength; start++) {
                verifyCheck(weak, array);
            }
        }
    }
}