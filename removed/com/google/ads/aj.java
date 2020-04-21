package com.google.ads;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;

public abstract class aj implements ai {
    protected MotionEvent a = null;
    protected DisplayMetrics b = null;
    private au c = null;
    private ByteArrayOutputStream d = null;

    /* access modifiers changed from: protected */
    public abstract void b(Context context);

    /* access modifiers changed from: protected */
    public abstract void c(Context context);

    protected aj(Context context) {
        try {
            this.b = context.getResources().getDisplayMetrics();
        } catch (UnsupportedOperationException e) {
            this.b = new DisplayMetrics();
            this.b.density = 1.0f;
        }
    }

    public String a(Context context) {
        return a(context, (String) null, false);
    }

    public String a(Context context, String str) {
        return a(context, str, true);
    }

    public void a(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (this.a != null) {
                this.a.recycle();
            }
            this.a = MotionEvent.obtain(motionEvent);
        }
    }

    public void a(int i, int i2, int i3) {
        if (this.a != null) {
            this.a.recycle();
        }
        this.a = MotionEvent.obtain(0, (long) i3, 1, ((float) i) * this.b.density, ((float) i2) * this.b.density, 0.0f, 0.0f, 0, 0.0f, 0.0f, 0, 0);
    }

    private String a(Context context, String str, boolean z) {
        try {
            a();
            if (z) {
                c(context);
            } else {
                b(context);
            }
            byte[] b2 = b();
            if (b2.length == 0) {
                return Integer.toString(5);
            }
            return a(b2, str);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toString(7);
        } catch (UnsupportedEncodingException e2) {
            return Integer.toString(7);
        } catch (IOException e3) {
            return Integer.toString(3);
        }
    }

    /* access modifiers changed from: protected */
    public void a(int i, long j) throws IOException {
        this.c.a(i, j);
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str) throws IOException {
        this.c.a(i, str);
    }

    private void a() {
        this.d = new ByteArrayOutputStream();
        this.c = au.a((OutputStream) this.d);
    }

    private byte[] b() throws IOException {
        this.c.a();
        return this.d.toByteArray();
    }

    /* access modifiers changed from: package-private */
    public String a(byte[] bArr, String str) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        byte[] array;
        if (bArr.length > 239) {
            a();
            a(20, 1);
            bArr = b();
        }
        if (bArr.length < 239) {
            byte[] bArr2 = new byte[(239 - bArr.length)];
            new SecureRandom().nextBytes(bArr2);
            array = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).put(bArr2).array();
        } else {
            array = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).array();
        }
        MessageDigest instance = MessageDigest.getInstance(Digest.MD5);
        instance.update(array);
        byte[] array2 = ByteBuffer.allocate(256).put(instance.digest()).put(array).array();
        byte[] bArr3 = new byte[256];
        new ag().a(array2, bArr3);
        if (str != null && str.length() > 0) {
            a(str, bArr3);
        }
        return aq.a(bArr3, false);
    }

    /* access modifiers changed from: package-private */
    public void a(String str, byte[] bArr) throws UnsupportedEncodingException {
        if (str.length() > 32) {
            str = str.substring(0, 32);
        }
        new ar(str.getBytes("UTF-8")).a(bArr);
    }
}
