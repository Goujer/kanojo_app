package twitter4j.auth;

import twitter4j.conf.Configuration;

public final class AuthorizationFactory {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: twitter4j.auth.BasicAuthorization} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: twitter4j.auth.BasicAuthorization} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: twitter4j.auth.BasicAuthorization} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: twitter4j.auth.OAuthAuthorization} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: twitter4j.auth.BasicAuthorization} */
    /* JADX WARNING: Multi-variable type inference failed */
    public static Authorization getInstance(Configuration conf) {
        Authorization auth = null;
        String consumerKey = conf.getOAuthConsumerKey();
        String consumerSecret = conf.getOAuthConsumerSecret();
        if (consumerKey == null || consumerSecret == null) {
            String screenName = conf.getUser();
            String password = conf.getPassword();
            if (!(screenName == null || password == null)) {
                auth = new BasicAuthorization(screenName, password);
            }
        } else {
            OAuthAuthorization oauth = new OAuthAuthorization(conf);
            String accessToken = conf.getOAuthAccessToken();
            String accessTokenSecret = conf.getOAuthAccessTokenSecret();
            if (!(accessToken == null || accessTokenSecret == null)) {
                oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
            }
            auth = oauth;
        }
        if (auth == null) {
            return NullAuthorization.getInstance();
        }
        return auth;
    }
}
