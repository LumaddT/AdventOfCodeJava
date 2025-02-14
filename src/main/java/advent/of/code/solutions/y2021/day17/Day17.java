package advent.of.code.solutions.y2021.day17;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day17 implements Day {
    // There has to be a way to forego this constant and calculate what the
    // max possible Y is on the fly.
    private static final int MAX_Y_ATTEMPTS = 1_000;

    @Override
    public String part1(String inputString) {
        Input input = parseInputString(inputString);
        Tuple<Integer, Integer> output = calculateMaxYAndTotalShots(input);

        return String.format("%d", output.First());
    }

    @Override
    public String part2(String inputString) {
        Input input = parseInputString(inputString);
        Tuple<Integer, Integer> output = calculateMaxYAndTotalShots(input);

        return String.format("%d", output.Second());
    }

    private Input parseInputString(String inputString) {
        String regex = "^target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString.trim());

        if (!matcher.find()) {
            throw new RuntimeException("Bad regex.");
        }

        return new Input(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
        );
    }

    private Tuple<Integer, Integer> calculateMaxYAndTotalShots(Input input) {
        int maxHeight = 0;
        int hitTarget = 0;

        for (int initialXVelocity = 1; initialXVelocity <= input.maxX(); initialXVelocity++) {
            for (int initialYVelocity = input.minY(); initialYVelocity < MAX_Y_ATTEMPTS; initialYVelocity++) {
                int x = 0;
                int y = 0;

                int velocityX = initialXVelocity;
                int velocityY = initialYVelocity;

                int attemptMaxHeight = 0;

                while (y >= input.minY() && x <= input.maxX()) {
                    x += velocityX;
                    y += velocityY;

                    if (velocityX > 0) {
                        velocityX--;
                    }
                    velocityY--;

                    if (velocityY == 0) {
                        attemptMaxHeight = y;
                    }

                    if (input.minX() <= x && x <= input.maxX() && input.minY() <= y && y <= input.maxY()) {
                        maxHeight = Math.max(attemptMaxHeight, maxHeight);
                        hitTarget++;
                        break;
                    }
                }
            }
        }

        return new Tuple<>(maxHeight, hitTarget);
    }
}
