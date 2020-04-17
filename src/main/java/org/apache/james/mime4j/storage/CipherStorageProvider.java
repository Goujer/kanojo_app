package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class CipherStorageProvider extends AbstractStorageProvider {
    private final String algorithm;
    private final StorageProvider backend;
    private final KeyGenerator keygen;

    public CipherStorageProvider(StorageProvider backend2) {
        this(backend2, "Blowfish");
    }

    public CipherStorageProvider(StorageProvider backend2, String algorithm2) {
        if (backend2 == null) {
            throw new IllegalArgumentException();
        }
        try {
            this.backend = backend2;
            this.algorithm = algorithm2;
            this.keygen = KeyGenerator.getInstance(algorithm2);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public StorageOutputStream createStorageOutputStream() throws IOException {
        return new CipherStorageOutputStream(this.backend.createStorageOutputStream(), this.algorithm, getSecretKeySpec());
    }

    private SecretKeySpec getSecretKeySpec() {
        return new SecretKeySpec(this.keygen.generateKey().getEncoded(), this.algorithm);
    }

    private static final class CipherStorageOutputStream extends StorageOutputStream {
        private final String algorithm;
        private final CipherOutputStream cipherOut;
        private final SecretKeySpec skeySpec;
        private final StorageOutputStream storageOut;

        public CipherStorageOutputStream(StorageOutputStream out, String algorithm2, SecretKeySpec skeySpec2) throws IOException {
            try {
                this.storageOut = out;
                this.algorithm = algorithm2;
                this.skeySpec = skeySpec2;
                Cipher cipher = Cipher.getInstance(algorithm2);
                cipher.init(1, skeySpec2);
                this.cipherOut = new CipherOutputStream(out, cipher);
            } catch (GeneralSecurityException e) {
                throw ((IOException) new IOException().initCause(e));
            }
        }

        public void close() throws IOException {
            super.close();
            this.cipherOut.close();
        }

        /* access modifiers changed from: protected */
        public void write0(byte[] buffer, int offset, int length) throws IOException {
            this.cipherOut.write(buffer, offset, length);
        }

        /* access modifiers changed from: protected */
        public Storage toStorage0() throws IOException {
            return new CipherStorage(this.storageOut.toStorage(), this.algorithm, this.skeySpec);
        }
    }

    private static final class CipherStorage implements Storage {
        private final String algorithm;
        private Storage encrypted;
        private final SecretKeySpec skeySpec;

        public CipherStorage(Storage encrypted2, String algorithm2, SecretKeySpec skeySpec2) {
            this.encrypted = encrypted2;
            this.algorithm = algorithm2;
            this.skeySpec = skeySpec2;
        }

        public void delete() {
            if (this.encrypted != null) {
                this.encrypted.delete();
                this.encrypted = null;
            }
        }

        public InputStream getInputStream() throws IOException {
            if (this.encrypted == null) {
                throw new IllegalStateException("storage has been deleted");
            }
            try {
                Cipher cipher = Cipher.getInstance(this.algorithm);
                cipher.init(2, this.skeySpec);
                return new CipherInputStream(this.encrypted.getInputStream(), cipher);
            } catch (GeneralSecurityException e) {
                throw ((IOException) new IOException().initCause(e));
            }
        }
    }
}
