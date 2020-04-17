package twitter4j.internal.async;

import java.lang.reflect.InvocationTargetException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public final class DispatcherFactory {
    private Configuration conf;
    private String dispatcherImpl;

    public DispatcherFactory(Configuration conf2) {
        this.dispatcherImpl = conf2.getDispatcherImpl();
        this.conf = conf2;
    }

    public DispatcherFactory() {
        this(ConfigurationContext.getInstance());
    }

    public Dispatcher getInstance() {
        try {
            return (Dispatcher) Class.forName(this.dispatcherImpl).getConstructor(new Class[]{Configuration.class}).newInstance(new Object[]{this.conf});
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (ClassNotFoundException e3) {
            throw new AssertionError(e3);
        } catch (ClassCastException e4) {
            throw new AssertionError(e4);
        } catch (NoSuchMethodException e5) {
            throw new AssertionError(e5);
        } catch (InvocationTargetException e6) {
            throw new AssertionError(e6);
        }
    }
}
