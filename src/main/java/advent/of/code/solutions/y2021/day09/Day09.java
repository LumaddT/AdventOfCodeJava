package advent.of.code.solutions.y2021.day09;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.SetUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day09 implements Day {
    private static final int LIMIT_PART_2 = 3;

    @Override
    public String part1(String inputString) {
        Integer[][] input = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars().map(n -> n - '0').boxed().toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        int total = 0;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                boolean isLowest = true;
                int value = MatrixUtils.getMatrixCoord(input, coordinate);

                for (Coordinate adjacent : coordinate.orthogonallyAdjacent()) {
                    if (!MatrixUtils.isCoordInRange(input, adjacent)) {
                        continue;
                    }

                    if (value >= MatrixUtils.getMatrixCoord(input, adjacent)) {
                        isLowest = false;
                        break;
                    }

                }

                if (isLowest) {
                    total += value + 1;
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Integer[][] input = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars().map(n -> n - '0').boxed().toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        int currentBasin = -1;
        Set<Coordinate> exploredCoordinates = new HashSet<>();
        Map<Integer, Set<Coordinate>> basinCoordinates = new HashMap<>();

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                Coordinate initialCoordinate = new Coordinate(row, column);

                if (!MatrixUtils.isCoordInRange(input, initialCoordinate)
                        || exploredCoordinates.contains(initialCoordinate)
                        || MatrixUtils.getMatrixCoord(input, initialCoordinate) == 9) {
                    continue;
                }

                currentBasin++;
                basinCoordinates.put(currentBasin, new HashSet<>());
                basinCoordinates.get(currentBasin).add(initialCoordinate);
                exploredCoordinates.add(initialCoordinate);

                Queue<Coordinate> toExplore = new LinkedList<>(initialCoordinate.orthogonallyAdjacent());

                while (!toExplore.isEmpty()) {
                    Coordinate currentCoordinate = toExplore.poll();
                    if (!MatrixUtils.isCoordInRange(input, currentCoordinate)) {
                        continue;
                    }

                    exploredCoordinates.add(currentCoordinate);

                    if (MatrixUtils.getMatrixCoord(input, currentCoordinate) != 9) {
                        basinCoordinates.get(currentBasin).add(currentCoordinate);
                        toExplore.addAll(SetUtils.difference(currentCoordinate.orthogonallyAdjacent(), exploredCoordinates));
                    }
                }
            }
        }

        int[] largestBasins = basinCoordinates.values().stream()
                .mapToInt(Set::size)
                .map(n -> -n)
                .sorted()
                .map(n -> -n)
                .limit(LIMIT_PART_2)
                .toArray();

        int total = 1;
        for (int value : largestBasins) {
            total *= value;
        }

        return String.format("%d", total);
    }
}
