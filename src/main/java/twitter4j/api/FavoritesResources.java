package twitter4j.api;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface FavoritesResources {
    Status createFavorite(long j) throws TwitterException;

    Status destroyFavorite(long j) throws TwitterException;

    ResponseList<Status> getFavorites() throws TwitterException;

    ResponseList<Status> getFavorites(long j) throws TwitterException;

    ResponseList<Status> getFavorites(long j, Paging paging) throws TwitterException;

    ResponseList<Status> getFavorites(String str) throws TwitterException;

    ResponseList<Status> getFavorites(String str, Paging paging) throws TwitterException;

    ResponseList<Status> getFavorites(Paging paging) throws TwitterException;
}
