package advent.of.code.solutions.y2019.day14;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day14 implements Day {
    private static final long ORE_PART_2 = 1_000_000_000_000L;

    @Override
    public String part1(String inputString) {
        Map<String, Reaction> input = parseInputString(inputString);

        Reaction fuel = input.get("FUEL");
        Map<Reaction, Long> bank = new HashMap<>();
        for (Reaction reaction : input.values()) {
            bank.put(reaction, 0L);
        }

        return String.format("%d", fuel.getOreQuantity(1, bank));
    }

    @Override
    public String part2(String inputString) {
        Map<String, Reaction> input = parseInputString(inputString);

        Reaction fuel = input.get("FUEL");
        Map<Reaction, Long> bank = new HashMap<>();
        for (Reaction reaction : input.values()) {
            bank.put(reaction, 0L);
        }

        long oreLeft = ORE_PART_2;
        int total = 0;
        int tryProduce = 10_000_000;

        while (tryProduce > 0) {
            Map<Reaction, Long> oldBank = new HashMap<>(bank);

            long oreNeeded = fuel.getOreQuantity(tryProduce, bank);
            if (oreNeeded > oreLeft) {
                bank = oldBank;
                tryProduce /= 10;
            } else {
                oreLeft -= oreNeeded;
                total += tryProduce;
            }
        }

        return String.format("%d", total);
    }

    private Map<String, Reaction> parseInputString(String inputString) {
        String[][] splitInputString = Arrays.stream(inputString.split("\n"))
                .map(l -> l.split(" => "))
                .toArray(String[][]::new);

        Map<String, Reaction> returnValue = new HashMap<>();
        returnValue.put("ORE", new Reaction(1, "ORE"));

        for (String[] line : splitInputString) {
            String[] splitOutput = line[1].split(" ");

            int quantity = Integer.parseInt(splitOutput[0]);
            String output = splitOutput[1];

            returnValue.put(output, new Reaction(quantity, output));
        }

        for (String[] line : splitInputString) {
            String output = line[1].split(" ")[1];

            Reaction reaction = returnValue.get(output);
            Set<Ingredient> ingredients = new HashSet<>();

            String[] splitIngredientsString = line[0].split(", ");
            for (String ingredientString : splitIngredientsString) {
                String[] splitIngredient = ingredientString.split(" ");
                int ingredientQuantity = Integer.parseInt(splitIngredient[0]);
                String ingredientChemical = splitIngredient[1];
                Reaction ingredient = returnValue.get(ingredientChemical);

                ingredients.add(new Ingredient(ingredientQuantity, ingredient));
            }

            reaction.setIngredients(ingredients);
        }

        return Collections.unmodifiableMap(returnValue);
    }
}
