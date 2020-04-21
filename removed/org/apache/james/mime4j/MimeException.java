package org.apache.james.mime4j;

public class MimeException extends Exception {
    private static final long serialVersionUID = 8352821278714188542L;

    public MimeException(String message) {
        super(message);
    }

    public MimeException(Throwable cause) {
        super(cause);
    }

    public MimeException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
