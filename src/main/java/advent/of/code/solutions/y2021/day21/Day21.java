package advent.of.code.solutions.y2021.day21;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day21 implements Day {
    private final static int DICE_MAX = 100;
    private final static int SPACE_MAX = 10;

    private final static int TARGET_SCORE_PART_1 = 1_000;
    private final static int TARGET_SCORE_PART_2 = 21;

    @Override
    public String part1(String inputString) {
        Tuple<Integer, Integer> input = parseInputString(inputString);

        int player1 = input.First() - 1;
        int player2 = input.Second() - 1;
        int dice = 0;

        int player1Score = 0;
        int player2Score = 0;

        while (true) {
            int player1roll = (dice % DICE_MAX) + ((dice + 1) % DICE_MAX) + ((dice + 2) % DICE_MAX) + 3;
            dice += 3;
            player1 += player1roll;
            player1 %= SPACE_MAX;
            player1Score += player1 + 1;

            if (player1Score >= TARGET_SCORE_PART_1) {
                return String.format("%d", player2Score * dice);
            }

            int player2roll = (dice % DICE_MAX) + ((dice + 1) % DICE_MAX) + ((dice + 2) % DICE_MAX) + 3;
            dice += 3;
            player2 += player2roll;
            player2 %= SPACE_MAX;
            player2Score += player2 + 1;

            if (player2Score >= TARGET_SCORE_PART_1) {
                return String.format("%d", player1Score * dice);
            }
        }
    }

    @Override
    public String part2(String inputString) {
        Tuple<Integer, Integer> input = parseInputString(inputString);

        int player1 = input.First() - 1;
        int player2 = input.Second() - 1;

        return String.format("%d", mostUniversesWinner(player1, player2));
    }

    private Tuple<Integer, Integer> parseInputString(String inputString) {
        String regex = "^Player 1 starting position: (\\d{1,2})\nPlayer 2 starting position: (\\d{1,2})\n$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        if (!matcher.find()) {
            throw new RuntimeException("Bad regex.");
        }

        return new Tuple<>(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    private long mostUniversesWinner(int player1, int player2) {
        Map<String, Tuple<Long, Long>> cache = new HashMap<>();

        Tuple<Long, Long> three = universesEachPlayerWins(player1, player2, 3, 0, 0, true, cache);
        Tuple<Long, Long> four = universesEachPlayerWins(player1, player2, 4, 0, 0, true, cache);
        Tuple<Long, Long> five = universesEachPlayerWins(player1, player2, 5, 0, 0, true, cache);
        Tuple<Long, Long> six = universesEachPlayerWins(player1, player2, 6, 0, 0, true, cache);
        Tuple<Long, Long> seven = universesEachPlayerWins(player1, player2, 7, 0, 0, true, cache);
        Tuple<Long, Long> eight = universesEachPlayerWins(player1, player2, 8, 0, 0, true, cache);
        Tuple<Long, Long> nine = universesEachPlayerWins(player1, player2, 9, 0, 0, true, cache);

        long totalPlayer1 = three.First()
                + 3 * four.First()
                + 6 * five.First()
                + 7 * six.First()
                + 6 * seven.First()
                + 3 * eight.First()
                + nine.First();

        long totalPlayer2 = three.Second()
                + 3 * four.Second()
                + 6 * five.Second()
                + 7 * six.Second()
                + 6 * seven.Second()
                + 3 * eight.Second()
                + nine.Second();

        return Math.max(totalPlayer1, totalPlayer2);
    }

    private Tuple<Long, Long> universesEachPlayerWins(int player1, int player2, int roll, long player1Score, long player2Score, boolean isPlayer1Turn, Map<String, Tuple<Long, Long>> cache) {
        String cacheKey = generateCacheKey(player1, player2, roll, player1Score, player2Score, isPlayer1Turn);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        if (isPlayer1Turn) {
            player1 += roll;
            player1 %= SPACE_MAX;
            player1Score += player1 + 1;

            if (player1Score >= TARGET_SCORE_PART_2) {
                Tuple<Long, Long> returnValue = new Tuple<>(1L, 0L);
                cache.put(cacheKey, returnValue);
                return returnValue;
            }
        } else {
            player2 += roll;
            player2 %= SPACE_MAX;
            player2Score += player2 + 1;

            if (player2Score >= TARGET_SCORE_PART_2) {
                Tuple<Long, Long> returnValue = new Tuple<>(0L, 1L);
                cache.put(cacheKey, returnValue);
                return returnValue;
            }
        }

        Tuple<Long, Long> three = universesEachPlayerWins(player1, player2, 3, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> four = universesEachPlayerWins(player1, player2, 4, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> five = universesEachPlayerWins(player1, player2, 5, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> six = universesEachPlayerWins(player1, player2, 6, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> seven = universesEachPlayerWins(player1, player2, 7, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> eight = universesEachPlayerWins(player1, player2, 8, player1Score, player2Score, !isPlayer1Turn, cache);
        Tuple<Long, Long> nine = universesEachPlayerWins(player1, player2, 9, player1Score, player2Score, !isPlayer1Turn, cache);

        long totalPlayer1 = three.First()
                + 3 * four.First()
                + 6 * five.First()
                + 7 * six.First()
                + 6 * seven.First()
                + 3 * eight.First()
                + nine.First();

        long totalPlayer2 = three.Second()
                + 3 * four.Second()
                + 6 * five.Second()
                + 7 * six.Second()
                + 6 * seven.Second()
                + 3 * eight.Second()
                + nine.Second();

        Tuple<Long, Long> returnValue = new Tuple<>(totalPlayer1, totalPlayer2);
        cache.put(cacheKey, returnValue);
        return returnValue;
    }

    private String generateCacheKey(int player1, int player2, int roll, long player1Score, long player2Score, boolean isPlayer1Turn) {
        StringBuilder returnValue = new StringBuilder();

        returnValue.append(player1);
        returnValue.append(player2);
        returnValue.append(roll);
        returnValue.append(player1Score);
        returnValue.append('|');
        returnValue.append(player2Score);
        if (isPlayer1Turn) {
            returnValue.append('T');
        } else {
            returnValue.append('F');
        }

        return returnValue.toString();
    }
}
