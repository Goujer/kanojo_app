package twitter4j.internal.org.json;

public class JSONException extends Exception {
    private static final long serialVersionUID = 0;
    private Throwable cause;

    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable cause2) {
        super(cause2.getMessage());
        this.cause = cause2;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
