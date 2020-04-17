package jp.co.cybird.app.android.lib.commons.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private boolean isBinaryRequest;
    private final HttpUriRequest request;
    private final AsyncHttpResponseHandler responseHandler;

    public AsyncHttpRequest(AbstractHttpClient client2, HttpContext context2, HttpUriRequest request2, AsyncHttpResponseHandler responseHandler2) {
        this.client = client2;
        this.context = context2;
        this.request = request2;
        this.responseHandler = responseHandler2;
        if (responseHandler2 instanceof BinaryHttpResponseHandler) {
            this.isBinaryRequest = true;
        }
    }

    public void run() {
        try {
            if (this.responseHandler != null) {
                this.responseHandler.sendStartMessage();
            }
            makeRequestWithRetries();
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
            }
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.sendFinishMessage();
                if (this.isBinaryRequest) {
                    this.responseHandler.sendFailureMessage((Throwable) e, (byte[]) null);
                } else {
                    this.responseHandler.sendFailureMessage((Throwable) e, (String) null);
                }
            }
        }
    }

    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            HttpResponse response = this.client.execute(this.request, this.context);
            if (!Thread.currentThread().isInterrupted() && this.responseHandler != null) {
                this.responseHandler.sendResponseMessage(response);
            }
        }
    }

    private void makeRequestWithRetries() throws ConnectException {
        boolean retry = true;
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = this.client.getHttpRequestRetryHandler();
        while (retry) {
            try {
                makeRequest();
                return;
            } catch (UnknownHostException e) {
                if (this.responseHandler != null) {
                    this.responseHandler.sendFailureMessage((Throwable) e, "can not resolve host");
                    return;
                }
                return;
            } catch (IOException e2) {
                cause = e2;
                int i = this.executionCount + 1;
                this.executionCount = i;
                retry = retryHandler.retryRequest(cause, i, this.context);
            } catch (NullPointerException e3) {
                cause = new IOException("NullPo in HttpClient" + e3.getMessage());
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                retry = retryHandler.retryRequest(cause, i2, this.context);
            }
        }
        ConnectException ex = new ConnectException();
        ex.initCause(cause);
        throw ex;
    }
}
