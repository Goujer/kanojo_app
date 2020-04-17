package twitter4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.BasicAuthorization;
import twitter4j.auth.NullAuthorization;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.OAuthSupport;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.http.HttpResponseEvent;
import twitter4j.internal.http.HttpResponseListener;
import twitter4j.internal.http.XAuthAuthorization;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;

abstract class TwitterBaseImpl implements TwitterBase, Serializable, OAuthSupport, HttpResponseListener {
    private static final long serialVersionUID = -3812176145960812140L;
    protected Authorization auth;
    protected Configuration conf;
    protected z_T4JInternalFactory factory;
    protected transient HttpClientWrapper http;
    protected transient long id = 0;
    private List<RateLimitStatusListener> rateLimitStatusListeners = new ArrayList(0);
    protected transient String screenName = null;

    TwitterBaseImpl(Configuration conf2, Authorization auth2) {
        this.conf = conf2;
        this.auth = auth2;
        init();
    }

    private void init() {
        if (this.auth == null) {
            String consumerKey = this.conf.getOAuthConsumerKey();
            String consumerSecret = this.conf.getOAuthConsumerSecret();
            if (consumerKey == null || consumerSecret == null) {
                this.auth = NullAuthorization.getInstance();
            } else {
                OAuthAuthorization oauth = new OAuthAuthorization(this.conf);
                String accessToken = this.conf.getOAuthAccessToken();
                String accessTokenSecret = this.conf.getOAuthAccessTokenSecret();
                if (!(accessToken == null || accessTokenSecret == null)) {
                    oauth.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
                }
                this.auth = oauth;
            }
        }
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
        setFactory();
    }

    /* access modifiers changed from: protected */
    public void setFactory() {
        this.factory = new z_T4JInternalJSONImplFactory(this.conf);
    }

    public String getScreenName() throws TwitterException, IllegalStateException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (this.screenName == null) {
            if (this.auth instanceof BasicAuthorization) {
                this.screenName = ((BasicAuthorization) this.auth).getUserId();
                if (-1 != this.screenName.indexOf("@")) {
                    this.screenName = null;
                }
            }
            if (this.screenName == null) {
                fillInIDAndScreenName();
            }
        }
        return this.screenName;
    }

    public long getId() throws TwitterException, IllegalStateException {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        if (0 == this.id) {
            fillInIDAndScreenName();
        }
        return this.id;
    }

    /* access modifiers changed from: protected */
    public User fillInIDAndScreenName() throws TwitterException {
        ensureAuthorizationEnabled();
        User user = this.factory.createUser(this.http.get(this.conf.getRestBaseURL() + "account/verify_credentials.json", this.auth));
        this.screenName = user.getScreenName();
        this.id = user.getId();
        return user;
    }

    public void addRateLimitStatusListener(RateLimitStatusListener listener) {
        this.rateLimitStatusListeners.add(listener);
    }

    public void httpResponseReceived(HttpResponseEvent event) {
        RateLimitStatus rateLimitStatus;
        int statusCode;
        if (this.rateLimitStatusListeners.size() != 0) {
            HttpResponse res = event.getResponse();
            TwitterException te = event.getTwitterException();
            if (te != null) {
                rateLimitStatus = te.getRateLimitStatus();
                statusCode = te.getStatusCode();
            } else {
                rateLimitStatus = z_T4JInternalJSONImplFactory.createRateLimitStatusFromResponseHeader(res);
                statusCode = res.getStatusCode();
            }
            if (rateLimitStatus != null) {
                RateLimitStatusEvent statusEvent = new RateLimitStatusEvent(this, rateLimitStatus, event.isAuthenticated());
                if (statusCode == 420 || statusCode == 503) {
                    for (RateLimitStatusListener listener : this.rateLimitStatusListeners) {
                        listener.onRateLimitStatus(statusEvent);
                        listener.onRateLimitReached(statusEvent);
                    }
                    return;
                }
                for (RateLimitStatusListener listener2 : this.rateLimitStatusListeners) {
                    listener2.onRateLimitStatus(statusEvent);
                }
            }
        }
    }

    public final Authorization getAuthorization() {
        return this.auth;
    }

    public Configuration getConfiguration() {
        return this.conf;
    }

    public void shutdown() {
        if (this.http != null) {
            this.http.shutdown();
        }
    }

    /* access modifiers changed from: protected */
    public final void ensureAuthorizationEnabled() {
        if (!this.auth.isEnabled()) {
            throw new IllegalStateException("Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    /* access modifiers changed from: protected */
    public final void ensureOAuthEnabled() {
        if (!(this.auth instanceof OAuthAuthorization)) {
            throw new IllegalStateException("OAuth required. Authentication credentials are missing. See http://twitter4j.org/configuration.html for the detail.");
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.putFields();
        out.writeFields();
        out.writeObject(this.conf);
        out.writeObject(this.auth);
        List<RateLimitStatusListener> serializableRateLimitStatusListeners = new ArrayList<>(0);
        for (RateLimitStatusListener listener : this.rateLimitStatusListeners) {
            if (listener instanceof Serializable) {
                serializableRateLimitStatusListeners.add(listener);
            }
        }
        out.writeObject(serializableRateLimitStatusListeners);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.readFields();
        this.conf = (Configuration) stream.readObject();
        this.auth = (Authorization) stream.readObject();
        this.rateLimitStatusListeners = (List) stream.readObject();
        this.http = new HttpClientWrapper(this.conf);
        this.http.setHttpResponseListener(this);
        setFactory();
    }

    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret) {
        if (consumerKey == null) {
            throw new NullPointerException("consumer key is null");
        } else if (consumerSecret == null) {
            throw new NullPointerException("consumer secret is null");
        } else if (this.auth instanceof NullAuthorization) {
            OAuthAuthorization oauth = new OAuthAuthorization(this.conf);
            oauth.setOAuthConsumer(consumerKey, consumerSecret);
            this.auth = oauth;
        } else if (this.auth instanceof BasicAuthorization) {
            XAuthAuthorization xauth = new XAuthAuthorization((BasicAuthorization) this.auth);
            xauth.setOAuthConsumer(consumerKey, consumerSecret);
            this.auth = xauth;
        } else if (this.auth instanceof OAuthAuthorization) {
            throw new IllegalStateException("consumer key/secret pair already set.");
        }
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken((String) null);
    }

    public RequestToken getOAuthRequestToken(String callbackUrl) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl);
    }

    public RequestToken getOAuthRequestToken(String callbackUrl, String xAuthAccessType) throws TwitterException {
        return getOAuth().getOAuthRequestToken(callbackUrl, xAuthAccessType);
    }

    public synchronized AccessToken getOAuthAccessToken() throws TwitterException {
        AccessToken oauthAccessToken;
        Authorization auth2 = getAuthorization();
        if (auth2 instanceof BasicAuthorization) {
            BasicAuthorization basicAuth = (BasicAuthorization) auth2;
            Authorization auth3 = AuthorizationFactory.getInstance(this.conf);
            if (auth3 instanceof OAuthAuthorization) {
                this.auth = auth3;
                oauthAccessToken = ((OAuthAuthorization) auth3).getOAuthAccessToken(basicAuth.getUserId(), basicAuth.getPassword());
            } else {
                throw new IllegalStateException("consumer key / secret combination not supplied.");
            }
        } else if (auth2 instanceof XAuthAuthorization) {
            XAuthAuthorization xauth = (XAuthAuthorization) auth2;
            this.auth = xauth;
            OAuthAuthorization oauthAuth = new OAuthAuthorization(this.conf);
            oauthAuth.setOAuthConsumer(xauth.getConsumerKey(), xauth.getConsumerSecret());
            oauthAccessToken = oauthAuth.getOAuthAccessToken(xauth.getUserId(), xauth.getPassword());
        } else {
            oauthAccessToken = getOAuth().getOAuthAccessToken();
        }
        this.screenName = oauthAccessToken.getScreenName();
        this.id = oauthAccessToken.getUserId();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        AccessToken oauthAccessToken;
        oauthAccessToken = getOAuth().getOAuthAccessToken(oauthVerifier);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        AccessToken oauthAccessToken;
        oauthAccessToken = getOAuth().getOAuthAccessToken(requestToken);
        this.screenName = oauthAccessToken.getScreenName();
        return oauthAccessToken;
    }

    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        return getOAuth().getOAuthAccessToken(requestToken, oauthVerifier);
    }

    public synchronized void setOAuthAccessToken(AccessToken accessToken) {
        getOAuth().setOAuthAccessToken(accessToken);
    }

    public synchronized AccessToken getOAuthAccessToken(String screenName2, String password) throws TwitterException {
        return getOAuth().getOAuthAccessToken(screenName2, password);
    }

    private OAuthSupport getOAuth() {
        if (this.auth instanceof OAuthSupport) {
            return (OAuthSupport) this.auth;
        }
        throw new IllegalStateException("OAuth consumer key/secret combination not supplied");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterBaseImpl)) {
            return false;
        }
        TwitterBaseImpl that = (TwitterBaseImpl) o;
        if (this.auth == null ? that.auth != null : !this.auth.equals(that.auth)) {
            return false;
        }
        if (!this.conf.equals(that.conf)) {
            return false;
        }
        if (this.http == null ? that.http != null : !this.http.equals(that.http)) {
            return false;
        }
        if (!this.rateLimitStatusListeners.equals(that.rateLimitStatusListeners)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int hashCode = this.conf.hashCode() * 31;
        if (this.http != null) {
            i = this.http.hashCode();
        } else {
            i = 0;
        }
        int hashCode2 = (((hashCode + i) * 31) + this.rateLimitStatusListeners.hashCode()) * 31;
        if (this.auth != null) {
            i2 = this.auth.hashCode();
        }
        return hashCode2 + i2;
    }

    public String toString() {
        return "TwitterBase{conf=" + this.conf + ", http=" + this.http + ", rateLimitStatusListeners=" + this.rateLimitStatusListeners + ", auth=" + this.auth + '}';
    }
}
