package advent.of.code.solutions.y2024.day05;

import advent.of.code.solutions.Day;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day05 implements Day {
    @Override
    public String part1(String inputString) {
        Map<Integer, Page> pages = parsePages(inputString);
        Set<Page[]> printings = parsePrintings(inputString, pages);

        int total = 0;

        for (Page[] printing : printings) {
            if (isPrintingValid(printing)) {
                total += printing[printing.length / 2].getNumber();
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Map<Integer, Page> pages = parsePages(inputString);
        Set<Page[]> printings = parsePrintings(inputString, pages);

        int total = 0;

        for (Page[] printing : printings) {
            if (!isPrintingValid(printing)) {
                Page[] sortedPrinting = sortPrinting(printing);
                total += sortedPrinting[sortedPrinting.length / 2].getNumber();
            }
        }

        return String.format("%d", total);
    }

    private Map<Integer, Page> parsePages(String inputString) {
        String[] pagesStrings = inputString.split("\n\n")[0].split("\n");

        Map<Integer, Page> pages = new HashMap<>();

        for (String pageString : pagesStrings) {
            String[] pageSplit = pageString.split("\\|");
            int pageLeft = Integer.parseInt(pageSplit[0]);
            int pageRight = Integer.parseInt(pageSplit[1]);

            if (!pages.containsKey(pageLeft)) {
                pages.put(pageLeft, new Page(pageLeft));
            }

            if (!pages.containsKey(pageRight)) {
                pages.put(pageRight, new Page(pageRight));
            }
        }

        for (String pageString : pagesStrings) {
            String[] pageSplit = pageString.split("\\|");
            int pageLeft = Integer.parseInt(pageSplit[0]);
            int pageRight = Integer.parseInt(pageSplit[1]);

            pages.get(pageLeft).addAfter(pages.get(pageRight));
        }

        return Collections.unmodifiableMap(pages);
    }

    private Set<Page[]> parsePrintings(String inputString, Map<Integer, Page> pages) {
        String[] printingsStrings = inputString.split("\n\n")[1].split("\n");

        return Arrays.stream(printingsStrings)
                .map(l -> Arrays.stream(l.split(","))
                        .map(Integer::parseInt)
                        .map(pages::get)
                        .toArray(Page[]::new))
                .collect(Collectors.toSet());
    }

    private boolean isPrintingValid(Page[] printing) {
        for (int i = 0; i < printing.length - 1; i++) {
            Page thisPage = printing[i];
            for (int k = i + 1; k < printing.length; k++) {
                Page otherPage = printing[k];

                if (otherPage.getAfter().contains(thisPage)) {
                    return false;
                }
            }
        }

        return true;
    }

    private Page[] sortPrinting(Page[] printing) {
        List<Page> printingList = new ArrayList<>(Arrays.stream(printing).toList());
        List<Page> returnValue = new ArrayList<>();

        while (!printingList.isEmpty()) {
            Page firstPage = null;
            for (int i = 0; i < printingList.size(); i++) {
                boolean isFirst = true;
                Page thisPage = printingList.get(i);

                for (Page otherPage : printingList) {
                    if (otherPage.getAfter().contains(thisPage)) {
                        isFirst = false;
                        break;
                    }
                }

                if (isFirst) {
                    firstPage = thisPage;
                    break;
                }
            }

            if (firstPage == null) {
                throw new RuntimeException("Bad sorting method.");
            }

            returnValue.add(firstPage);
            printingList.remove(firstPage);
        }

        return returnValue.toArray(Page[]::new);
    }
}
