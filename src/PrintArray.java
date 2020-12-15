public class PrintArray {
    static final void print(boolean[] array) {
        int length = array.length - 1, i = 0;
        System.out.print("{ ");
        while (i < length) {
            System.out.print((array[i++] ? 'T' : 'F') + ", ");
        }
        System.out.println((array[i] ? 'T' : 'F') + " }");
    }

    static final void print(boolean[][] array) {
        int length, i;
        System.out.print("{\n");
        for (boolean[] a : array) {
            if (a == null) {
                System.out.println("\tnull, ");
            } else {
                length = a.length - 1;
                i = 0;
                System.out.print("\t{ ");
                while (i < length) {
                    System.out.print((a[i++] ? 'T' : 'F') + ", ");
                }
                System.out.println((a[i] ? 'T' : 'F') + " }");
            }
        }
        System.out.println("}");
    }

    static final void print(char[] array) {
        int length = array.length - 1, i = 0;
        System.out.print("{ ");
        while (i < length) {
            System.out.print(array[i++] + ", ");
        }
        System.out.println(array[i] + " }");
    }

    static final void print(char[][] array) {
        int length, i;
        System.out.print("{\n");
        for (char[] a : array) {
            if (a == null) {
                System.out.println("\tnull, ");
            } else {
                length = a.length - 1;
                i = 0;
                System.out.print("\t{ ");
                while (i < length) {
                    System.out.print(a[i++] + ", ");
                }
                System.out.println(a[i] + " }");
            }
        }
        System.out.println("}");
    }

    static final void print(int[] array) {
        int length = array.length - 1, i = 0;
        System.out.print("{ ");
        while (i < length) {
            System.out.print(array[i++] + ", ");
        }
        System.out.println(array[i] + " }");
    }

    static final void print(int[][] array) {
        int length, i;
        System.out.print("{\n");
        for (int[] a : array) {
            if (a == null) {
                System.out.println("\tnull, ");
            } else {
                length = a.length - 1;
                i = 0;
                System.out.print("\t{ ");
                while (i < length) {
                    System.out.print(a[i++] + ", ");
                }
                System.out.println(a[i] + " }");
            }
        }
        System.out.println("}");
    }

    static final void print(String[] array) {
        int length = array.length - 1, i = 0;
        System.out.print("{ ");
        while (i < length) {
            System.out.print(array[i++] + ", ");
        }
        System.out.println(array[i] + " }");
    }

    static final void print(String[][] array) {
        int length, i;
        System.out.print("{\n");
        for (String[] a : array) {
            if (a == null) {
                System.out.println("\tnull, ");
            } else {
                length = a.length - 1;
                i = 0;
                System.out.print("\t{ ");
                while (i < length) {
                    System.out.print(a[i++] + ", ");
                }
                System.out.println(a[i] + " }");
            }
        }
        System.out.println("}");
    }
}
