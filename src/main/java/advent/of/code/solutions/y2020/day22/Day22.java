package advent.of.code.solutions.y2020.day22;

import advent.of.code.solutions.Day;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day22 implements Day {
    @Override
    public String part1(String inputString) {
        Queue<Integer> player1 = parseInputString(inputString.split("\n\n")[0]);
        Queue<Integer> player2 = parseInputString(inputString.split("\n\n")[1]);

        while (!player1.isEmpty() && !player2.isEmpty()) {
            playRoundPart1(player1, player2);
        }

        Queue<Integer> winner;

        if (player1.isEmpty()) {
            winner = player2;
        } else {
            winner = player1;
        }

        int total = 0;
        int multiplier = winner.size();

        while (!winner.isEmpty()) {
            total += multiplier * winner.poll();
            multiplier--;
        }

        return String.format("%d", total);
    }

    /**
     * 5 to 7 seconds on my machine. I don't know where the improvement could be. Maybe decksToString() is too slow?
     */
    @Override
    public String part2(String inputString) {
        Queue<Integer> player1 = parseInputString(inputString.split("\n\n")[0]);
        Queue<Integer> player2 = parseInputString(inputString.split("\n\n")[1]);

        boolean player1won = playRecursiveGame(player1, player2);

        Queue<Integer> winner;
        if (player1won) {
            winner = player1;
        } else {
            winner = player2;
        }

        int total = 0;
        int multiplier = winner.size();

        while (!winner.isEmpty()) {
            total += multiplier * winner.poll();
            multiplier--;
        }

        return String.format("%d", total);
    }

    private Queue<Integer> parseInputString(String inputString) {
        Queue<Integer> returnValue = new LinkedList<>();
        String[] inputSplit = inputString.split("\n");

        for (int i = 1; i < inputSplit.length; i++) {
            returnValue.add(Integer.parseInt(inputSplit[i]));
        }

        return returnValue;
    }

    private void playRoundPart1(Queue<Integer> player1, Queue<Integer> player2) {
        Integer card1 = player1.poll();
        Integer card2 = player2.poll();

        if (card1 == null || card2 == null) {
            throw new RuntimeException("null in player hand.");
        }

        if (card1 > card2) {
            player1.add(card1);
            player1.add(card2);
        } else {
            player2.add(card2);
            player2.add(card1);
        }
    }

    /**
     * Returns `true` is player1 wins and false if player2 wins.
     */
    private boolean playRecursiveGame(Queue<Integer> player1, Queue<Integer> player2) {
        Set<String> roundCache = new HashSet<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            String roundCacheKey = decksToString(player1, player2);
            if (roundCache.contains(roundCacheKey)) {
                return true;
            }
            roundCache.add(roundCacheKey);

            Integer card1 = player1.poll();
            Integer card2 = player2.poll();

            if (card1 == null || card2 == null) {
                throw new RuntimeException("null in player hand.");
            }

            boolean player1won;
            if (player1.size() >= card1 && player2.size() >= card2) {
                Queue<Integer> newPlayer1 = new LinkedList<>(player1.stream().limit(card1).toList());
                Queue<Integer> newPlayer2 = new LinkedList<>(player2.stream().limit(card2).toList());
                player1won = playRecursiveGame(newPlayer1, newPlayer2);
            } else {
                player1won = card1 > card2;
            }

            if (player1won) {
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }

        return !player1.isEmpty();
    }

    private String decksToString(Queue<Integer> player1, Queue<Integer> player2) {
        String player1String = player1.stream()
                .map(v -> String.format("%d", v))
                .collect(Collectors.joining("|"));
        String player2String = player2.stream()
                .map(v -> String.format("%d", v))
                .collect(Collectors.joining("|"));

        return player1String + "||" + player2String;
    }
}
