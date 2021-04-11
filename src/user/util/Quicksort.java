package user.util;

public class Quicksort {
    int[][] bucket = new int['z'][];
    int[] sequence = { 1, 4, 10, 23, 57, 132, 301, 701 };

    {
        for (int i = 'a'; i <= 'z'; i++) {
            bucket[i] = new int[1000];
        }
    }

    public Quicksort() {
        int length = 100000;
        long start;
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        quickSort(array, 0, array.length - 1);

        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }
        printArray(array);
        start = System.nanoTime();
        quickSort(array, 0, array.length - 1);
        double time = (System.nanoTime() - start) / 1000000.0;
        System.out.println(time + "ms");
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
    void quickSort(int[] array, int start, int end) {
        if (end - start > 1000) {
            int pivot = array[start];
            int left = start;
            int right = end + 1;
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
        } else {
            int index =
                    redixSort(array, start, end, index);
        }
    }

    void redixSort(int[] array, int start, int end, int index) {

    }

    void shellSort() {

    }

    boolean verifyOrder(int a, int b) {
        return a <= b;
    }

    public static void main(String[] args) {
        new Quicksort();
    }
}
