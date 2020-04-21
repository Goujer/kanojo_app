package twitter4j.conf;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import twitter4j.Version;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

class ConfigurationBase implements Configuration, Serializable {
    public static final String DALVIK = "twitter4j.dalvik";
    private static final String DEFAULT_OAUTH_ACCESS_TOKEN_URL = "http://api.twitter.com/oauth/access_token";
    private static final String DEFAULT_OAUTH_AUTHENTICATION_URL = "http://api.twitter.com/oauth/authenticate";
    private static final String DEFAULT_OAUTH_AUTHORIZATION_URL = "http://api.twitter.com/oauth/authorize";
    private static final String DEFAULT_OAUTH_REQUEST_TOKEN_URL = "http://api.twitter.com/oauth/request_token";
    private static final String DEFAULT_REST_BASE_URL = "http://api.twitter.com/1.1/";
    private static final String DEFAULT_SITE_STREAM_BASE_URL = "https://sitestream.twitter.com/1.1/";
    private static final String DEFAULT_STREAM_BASE_URL = "https://stream.twitter.com/1.1/";
    private static final String DEFAULT_USER_STREAM_BASE_URL = "https://userstream.twitter.com/1.1/";
    public static final String GAE = "twitter4j.gae";
    static String dalvikDetected = null;
    static String gaeDetected = null;
    private static final List<ConfigurationBase> instances = new ArrayList();
    private static final long serialVersionUID = -6610497517837844232L;
    private boolean IS_DALVIK;
    private boolean IS_GAE;
    private int asyncNumThreads;
    private String clientURL;
    private String clientVersion;
    private long contributingTo;
    private boolean debug;
    private int defaultMaxPerRoute;
    private String dispatcherImpl;
    private boolean gzipEnabled;
    private int httpConnectionTimeout;
    private String httpProxyHost;
    private String httpProxyPassword;
    private int httpProxyPort;
    private String httpProxyUser;
    private int httpReadTimeout;
    private int httpRetryCount;
    private int httpRetryIntervalSeconds;
    private int httpStreamingReadTimeout;
    private boolean includeEntitiesEnabled = true;
    private boolean includeMyRetweetEnabled = true;
    private boolean includeRTsEnabled = true;
    private boolean jsonStoreEnabled;
    private String loggerFactory;
    private int maxTotalConnections;
    private boolean mbeanEnabled;
    private String mediaProvider;
    private String mediaProviderAPIKey;
    private Properties mediaProviderParameters;
    private String oAuthAccessToken;
    private String oAuthAccessTokenSecret;
    private String oAuthAccessTokenURL;
    private String oAuthAuthenticationURL;
    private String oAuthAuthorizationURL;
    private String oAuthConsumerKey;
    private String oAuthConsumerSecret;
    private String oAuthRequestTokenURL;
    private String password;
    private boolean prettyDebug;
    Map<String, String> requestHeaders;
    private String restBaseURL;
    private String siteStreamBaseURL;
    private boolean stallWarningsEnabled;
    private String streamBaseURL;
    private boolean useSSL;
    private String user;
    private String userAgent;
    private String userStreamBaseURL;
    private boolean userStreamRepliesAllEnabled;

    static {
        try {
            Class.forName("dalvik.system.VMRuntime");
            dalvikDetected = "true";
        } catch (ClassNotFoundException e) {
            dalvikDetected = "false";
        }
        try {
            Class.forName("com.google.appengine.api.urlfetch.URLFetchService");
            gaeDetected = "true";
        } catch (ClassNotFoundException e2) {
            gaeDetected = "false";
        }
    }

    protected ConfigurationBase() {
        String isDalvik;
        String isGAE;
        setDebug(false);
        setUser((String) null);
        setPassword((String) null);
        setUseSSL(false);
        setPrettyDebugEnabled(false);
        setGZIPEnabled(true);
        setHttpProxyHost((String) null);
        setHttpProxyUser((String) null);
        setHttpProxyPassword((String) null);
        setHttpProxyPort(-1);
        setHttpConnectionTimeout(20000);
        setHttpReadTimeout(120000);
        setHttpStreamingReadTimeout(40000);
        setHttpRetryCount(0);
        setHttpRetryIntervalSeconds(5);
        setHttpMaxTotalConnections(20);
        setHttpDefaultMaxPerRoute(2);
        setOAuthConsumerKey((String) null);
        setOAuthConsumerSecret((String) null);
        setOAuthAccessToken((String) null);
        setOAuthAccessTokenSecret((String) null);
        setAsyncNumThreads(1);
        setContributingTo(-1);
        setClientVersion(Version.getVersion());
        setClientURL("http://twitter4j.org/en/twitter4j-" + Version.getVersion() + ".xml");
        setUserAgent("twitter4j http://twitter4j.org/ /" + Version.getVersion());
        setJSONStoreEnabled(false);
        setMBeanEnabled(false);
        setOAuthRequestTokenURL(DEFAULT_OAUTH_REQUEST_TOKEN_URL);
        setOAuthAuthorizationURL(DEFAULT_OAUTH_AUTHORIZATION_URL);
        setOAuthAccessTokenURL(DEFAULT_OAUTH_ACCESS_TOKEN_URL);
        setOAuthAuthenticationURL(DEFAULT_OAUTH_AUTHENTICATION_URL);
        setRestBaseURL(DEFAULT_REST_BASE_URL);
        setStreamBaseURL(DEFAULT_STREAM_BASE_URL);
        setUserStreamBaseURL(DEFAULT_USER_STREAM_BASE_URL);
        setSiteStreamBaseURL(DEFAULT_SITE_STREAM_BASE_URL);
        setDispatcherImpl("twitter4j.internal.async.DispatcherImpl");
        setLoggerFactory((String) null);
        setUserStreamRepliesAllEnabled(false);
        setStallWarningsEnabled(true);
        try {
            isDalvik = System.getProperty(DALVIK, dalvikDetected);
        } catch (SecurityException e) {
            isDalvik = dalvikDetected;
        }
        this.IS_DALVIK = Boolean.valueOf(isDalvik).booleanValue();
        try {
            isGAE = System.getProperty(GAE, gaeDetected);
        } catch (SecurityException e2) {
            isGAE = gaeDetected;
        }
        this.IS_GAE = Boolean.valueOf(isGAE).booleanValue();
        setMediaProvider("TWITTER");
        setMediaProviderAPIKey((String) null);
        setMediaProviderParameters((Properties) null);
    }

    public void dumpConfiguration() {
        Logger log = Logger.getLogger(ConfigurationBase.class);
        if (this.debug) {
            for (Field field : ConfigurationBase.class.getDeclaredFields()) {
                try {
                    Object value = field.get(this);
                    String strValue = String.valueOf(value);
                    if (value != null && field.getName().matches("oAuthConsumerSecret|oAuthAccessTokenSecret|password")) {
                        strValue = z_T4JInternalStringUtil.maskString(String.valueOf(value));
                    }
                    log.debug(field.getName() + ": " + strValue);
                } catch (IllegalAccessException e) {
                }
            }
        }
        if (!this.includeRTsEnabled) {
            log.warn("includeRTsEnabled is set to false. This configuration may not take effect after May 14th, 2012. https://dev.twitter.com/blog/api-housekeeping");
        }
        if (!this.includeEntitiesEnabled) {
            log.warn("includeEntitiesEnabled is set to false. This configuration may not take effect after May 14th, 2012. https://dev.twitter.com/blog/api-housekeeping");
        }
    }

    public final boolean isDalvik() {
        return this.IS_DALVIK;
    }

    public boolean isGAE() {
        return this.IS_GAE;
    }

    public final boolean isDebugEnabled() {
        return this.debug;
    }

    /* access modifiers changed from: protected */
    public final void setDebug(boolean debug2) {
        this.debug = debug2;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }

    /* access modifiers changed from: protected */
    public final void setUserAgent(String userAgent2) {
        this.userAgent = userAgent2;
        initRequestHeaders();
    }

    public final String getUser() {
        return this.user;
    }

    /* access modifiers changed from: protected */
    public final void setUser(String user2) {
        this.user = user2;
    }

    public final String getPassword() {
        return this.password;
    }

    /* access modifiers changed from: protected */
    public final void setPassword(String password2) {
        this.password = password2;
    }

    public boolean isPrettyDebugEnabled() {
        return this.prettyDebug;
    }

    /* access modifiers changed from: protected */
    public final void setUseSSL(boolean useSSL2) {
        this.useSSL = useSSL2;
        fixRestBaseURL();
    }

    /* access modifiers changed from: protected */
    public final void setPrettyDebugEnabled(boolean prettyDebug2) {
        this.prettyDebug = prettyDebug2;
    }

    /* access modifiers changed from: protected */
    public final void setGZIPEnabled(boolean gzipEnabled2) {
        this.gzipEnabled = gzipEnabled2;
        initRequestHeaders();
    }

    public boolean isGZIPEnabled() {
        return this.gzipEnabled;
    }

    private void initRequestHeaders() {
        this.requestHeaders = new HashMap();
        this.requestHeaders.put("X-Twitter-Client-Version", getClientVersion());
        this.requestHeaders.put("X-Twitter-Client-URL", getClientURL());
        this.requestHeaders.put("X-Twitter-Client", "Twitter4J");
        this.requestHeaders.put(DownloadHelper.REQUEST_HEADER_USERAGENT, getUserAgent());
        if (this.gzipEnabled) {
            this.requestHeaders.put("Accept-Encoding", "gzip");
        }
        if (this.IS_DALVIK) {
            this.requestHeaders.put("Connection", "close");
        }
    }

    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    public final String getHttpProxyHost() {
        return this.httpProxyHost;
    }

    /* access modifiers changed from: protected */
    public final void setHttpProxyHost(String proxyHost) {
        this.httpProxyHost = proxyHost;
    }

    public final String getHttpProxyUser() {
        return this.httpProxyUser;
    }

    /* access modifiers changed from: protected */
    public final void setHttpProxyUser(String proxyUser) {
        this.httpProxyUser = proxyUser;
    }

    public final String getHttpProxyPassword() {
        return this.httpProxyPassword;
    }

    /* access modifiers changed from: protected */
    public final void setHttpProxyPassword(String proxyPassword) {
        this.httpProxyPassword = proxyPassword;
    }

    public final int getHttpProxyPort() {
        return this.httpProxyPort;
    }

    /* access modifiers changed from: protected */
    public final void setHttpProxyPort(int proxyPort) {
        this.httpProxyPort = proxyPort;
    }

    public final int getHttpConnectionTimeout() {
        return this.httpConnectionTimeout;
    }

    /* access modifiers changed from: protected */
    public final void setHttpConnectionTimeout(int connectionTimeout) {
        this.httpConnectionTimeout = connectionTimeout;
    }

    public final int getHttpReadTimeout() {
        return this.httpReadTimeout;
    }

    /* access modifiers changed from: protected */
    public final void setHttpReadTimeout(int readTimeout) {
        this.httpReadTimeout = readTimeout;
    }

    public int getHttpStreamingReadTimeout() {
        return this.httpStreamingReadTimeout;
    }

    /* access modifiers changed from: protected */
    public final void setHttpStreamingReadTimeout(int httpStreamingReadTimeout2) {
        this.httpStreamingReadTimeout = httpStreamingReadTimeout2;
    }

    public final int getHttpRetryCount() {
        return this.httpRetryCount;
    }

    /* access modifiers changed from: protected */
    public final void setHttpRetryCount(int retryCount) {
        this.httpRetryCount = retryCount;
    }

    public final int getHttpRetryIntervalSeconds() {
        return this.httpRetryIntervalSeconds;
    }

    /* access modifiers changed from: protected */
    public final void setHttpRetryIntervalSeconds(int retryIntervalSeconds) {
        this.httpRetryIntervalSeconds = retryIntervalSeconds;
    }

    public final int getHttpMaxTotalConnections() {
        return this.maxTotalConnections;
    }

    /* access modifiers changed from: protected */
    public final void setHttpMaxTotalConnections(int maxTotalConnections2) {
        this.maxTotalConnections = maxTotalConnections2;
    }

    public final int getHttpDefaultMaxPerRoute() {
        return this.defaultMaxPerRoute;
    }

    /* access modifiers changed from: protected */
    public final void setHttpDefaultMaxPerRoute(int defaultMaxPerRoute2) {
        this.defaultMaxPerRoute = defaultMaxPerRoute2;
    }

    public final String getOAuthConsumerKey() {
        return this.oAuthConsumerKey;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthConsumerKey(String oAuthConsumerKey2) {
        this.oAuthConsumerKey = oAuthConsumerKey2;
        fixRestBaseURL();
    }

    public final String getOAuthConsumerSecret() {
        return this.oAuthConsumerSecret;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthConsumerSecret(String oAuthConsumerSecret2) {
        this.oAuthConsumerSecret = oAuthConsumerSecret2;
        fixRestBaseURL();
    }

    public String getOAuthAccessToken() {
        return this.oAuthAccessToken;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthAccessToken(String oAuthAccessToken2) {
        this.oAuthAccessToken = oAuthAccessToken2;
    }

    public String getOAuthAccessTokenSecret() {
        return this.oAuthAccessTokenSecret;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret2) {
        this.oAuthAccessTokenSecret = oAuthAccessTokenSecret2;
    }

    public final int getAsyncNumThreads() {
        return this.asyncNumThreads;
    }

    /* access modifiers changed from: protected */
    public final void setAsyncNumThreads(int asyncNumThreads2) {
        this.asyncNumThreads = asyncNumThreads2;
    }

    public final long getContributingTo() {
        return this.contributingTo;
    }

    /* access modifiers changed from: protected */
    public final void setContributingTo(long contributingTo2) {
        this.contributingTo = contributingTo2;
    }

    public final String getClientVersion() {
        return this.clientVersion;
    }

    /* access modifiers changed from: protected */
    public final void setClientVersion(String clientVersion2) {
        this.clientVersion = clientVersion2;
        initRequestHeaders();
    }

    public final String getClientURL() {
        return this.clientURL;
    }

    /* access modifiers changed from: protected */
    public final void setClientURL(String clientURL2) {
        this.clientURL = clientURL2;
        initRequestHeaders();
    }

    public String getRestBaseURL() {
        return this.restBaseURL;
    }

    /* access modifiers changed from: protected */
    public final void setRestBaseURL(String restBaseURL2) {
        this.restBaseURL = restBaseURL2;
        fixRestBaseURL();
    }

    private void fixRestBaseURL() {
        if (DEFAULT_REST_BASE_URL.equals(fixURL(false, this.restBaseURL))) {
            this.restBaseURL = fixURL(this.useSSL, this.restBaseURL);
        }
        if (DEFAULT_OAUTH_ACCESS_TOKEN_URL.equals(fixURL(false, this.oAuthAccessTokenURL))) {
            this.oAuthAccessTokenURL = fixURL(this.useSSL, this.oAuthAccessTokenURL);
        }
        if (DEFAULT_OAUTH_AUTHENTICATION_URL.equals(fixURL(false, this.oAuthAuthenticationURL))) {
            this.oAuthAuthenticationURL = fixURL(this.useSSL, this.oAuthAuthenticationURL);
        }
        if (DEFAULT_OAUTH_AUTHORIZATION_URL.equals(fixURL(false, this.oAuthAuthorizationURL))) {
            this.oAuthAuthorizationURL = fixURL(this.useSSL, this.oAuthAuthorizationURL);
        }
        if (DEFAULT_OAUTH_REQUEST_TOKEN_URL.equals(fixURL(false, this.oAuthRequestTokenURL))) {
            this.oAuthRequestTokenURL = fixURL(this.useSSL, this.oAuthRequestTokenURL);
        }
    }

    public String getStreamBaseURL() {
        return this.streamBaseURL;
    }

    /* access modifiers changed from: protected */
    public final void setStreamBaseURL(String streamBaseURL2) {
        this.streamBaseURL = streamBaseURL2;
    }

    public String getUserStreamBaseURL() {
        return this.userStreamBaseURL;
    }

    /* access modifiers changed from: protected */
    public final void setUserStreamBaseURL(String siteStreamBaseURL2) {
        this.userStreamBaseURL = siteStreamBaseURL2;
    }

    public String getSiteStreamBaseURL() {
        return this.siteStreamBaseURL;
    }

    /* access modifiers changed from: protected */
    public final void setSiteStreamBaseURL(String siteStreamBaseURL2) {
        this.siteStreamBaseURL = siteStreamBaseURL2;
    }

    public String getOAuthRequestTokenURL() {
        return this.oAuthRequestTokenURL;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthRequestTokenURL(String oAuthRequestTokenURL2) {
        this.oAuthRequestTokenURL = oAuthRequestTokenURL2;
        fixRestBaseURL();
    }

    public String getOAuthAuthorizationURL() {
        return this.oAuthAuthorizationURL;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthAuthorizationURL(String oAuthAuthorizationURL2) {
        this.oAuthAuthorizationURL = oAuthAuthorizationURL2;
        fixRestBaseURL();
    }

    public String getOAuthAccessTokenURL() {
        return this.oAuthAccessTokenURL;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthAccessTokenURL(String oAuthAccessTokenURL2) {
        this.oAuthAccessTokenURL = oAuthAccessTokenURL2;
        fixRestBaseURL();
    }

    public String getOAuthAuthenticationURL() {
        return this.oAuthAuthenticationURL;
    }

    /* access modifiers changed from: protected */
    public final void setOAuthAuthenticationURL(String oAuthAuthenticationURL2) {
        this.oAuthAuthenticationURL = oAuthAuthenticationURL2;
        fixRestBaseURL();
    }

    public String getDispatcherImpl() {
        return this.dispatcherImpl;
    }

    /* access modifiers changed from: protected */
    public final void setDispatcherImpl(String dispatcherImpl2) {
        this.dispatcherImpl = dispatcherImpl2;
    }

    public String getLoggerFactory() {
        return this.loggerFactory;
    }

    public boolean isIncludeRTsEnabled() {
        return this.includeRTsEnabled;
    }

    public boolean isIncludeEntitiesEnabled() {
        return this.includeEntitiesEnabled;
    }

    /* access modifiers changed from: protected */
    public final void setLoggerFactory(String loggerImpl) {
        this.loggerFactory = loggerImpl;
    }

    /* access modifiers changed from: protected */
    public final void setIncludeRTsEnbled(boolean enabled) {
        this.includeRTsEnabled = enabled;
    }

    /* access modifiers changed from: protected */
    public final void setIncludeEntitiesEnbled(boolean enabled) {
        this.includeEntitiesEnabled = enabled;
    }

    public boolean isIncludeMyRetweetEnabled() {
        return this.includeMyRetweetEnabled;
    }

    public void setIncludeMyRetweetEnabled(boolean enabled) {
        this.includeMyRetweetEnabled = enabled;
    }

    public boolean isJSONStoreEnabled() {
        return this.jsonStoreEnabled;
    }

    /* access modifiers changed from: protected */
    public final void setJSONStoreEnabled(boolean enabled) {
        this.jsonStoreEnabled = enabled;
    }

    public boolean isMBeanEnabled() {
        return this.mbeanEnabled;
    }

    /* access modifiers changed from: protected */
    public final void setMBeanEnabled(boolean enabled) {
        this.mbeanEnabled = enabled;
    }

    public boolean isUserStreamRepliesAllEnabled() {
        return this.userStreamRepliesAllEnabled;
    }

    /* access modifiers changed from: protected */
    public final void setUserStreamRepliesAllEnabled(boolean enabled) {
        this.userStreamRepliesAllEnabled = enabled;
    }

    public boolean isStallWarningsEnabled() {
        return this.stallWarningsEnabled;
    }

    /* access modifiers changed from: protected */
    public final void setStallWarningsEnabled(boolean stallWarningsEnabled2) {
        this.stallWarningsEnabled = stallWarningsEnabled2;
    }

    public String getMediaProvider() {
        return this.mediaProvider;
    }

    /* access modifiers changed from: protected */
    public final void setMediaProvider(String mediaProvider2) {
        this.mediaProvider = mediaProvider2;
    }

    public String getMediaProviderAPIKey() {
        return this.mediaProviderAPIKey;
    }

    /* access modifiers changed from: protected */
    public final void setMediaProviderAPIKey(String mediaProviderAPIKey2) {
        this.mediaProviderAPIKey = mediaProviderAPIKey2;
    }

    public Properties getMediaProviderParameters() {
        return this.mediaProviderParameters;
    }

    /* access modifiers changed from: protected */
    public final void setMediaProviderParameters(Properties props) {
        this.mediaProviderParameters = props;
    }

    static String fixURL(boolean useSSL2, String url) {
        if (url == null) {
            return null;
        }
        int index = url.indexOf("://");
        if (-1 == index) {
            throw new IllegalArgumentException("url should contain '://'");
        }
        String hostAndLater = url.substring(index + 3);
        if (useSSL2) {
            return "https://" + hostAndLater;
        }
        return "http://" + hostAndLater;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationBase that = (ConfigurationBase) o;
        if (this.IS_DALVIK != that.IS_DALVIK) {
            return false;
        }
        if (this.IS_GAE != that.IS_GAE) {
            return false;
        }
        if (this.asyncNumThreads != that.asyncNumThreads) {
            return false;
        }
        if (this.contributingTo != that.contributingTo) {
            return false;
        }
        if (this.debug != that.debug) {
            return false;
        }
        if (this.defaultMaxPerRoute != that.defaultMaxPerRoute) {
            return false;
        }
        if (this.gzipEnabled != that.gzipEnabled) {
            return false;
        }
        if (this.httpConnectionTimeout != that.httpConnectionTimeout) {
            return false;
        }
        if (this.httpProxyPort != that.httpProxyPort) {
            return false;
        }
        if (this.httpReadTimeout != that.httpReadTimeout) {
            return false;
        }
        if (this.httpRetryCount != that.httpRetryCount) {
            return false;
        }
        if (this.httpRetryIntervalSeconds != that.httpRetryIntervalSeconds) {
            return false;
        }
        if (this.httpStreamingReadTimeout != that.httpStreamingReadTimeout) {
            return false;
        }
        if (this.includeEntitiesEnabled != that.includeEntitiesEnabled) {
            return false;
        }
        if (this.includeMyRetweetEnabled != that.includeMyRetweetEnabled) {
            return false;
        }
        if (this.includeRTsEnabled != that.includeRTsEnabled) {
            return false;
        }
        if (this.jsonStoreEnabled != that.jsonStoreEnabled) {
            return false;
        }
        if (this.maxTotalConnections != that.maxTotalConnections) {
            return false;
        }
        if (this.mbeanEnabled != that.mbeanEnabled) {
            return false;
        }
        if (this.prettyDebug != that.prettyDebug) {
            return false;
        }
        if (this.stallWarningsEnabled != that.stallWarningsEnabled) {
            return false;
        }
        if (this.useSSL != that.useSSL) {
            return false;
        }
        if (this.userStreamRepliesAllEnabled != that.userStreamRepliesAllEnabled) {
            return false;
        }
        if (this.clientURL == null ? that.clientURL != null : !this.clientURL.equals(that.clientURL)) {
            return false;
        }
        if (this.clientVersion == null ? that.clientVersion != null : !this.clientVersion.equals(that.clientVersion)) {
            return false;
        }
        if (this.dispatcherImpl == null ? that.dispatcherImpl != null : !this.dispatcherImpl.equals(that.dispatcherImpl)) {
            return false;
        }
        if (this.httpProxyHost == null ? that.httpProxyHost != null : !this.httpProxyHost.equals(that.httpProxyHost)) {
            return false;
        }
        if (this.httpProxyPassword == null ? that.httpProxyPassword != null : !this.httpProxyPassword.equals(that.httpProxyPassword)) {
            return false;
        }
        if (this.httpProxyUser == null ? that.httpProxyUser != null : !this.httpProxyUser.equals(that.httpProxyUser)) {
            return false;
        }
        if (this.loggerFactory == null ? that.loggerFactory != null : !this.loggerFactory.equals(that.loggerFactory)) {
            return false;
        }
        if (this.mediaProvider == null ? that.mediaProvider != null : !this.mediaProvider.equals(that.mediaProvider)) {
            return false;
        }
        if (this.mediaProviderAPIKey == null ? that.mediaProviderAPIKey != null : !this.mediaProviderAPIKey.equals(that.mediaProviderAPIKey)) {
            return false;
        }
        if (this.mediaProviderParameters == null ? that.mediaProviderParameters != null : !this.mediaProviderParameters.equals(that.mediaProviderParameters)) {
            return false;
        }
        if (this.oAuthAccessToken == null ? that.oAuthAccessToken != null : !this.oAuthAccessToken.equals(that.oAuthAccessToken)) {
            return false;
        }
        if (this.oAuthAccessTokenSecret == null ? that.oAuthAccessTokenSecret != null : !this.oAuthAccessTokenSecret.equals(that.oAuthAccessTokenSecret)) {
            return false;
        }
        if (this.oAuthAccessTokenURL == null ? that.oAuthAccessTokenURL != null : !this.oAuthAccessTokenURL.equals(that.oAuthAccessTokenURL)) {
            return false;
        }
        if (this.oAuthAuthenticationURL == null ? that.oAuthAuthenticationURL != null : !this.oAuthAuthenticationURL.equals(that.oAuthAuthenticationURL)) {
            return false;
        }
        if (this.oAuthAuthorizationURL == null ? that.oAuthAuthorizationURL != null : !this.oAuthAuthorizationURL.equals(that.oAuthAuthorizationURL)) {
            return false;
        }
        if (this.oAuthConsumerKey == null ? that.oAuthConsumerKey != null : !this.oAuthConsumerKey.equals(that.oAuthConsumerKey)) {
            return false;
        }
        if (this.oAuthConsumerSecret == null ? that.oAuthConsumerSecret != null : !this.oAuthConsumerSecret.equals(that.oAuthConsumerSecret)) {
            return false;
        }
        if (this.oAuthRequestTokenURL == null ? that.oAuthRequestTokenURL != null : !this.oAuthRequestTokenURL.equals(that.oAuthRequestTokenURL)) {
            return false;
        }
        if (this.password == null ? that.password != null : !this.password.equals(that.password)) {
            return false;
        }
        if (this.requestHeaders == null ? that.requestHeaders != null : !this.requestHeaders.equals(that.requestHeaders)) {
            return false;
        }
        if (this.restBaseURL == null ? that.restBaseURL != null : !this.restBaseURL.equals(that.restBaseURL)) {
            return false;
        }
        if (this.siteStreamBaseURL == null ? that.siteStreamBaseURL != null : !this.siteStreamBaseURL.equals(that.siteStreamBaseURL)) {
            return false;
        }
        if (this.streamBaseURL == null ? that.streamBaseURL != null : !this.streamBaseURL.equals(that.streamBaseURL)) {
            return false;
        }
        if (this.user == null ? that.user != null : !this.user.equals(that.user)) {
            return false;
        }
        if (this.userAgent == null ? that.userAgent != null : !this.userAgent.equals(that.userAgent)) {
            return false;
        }
        if (this.userStreamBaseURL != null) {
            if (this.userStreamBaseURL.equals(that.userStreamBaseURL)) {
                return true;
            }
        } else if (that.userStreamBaseURL == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30;
        int i31;
        int i32;
        int i33;
        int i34;
        int i35;
        int i36;
        int i37 = 1;
        int i38 = 0;
        if (this.debug) {
            result = 1;
        } else {
            result = 0;
        }
        int i39 = result * 31;
        if (this.userAgent != null) {
            i = this.userAgent.hashCode();
        } else {
            i = 0;
        }
        int i40 = (i39 + i) * 31;
        if (this.user != null) {
            i2 = this.user.hashCode();
        } else {
            i2 = 0;
        }
        int i41 = (i40 + i2) * 31;
        if (this.password != null) {
            i3 = this.password.hashCode();
        } else {
            i3 = 0;
        }
        int i42 = (i41 + i3) * 31;
        if (this.useSSL) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i43 = (i42 + i4) * 31;
        if (this.prettyDebug) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i44 = (i43 + i5) * 31;
        if (this.gzipEnabled) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i45 = (i44 + i6) * 31;
        if (this.httpProxyHost != null) {
            i7 = this.httpProxyHost.hashCode();
        } else {
            i7 = 0;
        }
        int i46 = (i45 + i7) * 31;
        if (this.httpProxyUser != null) {
            i8 = this.httpProxyUser.hashCode();
        } else {
            i8 = 0;
        }
        int i47 = (i46 + i8) * 31;
        if (this.httpProxyPassword != null) {
            i9 = this.httpProxyPassword.hashCode();
        } else {
            i9 = 0;
        }
        int i48 = (((((((((((((((((i47 + i9) * 31) + this.httpProxyPort) * 31) + this.httpConnectionTimeout) * 31) + this.httpReadTimeout) * 31) + this.httpStreamingReadTimeout) * 31) + this.httpRetryCount) * 31) + this.httpRetryIntervalSeconds) * 31) + this.maxTotalConnections) * 31) + this.defaultMaxPerRoute) * 31;
        if (this.oAuthConsumerKey != null) {
            i10 = this.oAuthConsumerKey.hashCode();
        } else {
            i10 = 0;
        }
        int i49 = (i48 + i10) * 31;
        if (this.oAuthConsumerSecret != null) {
            i11 = this.oAuthConsumerSecret.hashCode();
        } else {
            i11 = 0;
        }
        int i50 = (i49 + i11) * 31;
        if (this.oAuthAccessToken != null) {
            i12 = this.oAuthAccessToken.hashCode();
        } else {
            i12 = 0;
        }
        int i51 = (i50 + i12) * 31;
        if (this.oAuthAccessTokenSecret != null) {
            i13 = this.oAuthAccessTokenSecret.hashCode();
        } else {
            i13 = 0;
        }
        int i52 = (i51 + i13) * 31;
        if (this.oAuthRequestTokenURL != null) {
            i14 = this.oAuthRequestTokenURL.hashCode();
        } else {
            i14 = 0;
        }
        int i53 = (i52 + i14) * 31;
        if (this.oAuthAuthorizationURL != null) {
            i15 = this.oAuthAuthorizationURL.hashCode();
        } else {
            i15 = 0;
        }
        int i54 = (i53 + i15) * 31;
        if (this.oAuthAccessTokenURL != null) {
            i16 = this.oAuthAccessTokenURL.hashCode();
        } else {
            i16 = 0;
        }
        int i55 = (i54 + i16) * 31;
        if (this.oAuthAuthenticationURL != null) {
            i17 = this.oAuthAuthenticationURL.hashCode();
        } else {
            i17 = 0;
        }
        int i56 = (i55 + i17) * 31;
        if (this.restBaseURL != null) {
            i18 = this.restBaseURL.hashCode();
        } else {
            i18 = 0;
        }
        int i57 = (i56 + i18) * 31;
        if (this.streamBaseURL != null) {
            i19 = this.streamBaseURL.hashCode();
        } else {
            i19 = 0;
        }
        int i58 = (i57 + i19) * 31;
        if (this.userStreamBaseURL != null) {
            i20 = this.userStreamBaseURL.hashCode();
        } else {
            i20 = 0;
        }
        int i59 = (i58 + i20) * 31;
        if (this.siteStreamBaseURL != null) {
            i21 = this.siteStreamBaseURL.hashCode();
        } else {
            i21 = 0;
        }
        int i60 = (i59 + i21) * 31;
        if (this.dispatcherImpl != null) {
            i22 = this.dispatcherImpl.hashCode();
        } else {
            i22 = 0;
        }
        int i61 = (i60 + i22) * 31;
        if (this.loggerFactory != null) {
            i23 = this.loggerFactory.hashCode();
        } else {
            i23 = 0;
        }
        int i62 = (((((i61 + i23) * 31) + this.asyncNumThreads) * 31) + ((int) (this.contributingTo ^ (this.contributingTo >>> 32)))) * 31;
        if (this.includeRTsEnabled) {
            i24 = 1;
        } else {
            i24 = 0;
        }
        int i63 = (i62 + i24) * 31;
        if (this.includeEntitiesEnabled) {
            i25 = 1;
        } else {
            i25 = 0;
        }
        int i64 = (i63 + i25) * 31;
        if (this.includeMyRetweetEnabled) {
            i26 = 1;
        } else {
            i26 = 0;
        }
        int i65 = (i64 + i26) * 31;
        if (this.jsonStoreEnabled) {
            i27 = 1;
        } else {
            i27 = 0;
        }
        int i66 = (i65 + i27) * 31;
        if (this.mbeanEnabled) {
            i28 = 1;
        } else {
            i28 = 0;
        }
        int i67 = (i66 + i28) * 31;
        if (this.userStreamRepliesAllEnabled) {
            i29 = 1;
        } else {
            i29 = 0;
        }
        int i68 = (i67 + i29) * 31;
        if (this.stallWarningsEnabled) {
            i30 = 1;
        } else {
            i30 = 0;
        }
        int i69 = (i68 + i30) * 31;
        if (this.mediaProvider != null) {
            i31 = this.mediaProvider.hashCode();
        } else {
            i31 = 0;
        }
        int i70 = (i69 + i31) * 31;
        if (this.mediaProviderAPIKey != null) {
            i32 = this.mediaProviderAPIKey.hashCode();
        } else {
            i32 = 0;
        }
        int i71 = (i70 + i32) * 31;
        if (this.mediaProviderParameters != null) {
            i33 = this.mediaProviderParameters.hashCode();
        } else {
            i33 = 0;
        }
        int i72 = (i71 + i33) * 31;
        if (this.clientVersion != null) {
            i34 = this.clientVersion.hashCode();
        } else {
            i34 = 0;
        }
        int i73 = (i72 + i34) * 31;
        if (this.clientURL != null) {
            i35 = this.clientURL.hashCode();
        } else {
            i35 = 0;
        }
        int i74 = (i73 + i35) * 31;
        if (this.IS_DALVIK) {
            i36 = 1;
        } else {
            i36 = 0;
        }
        int i75 = (i74 + i36) * 31;
        if (!this.IS_GAE) {
            i37 = 0;
        }
        int i76 = (i75 + i37) * 31;
        if (this.requestHeaders != null) {
            i38 = this.requestHeaders.hashCode();
        }
        return i76 + i38;
    }

    public String toString() {
        return "ConfigurationBase{debug=" + this.debug + ", userAgent='" + this.userAgent + '\'' + ", user='" + this.user + '\'' + ", password='" + this.password + '\'' + ", useSSL=" + this.useSSL + ", prettyDebug=" + this.prettyDebug + ", gzipEnabled=" + this.gzipEnabled + ", httpProxyHost='" + this.httpProxyHost + '\'' + ", httpProxyUser='" + this.httpProxyUser + '\'' + ", httpProxyPassword='" + this.httpProxyPassword + '\'' + ", httpProxyPort=" + this.httpProxyPort + ", httpConnectionTimeout=" + this.httpConnectionTimeout + ", httpReadTimeout=" + this.httpReadTimeout + ", httpStreamingReadTimeout=" + this.httpStreamingReadTimeout + ", httpRetryCount=" + this.httpRetryCount + ", httpRetryIntervalSeconds=" + this.httpRetryIntervalSeconds + ", maxTotalConnections=" + this.maxTotalConnections + ", defaultMaxPerRoute=" + this.defaultMaxPerRoute + ", oAuthConsumerKey='" + this.oAuthConsumerKey + '\'' + ", oAuthConsumerSecret='" + this.oAuthConsumerSecret + '\'' + ", oAuthAccessToken='" + this.oAuthAccessToken + '\'' + ", oAuthAccessTokenSecret='" + this.oAuthAccessTokenSecret + '\'' + ", oAuthRequestTokenURL='" + this.oAuthRequestTokenURL + '\'' + ", oAuthAuthorizationURL='" + this.oAuthAuthorizationURL + '\'' + ", oAuthAccessTokenURL='" + this.oAuthAccessTokenURL + '\'' + ", oAuthAuthenticationURL='" + this.oAuthAuthenticationURL + '\'' + ", restBaseURL='" + this.restBaseURL + '\'' + ", streamBaseURL='" + this.streamBaseURL + '\'' + ", userStreamBaseURL='" + this.userStreamBaseURL + '\'' + ", siteStreamBaseURL='" + this.siteStreamBaseURL + '\'' + ", dispatcherImpl='" + this.dispatcherImpl + '\'' + ", loggerFactory='" + this.loggerFactory + '\'' + ", asyncNumThreads=" + this.asyncNumThreads + ", contributingTo=" + this.contributingTo + ", includeRTsEnabled=" + this.includeRTsEnabled + ", includeEntitiesEnabled=" + this.includeEntitiesEnabled + ", includeMyRetweetEnabled=" + this.includeMyRetweetEnabled + ", jsonStoreEnabled=" + this.jsonStoreEnabled + ", mbeanEnabled=" + this.mbeanEnabled + ", userStreamRepliesAllEnabled=" + this.userStreamRepliesAllEnabled + ", stallWarningsEnabled=" + this.stallWarningsEnabled + ", mediaProvider='" + this.mediaProvider + '\'' + ", mediaProviderAPIKey='" + this.mediaProviderAPIKey + '\'' + ", mediaProviderParameters=" + this.mediaProviderParameters + ", clientVersion='" + this.clientVersion + '\'' + ", clientURL='" + this.clientURL + '\'' + ", IS_DALVIK=" + this.IS_DALVIK + ", IS_GAE=" + this.IS_GAE + ", requestHeaders=" + this.requestHeaders + '}';
    }

    private static void cacheInstance(ConfigurationBase conf) {
        if (!instances.contains(conf)) {
            instances.add(conf);
        }
    }

    /* access modifiers changed from: protected */
    public void cacheInstance() {
        cacheInstance(this);
    }

    private static ConfigurationBase getInstance(ConfigurationBase configurationBase) {
        int index = instances.indexOf(configurationBase);
        if (index != -1) {
            return instances.get(index);
        }
        instances.add(configurationBase);
        return configurationBase;
    }

    /* access modifiers changed from: protected */
    public Object readResolve() throws ObjectStreamException {
        return getInstance(this);
    }
}
