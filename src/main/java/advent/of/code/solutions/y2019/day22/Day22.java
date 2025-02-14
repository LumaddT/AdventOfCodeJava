package advent.of.code.solutions.y2019.day22;

import advent.of.code.solutions.Day;
import advent.of.code.utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Day22 implements Day {
    private static final BigInteger DECK_SIZE_PART_1 = new BigInteger("10007");
    private static final BigInteger ANSWER_CARD_NUMBER_PART_1 = new BigInteger("2019");

    private static final BigInteger DECK_SIZE_PART_2 = new BigInteger("119315717514047");
    private static final BigInteger ANSWER_CARD_POSITION_PART_2 = new BigInteger("2020");
    private static final BigInteger ITERATIONS_PART_2 = new BigInteger("101741582076661");

    /**
     * The 3 actions translate to operations in modular arithmetic, modulo the size of the deck.
     * Dealing with increment is multiplying, cutting is subtracting (with a positive cut the cards shift left),
     * and dealing into a new stack is calculating the modular opposite.
     * The whole list of actions (the puzzle input) can be coalesced into a single dealing with increment
     * action followed by a single action cutting action. This is what coalesceActions() achieves.
     */
    @Override
    public String part1(String inputString) {
        List<Action> input = parseInputString(inputString);
        CoalescedActions coalescedActions = coalesceActions(input, DECK_SIZE_PART_1);

        return String.format("%d", ANSWER_CARD_NUMBER_PART_1
                .multiply(coalescedActions.TotalDeal())
                .subtract(coalescedActions.TotalCut())
                .mod(DECK_SIZE_PART_1));
    }

    /**
     * I submitted well over 15 answers for this part because of long overflows. I ended up opting
     * to use BigIntegers all over the code to avoid these issues.
     * Part2 is opposite of part 1 in that it requires to find the card in position 2020 after a
     * lot of iterations rather than finding where the card 2020 ends up. This requires to do the
     * opposite operation to Part1 repeatedly: adding the total cut value of the coalesced actions
     * and then dividing by the total coalesced deal value. After trying 3 different formulas and
     * with some help from an LLM for the math (who knew b^(−1) ≡ b^(p−2) mod p?) I found this solution.
     * I believe I found the correct mathematical solution several hours before finding the correct result,
     * but it appeared wrong due to overflows.
     */
    @Override
    public String part2(String inputString) {
        List<Action> input = parseInputString(inputString);
        CoalescedActions coalescedActions = coalesceActions(input, DECK_SIZE_PART_2);

        BigInteger totalDealMultiplicativeInverse = BigIntegerUtils.modularDivision(BigInteger.ONE, coalescedActions.TotalDeal(), DECK_SIZE_PART_2);

        BigInteger numerator = totalDealMultiplicativeInverse
                .modPow(ITERATIONS_PART_2.add(BigInteger.ONE), DECK_SIZE_PART_2)
                .subtract(BigInteger.ONE);
        BigInteger denominator = totalDealMultiplicativeInverse
                .subtract(BigInteger.ONE);
        BigInteger cutFactor = BigIntegerUtils.modularDivision(numerator, denominator, DECK_SIZE_PART_2)
                .subtract(BigInteger.ONE);
        BigInteger result = ANSWER_CARD_POSITION_PART_2
                .multiply(totalDealMultiplicativeInverse.modPow(ITERATIONS_PART_2, DECK_SIZE_PART_2))
                .add(coalescedActions.TotalCut().multiply(cutFactor)).mod(DECK_SIZE_PART_2);

        return String.format("%d", result);
    }

    List<Action> parseInputString(String inputString) {
        List<Action> returnValue = new ArrayList<>();
        String[] inputSplit = inputString.split("\n");

        for (String line : inputSplit) {
            String[] lineSplit = line.split(" ");

            if (lineSplit[0].equals("cut")) {
                BigInteger value = new BigInteger(lineSplit[1]);
                returnValue.add(new Action(ActionTypes.CUT, value));
            } else if (lineSplit[2].equals("increment")) {
                returnValue.add(new Action(ActionTypes.DEAL, new BigInteger(lineSplit[3])));
            } else {
                returnValue.add(new Action(ActionTypes.INVERT, null));
            }
        }

        return Collections.unmodifiableList(returnValue);
    }

    private CoalescedActions coalesceActions(List<Action> input, BigInteger deckSize) {
        List<CoalescedActions> partiallyCoalescedActions = getPartiallyCoalescedActions(input, deckSize);

        if (partiallyCoalescedActions.size() % 2 == 0) {
            throw new RuntimeException("Bad assumption: deal into new stack appeared an even amount of times.");
        }

        BigInteger totalCut = partiallyCoalescedActions.getFirst().TotalCut();
        BigInteger totalDeal = partiallyCoalescedActions.getFirst().TotalDeal();

        for (int i = 1; i < partiallyCoalescedActions.size(); i += 2) {
            totalDeal = totalDeal.multiply(partiallyCoalescedActions.get(i).TotalDeal())
                    .multiply(partiallyCoalescedActions.get(i + 1).TotalDeal())
                    .mod(deckSize);

            totalCut = totalCut.multiply(partiallyCoalescedActions.get(i).TotalDeal())
                    .subtract(partiallyCoalescedActions.get(i).TotalDeal().subtract(BigInteger.ONE))
                    .subtract(partiallyCoalescedActions.get(i).TotalCut())
                    .multiply(partiallyCoalescedActions.get(i + 1).TotalDeal())
                    .add(partiallyCoalescedActions.get(i + 1).TotalCut())
                    .mod(deckSize);
        }

        return new CoalescedActions(totalCut, totalDeal);
    }

    private List<CoalescedActions> getPartiallyCoalescedActions(List<Action> input, BigInteger deckSize) {
        BigInteger currentCut = BigInteger.ZERO;
        BigInteger currentDeal = BigInteger.ONE;
        List<CoalescedActions> partiallyCoalescedActions = new ArrayList<>();

        for (Action action : input) {
            switch (action.Type()) {
                case INVERT -> {
                    partiallyCoalescedActions.add(new CoalescedActions(currentCut, currentDeal));
                    currentCut = BigInteger.ZERO;
                    currentDeal = BigInteger.ONE;
                }
                case CUT -> currentCut = currentCut.add(action.Amount()).mod(deckSize);
                case ActionTypes.DEAL -> {
                    currentDeal = currentDeal.multiply(action.Amount()).mod(deckSize);
                    currentCut = currentCut.multiply(action.Amount()).mod(deckSize);
                }
            }
        }

        partiallyCoalescedActions.add(new CoalescedActions(currentCut, currentDeal));
        return partiallyCoalescedActions;
    }

}
