package advent.of.code.solutions.y2020.day21;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Food {
    private final Set<String> Ingredients;
    private final Set<String> Allergens;

    public Food(String inputString) {
        String[] inputSplit = inputString.split(" \\(contains |\\)");

        Ingredients = Arrays.stream(inputSplit[0].split(" ")).collect(Collectors.toSet());
        Allergens = Arrays.stream(inputSplit[1].split(", ")).collect(Collectors.toSet());
    }

    public Set<String> getIngredients() {
        return Collections.unmodifiableSet(Ingredients);
    }

    public Set<String> getAllergens() {
        return Collections.unmodifiableSet(Allergens);
    }
}
