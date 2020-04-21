package twitter4j.internal.http;

import java.io.Serializable;
import twitter4j.auth.Authorization;
import twitter4j.auth.BasicAuthorization;

public class XAuthAuthorization implements Authorization, Serializable {
    private static final long serialVersionUID = -6082451214083464902L;
    private BasicAuthorization basic;
    private String consumerKey;
    private String consumerSecret;

    public XAuthAuthorization(BasicAuthorization basic2) {
        this.basic = basic2;
    }

    public String getAuthorizationHeader(HttpRequest req) {
        return this.basic.getAuthorizationHeader(req);
    }

    public String getUserId() {
        return this.basic.getUserId();
    }

    public String getPassword() {
        return this.basic.getPassword();
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public synchronized void setOAuthConsumer(String consumerKey2, String consumerSecret2) {
        this.consumerKey = consumerKey2;
        this.consumerSecret = consumerSecret2;
    }

    public boolean isEnabled() {
        return this.basic.isEnabled();
    }
}
