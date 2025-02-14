package advent.of.code.solutions.y2020.day02;

public record Line(int Min, int Max, char Ch, String Password) {
    public boolean isValidPart1() {
        int instances = (int) Password.chars().filter(ch -> ch == Ch).count();
        return instances >= Min && instances <= Max;
    }

    public boolean isValidPart2() {
        char chMin = Password.charAt(Min - 1);
        char chMax = Password.charAt(Max - 1);

        return chMin == Ch ^ chMax == Ch;
    }
}
