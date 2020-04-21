package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.storage.DefaultStorageProvider;
import org.apache.james.mime4j.storage.MultiReferenceStorage;
import org.apache.james.mime4j.storage.Storage;
import org.apache.james.mime4j.storage.StorageProvider;
import org.apache.james.mime4j.util.CharsetUtil;

public class BodyFactory {
    private static final Charset FALLBACK_CHARSET = CharsetUtil.DEFAULT_CHARSET;
    private static Log log = LogFactory.getLog(BodyFactory.class);
    private StorageProvider storageProvider;

    public BodyFactory() {
        this.storageProvider = DefaultStorageProvider.getInstance();
    }

    public BodyFactory(StorageProvider storageProvider2) {
        this.storageProvider = storageProvider2 == null ? DefaultStorageProvider.getInstance() : storageProvider2;
    }

    public StorageProvider getStorageProvider() {
        return this.storageProvider;
    }

    public BinaryBody binaryBody(InputStream is) throws IOException {
        if (is != null) {
            return new StorageBinaryBody(new MultiReferenceStorage(this.storageProvider.store(is)));
        }
        throw new IllegalArgumentException();
    }

    public BinaryBody binaryBody(Storage storage) throws IOException {
        if (storage != null) {
            return new StorageBinaryBody(new MultiReferenceStorage(storage));
        }
        throw new IllegalArgumentException();
    }

    public TextBody textBody(InputStream is) throws IOException {
        if (is != null) {
            return new StorageTextBody(new MultiReferenceStorage(this.storageProvider.store(is)), CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }

    public TextBody textBody(InputStream is, String mimeCharset) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException();
        } else if (mimeCharset == null) {
            throw new IllegalArgumentException();
        } else {
            Storage storage = this.storageProvider.store(is);
            return new StorageTextBody(new MultiReferenceStorage(storage), toJavaCharset(mimeCharset, false));
        }
    }

    public TextBody textBody(Storage storage) throws IOException {
        if (storage != null) {
            return new StorageTextBody(new MultiReferenceStorage(storage), CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }

    public TextBody textBody(Storage storage, String mimeCharset) throws IOException {
        if (storage == null) {
            throw new IllegalArgumentException();
        } else if (mimeCharset == null) {
            throw new IllegalArgumentException();
        } else {
            return new StorageTextBody(new MultiReferenceStorage(storage), toJavaCharset(mimeCharset, false));
        }
    }

    public TextBody textBody(String text) {
        if (text != null) {
            return new StringTextBody(text, CharsetUtil.DEFAULT_CHARSET);
        }
        throw new IllegalArgumentException();
    }

    public TextBody textBody(String text, String mimeCharset) {
        if (text == null) {
            throw new IllegalArgumentException();
        } else if (mimeCharset != null) {
            return new StringTextBody(text, toJavaCharset(mimeCharset, true));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Charset toJavaCharset(String mimeCharset, boolean forEncoding) {
        String charset = CharsetUtil.toJavaCharset(mimeCharset);
        if (charset == null) {
            if (log.isWarnEnabled()) {
                log.warn("MIME charset '" + mimeCharset + "' has no " + "corresponding Java charset. Using " + FALLBACK_CHARSET + " instead.");
            }
            return FALLBACK_CHARSET;
        } else if (forEncoding && !CharsetUtil.isEncodingSupported(charset)) {
            if (log.isWarnEnabled()) {
                log.warn("MIME charset '" + mimeCharset + "' does not support encoding. Using " + FALLBACK_CHARSET + " instead.");
            }
            return FALLBACK_CHARSET;
        } else if (forEncoding || CharsetUtil.isDecodingSupported(charset)) {
            return Charset.forName(charset);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("MIME charset '" + mimeCharset + "' does not support decoding. Using " + FALLBACK_CHARSET + " instead.");
            }
            return FALLBACK_CHARSET;
        }
    }
}
