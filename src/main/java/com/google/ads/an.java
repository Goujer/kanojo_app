package com.google.ads;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class an {

    public static class a extends Exception {
        public a() {
        }

        public a(Throwable th) {
            super(th);
        }
    }

    static void a(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 68);
        }
    }

    public static byte[] a(String str) throws ap, a {
        byte[] a2 = aq.a(str);
        if (a2.length != 32) {
            throw new a();
        }
        byte[] bArr = new byte[16];
        ByteBuffer.wrap(a2, 4, 16).get(bArr);
        a(bArr);
        return bArr;
    }

    public static byte[] a(byte[] bArr, String str) throws a, ap {
        if (bArr.length != 16) {
            throw new a();
        }
        try {
            byte[] a2 = aq.a(str);
            if (a2.length <= 16) {
                throw new a();
            }
            ByteBuffer allocate = ByteBuffer.allocate(a2.length);
            allocate.put(a2);
            allocate.flip();
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[(a2.length - 16)];
            allocate.get(bArr2);
            allocate.get(bArr3);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, secretKeySpec, new IvParameterSpec(bArr2));
            return instance.doFinal(bArr3);
        } catch (NoSuchAlgorithmException e) {
            throw new a(e);
        } catch (InvalidKeyException e2) {
            throw new a(e2);
        } catch (IllegalBlockSizeException e3) {
            throw new a(e3);
        } catch (NoSuchPaddingException e4) {
            throw new a(e4);
        } catch (BadPaddingException e5) {
            throw new a(e5);
        } catch (InvalidAlgorithmParameterException e6) {
            throw new a(e6);
        }
    }
}
