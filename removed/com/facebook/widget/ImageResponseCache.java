package com.facebook.widget;

import android.content.Context;
import com.facebook.LoggingBehavior;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class ImageResponseCache {
    static final String TAG = ImageResponseCache.class.getSimpleName();
    private static volatile FileLruCache imageCache;

    ImageResponseCache() {
    }

    static synchronized FileLruCache getCache(Context context) throws IOException {
        FileLruCache fileLruCache;
        synchronized (ImageResponseCache.class) {
            if (imageCache == null) {
                imageCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());
            }
            fileLruCache = imageCache;
        }
        return fileLruCache;
    }

    static InputStream getCachedImageStream(URL url, Context context) {
        if (url == null || !isCDNURL(url)) {
            return null;
        }
        try {
            return getCache(context).get(url.toString());
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, e.toString());
            return null;
        }
    }

    static InputStream interceptAndCacheImageStream(Context context, HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != 200) {
            return null;
        }
        URL url = connection.getURL();
        InputStream stream = connection.getInputStream();
        if (!isCDNURL(url)) {
            return stream;
        }
        try {
            return getCache(context).interceptAndPut(url.toString(), new BufferedHttpInputStream(stream, connection));
        } catch (IOException e) {
            return stream;
        }
    }

    private static boolean isCDNURL(URL url) {
        if (url != null) {
            String uriHost = url.getHost();
            if (uriHost.endsWith("fbcdn.net")) {
                return true;
            }
            if (!uriHost.startsWith("fbcdn") || !uriHost.endsWith("akamaihd.net")) {
                return false;
            }
            return true;
        }
        return false;
    }

    private static class BufferedHttpInputStream extends BufferedInputStream {
        HttpURLConnection connection;

        BufferedHttpInputStream(InputStream stream, HttpURLConnection connection2) {
            super(stream, 8192);
            this.connection = connection2;
        }

        public void close() throws IOException {
            super.close();
            Utility.disconnectQuietly(this.connection);
        }
    }
}
