package twitter4j;

import java.io.Serializable;

public interface URLEntity extends Serializable {
    String getDisplayURL();

    int getEnd();

    String getExpandedURL();

    int getStart();

    String getURL();
}
