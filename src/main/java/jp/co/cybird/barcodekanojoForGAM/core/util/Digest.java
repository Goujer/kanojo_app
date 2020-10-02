package jp.co.cybird.barcodekanojoForGAM.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class Digest {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    private MessageDigest messageDigest;

    public Digest(String algorithm) {
        if (algorithm == null) {
            throw new NullPointerException("Algorithm must not be null");
        }
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static String hex(String algorithm, String message) {
        return new Digest(algorithm).hex(message);
    }

    public String hex(String message) {
        if (message == null) {
            throw new NullPointerException("Message must not be null");
        } else if (this.messageDigest == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            this.messageDigest.reset();
            this.messageDigest.update(message.getBytes());
            byte[] digest = this.messageDigest.digest();
            for (byte b : digest) {
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    builder.append(GreeDefs.BARCODE);
                }
                builder.append(hex);
            }
            return builder.toString();
        }
    }
}
