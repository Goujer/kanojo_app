package jp.co.cybird.app.android.lib.commons.security.obfuscator;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import jp.co.cybird.app.android.lib.commons.security.Obfuscator;
import jp.co.cybird.app.android.lib.commons.security.ValidationException;
import jp.co.cybird.app.android.lib.commons.security.util.Base64;
import jp.co.cybird.app.android.lib.commons.security.util.Base64DecoderException;

public class AESObfuscator implements Obfuscator {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] IV;
    private static final String KEYGEN_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
    private static final String UTF8 = "UTF-8";
    private static final String header = "jp.co.cybird.app.android.lib.commons.security.obfuscator.AESObfuscator-1|";
    private Cipher mDecryptor;
    private Cipher mEncryptor;

    static {
        byte[] bArr = new byte[16];
        bArr[0] = 16;
        bArr[1] = 74;
        bArr[2] = 71;
        bArr[3] = -80;
        bArr[4] = 32;
        bArr[5] = 101;
        bArr[6] = -47;
        bArr[7] = 72;
        bArr[8] = 117;
        bArr[9] = -14;
        bArr[11] = -29;
        bArr[12] = 70;
        bArr[13] = 65;
        bArr[14] = -12;
        bArr[15] = 74;
        IV = bArr;
    }

    public AESObfuscator(byte[] salt, String applicationId, String deviceId) {
        try {
            SecretKey secret = new SecretKeySpec(SecretKeyFactory.getInstance(KEYGEN_ALGORITHM).generateSecret(new PBEKeySpec((String.valueOf(applicationId) + deviceId).toCharArray(), salt, 1024, 256)).getEncoded(), "AES");
            this.mEncryptor = Cipher.getInstance(CIPHER_ALGORITHM);
            this.mEncryptor.init(1, secret, new IvParameterSpec(IV));
            this.mDecryptor = Cipher.getInstance(CIPHER_ALGORITHM);
            this.mDecryptor.init(2, secret, new IvParameterSpec(IV));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Invalid environment", e);
        }
    }

    public String obfuscate(String original, String key) {
        if (original == null) {
            return null;
        }
        try {
            return Base64.encode(this.mEncryptor.doFinal((header + key + original).getBytes(UTF8)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid environment", e);
        } catch (GeneralSecurityException e2) {
            throw new RuntimeException("Invalid environment", e2);
        }
    }

    public String unobfuscate(String obfuscated, String key) throws ValidationException {
        if (obfuscated == null) {
            return null;
        }
        try {
            String result = new String(this.mDecryptor.doFinal(Base64.decode(obfuscated)), UTF8);
            if (result.indexOf(header + key) == 0) {
                return result.substring(header.length() + key.length(), result.length());
            }
            throw new ValidationException("HeaderNotFoundEx (invalid data or key):" + obfuscated);
        } catch (Base64DecoderException e) {
            throw new ValidationException(String.valueOf(e.getMessage()) + ":" + obfuscated);
        } catch (IllegalBlockSizeException e2) {
            throw new ValidationException(String.valueOf(e2.getMessage()) + ":" + obfuscated);
        } catch (BadPaddingException e3) {
            throw new ValidationException(String.valueOf(e3.getMessage()) + ":" + obfuscated);
        } catch (UnsupportedEncodingException e4) {
            throw new RuntimeException("Invalid environment", e4);
        }
    }
}
