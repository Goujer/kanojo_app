package twitter4j;

import java.io.Serializable;
import java.util.Date;

public interface Status extends Comparable<Status>, TwitterResponse, EntitySupport, Serializable {
    long[] getContributors();

    Date getCreatedAt();

    long getCurrentUserRetweetId();

    GeoLocation getGeoLocation();

    long getId();

    String getInReplyToScreenName();

    long getInReplyToStatusId();

    long getInReplyToUserId();

    Place getPlace();

    long getRetweetCount();

    Status getRetweetedStatus();

    String getSource();

    String getText();

    User getUser();

    boolean isFavorited();

    boolean isPossiblySensitive();

    boolean isRetweet();

    boolean isRetweetedByMe();

    boolean isTruncated();
}
