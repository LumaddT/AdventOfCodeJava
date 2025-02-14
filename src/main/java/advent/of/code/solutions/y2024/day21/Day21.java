package advent.of.code.solutions.y2024.day21;

import advent.of.code.solutions.Day;
import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day21 implements Day {
    private final static Character[][] NUMERIC_KEYPAD = {
            {'7', '8', '9'},
            {'4', '5', '6'},
            {'1', '2', '3'},
            {null, '0', 'A'}
    };

    private final static Character[][] DIRECTIONAL_KEYPAD = {
            {null, '^', 'A'},
            {'<', 'v', '>'}
    };

    private final static int DIRECTIONAL_KEYPADS_PART_1 = 3;
    private final static int DIRECTIONAL_KEYPADS_PART_2 = 26;

    @Override
    public String part1(String inputString) {
        String[] input = inputString.split("\n");

        Map<String, Set<String>> mappingsNumeric = findAllSequences(NUMERIC_KEYPAD);
        Map<String, Set<String>> mappingsDirectional = findAllSequences(DIRECTIONAL_KEYPAD);

        Map<String, Set<String>> sequencesMappingsCache = new HashMap<>();
        Map<String, Long> minLengthCache = new HashMap<>();

        long total = 0;

        for (String sequence : input) {
            Set<String> firstLayerSequences = findNextLayerSequences(sequence, mappingsNumeric, sequencesMappingsCache);
            long finalLength = Long.MAX_VALUE;
            for (String firstLayerSequence : firstLayerSequences) {
                long tempLength = 0;
                String[] subSequences = firstLayerSequence.split("(?<=A)");
                for (String subSequence : subSequences) {
                    tempLength += findFinalLength(subSequence, mappingsDirectional, DIRECTIONAL_KEYPADS_PART_1 - 1, minLengthCache, sequencesMappingsCache);
                }

                finalLength = Math.min(finalLength, tempLength);
            }

            int numericPart = Integer.parseInt(sequence.substring(0, sequence.length() - 1));
            total += finalLength * numericPart;
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        String[] input = inputString.split("\n");

        Map<String, Set<String>> mappingsNumeric = findAllSequences(NUMERIC_KEYPAD);
        Map<String, Set<String>> mappingsDirectional = findAllSequences(DIRECTIONAL_KEYPAD);

        Map<String, Set<String>> sequencesMappingsCache = new HashMap<>();
        Map<String, Long> minLengthCache = new HashMap<>();

        long total = 0;

        for (String sequence : input) {
            Set<String> firstLayerSequences = findNextLayerSequences(sequence, mappingsNumeric, sequencesMappingsCache);
            long finalLength = Long.MAX_VALUE;
            for (String firstLayerSequence : firstLayerSequences) {
                long tempLength = 0;
                String[] subSequences = firstLayerSequence.split("(?<=A)");
                for (String subSequence : subSequences) {
                    tempLength += findFinalLength(subSequence, mappingsDirectional, DIRECTIONAL_KEYPADS_PART_2 - 1, minLengthCache, sequencesMappingsCache);
                }

                finalLength = Math.min(finalLength, tempLength);
            }

            int numericPart = Integer.parseInt(sequence.substring(0, sequence.length() - 1));
            total += finalLength * numericPart;
        }

        return String.format("%d", total);
    }

    private Map<String, Set<String>> findAllSequences(Character[][] keypad) {
        Map<String, Set<String>> returnValue = new HashMap<>();

        Set<Character> characters = MatrixUtils.toSet(keypad).stream().filter(Objects::nonNull).collect(Collectors.toSet());

        for (char source : characters) {
            Integer[][] distances = findDistances(keypad, source);
            Coordinate sourceCoordinate = MatrixUtils.coordinateOf(keypad, source);
            if (sourceCoordinate == null) {
                throw new RuntimeException(String.format("Bad search for source %c", source));
            }

            for (char destination : characters) {
                String travel = "" + source + destination;
                Coordinate destinationCoordinate = MatrixUtils.coordinateOf(keypad, destination);

                returnValue.put(travel, findSequences(sourceCoordinate, destinationCoordinate, distances));
            }
        }

        return returnValue;
    }

    private Set<String> findSequences(Coordinate source, Coordinate destination, Integer[][] distances) {
        Set<String> returnValue = new HashSet<>();

        if (source.equals(destination)) {
            returnValue.add("A");
            return returnValue;
        }

        int expectedDistance = MatrixUtils.getMatrixCoord(distances, source) + 1;
        for (Directions direction : DirectionUtils.ALL_DIRECTIONS) {
            Coordinate newCoordinate = source.move(direction);
            if (MatrixUtils.isCoordInRange(distances, newCoordinate)
                    && MatrixUtils.getMatrixCoord(distances, newCoordinate).equals(expectedDistance)) {
                Set<String> suffixes = findSequences(newCoordinate, destination, distances);
                for (String suffix : suffixes) {
                    String sequence = DirectionUtils.directionToChar(direction) + suffix;

                    returnValue.add(sequence);
                }
            }
        }

        return returnValue;
    }

    private Integer[][] findDistances(Character[][] keypad, char source) {
        Integer[][] distances = new Integer[keypad.length][keypad[0].length];
        MatrixUtils.fillMatrix(distances, Integer.MAX_VALUE);
        Coordinate sourceCoordinate = MatrixUtils.coordinateOf(keypad, source);
        if (sourceCoordinate == null) {
            throw new RuntimeException(String.format("Bad source %c in findDistances.", source));
        }
        MatrixUtils.setMatrixCoord(distances, sourceCoordinate, 0);

        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(sourceCoordinate);

        while (!toCheck.isEmpty()) {
            Coordinate currentCoordinate = toCheck.poll();
            int nextValue = MatrixUtils.getMatrixCoord(distances, currentCoordinate) + 1;

            for (Coordinate nextCoordinate : currentCoordinate.orthogonallyAdjacent()) {
                if (MatrixUtils.isCoordInRange(distances, nextCoordinate)
                        && MatrixUtils.getMatrixCoord(keypad, nextCoordinate) != null
                        && nextValue < MatrixUtils.getMatrixCoord(distances, nextCoordinate)) {
                    MatrixUtils.setMatrixCoord(distances, nextCoordinate, nextValue);
                    toCheck.add(nextCoordinate);
                }
            }
        }

        return distances;
    }

    private long findFinalLength(String sequence, Map<String, Set<String>> mappingsDirectional, int iterations, Map<String, Long> minLengthCache, Map<String, Set<String>> sequencesMappingsCache) {
        if (iterations == 0) {
            return sequence.length();
        }

        String cacheKey = sequence + '|' + iterations;
        if (minLengthCache.containsKey(cacheKey)) {
            return minLengthCache.get(cacheKey);
        }

        Set<String> nextLayerSequences = findNextLayerSequences(sequence, mappingsDirectional, sequencesMappingsCache);
        long minValue = Long.MAX_VALUE;
        for (String firstLayerSequence : nextLayerSequences) {
            long finalLength = 0;
            String[] subSequences = firstLayerSequence.split("(?<=A)");

            for (String subSequence : subSequences) {
                finalLength += findFinalLength(subSequence, mappingsDirectional, iterations - 1, minLengthCache, sequencesMappingsCache);
            }

            minValue = Math.min(minValue, finalLength);
        }

        minLengthCache.put(cacheKey, minValue);

        return minValue;
    }

    private Set<String> findNextLayerSequences(String sequence, Map<String, Set<String>> mappingsDirectional, Map<String, Set<String>> sequencesMappingsCache) {
        if (sequencesMappingsCache.containsKey(sequence)) {
            return sequencesMappingsCache.get(sequence);
        }

        Set<String> returnValue = new HashSet<>();
        returnValue.add("");

        for (int i = 0; i < sequence.length(); i++) {
            String travel;
            if (i == 0) {
                travel = "" + 'A' + sequence.charAt(i);
            } else {
                travel = "" + sequence.charAt(i - 1) + sequence.charAt(i);
            }

            Set<String> newReturnValue = new HashSet<>();

            for (String prefix : returnValue) {
                for (String suffix : mappingsDirectional.get(travel)) {
                    newReturnValue.add(prefix + suffix);
                }
                returnValue = newReturnValue;
            }
        }

        sequencesMappingsCache.put(sequence, returnValue);

        return returnValue;
    }
}
