package com.google.tagmanager;

public interface Logger {

    public enum LogLevel {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        NONE
    }

    void d(String str);

    void d(String str, Throwable th);

    void e(String str);

    void e(String str, Throwable th);

    LogLevel getLogLevel();

    void i(String str);

    void i(String str, Throwable th);

    void setLogLevel(LogLevel logLevel);

    void v(String str);

    void v(String str, Throwable th);

    void w(String str);

    void w(String str, Throwable th);
}
