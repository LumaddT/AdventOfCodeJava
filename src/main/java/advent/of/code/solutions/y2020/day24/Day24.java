package advent.of.code.solutions.y2020.day24;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.SetUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day24 implements Day {
    private static final Set<String> LEGAL_MOVEMENTS = Set.of("w", "e", "nw", "ne", "sw", "se");

    private static final int ITERATIONS_PART_2 = 100;

    @Override
    public String part1(String inputString) {
        Set<Coordinate> activeCoordinates = parseInputString(inputString);
        return String.format("%d", activeCoordinates.size());
    }

    @Override
    public String part2(String inputString) {
        Set<Coordinate> activeCoordinates = parseInputString(inputString);

        for (int i = 0; i < ITERATIONS_PART_2; i++) {
            activeCoordinates = performStep(activeCoordinates);
        }

        return String.format("%d", activeCoordinates.size());
    }

    private Set<Coordinate> parseInputString(String inputString) {
        Set<Coordinate> activeCoordinates = new HashSet<>();
        String[] input = inputString.split("\n");
        List<Coordinate> targetCoordinates = Arrays.stream(input).map(this::stringToCoordinate).toList();

        for (Coordinate targetCoordinate : targetCoordinates) {
            if (activeCoordinates.contains(targetCoordinate)) {
                activeCoordinates.remove(targetCoordinate);
            } else {
                activeCoordinates.add(targetCoordinate);
            }
        }

        return Collections.unmodifiableSet(activeCoordinates);
    }

    private HexDirections nameToDirection(String name) {
        return switch (name) {
            case "w" -> HexDirections.LEFT;
            case "e" -> HexDirections.RIGHT;
            case "nw" -> HexDirections.UP_LEFT;
            case "ne" -> HexDirections.UP_RIGHT;
            case "sw" -> HexDirections.DOWN_LEFT;
            case "se" -> HexDirections.DOWN_RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    private Coordinate move(Coordinate coordinate, HexDirections direction) {
        return switch (direction) {
            case LEFT -> coordinate.left();
            case RIGHT -> coordinate.right();
            case UP_LEFT -> coordinate.up();
            case UP_RIGHT -> coordinate.up().right();
            case DOWN_LEFT -> coordinate.down().left();
            case DOWN_RIGHT -> coordinate.down();
        };
    }

    private Coordinate stringToCoordinate(String string) {
        Coordinate currentCoordinate = Coordinate.ORIGIN;
        String currentDirection = "";

        for (int i = 0; i < string.length(); i++) {
            currentDirection += string.charAt(i);

            if (LEGAL_MOVEMENTS.contains(currentDirection)) {
                currentCoordinate = move(currentCoordinate, nameToDirection(currentDirection));
                currentDirection = "";
            }
        }

        return currentCoordinate;
    }

    private Set<Coordinate> getAdjacent(Coordinate coordinate) {
        return Arrays.stream(HexDirections.values()).map(d -> move(coordinate, d)).collect(Collectors.toSet());
    }

    private Set<Coordinate> performStep(Set<Coordinate> activeCoordinates) {
        Set<Coordinate> returnValue = new HashSet<>();

        int minRow = activeCoordinates.stream().mapToInt(Coordinate::Row).min().orElseThrow() - 1;
        int maxRow = activeCoordinates.stream().mapToInt(Coordinate::Row).max().orElseThrow() + 1;
        int minColumn = activeCoordinates.stream().mapToInt(Coordinate::Column).min().orElseThrow() - 1;
        int maxColumn = activeCoordinates.stream().mapToInt(Coordinate::Column).max().orElseThrow() + 1;

        for (int row = minRow; row <= maxRow; row++) {
            for (int column = minColumn; column <= maxColumn; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                int activeAdjacent = SetUtils.intersection(getAdjacent(coordinate), activeCoordinates).size();

                if (activeCoordinates.contains(coordinate) && !(activeAdjacent == 0 || activeAdjacent > 2)
                        || !activeCoordinates.contains(coordinate) && activeAdjacent == 2) {
                    returnValue.add(coordinate);
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }
}
