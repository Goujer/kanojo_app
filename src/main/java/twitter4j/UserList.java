package twitter4j;

import java.io.Serializable;
import java.net.URI;

public interface UserList extends Comparable<UserList>, TwitterResponse, Serializable {
    String getDescription();

    String getFullName();

    int getId();

    int getMemberCount();

    String getName();

    String getSlug();

    int getSubscriberCount();

    URI getURI();

    User getUser();

    boolean isFollowing();

    boolean isPublic();
}
