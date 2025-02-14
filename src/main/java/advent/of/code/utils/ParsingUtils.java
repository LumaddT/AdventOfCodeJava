package advent.of.code.utils;

import java.util.Arrays;

public class ParsingUtils {
    public static char[][] toGrid(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    public static Character[][] toGridBoxed(String inputString) {
        char[][] unboxed = toGrid(inputString);
        Character[][] returnValue = new Character[unboxed.length][unboxed[0].length];

        for (int row = 0; row < unboxed.length; row++) {
            for (int column = 0; column < unboxed[0].length; column++) {
                returnValue[row][column] = unboxed[row][column];
            }
        }

        return returnValue;
    }

    public static Integer[][] toIntegerGrid(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars().map(n -> n - '0').boxed().toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }

    public static Directions charToDirection(char ch) {
        return switch (ch) {
            case '^' -> Directions.UP;
            case 'v' -> Directions.DOWN;
            case '<' -> Directions.LEFT;
            case '>' -> Directions.RIGHT;
            default -> throw new IllegalArgumentException(String.format("Bad direction char %c.", ch));
        };
    }

    public static Directions letterToDirection(char ch) {
        return switch (ch) {
            case 'U' -> Directions.UP;
            case 'D' -> Directions.DOWN;
            case 'L' -> Directions.LEFT;
            case 'R' -> Directions.RIGHT;
            default -> throw new IllegalArgumentException(String.format("Bad direction letter %c.", ch));
        };
    }
}
