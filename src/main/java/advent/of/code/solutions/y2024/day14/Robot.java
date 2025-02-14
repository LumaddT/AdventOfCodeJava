package advent.of.code.solutions.y2024.day14;

import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.RegexUtils;

import java.util.regex.Matcher;

class Robot {
    private final int InitialRow;
    private final int InitialColumn;
    private final int SpeedRow;
    private final int SpeedColumn;

    public Robot(String inputString) {
        String regex = "^p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)$";
        Matcher matcher = RegexUtils.match(regex, inputString, 4);

        InitialColumn = Integer.parseInt(matcher.group(1));
        InitialRow = Integer.parseInt(matcher.group(2));
        SpeedColumn = Integer.parseInt(matcher.group(3));
        SpeedRow = Integer.parseInt(matcher.group(4));
    }

    public Coordinate move(int width, int height, int iterations) {
        int finalX = (InitialColumn + SpeedColumn * iterations) % width;
        while (finalX < 0) {
            finalX += width;
        }

        int finalY = (InitialRow + SpeedRow * iterations) % height;
        while (finalY < 0) {
            finalY += height;
        }

        return new Coordinate(finalY, finalX);
    }
}
