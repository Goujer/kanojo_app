package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

class GAServiceProxy implements ServiceProxy, AnalyticsGmsCoreClient.OnConnectedListener, AnalyticsGmsCoreClient.OnConnectionFailedListener {
    private static final long FAILED_CONNECT_WAIT_TIME = 3000;
    private static final int MAX_TRIES = 2;
    private static final long RECONNECT_WAIT_TIME = 5000;
    private static final long SERVICE_CONNECTION_TIMEOUT = 300000;
    private volatile AnalyticsClient client;
    private Clock clock;
    private volatile int connectTries;
    private final Context ctx;
    private volatile Timer disconnectCheckTimer;
    private volatile Timer failedConnectTimer;
    private boolean forceLocalDispatch;
    private final GoogleAnalytics gaInstance;
    private long idleTimeout;
    private volatile long lastRequestTime;
    private boolean pendingClearHits;
    private boolean pendingDispatch;
    private boolean pendingServiceDisconnect;
    private final ConcurrentLinkedQueue<HitParams> queue;
    private volatile Timer reConnectTimer;
    private volatile ConnectState state;
    private AnalyticsStore store;
    private AnalyticsStore testStore;
    private final AnalyticsThread thread;

    private enum ConnectState {
        CONNECTING,
        CONNECTED_SERVICE,
        CONNECTED_LOCAL,
        BLOCKED,
        PENDING_CONNECTION,
        PENDING_DISCONNECT,
        DISCONNECTED
    }

    @VisibleForTesting
	private GAServiceProxy(Context ctx2, AnalyticsThread thread2, AnalyticsStore store2, GoogleAnalytics gaInstance2) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.idleTimeout = 300000;
        this.testStore = store2;
        this.ctx = ctx2;
        this.thread = thread2;
        this.gaInstance = gaInstance2;
        this.clock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.connectTries = 0;
        this.state = ConnectState.DISCONNECTED;
    }

    GAServiceProxy(Context ctx2, AnalyticsThread thread2) {
        this(ctx2, thread2, null, GoogleAnalytics.getInstance(ctx2));
    }

//    void setClock(Clock clock2) {
//        this.clock = clock2;
//    }

    public void putHit(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, List<Command> commands) {
        Log.v("putHit called");
        this.queue.add(new HitParams(wireFormatParams, hitTimeInMilliseconds, path, commands));
        sendQueue();
    }

    public void dispatch() {
        switch (this.state) {
            case CONNECTED_LOCAL:
                dispatchToStore();
                return;
            case CONNECTED_SERVICE:
                return;
            default:
                this.pendingDispatch = true;
		}
    }

    public void clearHits() {
        Log.v("clearHits called");
        this.queue.clear();
        switch (this.state) {
            case CONNECTED_LOCAL:
                this.store.clearHits(0);
                this.pendingClearHits = false;
                return;
            case CONNECTED_SERVICE:
                this.client.clearHits();
                this.pendingClearHits = false;
                return;
            default:
                this.pendingClearHits = true;
		}
    }

    public synchronized void setForceLocalDispatch() {
        if (!this.forceLocalDispatch) {
            Log.v("setForceLocalDispatch called.");
            this.forceLocalDispatch = true;
            switch (this.state) {
                case CONNECTED_LOCAL:
                case PENDING_CONNECTION:
                case PENDING_DISCONNECT:
                case DISCONNECTED:
                    break;
                case CONNECTED_SERVICE:
                    disconnectFromService();
                    break;
                case CONNECTING:
                    this.pendingServiceDisconnect = true;
                    break;
            }
        }
    }

    private Timer cancelTimer(Timer timer) {
        if (timer == null) {
            return null;
        }
        timer.cancel();
        return null;
    }

    private void clearAllTimers() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
    }

    public void createService() {
        if (this.client == null) {
            this.client = new AnalyticsGmsCoreClient(this.ctx, this, this);
            connectToService();
        }
    }

//    void createService(AnalyticsClient client2) {
//        if (this.client == null) {
//            this.client = client2;
//            connectToService();
//        }
//    }

//    public void setIdleTimeout(long idleTimeout2) {
//        this.idleTimeout = idleTimeout2;
//    }

	/* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
	private synchronized void sendQueue() {
		if (Thread.currentThread().equals(this.thread.getThread())) {
			if (this.pendingClearHits) {
				clearHits();
			}
			switch (this.state) {
				case CONNECTED_LOCAL:
					while (!this.queue.isEmpty()) {
						HitParams hitParams = this.queue.poll();
						Log.v("Sending hit to store  " + hitParams);
						this.store.putHit(hitParams.getWireFormatParams(), hitParams.getHitTimeInMilliseconds(), hitParams.getPath(), hitParams.getCommands());
					}
					if (this.pendingDispatch) {
						dispatchToStore();
						break;
					}
					break;
				case CONNECTED_SERVICE:
					while (!this.queue.isEmpty()) {
						HitParams hitParams2 = this.queue.peek();
						Log.v("Sending hit to service   " + hitParams2);
						if (!this.gaInstance.isDryRunEnabled()) {
							this.client.sendHit(hitParams2.getWireFormatParams(), hitParams2.getHitTimeInMilliseconds(), hitParams2.getPath(), hitParams2.getCommands());
						} else {
							Log.v("Dry run enabled. Hit not actually sent to service.");
						}
						this.queue.poll();
					}
					this.lastRequestTime = this.clock.currentTimeMillis();
					break;
				case DISCONNECTED:
					Log.v("Need to reconnect");
					if (!this.queue.isEmpty()) {
						connectToService();
						break;
					}
					break;
			}
		} else {
			this.thread.getQueue().add(new Runnable() {
				/* class com.google.analytics.tracking.android.GAServiceProxy.AnonymousClass2 */

				public void run() {
					GAServiceProxy.this.sendQueue();
				}
			});
		}
	}


    private void dispatchToStore() {
        this.store.dispatch();
        this.pendingDispatch = false;
    }

    private synchronized void useStore() {
        if (this.state != ConnectState.CONNECTED_LOCAL) {
            clearAllTimers();
            Log.v("falling back to local store");
            if (this.testStore != null) {
                this.store = this.testStore;
            } else {
                GAServiceManager instance = GAServiceManager.getInstance();
                instance.initialize(this.ctx, this.thread);
                this.store = instance.getStore();
            }
            this.state = ConnectState.CONNECTED_LOCAL;
            sendQueue();
        }
    }

    private synchronized void connectToService() {
        if (this.forceLocalDispatch || this.client == null || this.state == ConnectState.CONNECTED_LOCAL) {
            Log.w("client not initialized.");
            useStore();
        } else {
            try {
                this.connectTries++;
                cancelTimer(this.failedConnectTimer);
                this.state = ConnectState.CONNECTING;
                this.failedConnectTimer = new Timer("Failed Connect");
                this.failedConnectTimer.schedule(new FailedConnectTask(), FAILED_CONNECT_WAIT_TIME);
                Log.v("connecting to Analytics service");
                this.client.connect();
            } catch (SecurityException e) {
                Log.w("security exception on connectToService");
                useStore();
            }
        }
	}

    private synchronized void disconnectFromService() {
        if (this.client != null && this.state == ConnectState.CONNECTED_SERVICE) {
            this.state = ConnectState.PENDING_DISCONNECT;
            this.client.disconnect();
        }
    }

    @Override
    public synchronized void onConnected() {
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.connectTries = 0;
        Log.v("Connected to service");
        this.state = ConnectState.CONNECTED_SERVICE;
        if (this.pendingServiceDisconnect) {
            disconnectFromService();
            this.pendingServiceDisconnect = false;
        } else {
            sendQueue();
            this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
            this.disconnectCheckTimer = new Timer("disconnect check");
            this.disconnectCheckTimer.schedule(new DisconnectCheckTask(), this.idleTimeout);
        }
    }

    public synchronized void onDisconnected() {
        if (this.state == ConnectState.PENDING_DISCONNECT) {
            Log.v("Disconnected from service");
            clearAllTimers();
            this.state = ConnectState.DISCONNECTED;
        } else {
            Log.v("Unexpected disconnect.");
            this.state = ConnectState.PENDING_CONNECTION;
            if (this.connectTries < 2) {
                fireReconnectAttempt();
            } else {
                useStore();
            }
        }
    }

    public synchronized void onConnectionFailed(int errorCode, Intent resolution) {
        this.state = ConnectState.PENDING_CONNECTION;
        if (this.connectTries < 2) {
            Log.w("Service unavailable (code=" + errorCode + "), will retry.");
            fireReconnectAttempt();
        } else {
            Log.w("Service unavailable (code=" + errorCode + "), using local store.");
            useStore();
        }
    }

    private void fireReconnectAttempt() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.reConnectTimer = new Timer("Service Reconnect");
        this.reConnectTimer.schedule(new ReconnectTask(), RECONNECT_WAIT_TIME);
    }

    private class FailedConnectTask extends TimerTask {
        private FailedConnectTask() {
        }

        public void run() {
            if (GAServiceProxy.this.state == ConnectState.CONNECTING) {
                GAServiceProxy.this.useStore();
            }
        }
    }

    private class ReconnectTask extends TimerTask {
        private ReconnectTask() {
        }

        public void run() {
            GAServiceProxy.this.connectToService();
        }
    }

    private class DisconnectCheckTask extends TimerTask {
        private DisconnectCheckTask() {
        }

        public void run() {
            if (GAServiceProxy.this.state != ConnectState.CONNECTED_SERVICE || !GAServiceProxy.this.queue.isEmpty() || GAServiceProxy.this.lastRequestTime + GAServiceProxy.this.idleTimeout >= GAServiceProxy.this.clock.currentTimeMillis()) {
                GAServiceProxy.this.disconnectCheckTimer.schedule(new DisconnectCheckTask(), GAServiceProxy.this.idleTimeout);
                return;
            }
            Log.v("Disconnecting due to inactivity");
            GAServiceProxy.this.disconnectFromService();
        }
    }

    private static class HitParams {
        private final List<Command> commands;
        private final long hitTimeInMilliseconds;
        private final String path;
        private final Map<String, String> wireFormatParams;

        HitParams(Map<String, String> wireFormatParams2, long hitTimeInMilliseconds2, String path2, List<Command> commands2) {
            this.wireFormatParams = wireFormatParams2;
            this.hitTimeInMilliseconds = hitTimeInMilliseconds2;
            this.path = path2;
            this.commands = commands2;
        }

        Map<String, String> getWireFormatParams() {
            return this.wireFormatParams;
        }

        long getHitTimeInMilliseconds() {
            return this.hitTimeInMilliseconds;
        }

        public String getPath() {
            return this.path;
        }

        List<Command> getCommands() {
            return this.commands;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PATH: ");
            sb.append(this.path);
            if (this.wireFormatParams != null) {
                sb.append("  PARAMS: ");
                for (Map.Entry<String, String> entry : this.wireFormatParams.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append(",  ");
                }
            }
            return sb.toString();
        }
    }
}
