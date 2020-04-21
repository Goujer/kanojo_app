package com.google.tagmanager;

import com.google.tagmanager.Logger;

class NoOpLogger implements Logger {
    NoOpLogger() {
    }

    public void e(String message) {
    }

    public void e(String message, Throwable t) {
    }

    public void w(String message) {
    }

    public void w(String message, Throwable t) {
    }

    public void i(String message) {
    }

    public void i(String message, Throwable t) {
    }

    public void d(String message) {
    }

    public void d(String message, Throwable t) {
    }

    public void v(String message) {
    }

    public void v(String message, Throwable t) {
    }

    public Logger.LogLevel getLogLevel() {
        return Logger.LogLevel.NONE;
    }

    public void setLogLevel(Logger.LogLevel logLevel) {
    }
}
