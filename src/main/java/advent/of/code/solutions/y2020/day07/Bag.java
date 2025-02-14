package advent.of.code.solutions.y2020.day07;

import java.util.*;

class Bag {
    private final Map<Bag, Integer> MustContain;
    private final Set<Bag> ContainedIn;

    public Bag() {
        MustContain = new HashMap<>();
        ContainedIn = new HashSet<>();
    }

    public void addMustContain(Bag bag, int amount) {
        MustContain.put(bag, amount);
        bag.ContainedIn.add(this);
    }

    public Set<Bag> getContainedIn() {
        return Collections.unmodifiableSet(ContainedIn);
    }

    public int totalBagsContained() {
        int returnValue = 0;

        for (Bag bag : MustContain.keySet()) {
            returnValue += MustContain.get(bag) + bag.totalBagsContained() * MustContain.get(bag);
        }

        return returnValue;
    }
}
