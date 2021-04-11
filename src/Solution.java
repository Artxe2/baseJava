class Solution {
    public String solution(String[] participant, String[] completion) {
        int length = completion.length;
        quickSort(participant, 0, length);
        quickSort(completion, 0, length - 1);
        int i = 0;
        for (; i < length; i++) {
            if (!participant[i].equals(completion[i])) {
                return participant[i];
            }
        }
        return participant[i];
    }

    void quickSort(String[] array, int start, int end) {
        if (start < end) {
            String pivot = array[start];
            int left = start, right = end + 1;
            if (start - end > 1) {
                int mid = (start + end) / 2;
                pivot = array[mid];
                array[mid] = array[start];
            }
            while (left < right) {
                while (left < right && verifyOrder(pivot, array[--right]));
                if (left < right) {
                    array[left] = array[right];
                }
                while (left < right && verifyOrder(array[++left], pivot));
                if (left < right) {
                    array[right] = array[left];
                }
            }
            array[right] = pivot;
            quickSort(array, start, left - 1);
            quickSort(array, left + 1, end);
        }
    }

    void redixSort() {

    }

    boolean verifyOrder(String a, String b) {
        return a.compareTo(b) <= 0;
    }
}