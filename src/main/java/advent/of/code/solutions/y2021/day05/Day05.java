package advent.of.code.solutions.y2021.day05;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ntuples.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day05 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Tuple<Coordinate, Coordinate>> input = parseInputString(inputString).stream()
                .filter(t -> t.First().Row() == t.Second().Row() || t.First().Column() == t.Second().Column())
                .collect(Collectors.toSet());

        return String.format("%d", seenTwiceAmount(input));
    }

    @Override
    public String part2(String inputString) {
        Set<Tuple<Coordinate, Coordinate>> input = parseInputString(inputString);

        return String.format("%d", seenTwiceAmount(input));
    }

    private Set<Tuple<Coordinate, Coordinate>> parseInputString(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(l -> Arrays.stream(l.split(" -> "))
                        .map(c -> Arrays.stream(c.split(","))
                                .map(Integer::parseInt)
                                .toArray(Integer[]::new))
                        .toArray(Integer[][]::new))
                .map(l -> new Tuple<>(new Coordinate(l[0][1], l[0][0]), new Coordinate(l[1][1], l[1][0])))
                .collect(Collectors.toSet());
    }

    private Set<Coordinate> formFullLine(Tuple<Coordinate, Coordinate> edges) {
        Coordinate first = edges.First();
        Coordinate second = edges.Second();

        Coordinate movement = new Coordinate((int) Math.signum(first.Row() - second.Row()), (int) Math.signum(first.Column() - second.Column()));

        Coordinate currentCoordinate = first;
        Set<Coordinate> returnValue = new HashSet<>();

        while (!currentCoordinate.equals(second)) {
            returnValue.add(currentCoordinate);
            currentCoordinate = currentCoordinate.subtract(movement);
        }

        returnValue.add(second);

        return Collections.unmodifiableSet(returnValue);
    }

    private int seenTwiceAmount(Set<Tuple<Coordinate, Coordinate>> input) {
        Set<Set<Coordinate>> fullLines = input.stream()
                .map(this::formFullLine)
                .collect(Collectors.toSet());

        Set<Coordinate> seenOnce = new HashSet<>();
        Set<Coordinate> seenTwice = new HashSet<>();

        for (Set<Coordinate> line : fullLines) {
            for (Coordinate coordinate : line) {
                if (seenOnce.contains(coordinate)) {
                    seenTwice.add(coordinate);
                } else {
                    seenOnce.add(coordinate);
                }
            }
        }

        return seenTwice.size();
    }
}
