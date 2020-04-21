package twitter4j.internal.http;

import java.util.Map;

public interface HttpClientWrapperConfiguration extends HttpClientConfiguration {
    Map<String, String> getRequestHeaders();
}
