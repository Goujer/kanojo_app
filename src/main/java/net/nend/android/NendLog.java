package net.nend.android;

import android.util.Log;

final class NendLog {
    static final String TAG = "nend_SDK";

    private NendLog() {
    }

    private static boolean isLoggable(String tag, int level) {
        switch (level) {
            case 2:
            case 3:
                if (!Log.isLoggable(tag, level) || !NendHelper.isDebuggable() || !NendHelper.isDev()) {
                    return false;
                }
                return true;
            default:
                if (!Log.isLoggable(tag, level) || !NendHelper.isDebuggable()) {
                    return false;
                }
                return true;
        }
    }

    private static int outputLog(int priority, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, priority)) {
            return 0;
        }
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String fixedMsg = String.valueOf(stackTraceElement.getClassName()) + "." + stackTraceElement.getMethodName() + ":\n" + msg;
        if (tr != null) {
            fixedMsg = String.valueOf(fixedMsg) + 10 + Log.getStackTraceString(tr);
        }
        return Log.println(priority, tag, fixedMsg);
    }

    static int v(String msg) {
        return outputLog(2, TAG, msg, (Throwable) null);
    }

    static int v(String msg, Throwable tr) {
        return outputLog(2, TAG, msg, tr);
    }

    static int v(String tag, String msg) {
        return outputLog(2, tag, msg, (Throwable) null);
    }

    static int v(String tag, String msg, Throwable tr) {
        return outputLog(2, tag, msg, tr);
    }

    static int v(NendStatus status) {
        return outputLog(2, TAG, status.getMsg(), (Throwable) null);
    }

    static int v(NendStatus status, String message) {
        return outputLog(2, TAG, status.getMsg(message), (Throwable) null);
    }

    static int v(NendStatus status, Throwable tr) {
        return outputLog(2, TAG, status.getMsg(), tr);
    }

    static int d(String msg) {
        return outputLog(3, TAG, msg, (Throwable) null);
    }

    static int d(String msg, Throwable tr) {
        return outputLog(3, TAG, msg, tr);
    }

    static int d(String tag, String msg) {
        return outputLog(3, tag, msg, (Throwable) null);
    }

    static int d(String tag, String msg, Throwable tr) {
        return outputLog(3, tag, msg, tr);
    }

    static int d(NendStatus status) {
        return outputLog(3, TAG, status.getMsg(), (Throwable) null);
    }

    static int d(NendStatus status, String message) {
        return outputLog(3, TAG, status.getMsg(message), (Throwable) null);
    }

    static int d(NendStatus status, Throwable tr) {
        return outputLog(3, TAG, status.getMsg(), tr);
    }

    static int i(String msg) {
        return outputLog(4, TAG, msg, (Throwable) null);
    }

    static int i(String msg, Throwable tr) {
        return outputLog(4, TAG, msg, tr);
    }

    static int i(String tag, String msg) {
        return outputLog(4, tag, msg, (Throwable) null);
    }

    static int i(String tag, String msg, Throwable tr) {
        return outputLog(4, tag, msg, tr);
    }

    static int i(NendStatus status) {
        return outputLog(4, TAG, status.getMsg(), (Throwable) null);
    }

    static int i(NendStatus status, String message) {
        return outputLog(4, TAG, status.getMsg(message), (Throwable) null);
    }

    static int i(NendStatus status, Throwable tr) {
        return outputLog(4, TAG, status.getMsg(), tr);
    }

    static int w(String msg) {
        return outputLog(5, TAG, msg, (Throwable) null);
    }

    static int w(String msg, Throwable tr) {
        return outputLog(5, TAG, msg, tr);
    }

    static int w(String tag, String msg) {
        return outputLog(5, tag, msg, (Throwable) null);
    }

    static int w(String tag, String msg, Throwable tr) {
        return outputLog(5, tag, msg, tr);
    }

    static int w(NendStatus status) {
        return outputLog(5, TAG, status.getMsg(), (Throwable) null);
    }

    static int w(NendStatus status, String message) {
        return outputLog(5, TAG, status.getMsg(message), (Throwable) null);
    }

    static int w(NendStatus status, Throwable tr) {
        return outputLog(5, TAG, status.getMsg(), tr);
    }

    static int e(String msg) {
        return outputLog(6, TAG, msg, (Throwable) null);
    }

    static int e(String msg, Throwable tr) {
        return outputLog(6, TAG, msg, tr);
    }

    static int e(String tag, String msg) {
        return outputLog(6, tag, msg, (Throwable) null);
    }

    static int e(String tag, String msg, Throwable tr) {
        return outputLog(6, tag, msg, tr);
    }

    static int e(NendStatus status) {
        return outputLog(6, TAG, status.getMsg(), (Throwable) null);
    }

    static int e(NendStatus status, String message) {
        return outputLog(6, TAG, status.getMsg(message), (Throwable) null);
    }

    static int e(NendStatus status, Throwable tr) {
        return outputLog(6, TAG, status.getMsg(), tr);
    }
}
