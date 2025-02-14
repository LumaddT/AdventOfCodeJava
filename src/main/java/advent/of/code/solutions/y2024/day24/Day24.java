package advent.of.code.solutions.y2024.day24;

import advent.of.code.solutions.Day;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day24 implements Day {
    @Override
    public String part1(String inputString) {
        Map<String, Node> input = parseInputString(inputString);

        Set<Node> zNodes = getNodes(input.values(), 'z');

        for (Node node : zNodes) {
            node.execute();
        }

        return String.format("%d", nodesToLong(input.values(), 'z'));
    }

    /**
     * I solved this part manually. First I printed all operations in alphabetic order
     * making sure the left operand is alphabetically before the right operand. This made analyzing
     * them a bit easier.
     * On the result, I run the following regexes:
     * "^[^x].. XOR [^y].. -> ([^z]..)$"
     * "^[^x].. AND [^y].. -> (z..)$"
     * "^x.. AND y.. -> (z..)$"
     * "^[^x].. OR [^y].. -> ([z]..)$"
     * With the exclusion of z45, which the last regex should match, I captured 6 strings. All 6 of
     * those are involved in a swap. All results of XORs should always be a z bit, while a z bit
     * should never be the result of an AND or a OR with those characteristics.
     * I then swapped those in the input with an appropriate enough match, added the x and y numbers
     * together, printed them in binary and compared the result with the result I got from applying
     * all the operations. The swaps were not perfect, but they were close enough to highlight the general
     * area of the last swap. That swap did not involve z bits, but it required slightly more analysis.
     * The result of an xXX AND yXX should always be put in OR with something else, while the result
     * of an xXX XOR yXX should always be ANDed and XORed with something else. I could find a situation where
     * the opposite was true, and added those to the list of swaps. The list happened to be correct.
     */
    @Override
    public String part2(String inputString) {
        return "Solved manually";
    }

    private Map<String, Node> parseInputString(String inputString) {
        String[] secondPart = inputString.split("\n\n")[1].split("\n");
        Map<String, Node> returnValue = new HashMap<>();

        for (String line : secondPart) {
            String[] lineSplit = line.split(" ");

            returnValue.putIfAbsent(lineSplit[0], new Node(lineSplit[0]));
            returnValue.putIfAbsent(lineSplit[2], new Node(lineSplit[2]));
            returnValue.putIfAbsent(lineSplit[4], new Node(lineSplit[4]));

            returnValue.get(lineSplit[4]).setOperation(lineSplit[1]);
        }

        for (String line : secondPart) {
            String[] lineSplit = line.split(" ");
            returnValue.get(lineSplit[4]).setPrevious(returnValue.get(lineSplit[0]), returnValue.get(lineSplit[2]));
        }

        String[] firstPart = inputString.split("\n\n")[0].split("\n");

        for (String line : firstPart) {
            String[] lineSplit = line.split(": ");
            returnValue.get(lineSplit[0]).setValue(lineSplit[1]);
        }

        for (Node node : returnValue.values()) {
            if (!node.verify()) {
                throw new RuntimeException("Bad node found.");
            }
        }

        return returnValue;
    }

    private Set<Node> getNodes(Collection<Node> nodes, char ch) {
        if (ch != 'x' && ch != 'y' && ch != 'z') {
            throw new RuntimeException(String.format("Bad character %c.", ch));
        }

        return nodes.stream().filter(n -> n.getName().charAt(0) == ch).collect(Collectors.toSet());
    }

    @SuppressWarnings("SameParameterValue")
    private long nodesToLong(Collection<Node> nodes, char ch) {
        Set<Node> chNodes = getNodes(nodes, ch);

        long returnValue = 0;

        for (Node node : chNodes) {
            if (node.getValue()) {
                returnValue += (long) Math.pow(2, Integer.parseInt(node.getName().substring(1)));
            }
        }

        return returnValue;
    }


}
