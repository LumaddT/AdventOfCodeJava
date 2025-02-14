package advent.of.code.utils;

public class MathUtils {
    /**
     * Greatest Common Divisor
     */
    public static int gcd(int first, int second) {
        if (first == second) {
            return first;
        }

        if (first > second) {
            return MathUtils.gcd(first - second, second);
        }

        return MathUtils.gcd(first, second - first);
    }

    /**
     * Least Common Multiple
     */
    public static long lcm(int first, int second) {
        return ((long) first) * second / MathUtils.gcd(first, second);
    }
}
