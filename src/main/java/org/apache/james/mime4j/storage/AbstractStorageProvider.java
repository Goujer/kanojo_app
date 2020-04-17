package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.codec.CodecUtil;

public abstract class AbstractStorageProvider implements StorageProvider {
    protected AbstractStorageProvider() {
    }

    public final Storage store(InputStream in) throws IOException {
        StorageOutputStream out = createStorageOutputStream();
        CodecUtil.copy(in, out);
        return out.toStorage();
    }
}
