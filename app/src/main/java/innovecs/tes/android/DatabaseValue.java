package innovecs.tes.android;

/**
 * Created by barsi on 3/20/2017.
 */

public class DatabaseValue {
    private String message;
    private String id;

    public DatabaseValue(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }
}