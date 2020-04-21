package com.facebook.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.Loader;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.internal.CacheableRequestBatch;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;

class GraphObjectPagingLoader<T extends GraphObject> extends Loader<SimpleGraphObjectCursor<T>> {
    private boolean appendResults = false;
    private Request currentRequest;
    private SimpleGraphObjectCursor<T> cursor;
    private final Class<T> graphObjectClass;
    private boolean loading = false;
    private Request nextRequest;
    private OnErrorListener onErrorListener;
    private Request originalRequest;
    private boolean skipRoundtripIfCached;

    public interface OnErrorListener {
        void onError(FacebookException facebookException, GraphObjectPagingLoader<?> graphObjectPagingLoader);
    }

    interface PagedResults extends GraphObject {
        GraphObjectList<GraphObject> getData();
    }

    public GraphObjectPagingLoader(Context context, Class<T> graphObjectClass2) {
        super(context);
        this.graphObjectClass = graphObjectClass2;
    }

    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.onErrorListener = listener;
    }

    public SimpleGraphObjectCursor<T> getCursor() {
        return this.cursor;
    }

    public void clearResults() {
        this.nextRequest = null;
        this.originalRequest = null;
        this.currentRequest = null;
        deliverResult((SimpleGraphObjectCursor) null);
    }

    public boolean isLoading() {
        return this.loading;
    }

    public void startLoading(Request request, boolean skipRoundtripIfCached2) {
        this.originalRequest = request;
        startLoading(request, skipRoundtripIfCached2, 0);
    }

    public void refreshOriginalRequest(long afterDelay) {
        if (this.originalRequest == null) {
            throw new FacebookException("refreshOriginalRequest may not be called until after startLoading has been called.");
        }
        startLoading(this.originalRequest, false, afterDelay);
    }

    public void followNextLink() {
        if (this.nextRequest != null) {
            this.appendResults = true;
            this.currentRequest = this.nextRequest;
            this.currentRequest.setCallback(new Request.Callback() {
                public void onCompleted(Response response) {
                    GraphObjectPagingLoader.this.requestCompleted(response);
                }
            });
            this.loading = true;
            Request.executeBatchAsync((RequestBatch) putRequestIntoBatch(this.currentRequest, this.skipRoundtripIfCached));
        }
    }

    public void deliverResult(SimpleGraphObjectCursor<T> cursor2) {
        SimpleGraphObjectCursor<T> oldCursor = this.cursor;
        this.cursor = cursor2;
        if (isStarted()) {
            super.deliverResult(cursor2);
            if (oldCursor != null && oldCursor != cursor2 && !oldCursor.isClosed()) {
                oldCursor.close();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStartLoading() {
        super.onStartLoading();
        if (this.cursor != null) {
            deliverResult(this.cursor);
        }
    }

    private void startLoading(Request request, boolean skipRoundtripIfCached2, long afterDelay) {
        this.skipRoundtripIfCached = skipRoundtripIfCached2;
        this.appendResults = false;
        this.nextRequest = null;
        this.currentRequest = request;
        this.currentRequest.setCallback(new Request.Callback() {
            public void onCompleted(Response response) {
                GraphObjectPagingLoader.this.requestCompleted(response);
            }
        });
        this.loading = true;
        final RequestBatch batch = putRequestIntoBatch(request, skipRoundtripIfCached2);
        Runnable r = new Runnable() {
            public void run() {
                Request.executeBatchAsync(batch);
            }
        };
        if (afterDelay == 0) {
            r.run();
        } else {
            new Handler().postDelayed(r, afterDelay);
        }
    }

    private CacheableRequestBatch putRequestIntoBatch(Request request, boolean skipRoundtripIfCached2) {
        boolean z = false;
        CacheableRequestBatch batch = new CacheableRequestBatch(request);
        if (!skipRoundtripIfCached2) {
            z = true;
        }
        batch.setForceRoundTrip(z);
        return batch;
    }

    /* access modifiers changed from: private */
    public void requestCompleted(Response response) {
        if (response.getRequest() == this.currentRequest) {
            this.loading = false;
            this.currentRequest = null;
            FacebookRequestError requestError = response.getError();
            FacebookException exception = requestError == null ? null : requestError.getException();
            if (response.getGraphObject() == null && exception == null) {
                exception = new FacebookException("GraphObjectPagingLoader received neither a result nor an error.");
            }
            if (exception != null) {
                this.nextRequest = null;
                if (this.onErrorListener != null) {
                    this.onErrorListener.onError(exception, this);
                    return;
                }
                return;
            }
            addResults(response);
        }
    }

    private void addResults(Response response) {
        SimpleGraphObjectCursor<T> cursorToModify;
        boolean haveData;
        if (this.cursor == null || !this.appendResults) {
            cursorToModify = new SimpleGraphObjectCursor<>();
        } else {
            cursorToModify = new SimpleGraphObjectCursor<>(this.cursor);
        }
        boolean fromCache = response.getIsFromCache();
        GraphObjectList<U> castToListOf = ((PagedResults) response.getGraphObjectAs(PagedResults.class)).getData().castToListOf(this.graphObjectClass);
        if (castToListOf.size() > 0) {
            haveData = true;
        } else {
            haveData = false;
        }
        if (haveData) {
            this.nextRequest = response.getRequestForPagedResults(Response.PagingDirection.NEXT);
            cursorToModify.addGraphObjects(castToListOf, fromCache);
            cursorToModify.setMoreObjectsAvailable(true);
        }
        if (!haveData) {
            cursorToModify.setMoreObjectsAvailable(false);
            cursorToModify.setFromCache(fromCache);
            this.nextRequest = null;
        }
        if (!fromCache) {
            this.skipRoundtripIfCached = false;
        }
        deliverResult(cursorToModify);
    }
}
