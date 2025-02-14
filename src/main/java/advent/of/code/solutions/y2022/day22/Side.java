package advent.of.code.solutions.y2022.day22;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.Directions;
import lombok.Getter;

public class Side {
    @Getter
    private final Coordinate TopLeftCorner;

    private Side Up = null;
    private Directions UpNewDirection = null;
    private Side Down = null;
    private Directions DownNewDirection = null;
    private Side Left = null;
    private Directions LeftNewDirection = null;
    private Side Right = null;
    private Directions RightNewDirection = null;

    private final Tiles[][] Grid;

    public Side(Coordinate topLeftCorner, Tiles[][] grid) {
        TopLeftCorner = topLeftCorner;
        Grid = grid;
    }

    public void setUp(Side side, Directions newDirection) {
        Up = side;
        UpNewDirection = newDirection;
    }

    public void setDown(Side side, Directions newDirection) {
        Down = side;
        DownNewDirection = newDirection;
    }

    public void setLeft(Side side, Directions newDirection) {
        Left = side;
        LeftNewDirection = newDirection;
    }

    public void setRight(Side side, Directions newDirection) {
        Right = side;
        RightNewDirection = newDirection;
    }

    public boolean isWall(Coordinate coordinate) {
        return MatrixUtils.getMatrixCoord(Grid, coordinate) == Tiles.WALL;
    }

    public boolean isCoordInRange(Coordinate coordinate) {
        return MatrixUtils.isCoordInRange(Grid, coordinate);
    }

    public Side getSide(Directions direction) {
        return switch (direction) {
            case UP -> Up;
            case DOWN -> Down;
            case LEFT -> Left;
            case RIGHT -> Right;
        };
    }

    public Directions getNewDirection(Directions direction) {
        return switch (direction) {
            case UP -> UpNewDirection;
            case DOWN -> DownNewDirection;
            case LEFT -> LeftNewDirection;
            case RIGHT -> RightNewDirection;
        };
    }
}
