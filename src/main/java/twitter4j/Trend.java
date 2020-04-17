package twitter4j;

import java.io.Serializable;

public interface Trend extends Serializable {
    String getName();

    String getQuery();

    String getURL();

    String getUrl();
}
