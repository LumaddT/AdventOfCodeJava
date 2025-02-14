package advent.of.code.solutions.y2020.day25;

import advent.of.code.solutions.Day;

@SuppressWarnings("unused")
public class Day25 implements Day {
    private static final int MODULO = 20_201_227;
    private static final int BASE_SUBJECT_NUMBER = 7;

    @Override
    public String part1(String inputString) {
        int cardPublicKey = Integer.parseInt(inputString.split("\n")[0]);
        int doorPublicKey = Integer.parseInt(inputString.split("\n")[1]);

        int doorLoopSize = findLoopSize(doorPublicKey);
        int secretKey = applyLoopSize(cardPublicKey, doorLoopSize);

        return String.format("%d", secretKey);
    }

    @Override
    public String part2(String inputString) {
        return "No part 2 on Christmas.";
    }

    private int findLoopSize(int publicKey) {
        int loopSize = 0;
        int currentValue = 1;

        while (currentValue != publicKey) {
            loopSize++;
            currentValue = (int) (((long) currentValue * BASE_SUBJECT_NUMBER) % MODULO);
        }

        return loopSize;
    }

    private int applyLoopSize(int subjectNumber, int loopSize) {
        int currentValue = 1;

        for (int i = 0; i < loopSize; i++) {
            currentValue = (int) (((long) currentValue * subjectNumber) % MODULO);
        }

        return currentValue;
    }
}
