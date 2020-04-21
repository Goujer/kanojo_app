package org.apache.james.mime4j.storage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TempFileStorageProvider extends AbstractStorageProvider {
    private static final String DEFAULT_PREFIX = "m4j";
    private final File directory;
    private final String prefix;
    private final String suffix;

    public TempFileStorageProvider() {
        this(DEFAULT_PREFIX, (String) null, (File) null);
    }

    public TempFileStorageProvider(File directory2) {
        this(DEFAULT_PREFIX, (String) null, directory2);
    }

    public TempFileStorageProvider(String prefix2, String suffix2, File directory2) {
        if (prefix2 == null || prefix2.length() < 3) {
            throw new IllegalArgumentException("invalid prefix");
        } else if (directory2 == null || directory2.isDirectory() || directory2.mkdirs()) {
            this.prefix = prefix2;
            this.suffix = suffix2;
            this.directory = directory2;
        } else {
            throw new IllegalArgumentException("invalid directory");
        }
    }

    public StorageOutputStream createStorageOutputStream() throws IOException {
        File file = File.createTempFile(this.prefix, this.suffix, this.directory);
        file.deleteOnExit();
        return new TempFileStorageOutputStream(file);
    }

    private static final class TempFileStorageOutputStream extends StorageOutputStream {
        private File file;
        private OutputStream out;

        public TempFileStorageOutputStream(File file2) throws IOException {
            this.file = file2;
            this.out = new FileOutputStream(file2);
        }

        public void close() throws IOException {
            super.close();
            this.out.close();
        }

        /* access modifiers changed from: protected */
        public void write0(byte[] buffer, int offset, int length) throws IOException {
            this.out.write(buffer, offset, length);
        }

        /* access modifiers changed from: protected */
        public Storage toStorage0() throws IOException {
            return new TempFileStorage(this.file);
        }
    }

    private static final class TempFileStorage implements Storage {
        private static final Set<File> filesToDelete = new HashSet();
        private File file;

        public TempFileStorage(File file2) {
            this.file = file2;
        }

        public void delete() {
            synchronized (filesToDelete) {
                if (this.file != null) {
                    filesToDelete.add(this.file);
                    this.file = null;
                }
                Iterator<File> iterator = filesToDelete.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().delete()) {
                        iterator.remove();
                    }
                }
            }
        }

        public InputStream getInputStream() throws IOException {
            if (this.file != null) {
                return new BufferedInputStream(new FileInputStream(this.file));
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
}
