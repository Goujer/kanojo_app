package twitter4j.internal.logging;

import java.util.Date;
import twitter4j.conf.ConfigurationContext;

final class StdOutLogger extends Logger {
    private static final boolean DEBUG = ConfigurationContext.getInstance().isDebugEnabled();

    StdOutLogger() {
    }

    public boolean isDebugEnabled() {
        return DEBUG;
    }

    public boolean isInfoEnabled() {
        return true;
    }

    public boolean isWarnEnabled() {
        return true;
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public void debug(String message) {
        if (DEBUG) {
            System.out.println("[" + new Date() + "]" + message);
        }
    }

    public void debug(String message, String message2) {
        if (DEBUG) {
            debug(message + message2);
        }
    }

    public void info(String message) {
        System.out.println("[" + new Date() + "]" + message);
    }

    public void info(String message, String message2) {
        info(message + message2);
    }

    public void warn(String message) {
        System.out.println("[" + new Date() + "]" + message);
    }

    public void warn(String message, String message2) {
        warn(message + message2);
    }

    public void error(String message) {
        System.out.println("[" + new Date() + "]" + message);
    }

    public void error(String message, Throwable th) {
        System.out.println(message);
        th.printStackTrace(System.err);
    }
}
