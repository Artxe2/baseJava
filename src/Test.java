public class Test {
    public static void main(String[] args) {
        int index = 0;
        double time = 0;
        String s = "채점을 시작합니다.\r\n"
                + "정확성  테스트\r\n"
                + "테스트 1 〉 통과 (0.77ms, 52.2MB)\r\n"
                + "테스트 2 〉 통과 (1.06ms, 52.3MB)\r\n"
                + "테스트 3 〉 통과 (0.97ms, 51.7MB)\r\n"
                + "테스트 4 〉 통과 (1.00ms, 52.8MB)\r\n"
                + "테스트 5 〉 통과 (1.02ms, 53MB)\r\n"
                + "테스트 6 〉 통과 (0.81ms, 52.6MB)\r\n"
                + "테스트 7 〉 통과 (1.03ms, 52MB)\r\n"
                + "테스트 8 〉 통과 (0.85ms, 52.8MB)\r\n"
                + "테스트 9 〉 통과 (1.07ms, 52.5MB)\r\n"
                + "테스트 10 〉    통과 (1.07ms, 53.7MB)\r\n"
                + "테스트 11 〉    통과 (1.61ms, 52.1MB)\r\n"
                + "테스트 12 〉    통과 (1.03ms, 51.9MB)\r\n"
                + "테스트 13 〉    통과 (0.95ms, 52.8MB)\r\n"
                + "테스트 14 〉    통과 (1.37ms, 52.6MB)\r\n"
                + "테스트 15 〉    통과 (3.52ms, 53.4MB)\r\n"
                + "테스트 16 〉    통과 (10.91ms, 52.6MB)\r\n"
                + "테스트 17 〉    통과 (29.42ms, 53.1MB)\r\n"
                + "테스트 18 〉    통과 (25.71ms, 56.9MB)\r\n"
                + "테스트 19 〉    통과 (26.26ms, 56.6MB)\r\n"
                + "테스트 20 〉    통과 (76.38ms, 62.8MB)\r\n"
                + "테스트 21 〉    통과 (72.86ms, 60.3MB)\r\n"
                + "테스트 22 〉    통과 (126.70ms, 66.4MB)\r\n"
                + "테스트 23 〉    통과 (119.29ms, 68.7MB)\r\n"
                + "테스트 24 〉    통과 (21.96ms, 60.8MB)\r\n"
                + "테스트 25 〉    통과 (55.26ms, 64.3MB)\r\n"
                + "테스트 26 〉    통과 (115.41ms, 65.7MB)\r\n"
                + "테스트 27 〉    통과 (115.92ms, 65.8MB)\r\n"
                + "테스트 28 〉    통과 (130.25ms, 68.7MB)\r\n"
                + "테스트 29 〉    통과 (107.64ms, 68MB)\r\n"
                + "테스트 30 〉    통과 (110.41ms, 64.8MB)\r\n"
                + "채점 결과\r\n"
                + "정확성: 100.0\r\n"
                + "합계: 100.0 / 100.0";

        int i = 1;
        while (index < s.length()) {
            int start = s.indexOf('(', index) + 1, end = s.indexOf('m', start);
            index = end + 1;
            if (start > 0) {
                time += Double.parseDouble(s.substring(start, end));
                System.out.println(i++ + ": " + time + "ms");
            } else {
                break;
            }
        }
        System.out.println(Math.round(time * 100) / 100.0 + "ms");
    }
}
