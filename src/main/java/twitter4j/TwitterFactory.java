package twitter4j;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public final class TwitterFactory implements Serializable {
    static final Authorization DEFAULT_AUTHORIZATION = AuthorizationFactory.getInstance(ConfigurationContext.getInstance());
    private static final Twitter SINGLETON;
    private static final Constructor<Twitter> TWITTER_CONSTRUCTOR;
    private static final long serialVersionUID = 5193900138477709155L;
    private final Configuration conf;

    static {
        String className = null;
        if (ConfigurationContext.getInstance().isGAE()) {
            try {
                Class.forName("twitter4j.AppEngineTwitterImpl");
                className = "twitter4j.AppEngineTwitterImpl";
            } catch (ClassNotFoundException e) {
            }
        }
        if (className == null) {
            className = "twitter4j.TwitterImpl";
        }
        try {
            TWITTER_CONSTRUCTOR = Class.forName(className).getDeclaredConstructor(new Class[]{Configuration.class, Authorization.class});
            try {
                SINGLETON = TWITTER_CONSTRUCTOR.newInstance(new Object[]{ConfigurationContext.getInstance(), DEFAULT_AUTHORIZATION});
            } catch (InstantiationException e2) {
                throw new AssertionError(e2);
            } catch (IllegalAccessException e3) {
                throw new AssertionError(e3);
            } catch (InvocationTargetException e4) {
                throw new AssertionError(e4);
            }
        } catch (NoSuchMethodException e5) {
            throw new AssertionError(e5);
        } catch (ClassNotFoundException e6) {
            throw new AssertionError(e6);
        }
    }

    public TwitterFactory() {
        this(ConfigurationContext.getInstance());
    }

    public TwitterFactory(Configuration conf2) {
        if (conf2 == null) {
            throw new NullPointerException("configuration cannot be null");
        }
        this.conf = conf2;
    }

    public TwitterFactory(String configTreePath) {
        this(ConfigurationContext.getInstance(configTreePath));
    }

    public Twitter getInstance() {
        return getInstance(AuthorizationFactory.getInstance(this.conf));
    }

    public Twitter getInstance(AccessToken accessToken) {
        String consumerKey = this.conf.getOAuthConsumerKey();
        String consumerSecret = this.conf.getOAuthConsumerSecret();
        if (consumerKey == null && consumerSecret == null) {
            throw new IllegalStateException("Consumer key and Consumer secret not supplied.");
        }
        OAuthAuthorization oauth = new OAuthAuthorization(this.conf);
        oauth.setOAuthAccessToken(accessToken);
        return getInstance((Authorization) oauth);
    }

    public Twitter getInstance(Authorization auth) {
        try {
            return TWITTER_CONSTRUCTOR.newInstance(new Object[]{this.conf, auth});
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InvocationTargetException e3) {
            throw new AssertionError(e3);
        }
    }

    public static Twitter getSingleton() {
        return SINGLETON;
    }
}
