package advent.of.code.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    /**
     * Hides the boilerplate necessary to match a regex to a string and obtain the groups.
     *
     * @param regex           The regular expression to match.
     * @param text            The text to match the regex against.
     * @param expectedMatches The exact number of groups that should be captured.
     * @return The Matcher object with the groups already found.
     * @throws RuntimeException If the regex does not match the text or if the number of groups found is wrong.
     */
    public static Matcher match(String regex, String text, int expectedMatches) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find() || matcher.groupCount() != expectedMatches) {
            throw new RuntimeException(String.format("Regex %s did not match properly.%n", regex));
        }

        return matcher;
    }
}
