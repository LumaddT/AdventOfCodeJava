package advent.of.code.solutions.y2019.day11;

import advent.of.code.utils.coordinates.Coordinate;

import java.util.*;
import java.util.stream.IntStream;

class Grid {
    private final Map<Coordinate, Integer> Mapping = new HashMap<>();

    public int getColor(Coordinate coordinate) {
        if (Mapping.containsKey(coordinate)) {
            return Mapping.get(coordinate);
        }

        return 0;
    }

    public void putColor(Coordinate coordinate, int color) {
        if (color != 0 && color != 1) {
            throw new RuntimeException(String.format("Bad color %d.", color));
        }

        Mapping.put(coordinate, color);
    }

    public int getColoredTilesCount() {
        return Mapping.size();
    }

    public int getRightmostColumn() {
        return Mapping.keySet().stream().max(Comparator.comparingInt(Coordinate::Column)).orElseThrow().Column();
    }

    public int getLeftmostColumn() {
        return Mapping.keySet().stream().min(Comparator.comparingInt(Coordinate::Column)).orElseThrow().Column();
    }

    public void keepFullColumns() {
        int minColumn = Mapping.keySet().stream()
                .mapToInt(Coordinate::Column)
                .min()
                .orElseThrow();
        int maxColumn = Mapping.keySet().stream()
                .mapToInt(Coordinate::Column)
                .max()
                .orElseThrow();
        int minRow = Mapping.keySet().stream()
                .mapToInt(Coordinate::Row)
                .min()
                .orElseThrow();
        int maxRow = Mapping.keySet().stream()
                .mapToInt(Coordinate::Row)
                .max()
                .orElseThrow();

        if (minColumn != 0 || minRow != 0 || maxRow != 5) {
            throw new RuntimeException("Bad rows and columns assumptions.");
        }

        Boolean[] isColumnFull = IntStream.range(0, maxColumn + 1).boxed().map(l -> true).toArray(Boolean[]::new);

        for (int row = 0; row <= 5; row++) {
            for (int column = 0; column <= maxColumn; column++) {
                if (!Mapping.containsKey(new Coordinate(row, column))) {
                    isColumnFull[column] = false;
                }
            }
        }

        Set<Coordinate> toRemove = new HashSet<>();
        for (Coordinate coordinate : Mapping.keySet()) {
            if (!isColumnFull[coordinate.Column()]) {
                toRemove.add(coordinate);
            }
        }

        for (Coordinate coordinate : toRemove) {
            Mapping.remove(coordinate);
        }
    }
}