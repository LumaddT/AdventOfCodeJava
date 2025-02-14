package advent.of.code.solutions.y2022.day17;

import java.util.List;

class Rocks {
    private static final Boolean[][] Rock1 = {
            {true, true, true, true}
    };

    private static final Boolean[][] Rock2 = {
            {false, true, false},
            {true, true, true},
            {false, true, false}
    };

    private static final Boolean[][] Rock3 = {
            {true, true, true},
            {false, false, true},
            {false, false, true}
    };

    private static final Boolean[][] Rock4 = {
            {true},
            {true},
            {true},
            {true}
    };

    private static final Boolean[][] Rock5 = {
            {true, true},
            {true, true}
    };

    public static final List<Boolean[][]> ALL_ROCKS = List.of(Rock1, Rock2, Rock3, Rock4, Rock5);
}
