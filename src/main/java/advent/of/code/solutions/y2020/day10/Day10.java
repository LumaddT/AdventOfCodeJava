package advent.of.code.solutions.y2020.day10;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements Day {
    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .boxed().toList();

        input = new ArrayList<>(input);

        input.addFirst(0);
        input.add(input.getLast() + 3);

        int oneJumps = 0;
        int threeJumps = 0;

        for (int i = 1; i < input.size(); i++) {
            switch (input.get(i) - input.get(i - 1)) {
                case 1 -> oneJumps++;
                case 3 -> threeJumps++;
            }
        }

        return String.format("%d", oneJumps * threeJumps);
    }

    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .boxed().toList();

        input = new ArrayList<>(input);

        input.addFirst(0);
        input.add(input.getLast() + 3);

        return String.format("%d", countArrangements(input, 0, new HashMap<>()));
    }

    private long countArrangements(List<Integer> adapters, int index, Map<Integer, Long> cache) {
        if (index == adapters.size() - 1) {
            return 1;
        }

        if (cache.containsKey(index)) {
            return cache.get(index);
        }

        long arrangements = 0;

        for (int i = 1; i <= 3; i++) {
            int newIndex = index + i;
            if (newIndex < adapters.size() && adapters.get(newIndex) - adapters.get(index) <= 3) {
                arrangements += countArrangements(adapters, newIndex, cache);
            } else {
                break;
            }
        }

        cache.put(index, arrangements);
        return arrangements;
    }
}
