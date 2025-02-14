package advent.of.code.utils.coordinates;

import advent.of.code.utils.Directions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record Coordinate(int Row, int Column) {
    public static final Coordinate ORIGIN = new Coordinate(0, 0);

    public Coordinate up() {
        return new Coordinate(Row - 1, Column);
    }

    public Coordinate down() {
        return new Coordinate(Row + 1, Column);
    }

    public Coordinate left() {
        return new Coordinate(Row, Column - 1);
    }

    public Coordinate right() {
        return new Coordinate(Row, Column + 1);
    }

    public Coordinate move(Directions direction) {
        return switch (direction) {
            case UP -> this.up();
            case DOWN -> this.down();
            case LEFT -> this.left();
            case RIGHT -> this.right();
        };
    }

    public Coordinate up(int i) {
        if (i < 0) {
            throw new RuntimeException("Negative direction movement amount.");
        }
        return (new Coordinate(Row - i, Column));
    }

    public Coordinate down(int i) {
        if (i < 0) {
            throw new RuntimeException("Negative direction movement amount.");
        }
        return (new Coordinate(Row + i, Column));
    }

    public Coordinate left(int i) {
        if (i < 0) {
            throw new RuntimeException("Negative direction movement amount.");
        }
        return (new Coordinate(Row, Column - i));
    }

    public Coordinate right(int i) {
        if (i < 0) {
            throw new RuntimeException("Negative direction movement amount.");
        }
        return (new Coordinate(Row, Column + i));
    }

    public Coordinate move(Directions direction, int i) {
        return switch (direction) {
            case UP -> this.up(i);
            case DOWN -> this.down(i);
            case LEFT -> this.left(i);
            case RIGHT -> this.right(i);
        };
    }

    public Set<Coordinate> orthogonallyAdjacent() {
        return Set.of(this.up(), this.down(), this.left(), this.right());
    }

    public Set<Coordinate> adjacent() {
        return Set.of(this.up(), this.down(), this.left(), this.right(),
                this.up().left(), this.up().right(), this.down().left(), this.down().right());
    }

    public Coordinate add(Coordinate other) {
        return new Coordinate(this.Row + other.Row, this.Column + other.Column);
    }

    public Coordinate subtract(Coordinate other) {
        return new Coordinate(this.Row - other.Row, this.Column - other.Column);
    }

    public Set<Coordinate> coordinatesInRadius(int radius) {
        Set<Coordinate> returnValue = new HashSet<>();
        Coordinate topCoordinate = this;

        for (int i = 0; i < radius; i++) {
            topCoordinate = topCoordinate.up();
        }

        returnValue.add(topCoordinate);
        Coordinate rowLeftmostCoordinate = topCoordinate;
        int rowWidth = 1;

        for (int row = 0; row < radius; row++) {
            rowWidth += 2;
            rowLeftmostCoordinate = rowLeftmostCoordinate.down().left();
            Coordinate currentCoordinate = rowLeftmostCoordinate;
            returnValue.add(rowLeftmostCoordinate);
            for (int column = 1; column < rowWidth; column++) {
                currentCoordinate = currentCoordinate.right();
                returnValue.add(currentCoordinate);
            }
        }

        for (int row = 0; row < radius; row++) {
            rowWidth -= 2;
            rowLeftmostCoordinate = rowLeftmostCoordinate.down().right();
            Coordinate currentCoordinate = rowLeftmostCoordinate;
            returnValue.add(rowLeftmostCoordinate);
            for (int column = 1; column < rowWidth; column++) {
                currentCoordinate = currentCoordinate.right();
                returnValue.add(currentCoordinate);
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    public int manhattanDistance(Coordinate other) {
        return Math.abs(this.Row - other.Row) + Math.abs(this.Column - other.Column);
    }

    public Coordinate leftmostColumn() {
        return new Coordinate(Row, 0);
    }

    public Coordinate topRow() {
        return new Coordinate(0, Column);
    }

    public Coordinate setRow(int row) {
        return new Coordinate(row, Column);
    }

    public Coordinate setColumn(int column) {
        return new Coordinate(Row, column);
    }

    public Coordinate rowModulo(int mod) {
        return new Coordinate(Row % mod, Column);
    }

    public Coordinate columnModulo(int mod) {
        return new Coordinate(Row, Column % mod);
    }

    /**
     * All limits are inclusive.
     */
    public boolean isInRange(int minRow, int minColumn, int maxRow, int maxColumn) {
        return Row >= minRow && Row <= maxRow && Column >= minColumn && Column <= maxColumn;
    }
}
