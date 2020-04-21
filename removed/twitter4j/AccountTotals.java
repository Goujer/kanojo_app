package twitter4j;

import java.io.Serializable;

public interface AccountTotals extends TwitterResponse, Serializable {
    int getFavorites();

    int getFollowers();

    int getFriends();

    int getUpdates();
}
