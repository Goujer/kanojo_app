package twitter4j.auth;

import java.io.Serializable;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;

public class AccessToken extends OAuthToken implements Serializable {
    private static final long serialVersionUID = -8344528374458826291L;
    private String screenName;
    private long userId;

    public /* bridge */ /* synthetic */ String getParameter(String x0) {
        return super.getParameter(x0);
    }

    public /* bridge */ /* synthetic */ String getToken() {
        return super.getToken();
    }

    public /* bridge */ /* synthetic */ String getTokenSecret() {
        return super.getTokenSecret();
    }

    AccessToken(HttpResponse res) throws TwitterException {
        this(res.asString());
    }

    AccessToken(String str) {
        super(str);
        this.userId = -1;
        this.screenName = getParameter("screen_name");
        String sUserId = getParameter("user_id");
        if (sUserId != null) {
            this.userId = Long.parseLong(sUserId);
        }
    }

    public AccessToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        this.userId = -1;
        int dashIndex = token.indexOf("-");
        if (dashIndex != -1) {
            try {
                this.userId = Long.parseLong(token.substring(0, dashIndex));
            } catch (NumberFormatException e) {
            }
        }
    }

    public AccessToken(String token, String tokenSecret, long userId2) {
        super(token, tokenSecret);
        this.userId = -1;
        this.userId = userId2;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public long getUserId() {
        return this.userId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AccessToken that = (AccessToken) o;
        if (this.userId != that.userId) {
            return false;
        }
        if (this.screenName != null) {
            if (this.screenName.equals(that.screenName)) {
                return true;
            }
        } else if (that.screenName == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((super.hashCode() * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }

    public String toString() {
        return "AccessToken{screenName='" + this.screenName + '\'' + ", userId=" + this.userId + '}';
    }
}
