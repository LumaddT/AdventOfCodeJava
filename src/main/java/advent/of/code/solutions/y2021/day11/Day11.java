package advent.of.code.solutions.y2021.day11;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day11 implements Day {
    private static final int STEPS_PART_1 = 100;

    @Override
    public String part1(String inputString) {
        Set<Octopus> octopuses = parseInputString(inputString);

        int total = 0;

        for (int i = 0; i < STEPS_PART_1; i++) {
            octopuses.forEach(Octopus::increaseEnergy);
            total += (int) octopuses.stream().filter(Octopus::isFlashed).count();
            octopuses.forEach(Octopus::resetIfFlashed);
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Set<Octopus> octopuses = parseInputString(inputString);

        int iteration = 0;

        while (true) {
            iteration++;

            octopuses.forEach(Octopus::increaseEnergy);
            if (octopuses.stream().filter(Octopus::isFlashed).count() == octopuses.size()) {
                return String.format("%d", iteration);
            }

            octopuses.forEach(Octopus::resetIfFlashed);
        }
    }

    private Set<Octopus> parseInputString(String inputString) {
        Integer[][] inputGrid = ParsingUtils.toIntegerGrid(inputString);

        Octopus[][] octopusesGrid = Arrays.stream(inputGrid).map(
                        l -> Arrays.stream(l)
                                .map(Octopus::new)
                                .toArray(Octopus[]::new))
                .toArray(Octopus[][]::new);

        Set<Octopus> returnValue = new HashSet<>();

        for (int row = 0; row < octopusesGrid.length; row++) {
            for (int column = 0; column < octopusesGrid[row].length; column++) {
                Coordinate thisCoordinate = new Coordinate(row, column);
                Octopus thisOctopus = MatrixUtils.getMatrixCoord(octopusesGrid, thisCoordinate);

                returnValue.add(thisOctopus);

                for (Coordinate thatCoordinate : thisCoordinate.adjacent()) {
                    if (MatrixUtils.isCoordInRange(octopusesGrid, thatCoordinate)) {
                        Octopus thatOctopus = MatrixUtils.getMatrixCoord(octopusesGrid, thatCoordinate);
                        thisOctopus.addNeighbor(thatOctopus);
                    }
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }
}
