package twitter4j.internal.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

final class Log4JLogger extends Logger {
    private final Logger LOGGER;

    Log4JLogger(Logger logger) {
        this.LOGGER = logger;
    }

    public boolean isDebugEnabled() {
        return this.LOGGER.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return this.LOGGER.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return this.LOGGER.isEnabledFor(Level.WARN);
    }

    public boolean isErrorEnabled() {
        return this.LOGGER.isEnabledFor(Level.WARN);
    }

    public void debug(String message) {
        this.LOGGER.debug(message);
    }

    public void debug(String message, String message2) {
        debug(message + message2);
    }

    public void info(String message) {
        this.LOGGER.info(message);
    }

    public void info(String message, String message2) {
        info(message + message2);
    }

    public void warn(String message) {
        this.LOGGER.warn(message);
    }

    public void warn(String message, String message2) {
        warn(message + message2);
    }

    public void error(String message) {
        this.LOGGER.error(message);
    }

    public void error(String message, Throwable th) {
        this.LOGGER.error(message, th);
    }
}
