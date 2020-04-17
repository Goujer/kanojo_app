package twitter4j;

import java.io.Serializable;

public interface HashtagEntity extends Serializable {
    int getEnd();

    int getStart();

    String getText();
}
