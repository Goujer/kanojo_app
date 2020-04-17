package twitter4j.auth;

public interface AuthorizationConfiguration {
    String getOAuthAccessToken();

    String getOAuthAccessTokenSecret();

    String getOAuthConsumerKey();

    String getOAuthConsumerSecret();

    String getPassword();

    String getUser();
}
