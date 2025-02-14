package advent.of.code.utils;

import advent.of.code.utils.coordinates.Coordinate;

import java.lang.reflect.Array;
import java.util.*;

public class MatrixUtils {
    public static <T> boolean isCoordInRange(T[][] matrix, Coordinate coord) {
        int row = coord.Row();
        int column = coord.Column();

        return row >= 0 && column >= 0 && row < matrix.length && column < matrix[row].length;
    }

    public static <T> T getMatrixCoord(T[][] matrix, Coordinate coord) {
        int row = coord.Row();
        int column = coord.Column();

        if (row < 0 || column < 0 || row >= matrix.length || column >= matrix[row].length) {
            throw new IndexOutOfBoundsException(String.format("Coordinate %s is out of bounds for matrix.", coord));
        }

        return matrix[row][column];
    }

    public static <T> T getMatrixCoord(T[][] matrix, Coordinate coord, T fallbackValue) {
        int row = coord.Row();
        int column = coord.Column();

        if (row < 0 || column < 0 || row >= matrix.length || column >= matrix[row].length) {
            return fallbackValue;
        }

        return matrix[row][column];
    }

    public static <T> void setMatrixCoord(T[][] matrix, Coordinate coord, T value) {
        int row = coord.Row();
        int column = coord.Column();

        if (row < 0 || column < 0 || row >= matrix.length || column >= matrix[row].length) {
            throw new IllegalArgumentException("coord is outside of legal range.");
        }

        matrix[row][column] = value;
    }

    /**
     * Finds the coordinates of the first occurrence (top to bottom, left to right) of a value in the matrix.
     * Equality is checked via .equals()
     *
     * @return A Coordinate object with the location of the first occurrence of value, or null if the
     * element is not found.
     */
    public static <T> Coordinate coordinateOf(T[][] matrix, T value) {
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[row].length; column++) {
                if (Objects.equals(matrix[row][column], value)) {
                    return new Coordinate(row, column);
                }
            }
        }

        return null;
    }

    /**
     * Finds the coordinates of the last occurrence (bottom to top, right to left) of a value in the matrix.
     * Equality is checked via .equals()
     *
     * @return A Coordinate object with the location of the first occurrence of value, or null if the
     * element is not found.
     */
    public static <T> Coordinate lastCoordinateOf(T[][] matrix, T value) {
        for (int row = matrix.length - 1; row >= 0; row--) {
            for (int column = matrix[row].length - 1; column >= 0; column--) {
                if (Objects.equals(matrix[row][column], value)) {
                    return new Coordinate(row, column);
                }
            }
        }

        return null;
    }

    public static <T> void fillMatrix(T[][] matrix, T value) {
        for (T[] ts : matrix) {
            Arrays.fill(ts, value);
        }
    }

    public static <T> Set<T> toSet(T[][] matrix) {
        Set<T> returnValue = new HashSet<>();

        for (T[] row : matrix) {
            returnValue.addAll(Arrays.asList(row));
        }

        return returnValue;
    }

    public static <T> int count(T[][] matrix, T value) {
        int count = 0;

        for (T[] row : matrix) {
            for (T element : row) {
                if (Objects.equals(element, value)) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Equality is checked by comparing via .equals() all elements in the same coordinate.
     */
    public static <T> boolean equals(T[][] first, T[][] second) {
        if (first.length != second.length) {
            return false;
        }

        for (int row = 0; row < first.length; row++) {
            if (first[row].length != second[row].length) {
                return false;
            }

            for (int column = 0; column < first[row].length; column++) {
                if (!first[row][column].equals(second[row][column])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Based on Arrays.copyOf and some help from some random AI.
     */
    private static <T> T[][] emptyMatrix(T[][] matrix, int rows, int columns) {
        //noinspection unchecked
        Class<? extends T[][]> componentType = (Class<? extends T[][]>) matrix.getClass().getComponentType().getComponentType();
        //noinspection unchecked
        return (T[][]) Array.newInstance(componentType, rows, columns);
    }

    private static <T> T[][] emptyMatrix(T[][] matrix) {
        return emptyMatrix(matrix, matrix.length, matrix[0].length);
    }

    public static <T> T[][] subSquare(T[][] matrix, Coordinate topLeft, Coordinate bottomRight) {
        if (!isCoordInRange(matrix, topLeft) || !isCoordInRange(matrix, bottomRight)) {
            throw new RuntimeException("Coordinates of subSquare limits are outside of matrix range.");
        }

        if (topLeft.Row() > bottomRight.Row() || topLeft.Column() > bottomRight.Column()) {
            throw new RuntimeException("topLeft and BottomRight coordinates are in a bad relative position.");
        }

        int rows = bottomRight.Row() - topLeft.Row() + 1;
        int columns = bottomRight.Column() - topLeft.Column() + 1;
        T[][] returnValue = emptyMatrix(matrix, rows, columns);

        for (int row = topLeft.Row(); row <= bottomRight.Row(); row++) {
            System.arraycopy(matrix[row], topLeft.Column(), returnValue[row - topLeft.Row()], 0, columns);
        }

        return returnValue;
    }

    public static <T> T[][] subSquare(T[][] matrix, Coordinate topLeft, int rows, int columns) {
        return subSquare(matrix, topLeft, new Coordinate(topLeft.Row() + rows - 1, topLeft.Column() + columns - 1));
    }

    public static <T> T[][] addFrame(T[][] matrix, T value) {
        T[][] returnValue = emptyMatrix(matrix, matrix.length + 2, matrix[0].length + 2);

        for (int i = 0; i < returnValue.length; i++) {
            returnValue[i][0] = value;
            returnValue[i][returnValue[0].length - 1] = value;
        }

        for (int i = 0; i < returnValue[0].length; i++) {
            returnValue[0][i] = value;
            returnValue[returnValue.length - 1][i] = value;
        }

        for (int row = 0; row < matrix.length; row++) {
            System.arraycopy(matrix[row], 0, returnValue[row + 1], 1, matrix[row].length);
        }

        return returnValue;
    }

    public static <T> T[][] removeFrame(T[][] matrix) {
        Coordinate topLeft = new Coordinate(1, 1);
        int rows = matrix.length - 2;
        int columns = matrix[0].length - 2;
        return subSquare(matrix, topLeft, rows, columns);
    }

    public static <T> T[][] flipVertically(T[][] matrix) {
        T[][] returnValue = emptyMatrix(matrix);

        for (int row = 0; row < matrix.length; row++) {
            for (int mainColumn = 0; mainColumn < matrix[0].length; mainColumn++) {
                returnValue[row][matrix[0].length - mainColumn - 1] = matrix[row][mainColumn];
            }
        }

        return returnValue;
    }

    public static <T> T[][] rotateClockwise(T[][] matrix) {
        T[][] returnValue = emptyMatrix(matrix);

        for (int mainRow = 0; mainRow < matrix.length; mainRow++) {
            for (int mainColumn = 0; mainColumn < matrix[0].length; mainColumn++) {
                returnValue[mainColumn][matrix.length - mainRow - 1] = matrix[mainRow][mainColumn];
            }
        }

        return returnValue;
    }
}
