package twitter4j.conf;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import twitter4j.auth.AuthorizationConfiguration;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpClientWrapperConfiguration;

public interface Configuration extends HttpClientConfiguration, HttpClientWrapperConfiguration, AuthorizationConfiguration, Serializable {
    int getAsyncNumThreads();

    String getClientURL();

    String getClientVersion();

    long getContributingTo();

    String getDispatcherImpl();

    int getHttpConnectionTimeout();

    int getHttpDefaultMaxPerRoute();

    int getHttpMaxTotalConnections();

    String getHttpProxyHost();

    String getHttpProxyPassword();

    int getHttpProxyPort();

    String getHttpProxyUser();

    int getHttpReadTimeout();

    int getHttpRetryCount();

    int getHttpRetryIntervalSeconds();

    int getHttpStreamingReadTimeout();

    String getLoggerFactory();

    String getMediaProvider();

    String getMediaProviderAPIKey();

    Properties getMediaProviderParameters();

    String getOAuthAccessToken();

    String getOAuthAccessTokenSecret();

    String getOAuthAccessTokenURL();

    String getOAuthAuthenticationURL();

    String getOAuthAuthorizationURL();

    String getOAuthConsumerKey();

    String getOAuthConsumerSecret();

    String getOAuthRequestTokenURL();

    String getPassword();

    Map<String, String> getRequestHeaders();

    String getRestBaseURL();

    String getSiteStreamBaseURL();

    String getStreamBaseURL();

    String getUser();

    String getUserAgent();

    String getUserStreamBaseURL();

    boolean isDalvik();

    boolean isDebugEnabled();

    boolean isGAE();

    boolean isIncludeEntitiesEnabled();

    boolean isIncludeMyRetweetEnabled();

    boolean isIncludeRTsEnabled();

    boolean isJSONStoreEnabled();

    boolean isMBeanEnabled();

    boolean isStallWarningsEnabled();

    boolean isUserStreamRepliesAllEnabled();
}
