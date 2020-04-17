package twitter4j;

import java.io.Serializable;
import java.util.Date;

public interface Trends extends TwitterResponse, Comparable<Trends>, Serializable {
    Date getAsOf();

    Location getLocation();

    Date getTrendAt();

    Trend[] getTrends();
}
