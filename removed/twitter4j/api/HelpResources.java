package twitter4j.api;

import java.util.Map;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;

public interface HelpResources {

    public interface Language {
        String getCode();

        String getName();

        String getStatus();
    }

    TwitterAPIConfiguration getAPIConfiguration() throws TwitterException;

    ResponseList<Language> getLanguages() throws TwitterException;

    String getPrivacyPolicy() throws TwitterException;

    Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException;

    Map<String, RateLimitStatus> getRateLimitStatus(String... strArr) throws TwitterException;

    String getTermsOfService() throws TwitterException;
}
