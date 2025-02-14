package advent.of.code.utils.screenParser;

import java.util.List;
import java.util.Map;

/**
 * Tables copied from
 * <a href="https://github.com/fornwall/advent-of-code/blob/f6be3f06dc0239d3c7a2a9a7dfc9f9fe764c004b/crates/core/src/common/character_recognition.rs">
 * https://github.com/fornwall/advent-of-code/blob/f6be3f06dc0239d3c7a2a9a7dfc9f9fe764c004b/crates/core/src/common/character_recognition.rs
 * </a>
 */
class Letters {
    static final Boolean[][] LetterA = {{false, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, true, true, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false}};

    static final Boolean[][] LetterB = {{true, true, true, false, false},
            {true, false, false, true, false},
            {true, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, true, true, false, false}};

    static final Boolean[][] LetterC = {{false, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, false, false, true, false},
            {false, true, true, false, false}};

    static final Boolean[][] LetterE = {{true, true, true, true, false},
            {true, false, false, false, false},
            {true, true, true, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, true, true, true, false}};

    static final Boolean[][] LetterF = {{true, true, true, true, false},
            {true, false, false, false, false},
            {true, true, true, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false}};

    static final Boolean[][] LetterG = {{false, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, false, false},
            {true, false, true, true, false},
            {true, false, false, true, false},
            {false, true, true, true, false}};

    static final Boolean[][] LetterH = {{true, false, false, true, false},
            {true, false, false, true, false},
            {true, true, true, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false}};

    static final Boolean[][] LetterI = {{false, true, true, true, false},
            {false, false, true, false, false},
            {false, false, true, false, false},
            {false, false, true, false, false},
            {false, false, true, false, false},
            {false, true, true, true, false}};

    static final Boolean[][] LetterJ = {{false, false, true, true, false},
            {false, false, false, true, false},
            {false, false, false, true, false},
            {false, false, false, true, false},
            {true, false, false, true, false},
            {false, true, true, false, false}};

    static final Boolean[][] LetterK = {{true, false, false, true, false},
            {true, false, true, false, false},
            {true, true, false, false, false},
            {true, false, true, false, false},
            {true, false, true, false, false},
            {true, false, false, true, false}};

    static final Boolean[][] LetterL = {{true, false, false, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {true, true, true, true, false}};

    static final Boolean[][] LetterO = {{false, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {false, true, true, false, false}};

    static final Boolean[][] LetterP = {{true, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, true, true, false, false},
            {true, false, false, false, false},
            {true, false, false, false, false}};

    static final Boolean[][] LetterR = {{true, true, true, false, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, true, true, false, false},
            {true, false, true, false, false},
            {true, false, false, true, false}};

    static final Boolean[][] LetterS = {{false, true, true, true, false},
            {true, false, false, false, false},
            {true, false, false, false, false},
            {false, true, true, false, false},
            {false, false, false, true, false},
            {true, true, true, false, false}};

    static final Boolean[][] LetterU = {{true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {false, true, true, false, false}};

    static final Boolean[][] LetterY = {{true, false, false, false, true},
            {true, false, false, false, true},
            {false, true, false, true, false},
            {false, false, true, false, false},
            {false, false, true, false, false},
            {false, false, true, false, false}};

    static final Boolean[][] LetterZ = {{true, true, true, true, false},
            {false, false, false, true, false},
            {false, false, true, false, false},
            {false, true, false, false, false},
            {true, false, false, false, false},
            {true, true, true, true, false}};

    static final List<Boolean[][]> LETTERS_LIST = List.of(LetterA, LetterB, LetterC, LetterE, LetterF, LetterG, LetterH, LetterI, LetterJ, LetterK, LetterL, LetterO, LetterP, LetterR, LetterS, LetterU, LetterY, LetterZ);

    static final Map<Character, Boolean[][]> CHAR_TO_TABLE = Map.ofEntries(
            Map.entry('A', LetterA),
            Map.entry('B', LetterB),
            Map.entry('C', LetterC),
            Map.entry('E', LetterE),
            Map.entry('F', LetterF),
            Map.entry('G', LetterG),
            Map.entry('H', LetterH),
            Map.entry('I', LetterI),
            Map.entry('J', LetterJ),
            Map.entry('K', LetterK),
            Map.entry('L', LetterL),
            Map.entry('O', LetterO),
            Map.entry('P', LetterP),
            Map.entry('R', LetterR),
            Map.entry('S', LetterS),
            Map.entry('U', LetterU),
            Map.entry('Y', LetterY),
            Map.entry('Z', LetterZ));

    static final Map<Boolean[][], Character> TABLE_TO_CHAR = Map.ofEntries(
            Map.entry(LetterA, 'A'),
            Map.entry(LetterB, 'B'),
            Map.entry(LetterC, 'C'),
            Map.entry(LetterE, 'E'),
            Map.entry(LetterF, 'F'),
            Map.entry(LetterG, 'G'),
            Map.entry(LetterH, 'H'),
            Map.entry(LetterI, 'I'),
            Map.entry(LetterJ, 'J'),
            Map.entry(LetterK, 'K'),
            Map.entry(LetterL, 'L'),
            Map.entry(LetterO, 'O'),
            Map.entry(LetterP, 'P'),
            Map.entry(LetterR, 'R'),
            Map.entry(LetterS, 'S'),
            Map.entry(LetterU, 'U'),
            Map.entry(LetterY, 'Y'),
            Map.entry(LetterZ, 'Z'));
}
