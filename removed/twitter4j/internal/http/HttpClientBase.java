package twitter4j.internal.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import twitter4j.internal.logging.Logger;

public abstract class HttpClientBase implements HttpClient, Serializable {
    private static final Logger logger = Logger.getLogger(HttpClientBase.class);
    private static final long serialVersionUID = 6944924907755685265L;
    protected final HttpClientConfiguration CONF;

    public HttpClientBase(HttpClientConfiguration conf) {
        this.CONF = conf;
    }

    public void shutdown() {
    }

    /* access modifiers changed from: protected */
    public boolean isProxyConfigured() {
        return this.CONF.getHttpProxyHost() != null && !this.CONF.getHttpProxyHost().equals("");
    }

    public void write(DataOutputStream out, String outStr) throws IOException {
        out.writeBytes(outStr);
        logger.debug(outStr);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpClientBase)) {
            return false;
        }
        if (!this.CONF.equals(((HttpClientBase) o).CONF)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.CONF.hashCode();
    }

    public String toString() {
        return "HttpClientBase{CONF=" + this.CONF + '}';
    }
}
