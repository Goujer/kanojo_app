package twitter4j.internal.logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public abstract class Logger {
    private static final LoggerFactory LOGGER_FACTORY;
    private static final String LOGGER_FACTORY_IMPLEMENTATION = "twitter4j.loggerFactory";

    public abstract void debug(String str);

    public abstract void debug(String str, String str2);

    public abstract void error(String str);

    public abstract void error(String str, Throwable th);

    public abstract void info(String str);

    public abstract void info(String str, String str2);

    public abstract boolean isDebugEnabled();

    public abstract boolean isErrorEnabled();

    public abstract boolean isInfoEnabled();

    public abstract boolean isWarnEnabled();

    public abstract void warn(String str);

    public abstract void warn(String str, String str2);

    static {
        LoggerFactory loggerFactory = null;
        String loggerFactoryImpl = System.getProperty(LOGGER_FACTORY_IMPLEMENTATION);
        if (loggerFactoryImpl != null) {
            loggerFactory = getLoggerFactoryIfAvailable(loggerFactoryImpl, loggerFactoryImpl);
        }
        Configuration conf = ConfigurationContext.getInstance();
        String loggerFactoryImpl2 = conf.getLoggerFactory();
        if (loggerFactoryImpl2 != null) {
            loggerFactory = getLoggerFactoryIfAvailable(loggerFactoryImpl2, loggerFactoryImpl2);
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactoryIfAvailable("org.slf4j.impl.StaticLoggerBinder", "twitter4j.internal.logging.SLF4JLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactoryIfAvailable("org.apache.commons.logging.Log", "twitter4j.internal.logging.CommonsLoggingLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactoryIfAvailable("org.apache.log4j.Logger", "twitter4j.internal.logging.Log4JLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = getLoggerFactoryIfAvailable("com.google.appengine.api.urlfetch.URLFetchService", "twitter4j.internal.logging.JULLoggerFactory");
        }
        if (loggerFactory == null) {
            loggerFactory = new StdOutLoggerFactory();
        }
        LOGGER_FACTORY = loggerFactory;
        try {
            Method method = conf.getClass().getMethod("dumpConfiguration", new Class[0]);
            method.setAccessible(true);
            method.invoke(conf, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        }
    }

    private static LoggerFactory getLoggerFactoryIfAvailable(String checkClassName, String implementationClass) {
        try {
            Class.forName(checkClassName);
            return (LoggerFactory) Class.forName(implementationClass).newInstance();
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException | SecurityException e2) {
            return null;
        } catch (IllegalAccessException e3) {
            throw new AssertionError(e3);
        }
    }

    public static Logger getLogger(Class clazz) {
        return LOGGER_FACTORY.getLogger(clazz);
    }
}
