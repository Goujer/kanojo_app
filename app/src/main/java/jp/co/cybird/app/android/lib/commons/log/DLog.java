package jp.co.cybird.app.android.lib.commons.log;

import android.util.Log;

public class DLog {
    private static boolean mDebuggable = false;

    public static void setDebuggable(boolean debuggable) {
        mDebuggable = debuggable;
    }

    public static void d(String tag, String msg) {
        if (mDebuggable) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (mDebuggable) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mDebuggable) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mDebuggable) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mDebuggable) {
            Log.i(tag, msg);
        }
    }
}
