package jp.co.cybird.app.android.lib.commons.file.json.io;

import java.io.IOException;

public interface OutputSource {
    void append(char c) throws IOException;

    void append(String str) throws IOException;

    void append(String str, int i, int i2) throws IOException;

    void flush() throws IOException;
}
