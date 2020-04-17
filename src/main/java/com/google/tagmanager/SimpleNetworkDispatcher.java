package com.google.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

class SimpleNetworkDispatcher implements Dispatcher {
    private static final String USER_AGENT_TEMPLATE = "%s/%s (Linux; U; Android %s; %s; %s Build/%s)";
    private final Context ctx;
    private DispatchListener dispatchListener;
    private final HttpClient httpClient;
    private final String userAgent = createUserAgentString("GoogleTagManager", "3.01", Build.VERSION.RELEASE, getUserAgentLanguage(Locale.getDefault()), Build.MODEL, Build.ID);

    public interface DispatchListener {
        void onHitDispatched(Hit hit);

        void onHitPermanentDispatchFailure(Hit hit);

        void onHitTransientDispatchFailure(Hit hit);
    }

    @VisibleForTesting
    SimpleNetworkDispatcher(HttpClient httpClient2, Context ctx2, DispatchListener dispatchListener2) {
        this.ctx = ctx2.getApplicationContext();
        this.httpClient = httpClient2;
        this.dispatchListener = dispatchListener2;
    }

    public boolean okToDispatch() {
        NetworkInfo network = ((ConnectivityManager) this.ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            return true;
        }
        Log.v("...no network connectivity");
        return false;
    }

    public void dispatchHits(List<Hit> hits) {
        int maxHits = Math.min(hits.size(), 40);
        boolean firstSend = true;
        for (int i = 0; i < maxHits; i++) {
            Hit hit = hits.get(i);
            URL url = getUrl(hit);
            if (url == null) {
                Log.w("No destination: discarding hit.");
                this.dispatchListener.onHitPermanentDispatchFailure(hit);
            } else {
                HttpEntityEnclosingRequest request = constructGtmRequest(url);
                if (request == null) {
                    this.dispatchListener.onHitPermanentDispatchFailure(hit);
                } else {
                    HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
                    request.addHeader("Host", targetHost.toHostString());
                    logDebugInformation(request);
                    if (firstSend) {
                        try {
                            NetworkReceiver.sendRadioPoweredBroadcast(this.ctx);
                            firstSend = false;
                        } catch (ClientProtocolException e) {
                            Log.w("ClientProtocolException sending hit; discarding hit...");
                            this.dispatchListener.onHitPermanentDispatchFailure(hit);
                        } catch (IOException e2) {
                            Log.w("Exception sending hit: " + e2.getClass().getSimpleName());
                            Log.w(e2.getMessage());
                            this.dispatchListener.onHitTransientDispatchFailure(hit);
                        }
                    }
                    HttpResponse response = this.httpClient.execute(targetHost, request);
                    int statusCode = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        entity.consumeContent();
                    }
                    if (statusCode != 200) {
                        Log.w("Bad response: " + response.getStatusLine().getStatusCode());
                        this.dispatchListener.onHitTransientDispatchFailure(hit);
                    } else {
                        this.dispatchListener.onHitDispatched(hit);
                    }
                }
            }
        }
    }

    public void close() {
        this.httpClient.getConnectionManager().shutdown();
    }

    private HttpEntityEnclosingRequest constructGtmRequest(URL url) {
        HttpEntityEnclosingRequest request = null;
        try {
            HttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest(DownloadHelper.REQUEST_METHOD_GET, url.toURI().toString());
            try {
                request2.addHeader(DownloadHelper.REQUEST_HEADER_USERAGENT, this.userAgent);
                return request2;
            } catch (URISyntaxException e) {
                e = e;
                request = request2;
                Log.w("Exception sending hit: " + e.getClass().getSimpleName());
                Log.w(e.getMessage());
                return request;
            }
        } catch (URISyntaxException e2) {
            e = e2;
            Log.w("Exception sending hit: " + e.getClass().getSimpleName());
            Log.w(e.getMessage());
            return request;
        }
    }

    private void logDebugInformation(HttpEntityEnclosingRequest request) {
        int avail;
        StringBuffer httpHeaders = new StringBuffer();
        for (Header header : request.getAllHeaders()) {
            httpHeaders.append(header.toString()).append("\n");
        }
        httpHeaders.append(request.getRequestLine().toString()).append("\n");
        if (request.getEntity() != null) {
            try {
                InputStream is = request.getEntity().getContent();
                if (is != null && (avail = is.available()) > 0) {
                    byte[] b = new byte[avail];
                    is.read(b);
                    httpHeaders.append("POST:\n");
                    httpHeaders.append(new String(b)).append("\n");
                }
            } catch (IOException e) {
                Log.v("Error Writing hit to log...");
            }
        }
        Log.v(httpHeaders.toString());
    }

    /* access modifiers changed from: package-private */
    public String createUserAgentString(String product, String version, String release, String language, String model, String id) {
        return String.format(USER_AGENT_TEMPLATE, new Object[]{product, version, release, language, model, id});
    }

    static String getUserAgentLanguage(Locale locale) {
        if (locale == null || locale.getLanguage() == null || locale.getLanguage().length() == 0) {
            return null;
        }
        StringBuilder lang = new StringBuilder();
        lang.append(locale.getLanguage().toLowerCase());
        if (!(locale.getCountry() == null || locale.getCountry().length() == 0)) {
            lang.append("-").append(locale.getCountry().toLowerCase());
        }
        return lang.toString();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public URL getUrl(Hit hit) {
        try {
            return new URL(hit.getHitUrl());
        } catch (MalformedURLException e) {
            Log.e("Error trying to parse the GTM url.");
            return null;
        }
    }
}
