package twitter4j;

public interface RateLimitStatusListener {
    void onRateLimitReached(RateLimitStatusEvent rateLimitStatusEvent);

    void onRateLimitStatus(RateLimitStatusEvent rateLimitStatusEvent);
}
