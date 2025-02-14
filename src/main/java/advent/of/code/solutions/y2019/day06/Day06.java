package advent.of.code.solutions.y2019.day06;

import advent.of.code.solutions.Day;
import advent.of.code.utils.SetUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day06 implements Day {
    @Override
    public String part1(String inputString) {
        Set<CelestialObject> input = parseInputString(inputString);

        return String.format("%d", input.stream()
                .mapToInt(c -> c.getDirectAndIndirectOrbits().size())
                .sum());
    }

    @Override
    public String part2(String inputString) {
        Set<CelestialObject> input = parseInputString(inputString);

        CelestialObject you = input.stream().filter(c -> c.getName().equals("YOU")).findAny().orElseThrow();
        CelestialObject san = input.stream().filter(c -> c.getName().equals("SAN")).findAny().orElseThrow();

        Set<CelestialObject> sanDirectAndIndirectOrbits = san.getDirectAndIndirectOrbits();
        Set<CelestialObject> youDirectAndIndirectOrbits = you.getDirectAndIndirectOrbits();

        Set<CelestialObject> orbitsInCommon = SetUtils.intersection(sanDirectAndIndirectOrbits, youDirectAndIndirectOrbits);

        return String.format("%d", sanDirectAndIndirectOrbits.size() + youDirectAndIndirectOrbits.size() - 2 * orbitsInCommon.size());
    }

    private Set<CelestialObject> parseInputString(String inputString) {
        String[][] inputSplit = Arrays.stream(inputString.split("\n"))
                .map(s -> s.split("\\)"))
                .toArray(String[][]::new);
        Map<String, CelestialObject> returnValue = new HashMap<>();

        for (String[] line : inputSplit) {
            returnValue.putIfAbsent(line[0], new CelestialObject(line[0]));
            returnValue.putIfAbsent(line[1], new CelestialObject(line[1]));
        }

        for (String[] line : inputSplit) {
            returnValue.get(line[1]).setOrbits(returnValue.get(line[0]));
        }

        return new HashSet<>(returnValue.values());
    }
}
