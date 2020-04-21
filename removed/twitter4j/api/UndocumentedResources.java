package twitter4j.api;

import twitter4j.RelatedResults;
import twitter4j.TwitterException;

public interface UndocumentedResources {
    RelatedResults getRelatedResults(long j) throws TwitterException;
}
