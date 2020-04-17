package twitter4j.internal.http;

public interface HttpResponseListener {
    void httpResponseReceived(HttpResponseEvent httpResponseEvent);
}
