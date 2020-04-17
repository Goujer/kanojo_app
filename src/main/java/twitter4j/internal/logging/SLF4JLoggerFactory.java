package twitter4j.internal.logging;

import org.slf4j.LoggerFactory;

final class SLF4JLoggerFactory extends LoggerFactory {
    SLF4JLoggerFactory() {
    }

    public Logger getLogger(Class clazz) {
        return new SLF4JLogger(LoggerFactory.getLogger(clazz));
    }
}
