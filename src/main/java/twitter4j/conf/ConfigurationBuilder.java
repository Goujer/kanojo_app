package twitter4j.conf;

import java.util.Properties;

public final class ConfigurationBuilder {
    private ConfigurationBase configurationBean = new PropertyConfiguration();

    public ConfigurationBuilder setUseSSL(boolean useSSL) {
        checkNotBuilt();
        this.configurationBean.setUseSSL(useSSL);
        return this;
    }

    public ConfigurationBuilder setPrettyDebugEnabled(boolean prettyDebugEnabled) {
        checkNotBuilt();
        this.configurationBean.setPrettyDebugEnabled(prettyDebugEnabled);
        return this;
    }

    public ConfigurationBuilder setGZIPEnabled(boolean gzipEnabled) {
        checkNotBuilt();
        this.configurationBean.setGZIPEnabled(gzipEnabled);
        return this;
    }

    public ConfigurationBuilder setDebugEnabled(boolean debugEnabled) {
        checkNotBuilt();
        this.configurationBean.setDebug(debugEnabled);
        return this;
    }

    public ConfigurationBuilder setUser(String user) {
        checkNotBuilt();
        this.configurationBean.setUser(user);
        return this;
    }

    public ConfigurationBuilder setPassword(String password) {
        checkNotBuilt();
        this.configurationBean.setPassword(password);
        return this;
    }

    public ConfigurationBuilder setHttpProxyHost(String httpProxyHost) {
        checkNotBuilt();
        this.configurationBean.setHttpProxyHost(httpProxyHost);
        return this;
    }

    public ConfigurationBuilder setHttpProxyUser(String httpProxyUser) {
        checkNotBuilt();
        this.configurationBean.setHttpProxyUser(httpProxyUser);
        return this;
    }

    public ConfigurationBuilder setHttpProxyPassword(String httpProxyPassword) {
        checkNotBuilt();
        this.configurationBean.setHttpProxyPassword(httpProxyPassword);
        return this;
    }

    public ConfigurationBuilder setHttpProxyPort(int httpProxyPort) {
        checkNotBuilt();
        this.configurationBean.setHttpProxyPort(httpProxyPort);
        return this;
    }

    public ConfigurationBuilder setHttpConnectionTimeout(int httpConnectionTimeout) {
        checkNotBuilt();
        this.configurationBean.setHttpConnectionTimeout(httpConnectionTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpReadTimeout(int httpReadTimeout) {
        checkNotBuilt();
        this.configurationBean.setHttpReadTimeout(httpReadTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpStreamingReadTimeout(int httpStreamingReadTimeout) {
        checkNotBuilt();
        this.configurationBean.setHttpStreamingReadTimeout(httpStreamingReadTimeout);
        return this;
    }

    public ConfigurationBuilder setHttpRetryCount(int httpRetryCount) {
        checkNotBuilt();
        this.configurationBean.setHttpRetryCount(httpRetryCount);
        return this;
    }

    public ConfigurationBuilder setHttpMaxTotalConnections(int httpMaxConnections) {
        checkNotBuilt();
        this.configurationBean.setHttpMaxTotalConnections(httpMaxConnections);
        return this;
    }

    public ConfigurationBuilder setHttpDefaultMaxPerRoute(int httpDefaultMaxPerRoute) {
        checkNotBuilt();
        this.configurationBean.setHttpDefaultMaxPerRoute(httpDefaultMaxPerRoute);
        return this;
    }

    public ConfigurationBuilder setHttpRetryIntervalSeconds(int httpRetryIntervalSeconds) {
        checkNotBuilt();
        this.configurationBean.setHttpRetryIntervalSeconds(httpRetryIntervalSeconds);
        return this;
    }

    public ConfigurationBuilder setOAuthConsumerKey(String oAuthConsumerKey) {
        checkNotBuilt();
        this.configurationBean.setOAuthConsumerKey(oAuthConsumerKey);
        return this;
    }

    public ConfigurationBuilder setOAuthConsumerSecret(String oAuthConsumerSecret) {
        checkNotBuilt();
        this.configurationBean.setOAuthConsumerSecret(oAuthConsumerSecret);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessToken(String oAuthAccessToken) {
        checkNotBuilt();
        this.configurationBean.setOAuthAccessToken(oAuthAccessToken);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
        checkNotBuilt();
        this.configurationBean.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        return this;
    }

    public ConfigurationBuilder setOAuthRequestTokenURL(String oAuthRequestTokenURL) {
        checkNotBuilt();
        this.configurationBean.setOAuthRequestTokenURL(oAuthRequestTokenURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAuthorizationURL(String oAuthAuthorizationURL) {
        checkNotBuilt();
        this.configurationBean.setOAuthAuthorizationURL(oAuthAuthorizationURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAccessTokenURL(String oAuthAccessTokenURL) {
        checkNotBuilt();
        this.configurationBean.setOAuthAccessTokenURL(oAuthAccessTokenURL);
        return this;
    }

    public ConfigurationBuilder setOAuthAuthenticationURL(String oAuthAuthenticationURL) {
        checkNotBuilt();
        this.configurationBean.setOAuthAuthenticationURL(oAuthAuthenticationURL);
        return this;
    }

    public ConfigurationBuilder setRestBaseURL(String restBaseURL) {
        checkNotBuilt();
        this.configurationBean.setRestBaseURL(restBaseURL);
        return this;
    }

    public ConfigurationBuilder setStreamBaseURL(String streamBaseURL) {
        checkNotBuilt();
        this.configurationBean.setStreamBaseURL(streamBaseURL);
        return this;
    }

    public ConfigurationBuilder setUserStreamBaseURL(String userStreamBaseURL) {
        checkNotBuilt();
        this.configurationBean.setUserStreamBaseURL(userStreamBaseURL);
        return this;
    }

    public ConfigurationBuilder setSiteStreamBaseURL(String siteStreamBaseURL) {
        checkNotBuilt();
        this.configurationBean.setSiteStreamBaseURL(siteStreamBaseURL);
        return this;
    }

    public ConfigurationBuilder setAsyncNumThreads(int asyncNumThreads) {
        checkNotBuilt();
        this.configurationBean.setAsyncNumThreads(asyncNumThreads);
        return this;
    }

    public ConfigurationBuilder setContributingTo(long contributingTo) {
        checkNotBuilt();
        this.configurationBean.setContributingTo(contributingTo);
        return this;
    }

    public ConfigurationBuilder setClientVersion(String clientVersion) {
        checkNotBuilt();
        this.configurationBean.setClientVersion(clientVersion);
        return this;
    }

    public ConfigurationBuilder setClientURL(String clientURL) {
        checkNotBuilt();
        this.configurationBean.setClientURL(clientURL);
        return this;
    }

    public ConfigurationBuilder setDispatcherImpl(String dispatcherImpl) {
        checkNotBuilt();
        this.configurationBean.setDispatcherImpl(dispatcherImpl);
        return this;
    }

    public ConfigurationBuilder setIncludeRTsEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setIncludeRTsEnbled(enabled);
        return this;
    }

    public ConfigurationBuilder setIncludeEntitiesEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setIncludeEntitiesEnbled(enabled);
        return this;
    }

    public ConfigurationBuilder setIncludeMyRetweetEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setIncludeMyRetweetEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setJSONStoreEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setJSONStoreEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setMBeanEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setMBeanEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setUserStreamRepliesAllEnabled(boolean enabled) {
        checkNotBuilt();
        this.configurationBean.setUserStreamRepliesAllEnabled(enabled);
        return this;
    }

    public ConfigurationBuilder setMediaProvider(String mediaProvider) {
        checkNotBuilt();
        this.configurationBean.setMediaProvider(mediaProvider);
        return this;
    }

    public ConfigurationBuilder setMediaProviderAPIKey(String mediaProviderAPIKey) {
        checkNotBuilt();
        this.configurationBean.setMediaProviderAPIKey(mediaProviderAPIKey);
        return this;
    }

    public ConfigurationBuilder setMediaProviderParameters(Properties props) {
        checkNotBuilt();
        this.configurationBean.setMediaProviderParameters(props);
        return this;
    }

    public ConfigurationBuilder setLoggerImpl(String loggerImpl) {
        checkNotBuilt();
        this.configurationBean.setLoggerFactory(loggerImpl);
        return this;
    }

    public Configuration build() {
        checkNotBuilt();
        this.configurationBean.cacheInstance();
        try {
            return this.configurationBean;
        } finally {
            this.configurationBean = null;
        }
    }

    private void checkNotBuilt() {
        if (this.configurationBean == null) {
            throw new IllegalStateException("Cannot use this builder any longer, build() has already been called");
        }
    }
}
