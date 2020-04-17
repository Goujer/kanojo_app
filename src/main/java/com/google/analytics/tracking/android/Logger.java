package com.google.analytics.tracking.android;

public interface Logger {

    public enum LogLevel {
        VERBOSE,
        INFO,
        WARNING,
        ERROR
    }

    void error(Exception exc);

    void error(String str);

    LogLevel getLogLevel();

    void info(String str);

    void setLogLevel(LogLevel logLevel);

    void verbose(String str);

    void warn(String str);
}
