package advent.of.code.solutions.y2020.day21;

import advent.of.code.solutions.Day;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day21 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Food> foods = Arrays.stream(inputString.split("\n"))
                .map(Food::new)
                .collect(Collectors.toSet());
        Map<String, String> allergensToIngredients = findAllergensToIngredients(foods);

        int total = 0;

        for (String ingredient : foods.stream().flatMap(f -> f.getIngredients().stream()).collect(Collectors.toSet())) {
            if (allergensToIngredients.containsValue(ingredient)) {
                continue;
            }

            total += foods.stream().filter(f -> f.getIngredients().contains(ingredient)).toList().size();
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Set<Food> foods = Arrays.stream(inputString.split("\n"))
                .map(Food::new)
                .collect(Collectors.toSet());
        Map<String, String> allergensToIngredients = findAllergensToIngredients(foods);

        StringBuilder canonicalList = new StringBuilder();
        for (String allergen : foods.stream().flatMap(f -> f.getAllergens().stream()).collect(Collectors.toSet()).stream().sorted().toList()) {
            canonicalList.append(allergensToIngredients.get(allergen));
            canonicalList.append(',');
        }

        canonicalList.setLength(canonicalList.length() - 1);

        return canonicalList.toString();
    }

    private Map<String, String> findAllergensToIngredients(Set<Food> foods) {
        Set<String> ingredients = foods.stream().flatMap(f -> f.getIngredients().stream()).collect(Collectors.toSet());
        Set<String> allergens = foods.stream().flatMap(f -> f.getAllergens().stream()).collect(Collectors.toSet());

        Map<String, Set<String>> candidateIngredients = new HashMap<>();

        for (String allergen : allergens) {
            Set<String> currentCandidateIngredients = new HashSet<>();
            Set<Food> foodsWithAllergen = foods.stream()
                    .filter(f -> f.getAllergens().contains(allergen))
                    .collect(Collectors.toSet());

            for (String ingredient : ingredients) {
                if (foodsWithAllergen.size() == foodsWithAllergen.stream()
                        .filter(f -> f.getIngredients().contains(ingredient))
                        .count()) {
                    currentCandidateIngredients.add(ingredient);
                }
            }

            candidateIngredients.put(allergen, currentCandidateIngredients);
        }

        Set<String> confirmedIngredients = new HashSet<>();
        while (candidateIngredients.values().stream().mapToInt(Set::size).max().orElseThrow() > 1) {
            for (String allergen : allergens) {
                if (candidateIngredients.get(allergen).size() == 1) {
                    confirmedIngredients.add(candidateIngredients.get(allergen).stream().findAny().orElseThrow());
                } else {
                    candidateIngredients.get(allergen).removeAll(confirmedIngredients);
                }
            }
        }

        Map<String, String> allergensToIngredients = new HashMap<>();

        for (String allergen : allergens) {
            allergensToIngredients.put(allergen, candidateIngredients.get(allergen).stream().findAny().orElseThrow());
        }

        return Collections.unmodifiableMap(allergensToIngredients);
    }
}
