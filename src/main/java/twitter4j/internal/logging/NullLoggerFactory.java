package twitter4j.internal.logging;

final class NullLoggerFactory extends LoggerFactory {
    private static final Logger SINGLETON = new NullLogger();

    NullLoggerFactory() {
    }

    public Logger getLogger(Class clazz) {
        return SINGLETON;
    }
}
