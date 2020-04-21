package twitter4j;

import java.io.Serializable;

public interface OEmbed extends TwitterResponse, Serializable {
    String getAuthorName();

    String getAuthorURL();

    long getCacheAge();

    String getHtml();

    String getURL();

    String getVersion();

    int getWidth();
}
