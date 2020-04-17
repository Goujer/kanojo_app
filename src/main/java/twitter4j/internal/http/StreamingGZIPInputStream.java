package twitter4j.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

final class StreamingGZIPInputStream extends GZIPInputStream {
    private final InputStream wrapped;

    public StreamingGZIPInputStream(InputStream is) throws IOException {
        super(is);
        this.wrapped = is;
    }

    public int available() throws IOException {
        return this.wrapped.available();
    }
}
