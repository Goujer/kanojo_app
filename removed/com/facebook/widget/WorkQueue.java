package com.facebook.widget;

import com.facebook.Settings;
import java.util.concurrent.Executor;

class WorkQueue {
    static final /* synthetic */ boolean $assertionsDisabled = (!WorkQueue.class.desiredAssertionStatus());
    public static final int DEFAULT_MAX_CONCURRENT = 8;
    private final Executor executor;
    private final int maxConcurrent;
    /* access modifiers changed from: private */
    public WorkNode pendingJobs;
    private int runningCount;
    private WorkNode runningJobs;
    /* access modifiers changed from: private */
    public final Object workLock;

    interface WorkItem {
        boolean cancel();

        boolean isRunning();

        void moveToFront();
    }

    WorkQueue() {
        this(8);
    }

    WorkQueue(int maxConcurrent2) {
        this(maxConcurrent2, Settings.getExecutor());
    }

    WorkQueue(int maxConcurrent2, Executor executor2) {
        this.workLock = new Object();
        this.runningJobs = null;
        this.runningCount = 0;
        this.maxConcurrent = maxConcurrent2;
        this.executor = executor2;
    }

    /* access modifiers changed from: package-private */
    public WorkItem addActiveWorkItem(Runnable callback) {
        return addActiveWorkItem(callback, true);
    }

    /* access modifiers changed from: package-private */
    public WorkItem addActiveWorkItem(Runnable callback, boolean addToFront) {
        WorkNode node = new WorkNode(callback);
        synchronized (this.workLock) {
            this.pendingJobs = node.addToList(this.pendingJobs, addToFront);
        }
        startItem();
        return node;
    }

    /* access modifiers changed from: package-private */
    public void validate() {
        synchronized (this.workLock) {
            int count = 0;
            if (this.runningJobs != null) {
                WorkNode walk = this.runningJobs;
                do {
                    walk.verify(true);
                    count++;
                    walk = walk.getNext();
                } while (walk != this.runningJobs);
            }
            if (!$assertionsDisabled && this.runningCount != count) {
                throw new AssertionError();
            }
        }
    }

    private void startItem() {
        finishItemAndStartNew((WorkNode) null);
    }

    /* access modifiers changed from: private */
    public void finishItemAndStartNew(WorkNode finished) {
        WorkNode ready = null;
        synchronized (this.workLock) {
            if (finished != null) {
                this.runningJobs = finished.removeFromList(this.runningJobs);
                this.runningCount--;
            }
            if (this.runningCount < this.maxConcurrent && (ready = this.pendingJobs) != null) {
                this.pendingJobs = ready.removeFromList(this.pendingJobs);
                this.runningJobs = ready.addToList(this.runningJobs, false);
                this.runningCount++;
                ready.setIsRunning(true);
            }
        }
        if (ready != null) {
            execute(ready);
        }
    }

    private void execute(final WorkNode node) {
        this.executor.execute(new Runnable() {
            public void run() {
                try {
                    node.getCallback().run();
                } finally {
                    WorkQueue.this.finishItemAndStartNew(node);
                }
            }
        });
    }

    private class WorkNode implements WorkItem {
        static final /* synthetic */ boolean $assertionsDisabled = (!WorkQueue.class.desiredAssertionStatus());
        private final Runnable callback;
        private boolean isRunning;
        private WorkNode next;
        private WorkNode prev;

        WorkNode(Runnable callback2) {
            this.callback = callback2;
        }

        public boolean cancel() {
            synchronized (WorkQueue.this.workLock) {
                if (isRunning()) {
                    return false;
                }
                WorkQueue.this.pendingJobs = removeFromList(WorkQueue.this.pendingJobs);
                return true;
            }
        }

        public void moveToFront() {
            synchronized (WorkQueue.this.workLock) {
                if (!isRunning()) {
                    WorkQueue.this.pendingJobs = removeFromList(WorkQueue.this.pendingJobs);
                    WorkQueue.this.pendingJobs = addToList(WorkQueue.this.pendingJobs, true);
                }
            }
        }

        public boolean isRunning() {
            return this.isRunning;
        }

        /* access modifiers changed from: package-private */
        public Runnable getCallback() {
            return this.callback;
        }

        /* access modifiers changed from: package-private */
        public WorkNode getNext() {
            return this.next;
        }

        /* access modifiers changed from: package-private */
        public void setIsRunning(boolean isRunning2) {
            this.isRunning = isRunning2;
        }

        /* Debug info: failed to restart local var, previous not found, register: 2 */
        /* access modifiers changed from: package-private */
        public WorkNode addToList(WorkNode list, boolean addToFront) {
            if (!$assertionsDisabled && this.next != null) {
                throw new AssertionError();
            } else if ($assertionsDisabled || this.prev == null) {
                if (list == null) {
                    this.prev = this;
                    this.next = this;
                    list = this;
                } else {
                    this.next = list;
                    this.prev = list.prev;
                    WorkNode workNode = this.next;
                    this.prev.next = this;
                    workNode.prev = this;
                }
                if (addToFront) {
                    return this;
                }
                return list;
            } else {
                throw new AssertionError();
            }
        }

        /* access modifiers changed from: package-private */
        public WorkNode removeFromList(WorkNode list) {
            if (!$assertionsDisabled && this.next == null) {
                throw new AssertionError();
            } else if ($assertionsDisabled || this.prev != null) {
                if (list == this) {
                    if (this.next == this) {
                        list = null;
                    } else {
                        list = this.next;
                    }
                }
                this.next.prev = this.prev;
                this.prev.next = this.next;
                this.prev = null;
                this.next = null;
                return list;
            } else {
                throw new AssertionError();
            }
        }

        /* access modifiers changed from: package-private */
        public void verify(boolean shouldBeRunning) {
            if (!$assertionsDisabled && this.prev.next != this) {
                throw new AssertionError();
            } else if (!$assertionsDisabled && this.next.prev != this) {
                throw new AssertionError();
            } else if (!$assertionsDisabled && isRunning() != shouldBeRunning) {
                throw new AssertionError();
            }
        }
    }
}
