package advent.of.code.solutions.y2018.day25;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate4;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day25 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Coordinate4> input = parseInputSet(inputString);
        Map<Coordinate4, Set<Coordinate4>> coordinatesToSet = new HashMap<>();
        int constellations = input.size();

        for (Coordinate4 coordinate : input) {
            Set<Coordinate4> newConstellation = new HashSet<>();
            newConstellation.add(coordinate);
            coordinatesToSet.put(coordinate, newConstellation);
        }

        for (Coordinate4 coordinate : input) {
            for (Coordinate4 other : input) {
                if (coordinatesToSet.get(coordinate).contains(other)) {
                    continue;
                }

                if (coordinate.distance(other) <= 3) {
                    coordinatesToSet.get(coordinate).addAll(coordinatesToSet.get(other));
                    for (Coordinate4 otherFriend : coordinatesToSet.get(other)) {
                        coordinatesToSet.put(otherFriend, coordinatesToSet.get(coordinate));
                    }
                    constellations--;
                }
            }
        }

        return String.format("%d", constellations);
    }

    @Override
    public String part2(String inputString) {
        return "No part 2 on Christmas.";
    }

    private Set<Coordinate4> parseInputSet(String inputString) {
        Set<Coordinate4> returnValue = new HashSet<>();
        Set<String[]> splitInput = Arrays.stream(inputString.split("\n")).map(l -> l.split(",")).collect(Collectors.toSet());
        for (String[] line : splitInput) {
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            int z = Integer.parseInt(line[2]);
            int t = Integer.parseInt(line[3]);
            returnValue.add(new Coordinate4(x, y, z, t));
        }

        return returnValue;
    }
}
