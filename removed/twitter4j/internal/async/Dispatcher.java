package twitter4j.internal.async;

public interface Dispatcher {
    void invokeLater(Runnable runnable);

    void shutdown();
}
