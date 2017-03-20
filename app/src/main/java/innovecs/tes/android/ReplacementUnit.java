package innovecs.tes.android;

/**
 * Created by barsi on 3/20/2017.
 */

public class ReplacementUnit {
    private String value;
    private int replacement;

    public ReplacementUnit(String value, int replacement) {
        this.value = value;
        this.replacement = replacement;
    }

    public String getValue() {
        return value;
    }

    public int getSize() {
        return replacement;
    }
}
