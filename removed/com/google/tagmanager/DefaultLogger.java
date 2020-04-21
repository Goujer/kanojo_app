package com.google.tagmanager;

import android.util.Log;
import com.google.tagmanager.Logger;

class DefaultLogger implements Logger {
    private static final String LOG_TAG = "GoogleTagManager";
    private Logger.LogLevel mLogLevel = Logger.LogLevel.WARNING;

    DefaultLogger() {
    }

    public void e(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            Log.e(LOG_TAG, message);
        }
    }

    public void e(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            Log.e(LOG_TAG, message, t);
        }
    }

    public void w(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            Log.w(LOG_TAG, message);
        }
    }

    public void w(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            Log.w(LOG_TAG, message, t);
        }
    }

    public void i(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            Log.i(LOG_TAG, message);
        }
    }

    public void i(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            Log.i(LOG_TAG, message, t);
        }
    }

    public void d(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.DEBUG.ordinal()) {
            Log.d(LOG_TAG, message);
        }
    }

    public void d(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.DEBUG.ordinal()) {
            Log.d(LOG_TAG, message, t);
        }
    }

    public void v(String message) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            Log.v(LOG_TAG, message);
        }
    }

    public void v(String message, Throwable t) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            Log.v(LOG_TAG, message, t);
        }
    }

    public Logger.LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    public void setLogLevel(Logger.LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }
}
