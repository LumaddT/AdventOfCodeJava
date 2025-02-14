package advent.of.code.solutions.y2022.day16;

import advent.of.code.solutions.Day;
import advent.of.code.utils.SetUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day16 implements Day {
    private static final int TIME_PART_1 = 30;
    private static final int TIME_PART_2 = 26;

    private static final String ENTRANCE_NODE = "AA";

    @Override
    public String part1(String inputString) {
        Node entrance = parseInputString(inputString);

        Node[] currentNodes = {entrance};
        int[] timeLefts = {TIME_PART_1};
        List<Node> closedNodes = entrance.getEffectiveDistances().keySet().stream()
                .sorted(Comparator.comparing(Node::getName))
                .toList();

        return String.format("%d", calculateMaxFlow(currentNodes, timeLefts, 0, closedNodes, new HashMap<>()));
    }

    /**
     * Part 2 takes a good 40 seconds on my machine. There has to be a better way,
     * but I can't think of any right now. Maybe I waste too much time copying the
     * arrays and the closedNodes list, or maybe I can do better caching.
     */
    @Override
    public String part2(String inputString) {
        Node entrance = parseInputString(inputString);
        Node[] currentNodes = {entrance, entrance};
        int[] timeLefts = {TIME_PART_2, TIME_PART_2};
        List<Node> closedNodes = entrance.getEffectiveDistances().keySet().stream()
                .sorted(Comparator.comparing(Node::getName))
                .toList();

        return String.format("%d", calculateMaxFlow(currentNodes, timeLefts, 0, closedNodes, new HashMap<>()));
    }

    private Node parseInputString(String inputString) {
        Map<String, Node> nodes = new HashMap<>();
        String regex = "Valve (..) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)\n";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        while (matcher.find()) {
            String name = matcher.group(1);
            int flowRate = Integer.parseInt(matcher.group(2));

            nodes.put(name, new Node(name, flowRate));
        }

        matcher = pattern.matcher(inputString);

        while (matcher.find()) {
            String name = matcher.group(1);
            String[] neighbors = matcher.group(3).split(", ");

            for (String neighbor : neighbors) {
                nodes.get(name).addNeighbor(nodes.get(neighbor));
            }
        }

        calculateEffectiveDistances(nodes);

        return nodes.get(ENTRANCE_NODE);
    }

    private void calculateEffectiveDistances(Map<String, Node> nodes) {
        Set<Node> nonZeroNodes = nodes.values().stream()
                .filter(n -> n.getFlowRate() > 0)
                .collect(Collectors.toSet());

        for (Node source : SetUtils.add(nonZeroNodes, nodes.get(ENTRANCE_NODE))) {
            for (Node destination : nonZeroNodes) {
                if (source == destination) {
                    continue;
                }

                int distance = 1;
                Set<Node> currentLayer = source.getNeighbors();

                while (!currentLayer.contains(destination)) {
                    currentLayer = currentLayer.stream()
                            .map(Node::getNeighbors)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet());
                    distance++;
                }

                source.addEffectiveDistance(destination, distance);
            }
        }
    }

    private int calculateMaxFlow(Node[] currentNodes, int[] timeLefts, int lastOpenedNodeFlow, List<Node> closedNodes, Map<String, Integer> cache) {
        String cacheKey = generateCacheKey(timeLefts, currentNodes, closedNodes);

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey) + lastOpenedNodeFlow;
        }

        int returnValue = 0;

        for (int i = 0; i < currentNodes.length; i++) {
            Node currentNode = currentNodes[i];
            int timeLeft = timeLefts[i];

            for (Node closedNode : closedNodes) {
                int timeToOpen = currentNode.getEffectiveDistances().get(closedNode) + 1;

                if (timeLeft > timeToOpen) {
                    Node[] currentNodesCopy = Arrays.copyOf(currentNodes, currentNodes.length);
                    int[] timeLeftsCopy = Arrays.copyOf(timeLefts, currentNodes.length);
                    List<Node> closedNodesCopy = new ArrayList<>(closedNodes);
                    closedNodesCopy.remove(closedNode);

                    currentNodesCopy[i] = closedNode;
                    timeLeftsCopy[i] = timeLeft - timeToOpen;

                    int potentialReturnValue = calculateMaxFlow(currentNodesCopy, timeLeftsCopy, closedNode.getPotentialTotalFlow(timeLeft - timeToOpen), closedNodesCopy, cache);
                    returnValue = Math.max(returnValue, potentialReturnValue);
                }
            }
        }

        cache.put(cacheKey, returnValue);
        return returnValue + lastOpenedNodeFlow;
    }

    private String generateCacheKey(int[] timeLefts, Node[] currentNodes, List<Node> closedNodes) {
        StringBuilder returnValue = new StringBuilder();

        // Ugly, but there are at most 2 elements in each array
        // I'd like to do this with arrays of arbitrary length, but it's not worth it for the puzzle
        if (currentNodes.length == 1) {
            returnValue.append(timeLefts[0]);
            returnValue.append('|');
            returnValue.append(currentNodes[0].getName());
            returnValue.append('|');
        } else if (currentNodes[0].getName().compareTo(currentNodes[1].getName()) < 0) {
            returnValue.append(timeLefts[0]);
            returnValue.append('|');
            returnValue.append(timeLefts[1]);
            returnValue.append('|');
            returnValue.append(currentNodes[0].getName());
            returnValue.append('|');
            returnValue.append(currentNodes[1].getName());
            returnValue.append('|');
        } else {
            returnValue.append(timeLefts[1]);
            returnValue.append('|');
            returnValue.append(timeLefts[0]);
            returnValue.append('|');
            returnValue.append(currentNodes[1].getName());
            returnValue.append('|');
            returnValue.append(currentNodes[0].getName());
            returnValue.append('|');
        }

        returnValue.append('|');

        // closedNodes is already sorted
        for (Node closedNode : closedNodes) {
            returnValue.append(closedNode.getName());
            returnValue.append('|');
        }

        return returnValue.toString();
    }
}
