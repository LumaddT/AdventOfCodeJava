package advent.of.code.solutions.y2019.day03;

import advent.of.code.utils.Directions;
import advent.of.code.utils.ParsingUtils;
import lombok.Getter;

@Getter
public class Instruction {
    private final Directions Direction;
    private final int Amount;

    public Instruction(String inputString) {
        Direction = ParsingUtils.letterToDirection(inputString.charAt(0));
        Amount = Integer.parseInt(inputString.substring(1));
    }
}
