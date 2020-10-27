package user.util;

public class Quicksort {
    public Quicksort() {
        int length = 100000;
        long start;
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        printArray(array);
        start = System.nanoTime();
        sort(array, 0, array.length - 1);
        System.out.println((System.nanoTime() - start) / 1000000.0 + "ms");
        printArray(array);
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        printArray(array);
        start = System.nanoTime();
        java.util.Arrays.sort(array);
        System.out.println((System.nanoTime() - start) / 1000000.0 + "ms");
        printArray(array);
    }

    void printArray(int[] array) {
        int length = array.length;
        System.out.print("{ ");
        if (length < 25) {
            for (int i = 0; i < length - 1; i++) {
                System.out.print(array[i] + ", ");
            }
        } else {
            for (int i = 0; i < 10 - 1; i++) {
                System.out.print(array[i] + ", ");
            }
            System.out.print(array[10] + " ... ");
            for (int i = length - 10; i < length - 1; i++) {
                System.out.print(array[i] + ", ");
            }
        }
        System.out.print(array[length - 1] + " }\n");
    }

    /*
     * custom sort
     */
    void sort(int[] array, int start, int end) {
        if (start < end) {
            int pivot = array[start];
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
            sort(array, start, left - 1);
            sort(array, left + 1, end);
        }
    }

    boolean verifyOrder(int a, int b) {
        if (a > b) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        new Quicksort();
    }
}
