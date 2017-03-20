package innovecs.tes.android.helpers;

import java.util.regex.Pattern;

/**
 * Created by bars on 3/20/2017.
 */

public class WordFilterHelper {

    // Hello - h e l l o
    public static String addSpacesSenitise(String word) {
        return addCharacterToEach(word, "$0 ");
    }

    // Hello - h.e.l.l.o
    public static String addDotsSenitise(String word) {
        return addCharacterToEach(word, "$0.");
    }

    // Hello - h_e_l_l_o
    public static String addUnderlineSenitise(String word) {
        return addCharacterToEach(word, "$0_");
    }

    // Hello - |-|ello
    public static String replaceSymbolHSenitise(String word) {
        return replaceSymbol(word, "H", "|-|");
    }

    // Hello - h3llo
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
