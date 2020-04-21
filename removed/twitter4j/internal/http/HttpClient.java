package twitter4j.internal.http;

import twitter4j.TwitterException;

public interface HttpClient {
    HttpResponse request(HttpRequest httpRequest) throws TwitterException;

    void shutdown();
}
