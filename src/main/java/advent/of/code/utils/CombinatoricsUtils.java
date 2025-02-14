package advent.of.code.utils;

import java.util.ArrayList;
import java.util.List;

public class CombinatoricsUtils {
    // https://stackoverflow.com/a/10305419
    public static List<List<Integer>> generatePermutations(List<Integer> original) {
        if (original.isEmpty()) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        original = new ArrayList<>(original);

        Integer firstElement = original.removeFirst();
        List<List<Integer>> returnValue = new ArrayList<>();
        List<List<Integer>> permutations = generatePermutations(original);
        for (List<Integer> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<Integer> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }

        return returnValue;
    }
}
