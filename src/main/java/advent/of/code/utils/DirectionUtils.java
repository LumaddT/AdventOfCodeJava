package advent.of.code.utils;

import java.util.Set;

public class DirectionUtils {
    public static final Set<Directions> ALL_DIRECTIONS = Set.of(Directions.UP, Directions.DOWN, Directions.LEFT, Directions.RIGHT);
    public static final Set<Directions> VERTICAL_DIRECTIONS = Set.of(Directions.UP, Directions.DOWN);
    public static final Set<Directions> HORIZONTAL_DIRECTIONS = Set.of(Directions.LEFT, Directions.RIGHT);

    public static Directions opposite(Directions direction) {
        return switch (direction) {
            case UP -> Directions.DOWN;
            case DOWN -> Directions.UP;
            case LEFT -> Directions.RIGHT;
            case RIGHT -> Directions.LEFT;
        };
    }

    public static Directions rotateClockwise(Directions direction) {
        return switch (direction) {
            case UP -> Directions.RIGHT;
            case RIGHT -> Directions.DOWN;
            case DOWN -> Directions.LEFT;
            case LEFT -> Directions.UP;
        };
    }

    public static Directions rotateCounterclockwise(Directions direction) {
        return switch (direction) {
            case UP -> Directions.LEFT;
            case LEFT -> Directions.DOWN;
            case DOWN -> Directions.RIGHT;
            case RIGHT -> Directions.UP;
        };
    }

    public static Directions rotateClockwise(Directions direction, int amount) {
        for (int i = 0; i < amount % 4; i++) {
            direction = rotateClockwise(direction);
        }

        return direction;
    }

    public static Directions rotateCounterclockwise(Directions direction, int amount) {
        for (int i = 0; i < amount % 4; i++) {
            direction = rotateCounterclockwise(direction);
        }

        return direction;
    }

    public static char directionToChar(Directions direction) {
        return switch (direction) {
            case UP -> '^';
            case DOWN -> 'v';
            case LEFT -> '<';
            case RIGHT -> '>';
        };
    }

    public static Set<Directions> perpendicularDirections(Directions direction) {
        return switch (direction) {
            case UP, DOWN -> HORIZONTAL_DIRECTIONS;
            case LEFT, RIGHT -> VERTICAL_DIRECTIONS;
        };
    }
}
