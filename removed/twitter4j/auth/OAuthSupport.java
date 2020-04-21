package twitter4j.auth;

import twitter4j.TwitterException;

public interface OAuthSupport {
    AccessToken getOAuthAccessToken() throws TwitterException;

    AccessToken getOAuthAccessToken(String str) throws TwitterException;

    AccessToken getOAuthAccessToken(String str, String str2) throws TwitterException;

    AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException;

    AccessToken getOAuthAccessToken(RequestToken requestToken, String str) throws TwitterException;

    RequestToken getOAuthRequestToken() throws TwitterException;

    RequestToken getOAuthRequestToken(String str) throws TwitterException;

    RequestToken getOAuthRequestToken(String str, String str2) throws TwitterException;

    void setOAuthAccessToken(AccessToken accessToken);

    void setOAuthConsumer(String str, String str2);
}
