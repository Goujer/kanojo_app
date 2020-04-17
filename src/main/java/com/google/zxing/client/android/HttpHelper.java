package com.google.zxing.client.android;

import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import twitter4j.internal.http.HttpResponseCode;

public final class HttpHelper {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType;
    private static final Collection<String> REDIRECTOR_DOMAINS = new HashSet(Arrays.asList(new String[]{"amzn.to", "bit.ly", "bitly.com", "fb.me", "goo.gl", "is.gd", "j.mp", "lnkd.in", "ow.ly", "R.BEETAGG.COM", "r.beetagg.com", "SCN.BY", "su.pr", "t.co", "tinyurl.com", "tr.im"}));
    private static final String TAG = HttpHelper.class.getSimpleName();

    public enum ContentType {
        HTML,
        JSON,
        XML,
        TEXT
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType() {
        int[] iArr = $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType;
        if (iArr == null) {
            iArr = new int[ContentType.values().length];
            try {
                iArr[ContentType.HTML.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ContentType.JSON.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ContentType.TEXT.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ContentType.XML.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType = iArr;
        }
        return iArr;
    }

    private HttpHelper() {
    }

    public static CharSequence downloadViaHttp(String uri, ContentType type) throws IOException {
        return downloadViaHttp(uri, type, Integer.MAX_VALUE);
    }

    public static CharSequence downloadViaHttp(String uri, ContentType type, int maxChars) throws IOException {
        String contentTypes;
        switch ($SWITCH_TABLE$com$google$zxing$client$android$HttpHelper$ContentType()[type.ordinal()]) {
            case 1:
                contentTypes = "application/xhtml+xml,text/html,text/*,*/*";
                break;
            case 2:
                contentTypes = "application/json,text/*,*/*";
                break;
            case 3:
                contentTypes = "application/xml,text/*,*/*";
                break;
            default:
                contentTypes = "text/*,*/*";
                break;
        }
        return downloadViaHttp(uri, contentTypes, maxChars);
    }

    private static CharSequence downloadViaHttp(String uri, String contentTypes, int maxChars) throws IOException {
        int redirects = 0;
        while (redirects < 5) {
            HttpURLConnection connection = safelyOpenConnection(new URL(uri));
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept", contentTypes);
            connection.setRequestProperty("Accept-Charset", "utf-8,*");
            connection.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, "ZXing (Android)");
            try {
                int responseCode = safelyConnect(uri, connection);
                switch (responseCode) {
                    case 200:
                        return consume(connection, maxChars);
                    case HttpResponseCode.FOUND /*302*/:
                        String location = connection.getHeaderField("Location");
                        if (location != null) {
                            uri = location;
                            redirects++;
                            connection.disconnect();
                        } else {
                            throw new IOException("No Location");
                        }
                    default:
                        throw new IOException("Bad HTTP response: " + responseCode);
                }
            } finally {
                connection.disconnect();
            }
            connection.disconnect();
        }
        throw new IOException("Too many redirects");
    }

    private static String getEncoding(URLConnection connection) {
        int charsetStart;
        String contentTypeHeader = connection.getHeaderField("Content-Type");
        if (contentTypeHeader == null || (charsetStart = contentTypeHeader.indexOf("charset=")) < 0) {
            return "UTF-8";
        }
        return contentTypeHeader.substring("charset=".length() + charsetStart);
    }

    private static CharSequence consume(URLConnection connection, int maxChars) throws IOException {
        int charsRead;
        String encoding = getEncoding(connection);
        StringBuilder out = new StringBuilder();
        Reader in = null;
        try {
            Reader in2 = new InputStreamReader(connection.getInputStream(), encoding);
            try {
                char[] buffer = new char[1024];
                while (out.length() < maxChars && (charsRead = in2.read(buffer)) > 0) {
                    out.append(buffer, 0, charsRead);
                }
                if (in2 != null) {
                    try {
                        in2.close();
                    } catch (IOException | NullPointerException e) {
                    }
                }
                return out;
            } catch (Throwable th) {
                th = th;
                in = in2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (in != null) {
                try {
                    in.close();
                } catch (IOException | NullPointerException e2) {
                }
            }
            throw th;
        }
    }

    public static URI unredirect(URI uri) throws IOException {
        if (!REDIRECTOR_DOMAINS.contains(uri.getHost())) {
            return uri;
        }
        HttpURLConnection connection = safelyOpenConnection(uri.toURL());
        connection.setInstanceFollowRedirects(false);
        connection.setDoInput(false);
        connection.setRequestMethod("HEAD");
        connection.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, "ZXing (Android)");
        try {
            switch (safelyConnect(uri.toString(), connection)) {
                case 300:
                case 301:
                case HttpResponseCode.FOUND /*302*/:
                case 303:
                case 307:
                    String location = connection.getHeaderField("Location");
                    if (location != null) {
                        try {
                            URI uri2 = new URI(location);
                            connection.disconnect();
                            return uri2;
                        } catch (URISyntaxException e) {
                            break;
                        }
                    }
                    break;
            }
            return uri;
        } finally {
            connection.disconnect();
        }
    }

    private static HttpURLConnection safelyOpenConnection(URL url) throws IOException {
        try {
            URLConnection conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                return (HttpURLConnection) conn;
            }
            throw new IOException();
        } catch (NullPointerException npe) {
            Log.w(TAG, "Bad URI? " + url);
            throw new IOException(npe.toString());
        }
    }

    private static int safelyConnect(String uri, HttpURLConnection connection) throws IOException {
        try {
            connection.connect();
            try {
                return connection.getResponseCode();
            } catch (NullPointerException npe) {
                Log.w(TAG, "Bad URI? " + uri);
                throw new IOException(npe.toString());
            } catch (IllegalArgumentException iae) {
                Log.w(TAG, "Bad server status? " + uri);
                throw new IOException(iae.toString());
            }
        } catch (NullPointerException npe2) {
            Log.w(TAG, "Bad URI? " + uri);
            throw new IOException(npe2.toString());
        } catch (IllegalArgumentException iae2) {
            Log.w(TAG, "Bad URI? " + uri);
            throw new IOException(iae2.toString());
        } catch (SecurityException se) {
            Log.w(TAG, "Restricted URI? " + uri);
            throw new IOException(se.toString());
        } catch (IndexOutOfBoundsException ioobe) {
            Log.w(TAG, "Bad URI? " + uri);
            throw new IOException(ioobe.toString());
        }
    }
}
