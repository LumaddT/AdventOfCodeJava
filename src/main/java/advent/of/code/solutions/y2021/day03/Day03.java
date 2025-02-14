package advent.of.code.solutions.y2021.day03;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day03 implements Day {
    @Override
    public String part1(String inputString) {
        String[] input = inputString.split("\n");

        int gammaRate = 0;
        int epsilonRate = 0;

        for (int i = 0; i < input[0].length(); i++) {
            int lambdaI = i;
            if (Arrays.stream(input).map(s -> s.charAt(lambdaI)).filter(ch -> ch == '1').count() > input.length / 2) {
                gammaRate += (int) Math.pow(2, input[0].length() - i - 1);
            } else {
                epsilonRate += (int) Math.pow(2, input[0].length() - i - 1);
            }
        }

        return String.format("%d", gammaRate * epsilonRate);
    }

    @Override
    public String part2(String inputString) {
        String[] input = inputString.split("\n");

        List<String> oxygenGeneratorRatingCandidates = Arrays.stream(input).toList();
        List<String> CO2ScrubberRatingCandidates = Arrays.stream(input).toList();

        for (int i = 0; i < input[0].length(); i++) {
            int lambdaI = i;

            if (oxygenGeneratorRatingCandidates.size() > 1) {
                if (oxygenGeneratorRatingCandidates.stream().map(s -> s.charAt(lambdaI)).filter(ch -> ch == '1').count() >= (float) oxygenGeneratorRatingCandidates.size() / 2) {
                    oxygenGeneratorRatingCandidates = oxygenGeneratorRatingCandidates.stream().filter(s -> s.charAt(lambdaI) == '1').toList();
                } else {
                    oxygenGeneratorRatingCandidates = oxygenGeneratorRatingCandidates.stream().filter(s -> s.charAt(lambdaI) == '0').toList();
                }
            }

            if (CO2ScrubberRatingCandidates.size() > 1) {
                if (CO2ScrubberRatingCandidates.stream().map(s -> s.charAt(lambdaI)).filter(ch -> ch == '1').count() >= (float) CO2ScrubberRatingCandidates.size() / 2) {
                    CO2ScrubberRatingCandidates = CO2ScrubberRatingCandidates.stream().filter(s -> s.charAt(lambdaI) == '0').toList();
                } else {
                    CO2ScrubberRatingCandidates = CO2ScrubberRatingCandidates.stream().filter(s -> s.charAt(lambdaI) == '1').toList();
                }
            }
        }

        if (oxygenGeneratorRatingCandidates.size() != 1 || CO2ScrubberRatingCandidates.size() != 1) {
            throw new RuntimeException("Bad solution.");
        }

        int oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorRatingCandidates.getFirst(), 2);
        int CO2ScrubberRating = Integer.parseInt(CO2ScrubberRatingCandidates.getFirst(), 2);

        return String.format("%d", oxygenGeneratorRating * CO2ScrubberRating);
    }
}
