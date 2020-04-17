package twitter4j;

import java.io.Serializable;

public interface Relationship extends TwitterResponse, Serializable {
    long getSourceUserId();

    String getSourceUserScreenName();

    long getTargetUserId();

    String getTargetUserScreenName();

    boolean isSourceBlockingTarget();

    boolean isSourceFollowedByTarget();

    boolean isSourceFollowingTarget();

    boolean isSourceNotificationsEnabled();

    boolean isSourceWantRetweets();

    boolean isTargetFollowedBySource();

    boolean isTargetFollowingSource();
}
