package advent.of.code.solutions.y2019.day14;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Reaction {
    private final int Quantity;
    private final String Chemical;
    private final Set<Ingredient> Ingredients = new HashSet<>();

    public Reaction(int quantity, String chemical) {
        Quantity = quantity;
        Chemical = chemical;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        if (Ingredients.isEmpty()) {
            Ingredients.addAll(ingredients);
        } else {
            throw new RuntimeException("Bad ingredients setting.");
        }
    }

    public long getOreQuantity(long repeatProcess, Map<Reaction, Long> bank) {
        if (this.Chemical.equals("ORE")) {
            return repeatProcess;
        }

        long returnValue = 0;

        for (Ingredient ingredient : Ingredients) {
            long totalIngredientQuantity = ingredient.Quantity() * repeatProcess;

            long inBank = bank.get(ingredient.Reaction());
            if (inBank >= totalIngredientQuantity) {
                bank.put(ingredient.Reaction(), inBank - totalIngredientQuantity);
                totalIngredientQuantity = 0;
            } else {
                totalIngredientQuantity -= inBank;
                bank.put(ingredient.Reaction(), 0L);
            }

            long numberOfReactions = (long) Math.ceil((double) totalIngredientQuantity / ingredient.Reaction().Quantity);

            long oreNeeded = ingredient.Reaction().getOreQuantity(numberOfReactions, bank);

            long produced = numberOfReactions * ingredient.Reaction().Quantity;
            long alreadyInBank = bank.get(ingredient.Reaction());
            bank.put(ingredient.Reaction(), alreadyInBank + produced - totalIngredientQuantity);

            returnValue += oreNeeded;
        }

        return returnValue;
    }
}
