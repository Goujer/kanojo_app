package twitter4j.internal.logging;

import org.apache.log4j.Logger;

final class Log4JLoggerFactory extends LoggerFactory {
    Log4JLoggerFactory() {
    }

    public Logger getLogger(Class clazz) {
        return new Log4JLogger(Logger.getLogger(clazz));
    }
}
