package twitter4j;

import java.io.Serializable;

public interface TimeZone extends Serializable {
    String getName();

    String tzinfoName();

    int utcOffset();
}
