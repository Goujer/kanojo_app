package com.facebook.internal;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class FileLruCache {
    private static final String HEADER_CACHEKEY_KEY = "key";
    private static final String HEADER_CACHE_CONTENT_TAG_KEY = "tag";
    static final String TAG = FileLruCache.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final AtomicLong bufferIndex = new AtomicLong();
    private final File directory;
    private boolean isTrimPending;
    private final Limits limits;
    private final Object lock = new Object();
    private final String tag;

    private interface StreamCloseCallback {
        void onClose();
    }

    public FileLruCache(Context context, String tag2, Limits limits2) {
        this.tag = tag2;
        this.limits = limits2;
        this.directory = new File(context.getCacheDir(), tag2);
        this.directory.mkdirs();
        BufferFile.deleteAll(this.directory);
    }

    public void clearForTest() throws IOException {
        for (File file : this.directory.listFiles()) {
            file.delete();
        }
    }

    /* access modifiers changed from: package-private */
    public long sizeInBytesForTest() {
        synchronized (this.lock) {
            while (this.isTrimPending) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        long total = 0;
        for (File file : this.directory.listFiles()) {
            total += file.length();
        }
        return total;
    }

    public InputStream get(String key) throws IOException {
        return get(key, (String) null);
    }

    public InputStream get(String key, String contentTag) throws IOException {
        File file = new File(this.directory, Utility.md5hash(key));
        try {
            FileInputStream input = new FileInputStream(file);
            BufferedInputStream buffered = new BufferedInputStream(input, 8192);
            try {
                JSONObject header = StreamHeader.readHeader(buffered);
                if (header == null) {
                    FileInputStream fileInputStream = input;
                    return null;
                }
                String foundKey = header.optString(HEADER_CACHEKEY_KEY);
                if (foundKey == null || !foundKey.equals(key)) {
                    if (0 == 0) {
                        buffered.close();
                    }
                    FileInputStream fileInputStream2 = input;
                    return null;
                }
                String headerContentTag = header.optString(HEADER_CACHE_CONTENT_TAG_KEY, (String) null);
                if ((contentTag != null || headerContentTag == null) && (contentTag == null || contentTag.equals(headerContentTag))) {
                    long accessTime = new Date().getTime();
                    Logger.log(LoggingBehavior.CACHE, TAG, "Setting lastModified to " + Long.valueOf(accessTime) + " for " + file.getName());
                    file.setLastModified(accessTime);
                    if (1 == 0) {
                        buffered.close();
                    }
                    FileInputStream fileInputStream3 = input;
                    return buffered;
                }
                if (0 == 0) {
                    buffered.close();
                }
                FileInputStream fileInputStream4 = input;
                return null;
            } finally {
                if (0 == 0) {
                    buffered.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public OutputStream openPutStream(String key) throws IOException {
        return openPutStream(key, (String) null);
    }

    public OutputStream openPutStream(final String key, String contentTag) throws IOException {
        final File buffer = BufferFile.newFile(this.directory);
        buffer.delete();
        if (!buffer.createNewFile()) {
            throw new IOException("Could not create file at " + buffer.getAbsolutePath());
        }
        try {
            BufferedOutputStream buffered = new BufferedOutputStream(new CloseCallbackOutputStream(new FileOutputStream(buffer), new StreamCloseCallback() {
                public void onClose() {
                    FileLruCache.this.renameToTargetAndTrim(key, buffer);
                }
            }), 8192);
            try {
                JSONObject header = new JSONObject();
                header.put(HEADER_CACHEKEY_KEY, key);
                if (!Utility.isNullOrEmpty(contentTag)) {
                    header.put(HEADER_CACHE_CONTENT_TAG_KEY, contentTag);
                }
                StreamHeader.writeHeader(buffered, header);
                if (1 == 0) {
                    buffered.close();
                }
                return buffered;
            } catch (JSONException e) {
                Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error creating JSON header for cache file: " + e);
                throw new IOException(e.getMessage());
            } catch (Throwable th) {
                if (0 == 0) {
                    buffered.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e2) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error creating buffer output stream: " + e2);
            throw new IOException(e2.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void renameToTargetAndTrim(String key, File buffer) {
        if (!buffer.renameTo(new File(this.directory, Utility.md5hash(key)))) {
            buffer.delete();
        }
        postTrim();
    }

    public InputStream interceptAndPut(String key, InputStream input) throws IOException {
        return new CopyingInputStream(input, openPutStream(key));
    }

    public String toString() {
        return "{FileLruCache: tag:" + this.tag + " file:" + this.directory.getName() + "}";
    }

    private void postTrim() {
        synchronized (this.lock) {
            if (!this.isTrimPending) {
                this.isTrimPending = true;
                Settings.getExecutor().execute(new Runnable() {
                    public void run() {
                        FileLruCache.this.trim();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void trim() {
        try {
            Logger.log(LoggingBehavior.CACHE, TAG, "trim started");
            PriorityQueue<ModifiedFile> heap = new PriorityQueue<>();
            long size = 0;
            long count = 0;
            for (File file : this.directory.listFiles(BufferFile.excludeBufferFiles())) {
                ModifiedFile modified = new ModifiedFile(file);
                heap.add(modified);
                Logger.log(LoggingBehavior.CACHE, TAG, "  trim considering time=" + Long.valueOf(modified.getModified()) + " name=" + modified.getFile().getName());
                size += file.length();
                count++;
            }
            while (true) {
                if (size > ((long) this.limits.getByteCount()) || count > ((long) this.limits.getFileCount())) {
                    File file2 = ((ModifiedFile) heap.remove()).getFile();
                    Logger.log(LoggingBehavior.CACHE, TAG, "  trim removing " + file2.getName());
                    size -= file2.length();
                    count--;
                    file2.delete();
                } else {
                    synchronized (this.lock) {
                        this.isTrimPending = false;
                        this.lock.notifyAll();
                    }
                    return;
                }
            }
        } catch (Throwable th) {
            synchronized (this.lock) {
                this.isTrimPending = false;
                this.lock.notifyAll();
                throw th;
            }
        }
    }

    private static class BufferFile {
        private static final String FILE_NAME_PREFIX = "buffer";
        private static final FilenameFilter filterExcludeBufferFiles = new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return !filename.startsWith(BufferFile.FILE_NAME_PREFIX);
            }
        };
        private static final FilenameFilter filterExcludeNonBufferFiles = new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.startsWith(BufferFile.FILE_NAME_PREFIX);
            }
        };

        private BufferFile() {
        }

        static void deleteAll(File root) {
            for (File file : root.listFiles(excludeNonBufferFiles())) {
                file.delete();
            }
        }

        static FilenameFilter excludeBufferFiles() {
            return filterExcludeBufferFiles;
        }

        static FilenameFilter excludeNonBufferFiles() {
            return filterExcludeNonBufferFiles;
        }

        static File newFile(File root) {
            return new File(root, FILE_NAME_PREFIX + Long.valueOf(FileLruCache.bufferIndex.incrementAndGet()).toString());
        }
    }

    private static final class StreamHeader {
        private static final int HEADER_VERSION = 0;

        private StreamHeader() {
        }

        static void writeHeader(OutputStream stream, JSONObject header) throws IOException {
            byte[] headerBytes = header.toString().getBytes();
            stream.write(0);
            stream.write((headerBytes.length >> 16) & MotionEventCompat.ACTION_MASK);
            stream.write((headerBytes.length >> 8) & MotionEventCompat.ACTION_MASK);
            stream.write((headerBytes.length >> 0) & MotionEventCompat.ACTION_MASK);
            stream.write(headerBytes);
        }

        static JSONObject readHeader(InputStream stream) throws IOException {
            if (stream.read() != 0) {
                return null;
            }
            int headerSize = 0;
            for (int i = 0; i < 3; i++) {
                int b = stream.read();
                if (b == -1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read returned -1 while reading header size");
                    return null;
                }
                headerSize = (headerSize << 8) + (b & MotionEventCompat.ACTION_MASK);
            }
            byte[] headerBytes = new byte[headerSize];
            int count = 0;
            while (count < headerBytes.length) {
                int readCount = stream.read(headerBytes, count, headerBytes.length - count);
                if (readCount < 1) {
                    Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: stream.read stopped at " + Integer.valueOf(count) + " when expected " + headerBytes.length);
                    return null;
                }
                count += readCount;
            }
            try {
                Object parsed = new JSONTokener(new String(headerBytes)).nextValue();
                if (parsed instanceof JSONObject) {
                    return (JSONObject) parsed;
                }
                Logger.log(LoggingBehavior.CACHE, FileLruCache.TAG, "readHeader: expected JSONObject, got " + parsed.getClass().getCanonicalName());
                return null;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private static class CloseCallbackOutputStream extends OutputStream {
        final StreamCloseCallback callback;
        final OutputStream innerStream;

        CloseCallbackOutputStream(OutputStream innerStream2, StreamCloseCallback callback2) {
            this.innerStream = innerStream2;
            this.callback = callback2;
        }

        public void close() throws IOException {
            try {
                this.innerStream.close();
            } finally {
                this.callback.onClose();
            }
        }

        public void flush() throws IOException {
            this.innerStream.flush();
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.innerStream.write(buffer, offset, count);
        }

        public void write(byte[] buffer) throws IOException {
            this.innerStream.write(buffer);
        }

        public void write(int oneByte) throws IOException {
            this.innerStream.write(oneByte);
        }
    }

    private static final class CopyingInputStream extends InputStream {
        final InputStream input;
        final OutputStream output;

        CopyingInputStream(InputStream input2, OutputStream output2) {
            this.input = input2;
            this.output = output2;
        }

        public int available() throws IOException {
            return this.input.available();
        }

        public void close() throws IOException {
            try {
                this.input.close();
            } finally {
                this.output.close();
            }
        }

        public void mark(int readlimit) {
            throw new UnsupportedOperationException();
        }

        public boolean markSupported() {
            return false;
        }

        public int read(byte[] buffer) throws IOException {
            int count = this.input.read(buffer);
            if (count > 0) {
                this.output.write(buffer, 0, count);
            }
            return count;
        }

        public int read() throws IOException {
            int b = this.input.read();
            if (b >= 0) {
                this.output.write(b);
            }
            return b;
        }

        public int read(byte[] buffer, int offset, int length) throws IOException {
            int count = this.input.read(buffer, offset, length);
            if (count > 0) {
                this.output.write(buffer, offset, count);
            }
            return count;
        }

        public synchronized void reset() {
            throw new UnsupportedOperationException();
        }

        public long skip(long byteCount) throws IOException {
            int count;
            byte[] buffer = new byte[1024];
            long total = 0;
            while (total < byteCount && (count = read(buffer, 0, (int) Math.min(byteCount - total, (long) buffer.length))) >= 0) {
                total += (long) count;
            }
            return total;
        }
    }

    public static final class Limits {
        private int byteCount = 1048576;
        private int fileCount = 1024;

        /* access modifiers changed from: package-private */
        public int getByteCount() {
            return this.byteCount;
        }

        /* access modifiers changed from: package-private */
        public int getFileCount() {
            return this.fileCount;
        }

        /* access modifiers changed from: package-private */
        public void setByteCount(int n) {
            if (n < 0) {
                throw new InvalidParameterException("Cache byte-count limit must be >= 0");
            }
            this.byteCount = n;
        }

        /* access modifiers changed from: package-private */
        public void setFileCount(int n) {
            if (n < 0) {
                throw new InvalidParameterException("Cache file count limit must be >= 0");
            }
            this.fileCount = n;
        }
    }

    private static final class ModifiedFile implements Comparable<ModifiedFile> {
        private final File file;
        private final long modified;

        ModifiedFile(File file2) {
            this.file = file2;
            this.modified = file2.lastModified();
        }

        /* access modifiers changed from: package-private */
        public File getFile() {
            return this.file;
        }

        /* access modifiers changed from: package-private */
        public long getModified() {
            return this.modified;
        }

        public int compareTo(ModifiedFile another) {
            if (getModified() < another.getModified()) {
                return -1;
            }
            if (getModified() > another.getModified()) {
                return 1;
            }
            return getFile().compareTo(another.getFile());
        }

        public boolean equals(Object another) {
            return (another instanceof ModifiedFile) && compareTo((ModifiedFile) another) == 0;
        }
    }
}
