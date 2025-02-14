package advent.of.code.utils.screenParser;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Collection;

public class ScreenParser {
    public static String parseScreen(Boolean[][] screen) {
        if (screen.length % 6 != 0) {
            throw new RuntimeException(String.format(
                    "Tried to parse a screen with %d rows. Screen height must be exactly 6.", screen[0].length));
        }

        if (screen[0].length % 5 != 0) {
            throw new RuntimeException(String.format(
                    "Tried to parse a screen with %d columns. Screen width must be multiple of 5.", screen[0].length));
        }

        int lettersAmount = screen[0].length / 5;

        Boolean[][][] splitScreen = new Boolean[lettersAmount][6][5];

        for (int i = 0; i < lettersAmount; i++) {
            splitScreen[i] = MatrixUtils.subSquare(screen, new Coordinate(0, i * 5), 6, 5);
        }

        StringBuilder returnValue = new StringBuilder();

        for (Boolean[][] screenLetter : splitScreen) {
            for (Boolean[][] potentialLetter : Letters.LETTERS_LIST) {
                if (MatrixUtils.equals(screenLetter, potentialLetter)) {
                    returnValue.append(Letters.TABLE_TO_CHAR.get(potentialLetter));
                    break;
                }
            }
        }

        return returnValue.toString();
    }

    public static String parseScreen(Collection<Coordinate> coordinates) {
        int minRow = coordinates.stream().mapToInt(Coordinate::Row).min().orElseThrow();
        int maxRow = coordinates.stream().mapToInt(Coordinate::Row).max().orElseThrow();

        int maxColumn = coordinates.stream().mapToInt(Coordinate::Column).max().orElseThrow();

        if (minRow != 0 || maxRow != 5) {
            throw new RuntimeException("The coordinates provided for the screen are not vertically aligned.");
        }

        int columns = maxColumn - maxColumn % 5 + 5;

        Boolean[][] screen = new Boolean[6][columns];
        MatrixUtils.fillMatrix(screen, false);

        for (Coordinate coordinate : coordinates) {
            MatrixUtils.setMatrixCoord(screen, coordinate, true);
        }

        return parseScreen(screen);
    }
}
