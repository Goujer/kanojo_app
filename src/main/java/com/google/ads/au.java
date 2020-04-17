package com.google.ads;

import java.io.IOException;
import java.io.OutputStream;

public final class au {
    private final byte[] a;
    private final int b;
    private int c;
    private int d = 0;
    private final OutputStream e;

    private au(OutputStream outputStream, byte[] bArr) {
        this.e = outputStream;
        this.a = bArr;
        this.c = 0;
        this.b = bArr.length;
    }

    public static au a(OutputStream outputStream) {
        return a(outputStream, 4096);
    }

    public static au a(OutputStream outputStream, int i) {
        return new au(outputStream, new byte[i]);
    }

    public void a(int i, long j) throws IOException {
        a(i, 0);
        a(j);
    }

    public void a(int i, String str) throws IOException {
        a(i, 2);
        a(str);
    }

    public void a(long j) throws IOException {
        b(j);
    }

    public void a(String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        b(bytes.length);
        a(bytes);
    }

    private void b() throws IOException {
        if (this.e == null) {
            throw new a();
        }
        this.e.write(this.a, 0, this.c);
        this.c = 0;
    }

    public void a() throws IOException {
        if (this.e != null) {
            b();
        }
    }

    public static class a extends IOException {
        a() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }
    }

    public void a(byte b2) throws IOException {
        if (this.c == this.b) {
            b();
        }
        byte[] bArr = this.a;
        int i = this.c;
        this.c = i + 1;
        bArr[i] = b2;
        this.d++;
    }

    public void a(int i) throws IOException {
        a((byte) i);
    }

    public void a(byte[] bArr) throws IOException {
        a(bArr, 0, bArr.length);
    }

    public void a(byte[] bArr, int i, int i2) throws IOException {
        if (this.b - this.c >= i2) {
            System.arraycopy(bArr, i, this.a, this.c, i2);
            this.c += i2;
            this.d += i2;
            return;
        }
        int i3 = this.b - this.c;
        System.arraycopy(bArr, i, this.a, this.c, i3);
        int i4 = i + i3;
        int i5 = i2 - i3;
        this.c = this.b;
        this.d = i3 + this.d;
        b();
        if (i5 <= this.b) {
            System.arraycopy(bArr, i4, this.a, 0, i5);
            this.c = i5;
        } else {
            this.e.write(bArr, i4, i5);
        }
        this.d += i5;
    }

    public void a(int i, int i2) throws IOException {
        b(av.a(i, i2));
    }

    public void b(int i) throws IOException {
        while ((i & -128) != 0) {
            a((i & 127) | 128);
            i >>>= 7;
        }
        a(i);
    }

    public void b(long j) throws IOException {
        while ((-128 & j) != 0) {
            a((((int) j) & 127) | 128);
            j >>>= 7;
        }
        a((int) j);
    }
}
