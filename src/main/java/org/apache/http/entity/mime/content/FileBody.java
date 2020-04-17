package org.apache.http.entity.mime.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileBody extends AbstractContentBody {
    private final String charset;
    private final File file;
    private final String filename;

    public FileBody(File file2, String filename2, String mimeType, String charset2) {
        super(mimeType);
        if (file2 == null) {
            throw new IllegalArgumentException("File may not be null");
        }
        this.file = file2;
        if (filename2 != null) {
            this.filename = filename2;
        } else {
            this.filename = file2.getName();
        }
        this.charset = charset2;
    }

    public FileBody(File file2, String mimeType, String charset2) {
        this(file2, (String) null, mimeType, charset2);
    }

    public FileBody(File file2, String mimeType) {
        this(file2, mimeType, (String) null);
    }

    public FileBody(File file2) {
        this(file2, "application/octet-stream");
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Deprecated
    public void writeTo(OutputStream out, int mode) throws IOException {
        writeTo(out);
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new FileInputStream(this.file);
        try {
            byte[] tmp = new byte[4096];
            while (true) {
                int l = in.read(tmp);
                if (l != -1) {
                    out.write(tmp, 0, l);
                } else {
                    out.flush();
                    return;
                }
            }
        } finally {
            in.close();
        }
    }

    public String getTransferEncoding() {
        return "binary";
    }

    public String getCharset() {
        return this.charset;
    }

    public long getContentLength() {
        return this.file.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public File getFile() {
        return this.file;
    }
}
