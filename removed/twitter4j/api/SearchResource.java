package twitter4j.api;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

public interface SearchResource {
    QueryResult search(Query query) throws TwitterException;
}
