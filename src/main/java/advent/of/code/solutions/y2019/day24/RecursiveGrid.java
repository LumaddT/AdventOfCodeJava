package advent.of.code.solutions.y2019.day24;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;

class RecursiveGrid {
    private Tiles[][] Grid;
    private static final int GRID_SIDE = 5;

    private RecursiveGrid LevelDown = null;
    private RecursiveGrid LevelUp = null;

    private static final Coordinate MIDDLE_TOP = new Coordinate(1, 2);
    private static final Coordinate MIDDLE_BOTTOM = new Coordinate(3, 2);
    private static final Coordinate MIDDLE_LEFT = new Coordinate(2, 1);
    private static final Coordinate MIDDLE_RIGHT = new Coordinate(2, 3);

    private RecursiveGrid() {
        Grid = new Tiles[GRID_SIDE][GRID_SIDE];
        for (Tiles[] tiles : Grid) {
            Arrays.fill(tiles, Tiles.EMPTY);
        }
    }

    public RecursiveGrid(String inputString) {
        Grid = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .mapToObj(ch -> TilesUtils.charToTile((char) ch))
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);

        if (Grid.length != GRID_SIDE || Grid[0].length != GRID_SIDE) {
            throw new RuntimeException("Bad grid size in input string.");
        }

        this.initLevelUp();
        this.initLevelDown();
    }

    private void initLevelUp() {
        if (LevelUp != null) {
            throw new RuntimeException("Bad call to initLevelUp().");
        }

        LevelUp = new RecursiveGrid();
        LevelUp.LevelDown = this;
    }

    private void initLevelDown() {
        if (LevelDown != null) {
            throw new RuntimeException("Bad call to initLevelDown().");
        }

        LevelDown = new RecursiveGrid();
        LevelDown.LevelUp = this;
    }

    public RecursiveGrid addLevelUp() {
        LevelUp.initLevelUp();
        return LevelUp;
    }

    public RecursiveGrid addLevelDown() {
        LevelDown.initLevelDown();
        return LevelDown;
    }

    public void execute() {
        this.execute(LevelUp.Grid);
    }

    private void execute(Tiles[][] levelUpGrid) {
        Tiles[][] gridCopy = new Tiles[GRID_SIDE][GRID_SIDE];
        for (int row = 0; row < GRID_SIDE; row++) {
            System.arraycopy(Grid[row], 0, gridCopy[row], 0, GRID_SIDE);
        }

        Tiles[][] newGrid = new Tiles[GRID_SIDE][GRID_SIDE];
        for (int row = 0; row < GRID_SIDE; row++) {
            System.arraycopy(Grid[row], 0, newGrid[row], 0, GRID_SIDE);
        }

        boolean isTopBug = levelUpGrid[1][2] == Tiles.BUG;
        boolean isBottomBug = levelUpGrid[3][2] == Tiles.BUG;
        boolean isLeftBug = levelUpGrid[2][1] == Tiles.BUG;
        boolean isRightBug = levelUpGrid[2][3] == Tiles.BUG;

        for (int row = 0; row < GRID_SIDE; row++) {
            for (int column = 0; column < GRID_SIDE; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                int adjacentBugs = 0;

                for (Coordinate adjacent : coordinate.orthogonallyAdjacent()) {
                    if (MatrixUtils.isCoordInRange(Grid, adjacent) && MatrixUtils.getMatrixCoord(Grid, adjacent) == Tiles.BUG) {
                        adjacentBugs++;
                    }
                }

                if (row == 0 && isTopBug) {
                    adjacentBugs++;
                }

                if (row == GRID_SIDE - 1 && isBottomBug) {
                    adjacentBugs++;
                }

                if (column == 0 && isLeftBug) {
                    adjacentBugs++;
                }

                if (column == GRID_SIDE - 1 && isRightBug) {
                    adjacentBugs++;
                }

                if (LevelDown != null) {
                    if (coordinate.equals(MIDDLE_TOP)) {
                        adjacentBugs += LevelDown.countTopRowBugs();
                    }

                    if (coordinate.equals(MIDDLE_BOTTOM)) {
                        adjacentBugs += LevelDown.countBottomRowBugs();
                    }

                    if (coordinate.equals(MIDDLE_LEFT)) {
                        adjacentBugs += LevelDown.countLeftColumnBugs();
                    }

                    if (coordinate.equals(MIDDLE_RIGHT)) {
                        adjacentBugs += LevelDown.countRightColumnBugs();
                    }
                }

                switch (MatrixUtils.getMatrixCoord(Grid, coordinate)) {
                    case BUG -> {
                        if (adjacentBugs == 1) {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.BUG);
                        } else {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.EMPTY);
                        }
                    }
                    case EMPTY -> {
                        if (adjacentBugs == 1 || adjacentBugs == 2) {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.BUG);
                        } else {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.EMPTY);
                        }
                    }
                }
            }
        }

        newGrid[2][2] = Tiles.EMPTY;

        Grid = newGrid;

        if (LevelDown != null) {
            LevelDown.execute(gridCopy);
        }
    }

    private int countTopRowBugs() {
        int count = 0;

        for (int column = 0; column < GRID_SIDE; column++) {
            if (Grid[0][column] == Tiles.BUG) {
                count++;
            }
        }

        return count;
    }

    private int countBottomRowBugs() {
        int count = 0;

        for (int column = 0; column < GRID_SIDE; column++) {
            if (Grid[GRID_SIDE - 1][column] == Tiles.BUG) {
                count++;
            }
        }

        return count;
    }

    private int countLeftColumnBugs() {
        int count = 0;

        for (int row = 0; row < GRID_SIDE; row++) {
            if (Grid[row][0] == Tiles.BUG) {
                count++;
            }
        }

        return count;
    }

    private int countRightColumnBugs() {
        int count = 0;

        for (int row = 0; row < GRID_SIDE; row++) {
            if (Grid[row][GRID_SIDE - 1] == Tiles.BUG) {
                count++;
            }
        }

        return count;
    }

    public int countAllBugs() {
        int levelDownBugs;
        if (LevelDown != null) {
            levelDownBugs = LevelDown.countAllBugs();
        } else {
            levelDownBugs = 0;
        }

        int thisLLevelBugs = MatrixUtils.count(Grid, Tiles.BUG);

        return levelDownBugs + thisLLevelBugs;
    }
}
