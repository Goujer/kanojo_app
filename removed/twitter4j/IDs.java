package twitter4j;

import java.io.Serializable;

public interface IDs extends TwitterResponse, CursorSupport, Serializable {
    long[] getIDs();

    long getNextCursor();

    long getPreviousCursor();

    boolean hasNext();

    boolean hasPrevious();
}
