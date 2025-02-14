package advent.of.code.solutions.y2020.day12;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;
import advent.of.code.utils.ntuples.Tuple;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day12 implements Day {
    private static final Directions INITIAL_SHIP_DIRECTION = Directions.RIGHT;

    private static final Coordinate INITIAL_WAYPOINT_COORDINATE = new Coordinate(-1, 10);

    @Override
    public String part1(String inputString) {
        List<Instruction> input = Arrays.stream(inputString.split("\n"))
                .map(l -> new Instruction(l.charAt(0), Integer.parseInt(l.substring(1))))
                .toList();

        Coordinate shipCoordinate = Coordinate.ORIGIN;
        Directions shipDirection = INITIAL_SHIP_DIRECTION;

        for (Instruction instruction : input) {
            Tuple<Coordinate, Directions> result = performInstructionPart1(instruction, shipCoordinate, shipDirection);
            shipCoordinate = result.First();
            shipDirection = result.Second();
        }

        return String.format("%d", shipCoordinate.manhattanDistance(Coordinate.ORIGIN));
    }

    @Override
    public String part2(String inputString) {
        List<Instruction> input = Arrays.stream(inputString.split("\n"))
                .map(l -> new Instruction(l.charAt(0), Integer.parseInt(l.substring(1))))
                .toList();

        Coordinate shipCoordinate = Coordinate.ORIGIN;
        Coordinate waypointCoordinate = INITIAL_WAYPOINT_COORDINATE;

        for (Instruction instruction : input) {
            Tuple<Coordinate, Coordinate> result = performInstructionPart2(instruction, shipCoordinate, waypointCoordinate);
            shipCoordinate = result.First();
            waypointCoordinate = result.Second();
        }

        return String.format("%d", shipCoordinate.manhattanDistance(Coordinate.ORIGIN));
    }

    private Tuple<Coordinate, Directions> performInstructionPart1(Instruction instruction, Coordinate currentCoordinate, Directions currentDirection) {
        int amount = switch (instruction.Direction()) {
            case 'N', 'S', 'W', 'E', 'F' -> instruction.Amount();
            case 'L', 'R' -> instruction.Amount() / 90;
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        Coordinate newCoordinate = switch (instruction.Direction()) {
            case 'N' -> currentCoordinate.up(amount);
            case 'S' -> currentCoordinate.down(amount);
            case 'W' -> currentCoordinate.left(amount);
            case 'E' -> currentCoordinate.right(amount);
            case 'F' -> currentCoordinate.move(currentDirection, amount);
            case 'L', 'R' -> currentCoordinate;
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        Directions newDirection = switch (instruction.Direction()) {
            case 'N', 'S', 'W', 'E', 'F' -> currentDirection;
            case 'L' -> DirectionUtils.rotateCounterclockwise(currentDirection, amount);
            case 'R' -> DirectionUtils.rotateClockwise(currentDirection, amount);
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        return new Tuple<>(newCoordinate, newDirection);
    }

    private Tuple<Coordinate, Coordinate> performInstructionPart2(Instruction instruction, Coordinate shipCoordinate, Coordinate waypointCoordinate) {
        int amount = switch (instruction.Direction()) {
            case 'N', 'S', 'W', 'E', 'F' -> instruction.Amount();
            case 'L', 'R' -> instruction.Amount() / 90;
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        Coordinate newWaypointCoordinate = switch (instruction.Direction()) {
            case 'N' -> waypointCoordinate.up(amount);
            case 'S' -> waypointCoordinate.down(amount);
            case 'W' -> waypointCoordinate.left(amount);
            case 'E' -> waypointCoordinate.right(amount);
            case 'L', 'R' -> rotateWaypoint(waypointCoordinate, instruction.Direction(), amount);
            case 'F' -> waypointCoordinate;
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        Coordinate newShipCoordinate = switch (instruction.Direction()) {
            case 'N', 'S', 'W', 'E', 'L', 'R' -> shipCoordinate;
            case 'F' -> moveShip(shipCoordinate, waypointCoordinate, amount);
            default -> throw new IllegalStateException("Unexpected value: " + instruction.Direction());
        };

        return new Tuple<>(newShipCoordinate, newWaypointCoordinate);
    }

    private Coordinate rotateWaypoint(Coordinate waypointCoordinate, char direction, int amount) {
        for (int i = 0; i < amount; i++) {
            if (direction == 'L') {
                waypointCoordinate = new Coordinate(-waypointCoordinate.Column(), waypointCoordinate.Row());
            } else if (direction == 'R') {
                waypointCoordinate = new Coordinate(waypointCoordinate.Column(), -waypointCoordinate.Row());
            } else {
                throw new RuntimeException(String.format("Bad direction %c.", direction));
            }
        }

        return waypointCoordinate;
    }

    private Coordinate moveShip(Coordinate shipCoordinate, Coordinate waypointCoordinate, int amount) {
        for (int i = 0; i < amount; i++) {
            shipCoordinate = shipCoordinate.add(waypointCoordinate);
        }

        return shipCoordinate;
    }
}
