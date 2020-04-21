package com.facebook.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.Session;
import com.facebook.SessionState;

public class SessionTracker {
    private final LocalBroadcastManager broadcastManager;
    /* access modifiers changed from: private */
    public final Session.StatusCallback callback;
    private boolean isTracking;
    private final BroadcastReceiver receiver;
    /* access modifiers changed from: private */
    public Session session;

    public SessionTracker(Context context, Session.StatusCallback callback2) {
        this(context, callback2, (Session) null);
    }

    SessionTracker(Context context, Session.StatusCallback callback2, Session session2) {
        this(context, callback2, session2, true);
    }

    public SessionTracker(Context context, Session.StatusCallback callback2, Session session2, boolean startTracking) {
        this.isTracking = false;
        this.callback = new CallbackWrapper(callback2);
        this.session = session2;
        this.receiver = new ActiveSessionBroadcastReceiver(this, (ActiveSessionBroadcastReceiver) null);
        this.broadcastManager = LocalBroadcastManager.getInstance(context);
        if (startTracking) {
            startTracking();
        }
    }

    public Session getSession() {
        return this.session == null ? Session.getActiveSession() : this.session;
    }

    public Session getOpenSession() {
        Session openSession = getSession();
        if (openSession == null || !openSession.isOpened()) {
            return null;
        }
        return openSession;
    }

    public void setSession(Session newSession) {
        if (newSession != null) {
            if (this.session == null) {
                Session activeSession = Session.getActiveSession();
                if (activeSession != null) {
                    activeSession.removeCallback(this.callback);
                }
                this.broadcastManager.unregisterReceiver(this.receiver);
            } else {
                this.session.removeCallback(this.callback);
            }
            this.session = newSession;
            this.session.addCallback(this.callback);
        } else if (this.session != null) {
            this.session.removeCallback(this.callback);
            this.session = null;
            addBroadcastReceiver();
            if (getSession() != null) {
                getSession().addCallback(this.callback);
            }
        }
    }

    public void startTracking() {
        if (!this.isTracking) {
            if (this.session == null) {
                addBroadcastReceiver();
            }
            if (getSession() != null) {
                getSession().addCallback(this.callback);
            }
            this.isTracking = true;
        }
    }

    public void stopTracking() {
        if (this.isTracking) {
            Session session2 = getSession();
            if (session2 != null) {
                session2.removeCallback(this.callback);
            }
            this.broadcastManager.unregisterReceiver(this.receiver);
            this.isTracking = false;
        }
    }

    public boolean isTracking() {
        return this.isTracking;
    }

    public boolean isTrackingActiveSession() {
        return this.session == null;
    }

    private void addBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Session.ACTION_ACTIVE_SESSION_SET);
        filter.addAction(Session.ACTION_ACTIVE_SESSION_UNSET);
        this.broadcastManager.registerReceiver(this.receiver, filter);
    }

    private class ActiveSessionBroadcastReceiver extends BroadcastReceiver {
        private ActiveSessionBroadcastReceiver() {
        }

        /* synthetic */ ActiveSessionBroadcastReceiver(SessionTracker sessionTracker, ActiveSessionBroadcastReceiver activeSessionBroadcastReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            Session session;
            if (Session.ACTION_ACTIVE_SESSION_SET.equals(intent.getAction()) && (session = Session.getActiveSession()) != null) {
                session.addCallback(SessionTracker.this.callback);
            }
        }
    }

    private class CallbackWrapper implements Session.StatusCallback {
        private final Session.StatusCallback wrapped;

        public CallbackWrapper(Session.StatusCallback wrapped2) {
            this.wrapped = wrapped2;
        }

        public void call(Session session, SessionState state, Exception exception) {
            if (this.wrapped != null && SessionTracker.this.isTracking()) {
                this.wrapped.call(session, state, exception);
            }
            if (session == SessionTracker.this.session && state.isClosed()) {
                SessionTracker.this.setSession((Session) null);
            }
        }
    }
}
