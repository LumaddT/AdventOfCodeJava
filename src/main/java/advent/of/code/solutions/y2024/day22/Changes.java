package advent.of.code.solutions.y2024.day22;

record Changes(int First, int Second, int Third, int Fourth) {
    public Changes insertLast(int value) {
        return new Changes(Second, Third, Fourth, value);
    }
}
