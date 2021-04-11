import java.util.PriorityQueue;

class Main {
    public static void main(String[] args) {
        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("ACD");
        queue.add("ADE");
        queue.add("AD");
        queue.add("CD");
        queue.add("XYZ");
        System.out.println(queue);
    }
}