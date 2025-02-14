package advent.of.code.solutions.y2019.day24;

class TilesUtils {
    public static char tileToChar(Tiles tile) {
        return switch (tile) {
            case EMPTY -> '.';
            case BUG -> '#';
        };
    }

    public static Tiles charToTile(char ch) {
        return switch (ch) {
            case '#' -> Tiles.BUG;
            case '.' -> Tiles.EMPTY;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }
}
