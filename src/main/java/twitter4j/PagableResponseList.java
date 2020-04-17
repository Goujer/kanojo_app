package twitter4j;

import twitter4j.TwitterResponse;

public interface PagableResponseList<T extends TwitterResponse> extends ResponseList<T>, CursorSupport {
    long getNextCursor();

    long getPreviousCursor();

    boolean hasNext();

    boolean hasPrevious();
}
