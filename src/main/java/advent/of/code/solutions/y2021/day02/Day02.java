package advent.of.code.solutions.y2021.day02;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;

@SuppressWarnings("unused")
public class Day02 implements Day {
    @Override
    public String part1(String inputString) {
        String[] input = inputString.split("\n");
        Coordinate coordinate = Coordinate.ORIGIN;

        for (String line : input) {
            String command = line.split(" ")[0];
            int amount = Integer.parseInt(line.split(" ")[1]);

            coordinate = switch (command) {
                case "up" -> coordinate.up(amount);
                case "down" -> coordinate.down(amount);
                case "forward" -> coordinate.right(amount);
                default -> throw new IllegalStateException("Unexpected value: " + command);
            };
        }

        return String.format("%d", coordinate.Row() * coordinate.Column());
    }

    @Override
    public String part2(String inputString) {
        String[] input = inputString.split("\n");
        Coordinate coordinate = Coordinate.ORIGIN;
        int aim = 0;

        for (String line : input) {
            String command = line.split(" ")[0];
            int amount = Integer.parseInt(line.split(" ")[1]);

            switch (command) {
                case "up" -> aim -= amount;
                case "down" -> aim += amount;
                case "forward" -> coordinate = coordinate.right(amount).down(amount * aim);
                default -> throw new IllegalStateException("Unexpected value: " + command);
            }
        }

        return String.format("%d", coordinate.Row() * coordinate.Column());
    }
}
