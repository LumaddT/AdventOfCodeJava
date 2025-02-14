package advent.of.code.solutions.y2022.day17;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.Directions;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.ntuples.Tuple;

import java.util.*;


/**
 * Rocks go up instead of going down because of how I implemented Coordinate in my utils.
 */
@SuppressWarnings("unused")
public class Day17 implements Day {
    private static final int ITERATIONS_PART_1 = 2_022;
    private static final long ITERATIONS_PART_2 = 1_000_000_000_000L;

    private static final int GRID_WIDTH = 7;

    private static final int INIT_COLUMN = 2;
    private static final int INIT_ROW_OFFSET = 4;

    @Override
    public String part1(String inputString) {
        List<Directions> input = inputString.trim().chars()
                .mapToObj(ch -> ParsingUtils.charToDirection((char) ch))
                .toList();

        Set<Coordinate> filledSquares = new HashSet<>();
        int directionsIndex = 0;

        for (int i = 0; i < ITERATIONS_PART_1; i++) {
            directionsIndex = dropRock(filledSquares,
                    Rocks.ALL_ROCKS.get(i % Rocks.ALL_ROCKS.size()),
                    directionsIndex,
                    input);
        }

        return String.format("%d", filledSquares.stream().max(Comparator.comparingInt(Coordinate::Row)).orElseThrow().Row() + 1);
    }

    @Override
    public String part2(String inputString) {
        List<Directions> input = inputString.trim().chars()
                .mapToObj(ch -> ParsingUtils.charToDirection((char) ch))
                .toList();

        Set<Coordinate> filledSquares = new HashSet<>();
        int directionsIndex = 0;
        Map<Integer, Integer> heights = new HashMap<>();
        Map<Tuple<Integer, Integer>, Integer> cache = new HashMap<>();
        int cacheHits = 0;

        for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
            directionsIndex = dropRock(filledSquares,
                    Rocks.ALL_ROCKS.get(i % Rocks.ALL_ROCKS.size()),
                    directionsIndex,
                    input);
            Tuple<Integer, Integer> cacheKey = new Tuple<>(i % Rocks.ALL_ROCKS.size(), directionsIndex);
            if (cache.containsKey(cacheKey)) {
                // This is a terrible hack, but it works and that's all I need
                cacheHits++;
                if (cacheHits == 2000) {
                    int cycleStart = cache.get(cacheKey);
                    int cycleLength = i - cycleStart;
                    int heightStart = heights.get(cycleStart);
                    int currentHeight = filledSquares.stream().max(Comparator.comparingInt(Coordinate::Row)).orElseThrow().Row();
                    int heightDiff = currentHeight - heightStart;

                    long cyclesRemaining = (ITERATIONS_PART_2 - i) / cycleLength;
                    int remainder = (int) ((ITERATIONS_PART_2 - i) % cycleLength);

                    long returnValue = currentHeight + heightDiff * cyclesRemaining;

                    int remainderHeightDiff = heights.get(cycleStart + remainder - 1) - heightStart;
                    returnValue += remainderHeightDiff;

                    return String.format("%d", returnValue + 1);
                }
            }

            heights.put(i, filledSquares.stream().max(Comparator.comparingInt(Coordinate::Row)).orElseThrow().Row());
            cache.put(cacheKey, i);
        }

        throw new RuntimeException("Bad solution.");
    }

    private int dropRock(Set<Coordinate> filledSquares, Boolean[][] rock, int directionsIndex, List<Directions> directions) {
        int bottomFilledRow = filledSquares.stream().max(Comparator.comparingInt(Coordinate::Row)).orElse(new Coordinate(-1, -1)).Row();

        Coordinate topLeftCorner = new Coordinate(bottomFilledRow + INIT_ROW_OFFSET, INIT_COLUMN);

        while (true) {
            Directions direction = directions.get(directionsIndex);
            directionsIndex++;
            directionsIndex %= directions.size();

            topLeftCorner = move(filledSquares, rock, topLeftCorner, direction);
            if (topLeftCorner == null) {
                throw new RuntimeException("Bad direction sent.");
            }

            Coordinate oldTopLeftCorner = topLeftCorner;
            topLeftCorner = move(filledSquares, rock, topLeftCorner, Directions.UP);

            if (topLeftCorner == null) {
                setInPlace(filledSquares, rock, oldTopLeftCorner);
                break;
            }
        }

        return directionsIndex;
    }

    private Coordinate move(Set<Coordinate> filledSquares, Boolean[][] rock, Coordinate topLeftCorner, Directions direction) {
        Coordinate newTopLeftCorner = topLeftCorner.move(direction);

        for (int row = 0; row < rock.length; row++) {
            for (int column = 0; column < rock[0].length; column++) {
                Coordinate checkedCoordinate = newTopLeftCorner.down(row).right(column);
                if (checkedCoordinate.Column() < 0
                        || checkedCoordinate.Column() >= GRID_WIDTH
                        || checkedCoordinate.Row() < 0
                        || rock[row][column] && filledSquares.contains(checkedCoordinate)) {
                    if (direction == Directions.UP) {
                        return null;
                    }
                    return topLeftCorner;
                }
            }
        }

        return newTopLeftCorner;
    }

    private void setInPlace(Set<Coordinate> filledSquares, Boolean[][] rock, Coordinate topLeftCorner) {
        for (int row = rock.length - 1; row >= 0; row--) {
            for (int column = 0; column < rock[0].length; column++) {
                if (rock[row][column]) {
                    filledSquares.add(topLeftCorner.down(row).right(column));
                }
            }
        }
    }
}
