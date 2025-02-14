package advent.of.code.solutions.y2020.day05;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day05 implements Day {
    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .map(l -> new Tuple<>(l.substring(0, 7), l.substring(7, 10)))
                .mapToInt(this::seatToNumber)
                .max()
                .orElseThrow());
    }

    @Override
    public String part2(String inputString) {
        Set<Integer> seatNumbers = Arrays.stream(inputString.split("\n"))
                .map(l -> new Tuple<>(l.substring(0, 7), l.substring(7, 10)))
                .map(this::seatToNumber)
                .collect(Collectors.toSet());

        int minSeat = seatNumbers.stream().mapToInt(n -> n).min().orElseThrow();
        int maxSeat = seatNumbers.stream().mapToInt(n -> n).max().orElseThrow();

        for (int i = minSeat; i < maxSeat; i++) {
            if (!seatNumbers.contains(i) && seatNumbers.contains(i - 1) && seatNumbers.contains(i + 1)) {
                return String.format("%d", i);
            }
        }

        throw new RuntimeException("Bad solution");
    }

    private int seatToNumber(Tuple<String, String> seat) {
        int minRow = sequenceToNumber(seat.First(), 'F', 'B');
        int minColumn = sequenceToNumber(seat.Second(), 'L', 'R');

        return minRow * 8 + minColumn;
    }

    private int sequenceToNumber(String sequence, char lower, char higher) {
        int min = 0;
        int max = (int) Math.pow(2, sequence.length());

        for (char ch : sequence.toCharArray()) {
            if (ch == lower) {
                max -= (max - min) / 2;
            } else if (ch == higher) {
                min += (max - min) / 2;
            } else {
                throw new RuntimeException(String.format("Illegal character %c.", ch));
            }
        }

        return min;
    }
}
