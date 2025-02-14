package advent.of.code.solutions.y2019.day10;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day10 implements Day {
    private static final int INTERESTING_ASTEROID_PART_2 = 199;

    @Override
    public String part1(String inputString) {
        Set<Coordinate> input = parseInputString(inputString);

        return String.format("%d", input.stream()
                .mapToInt(a -> getAsteroidsItCanSee(a, input).size())
                .max()
                .orElseThrow());
    }

    @Override
    public String part2(String inputString) {
        Set<Coordinate> input = parseInputString(inputString);

        Coordinate asteroidCanSeeMost = input.stream()
                .max((left, right) -> getAsteroidsItCanSee(left, input).size() - getAsteroidsItCanSee(right, input).size())
                .orElseThrow();

        Coordinate interestingAsteroid = getAsteroidsItCanSee(asteroidCanSeeMost, input)
                .stream()
                .map(a -> a.subtract(asteroidCanSeeMost))
                .sorted(this::compareBasedOnAngle)
                .map(a -> a.add(asteroidCanSeeMost))
                .toList()
                .get(INTERESTING_ASTEROID_PART_2);

        return String.format("%d", interestingAsteroid.Column() * 100 + interestingAsteroid.Row());
    }

    private Set<Coordinate> parseInputString(String inputString) {
        Set<Coordinate> returnValue = new HashSet<>();
        char[][] inputGrid = ParsingUtils.toGrid(inputString);

        for (int row = 0; row < inputGrid.length; row++) {
            for (int column = 0; column < inputGrid[row].length; column++) {
                if (inputGrid[row][column] == '#') {
                    returnValue.add(new Coordinate(row, column));
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private Set<Coordinate> getAsteroidsItCanSee(Coordinate asteroid, Set<Coordinate> asteroids) {
        Set<Coordinate> returnValue = new HashSet<>();

        for (Coordinate other : asteroids) {
            if (asteroid == other) {
                continue;
            }

            Set<Coordinate> coordinatesBetween = getCoordinatesBetween(asteroid, other);
            boolean canSeeOther = true;

            for (Coordinate coordinateBetween : coordinatesBetween) {
                if (asteroids.contains(coordinateBetween)) {
                    canSeeOther = false;
                    break;
                }
            }

            if (canSeeOther) {
                returnValue.add(other);
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private Set<Coordinate> getCoordinatesBetween(Coordinate asteroid, Coordinate other) {
        Set<Coordinate> returnValue = new HashSet<>();
        Coordinate otherOffset = other.subtract(asteroid);

        for (int i = 2; i <= Math.max(Math.abs(otherOffset.Row()), Math.abs(otherOffset.Column())); i++) {
            if (otherOffset.Row() % i == 0 && otherOffset.Column() % i == 0) {
                int baseBetweenRow = otherOffset.Row() / i;
                int baseBetweenColumn = otherOffset.Column() / i;

                for (int multiplier = 1; multiplier < i; multiplier++) {
                    int betweenRow = baseBetweenRow * multiplier + asteroid.Row();
                    int betweenColumn = baseBetweenColumn * multiplier + asteroid.Column();
                    returnValue.add(new Coordinate(betweenRow, betweenColumn));
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    /**
     * Compare based on angle from vertical. A Coordinate on the y axes
     * is the first, and then each other coordinate in clockwise order.
     */
    private int compareBasedOnAngle(Coordinate left, Coordinate right) {
        // If the base is shooting the laser up in a clockwise manner, in an actual cartesian plane the laser is
        // being shot down, clockwise. The coordinate system in the problem has the Y axis inverted. Adding 90 degrees
        // accounts for that.
        double thisAngle = 90 + Math.toDegrees(Math.atan2(left.Row(), left.Column()));
        while (thisAngle < 0) {
            thisAngle += 360;
        }

        double otherAngle = 90 + Math.toDegrees(Math.atan2(right.Row(), right.Column()));
        while (otherAngle < 0) {
            otherAngle += 360;
        }

        return (int) Math.signum(thisAngle - otherAngle);
    }
}
