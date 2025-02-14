package advent.of.code.solutions.y2019.day08;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.screenParser.ScreenParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Day08 implements Day {
    private static final int SCREEN_WIDTH = 25;
    private static final int SCREEN_HEIGHT = 6;
    private static final int LAYER_SIZE = SCREEN_WIDTH * SCREEN_HEIGHT;

    @Override
    public String part1(String inputString) {
        List<Integer[][]> input = parseInputString(inputString);

        Integer[][] layerWithLeastZeros = null;
        int zerosAmount = Integer.MAX_VALUE;

        for (Integer[][] layer : input) {
            int zeros = MatrixUtils.count(layer, 0);
            if (zeros < zerosAmount) {
                zerosAmount = zeros;
                layerWithLeastZeros = layer;
            }
        }

        if (layerWithLeastZeros == null) {
            throw new RuntimeException("Bad solution.");
        }

        int ones = MatrixUtils.count(layerWithLeastZeros, 1);
        int twos = MatrixUtils.count(layerWithLeastZeros, 2);

        return String.format("%d", ones * twos);
    }

    @Override
    public String part2(String inputString) {
        List<Integer[][]> input = parseInputString(inputString);

        Integer[][] pixels = new Integer[SCREEN_HEIGHT][SCREEN_WIDTH];
        MatrixUtils.fillMatrix(pixels, 2);

        for (Integer[][] layer : input) {
            for (int row = 0; row < SCREEN_HEIGHT; row++) {
                for (int column = 0; column < SCREEN_WIDTH; column++) {
                    if (pixels[row][column] == 2) {
                        pixels[row][column] = layer[row][column];
                    }
                }
            }
        }

        Boolean[][] screen = Arrays.stream(pixels)
                .map(l -> Arrays.stream(l)
                        .map(p -> p == 1)
                        .toArray(Boolean[]::new))
                .toArray(Boolean[][]::new);

        return ScreenParser.parseScreen(screen);
    }

    private List<Integer[][]> parseInputString(String inputString) {
        List<Integer[][]> returnValue = new ArrayList<>();
        int layerCount = inputString.trim().length() / LAYER_SIZE;

        for (int i = 0; i < layerCount; i++) {
            returnValue.add(new Integer[SCREEN_HEIGHT][SCREEN_WIDTH]);
        }

        for (int i = 0; i < inputString.trim().length(); i++) {
            int layer = i / LAYER_SIZE;
            int row = (i % LAYER_SIZE) / SCREEN_WIDTH;
            int column = (i % LAYER_SIZE) % SCREEN_WIDTH;
            returnValue.get(layer)[row][column] = inputString.charAt((i)) - '0';
        }

        return Collections.unmodifiableList(returnValue);
    }
}
