package net.nend.android;

import android.os.AsyncTask;
import java.io.IOException;
import java.lang.ref.WeakReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

final class DownloadTask<T> extends AsyncTask<Void, Void, T> {
    /* access modifiers changed from: private */
    public final WeakReference<Downloadable<T>> mReference;

    interface Downloadable<T> {
        String getRequestUrl();

        T makeResponse(HttpEntity httpEntity);

        void onDownload(T t);
    }

    DownloadTask(Downloadable<T> downloadable) {
        this.mReference = new WeakReference<>(downloadable);
    }

    /* access modifiers changed from: protected */
    public T doInBackground(Void... params) {
        T t = null;
        Thread.currentThread().setPriority(10);
        if (!isCancelled()) {
            Downloadable<T> downloadable = (Downloadable) this.mReference.get();
            if (downloadable == null || downloadable.getRequestUrl() == null || downloadable.getRequestUrl().length() <= 0) {
                NendLog.w(NendStatus.ERR_INVALID_URL);
            } else {
                final String requestUrl = downloadable.getRequestUrl();
                NendLog.v("Download from " + requestUrl);
                DefaultHttpClient client = new DefaultHttpClient();
                try {
                    HttpParams httpParams = client.getParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                    HttpConnectionParams.setSoTimeout(httpParams, 10000);
                    NendLog.d("start request!");
                    t = client.execute(new HttpGet(requestUrl), new ResponseHandler<T>() {
                        public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                            Downloadable<T> downloadable;
                            NendLog.d("get response!");
                            if (!DownloadTask.this.isCancelled() && response.getStatusLine().getStatusCode() == 200 && (downloadable = (Downloadable) DownloadTask.this.mReference.get()) != null) {
                                return downloadable.makeResponse(response.getEntity());
                            }
                            if (!DownloadTask.this.isCancelled()) {
                                NendLog.w(NendStatus.ERR_HTTP_REQUEST, "http status : " + response.getStatusLine().getStatusCode());
                            }
                            return null;
                        }
                    });
                } catch (ClientProtocolException e) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, (Throwable) e);
                } catch (IOException e2) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, (Throwable) e2);
                } catch (IllegalStateException e3) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, (Throwable) e3);
                } catch (IllegalArgumentException e4) {
                    NendLog.w(NendStatus.ERR_HTTP_REQUEST, (Throwable) e4);
                } finally {
                    client.getConnectionManager().shutdown();
                }
            }
        }
        return t;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(T response) {
        Downloadable<T> downloadable = (Downloadable) this.mReference.get();
        if (!isCancelled() && downloadable != null) {
            downloadable.onDownload(response);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isFinished() {
        return getStatus() == AsyncTask.Status.FINISHED;
    }
}
