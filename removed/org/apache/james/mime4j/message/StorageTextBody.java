package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import org.apache.james.mime4j.codec.CodecUtil;
import org.apache.james.mime4j.storage.MultiReferenceStorage;
import org.apache.james.mime4j.util.CharsetUtil;

class StorageTextBody extends TextBody {
    private Charset charset;
    private MultiReferenceStorage storage;

    public StorageTextBody(MultiReferenceStorage storage2, Charset charset2) {
        this.storage = storage2;
        this.charset = charset2;
    }

    public String getMimeCharset() {
        return CharsetUtil.toMimeCharset(this.charset.name());
    }

    public Reader getReader() throws IOException {
        return new InputStreamReader(this.storage.getInputStream(), this.charset);
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException();
        }
        InputStream in = this.storage.getInputStream();
        CodecUtil.copy(in, out);
        in.close();
    }

    public StorageTextBody copy() {
        this.storage.addReference();
        return new StorageTextBody(this.storage, this.charset);
    }

    public void dispose() {
        if (this.storage != null) {
            this.storage.delete();
            this.storage = null;
        }
    }
}
