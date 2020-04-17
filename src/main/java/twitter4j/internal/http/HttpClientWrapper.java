package twitter4j.internal.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import twitter4j.TwitterException;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationContext;

public final class HttpClientWrapper implements Serializable {
    private static final long serialVersionUID = -6511977105603119379L;
    private HttpClient http;
    private HttpResponseListener httpResponseListener;
    private final Map<String, String> requestHeaders;
    private final HttpClientWrapperConfiguration wrapperConf;

    public HttpClientWrapper(HttpClientWrapperConfiguration wrapperConf2) {
        this.wrapperConf = wrapperConf2;
        this.requestHeaders = wrapperConf2.getRequestHeaders();
        this.http = HttpClientFactory.getInstance(wrapperConf2);
    }

    public HttpClientWrapper() {
        this.wrapperConf = ConfigurationContext.getInstance();
        this.requestHeaders = this.wrapperConf.getRequestHeaders();
        this.http = HttpClientFactory.getInstance(this.wrapperConf);
    }

    public void shutdown() {
        this.http.shutdown();
    }

    private HttpResponse request(HttpRequest req) throws TwitterException {
        try {
            HttpResponse res = this.http.request(req);
            if (this.httpResponseListener != null) {
                this.httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, res, (TwitterException) null));
            }
            return res;
        } catch (TwitterException te) {
            if (this.httpResponseListener != null) {
                this.httpResponseListener.httpResponseReceived(new HttpResponseEvent(req, (HttpResponse) null, te));
            }
            throw te;
        }
    }

    public void setHttpResponseListener(HttpResponseListener listener) {
        this.httpResponseListener = listener;
    }

    public HttpResponse get(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, parameters, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse get(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, (HttpParameter[]) null, authorization, this.requestHeaders));
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, (HttpParameter[]) null, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, parameters, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse post(String url, HttpParameter[] parameters, Map<String, String> requestHeaders2) throws TwitterException {
        Map<String, String> headers = new HashMap<>(this.requestHeaders);
        if (requestHeaders2 != null) {
            headers.putAll(requestHeaders2);
        }
        return request(new HttpRequest(RequestMethod.POST, url, parameters, (Authorization) null, headers));
    }

    public HttpResponse post(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, (HttpParameter[]) null, authorization, this.requestHeaders));
    }

    public HttpResponse post(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, (HttpParameter[]) null, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, parameters, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse delete(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, (HttpParameter[]) null, authorization, this.requestHeaders));
    }

    public HttpResponse delete(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.DELETE, url, (HttpParameter[]) null, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, parameters, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse head(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, (HttpParameter[]) null, authorization, this.requestHeaders));
    }

    public HttpResponse head(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.HEAD, url, (HttpParameter[]) null, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url, HttpParameter[] parameters) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, parameters, (Authorization) null, this.requestHeaders));
    }

    public HttpResponse put(String url, Authorization authorization) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, (HttpParameter[]) null, authorization, this.requestHeaders));
    }

    public HttpResponse put(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.PUT, url, (HttpParameter[]) null, (Authorization) null, this.requestHeaders));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientWrapper that = (HttpClientWrapper) o;
        if (!this.http.equals(that.http)) {
            return false;
        }
        if (!this.requestHeaders.equals(that.requestHeaders)) {
            return false;
        }
        if (!this.wrapperConf.equals(that.wrapperConf)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((this.wrapperConf.hashCode() * 31) + this.http.hashCode()) * 31) + this.requestHeaders.hashCode();
    }

    public String toString() {
        return "HttpClientWrapper{wrapperConf=" + this.wrapperConf + ", http=" + this.http + ", requestHeaders=" + this.requestHeaders + ", httpResponseListener=" + this.httpResponseListener + '}';
    }
}
