package advent.of.code.solutions.y2021.day04;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day04 implements Day {
    @Override
    public String part1(String inputString) {
        int[] numbers = Arrays.stream(inputString.split("\n\n")[0].split(",")).mapToInt(Integer::parseInt).toArray();
        Set<Board> boards = parseBoards(inputString);
        boards.forEach(b -> b.playBingo(numbers));

        List<Board> sortedBoards = boards.stream().sorted(Comparator.comparingInt(Board::getNumbersToWin)).toList();
        Board winner = sortedBoards.getFirst();

        return String.format("%d", winner.getWinnerNumber() * winner.getSumOfUnmarkedNumbers());
    }

    @Override
    public String part2(String inputString) {
        int[] numbers = Arrays.stream(inputString.split("\n\n")[0].split(",")).mapToInt(Integer::parseInt).toArray();
        Set<Board> boards = parseBoards(inputString);
        boards.forEach(b -> b.playBingo(numbers));

        List<Board> sortedBoards = boards.stream().sorted(Comparator.comparingInt(Board::getNumbersToWin)).toList();
        Board loser = sortedBoards.getLast();

        return String.format("%d", loser.getWinnerNumber() * loser.getSumOfUnmarkedNumbers());
    }

    private Set<Board> parseBoards(String inputString) {
        String[] inputSplit = inputString.split("\n\n");
        Set<Board> boards = new HashSet<>();
        for (int i = 1; i < inputSplit.length; i++) {
            boards.add(new Board(
                    Arrays.stream(inputSplit[i].split("\n"))
                            .map(l -> Arrays.stream(l.split(" "))
                                    .filter(s -> !s.isEmpty())
                                    .map(Integer::parseInt)
                                    .toArray(Integer[]::new))
                            .toArray(Integer[][]::new))
            );
        }

        return Collections.unmodifiableSet(boards);
    }
}
