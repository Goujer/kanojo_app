package org.apache.james.mime4j.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public class MemoryStorageProvider extends AbstractStorageProvider {
    public StorageOutputStream createStorageOutputStream() {
        return new MemoryStorageOutputStream();
    }

    private static final class MemoryStorageOutputStream extends StorageOutputStream {
        ByteArrayBuffer bab;

        private MemoryStorageOutputStream() {
            this.bab = new ByteArrayBuffer(1024);
        }

        /* access modifiers changed from: protected */
        public void write0(byte[] buffer, int offset, int length) throws IOException {
            this.bab.append(buffer, offset, length);
        }

        /* access modifiers changed from: protected */
        public Storage toStorage0() throws IOException {
            return new MemoryStorage(this.bab.buffer(), this.bab.length());
        }
    }

    static final class MemoryStorage implements Storage {
        private final int count;
        private byte[] data;

        public MemoryStorage(byte[] data2, int count2) {
            this.data = data2;
            this.count = count2;
        }

        public InputStream getInputStream() throws IOException {
            if (this.data != null) {
                return new ByteArrayInputStream(this.data, 0, this.count);
            }
            throw new IllegalStateException("storage has been deleted");
        }

        public void delete() {
            this.data = null;
        }
    }
}
