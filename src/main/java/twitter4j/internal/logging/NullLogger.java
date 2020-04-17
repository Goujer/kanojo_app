package twitter4j.internal.logging;

final class NullLogger extends Logger {
    NullLogger() {
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public boolean isErrorEnabled() {
        return false;
    }

    public void debug(String message) {
    }

    public void debug(String message, String message2) {
    }

    public void info(String message) {
    }

    public void info(String message, String message2) {
    }

    public void warn(String message) {
    }

    public void warn(String message, String message2) {
    }

    public void error(String message) {
    }

    public void error(String message, Throwable th) {
    }
}
