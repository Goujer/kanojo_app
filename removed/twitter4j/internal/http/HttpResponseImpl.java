package twitter4j.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class HttpResponseImpl extends HttpResponse {
    private HttpURLConnection con;

    HttpResponseImpl(HttpURLConnection con2, HttpClientConfiguration conf) throws IOException {
        super(conf);
        this.con = con2;
        this.statusCode = con2.getResponseCode();
        InputStream errorStream = con2.getErrorStream();
        this.is = errorStream;
        if (errorStream == null) {
            this.is = con2.getInputStream();
        }
        if (this.is != null && "gzip".equals(con2.getContentEncoding())) {
            this.is = new StreamingGZIPInputStream(this.is);
        }
    }

    HttpResponseImpl(String content) {
        this.responseAsString = content;
    }

    public String getResponseHeader(String name) {
        return this.con.getHeaderField(name);
    }

    public Map<String, List<String>> getResponseHeaderFields() {
        return this.con.getHeaderFields();
    }

    public void disconnect() {
        this.con.disconnect();
    }
}
