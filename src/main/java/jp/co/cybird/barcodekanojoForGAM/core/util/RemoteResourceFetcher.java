package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

class RemoteResourceFetcher extends Observable {
    public static final String TAG = "RemoteResourceFetcher";
    private ConcurrentHashMap<Request, Callable<Request>> mActiveRequestsMap = new ConcurrentHashMap<>();
    private ExecutorService mExecutor;
    private HttpClient mHttpClient;
    private DiskCache mResourceCache;

    public RemoteResourceFetcher(DiskCache cache) {
        this.mResourceCache = cache;
        this.mHttpClient = createHttpClient();
        this.mExecutor = Executors.newCachedThreadPool();
    }

    public void setDiskCache(DiskCache cache) {
        this.mResourceCache = cache;
    }

    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }

    public Future<Request> fetch(Uri uri, String hash) {
        Request request = new Request(uri, hash);
        synchronized (this.mActiveRequestsMap) {
            Callable newRequestCall = newRequestCall(request);
            if (this.mActiveRequestsMap.putIfAbsent(request, newRequestCall) != null) {
                return null;
            }
            Future<Request> submit = this.mExecutor.submit(newRequestCall);
            return submit;
        }
    }

    public void shutdown() {
        this.mExecutor.shutdownNow();
    }

    private Callable<Request> newRequestCall(final Request request) {
        return new Callable<Request>() {
            public Request call() {
                try {
                    HttpGet httpGet = new HttpGet(request.uri.toString());
                    httpGet.addHeader("Accept-Encoding", "gzip");
                    httpGet.addHeader(DownloadHelper.REQUEST_HEADER_USERAGENT, Defs.USER_AGENT());
                    HttpResponse response = RemoteResourceFetcher.this.mHttpClient.execute(httpGet);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        RemoteResourceFetcher.this.mResourceCache.store(request.hash, RemoteResourceFetcher.getUngzippedContent(response.getEntity()));
                    }
                } catch (IOException e) {
                } finally {
                    RemoteResourceFetcher.this.mActiveRequestsMap.remove(request);
                    RemoteResourceFetcher.this.notifyObservers(request.uri);
                }
                return request;
            }
        };
    }

    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null) {
            return responseStream;
        }
        Header header = entity.getContentEncoding();
        if (header == null) {
            return responseStream;
        }
        String contentEncoding = header.getValue();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains("gzip")) {
            responseStream = new GZIPInputStream(responseStream);
        }
        return responseStream;
    }

    public static final DefaultHttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        return new DefaultHttpClient(new ThreadSafeClientConnManager(params, supportedSchemes), params);
    }

    private static class Request {
        String hash;
        Uri uri;

        public Request(Uri requestUri, String requestHash) {
            this.uri = requestUri;
            this.hash = requestHash;
        }

        public int hashCode() {
            return this.hash.hashCode();
        }
    }
}
