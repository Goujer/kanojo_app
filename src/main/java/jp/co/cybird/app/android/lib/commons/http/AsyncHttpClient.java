package jp.co.cybird.app.android.lib.commons.http;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

public class AsyncHttpClient {
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static int maxConnections = 10;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    /* access modifiers changed from: private */
    public final Map<String, String> clientHeaderMap;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private ThreadPoolExecutor threadPool;

    public AsyncHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, (long) socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, 10);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, System.getProperty("http.agent"));
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        this.httpClient = new DefaultHttpClient(cm, httpParams);
        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
                }
                for (String header : AsyncHttpClient.this.clientHeaderMap.keySet()) {
                    request.addHeader(header, (String) AsyncHttpClient.this.clientHeaderMap.get(header));
                }
            }
        });
        this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                Header encoding;
                HttpEntity entity = response.getEntity();
                if (entity != null && (encoding = entity.getContentEncoding()) != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(AsyncHttpClient.ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(5));
        this.threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.requestMap = new WeakHashMap();
        this.clientHeaderMap = new HashMap();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    public void setThreadPool(ThreadPoolExecutor threadPool2) {
        this.threadPool = threadPool2;
    }

    public void setUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    public void setTimeout(int timeout) {
        HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, (long) timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public void setBasicAuth(String user, String pass) {
        setBasicAuth(user, pass, AuthScope.ANY);
    }

    public void setBasicAuth(String user, String pass, AuthScope scope) {
        this.httpClient.getCredentialsProvider().setCredentials(scope, new UsernamePasswordCredentials(user, pass));
    }

    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        List<WeakReference<Future<?>>> requestList = this.requestMap.get(context);
        if (requestList != null) {
            for (WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = (Future) requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
        this.requestMap.remove(context);
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        get((Context) null, url, (RequestParams) null, responseHandler);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        get((Context) null, url, params, responseHandler);
    }

    public void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        get(context, url, (RequestParams) null, responseHandler);
    }

    public void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        sendRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(url, params)), (String) null, responseHandler, context);
    }

    public void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, (String) null, responseHandler, context);
    }

    public void post(String url, AsyncHttpResponseHandler responseHandler) {
        post((Context) null, url, (RequestParams) null, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post((Context) null, url, params, responseHandler);
    }

    public void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post(context, url, paramsToEntity(params), (String) null, responseHandler);
    }

    public void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, responseHandler, context);
    }

    public void post(Context context, String url, Header[] headers, RequestParams params, String contentType, AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) {
            request.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, responseHandler, context);
    }

    public void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, responseHandler, context);
    }

    public void put(String url, AsyncHttpResponseHandler responseHandler) {
        put((Context) null, url, (RequestParams) null, responseHandler);
    }

    public void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        put((Context) null, url, params, responseHandler);
    }

    public void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        put(context, url, paramsToEntity(params), (String) null, responseHandler);
    }

    public void put(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, responseHandler, context);
    }

    public void put(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) {
            request.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, request, contentType, responseHandler, context);
    }

    public void delete(String url, AsyncHttpResponseHandler responseHandler) {
        delete((Context) null, url, responseHandler);
    }

    public void delete(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        sendRequest(this.httpClient, this.httpContext, new HttpDelete(url), (String) null, responseHandler, context);
    }

    public void delete(Context context, String url, Header[] headers, AsyncHttpResponseHandler responseHandler) {
        HttpDelete delete = new HttpDelete(url);
        if (headers != null) {
            delete.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, delete, (String) null, responseHandler, context);
    }

    private void sendRequest(DefaultHttpClient client, HttpContext httpContext2, HttpUriRequest uriRequest, String contentType, AsyncHttpResponseHandler responseHandler, Context context) {
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        DLog.i("sendRequest", uriRequest.getParams().toString());
        Future<?> request = this.threadPool.submit(new AsyncHttpRequest(client, httpContext2, uriRequest, responseHandler));
        if (context != null) {
            List<WeakReference<Future<?>>> requestList = this.requestMap.get(context);
            if (requestList == null) {
                requestList = new LinkedList<>();
                this.requestMap.put(context, requestList);
            }
            requestList.add(new WeakReference(request));
        }
    }

    public static String getUrlWithQueryString(String url, RequestParams params) {
        if (params == null) {
            return url;
        }
        return String.valueOf(url) + "?" + params.getParamString();
    }

    private HttpEntity paramsToEntity(RequestParams params) {
        if (params != null) {
            return params.getEntity();
        }
        return null;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        public InputStream getContent() throws IOException {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }
}
