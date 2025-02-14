package advent.of.code.solutions.y2020.day23;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Node {
    private final int Value;
    @Setter
    private Node Next;

    public Node(int value) {
        Value = value;
    }
}
