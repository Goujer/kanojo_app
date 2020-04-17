package twitter4j.api;

import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public interface TweetsResources {
    Status destroyStatus(long j) throws TwitterException;

    OEmbed getOEmbed(OEmbedRequest oEmbedRequest) throws TwitterException;

    ResponseList<Status> getRetweets(long j) throws TwitterException;

    Status retweetStatus(long j) throws TwitterException;

    Status showStatus(long j) throws TwitterException;

    Status updateStatus(String str) throws TwitterException;

    Status updateStatus(StatusUpdate statusUpdate) throws TwitterException;
}
