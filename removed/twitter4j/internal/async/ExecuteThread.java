package twitter4j.internal.async;

import twitter4j.internal.logging.Logger;

/* compiled from: DispatcherImpl */
class ExecuteThread extends Thread {
    private static Logger logger = Logger.getLogger(ExecuteThread.class);
    private boolean alive = true;
    DispatcherImpl q;

    ExecuteThread(String name, DispatcherImpl q2, int index) {
        super(name + "[" + index + "]");
        this.q = q2;
    }

    public void shutdown() {
        this.alive = false;
    }

    public void run() {
        while (this.alive) {
            Runnable task = this.q.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Exception ex) {
                    logger.error("Got an exception while running a task:", ex);
                }
            }
        }
    }
}
