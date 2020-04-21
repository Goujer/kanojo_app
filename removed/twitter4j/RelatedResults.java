package twitter4j;

import java.io.Serializable;

public interface RelatedResults extends TwitterResponse, Serializable {
    ResponseList<Status> getTweetsFromUser();

    ResponseList<Status> getTweetsWithConversation();

    ResponseList<Status> getTweetsWithReply();
}
