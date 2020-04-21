package twitter4j;

import java.util.List;

public interface ResponseList<T> extends TwitterResponse, List<T> {
    RateLimitStatus getRateLimitStatus();
}
