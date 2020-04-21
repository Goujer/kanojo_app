package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.james.mime4j.codec.CodecUtil;
import org.apache.james.mime4j.storage.MultiReferenceStorage;

class StorageBinaryBody extends BinaryBody {
    private MultiReferenceStorage storage;

    public StorageBinaryBody(MultiReferenceStorage storage2) {
        this.storage = storage2;
    }

    public InputStream getInputStream() throws IOException {
        return this.storage.getInputStream();
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException();
        }
        InputStream in = this.storage.getInputStream();
        CodecUtil.copy(in, out);
        in.close();
    }

    public StorageBinaryBody copy() {
        this.storage.addReference();
        return new StorageBinaryBody(this.storage);
    }

    public void dispose() {
        if (this.storage != null) {
            this.storage.delete();
            this.storage = null;
        }
    }
}
