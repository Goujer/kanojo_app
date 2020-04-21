package twitter4j;

public interface CursorSupport {
    public static final long START = -1;

    long getNextCursor();

    long getPreviousCursor();

    boolean hasNext();

    boolean hasPrevious();
}
