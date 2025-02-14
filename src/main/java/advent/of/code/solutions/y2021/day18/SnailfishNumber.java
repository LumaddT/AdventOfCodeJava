package advent.of.code.solutions.y2021.day18;

import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
class SnailfishNumber {
    private SnailfishNumber Parent;
    private SnailfishNumber ChildLeft;
    private SnailfishNumber ChildRight;

    private Integer Value;

    public SnailfishNumber(String inputString) {
        this(inputString, null);
    }

    private SnailfishNumber(String inputString, SnailfishNumber parent) {
        Parent = parent;
        if (inputString.length() == 1) {
            Value = Integer.parseInt(inputString);
            ChildLeft = null;
            ChildRight = null;
            return;
        }

        Value = null;

        inputString = inputString.substring(1, inputString.length() - 1);

        int level = 0;
        int splitIndex = 0;

        do {
            switch (inputString.charAt(splitIndex)) {
                case '[' -> level++;
                case ']' -> level--;
            }
            splitIndex++;
        } while (level > 0);

        ChildLeft = new SnailfishNumber(inputString.substring(0, splitIndex), this);
        ChildRight = new SnailfishNumber(inputString.substring(splitIndex + 1), this);
    }

    public SnailfishNumber(SnailfishNumber childLeft, SnailfishNumber right) {
        this(childLeft, right, null);
    }

    private SnailfishNumber(SnailfishNumber childLeft, SnailfishNumber right, SnailfishNumber parent) {
        ChildLeft = childLeft;
        ChildLeft.Parent = this;

        ChildRight = right;
        ChildRight.Parent = this;

        Value = null;
        Parent = parent;
    }

    private SnailfishNumber(int value, SnailfishNumber parent) {
        ChildLeft = null;
        ChildRight = null;
        Value = value;
        Parent = parent;
    }

    public int getMagnitude() {
        if (Value != null) {
            return Value;
        }

        if (ChildLeft == null || ChildRight == null) {
            throw new RuntimeException("Impossible SnailfishNumber state.");
        }

        return 3 * ChildLeft.getMagnitude() + 2 * ChildRight.getMagnitude();
    }

    public void makeZero() {
        if (Value != null || ChildLeft.Value == null || ChildRight.Value == null) {
            throw new RuntimeException("Bad call to explode().");
        }

        Value = 0;
        ChildLeft = null;
        ChildRight = null;
    }

    public void split() {
        if (Value == null || Value < 10) {
            throw new RuntimeException("Bad call to split().");
        }

        ChildLeft = new SnailfishNumber((int) Math.floor((float) Value / 2), this);
        ChildRight = new SnailfishNumber((int) Math.ceil((float) Value / 2), this);
        Value = null;
    }

    public List<SnailfishNumber> getLeaves() {
        if (Value != null) {
            return List.of(this);
        }

        return Stream.of(ChildLeft.getLeaves(), ChildRight.getLeaves())
                .flatMap(List::stream)
                .toList();
    }

    public int getLevel() {
        if (Parent == null) {
            return 0;
        }

        return Parent.getLevel() + 1;
    }

    public void addToValue(int value) {
        if (Value == null) {
            throw new RuntimeException("Bad call to addToValue");
        }

        Value += value;
    }
}
