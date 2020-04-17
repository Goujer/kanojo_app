package twitter4j.api;

import twitter4j.TwitterException;
import twitter4j.User;

public interface SpamReportingResource {
    User reportSpam(long j) throws TwitterException;

    User reportSpam(String str) throws TwitterException;
}
