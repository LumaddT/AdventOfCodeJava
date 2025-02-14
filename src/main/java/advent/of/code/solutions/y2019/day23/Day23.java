package advent.of.code.solutions.y2019.day23;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements Day {
    private static final int COMPUTERS_AMOUNT = 50;

    @Override
    public String part1(String inputString) {
        IntcodeProgram[] programs = initializePrograms(inputString);
        Map<IntcodeProgram, Queue<Message>> inputQueues = initializeQueues(programs);

        while (true) {
            for (IntcodeProgram program : programs) {
                switch (program.executeAll()) {
                    case WANTS_INPUT -> {
                        if (inputQueues.get(program).isEmpty()) {
                            program.setInput(-1);
                        } else {
                            setInput(program, inputQueues.get(program));
                        }
                    }
                    case WROTE_OUTPUT -> {
                        Message newMessage;
                        newMessage = getMessage(program);

                        if (newMessage.Destination() == 255) {
                            return String.format("%d", newMessage.Y());
                        }

                        inputQueues.get(programs[newMessage.Destination()]).add(newMessage);
                    }
                    case HALTED -> throw new RuntimeException("Program halted anomalously.");
                }
            }
        }
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram[] programs = initializePrograms(inputString);
        Map<IntcodeProgram, Queue<Message>> inputQueues = initializeQueues(programs);

        Message lastMessageToNAT = null;
        Long lastYSent = null;

        while (true) {
            boolean lastInputSentWasMessage = false;
            for (IntcodeProgram program : programs) {
                switch (program.executeAll()) {
                    case WANTS_INPUT -> {
                        if (inputQueues.get(program).isEmpty()) {
                            program.setInput(-1);
                        } else {
                            setInput(program, inputQueues.get(program));
                            lastInputSentWasMessage = true;
                        }
                    }
                    case WROTE_OUTPUT -> {
                        Message newMessage;
                        newMessage = getMessage(program);

                        if (newMessage.Destination() == 255) {
                            lastMessageToNAT = newMessage;
                        } else {
                            inputQueues.get(programs[newMessage.Destination()]).add(newMessage);
                        }
                    }
                    case HALTED -> throw new RuntimeException("Program halted anomalously.");
                }
            }

            if (!lastInputSentWasMessage && inputQueues.values().stream().mapToInt(Collection::size).sum() == 0) {
                if (lastMessageToNAT == null) {
                    throw new RuntimeException("Bad logic: no message was sent to NAT but queues are empty.");
                }

                if (lastYSent != null && lastMessageToNAT.Y() == lastYSent) {
                    return String.format("%d", lastYSent);
                } else {
                    lastYSent = lastMessageToNAT.Y();
                    inputQueues.get(programs[0]).add(lastMessageToNAT);
                }
            }
        }
    }

    private IntcodeProgram[] initializePrograms(String inputString) {
        IntcodeProgram[] programs = new IntcodeProgram[COMPUTERS_AMOUNT];
        for (int i = 0; i < COMPUTERS_AMOUNT; i++) {
            programs[i] = new IntcodeProgram(inputString);
            programs[i].setInput(i);
            programs[i].executeAll();
            programs[i].setInput(-1);
        }

        return programs;
    }

    private Map<IntcodeProgram, Queue<Message>> initializeQueues(IntcodeProgram[] programs) {
        Map<IntcodeProgram, Queue<Message>> inputQueues = new HashMap<>();

        for (IntcodeProgram program : programs) {
            inputQueues.put(program, new LinkedList<>());
        }

        return inputQueues;
    }

    private void setInput(IntcodeProgram program, Queue<Message> messages) {
        if (messages.isEmpty()) {
            throw new RuntimeException("Bad call to setInput with empty messages queue.");
        }

        Message message = messages.poll();

        program.setInput(message.X());
        program.executeAll();

        program.setInput(message.Y());
    }

    private Message getMessage(IntcodeProgram program) {
        int destination = program.readOutputAsInt();

        program.executeAll();
        long x = program.readOutput();

        program.executeAll();
        long y = program.readOutput();

        return new Message(destination, x, y);
    }
}
