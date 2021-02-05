package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

public class RemoteResourceManager extends Observable {

    private static final String TAG = "RemoteResourceManager";
    private DiskCache mDiskCache;
    private FetcherObserver mFetcherObserver;
    private RemoteResourceFetcher mRemoteResourceFetcher;

    public RemoteResourceManager(Context context) {
        this(new BaseDiskCache(context));
    }

//    public RemoteResourceManager(String cacheName, BaseDiskCache.BaseDiskCallBack listener, Context context) {
//        this(new BaseDiskCache("barcodekanojo", cacheName, listener, context));
//    }

    public RemoteResourceManager(DiskCache cache) {
        this.mFetcherObserver = new FetcherObserver(this, null);
        this.mDiskCache = cache;
        this.mRemoteResourceFetcher = new RemoteResourceFetcher(this.mDiskCache);
        this.mRemoteResourceFetcher.addObserver(this.mFetcherObserver);
    }

    public void setDiskCache(DiskCache cache) {
        this.mDiskCache = cache;
        this.mRemoteResourceFetcher.setDiskCache(this.mDiskCache);
    }

    public boolean exists(Uri uri) {
        return this.mDiskCache.exists(encode(uri));
    }

    public File getFile(Uri uri) {
        return this.mDiskCache.getFile(encode(uri));
    }

    public InputStream getInputStream(Uri uri) throws IOException {
        return this.mDiskCache.getInputStream(encode(uri));
    }

    public void request(Uri uri) {
        this.mRemoteResourceFetcher.fetch(uri, encode(uri));
    }

    public void invalidate(Uri uri) {
        this.mDiskCache.invalidate(encode(uri));
    }

    public void shutdown() {
        this.mRemoteResourceFetcher.shutdown();
        this.mDiskCache.cleanup();
    }

    public void clear() {
        this.mRemoteResourceFetcher.shutdown();
        this.mDiskCache.clear();
    }

    public static abstract class ResourceRequestObserver implements Observer {
        private Uri mRequestUri;

        public abstract void requestReceived(Observable observable, Uri uri);

        public ResourceRequestObserver(Uri requestUri) {
            this.mRequestUri = requestUri;
        }

        @Override
        public void update(Observable observable, Object data) {
            Uri dataUri = (Uri) data;
            if (dataUri == this.mRequestUri) {
                requestReceived(observable, dataUri);
            }
        }
    }

    private class FetcherObserver implements Observer {
        private FetcherObserver() {
        }

        /* synthetic */ FetcherObserver(RemoteResourceManager remoteResourceManager, FetcherObserver fetcherObserver) {
            this();
        }

        @Override
        public void update(Observable observable, Object data) {
            RemoteResourceManager.this.setChanged();
            RemoteResourceManager.this.notifyObservers(data);
        }
    }

    private String encode(Uri uri) {
        return this.mDiskCache.encode(uri);
    }
}
