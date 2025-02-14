package advent.of.code.solutions.y2020.day16;

record Range(int MinOne, int MaxOne, int MinTwo, int MaxTwo) {
    public boolean containsValue(int value) {
        return MinOne <= value && value <= MaxOne || MinTwo <= value && value <= MaxTwo;
    }
}
