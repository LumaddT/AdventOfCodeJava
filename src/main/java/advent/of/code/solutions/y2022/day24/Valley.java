package advent.of.code.solutions.y2022.day24;

import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;
import lombok.Getter;

import java.util.*;

class Valley {
    private final Character[][] Grid;
    private final int MaxRow;
    private final int MaxColumn;

    private final Map<Directions, Set<Coordinate>> Blizzards = new HashMap<>();
    private final Set<Coordinate> Walls;

    @Getter
    private final Coordinate Entrance;
    @Getter
    private final Coordinate Exit;

    @Getter
    private final int RepeatCycles;

    public Valley(String inputString) {
        Walls = new HashSet<>();
        Grid = ParsingUtils.toGridBoxed(inputString);

        MaxRow = Grid.length - 2;
        MaxColumn = Grid[0].length - 2;

        for (Directions direction : Directions.values()) {
            Blizzards.put(direction, new HashSet<>());
        }

        for (int row = 0; row < Grid.length; row++) {
            for (int column = 0; column < Grid[row].length; column++) {
                if (Grid[row][column] == '#') {
                    Walls.add(new Coordinate(row, column));
                } else if (Grid[row][column] != '.') {
                    Blizzards.get(ParsingUtils.charToDirection(Grid[row][column])).add(new Coordinate(row, column));
                }
            }
        }

        Entrance = MatrixUtils.coordinateOf(Grid, '.');
        Exit = MatrixUtils.lastCoordinateOf(Grid, '.');

        RepeatCycles = (int) MathUtils.lcm(MaxRow, MaxColumn);
    }

    public void move() {
        Map<Directions, Set<Coordinate>> oldBlizzards = new HashMap<>();
        for (Directions direction : Directions.values()) {
            oldBlizzards.put(direction, new HashSet<>(Blizzards.get(direction)));
            Blizzards.get(direction).clear();

            for (Coordinate blizzard : oldBlizzards.get(direction)) {
                Coordinate newBlizzard = blizzard.move(direction);

                if (newBlizzard.Row() > MaxRow) {
                    newBlizzard = newBlizzard.setRow(1);
                } else if (newBlizzard.Column() > MaxColumn) {
                    newBlizzard = newBlizzard.setColumn(1);
                } else if (newBlizzard.Row() < 1) {
                    newBlizzard = newBlizzard.setRow(MaxRow);
                } else if (newBlizzard.Column() < 1) {
                    newBlizzard = newBlizzard.setColumn(MaxColumn);
                }

                Blizzards.get(direction).add(newBlizzard);
            }
        }
    }

    private boolean isBlizzard(Coordinate coordinate) {
        for (Directions direction : Directions.values()) {
            if (Blizzards.get(direction).contains(coordinate)) {
                return true;
            }
        }

        return false;
    }

    public Set<Coordinate> getFreeSpaces() {
        Set<Coordinate> returnValue = new HashSet<>();

        for (int row = 0; row < Grid.length; row++) {
            for (int column = 0; column < Grid[row].length; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                if (!isBlizzard(coordinate) && !Walls.contains(coordinate)) {
                    returnValue.add(coordinate);
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }
}
