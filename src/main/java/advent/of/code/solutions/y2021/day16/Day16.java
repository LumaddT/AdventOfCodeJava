package advent.of.code.solutions.y2021.day16;

import advent.of.code.solutions.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A bit too many indentations in parseInputPackets() but it works so I'm not touching it.
 */
@SuppressWarnings("unused")
public class Day16 implements Day {
    @Override
    public String part1(String inputString) {
        List<Boolean> input = parseInputString(inputString.trim());
        List<Packet> parsedPackets = parseInputPackets(input, 1, LimitTypes.AMOUNT).packetsParsed();

        Packet topPacket = parsedPackets.getFirst();

        return String.format("%d", topPacket.sumVersionNumbers());
    }

    @Override
    public String part2(String inputString) {
        List<Boolean> input = parseInputString(inputString.trim());
        List<Packet> parsedPackets = parseInputPackets(input, 1, LimitTypes.AMOUNT).packetsParsed();

        Packet topPacket = parsedPackets.getFirst();

        return String.format("%d", topPacket.getResult());
    }

    private List<Boolean> parseInputString(String inputString) {
        List<Integer> returnValue = new ArrayList<>();

        for (char hex : inputString.toCharArray()) {
            switch (hex) {
                case '0' -> returnValue.addAll(List.of(0, 0, 0, 0));
                case '1' -> returnValue.addAll(List.of(0, 0, 0, 1));
                case '2' -> returnValue.addAll(List.of(0, 0, 1, 0));
                case '3' -> returnValue.addAll(List.of(0, 0, 1, 1));
                case '4' -> returnValue.addAll(List.of(0, 1, 0, 0));
                case '5' -> returnValue.addAll(List.of(0, 1, 0, 1));
                case '6' -> returnValue.addAll(List.of(0, 1, 1, 0));
                case '7' -> returnValue.addAll(List.of(0, 1, 1, 1));
                case '8' -> returnValue.addAll(List.of(1, 0, 0, 0));
                case '9' -> returnValue.addAll(List.of(1, 0, 0, 1));
                case 'A' -> returnValue.addAll(List.of(1, 0, 1, 0));
                case 'B' -> returnValue.addAll(List.of(1, 0, 1, 1));
                case 'C' -> returnValue.addAll(List.of(1, 1, 0, 0));
                case 'D' -> returnValue.addAll(List.of(1, 1, 0, 1));
                case 'E' -> returnValue.addAll(List.of(1, 1, 1, 0));
                case 'F' -> returnValue.addAll(List.of(1, 1, 1, 1));
                default -> throw new RuntimeException(String.format("Bad hex %c.", hex));
            }
        }

        return returnValue.stream().map(n -> n == 1).toList();
    }

    private ParsingResult parseInputPackets(List<Boolean> input, int limit, LimitTypes limitType) {
        int currentIndex = 0;
        int packetsRead = 0;
        List<Packet> returnValue = new ArrayList<>();

        while (isWithinLimit(currentIndex, packetsRead, limit, limitType)) {
            int version = 0;
            for (int i = 0; i < 3; i++) {
                if (input.get(currentIndex)) {
                    version += (int) Math.pow(2, 2 - i);
                }
                currentIndex++;
            }

            int typeId = 0;
            for (int i = 0; i < 3; i++) {
                if (input.get(currentIndex)) {
                    typeId += (int) Math.pow(2, 2 - i);
                }
                currentIndex++;
            }

            if (typeId == 4) {
                List<Boolean> valueBinary = new ArrayList<>();
                int indexInValue = 0;
                boolean shouldBreak = false;

                while (!(indexInValue % 5 == 0 && shouldBreak)) {
                    if (indexInValue % 5 == 0 && !input.get(currentIndex)) {
                        shouldBreak = true;
                    } else if (indexInValue % 5 != 0) {
                        valueBinary.add(input.get(currentIndex));
                    }

                    indexInValue++;
                    currentIndex++;
                }

                if (valueBinary.size() > 63) {
                    throw new RuntimeException("longs are not enough.");
                }

                long value = 0;

                for (int i = 0; i < valueBinary.size(); i++) {
                    if (valueBinary.get(i)) {
                        value += (long) Math.pow(2, valueBinary.size() - i - 1);
                    }
                }

                returnValue.add(new Packet(version, typeId, value));
            } else {
                LimitTypes parsedLimitType;
                if (input.get(currentIndex)) {
                    parsedLimitType = LimitTypes.AMOUNT;
                } else {
                    parsedLimitType = LimitTypes.LENGTH;
                }
                currentIndex++;

                int parsedLimit = 0;
                int lengthToParse = switch (parsedLimitType) {
                    case LENGTH -> 15;
                    case AMOUNT -> 11;
                };

                for (int i = 0; i < lengthToParse; i++) {
                    if (input.get(currentIndex)) {
                        parsedLimit += (int) Math.pow(2, lengthToParse - i - 1);
                    }
                    currentIndex++;
                }

                ParsingResult parsingResult = parseInputPackets(input.subList(currentIndex, input.size()), parsedLimit, parsedLimitType);
                currentIndex += parsingResult.lengthRead();

                returnValue.add(new Packet(version, typeId, parsingResult.packetsParsed()));
            }

            packetsRead++;
        }

        return new ParsingResult(currentIndex, Collections.unmodifiableList(returnValue));
    }

    private boolean isWithinLimit(int currentIndex, int packetsRead, int limit, LimitTypes limitType) {
        return switch (limitType) {
            case LENGTH -> currentIndex < limit;
            case AMOUNT -> packetsRead < limit;
        };
    }
}
