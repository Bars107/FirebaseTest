package innovecs.tes.android;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import innovecs.tes.android.DataStructures.DatabaseValue;
import innovecs.tes.android.DataStructures.ReplacementUnit;
import innovecs.tes.android.helpers.WordFilterHelper;

/**
 * Created by barsi on 3/20/2017.
 */

public class SanitiseManager {
    private static final String SENITISE_METHOD_TAG = "Senitise";

    private List<ReplacementUnit> replacementUnits = new ArrayList<>();

    /**
     * Values in this list will look like this
     * index - value
     * 0 - ""
     * 1 - "*"
     * 2 - "**"
     * 3 - "***"
     * ...
     *
     */
    private List<String> replacementSnowballs = new ArrayList<>();

    private List<Method> replacementMethods = new ArrayList<>();

    public SanitiseManager() {
        collectMethods();
        replacementSnowballs.add("");
    }

    public void createSenitiseList(ArrayList<DatabaseValue> wordsToSenitise) {
        replacementUnits.clear();
        for (DatabaseValue databaseValue : wordsToSenitise) {
            int lenght = databaseValue.getMessage().length();
            String word = databaseValue.getMessage();
            ReplacementUnit replacementUnit = new ReplacementUnit(word, lenght);
            replacementUnits.add(replacementUnit);
            addFilters(word, lenght);
        }
        Log.d("SENITISE_MANAGER", " " + String.valueOf(replacementUnits.size()));
    }

    public ArrayList<String> replaceWords(ArrayList<String> messages) {
        ArrayList<String> newMessages = new ArrayList<>();
        for (String message : messages) {
            StringBuilder builder = new StringBuilder(message);
            for (ReplacementUnit replacementUnit : replacementUnits) {
                String afterReplacement = doOneReplacement(builder.toString(), replacementUnit.getValue(), replacementUnit.getSize());
                builder = new StringBuilder(afterReplacement);
            }
            newMessages.add(builder.toString());
        }
        return newMessages;
    }

    private String doOneReplacement(String message, String toReplace, int length) {
        return message.replaceAll("(?i)" + Pattern.quote(toReplace), getReplacementSnowball(length));
    }

    private String getReplacementSnowball(int size) {
        if (replacementSnowballs.size() < size) {
            updateReplacementSnowballs(size);
        }
        return replacementSnowballs.get(size);
    }

    /**
     * If initial list size is less than number, it means that it doesn't have string of needed length
     * string of needed length will be added at the end of the list
     * @param size of the string which should be replaced by * symbols
     */
    private void updateReplacementSnowballs(int size) {
        int initialSize = size - replacementSnowballs.size() + 1;
        for (int i = 0; i < initialSize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < replacementSnowballs.size(); j++) {
                sb.append("*");
            }
            replacementSnowballs.add(sb.toString());
        }
    }

    /**
     *  uses reflection to iterate over methods of WordFilterHelper to get all needed modification rules
     */
    private void collectMethods(){
        Class wordFilterHelper = WordFilterHelper.class;
        for (Method method : wordFilterHelper.getMethods()){
            if (Modifier.isPublic(method.getModifiers()) && method.getName().contains(SENITISE_METHOD_TAG)) {
                replacementMethods.add(method);
            }
        }
    }

    private void addFilters(String word, int lenght) {
        for (Method method : replacementMethods){
            try {
                Object result = method.invoke(this, word);
                String filter = (String) result;
                if (!word.equals(filter)){
                    ReplacementUnit replacementUnit = new ReplacementUnit(filter, lenght);
                    replacementUnits.add(replacementUnit);
                }
            } catch (Exception e) {
                Log.e("SENITISE_MANAGER", e.getMessage());
            }
        }
    }

}
