package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;

public interface Storage {
    void delete();

    InputStream getInputStream() throws IOException;
}
