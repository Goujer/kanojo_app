package com.google.tagmanager;

import com.google.android.gms.common.util.VisibleForTesting;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

class HttpNetworkClient implements NetworkClient {
    private HttpClient mClient;

    HttpNetworkClient() {
    }

    public InputStream getInputStream(String url) throws IOException {
        this.mClient = createNewHttpClient();
        return handleServerResponse(this.mClient, this.mClient.execute(new HttpGet(url)));
    }

    public void sendPostRequest(String url, byte[] content) throws IOException {
        HttpClient client = createNewHttpClient();
        handleServerResponse(client, client.execute(createPostRequest(url, content)));
        closeWithClient(client);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public HttpPost createPostRequest(String url, byte[] content) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new ByteArrayEntity(content));
        return httpPost;
    }

    public void close() {
        closeWithClient(this.mClient);
    }

    private void closeWithClient(HttpClient client) {
        if (client != null && client.getConnectionManager() != null) {
            client.getConnectionManager().shutdown();
        }
    }

    private InputStream handleServerResponse(HttpClient client, HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == 200) {
            Log.v("Success response");
            return response.getEntity().getContent();
        }
        String errorMessage = "Bad response: " + responseCode;
        if (responseCode == 404) {
            throw new FileNotFoundException(errorMessage);
        }
        throw new IOException(errorMessage);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public HttpClient createNewHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 20000);
        HttpConnectionParams.setSoTimeout(params, 20000);
        return new DefaultHttpClient(params);
    }
}
