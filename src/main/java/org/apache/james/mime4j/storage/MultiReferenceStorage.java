package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;

public class MultiReferenceStorage implements Storage {
    private int referenceCounter;
    private final Storage storage;

    public MultiReferenceStorage(Storage storage2) {
        if (storage2 == null) {
            throw new IllegalArgumentException();
        }
        this.storage = storage2;
        this.referenceCounter = 1;
    }

    public void addReference() {
        incrementCounter();
    }

    public void delete() {
        if (decrementCounter()) {
            this.storage.delete();
        }
    }

    public InputStream getInputStream() throws IOException {
        return this.storage.getInputStream();
    }

    private synchronized void incrementCounter() {
        if (this.referenceCounter == 0) {
            throw new IllegalStateException("storage has been deleted");
        }
        this.referenceCounter++;
    }

    private synchronized boolean decrementCounter() {
        int i;
        if (this.referenceCounter == 0) {
            throw new IllegalStateException("storage has been deleted");
        }
        i = this.referenceCounter - 1;
        this.referenceCounter = i;
        return i == 0;
    }
}
