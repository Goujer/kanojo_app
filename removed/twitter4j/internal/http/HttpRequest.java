package twitter4j.internal.http;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import twitter4j.auth.Authorization;

public final class HttpRequest implements Serializable {
    private static final HttpParameter[] NULL_PARAMETERS = new HttpParameter[0];
    private static final long serialVersionUID = -3463594029098858381L;
    private final Authorization authorization;
    private final RequestMethod method;
    private final HttpParameter[] parameters;
    private Map<String, String> requestHeaders;
    private final String url;

    public HttpRequest(RequestMethod method2, String url2, HttpParameter[] parameters2, Authorization authorization2, Map<String, String> requestHeaders2) {
        this.method = method2;
        if (method2 == RequestMethod.POST || parameters2 == null || parameters2.length == 0) {
            this.url = url2;
            this.parameters = parameters2;
        } else {
            this.url = url2 + "?" + HttpParameter.encodeParameters(parameters2);
            this.parameters = NULL_PARAMETERS;
        }
        this.authorization = authorization2;
        this.requestHeaders = requestHeaders2;
    }

    public RequestMethod getMethod() {
        return this.method;
    }

    public HttpParameter[] getParameters() {
        return this.parameters;
    }

    public String getURL() {
        return this.url;
    }

    public Authorization getAuthorization() {
        return this.authorization;
    }

    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpRequest that = (HttpRequest) o;
        if (this.authorization == null ? that.authorization != null : !this.authorization.equals(that.authorization)) {
            return false;
        }
        if (!Arrays.equals(this.parameters, that.parameters)) {
            return false;
        }
        if (this.requestHeaders == null ? that.requestHeaders != null : !this.requestHeaders.equals(that.requestHeaders)) {
            return false;
        }
        if (this.method == null ? that.method != null : !this.method.equals(that.method)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(that.url)) {
                return true;
            }
        } else if (that.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 0;
        if (this.method != null) {
            result = this.method.hashCode();
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (this.url != null) {
            i = this.url.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.parameters != null) {
            i2 = Arrays.hashCode(this.parameters);
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.authorization != null) {
            i3 = this.authorization.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (this.requestHeaders != null) {
            i4 = this.requestHeaders.hashCode();
        }
        return i8 + i4;
    }

    public String toString() {
        return "HttpRequest{requestMethod=" + this.method + ", url='" + this.url + '\'' + ", postParams=" + (this.parameters == null ? null : Arrays.asList(this.parameters)) + ", authentication=" + this.authorization + ", requestHeaders=" + this.requestHeaders + '}';
    }
}
