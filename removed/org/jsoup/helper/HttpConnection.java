package org.jsoup.helper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;

public class HttpConnection implements Connection {
    private Connection.Request req = new Request();
    private Connection.Response res = new Response();

    public static Connection connect(String url) {
        Connection con = new HttpConnection();
        con.url(url);
        return con;
    }

    public static Connection connect(URL url) {
        Connection con = new HttpConnection();
        con.url(url);
        return con;
    }

    private HttpConnection() {
    }

    public Connection url(URL url) {
        this.req.url(url);
        return this;
    }

    public Connection url(String url) {
        Validate.notEmpty(url, "Must supply a valid URL");
        try {
            this.req.url(new URL(url));
            return this;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + url, e);
        }
    }

    public Connection userAgent(String userAgent) {
        Validate.notNull(userAgent, "User agent must not be null");
        this.req.header(DownloadHelper.REQUEST_HEADER_USERAGENT, userAgent);
        return this;
    }

    public Connection timeout(int millis) {
        this.req.timeout(millis);
        return this;
    }

    public Connection maxBodySize(int bytes) {
        this.req.maxBodySize(bytes);
        return this;
    }

    public Connection followRedirects(boolean followRedirects) {
        this.req.followRedirects(followRedirects);
        return this;
    }

    public Connection referrer(String referrer) {
        Validate.notNull(referrer, "Referrer must not be null");
        this.req.header("Referer", referrer);
        return this;
    }

    public Connection method(Connection.Method method) {
        this.req.method(method);
        return this;
    }

    public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
        this.req.ignoreHttpErrors(ignoreHttpErrors);
        return this;
    }

    public Connection ignoreContentType(boolean ignoreContentType) {
        this.req.ignoreContentType(ignoreContentType);
        return this;
    }

    public Connection data(String key, String value) {
        this.req.data(KeyVal.create(key, value));
        return this;
    }

    public Connection data(Map<String, String> data) {
        Validate.notNull(data, "Data map must not be null");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            this.req.data(KeyVal.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public Connection data(String... keyvals) {
        Validate.notNull(keyvals, "Data key value pairs must not be null");
        Validate.isTrue(keyvals.length % 2 == 0, "Must supply an even number of key value pairs");
        for (int i = 0; i < keyvals.length; i += 2) {
            String key = keyvals[i];
            String value = keyvals[i + 1];
            Validate.notEmpty(key, "Data key must not be empty");
            Validate.notNull(value, "Data value must not be null");
            this.req.data(KeyVal.create(key, value));
        }
        return this;
    }

    public Connection header(String name, String value) {
        this.req.header(name, value);
        return this;
    }

    public Connection cookie(String name, String value) {
        this.req.cookie(name, value);
        return this;
    }

    public Connection cookies(Map<String, String> cookies) {
        Validate.notNull(cookies, "Cookie map must not be null");
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            this.req.cookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Connection parser(Parser parser) {
        this.req.parser(parser);
        return this;
    }

    public Document get() throws IOException {
        this.req.method(Connection.Method.GET);
        execute();
        return this.res.parse();
    }

    public Document post() throws IOException {
        this.req.method(Connection.Method.POST);
        execute();
        return this.res.parse();
    }

    public Connection.Response execute() throws IOException {
        this.res = Response.execute(this.req);
        return this.res;
    }

    public Connection.Request request() {
        return this.req;
    }

    public Connection request(Connection.Request request) {
        this.req = request;
        return this;
    }

    public Connection.Response response() {
        return this.res;
    }

    public Connection response(Connection.Response response) {
        this.res = response;
        return this;
    }

    private static abstract class Base<T extends Connection.Base> implements Connection.Base<T> {
        Map<String, String> cookies;
        Map<String, String> headers;
        Connection.Method method;
        URL url;

        private Base() {
            this.headers = new LinkedHashMap();
            this.cookies = new LinkedHashMap();
        }

        public URL url() {
            return this.url;
        }

        public T url(URL url2) {
            Validate.notNull(url2, "URL must not be null");
            this.url = url2;
            return this;
        }

        public Connection.Method method() {
            return this.method;
        }

        public T method(Connection.Method method2) {
            Validate.notNull(method2, "Method must not be null");
            this.method = method2;
            return this;
        }

        public String header(String name) {
            Validate.notNull(name, "Header name must not be null");
            return getHeaderCaseInsensitive(name);
        }

        public T header(String name, String value) {
            Validate.notEmpty(name, "Header name must not be empty");
            Validate.notNull(value, "Header value must not be null");
            removeHeader(name);
            this.headers.put(name, value);
            return this;
        }

        public boolean hasHeader(String name) {
            Validate.notEmpty(name, "Header name must not be empty");
            return getHeaderCaseInsensitive(name) != null;
        }

        public T removeHeader(String name) {
            Validate.notEmpty(name, "Header name must not be empty");
            Map.Entry<String, String> entry = scanHeaders(name);
            if (entry != null) {
                this.headers.remove(entry.getKey());
            }
            return this;
        }

        public Map<String, String> headers() {
            return this.headers;
        }

        private String getHeaderCaseInsensitive(String name) {
            Map.Entry<String, String> entry;
            Validate.notNull(name, "Header name must not be null");
            String value = this.headers.get(name);
            if (value == null) {
                value = this.headers.get(name.toLowerCase());
            }
            if (value != null || (entry = scanHeaders(name)) == null) {
                return value;
            }
            return entry.getValue();
        }

        private Map.Entry<String, String> scanHeaders(String name) {
            String lc = name.toLowerCase();
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                if (entry.getKey().toLowerCase().equals(lc)) {
                    return entry;
                }
            }
            return null;
        }

        public String cookie(String name) {
            Validate.notNull(name, "Cookie name must not be null");
            return this.cookies.get(name);
        }

        public T cookie(String name, String value) {
            Validate.notEmpty(name, "Cookie name must not be empty");
            Validate.notNull(value, "Cookie value must not be null");
            this.cookies.put(name, value);
            return this;
        }

        public boolean hasCookie(String name) {
            Validate.notEmpty("Cookie name must not be empty");
            return this.cookies.containsKey(name);
        }

        public T removeCookie(String name) {
            Validate.notEmpty("Cookie name must not be empty");
            this.cookies.remove(name);
            return this;
        }

        public Map<String, String> cookies() {
            return this.cookies;
        }
    }

    public static class Request extends Base<Connection.Request> implements Connection.Request {
        private Collection<Connection.KeyVal> data;
        private boolean followRedirects;
        private boolean ignoreContentType;
        private boolean ignoreHttpErrors;
        private int maxBodySizeBytes;
        private Parser parser;
        private int timeoutMilliseconds;

        public /* bridge */ /* synthetic */ String cookie(String x0) {
            return super.cookie(x0);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String x0) {
            return super.hasCookie(x0);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String x0) {
            return super.hasHeader(x0);
        }

        public /* bridge */ /* synthetic */ String header(String x0) {
            return super.header(x0);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Connection.Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        private Request() {
            super();
            this.ignoreHttpErrors = false;
            this.ignoreContentType = false;
            this.timeoutMilliseconds = 3000;
            this.maxBodySizeBytes = 1048576;
            this.followRedirects = true;
            this.data = new ArrayList();
            this.method = Connection.Method.GET;
            this.headers.put("Accept-Encoding", "gzip");
            this.parser = Parser.htmlParser();
        }

        public int timeout() {
            return this.timeoutMilliseconds;
        }

        public Request timeout(int millis) {
            Validate.isTrue(millis >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
            this.timeoutMilliseconds = millis;
            return this;
        }

        public int maxBodySize() {
            return this.maxBodySizeBytes;
        }

        public Connection.Request maxBodySize(int bytes) {
            Validate.isTrue(bytes >= 0, "maxSize must be 0 (unlimited) or larger");
            this.maxBodySizeBytes = bytes;
            return this;
        }

        public boolean followRedirects() {
            return this.followRedirects;
        }

        public Connection.Request followRedirects(boolean followRedirects2) {
            this.followRedirects = followRedirects2;
            return this;
        }

        public boolean ignoreHttpErrors() {
            return this.ignoreHttpErrors;
        }

        public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors2) {
            this.ignoreHttpErrors = ignoreHttpErrors2;
            return this;
        }

        public boolean ignoreContentType() {
            return this.ignoreContentType;
        }

        public Connection.Request ignoreContentType(boolean ignoreContentType2) {
            this.ignoreContentType = ignoreContentType2;
            return this;
        }

        public Request data(Connection.KeyVal keyval) {
            Validate.notNull(keyval, "Key val must not be null");
            this.data.add(keyval);
            return this;
        }

        public Collection<Connection.KeyVal> data() {
            return this.data;
        }

        public Request parser(Parser parser2) {
            this.parser = parser2;
            return this;
        }

        public Parser parser() {
            return this.parser;
        }
    }

    public static class Response extends Base<Connection.Response> implements Connection.Response {
        private static final int MAX_REDIRECTS = 20;
        private ByteBuffer byteData;
        private String charset;
        private String contentType;
        private boolean executed = false;
        private int numRedirects = 0;
        private Connection.Request req;
        private int statusCode;
        private String statusMessage;

        public /* bridge */ /* synthetic */ String cookie(String x0) {
            return super.cookie(x0);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String x0) {
            return super.hasCookie(x0);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String x0) {
            return super.hasHeader(x0);
        }

        public /* bridge */ /* synthetic */ String header(String x0) {
            return super.header(x0);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Connection.Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        Response() {
            super();
        }

        private Response(Response previousResponse) throws IOException {
            super();
            if (previousResponse != null) {
                this.numRedirects = previousResponse.numRedirects + 1;
                if (this.numRedirects >= 20) {
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", new Object[]{previousResponse.url()}));
                }
            }
        }

        static Response execute(Connection.Request req2) throws IOException {
            return execute(req2, (Response) null);
        }

        static Response execute(Connection.Request req2, Response previousResponse) throws IOException {
            InputStream bodyStream;
            InputStream dataStream;
            Validate.notNull(req2, "Request must not be null");
            String protocol = req2.url().getProtocol();
            if (protocol.equals("http") || protocol.equals("https")) {
                if (req2.method() == Connection.Method.GET && req2.data().size() > 0) {
                    serialiseRequestUrl(req2);
                }
                HttpURLConnection conn = createConnection(req2);
                try {
                    conn.connect();
                    if (req2.method() == Connection.Method.POST) {
                        writePost(req2.data(), conn.getOutputStream());
                    }
                    int status = conn.getResponseCode();
                    boolean needsRedirect = false;
                    if (status != 200) {
                        if (status == 302 || status == 301 || status == 303) {
                            needsRedirect = true;
                        } else if (!req2.ignoreHttpErrors()) {
                            throw new HttpStatusException("HTTP error fetching URL", status, req2.url().toString());
                        }
                    }
                    Response res = new Response(previousResponse);
                    res.setupFromConnection(conn, previousResponse);
                    if (!needsRedirect || !req2.followRedirects()) {
                        res.req = req2;
                        String contentType2 = res.contentType();
                        if (contentType2 == null || req2.ignoreContentType() || contentType2.startsWith("text/") || contentType2.startsWith("application/xml") || contentType2.startsWith("application/xhtml+xml")) {
                            bodyStream = null;
                            dataStream = null;
                            InputStream dataStream2 = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
                            InputStream bodyStream2 = (!res.hasHeader("Content-Encoding") || !res.header("Content-Encoding").equalsIgnoreCase("gzip")) ? new BufferedInputStream(dataStream2) : new BufferedInputStream(new GZIPInputStream(dataStream2));
                            res.byteData = DataUtil.readToByteBuffer(bodyStream2, req2.maxBodySize());
                            res.charset = DataUtil.getCharsetFromContentType(res.contentType);
                            if (bodyStream2 != null) {
                                bodyStream2.close();
                            }
                            if (dataStream2 != null) {
                                dataStream2.close();
                            }
                            conn.disconnect();
                            res.executed = true;
                        } else {
                            throw new UnsupportedMimeTypeException("Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml", contentType2, req2.url().toString());
                        }
                    } else {
                        req2.method(Connection.Method.GET);
                        req2.data().clear();
                        req2.url(new URL(req2.url(), res.header("Location")));
                        for (Map.Entry<String, String> cookie : res.cookies.entrySet()) {
                            req2.cookie(cookie.getKey(), cookie.getValue());
                        }
                        res = execute(req2, res);
                        conn.disconnect();
                    }
                    return res;
                } catch (Throwable th) {
                    conn.disconnect();
                    throw th;
                }
            } else {
                throw new MalformedURLException("Only http & https protocols supported");
            }
        }

        public int statusCode() {
            return this.statusCode;
        }

        public String statusMessage() {
            return this.statusMessage;
        }

        public String charset() {
            return this.charset;
        }

        public String contentType() {
            return this.contentType;
        }

        public Document parse() throws IOException {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
            Document doc = DataUtil.parseByteData(this.byteData, this.charset, this.url.toExternalForm(), this.req.parser());
            this.byteData.rewind();
            this.charset = doc.outputSettings().charset().name();
            return doc;
        }

        public String body() {
            String body;
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            if (this.charset == null) {
                body = Charset.forName("UTF-8").decode(this.byteData).toString();
            } else {
                body = Charset.forName(this.charset).decode(this.byteData).toString();
            }
            this.byteData.rewind();
            return body;
        }

        public byte[] bodyAsBytes() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            return this.byteData.array();
        }

        private static HttpURLConnection createConnection(Connection.Request req2) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) req2.url().openConnection();
            conn.setRequestMethod(req2.method().name());
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(req2.timeout());
            conn.setReadTimeout(req2.timeout());
            if (req2.method() == Connection.Method.POST) {
                conn.setDoOutput(true);
            }
            if (req2.cookies().size() > 0) {
                conn.addRequestProperty("Cookie", getRequestCookieString(req2));
            }
            for (Map.Entry<String, String> header : req2.headers().entrySet()) {
                conn.addRequestProperty(header.getKey(), header.getValue());
            }
            return conn;
        }

        private void setupFromConnection(HttpURLConnection conn, Connection.Response previousResponse) throws IOException {
            this.method = Connection.Method.valueOf(conn.getRequestMethod());
            this.url = conn.getURL();
            this.statusCode = conn.getResponseCode();
            this.statusMessage = conn.getResponseMessage();
            this.contentType = conn.getContentType();
            processResponseHeaders(conn.getHeaderFields());
            if (previousResponse != null) {
                for (Map.Entry<String, String> prevCookie : previousResponse.cookies().entrySet()) {
                    if (!hasCookie(prevCookie.getKey())) {
                        cookie(prevCookie.getKey(), prevCookie.getValue());
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void processResponseHeaders(Map<String, List<String>> resHeaders) {
            for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
                String name = entry.getKey();
                if (name != null) {
                    List<String> values = entry.getValue();
                    if (name.equalsIgnoreCase("Set-Cookie")) {
                        for (String value : values) {
                            if (value != null) {
                                TokenQueue cd = new TokenQueue(value);
                                String cookieName = cd.chompTo("=").trim();
                                String cookieVal = cd.consumeTo(";").trim();
                                if (cookieVal == null) {
                                    cookieVal = "";
                                }
                                if (cookieName != null && cookieName.length() > 0) {
                                    cookie(cookieName, cookieVal);
                                }
                            }
                        }
                    } else if (!values.isEmpty()) {
                        header(name, values.get(0));
                    }
                }
            }
        }

        private static void writePost(Collection<Connection.KeyVal> data, OutputStream outputStream) throws IOException {
            OutputStreamWriter w = new OutputStreamWriter(outputStream, "UTF-8");
            boolean first = true;
            for (Connection.KeyVal keyVal : data) {
                if (!first) {
                    w.append('&');
                } else {
                    first = false;
                }
                w.write(URLEncoder.encode(keyVal.key(), "UTF-8"));
                w.write(61);
                w.write(URLEncoder.encode(keyVal.value(), "UTF-8"));
            }
            w.close();
        }

        private static String getRequestCookieString(Connection.Request req2) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> cookie : req2.cookies().entrySet()) {
                if (!first) {
                    sb.append("; ");
                } else {
                    first = false;
                }
                sb.append(cookie.getKey()).append('=').append(cookie.getValue());
            }
            return sb.toString();
        }

        private static void serialiseRequestUrl(Connection.Request req2) throws IOException {
            URL in = req2.url();
            StringBuilder url = new StringBuilder();
            boolean first = true;
            url.append(in.getProtocol()).append("://").append(in.getAuthority()).append(in.getPath()).append("?");
            if (in.getQuery() != null) {
                url.append(in.getQuery());
                first = false;
            }
            for (Connection.KeyVal keyVal : req2.data()) {
                if (!first) {
                    url.append('&');
                } else {
                    first = false;
                }
                url.append(URLEncoder.encode(keyVal.key(), "UTF-8")).append('=').append(URLEncoder.encode(keyVal.value(), "UTF-8"));
            }
            req2.url(new URL(url.toString()));
            req2.data().clear();
        }
    }

    public static class KeyVal implements Connection.KeyVal {
        private String key;
        private String value;

        public static KeyVal create(String key2, String value2) {
            Validate.notEmpty(key2, "Data key must not be empty");
            Validate.notNull(value2, "Data value must not be null");
            return new KeyVal(key2, value2);
        }

        private KeyVal(String key2, String value2) {
            this.key = key2;
            this.value = value2;
        }

        public KeyVal key(String key2) {
            Validate.notEmpty(key2, "Data key must not be empty");
            this.key = key2;
            return this;
        }

        public String key() {
            return this.key;
        }

        public KeyVal value(String value2) {
            Validate.notNull(value2, "Data value must not be null");
            this.value = value2;
            return this;
        }

        public String value() {
            return this.value;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
