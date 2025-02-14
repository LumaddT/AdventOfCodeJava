package advent.of.code.solutions.y2021.day24;

import advent.of.code.solutions.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * I ended up solving this manually. By manipulating the variables matrix it should
 * be relatively straightforward to write a general solution. Some other time.
 */
@SuppressWarnings("unused")
public class Day24 implements Day {
    @Override
    public String part1(String inputString) {
        int[] serialNumber = {9, 6, 9, 7, 9, 9, 8, 9, 6, 9, 2, 4, 9, 5};
        int[][] variables = parseVariables(inputString);

        if (serialNumber.length != variables.length) {
            throw new RuntimeException("Serial number and variables should have the same length.");
        }

        long z = calculateFinalZ(serialNumber, variables);

        if (z == 0) {
            return String.format("%d", intArrayToNumber(serialNumber));
        }

        throw new RuntimeException("Bad solution.");
    }

    @Override
    public String part2(String inputString) {
        int[][] variables = parseVariables(inputString);
        int[] serialNumber = {5, 1, 3, 1, 6, 2, 1, 4, 1, 8, 1, 1, 4, 1};

        if (serialNumber.length != variables.length) {
            throw new RuntimeException("Serial number and variables should have the same length.");
        }

        long z = calculateFinalZ(serialNumber, variables);

        if (z == 0) {
            return String.format("%d", intArrayToNumber(serialNumber));
        }

        throw new RuntimeException("Bad solution.");
    }

    private int[][] parseVariables(String inputString) {
        String regex = "inp w\\nmul x 0\\nadd x z\\nmod x 26\\ndiv z (-?\\d+)\\nadd x (-?\\d+)\\neql x w\\neql x 0\\nmul y 0\\nadd y 25\\nmul y x\\nadd y 1\\nmul z y\\nmul y 0\\nadd y w\\nadd y (-?\\d+)\\nmul y x\\nadd z y";
        Pattern patter = Pattern.compile(regex);
        Matcher matcher = patter.matcher(inputString);

        List<int[]> returnValue = new ArrayList<>();

        while (matcher.find()) {
            int[] variables = new int[3];
            variables[0] = Integer.parseInt(matcher.group(1));
            variables[1] = Integer.parseInt(matcher.group(2));
            variables[2] = Integer.parseInt(matcher.group(3));

            returnValue.add(variables);
        }

        return returnValue.toArray(int[][]::new);
    }

    private long intArrayToNumber(int[] serialNumber) {
        long returnValue = 0;

        for (int i = 0; i < serialNumber.length; i++) {
            returnValue += serialNumber[i] * (long) Math.pow(10, serialNumber.length - i - 1);
        }

        return returnValue;
    }

    private int calculateFinalZ(int[] serialNumber, int[][] variables) {
        int z = 0;
        for (int i = 0; i < serialNumber.length; i++) {
            int digit = serialNumber[i];

            int x = z % 26 + variables[i][1];

            z /= variables[i][0];

            if (x != digit) {
                z = z * 26 + digit + variables[i][2];
            }
        }

        return z;
    }
}
