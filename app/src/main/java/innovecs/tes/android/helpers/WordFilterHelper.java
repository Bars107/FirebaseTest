package innovecs.tes.android.helpers;

import java.util.regex.Pattern;

/**
 * Created by bars on 3/20/2017.
 */

public class WordFilterHelper {

    public static String addSpacesSenitise(String word) {
        return addCharacterToEach(word, "$0 ");
    }

    public static String addDotsSenitise(String word) {
        return addCharacterToEach(word, "$0.");
    }

    public static String addUnderlineSenitise(String word) {
        return addCharacterToEach(word, "$0_");
    }

    public static String replaceSymbolHSenitise(String word) {
        return replaceSymbol(word, "H", "|-|");
    }

    public static String replaceSymbolESenitise(String word) {
        return replaceSymbol(word, "e", "3");
    }

    private static String addCharacterToEach(String word, String character) {
        return word.replaceAll(".(?!$)", character);
    }

    //Pattern qoute is needed to insure that search string is not interpreted as a regex.
    private static String replaceSymbol(String word, String symbol, String replacement) {
        return word.replaceAll("(?i)" + Pattern.quote(symbol), replacement);
    }

}
