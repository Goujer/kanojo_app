package twitter4j.auth;

import java.io.Serializable;
import javax.crypto.spec.SecretKeySpec;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.util.z_T4JInternalStringUtil;

abstract class OAuthToken implements Serializable {
    private static final long serialVersionUID = 3891133932519746686L;
    String[] responseStr;
    private transient SecretKeySpec secretKeySpec;
    private String token;
    private String tokenSecret;

    public OAuthToken(String token2, String tokenSecret2) {
        this.responseStr = null;
        this.token = token2;
        this.tokenSecret = tokenSecret2;
    }

    OAuthToken(HttpResponse response) throws TwitterException {
        this(response.asString());
    }

    OAuthToken(String string) {
        this.responseStr = null;
        this.responseStr = z_T4JInternalStringUtil.split(string, "&");
        this.tokenSecret = getParameter("oauth_token_secret");
        this.token = getParameter("oauth_token");
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    /* access modifiers changed from: package-private */
    public void setSecretKeySpec(SecretKeySpec secretKeySpec2) {
        this.secretKeySpec = secretKeySpec2;
    }

    /* access modifiers changed from: package-private */
    public SecretKeySpec getSecretKeySpec() {
        return this.secretKeySpec;
    }

    public String getParameter(String parameter) {
        for (String str : this.responseStr) {
            if (str.startsWith(parameter + '=')) {
                return z_T4JInternalStringUtil.split(str, "=")[1].trim();
            }
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthToken)) {
            return false;
        }
        OAuthToken that = (OAuthToken) o;
        if (!this.token.equals(that.token)) {
            return false;
        }
        if (!this.tokenSecret.equals(that.tokenSecret)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.token.hashCode() * 31) + this.tokenSecret.hashCode();
    }

    public String toString() {
        return "OAuthToken{token='" + this.token + '\'' + ", tokenSecret='" + this.tokenSecret + '\'' + ", secretKeySpec=" + this.secretKeySpec + '}';
    }
}
