package org.apache.james.mime4j.io;

import org.apache.james.mime4j.MimeIOException;

public class MaxLineLimitException extends MimeIOException {
    private static final long serialVersionUID = 8039001187837730773L;

    public MaxLineLimitException(String message) {
        super(message);
    }
}
