package advent.of.code.utils;

import java.util.*;
import java.util.function.Supplier;

public class SetUtils {
    /**
     * Defaults to HashSet.
     */
    public static <T> Set<T> union(Set<T> first, Set<T> second) {
        return union(first, second, HashSet::new);
    }

    public static <T> Set<T> union(Set<T> first, Set<T> second, Supplier<Set<T>> supplier) {
        Set<T> returnValue = supplier.get();

        returnValue.addAll(first);
        returnValue.addAll(second);

        return returnValue;
    }

    /**
     * Defaults to HashSet.
     */
    public static <T> Set<T> difference(Set<T> minuend, Set<T> subtrahend) {
        return difference(minuend, subtrahend, HashSet::new);
    }

    public static <T> Set<T> difference(Set<T> minuend, Set<T> subtrahend, Supplier<Set<T>> supplier) {
        Set<T> returnValue = supplier.get();

        returnValue.addAll(minuend);
        returnValue.removeAll(subtrahend);

        return returnValue;
    }

    /**
     * Defaults to HashSet.
     */
    public static <T> Set<T> intersection(Set<T> first, Set<T> second) {
        return intersection(first, second, HashSet::new);
    }

    public static <T> Set<T> intersection(Set<T> first, Set<T> second, Supplier<Set<T>> supplier) {
        Set<T> returnValue = supplier.get();

        returnValue.addAll(first);
        returnValue.retainAll(second);

        return returnValue;
    }

    /**
     * Defaults to HashSet.
     */
    public static <T> Set<T> add(Set<T> set, T element) {
        return add(set, element, HashSet::new);
    }

    public static <T> Set<T> add(Set<T> set, T element, Supplier<Set<T>> supplier) {
        Set<T> returnValue = supplier.get();

        returnValue.addAll(set);
        returnValue.add(element);

        return returnValue;
    }

    /**
     * Defaults to HashSet.
     */
    public static <T> Set<T> remove(Set<T> set, T element) {
        return remove(set, element, HashSet::new);
    }

    public static <T> Set<T> remove(Set<T> set, T element, Supplier<Set<T>> supplier) {
        Set<T> returnValue = supplier.get();

        returnValue.addAll(set);
        returnValue.remove(element);

        return returnValue;
    }

    public static <T> Set<Set<T>> powerSet(Collection<T> collection) {
        Set<Set<T>> returnValue = new HashSet<>();

        if (collection.isEmpty()) {
            returnValue.add(new HashSet<>());
            return returnValue;
        }

        List<T> list = new ArrayList<>(collection.stream().toList());
        T element = list.getFirst();
        list.removeFirst();

        Set<Set<T>> otherSets = powerSet(list);

        for (Set<T> otherSet : otherSets) {
            returnValue.add(new HashSet<>(otherSet));

            Set<T> otherSetCopy = new HashSet<>(otherSet);
            otherSetCopy.add(element);
            returnValue.add(otherSetCopy);
        }

        return returnValue;
    }
}
