package advent.of.code.solutions.y2019.day25;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;
import advent.of.code.utils.ntuples.Tuple;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day25 implements Day {
    private static final Set<String> FORBIDDEN_OBJECTS = Set.of("escape pod", "giant electromagnet", "infinite loop", "molten lava", "photons");

    private static final String PRESSURE_PLATE_ROOM_IDENTIFIER = "pressure plate room";

    private static final String DIRECTIONS_HEADER = "Doors here lead:";
    private static final String OBJECTS_HEADER = "Items here:";

    private static final String PRESSURE_PLATE_ROOM_STRING = "== Pressure-Sensitive Floor ==";

    private static final String SOLVED_STATE_REGEX = "You should be able to get in by typing (\\d+) on the keypad at the main airlock";

    private static final int OBJECTS_AMOUNT = 8;

    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        Map<String, List<String>> objectsToRooms = findPathsToObjects(program);
        List<String> objects = objectsToRooms.keySet().stream()
                .filter(s -> !s.equals(PRESSURE_PLATE_ROOM_IDENTIFIER))
                .toList();

        if (objects.size() != OBJECTS_AMOUNT) {
            throw new RuntimeException("Expected %d objects but found %d.".formatted(OBJECTS_AMOUNT, objects.size()));
        }

        takeAllObjects(program, objects, objectsToRooms);

        List<String> pathToPlate = objectsToRooms.get(PRESSURE_PLATE_ROOM_IDENTIFIER);
        navigate(program, pathToPlate.subList(0, pathToPlate.size() - 1));
        // At this point it's in the room before the plate

        return getCode(program, objects, objectsToRooms.get(PRESSURE_PLATE_ROOM_IDENTIFIER).getLast());
    }

    @Override
    public String part2(String inputString) {
        return "No part 2 on Christmas.";
    }

    private String getFullOutput(IntcodeProgram program) {
        StringBuilder returnValue = new StringBuilder();
        IntcodeProgram.ResultCode result;

        do {
            result = program.executeAll();

            if (result == IntcodeProgram.ResultCode.WROTE_OUTPUT) {
                returnValue.append((char) Math.toIntExact(program.readOutput()));
            }
        } while (result == IntcodeProgram.ResultCode.WROTE_OUTPUT);

        return returnValue.toString();
    }

    private void writeInput(IntcodeProgram program, String input) {
        for (char ch : input.toCharArray()) {
            program.setInput(ch);
            program.executeAll();
        }

        program.setInput(10);
    }

    private Map<String, List<String>> findPathsToObjects(IntcodeProgram program) {
        Map<String, List<String>> objectsToRooms = new HashMap<>();

        Queue<List<String>> toCheck = new LinkedList<>();
        toCheck.add(new ArrayList<>());

        while (!toCheck.isEmpty()) {
            List<String> routeFromStart = toCheck.poll();
            if (routeFromStart == null) {
                throw new IllegalStateException("toCheck was empty. This cannot happen.");
            }
            navigate(program, routeFromStart);

            String roomString = getFullOutput(program);

            if (roomString.contains(PRESSURE_PLATE_ROOM_STRING)) {
                objectsToRooms.put(PRESSURE_PLATE_ROOM_IDENTIFIER, routeFromStart);
                navigate(program, invertPath(routeFromStart.subList(0, routeFromStart.size() - 1)));
                continue;
            }

            Tuple<Set<String>, Set<String>> parsedRoom = parseRoom(roomString);
            Set<String> allowedDirections = parsedRoom.First();
            Set<String> nativeObjects = parsedRoom.Second();

            for (String object : nativeObjects) {
                objectsToRooms.put(object, routeFromStart);
            }

            for (String cardinalDirection : allowedDirections) {
                if (!routeFromStart.isEmpty() && cardinalDirection.equals(oppositeCardinal(routeFromStart.getLast()))) {
                    continue;
                }

                List<String> newRoute = new ArrayList<>(routeFromStart);
                newRoute.add(cardinalDirection);
                toCheck.add(newRoute);
            }

            navigate(program, invertPath(routeFromStart));
        }

        return Collections.unmodifiableMap(objectsToRooms);
    }

    private void navigate(IntcodeProgram program, List<String> path) {
        for (String cardinalDirection : path) {
            getFullOutput(program);
            writeInput(program, cardinalDirection);
        }
    }

    private Tuple<Set<String>, Set<String>> parseRoom(String roomString) {
        Set<String> allowedDirections = new HashSet<>();
        Set<String> nativeObjects = new HashSet<>();

        String[][] splitRoomString = Arrays.stream(roomString.trim().split("\n\n"))
                .map(s -> s.split("\n"))
                .toArray(String[][]::new);

        if (splitRoomString[1][0].equals(DIRECTIONS_HEADER)) {
            for (int i = 1; i < splitRoomString[1].length; i++) {
                allowedDirections.add(splitRoomString[1][i].substring(2));
            }
        }

        if (splitRoomString[2][0].equals(OBJECTS_HEADER)) {
            for (int i = 1; i < splitRoomString[2].length; i++) {
                String object = splitRoomString[2][i].substring(2);
                if (!FORBIDDEN_OBJECTS.contains(object)) {
                    nativeObjects.add(object);
                }
            }
        }

        return new Tuple<>(allowedDirections, nativeObjects);
    }

    private String oppositeCardinal(String cardinal) {
        return switch (cardinal) {
            case "north" -> "south";
            case "south" -> "north";
            case "west" -> "east";
            case "east" -> "west";
            default -> throw new IllegalStateException("Unexpected value: " + cardinal);
        };
    }

    private void takeAllObjects(IntcodeProgram program, List<String> objects, Map<String, List<String>> objectsToRooms) {
        for (String object : objects) {
            List<String> path = objectsToRooms.get(object);

            getFullOutput(program);
            navigate(program, path);
            getFullOutput(program);

            writeInput(program, "take %s".formatted(object));

            getFullOutput(program);
            navigate(program, invertPath(path));
            getFullOutput(program);
        }
    }

    private List<String> invertPath(List<String> path) {
        return path.reversed().stream()
                .map(this::oppositeCardinal)
                .toList();
    }

    private String getCode(IntcodeProgram program, List<String> objects, String plateDirection) {
        Pattern solvedStatePattern = Pattern.compile(SOLVED_STATE_REGEX);

        for (int i = 0; i < (int) Math.pow(2, OBJECTS_AMOUNT); i++) {
            dropAllObjects(program, objects);
            for (int objectIndex = 0; objectIndex < OBJECTS_AMOUNT; objectIndex++) {
                boolean shouldTake = ((i >> objectIndex) & 1) == 1;
                if (shouldTake) {
                    writeInput(program, "take %s".formatted(objects.get(objectIndex)));
                    getFullOutput(program);
                }
            }

            writeInput(program, plateDirection);
            String output = getFullOutput(program);

            Matcher matcher = solvedStatePattern.matcher(output);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        throw new RuntimeException("No combination of objects solved the puzzle.");
    }

    private void dropAllObjects(IntcodeProgram program, List<String> objects) {
        getFullOutput(program);
        for (String object : objects) {
            writeInput(program, "drop %s".formatted(object));
            getFullOutput(program);
        }
    }
}
