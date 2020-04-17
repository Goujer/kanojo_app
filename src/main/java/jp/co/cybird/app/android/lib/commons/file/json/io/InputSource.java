package jp.co.cybird.app.android.lib.commons.file.json.io;

import java.io.IOException;

public interface InputSource {
    void back();

    String copy(int i);

    void copy(StringBuilder sb, int i);

    long getColumnNumber();

    long getLineNumber();

    long getOffset();

    int mark() throws IOException;

    int next() throws IOException;
}
