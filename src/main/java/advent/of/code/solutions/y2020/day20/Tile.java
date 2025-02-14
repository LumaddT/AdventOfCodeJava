package advent.of.code.solutions.y2020.day20;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

class Tile {
    @Getter
    private final int Id;
    private final Boolean[][] FramedImage;
    private final Boolean[][] RealImage;
    private final boolean Flipped;
    private final int ClockwiseRotations;

    private final Map<Directions, Set<Tile>> Matches = new HashMap<>();

    public Tile(String inputString) {
        Id = Integer.parseInt(inputString.split(":\n")[0].substring(5));

        FramedImage = Arrays.stream(inputString.split(":\n")[1].split("\n"))
                .map(l -> l.chars().mapToObj(ch -> ch == '#').toArray(Boolean[]::new))
                .toArray(Boolean[][]::new);
        RealImage = MatrixUtils.removeFrame(FramedImage);

        Flipped = false;
        ClockwiseRotations = 0;

        for (Directions directions : Directions.values()) {
            Matches.put(directions, new HashSet<>());
        }
    }

    public Tile(Tile mainTile, boolean flipped, int clockwiseRotations) {
        if (flipped == mainTile.Flipped && clockwiseRotations == mainTile.ClockwiseRotations || clockwiseRotations < 0 || clockwiseRotations > 3) {
            throw new RuntimeException("Bad \"copy\" constructor call.");
        }

        Flipped = flipped;
        ClockwiseRotations = clockwiseRotations;
        Id = mainTile.Id;

        for (Directions directions : Directions.values()) {
            Matches.put(directions, new HashSet<>());
        }


        Boolean[][] newImage = mainTile.FramedImage;

        if (flipped) {
            newImage = MatrixUtils.flipVertically(newImage);
        }

        for (int i = 0; i < clockwiseRotations; i++) {
            newImage = MatrixUtils.rotateClockwise(newImage);
        }

        FramedImage = newImage;
        RealImage = MatrixUtils.removeFrame(FramedImage);
    }

    public Set<Tile> getAllVersions() {
        Set<Tile> returnValue = new HashSet<>();

        for (int i = 1; i <= 3; i++) {
            returnValue.add(new Tile(this, false, i));
        }

        for (int i = 0; i <= 3; i++) {
            returnValue.add(new Tile(this, true, i));
        }

        return Collections.unmodifiableSet(returnValue);
    }

    public boolean matchesUp(Tile other) {
        for (int column = 0; column < FramedImage[0].length; column++) {
            if (this.FramedImage[0][column] != other.FramedImage[FramedImage.length - 1][column]) {
                return false;
            }
        }

        return true;
    }

    public boolean matchesDown(Tile other) {
        return other.matchesUp(this);

    }

    public boolean matchesRight(Tile other) {
        return other.matchesLeft(this);
    }

    public boolean matchesLeft(Tile other) {
        for (int row = 0; row < FramedImage.length; row++) {
            if (this.FramedImage[row][0] != other.FramedImage[row][FramedImage[0].length - 1]) {
                return false;
            }
        }

        return true;
    }

    public boolean matches(Directions direction, Tile other) {
        return switch (direction) {
            case UP -> this.matchesUp(other);
            case DOWN -> this.matchesDown(other);
            case LEFT -> this.matchesLeft(other);
            case RIGHT -> this.matchesRight(other);
        };
    }

    public void addMatch(Directions direction, Tile other) {
        this.Matches.get(direction).add(other);
        other.Matches.get(DirectionUtils.opposite(direction)).add(this);
    }

    public Set<Tile> getMatchedTiles() {
        return Matches.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public boolean isCorner() {
        return getMatchedTiles().size() == 2;
    }

    public boolean isTopLeftCorner() {
        return Matches.get(Directions.UP).isEmpty()
                && Matches.get(Directions.LEFT).isEmpty()
                && Matches.get(Directions.RIGHT).size() == 1
                && Matches.get(Directions.DOWN).size() == 1;
    }

    public Tile getUp() {
        if (Matches.get(Directions.UP).size() == 1) {
            return Matches.get(Directions.UP).stream().findAny().orElseThrow();
        }

        return null;
    }

    public Tile getDown() {
        if (Matches.get(Directions.DOWN).size() == 1) {
            return Matches.get(Directions.DOWN).stream().findAny().orElseThrow();
        }

        return null;
    }

    public Tile getLeft() {
        if (Matches.get(Directions.LEFT).size() == 1) {
            return Matches.get(Directions.LEFT).stream().findAny().orElseThrow();
        }

        return null;
    }

    public Tile getRight() {
        if (Matches.get(Directions.RIGHT).size() == 1) {
            return Matches.get(Directions.RIGHT).stream().findAny().orElseThrow();
        }

        return null;
    }

    /**
     * Treats the whole tile set as a matrix. It does not check for the validity of `row` and `column`.
     * Null pointer exception if they're out of bounds.
     */
    public Tile get(int row, int column) {
        Tile returnValue = this;

        for (int i = 0; i < row; i++) {
            returnValue = returnValue.getDown();
        }

        for (int i = 0; i < column; i++) {
            returnValue = returnValue.getRight();
        }

        return returnValue;
    }

    public boolean getBit(int row, int column) {
        return RealImage[row][column];
    }
}
