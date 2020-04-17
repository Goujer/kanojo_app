package jp.co.cybird.app.android.lib.commons.http;

import android.os.Message;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
    private static String[] mAllowedContentTypes = {"image/jpeg", "image/png"};

    public BinaryHttpResponseHandler() {
    }

    public BinaryHttpResponseHandler(String[] allowedContentTypes) {
        this();
        mAllowedContentTypes = allowedContentTypes;
    }

    public void onSuccess(byte[] binaryData) {
    }

    public void onSuccess(int statusCode, byte[] binaryData) {
        onSuccess(binaryData);
    }

    public void onFailure(Throwable error, byte[] binaryData) {
        onFailure(error);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, byte[] responseBody) {
        sendMessage(obtainMessage(0, new Object[]{Integer.valueOf(statusCode), responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(int statusCode, byte[] responseBody) {
        onSuccess(statusCode, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, byte[] responseBody) {
        onFailure(e, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Object[] response = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (byte[]) response[1]);
                return;
            case 1:
                Object[] response2 = (Object[]) msg.obj;
                handleFailureMessage((Throwable) response2[0], (byte[]) response2[1]);
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void sendResponseMessage(HttpResponse response) {
        StatusLine status = response.getStatusLine();
        Header[] contentTypeHeaders = response.getHeaders("Content-Type");
        byte[] responseBody = null;
        if (contentTypeHeaders.length != 1) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), "None, or more than one, Content-Type Header found!"), responseBody);
            return;
        }
        Header contentTypeHeader = contentTypeHeaders[0];
        boolean foundAllowedContentType = false;
        for (String anAllowedContentType : mAllowedContentTypes) {
            if (anAllowedContentType.equals(contentTypeHeader.getValue())) {
                foundAllowedContentType = true;
            }
        }
        if (!foundAllowedContentType) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), "Content-Type not allowed!"), responseBody);
            return;
        }
        HttpEntity entity = null;
        try {
            HttpEntity temp = response.getEntity();
            if (temp != null) {
                entity = new BufferedHttpEntity(temp);
            }
            responseBody = EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            sendFailureMessage(e, (byte[]) null);
        }
        if (status.getStatusCode() >= 300) {
            sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
        } else {
            sendSuccessMessage(status.getStatusCode(), responseBody);
        }
    }
}
