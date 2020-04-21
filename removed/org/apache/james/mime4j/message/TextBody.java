package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.Reader;

public abstract class TextBody extends SingleBody {
    public abstract String getMimeCharset();

    public abstract Reader getReader() throws IOException;

    protected TextBody() {
    }
}
