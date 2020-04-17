package jp.co.cybird.app.android.lib.commons.security;

public class ValidationException extends Exception {
    private static final long serialVersionUID = 1;

    public ValidationException() {
    }

    public ValidationException(String s) {
        super(s);
    }
}
