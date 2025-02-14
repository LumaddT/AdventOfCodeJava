package advent.of.code.solutions.y2021.day18;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class Day18 implements Day {
    @Override
    public String part1(String inputString) {
        List<SnailfishNumber> input = Arrays.stream(inputString.split("\n"))
                .map(SnailfishNumber::new)
                .toList();

        Stack<SnailfishNumber> snailfishNumbers = new Stack<>();
        snailfishNumbers.addAll(input.reversed());

        while (snailfishNumbers.size() > 1) {
            SnailfishNumber first = snailfishNumbers.pop();
            SnailfishNumber second = snailfishNumbers.pop();

            SnailfishNumber result = new SnailfishNumber(first, second);
            reduce(result);
            snailfishNumbers.add(result);
        }

        return String.format("%d", snailfishNumbers.pop().getMagnitude());
    }

    @Override
    public String part2(String inputString) {
        String[] input = inputString.split("\n");

        int maxMagnitude = Integer.MIN_VALUE;

        for (String firstString : input) {
            for (String secondString : input) {
                if (firstString.equals(secondString)) {
                    continue;
                }

                SnailfishNumber first = new SnailfishNumber(firstString);
                SnailfishNumber second = new SnailfishNumber(secondString);

                SnailfishNumber result = new SnailfishNumber(first, second);
                reduce(result);

                maxMagnitude = Math.max(maxMagnitude, result.getMagnitude());
            }
        }

        return String.format("%d", maxMagnitude);

    }

    private void reduce(SnailfishNumber snailfishNumber) {
        boolean modified;
        do {
            modified = false;

            while (explodeFirst(snailfishNumber)) {
                modified = true;
            }

            if (!modified) {
                modified = splitFirst(snailfishNumber);
            }
        } while (modified);
    }

    private boolean explodeFirst(SnailfishNumber snailfishNumber) {
        List<SnailfishNumber> leaves = snailfishNumber.getLeaves();

        for (int i = 0; i < leaves.size(); i++) {
            SnailfishNumber leaf = leaves.get(i);
            if (leaf.getLevel() >= 5) {
                SnailfishNumber parent = leaf.getParent();

                if (i > 0) {
                    SnailfishNumber leftLeaf = leaves.get(i - 1);
                    leftLeaf.addToValue(parent.getChildLeft().getValue());
                }

                if (i < leaves.size() - 1) {
                    SnailfishNumber rightLeaf = leaves.get(i + 1);
                    if (rightLeaf.getParent() == leaf.getParent()) {
                        if (i < leaves.size() - 2) {
                            rightLeaf = leaves.get(i + 2);
                        }
                    }

                    if (rightLeaf.getParent() != leaf.getParent()) {
                        rightLeaf.addToValue(parent.getChildRight().getValue());
                    }
                }

                parent.makeZero();
                return true;
            }
        }

        return false;
    }

    private boolean splitFirst(SnailfishNumber snailfishNumber) {
        List<SnailfishNumber> leaves = snailfishNumber.getLeaves();

        for (SnailfishNumber leaf : leaves) {
            if (leaf.getValue() > 9) {
                leaf.split();
                return true;
            }
        }

        return false;
    }
}
