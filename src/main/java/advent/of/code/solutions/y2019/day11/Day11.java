package advent.of.code.solutions.y2019.day11;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;
import advent.of.code.utils.screenParser.ScreenParser;

@SuppressWarnings("unused")
public class Day11 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        return String.format("%d", executeProgram(program, false).getColoredTilesCount());
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        Grid grid = executeProgram(program, true);
        grid.keepFullColumns();

        int rightmostColumn = grid.getRightmostColumn();
        int leftmostColumn = grid.getLeftmostColumn();

        Boolean[][] screen = new Boolean[6][40];

        for (int row = 0; row < 6; row++) {
            for (int column = 0; column <= rightmostColumn - leftmostColumn; column++) {
                screen[row][column] = grid.getColor(new Coordinate(row, column + leftmostColumn)) == 1;
            }
        }

        return ScreenParser.parseScreen(screen);
    }

    private Grid executeProgram(IntcodeProgram program, boolean firstTileWhite) {
        Grid grid = new Grid();

        Coordinate coordinate = Coordinate.ORIGIN;

        if (firstTileWhite) {
            grid.putColor(coordinate, 1);
        }

        Directions direction = Directions.UP;
        IntcodeProgram.ResultCode resultCode;

        while (true) {
            program.setInput(grid.getColor(coordinate));

            resultCode = program.executeAll();

            if (resultCode == IntcodeProgram.ResultCode.HALTED) {
                break;
            }

            int colorToPaint = program.readOutputAsInt();
            grid.putColor(coordinate, colorToPaint);

            program.executeAll();
            int rotation = program.readOutputAsInt();

            direction = switch (rotation) {
                case 0 -> DirectionUtils.rotateCounterclockwise(direction);
                case 1 -> DirectionUtils.rotateClockwise(direction);
                default -> throw new RuntimeException(String.format("Bad rotation output %d.", rotation));
            };
            coordinate = coordinate.move(direction);
        }

        return grid;
    }
}
