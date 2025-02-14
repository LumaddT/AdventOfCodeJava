package advent.of.code.solutions.y2022.day22;

import advent.of.code.utils.Directions;
import advent.of.code.utils.ParsingUtils;
import lombok.Getter;

@Getter
public class Instruction {
    private final Directions Rotation;
    private final int Amount;

    public Instruction(String inputString) {
        if (inputString.charAt(inputString.length() - 1) == '\n') {
            Rotation = null;
        } else {
            Rotation = ParsingUtils.letterToDirection(inputString.charAt(inputString.length() - 1));
        }

        Amount = Integer.parseInt(inputString.substring(0, inputString.length() - 1));
    }
}
