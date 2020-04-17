package twitter4j.internal.logging;

import java.util.logging.Logger;

final class JULLoggerFactory extends LoggerFactory {
    JULLoggerFactory() {
    }

    public Logger getLogger(Class clazz) {
        return new JULLogger(Logger.getLogger(clazz.getName()));
    }
}
