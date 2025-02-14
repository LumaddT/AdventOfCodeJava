package advent.of.code.solutions.y2020.day16;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Triple;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day16 implements Day {
    @Override
    public String part1(String inputString) {
        Triple<List<Range>, List<Integer>, Set<List<Integer>>> input = parseInputString(inputString);
        List<Range> ranges = input.First();
        Set<List<Integer>> otherTickets = input.Third();

        int total = 0;

        for (List<Integer> ticket : otherTickets) {
            for (int value : ticket) {
                boolean isValid = false;
                for (Range range : ranges) {
                    if (range.containsValue(value)) {
                        isValid = true;
                        break;
                    }
                }

                if (!isValid) {
                    total += value;
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Triple<List<Range>, List<Integer>, Set<List<Integer>>> input = parseInputString(inputString);
        List<Range> ranges = input.First();
        List<Integer> yourTicket = input.Second();
        Set<List<Integer>> otherTickets = input.Third().stream().filter(t -> isValid(t, ranges)).collect(Collectors.toSet());

        Map<Range, Set<Integer>> rangesToPossibleIndexes = new HashMap<>();

        for (Range range : ranges) {
            rangesToPossibleIndexes.put(range, new HashSet<>());
            for (int i = 0; i < yourTicket.size(); i++) {
                if (isRangeValidForIndex(range, i, otherTickets)) {
                    rangesToPossibleIndexes.get(range).add(i);
                }
            }
        }

        Set<Integer> confirmedIndexes = new HashSet<>();
        while (rangesToPossibleIndexes.values().stream().mapToInt(Set::size).max().orElseThrow() != 1) {
            for (Range range : ranges) {
                if (rangesToPossibleIndexes.get(range).size() == 1) {
                    confirmedIndexes.add(rangesToPossibleIndexes.get(range).stream().findAny().orElseThrow());
                } else {
                    rangesToPossibleIndexes.get(range).removeAll(confirmedIndexes);
                }
            }
        }

        long total = 1;

        for (Range range : ranges.subList(0, 6)) {
            total *= yourTicket.get(rangesToPossibleIndexes.get(range).stream().findAny().orElseThrow());
        }

        return String.format("%d", total);
    }

    private Triple<List<Range>, List<Integer>, Set<List<Integer>>> parseInputString(String inputString) {
        String[] inputSplit = inputString.split("\n\n");
        return new Triple<>(parseRanges(inputSplit[0]), parseYourTicket(inputSplit[1]), parseOtherTickets(inputSplit[2]));
    }

    private List<Range> parseRanges(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(l -> Arrays.stream(l.split(": ")[1].split(" or |-"))
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new))
                .map(l -> new Range(l[0], l[1], l[2], l[3]))
                .toList();
    }

    private List<Integer> parseYourTicket(String inputString) {
        return Arrays.stream(inputString.split("\n")[1].split(",")).map(Integer::parseInt).toList();
    }

    private Set<List<Integer>> parseOtherTickets(String inputString) {
        String[] inputSplit = inputString.split("\n");

        Set<List<Integer>> returnValue = new HashSet<>();

        for (int i = 1; i < inputSplit.length; i++) {
            returnValue.add(Arrays.stream(inputSplit[i].split(",")).map(Integer::parseInt).toList());
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private boolean isValid(List<Integer> ticket, List<Range> ranges) {
        for (int value : ticket) {
            boolean isValid = false;
            for (Range range : ranges) {
                if (range.containsValue(value)) {
                    isValid = true;
                    break;
                }
            }

            if (!isValid) {
                return false;
            }
        }

        return true;
    }

    private boolean isRangeValidForIndex(Range range, int index, Set<List<Integer>> tickets) {
        for (List<Integer> ticket : tickets) {
            if (!range.containsValue(ticket.get(index))) {
                return false;
            }
        }

        return true;
    }
}
