package com.google.tagmanager;

import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.Logger;

final class Log {
    @VisibleForTesting
    static Logger sLogger = new DefaultLogger();

    Log() {
    }

    public static void setLogger(Logger logger) {
        if (logger == null) {
            sLogger = new NoOpLogger();
        } else {
            sLogger = logger;
        }
    }

    public static Logger getLogger() {
        if (sLogger.getClass() == NoOpLogger.class) {
            return null;
        }
        return sLogger;
    }

    public static void e(String message) {
        sLogger.e(message);
    }

    public static void e(String message, Throwable t) {
        sLogger.e(message, t);
    }

    public static void w(String message) {
        sLogger.w(message);
    }

    public static void w(String message, Throwable t) {
        sLogger.w(message, t);
    }

    public static void i(String message) {
        sLogger.i(message);
    }

    public static void i(String message, Throwable t) {
        sLogger.i(message, t);
    }

    public static void d(String message) {
        sLogger.d(message);
    }

    public static void d(String message, Throwable t) {
        sLogger.d(message, t);
    }

    public static void v(String message) {
        sLogger.v(message);
    }

    public static void v(String message, Throwable t) {
        sLogger.v(message, t);
    }

    public static Logger.LogLevel getLogLevel() {
        return sLogger.getLogLevel();
    }
}
