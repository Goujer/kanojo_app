package twitter4j.api;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface TimelinesResources {
    ResponseList<Status> getHomeTimeline() throws TwitterException;

    ResponseList<Status> getHomeTimeline(Paging paging) throws TwitterException;

    ResponseList<Status> getMentions() throws TwitterException;

    ResponseList<Status> getMentions(Paging paging) throws TwitterException;

    ResponseList<Status> getMentionsTimeline() throws TwitterException;

    ResponseList<Status> getMentionsTimeline(Paging paging) throws TwitterException;

    ResponseList<Status> getRetweetsOfMe() throws TwitterException;

    ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline() throws TwitterException;

    ResponseList<Status> getUserTimeline(long j) throws TwitterException;

    ResponseList<Status> getUserTimeline(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline(String str) throws TwitterException;

    ResponseList<Status> getUserTimeline(String str, Paging paging) throws TwitterException;

    ResponseList<Status> getUserTimeline(Paging paging) throws TwitterException;
}
