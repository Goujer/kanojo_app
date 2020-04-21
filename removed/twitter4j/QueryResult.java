package twitter4j;

import java.io.Serializable;
import java.util.List;

public interface QueryResult extends TwitterResponse, Serializable {
    double getCompletedIn();

    int getCount();

    long getMaxId();

    String getQuery();

    String getRefreshURL();

    String getRefreshUrl();

    long getSinceId();

    List<Status> getTweets();

    boolean hasNext();

    Query nextQuery();
}
