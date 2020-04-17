package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpResponse;

public final class RequestToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8214365845469757952L;
    private final Configuration conf = ConfigurationContext.getInstance();
    private OAuthSupport oauth;

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ String getParameter(String x0) {
        return super.getParameter(x0);
    }

    public /* bridge */ /* synthetic */ String getToken() {
        return super.getToken();
    }

    public /* bridge */ /* synthetic */ String getTokenSecret() {
        return super.getTokenSecret();
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    RequestToken(HttpResponse res, OAuthSupport oauth2) throws TwitterException {
        super(res);
        this.oauth = oauth2;
    }

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
    }

    RequestToken(String token, String tokenSecret, OAuthSupport oauth2) {
        super(token, tokenSecret);
        this.oauth = oauth2;
    }

    public String getAuthorizationURL() {
        return this.conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
    }

    public String getAuthenticationURL() {
        return this.conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
    }
}
