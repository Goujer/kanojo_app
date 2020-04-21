package twitter4j.internal.async;

import java.util.LinkedList;
import java.util.List;
import twitter4j.conf.Configuration;

final class DispatcherImpl implements Dispatcher {
    /* access modifiers changed from: private */
    public boolean active = true;
    private final List<Runnable> q = new LinkedList();
    private ExecuteThread[] threads;
    final Object ticket = new Object();

    public DispatcherImpl(Configuration conf) {
        this.threads = new ExecuteThread[conf.getAsyncNumThreads()];
        for (int i = 0; i < this.threads.length; i++) {
            this.threads[i] = new ExecuteThread("Twitter4J Async Dispatcher", this, i);
            this.threads[i].setDaemon(true);
            this.threads[i].start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (DispatcherImpl.this.active) {
                    DispatcherImpl.this.shutdown();
                }
            }
        });
    }

    public synchronized void invokeLater(Runnable task) {
        synchronized (this.q) {
            this.q.add(task);
        }
        synchronized (this.ticket) {
            this.ticket.notify();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
        r2 = r4.ticket;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        monitor-enter(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r4.ticket.wait();
     */
    public Runnable poll() {
        Object obj;
        Runnable task;
        while (this.active) {
            synchronized (this.q) {
                if (this.q.size() > 0 && (task = this.q.remove(0)) != null) {
                    return task;
                }
            }
        }
        return null;
    }

    public synchronized void shutdown() {
        if (this.active) {
            this.active = false;
            for (ExecuteThread thread : this.threads) {
                thread.shutdown();
            }
            synchronized (this.ticket) {
                this.ticket.notify();
            }
        }
    }
}
