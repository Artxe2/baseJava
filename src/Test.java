
public class Test {
    public static void main(String[] args) {
        int index = 0;
        double time = 0;
        String s = "정확성  테스트\r\n" +
                "테스트 1 〉 통과 (0.04ms, 52.1MB)\r\n" +
                "테스트 2 〉 통과 (0.04ms, 52MB)\r\n" +
                "테스트 3 〉 통과 (32.46ms, 54.2MB)\r\n" +
                "테스트 4 〉 통과 (30.03ms, 52.2MB)\r\n" +
                "테스트 5 〉 통과 (0.39ms, 52.3MB)\r\n" +
                "테스트 6 〉 통과 (3.57ms, 53.5MB)\r\n" +
                "테스트 7 〉 통과 (0.04ms, 52.5MB)\r\n" +
                "테스트 8 〉 통과 (0.09ms, 52.5MB)\r\n" +
                "테스트 9 〉 통과 (0.08ms, 51.7MB)\r\n" +
                "테스트 10 〉    통과 (16.02ms, 52.8MB)\r\n" +
                "테스트 11 〉    통과 (18.48ms, 52.1MB)\r\n" +
                "테스트 12 〉    통과 (15.07ms, 52.6MB)\r\n" +
                "테스트 13 〉    통과 (37.97ms, 52.7MB)\r\n" +
                "테스트 14 〉    통과 (26.35ms, 52MB)\r\n" +
                "테스트 15 〉    통과 (16.67ms, 52.7MB)\r\n" +
                "테스트 16 〉    통과 (0.69ms, 52.8MB)\r\n" +
                "테스트 17 〉    통과 (3.25ms, 51.7MB)\r\n" +
                "테스트 18 〉    통과 (6.82ms, 52.4MB)\r\n" +
                "테스트 19 〉    통과 (0.17ms, 52.8MB)\r\n" +
                "테스트 20 〉    통과 (0.69ms, 52.8MB)\r\n" +
                "테스트 21 〉    통과 (0.37ms, 52.3MB)\r\n" +
                "테스트 22 〉    통과 (0.13ms, 53.3MB)\r\n" +
                "테스트 23 〉    통과 (0.16ms, 53.2MB)\r\n" +
                "테스트 24 〉    통과 (0.14ms, 52.7MB)\r\n" +
                "테스트 25 〉    통과 (3.83ms, 51.9MB)\r\n" +
                "채점 결과\r\n" +
                "정확성: 100.0\r\n" +
                "합계: 100.0 / 100.0";

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
        System.out.println(time + "ms");
    }
}
