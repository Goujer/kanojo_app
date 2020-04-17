package com.facebook;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

@TargetApi(3)
public class RequestAsyncTask extends AsyncTask<Void, Void, List<Response>> {
    private static final String TAG = RequestAsyncTask.class.getCanonicalName();
    private static Method executeOnExecutorMethod;
    private final HttpURLConnection connection;
    private Exception exception;
    private final RequestBatch requests;

    static {
        for (Method method : AsyncTask.class.getMethods()) {
            if ("executeOnExecutor".equals(method.getName())) {
                Class[] parameters = method.getParameterTypes();
                if (parameters.length == 2 && parameters[0] == Executor.class && parameters[1].isArray()) {
                    executeOnExecutorMethod = method;
                    return;
                }
            }
        }
    }

    public RequestAsyncTask(Request... requests2) {
        this((HttpURLConnection) null, new RequestBatch(requests2));
    }

    public RequestAsyncTask(Collection<Request> requests2) {
        this((HttpURLConnection) null, new RequestBatch(requests2));
    }

    public RequestAsyncTask(RequestBatch requests2) {
        this((HttpURLConnection) null, requests2);
    }

    public RequestAsyncTask(HttpURLConnection connection2, Request... requests2) {
        this(connection2, new RequestBatch(requests2));
    }

    public RequestAsyncTask(HttpURLConnection connection2, Collection<Request> requests2) {
        this(connection2, new RequestBatch(requests2));
    }

    public RequestAsyncTask(HttpURLConnection connection2, RequestBatch requests2) {
        this.requests = requests2;
        this.connection = connection2;
    }

    /* access modifiers changed from: protected */
    public final Exception getException() {
        return this.exception;
    }

    /* access modifiers changed from: protected */
    public final RequestBatch getRequests() {
        return this.requests;
    }

    public String toString() {
        return "{RequestAsyncTask: " + " connection: " + this.connection + ", requests: " + this.requests + "}";
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
        if (this.requests.getCallbackHandler() == null) {
            this.requests.setCallbackHandler(new Handler());
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(List<Response> result) {
        super.onPostExecute(result);
        if (this.exception != null) {
            Log.d(TAG, String.format("onPostExecute: exception encountered during request: %s", new Object[]{this.exception.getMessage()}));
        }
    }

    /* access modifiers changed from: protected */
    public List<Response> doInBackground(Void... params) {
        try {
            if (this.connection == null) {
                return this.requests.executeAndWait();
            }
            return Request.executeConnectionAndWait(this.connection, this.requests);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public RequestAsyncTask executeOnSettingsExecutor() {
        try {
            if (executeOnExecutorMethod != null) {
                executeOnExecutorMethod.invoke(this, new Object[]{Settings.getExecutor(), null});
                return this;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
        }
        execute(new Void[0]);
        return this;
    }
}
