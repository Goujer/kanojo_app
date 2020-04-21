package twitter4j.internal.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

final class JULLogger extends Logger {
    private final Logger LOGGER;

    JULLogger(Logger logger) {
        this.LOGGER = logger;
    }

    public boolean isDebugEnabled() {
        return this.LOGGER.isLoggable(Level.FINE);
    }

    public boolean isInfoEnabled() {
        return this.LOGGER.isLoggable(Level.INFO);
    }

    public boolean isWarnEnabled() {
        return this.LOGGER.isLoggable(Level.WARNING);
    }

    public boolean isErrorEnabled() {
        return this.LOGGER.isLoggable(Level.SEVERE);
    }

    public void debug(String message) {
        this.LOGGER.fine(message);
    }

    public void debug(String message, String message2) {
        this.LOGGER.fine(message + message2);
    }

    public void info(String message) {
        this.LOGGER.info(message);
    }

    public void info(String message, String message2) {
        this.LOGGER.info(message + message2);
    }

    public void warn(String message) {
        this.LOGGER.warning(message);
    }

    public void warn(String message, String message2) {
        this.LOGGER.warning(message + message2);
    }

    public void error(String message) {
        this.LOGGER.severe(message);
    }

    public void error(String message, Throwable th) {
        this.LOGGER.severe(message + th.getMessage());
    }
}
