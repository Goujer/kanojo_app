package com.facebook.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.SessionTracker;
import java.util.Date;
import java.util.List;

class FacebookFragment extends Fragment {
    private SessionTracker sessionTracker;

    FacebookFragment() {
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.sessionTracker = new SessionTracker(getActivity(), new DefaultSessionStatusCallback(this, (DefaultSessionStatusCallback) null));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.sessionTracker.getSession().onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    public void onDestroy() {
        super.onDestroy();
        this.sessionTracker.stopTracking();
    }

    public void setSession(Session newSession) {
        if (this.sessionTracker != null) {
            this.sessionTracker.setSession(newSession);
        }
    }

    /* access modifiers changed from: protected */
    public void onSessionStateChange(SessionState state, Exception exception) {
    }

    /* access modifiers changed from: protected */
    public final Session getSession() {
        if (this.sessionTracker != null) {
            return this.sessionTracker.getSession();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final boolean isSessionOpen() {
        if (this.sessionTracker == null || this.sessionTracker.getOpenSession() == null) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final SessionState getSessionState() {
        Session currentSession;
        if (this.sessionTracker == null || (currentSession = this.sessionTracker.getSession()) == null) {
            return null;
        }
        return currentSession.getState();
    }

    /* access modifiers changed from: protected */
    public final String getAccessToken() {
        Session currentSession;
        if (this.sessionTracker == null || (currentSession = this.sessionTracker.getOpenSession()) == null) {
            return null;
        }
        return currentSession.getAccessToken();
    }

    /* access modifiers changed from: protected */
    public final Date getExpirationDate() {
        Session currentSession;
        if (this.sessionTracker == null || (currentSession = this.sessionTracker.getOpenSession()) == null) {
            return null;
        }
        return currentSession.getExpirationDate();
    }

    /* access modifiers changed from: protected */
    public final void closeSession() {
        Session currentSession;
        if (this.sessionTracker != null && (currentSession = this.sessionTracker.getOpenSession()) != null) {
            currentSession.close();
        }
    }

    /* access modifiers changed from: protected */
    public final void closeSessionAndClearTokenInformation() {
        Session currentSession;
        if (this.sessionTracker != null && (currentSession = this.sessionTracker.getOpenSession()) != null) {
            currentSession.closeAndClearTokenInformation();
        }
    }

    /* access modifiers changed from: protected */
    public final List<String> getSessionPermissions() {
        Session currentSession;
        if (this.sessionTracker == null || (currentSession = this.sessionTracker.getSession()) == null) {
            return null;
        }
        return currentSession.getPermissions();
    }

    /* access modifiers changed from: protected */
    public final void openSession() {
        openSessionForRead((String) null, (List<String>) null);
    }

    /* access modifiers changed from: protected */
    public final void openSessionForRead(String applicationId, List<String> permissions) {
        openSessionForRead(applicationId, permissions, SessionLoginBehavior.SSO_WITH_FALLBACK, Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
    }

    /* access modifiers changed from: protected */
    public final void openSessionForRead(String applicationId, List<String> permissions, SessionLoginBehavior behavior, int activityCode) {
        openSession(applicationId, permissions, behavior, activityCode, SessionAuthorizationType.READ);
    }

    /* access modifiers changed from: protected */
    public final void openSessionForPublish(String applicationId, List<String> permissions) {
        openSessionForPublish(applicationId, permissions, SessionLoginBehavior.SSO_WITH_FALLBACK, Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
    }

    /* access modifiers changed from: protected */
    public final void openSessionForPublish(String applicationId, List<String> permissions, SessionLoginBehavior behavior, int activityCode) {
        openSession(applicationId, permissions, behavior, activityCode, SessionAuthorizationType.PUBLISH);
    }

    private void openSession(String applicationId, List<String> permissions, SessionLoginBehavior behavior, int activityCode, SessionAuthorizationType authType) {
        if (this.sessionTracker != null) {
            Session currentSession = this.sessionTracker.getSession();
            if (currentSession == null || currentSession.getState().isClosed()) {
                Session session = new Session.Builder(getActivity()).setApplicationId(applicationId).build();
                Session.setActiveSession(session);
                currentSession = session;
            }
            if (!currentSession.isOpened()) {
                Session.OpenRequest openRequest = new Session.OpenRequest((Fragment) this).setPermissions((List) permissions).setLoginBehavior(behavior).setRequestCode(activityCode);
                if (SessionAuthorizationType.PUBLISH.equals(authType)) {
                    currentSession.openForPublish(openRequest);
                } else {
                    currentSession.openForRead(openRequest);
                }
            }
        }
    }

    private class DefaultSessionStatusCallback implements Session.StatusCallback {
        private DefaultSessionStatusCallback() {
        }

        /* synthetic */ DefaultSessionStatusCallback(FacebookFragment facebookFragment, DefaultSessionStatusCallback defaultSessionStatusCallback) {
            this();
        }

        public void call(Session session, SessionState state, Exception exception) {
            FacebookFragment.this.onSessionStateChange(state, exception);
        }
    }
}
