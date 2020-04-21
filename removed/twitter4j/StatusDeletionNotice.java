package twitter4j;

import java.io.Serializable;

public interface StatusDeletionNotice extends Comparable<StatusDeletionNotice>, Serializable {
    long getStatusId();

    long getUserId();
}
