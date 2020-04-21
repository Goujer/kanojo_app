package com.google.tagmanager;

import com.google.android.gms.common.util.VisibleForTesting;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;

class HttpUrlConnectionNetworkClient implements NetworkClient {
    private HttpURLConnection mConnection;

    HttpUrlConnectionNetworkClient() {
    }

    public InputStream getInputStream(String urlStr) throws IOException {
        this.mConnection = getUrlConnection(urlStr);
        return handleServerResponse(this.mConnection);
    }

    public void sendPostRequest(String urlStr, byte[] content) throws IOException {
        OutputStream sendToServer;
        HttpURLConnection connection = getUrlConnection(urlStr);
        try {
            connection.setRequestProperty("Content-Length", Integer.toString(content.length));
            connection.setRequestMethod(DownloadHelper.REQUEST_METHOD_POST);
            connection.setDoOutput(true);
            connection.connect();
            sendToServer = connection.getOutputStream();
            sendToServer.write(content);
            sendToServer.flush();
            sendToServer.close();
            handleServerResponse(connection);
            closeURLConnection(connection);
        } catch (Throwable th) {
            closeURLConnection(connection);
            throw th;
        }
    }

    private InputStream handleServerResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            return connection.getInputStream();
        }
        String errorMessage = "Bad response: " + responseCode;
        if (responseCode == 404) {
            throw new FileNotFoundException(errorMessage);
        }
        throw new IOException(errorMessage);
    }

    public void close() {
        closeURLConnection(this.mConnection);
    }

    private void closeURLConnection(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public HttpURLConnection getUrlConnection(String urlStr) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);
        return connection;
    }
}
