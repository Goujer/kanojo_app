package twitter4j.internal.logging;

final class StdOutLoggerFactory extends LoggerFactory {
    private static final Logger SINGLETON = new StdOutLogger();

    StdOutLoggerFactory() {
    }

    public Logger getLogger(Class clazz) {
        return SINGLETON;
    }
}
