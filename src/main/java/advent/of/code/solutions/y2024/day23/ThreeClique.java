package advent.of.code.solutions.y2024.day23;

import java.util.List;
import java.util.stream.Stream;

record ThreeClique(Node First, Node Second, Node Third) {
    public ThreeClique(Node First, Node Second, Node Third) {
        List<Node> sorted = Stream.of(First, Second, Third).sorted().toList();

        this.First = sorted.get(0);
        this.Second = sorted.get(1);
        this.Third = sorted.get(2);
    }

    public boolean nodeStartsWith(char ch) {
        return First.getName().charAt(0) == ch
                || Second.getName().charAt(0) == ch
                || Third.getName().charAt(0) == ch;
    }
}
