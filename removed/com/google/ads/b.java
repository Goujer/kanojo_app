package com.google.ads;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;

public class b {
    private static b c = null;
    private final BigInteger a = d();
    private BigInteger b = BigInteger.ONE;

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (c == null) {
                c = new b();
            }
            bVar = c;
        }
        return bVar;
    }

    public synchronized BigInteger b() {
        return this.a;
    }

    public synchronized BigInteger c() {
        BigInteger bigInteger;
        bigInteger = this.b;
        this.b = this.b.add(BigInteger.ONE);
        return bigInteger;
    }

    private b() {
    }

    private static BigInteger d() {
        try {
            MessageDigest instance = MessageDigest.getInstance(Digest.MD5);
            UUID randomUUID = UUID.randomUUID();
            instance.update(a(randomUUID.getLeastSignificantBits()));
            instance.update(a(randomUUID.getMostSignificantBits()));
            byte[] bArr = new byte[9];
            bArr[0] = 0;
            System.arraycopy(instance.digest(), 0, bArr, 1, 8);
            return new BigInteger(bArr);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find MD5 message digest algorithm.");
        }
    }

    private static byte[] a(long j) {
        return BigInteger.valueOf(j).toByteArray();
    }
}
