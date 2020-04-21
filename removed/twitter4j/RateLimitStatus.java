package twitter4j;

import java.io.Serializable;

public interface RateLimitStatus extends Serializable {
    int getLimit();

    int getRemaining();

    int getRemainingHits();

    int getResetTimeInSeconds();

    int getSecondsUntilReset();
}
