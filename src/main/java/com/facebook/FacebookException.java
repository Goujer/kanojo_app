package com.facebook;

public class FacebookException extends RuntimeException {
    static final long serialVersionUID = 1;

    public FacebookException() {
    }

    public FacebookException(String message) {
        super(message);
    }

    public FacebookException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FacebookException(Throwable throwable) {
        super(throwable);
    }
}
