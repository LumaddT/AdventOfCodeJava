package advent.of.code.solutions.y2020.day07;

import advent.of.code.solutions.Day;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day07 implements Day {
    private static final String IMPORTANT_BAG_NAME = "shiny gold";

    @Override
    public String part1(String inputString) {
        Bag importantBag = parseInputString(inputString);
        Set<Bag> containImportantBag = new HashSet<>(importantBag.getContainedIn());

        int setSize = containImportantBag.size();

        while (true) {
            containImportantBag.addAll(containImportantBag.stream()
                    .flatMap(b -> b.getContainedIn().stream())
                    .collect(Collectors.toSet())
            );

            if (containImportantBag.size() == setSize) {
                return String.format("%d", setSize);
            }

            setSize = containImportantBag.size();
        }
    }

    @Override
    public String part2(String inputString) {
        Bag importantBag = parseInputString(inputString);

        return String.format("%d", importantBag.totalBagsContained());
    }

    private Bag parseInputString(String inputString) {
        Map<String, Bag> bags = new HashMap<>();
        String[] inputSplit = inputString.split("\\.\n");

        for (String line : inputSplit) {
            String bagName = line.split(" bags ")[0];

            bags.put(bagName, new Bag());
        }

        for (String line : inputSplit) {
            String[] bagRequirements = line.split(" bags?,? ?(contain )?");

            if (bagRequirements[1].equals("no other")) {
                continue;
            }

            Bag bag = bags.get(bagRequirements[0]);

            for (String requirement : bagRequirements) {
                if (requirement.equals(bagRequirements[0])) {
                    continue;
                }

                int amount = Integer.parseInt(requirement.split(" ")[0]);
                String requiredBagName = requirement.substring(requirement.indexOf(' ') + 1);

                bag.addMustContain(bags.get(requiredBagName), amount);
            }
        }

        return bags.get(IMPORTANT_BAG_NAME);
    }
}
