package jp.co.cybird.app.android.lib.commons.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

public class AsyncHttpResponseHandler {
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int START_MESSAGE = 2;
    protected static final int SUCCESS_MESSAGE = 0;
    private Handler handler;

    public AsyncHttpResponseHandler() {
        if (Looper.myLooper() != null) {
            this.handler = new Handler() {
                public void handleMessage(Message msg) {
                    AsyncHttpResponseHandler.this.handleMessage(msg);
                }
            };
        }
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    public void onSuccess(String content) {
    }

    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    public void onFailure(Throwable error) {
    }

    public void onFailure(Throwable error, String content) {
        onFailure(error);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, String responseBody) {
        sendMessage(obtainMessage(0, new Object[]{new Integer(statusCode), responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendStartMessage() {
        sendMessage(obtainMessage(2, (Object) null));
    }

    /* access modifiers changed from: protected */
    public void sendFinishMessage() {
        sendMessage(obtainMessage(3, (Object) null));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(int statusCode, String responseBody) {
        onSuccess(statusCode, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, String responseBody) {
        onFailure(e, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Object[] response = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (String) response[1]);
                return;
            case 1:
                Object[] response2 = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response2[0], (String) response2[1]);
                return;
            case 2:
                onStart();
                return;
            case 3:
                onFinish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void sendMessage(Message msg) {
        if (this.handler != null) {
            this.handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    /* access modifiers changed from: protected */
    public Message obtainMessage(int responseMessage, Object response) {
        if (this.handler != null) {
            return this.handler.obtainMessage(responseMessage, response);
        }
        Message msg = new Message();
        msg.what = responseMessage;
        msg.obj = response;
        return msg;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x001f  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0036  */
    public void sendResponseMessage(HttpResponse response) {
        StatusLine status = response.getStatusLine();
        String responseBody = null;
        try {
            HttpEntity temp = response.getEntity();
            if (temp != null) {
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(temp);
                try {
                    responseBody = EntityUtils.toString(bufferedHttpEntity, "UTF-8");
                } catch (IOException e) {
                    e = e;
                    BufferedHttpEntity bufferedHttpEntity2 = bufferedHttpEntity;
                }
            }
        } catch (IOException e2) {
            sendFailureMessage(e2, (String) null);
            if (status.getStatusCode() < 300) {
            }
        }
        if (status.getStatusCode() < 300) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
        } else {
            sendSuccessMessage(status.getStatusCode(), responseBody);
        }
    }
}
