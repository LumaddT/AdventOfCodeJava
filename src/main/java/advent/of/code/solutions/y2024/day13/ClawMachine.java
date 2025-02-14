package advent.of.code.solutions.y2024.day13;

import advent.of.code.utils.RegexUtils;

import java.util.regex.Matcher;

class ClawMachine {
    private static final int COST_A = 3;
    private static final int COST_B = 1;
    private static final long PART_2_OFFSET = 10_000_000_000_000L;

    private final long TargetX;
    private final long TargetY;
    private final int ButtonAX;
    private final int ButtonAY;
    private final int ButtonBX;
    private final int ButtonBY;

    private long CurrentX;
    private long CurrentY;

    private long APresses;
    private long BPresses;

    public ClawMachine(String inputString, boolean isPart1) {
        String regex = "^Button A: X\\+(\\d+), Y\\+(\\d+)\\nButton B: X\\+(\\d+), Y\\+(\\d+)\\nPrize: X=(\\d+), Y=(\\d+)$";
        Matcher matcher = RegexUtils.match(regex, inputString, 6);

        ButtonAX = Integer.parseInt(matcher.group(1));
        ButtonAY = Integer.parseInt(matcher.group(2));
        ButtonBX = Integer.parseInt(matcher.group(3));
        ButtonBY = Integer.parseInt(matcher.group(4));

        if (isPart1) {
            TargetX = Integer.parseInt(matcher.group(5));
            TargetY = Integer.parseInt(matcher.group(6));
        } else {
            TargetX = Integer.parseInt(matcher.group(5)) + PART_2_OFFSET;
            TargetY = Integer.parseInt(matcher.group(6)) + PART_2_OFFSET;
        }

        CurrentX = 0;
        CurrentY = 0;

        APresses = 0;
        BPresses = 0;
    }

    public void pressA(long i) {
        if (i < 0) {
            throw new RuntimeException("Illegal amount of presses.");
        }

        APresses += i;

        CurrentX += ButtonAX * i;
        CurrentY += ButtonAY * i;
    }

    public void pressB(long i) {
        if (i < 0) {
            throw new RuntimeException("Illegal amount of presses.");
        }

        BPresses += i;

        CurrentX += ButtonBX * i;
        CurrentY += ButtonBY * i;
    }

    public void undoB(long i) {
        if (i < 0) {
            throw new RuntimeException("Illegal amount of presses.");
        }
        if (BPresses <= 0) {
            throw new RuntimeException("Cannot undo B press.");
        }

        BPresses -= i;

        CurrentX -= ButtonBX * i;
        CurrentY -= ButtonBY * i;
    }

    public boolean canUndoB() {
        return BPresses > 0;
    }

    public boolean isOnTarget() {
        return CurrentX == TargetX && CurrentY == TargetY;
    }

    public long getTotalCost() {
        long bPressesToBeyondX = (long) Math.ceil((double) TargetX / ButtonBX);
        long bPressesToBeyondY = (long) Math.ceil((double) TargetY / ButtonBY);
        long maxBPresses = Math.min(bPressesToBeyondX, bPressesToBeyondY);

        long aPressesToBeyondX = (long) Math.ceil((double) TargetX / ButtonAX);
        long aPressesToBeyondY = (long) Math.ceil((double) TargetY / ButtonAY);
        long maxAPresses = Math.min(aPressesToBeyondX, aPressesToBeyondY);

        long tempTargetXAfterB = TargetX - maxBPresses * ButtonBX;
        long tempTargetYAfterB = TargetY - maxBPresses * ButtonBY;
        long aPressesToBeyondTempX = (long) Math.ceil((double) tempTargetXAfterB / ButtonAX);
        long aPressesToBeyondTempY = (long) Math.ceil((double) tempTargetYAfterB / ButtonAY);
        long minAPresses = Math.max(aPressesToBeyondTempX, aPressesToBeyondTempY);

        long tempTargetXAfterA = TargetX - maxAPresses * ButtonAX;
        long tempTargetYAfterA = TargetY - maxAPresses * ButtonAY;
        long bPressesToBeyondTempX = (long) Math.ceil((double) tempTargetXAfterA / ButtonBX);
        long bPressesToBeyondTempY = (long) Math.ceil((double) tempTargetYAfterA / ButtonBY);
        long minBPresses = Math.max(bPressesToBeyondTempX, bPressesToBeyondTempY);

        this.pressA(minAPresses);
        this.pressB(maxBPresses);

        while (APresses <= maxAPresses && BPresses >= minBPresses && this.canUndoB()) {
            long excessBPressesX = (long) Math.ceil(((double) CurrentX - TargetX) / ButtonBX);
            long excessBPressesY = (long) Math.ceil(((double) CurrentY - TargetY) / ButtonBY);
            long bPressesToRemove = Math.max(excessBPressesX, excessBPressesY);
            if (bPressesToRemove > BPresses) {
                return 0;
            }
            this.undoB(bPressesToRemove);

            long belowAPressesX = (long) Math.ceil(((double) TargetX - CurrentX) / ButtonAX);
            long belowAPressesY = (long) Math.ceil(((double) TargetY - CurrentY) / ButtonAY);
            long aPressesToAdd = Math.max(belowAPressesX, belowAPressesY);
            this.pressA(aPressesToAdd);

            if (this.isOnTarget()) {
                return APresses * COST_A + BPresses * COST_B;
            }
        }

        return 0;
    }
}
