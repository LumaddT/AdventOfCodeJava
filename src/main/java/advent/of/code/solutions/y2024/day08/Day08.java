package advent.of.code.solutions.y2024.day08;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day08 implements Day {
    @Override
    public String part1(String inputString) {
        char[][] input = ParsingUtils.toGrid(inputString);
        Map<Character, List<Coordinate>> antennas = parseAntennas(input);

        return String.format("%d", findResonancePoints(antennas, input.length, input[0].length, false));
    }

    @Override
    public String part2(String inputString) {
        char[][] input = ParsingUtils.toGrid(inputString);
        Map<Character, List<Coordinate>> antennas = parseAntennas(input);

        return String.format("%d", findResonancePoints(antennas, input.length, input[0].length, true));
    }

    private Map<Character, List<Coordinate>> parseAntennas(char[][] input) {
        Map<Character, List<Coordinate>> returnValue = new HashMap<>();

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (input[row][column] != '.') {
                    if (!returnValue.containsKey(input[row][column])) {
                        returnValue.put(input[row][column], new ArrayList<>());
                    }

                    returnValue.get(input[row][column]).add(new Coordinate(row, column));
                }
            }
        }

        returnValue.replaceAll((k, v) -> Collections.unmodifiableList(returnValue.get(k)));

        return Collections.unmodifiableMap(returnValue);
    }

    private int findResonancePoints(Map<Character, List<Coordinate>> antennas, int rows, int columns, boolean isPart2) {
        Set<Coordinate> resonancePoints = new HashSet<>();

        for (List<Coordinate> coordinates : antennas.values()) {
            for (int i = 0; i < coordinates.size() - 1; i++) {
                for (int k = i + 1; k < coordinates.size(); k++) {
                    Coordinate first = coordinates.get(i);
                    Coordinate second = coordinates.get(k);

                    int rowDiff = second.Row() - first.Row();
                    int columnDiff = second.Column() - first.Column();

                    int iteration = isPart2 ? 0 : 1;
                    while (true) {
                        int resonanceTopRow = first.Row() - rowDiff * iteration;
                        int resonanceTopColumn = first.Column() - columnDiff * iteration;

                        iteration++;

                        if (resonanceTopRow >= 0 && resonanceTopColumn >= 0 && resonanceTopRow < rows && resonanceTopColumn < columns) {
                            resonancePoints.add(new Coordinate(resonanceTopRow, resonanceTopColumn));
                        } else {
                            break;
                        }

                        if (!isPart2) {
                            break;
                        }
                    }

                    iteration = isPart2 ? 0 : 1;
                    while (true) {
                        int resonanceBottomRow = second.Row() + rowDiff * iteration;
                        int resonanceBottomColumn = second.Column() + columnDiff * iteration;

                        iteration++;

                        if (resonanceBottomRow >= 0 && resonanceBottomColumn >= 0 && resonanceBottomRow < rows && resonanceBottomColumn < columns) {
                            resonancePoints.add(new Coordinate(resonanceBottomRow, resonanceBottomColumn));
                        } else {
                            break;
                        }

                        if (!isPart2) {
                            break;
                        }
                    }
                }
            }
        }

        return resonancePoints.size();
    }
}
