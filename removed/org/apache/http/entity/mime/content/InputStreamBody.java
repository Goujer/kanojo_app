package org.apache.http.entity.mime.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamBody extends AbstractContentBody {
    private final String filename;
    private final InputStream in;

    public InputStreamBody(InputStream in2, String mimeType, String filename2) {
        super(mimeType);
        if (in2 == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        }
        this.in = in2;
        this.filename = filename2;
    }

    public InputStreamBody(InputStream in2, String filename2) {
        this(in2, "application/octet-stream", filename2);
    }

    public InputStream getInputStream() {
        return this.in;
    }

    @Deprecated
    public void writeTo(OutputStream out, int mode) throws IOException {
        writeTo(out);
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        try {
            byte[] tmp = new byte[4096];
            while (true) {
                int l = this.in.read(tmp);
                if (l != -1) {
                    out.write(tmp, 0, l);
                } else {
                    out.flush();
                    return;
                }
            }
        } finally {
            this.in.close();
        }
    }

    public String getTransferEncoding() {
        return "binary";
    }

    public String getCharset() {
        return null;
    }

    public long getContentLength() {
        return -1;
    }

    public String getFilename() {
        return this.filename;
    }
}