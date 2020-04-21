package com.facebook;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.Session;

public class UiLifecycleHelper {
    private static final String ACTIVITY_NULL_MESSAGE = "activity cannot be null";
    private final Activity activity;
    private final LocalBroadcastManager broadcastManager;
    /* access modifiers changed from: private */
    public final Session.StatusCallback callback;
    private final BroadcastReceiver receiver;

    public UiLifecycleHelper(Activity activity2, Session.StatusCallback callback2) {
        if (activity2 == null) {
            throw new IllegalArgumentException(ACTIVITY_NULL_MESSAGE);
        }
        this.activity = activity2;
        this.callback = callback2;
        this.receiver = new ActiveSessionBroadcastReceiver(this, (ActiveSessionBroadcastReceiver) null);
        this.broadcastManager = LocalBroadcastManager.getInstance(activity2);
    }

    public void onCreate(Bundle savedInstanceState) {
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this.activity, (TokenCachingStrategy) null, this.callback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this.activity);
            }
            Session.setActiveSession(session);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Session.ACTION_ACTIVE_SESSION_SET);
        filter.addAction(Session.ACTION_ACTIVE_SESSION_UNSET);
        this.broadcastManager.registerReceiver(this.receiver, filter);
    }

    public void onResume() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (this.callback != null) {
                session.addCallback(this.callback);
            }
            if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())) {
                session.openForRead((Session.OpenRequest) null);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Session session = Session.getActiveSession();
        if (session != null) {
            session.onActivityResult(this.activity, requestCode, resultCode, data);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Session.saveSession(Session.getActiveSession(), outState);
    }

    public void onPause() {
        Session session;
        if (this.callback != null && (session = Session.getActiveSession()) != null) {
            session.removeCallback(this.callback);
        }
    }

    public void onDestroy() {
        this.broadcastManager.unregisterReceiver(this.receiver);
    }

    private class ActiveSessionBroadcastReceiver extends BroadcastReceiver {
        private ActiveSessionBroadcastReceiver() {
        }

        /* synthetic */ ActiveSessionBroadcastReceiver(UiLifecycleHelper uiLifecycleHelper, ActiveSessionBroadcastReceiver activeSessionBroadcastReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            Session session;
            if (Session.ACTION_ACTIVE_SESSION_SET.equals(intent.getAction())) {
                Session session2 = Session.getActiveSession();
                if (session2 != null && UiLifecycleHelper.this.callback != null) {
                    session2.addCallback(UiLifecycleHelper.this.callback);
                }
            } else if (Session.ACTION_ACTIVE_SESSION_UNSET.equals(intent.getAction()) && (session = Session.getActiveSession()) != null && UiLifecycleHelper.this.callback != null) {
                session.removeCallback(UiLifecycleHelper.this.callback);
            }
        }
    }
}
