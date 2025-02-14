package advent.of.code.solutions.y2021.day22;

import advent.of.code.solutions.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day22 implements Day {
    private static final int LIMIT_PART_1 = 50;

    @Override
    public String part1(String inputString) {
        List<Action> input = parseInputString(inputString);

        return String.format("%d", countActive(input, true));
    }

    @Override
    public String part2(String inputString) {
        List<Action> input = parseInputString(inputString);

        return String.format("%d", countActive(input, false));
    }

    private List<Action> parseInputString(String inputString) {
        List<Action> returnValue = new ArrayList<>();

        String regex = "(on|off) x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        while (matcher.find()) {
            returnValue.add(new Action(
                    matcher.group(1).equals("on"),
                    new Range(
                            Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4)),
                            Integer.parseInt(matcher.group(5)),
                            Integer.parseInt(matcher.group(6)),
                            Integer.parseInt(matcher.group(7))
                    )
            ));
        }

        return Collections.unmodifiableList(returnValue);
    }

    private long countActive(List<Action> input, boolean isPart1) {
        Set<Range> activeRanges = new HashSet<>();
        for (Action action : input) {
            if (isPart1 && Math.abs(action.range().minX()) > LIMIT_PART_1) {
                continue;
            }

            Range range = action.range();
            activeRanges = activeRanges.stream().flatMap(r -> r.remove(range).stream()).collect(Collectors.toSet());

            if (action.isOn()) {
                activeRanges.add(range);
            }
        }

        return activeRanges.stream().mapToLong(Range::size).sum();
    }
}
