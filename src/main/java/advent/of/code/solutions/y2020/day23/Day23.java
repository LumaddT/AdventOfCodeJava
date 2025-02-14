package advent.of.code.solutions.y2020.day23;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements Day {
    private final static int MOVES_PART_1 = 100;

    private final static int CUPS_PART_2 = 1_000_000;
    private final static int MOVES_PART_2 = 10_000_000;

    @Override
    public String part1(String inputString) {
        Map<Integer, Node> nodes = parseInputString(inputString, inputString.trim().length());
        Node activeNode = nodes.get(inputString.charAt(0) - '0');

        for (int i = 0; i < MOVES_PART_1; i++) {
            playRound(activeNode, nodes);
            activeNode = activeNode.getNext();
        }

        Node currentNode = nodes.get(1).getNext();
        StringBuilder returnValue = new StringBuilder();

        for (int i = 1; i < nodes.size(); i++) {
            returnValue.append(currentNode.getValue());
            currentNode = currentNode.getNext();
        }

        return returnValue.toString();
    }

    @Override
    public String part2(String inputString) {
        Map<Integer, Node> nodes = parseInputString(inputString, CUPS_PART_2);
        Node activeNode = nodes.get(inputString.charAt(0) - '0');

        for (int i = 0; i < MOVES_PART_2; i++) {
            playRound(activeNode, nodes);
            activeNode = activeNode.getNext();
        }

        Node node1 = nodes.get(1);

        return String.format("%d", (long) node1.getNext().getValue() * (long) node1.getNext().getNext().getValue());
    }

    private Map<Integer, Node> parseInputString(String inputString, int cups) {
        Map<Integer, Node> returnValue = new HashMap<>();
        List<Node> nodes = new ArrayList<>();

        List<Integer> nodeValues = inputString.trim().chars().mapToObj(ch -> ch - '0').toList();

        for (int value : nodeValues) {
            Node newNode = new Node(value);
            returnValue.put(value, newNode);
            nodes.add(newNode);
        }

        for (int value = nodes.size() + 1; value <= cups; value++) {
            Node newNode = new Node(value);
            returnValue.put(value, newNode);
            nodes.add(newNode);
        }

        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setNext(nodes.get((i + 1) % nodes.size()));
        }

        return Collections.unmodifiableMap(returnValue);
    }

    private void playRound(Node activeNode, Map<Integer, Node> nodes) {
        Node firstTaken = activeNode.getNext();

        activeNode.setNext(firstTaken.getNext().getNext().getNext());

        Set<Integer> illegalValues = Set.of(
                firstTaken.getValue(),
                firstTaken.getNext().getValue(),
                firstTaken.getNext().getNext().getValue()
        );

        int insertValue = activeNode.getValue() - 1;
        if (insertValue < 1) {
            insertValue += nodes.size();
        }

        while (illegalValues.contains(insertValue)) {
            insertValue--;
            if (insertValue < 1) {
                insertValue += nodes.size();
            }
        }

        Node insertNode = nodes.get(insertValue);

        firstTaken.getNext().getNext().setNext(insertNode.getNext());
        insertNode.setNext(firstTaken);
    }
}
