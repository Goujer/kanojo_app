package com.google.analytics.tracking.android;

import android.util.Log;
import com.google.analytics.tracking.android.Logger;
import com.google.android.gms.common.util.VisibleForTesting;

class DefaultLoggerImpl implements Logger {
    @VisibleForTesting
    static final String LOG_TAG = "GAV3";
    private Logger.LogLevel mLogLevel = Logger.LogLevel.INFO;

    DefaultLoggerImpl() {
    }

    public void verbose(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.VERBOSE.ordinal()) {
            Log.v(LOG_TAG, formatMessage(msg));
        }
    }

    public void info(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.INFO.ordinal()) {
            Log.i(LOG_TAG, formatMessage(msg));
        }
    }

    public void warn(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.WARNING.ordinal()) {
            Log.w(LOG_TAG, formatMessage(msg));
        }
    }

    public void error(String msg) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            Log.e(LOG_TAG, formatMessage(msg));
        }
    }

    public void error(Exception exception) {
        if (this.mLogLevel.ordinal() <= Logger.LogLevel.ERROR.ordinal()) {
            Log.e(LOG_TAG, (String) null, exception);
        }
    }

    public void setLogLevel(Logger.LogLevel level) {
        this.mLogLevel = level;
    }

    public Logger.LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    private String formatMessage(String msg) {
        return Thread.currentThread().toString() + ": " + msg;
    }
}
