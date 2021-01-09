class Main {
    public static void main(String[] args) {
        int hp = 2000;
        int lostHp = 0;
        int damage = 200;
        int result = 0;
        int start = 0, mid, end = (int) Math.ceil(hp * 0.77);
        while (start < end) {
            mid = (start + end + 1) / 2;
            lostHp = mid;
            result = (int) ((2 * (lostHp * 100.0 / hp > 77 ? 77 : lostHp * 100.0 / hp) / 77 + 1) * damage);
            if (result * 4 + lostHp > hp) {
                end = mid - 1;
            } else {
                start = mid;
            }
        }
    }
}