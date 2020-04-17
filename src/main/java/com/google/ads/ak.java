package com.google.ads;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.google.ads.an;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ak extends aj {
    static boolean c = false;
    private static Method d = null;
    private static Method e = null;
    private static Method f = null;
    private static Method g = null;
    private static Method h = null;
    private static String i = null;
    private static long j = 0;

    static class a extends Exception {
        public a() {
        }

        public a(Throwable th) {
            super(th);
        }
    }

    public static ak a(String str, Context context) {
        b(str, context);
        return new ak(context);
    }

    protected static synchronized void b(String str, Context context) {
        synchronized (ak.class) {
            if (!c) {
                try {
                    i = str;
                    f(context);
                    j = b().longValue();
                    c = true;
                } catch (a | UnsupportedOperationException e2) {
                }
            }
        }
    }

    protected ak(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027 A[ExcHandler: IOException (e java.io.IOException), Splitter:B:1:0x0001] */
    public void b(Context context) {
        try {
            a(1, c());
            a(2, a());
            a(25, b().longValue());
            a(24, d(context));
        } catch (a e2) {
        } catch (IOException e3) {
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x007d A[ExcHandler: IOException (e java.io.IOException), Splitter:B:6:0x0010] */
    public void c(Context context) {
        try {
            a(2, a());
        } catch (a e2) {
        } catch (IOException e3) {
        }
        try {
            a(1, c());
        } catch (a e4) {
        } catch (IOException e32) {
        }
        try {
            long longValue = b().longValue();
            a(25, longValue);
            if (j != 0) {
                a(17, longValue - j);
                a(23, j);
            }
            ArrayList<Long> a2 = a(this.a, this.b);
            a(14, a2.get(0).longValue());
            a(15, a2.get(1).longValue());
            if (a2.size() >= 3) {
                a(16, a2.get(2).longValue());
            }
            a(27, e(context));
        } catch (a e5) {
        } catch (IOException e322) {
        }
    }

    static String a() throws a {
        if (i != null) {
            return i;
        }
        throw new a();
    }

    static Long b() throws a {
        if (d == null) {
            throw new a();
        }
        try {
            return (Long) d.invoke((Object) null, new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new a(e2);
        } catch (InvocationTargetException e3) {
            throw new a(e3);
        }
    }

    static String d(Context context) throws a {
        if (h == null) {
            throw new a();
        }
        try {
            String str = (String) h.invoke((Object) null, new Object[]{context});
            if (str != null) {
                return str;
            }
            throw new a();
        } catch (IllegalAccessException e2) {
            throw new a(e2);
        } catch (InvocationTargetException e3) {
            throw new a(e3);
        }
    }

    static String c() throws a {
        if (e == null) {
            throw new a();
        }
        try {
            return (String) e.invoke((Object) null, new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new a(e2);
        } catch (InvocationTargetException e3) {
            throw new a(e3);
        }
    }

    static ArrayList<Long> a(MotionEvent motionEvent, DisplayMetrics displayMetrics) throws a {
        if (g == null || motionEvent == null) {
            throw new a();
        }
        try {
            return (ArrayList) g.invoke((Object) null, new Object[]{motionEvent, displayMetrics});
        } catch (IllegalAccessException e2) {
            throw new a(e2);
        } catch (InvocationTargetException e3) {
            throw new a(e3);
        }
    }

    static String e(Context context) throws a {
        if (f == null) {
            throw new a();
        }
        try {
            ByteBuffer byteBuffer = (ByteBuffer) f.invoke((Object) null, new Object[]{context});
            if (byteBuffer != null) {
                return aq.a(byteBuffer.array(), false);
            }
            throw new a();
        } catch (IllegalAccessException e2) {
            throw new a(e2);
        } catch (InvocationTargetException e3) {
            throw new a(e3);
        }
    }

    private static String b(byte[] bArr, String str) throws a {
        try {
            return new String(an.a(bArr, str), "UTF-8");
        } catch (an.a e2) {
            throw new a(e2);
        } catch (ap e3) {
            throw new a(e3);
        } catch (UnsupportedEncodingException e4) {
            throw new a(e4);
        }
    }

    private static void f(Context context) throws a {
        try {
            byte[] a2 = an.a(ao.a());
            byte[] a3 = an.a(a2, ao.b());
            File cacheDir = context.getCacheDir();
            if (cacheDir == null && (cacheDir = context.getDir("dex", 0)) == null) {
                throw new a();
            }
            File createTempFile = File.createTempFile("ads", ".jar", cacheDir);
            FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
            fileOutputStream.write(a3, 0, a3.length);
            fileOutputStream.close();
            DexClassLoader dexClassLoader = new DexClassLoader(createTempFile.getAbsolutePath(), cacheDir.getAbsolutePath(), (String) null, context.getClassLoader());
            Class loadClass = dexClassLoader.loadClass(b(a2, ao.c()));
            Class loadClass2 = dexClassLoader.loadClass(b(a2, ao.i()));
            Class loadClass3 = dexClassLoader.loadClass(b(a2, ao.g()));
            Class loadClass4 = dexClassLoader.loadClass(b(a2, ao.k()));
            Class loadClass5 = dexClassLoader.loadClass(b(a2, ao.e()));
            d = loadClass.getMethod(b(a2, ao.d()), new Class[0]);
            e = loadClass2.getMethod(b(a2, ao.j()), new Class[0]);
            f = loadClass3.getMethod(b(a2, ao.h()), new Class[]{Context.class});
            g = loadClass4.getMethod(b(a2, ao.l()), new Class[]{MotionEvent.class, DisplayMetrics.class});
            h = loadClass5.getMethod(b(a2, ao.f()), new Class[]{Context.class});
            String name = createTempFile.getName();
            createTempFile.delete();
            new File(cacheDir, name.replace(".jar", ".dex")).delete();
        } catch (ap e2) {
            throw new a(e2);
        } catch (FileNotFoundException e3) {
            throw new a(e3);
        } catch (IOException e4) {
            throw new a(e4);
        } catch (ClassNotFoundException e5) {
            throw new a(e5);
        } catch (an.a e6) {
            throw new a(e6);
        } catch (NoSuchMethodException e7) {
            throw new a(e7);
        } catch (NullPointerException e8) {
            throw new a(e8);
        }
    }
}
