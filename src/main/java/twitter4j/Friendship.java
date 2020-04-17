package twitter4j;

import java.io.Serializable;

public interface Friendship extends Serializable {
    long getId();

    String getName();

    String getScreenName();

    boolean isFollowedBy();

    boolean isFollowing();
}
