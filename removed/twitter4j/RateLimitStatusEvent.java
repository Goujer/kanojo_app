package twitter4j;

import java.util.EventObject;

public final class RateLimitStatusEvent extends EventObject {
    private static final long serialVersionUID = -2332507741769177298L;
    private boolean isAccountRateLimitStatus;
    private RateLimitStatus rateLimitStatus;

    RateLimitStatusEvent(Object source, RateLimitStatus rateLimitStatus2, boolean isAccountRateLimitStatus2) {
        super(source);
        this.rateLimitStatus = rateLimitStatus2;
        this.isAccountRateLimitStatus = isAccountRateLimitStatus2;
    }

    public RateLimitStatus getRateLimitStatus() {
        return this.rateLimitStatus;
    }

    public boolean isAccountRateLimitStatus() {
        return this.isAccountRateLimitStatus;
    }

    public boolean isIPRateLimitStatus() {
        return !this.isAccountRateLimitStatus;
    }
}
