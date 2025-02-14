package advent.of.code.solutions.y2019.day03;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ntuples.Tuple;

import java.util.*;

@SuppressWarnings("unused")
public class Day03 implements Day {
    @Override
    public String part1(String inputString) {
        Tuple<List<Instruction>, List<Instruction>> input = parseInputString(inputString);
        List<Instruction> wireOneInstructions = input.First();
        List<Instruction> wireTwoInstructions = input.Second();

        Coordinate coordinate = Coordinate.ORIGIN;
        Set<Coordinate> wireOneCoordinates = new HashSet<>();

        for (Instruction instruction : wireOneInstructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                coordinate = coordinate.move(instruction.getDirection());
                wireOneCoordinates.add(coordinate);
            }
        }

        int closestIntersection = Integer.MAX_VALUE;
        coordinate = Coordinate.ORIGIN;

        for (Instruction instruction : wireTwoInstructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                coordinate = coordinate.move(instruction.getDirection());

                if (wireOneCoordinates.contains(coordinate)) {
                    int currentDistance = coordinate.manhattanDistance(Coordinate.ORIGIN);

                    if (currentDistance < closestIntersection) {
                        closestIntersection = currentDistance;
                    }
                }
            }
        }

        return String.format("%d", closestIntersection);
    }

    @Override
    public String part2(String inputString) {
        Tuple<List<Instruction>, List<Instruction>> input = parseInputString(inputString);
        List<Instruction> wireOneInstructions = input.First();
        List<Instruction> wireTwoInstructions = input.Second();

        Map<Coordinate, Integer> wireOne = new HashMap<>();
        Coordinate coordinate = Coordinate.ORIGIN;
        int steps = 1;

        for (Instruction instruction : wireOneInstructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                coordinate = coordinate.move(instruction.getDirection());
                wireOne.putIfAbsent(coordinate, steps);
                steps++;
            }
        }

        int minSteps = Integer.MAX_VALUE;
        steps = 1;

        coordinate = Coordinate.ORIGIN;

        for (Instruction instruction : wireTwoInstructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                coordinate = coordinate.move(instruction.getDirection());

                if (wireOne.containsKey(coordinate)) {
                    int combinedSteps = steps + wireOne.get(coordinate);
                    if (combinedSteps < minSteps) {
                        minSteps = combinedSteps;
                    }
                }

                steps++;
            }

        }

        return String.format("%d", minSteps);
    }

    private Tuple<List<Instruction>, List<Instruction>> parseInputString(String inputString) {
        String[] inputSplit = inputString.split("\n");

        List<Instruction> wire1 = Arrays.stream(inputSplit[0].split(",")).map(Instruction::new).toList();
        List<Instruction> wire2 = Arrays.stream(inputSplit[1].split(",")).map(Instruction::new).toList();

        return new Tuple<>(wire1, wire2);
    }
}
