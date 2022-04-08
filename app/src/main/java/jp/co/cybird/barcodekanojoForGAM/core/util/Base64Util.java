package jp.co.cybird.barcodekanojoForGAM.core.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Base64Util {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static String DEFAULT_KEY_ALGORITHM = "AES";
    static final String V1_DATE_FORMAT = "yyyyMMddHHmmss";

    public static native String getAesIvFromNative();

    public static native String getAesKeyFromNative();

    static {
        System.loadLibrary("Base64UtilNative");
    }

    public static String encryptUserId(int greeid) {
        Date date = new Date();
        Encrypted encrypted = encryptString("[[[" + String.valueOf(greeid) + ":" + new SimpleDateFormat(V1_DATE_FORMAT).format(date) + "]]]", true);
        if (encrypted != null) {
            return encrypted.encriptedId;
        }
        return null;
    }

    private static String decode(String id, String iv) {
        String keyData = getAesKeyFromNative();
        byte[] dec64bytes = Base64.decode(id.getBytes(), 0);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(2, new SecretKeySpec(keyData.getBytes(), DEFAULT_KEY_ALGORITHM), new IvParameterSpec(Base64.decode(iv.getBytes(), 0)));
            return new String(cipher.doFinal(dec64bytes), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    private static Encrypted encryptString(String RAWDATA, boolean ENCODE) {
        String encryptedId;
        String encryptedIv;
        byte[] encryptedBytes = null;
        byte[] key = getAesKeyFromNative().getBytes();
        byte[] iv = getAesIvFromNative().getBytes();
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, DEFAULT_KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(1, keySpec, new IvParameterSpec(Base64.decode(iv, 0)));
            iv = cipher.getIV();
            encryptedBytes = cipher.doFinal(RAWDATA.getBytes());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
        }
        if (ENCODE) {
            encryptedId = new String(Base64.encodeToString(encryptedBytes, 0));
            encryptedIv = new String(Base64.encode(iv, 0));
        } else {
            encryptedId = new String(encryptedBytes);
            encryptedIv = new String(iv);
        }
        if (encryptedId == null || encryptedIv == null) {
            return null;
        }
        return new Encrypted(encryptedId, encryptedIv);
    }

    static class Encrypted {
        private String encriptedId;
        private String encriptedIv;

        public Encrypted(String id, String iv) {
            this.encriptedId = id;
            this.encriptedIv = iv;
        }

        public String getEncriptedId() {
            return this.encriptedId;
        }

        public String getEncriptedIv() {
            return this.encriptedIv;
        }
    }
}
