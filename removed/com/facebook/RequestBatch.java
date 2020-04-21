package com.facebook;

import android.os.Handler;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestBatch extends AbstractList<Request> {
    private static AtomicInteger idGenerator = new AtomicInteger();
    private String batchApplicationId;
    private Handler callbackHandler;
    private List<Callback> callbacks;
    private final String id;
    private List<Request> requests;
    private int timeoutInMilliseconds;

    public interface Callback {
        void onBatchCompleted(RequestBatch requestBatch);
    }

    public RequestBatch() {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList();
    }

    public RequestBatch(Collection<Request> requests2) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList(requests2);
    }

    public RequestBatch(Request... requests2) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = Arrays.asList(requests2);
    }

    public RequestBatch(RequestBatch requests2) {
        this.requests = new ArrayList();
        this.timeoutInMilliseconds = 0;
        this.id = Integer.valueOf(idGenerator.incrementAndGet()).toString();
        this.callbacks = new ArrayList();
        this.requests = new ArrayList(requests2);
        this.callbackHandler = requests2.callbackHandler;
        this.timeoutInMilliseconds = requests2.timeoutInMilliseconds;
        this.callbacks = new ArrayList(requests2.callbacks);
    }

    public int getTimeout() {
        return this.timeoutInMilliseconds;
    }

    public void setTimeout(int timeoutInMilliseconds2) {
        if (timeoutInMilliseconds2 < 0) {
            throw new IllegalArgumentException("Argument timeoutInMilliseconds must be >= 0.");
        }
        this.timeoutInMilliseconds = timeoutInMilliseconds2;
    }

    public void addCallback(Callback callback) {
        if (!this.callbacks.contains(callback)) {
            this.callbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        this.callbacks.remove(callback);
    }

    public final boolean add(Request request) {
        return this.requests.add(request);
    }

    public final void add(int location, Request request) {
        this.requests.add(location, request);
    }

    public final void clear() {
        this.requests.clear();
    }

    public final Request get(int i) {
        return this.requests.get(i);
    }

    public final Request remove(int location) {
        return this.requests.remove(location);
    }

    public final Request set(int location, Request request) {
        return this.requests.set(location, request);
    }

    public final int size() {
        return this.requests.size();
    }

    /* access modifiers changed from: package-private */
    public final String getId() {
        return this.id;
    }

    /* access modifiers changed from: package-private */
    public final Handler getCallbackHandler() {
        return this.callbackHandler;
    }

    /* access modifiers changed from: package-private */
    public final void setCallbackHandler(Handler callbackHandler2) {
        this.callbackHandler = callbackHandler2;
    }

    /* access modifiers changed from: package-private */
    public final List<Request> getRequests() {
        return this.requests;
    }

    /* access modifiers changed from: package-private */
    public final List<Callback> getCallbacks() {
        return this.callbacks;
    }

    /* access modifiers changed from: package-private */
    public final String getBatchApplicationId() {
        return this.batchApplicationId;
    }

    /* access modifiers changed from: package-private */
    public final void setBatchApplicationId(String batchApplicationId2) {
        this.batchApplicationId = batchApplicationId2;
    }

    public final List<Response> executeAndWait() {
        return executeAndWaitImpl();
    }

    public final RequestAsyncTask executeAsync() {
        return executeAsyncImpl();
    }

    /* access modifiers changed from: package-private */
    public List<Response> executeAndWaitImpl() {
        return Request.executeBatchAndWait(this);
    }

    /* access modifiers changed from: package-private */
    public RequestAsyncTask executeAsyncImpl() {
        return Request.executeBatchAsync(this);
    }
}
