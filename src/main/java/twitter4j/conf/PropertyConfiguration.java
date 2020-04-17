package twitter4j.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import twitter4j.internal.util.z_T4JInternalStringUtil;

public final class PropertyConfiguration extends ConfigurationBase implements Serializable {
    public static final String ASYNC_DISPATCHER_IMPL = "async.dispatcherImpl";
    public static final String ASYNC_NUM_THREADS = "async.numThreads";
    public static final String CLIENT_URL = "clientURL";
    public static final String CLIENT_VERSION = "clientVersion";
    public static final String CONTRIBUTING_TO = "contributingTo";
    public static final String DEBUG = "debug";
    public static final String HTTP_CONNECTION_TIMEOUT = "http.connectionTimeout";
    public static final String HTTP_DEFAULT_MAX_PER_ROUTE = "http.defaultMaxPerRoute";
    public static final String HTTP_GZIP = "http.gzip";
    public static final String HTTP_MAX_TOTAL_CONNECTIONS = "http.maxTotalConnections";
    public static final String HTTP_PRETTY_DEBUG = "http.prettyDebug";
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    public static final String HTTP_PROXY_HOST_FALLBACK = "http.proxyHost";
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    public static final String HTTP_PROXY_PORT = "http.proxyPort";
    public static final String HTTP_PROXY_PORT_FALLBACK = "http.proxyPort";
    public static final String HTTP_PROXY_USER = "http.proxyUser";
    public static final String HTTP_READ_TIMEOUT = "http.readTimeout";
    public static final String HTTP_RETRY_COUNT = "http.retryCount";
    public static final String HTTP_RETRY_INTERVAL_SECS = "http.retryIntervalSecs";
    public static final String HTTP_STREAMING_READ_TIMEOUT = "http.streamingReadTimeout";
    public static final String HTTP_USER_AGENT = "http.userAgent";
    public static final String HTTP_USE_SSL = "http.useSSL";
    public static final String INCLUDE_ENTITIES = "includeEntities";
    public static final String INCLUDE_MY_RETWEET = "includeMyRetweet";
    public static final String INCLUDE_RTS = "includeRTs";
    public static final String JSON_STORE_ENABLED = "jsonStoreEnabled";
    public static final String LOGGER_FACTORY = "loggerFactory";
    public static final String MBEAN_ENABLED = "mbeanEnabled";
    public static final String MEDIA_PROVIDER = "media.provider";
    public static final String MEDIA_PROVIDER_API_KEY = "media.providerAPIKey";
    public static final String MEDIA_PROVIDER_PARAMETERS = "media.providerParameters";
    public static final String OAUTH_ACCESS_TOKEN = "oauth.accessToken";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "oauth.accessTokenSecret";
    public static final String OAUTH_ACCESS_TOKEN_URL = "oauth.accessTokenURL";
    public static final String OAUTH_AUTHENTICATION_URL = "oauth.authenticationURL";
    public static final String OAUTH_AUTHORIZATION_URL = "oauth.authorizationURL";
    public static final String OAUTH_CONSUMER_KEY = "oauth.consumerKey";
    public static final String OAUTH_CONSUMER_SECRET = "oauth.consumerSecret";
    public static final String OAUTH_REQUEST_TOKEN_URL = "oauth.requestTokenURL";
    public static final String PASSWORD = "password";
    public static final String REST_BASE_URL = "restBaseURL";
    public static final String SITE_STREAM_BASE_URL = "siteStreamBaseURL";
    public static final String STREAM_BASE_URL = "streamBaseURL";
    public static final String STREAM_STALL_WARNINGS_ENABLED = "stream.enableStallWarnings";
    public static final String STREAM_USER_REPLIES_ALL = "stream.user.repliesAll";
    public static final String USER = "user";
    public static final String USER_STREAM_BASE_URL = "userStreamBaseURL";
    private static final long serialVersionUID = 6458764415636588373L;

    public /* bridge */ /* synthetic */ void dumpConfiguration() {
        super.dumpConfiguration();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object x0) {
        return super.equals(x0);
    }

    public /* bridge */ /* synthetic */ String getDispatcherImpl() {
        return super.getDispatcherImpl();
    }

    public /* bridge */ /* synthetic */ int getHttpStreamingReadTimeout() {
        return super.getHttpStreamingReadTimeout();
    }

    public /* bridge */ /* synthetic */ String getLoggerFactory() {
        return super.getLoggerFactory();
    }

    public /* bridge */ /* synthetic */ String getMediaProvider() {
        return super.getMediaProvider();
    }

    public /* bridge */ /* synthetic */ String getMediaProviderAPIKey() {
        return super.getMediaProviderAPIKey();
    }

    public /* bridge */ /* synthetic */ Properties getMediaProviderParameters() {
        return super.getMediaProviderParameters();
    }

    public /* bridge */ /* synthetic */ String getOAuthAccessToken() {
        return super.getOAuthAccessToken();
    }

    public /* bridge */ /* synthetic */ String getOAuthAccessTokenSecret() {
        return super.getOAuthAccessTokenSecret();
    }

    public /* bridge */ /* synthetic */ String getOAuthAccessTokenURL() {
        return super.getOAuthAccessTokenURL();
    }

    public /* bridge */ /* synthetic */ String getOAuthAuthenticationURL() {
        return super.getOAuthAuthenticationURL();
    }

    public /* bridge */ /* synthetic */ String getOAuthAuthorizationURL() {
        return super.getOAuthAuthorizationURL();
    }

    public /* bridge */ /* synthetic */ String getOAuthRequestTokenURL() {
        return super.getOAuthRequestTokenURL();
    }

    public /* bridge */ /* synthetic */ Map getRequestHeaders() {
        return super.getRequestHeaders();
    }

    public /* bridge */ /* synthetic */ String getRestBaseURL() {
        return super.getRestBaseURL();
    }

    public /* bridge */ /* synthetic */ String getSiteStreamBaseURL() {
        return super.getSiteStreamBaseURL();
    }

    public /* bridge */ /* synthetic */ String getStreamBaseURL() {
        return super.getStreamBaseURL();
    }

    public /* bridge */ /* synthetic */ String getUserStreamBaseURL() {
        return super.getUserStreamBaseURL();
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isGAE() {
        return super.isGAE();
    }

    public /* bridge */ /* synthetic */ boolean isGZIPEnabled() {
        return super.isGZIPEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isIncludeEntitiesEnabled() {
        return super.isIncludeEntitiesEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isIncludeMyRetweetEnabled() {
        return super.isIncludeMyRetweetEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isIncludeRTsEnabled() {
        return super.isIncludeRTsEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isJSONStoreEnabled() {
        return super.isJSONStoreEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isMBeanEnabled() {
        return super.isMBeanEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isPrettyDebugEnabled() {
        return super.isPrettyDebugEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isStallWarningsEnabled() {
        return super.isStallWarningsEnabled();
    }

    public /* bridge */ /* synthetic */ boolean isUserStreamRepliesAllEnabled() {
        return super.isUserStreamRepliesAllEnabled();
    }

    public /* bridge */ /* synthetic */ void setIncludeMyRetweetEnabled(boolean x0) {
        super.setIncludeMyRetweetEnabled(x0);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public PropertyConfiguration(InputStream is) {
        Properties props = new Properties();
        loadProperties(props, is);
        setFieldsWithTreePath(props, "/");
    }

    public PropertyConfiguration(Properties props) {
        this(props, "/");
    }

    public PropertyConfiguration(Properties props, String treePath) {
        setFieldsWithTreePath(props, treePath);
    }

    PropertyConfiguration(String treePath) {
        Properties props;
        try {
            props = (Properties) System.getProperties().clone();
            try {
                Map<String, String> envMap = System.getenv();
                for (String key : envMap.keySet()) {
                    props.setProperty(key, envMap.get(key));
                }
            } catch (SecurityException e) {
            }
            normalize(props);
        } catch (SecurityException e2) {
            props = new Properties();
        }
        loadProperties(props, "." + File.separatorChar + "twitter4j.properties");
        loadProperties(props, Configuration.class.getResourceAsStream("/twitter4j.properties"));
        loadProperties(props, Configuration.class.getResourceAsStream("/WEB-INF/twitter4j.properties"));
        try {
            loadProperties(props, (InputStream) new FileInputStream("WEB-INF/twitter4j.properties"));
        } catch (FileNotFoundException | SecurityException e3) {
        }
        setFieldsWithTreePath(props, treePath);
    }

    PropertyConfiguration() {
        this("/");
    }

    private boolean notNull(Properties props, String prefix, String name) {
        return props.getProperty(new StringBuilder().append(prefix).append(name).toString()) != null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002f A[SYNTHETIC, Splitter:B:20:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0038 A[SYNTHETIC, Splitter:B:25:0x0038] */
    private boolean loadProperties(Properties props, String path) {
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
                return false;
            }
            FileInputStream fis2 = new FileInputStream(file);
            try {
                props.load(fis2);
                normalize(props);
                if (fis2 != null) {
                    try {
                        fis2.close();
                    } catch (IOException e2) {
                    }
                }
                FileInputStream fileInputStream = fis2;
                return true;
            } catch (Exception e3) {
                fis = fis2;
                if (fis != null) {
                }
                return false;
            } catch (Throwable th) {
                th = th;
                fis = fis2;
                if (fis != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e5) {
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    private boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            normalize(props);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void normalize(Properties props) {
        Set<String> keys = props.keySet();
        ArrayList<String> toBeNormalized = new ArrayList<>(10);
        for (String keyStr : keys) {
            if (-1 != keyStr.indexOf("twitter4j.")) {
                toBeNormalized.add(keyStr);
            }
        }
        Iterator i$ = toBeNormalized.iterator();
        while (i$.hasNext()) {
            String keyStr2 = i$.next();
            String property = props.getProperty(keyStr2);
            int index = keyStr2.indexOf("twitter4j.");
            props.setProperty(keyStr2.substring(0, index) + keyStr2.substring(index + 10), property);
        }
    }

    private void setFieldsWithTreePath(Properties props, String treePath) {
        setFieldsWithPrefix(props, "");
        String prefix = null;
        for (String split : z_T4JInternalStringUtil.split(treePath, "/")) {
            if (!"".equals(split)) {
                if (prefix == null) {
                    prefix = split + ".";
                } else {
                    prefix = prefix + split + ".";
                }
                setFieldsWithPrefix(props, prefix);
            }
        }
    }

    private void setFieldsWithPrefix(Properties props, String prefix) {
        if (notNull(props, prefix, DEBUG)) {
            setDebug(getBoolean(props, prefix, DEBUG));
        }
        if (notNull(props, prefix, USER)) {
            setUser(getString(props, prefix, USER));
        }
        if (notNull(props, prefix, PASSWORD)) {
            setPassword(getString(props, prefix, PASSWORD));
        }
        if (notNull(props, prefix, HTTP_USE_SSL)) {
            setUseSSL(getBoolean(props, prefix, HTTP_USE_SSL));
        }
        if (notNull(props, prefix, HTTP_PRETTY_DEBUG)) {
            setPrettyDebugEnabled(getBoolean(props, prefix, HTTP_PRETTY_DEBUG));
        }
        if (notNull(props, prefix, HTTP_GZIP)) {
            setGZIPEnabled(getBoolean(props, prefix, HTTP_GZIP));
        }
        if (notNull(props, prefix, "http.proxyHost")) {
            setHttpProxyHost(getString(props, prefix, "http.proxyHost"));
        } else if (notNull(props, prefix, "http.proxyHost")) {
            setHttpProxyHost(getString(props, prefix, "http.proxyHost"));
        }
        if (notNull(props, prefix, HTTP_PROXY_USER)) {
            setHttpProxyUser(getString(props, prefix, HTTP_PROXY_USER));
        }
        if (notNull(props, prefix, HTTP_PROXY_PASSWORD)) {
            setHttpProxyPassword(getString(props, prefix, HTTP_PROXY_PASSWORD));
        }
        if (notNull(props, prefix, "http.proxyPort")) {
            setHttpProxyPort(getIntProperty(props, prefix, "http.proxyPort"));
        } else if (notNull(props, prefix, "http.proxyPort")) {
            setHttpProxyPort(getIntProperty(props, prefix, "http.proxyPort"));
        }
        if (notNull(props, prefix, HTTP_CONNECTION_TIMEOUT)) {
            setHttpConnectionTimeout(getIntProperty(props, prefix, HTTP_CONNECTION_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_READ_TIMEOUT)) {
            setHttpReadTimeout(getIntProperty(props, prefix, HTTP_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_STREAMING_READ_TIMEOUT)) {
            setHttpStreamingReadTimeout(getIntProperty(props, prefix, HTTP_STREAMING_READ_TIMEOUT));
        }
        if (notNull(props, prefix, HTTP_RETRY_COUNT)) {
            setHttpRetryCount(getIntProperty(props, prefix, HTTP_RETRY_COUNT));
        }
        if (notNull(props, prefix, HTTP_RETRY_INTERVAL_SECS)) {
            setHttpRetryIntervalSeconds(getIntProperty(props, prefix, HTTP_RETRY_INTERVAL_SECS));
        }
        if (notNull(props, prefix, HTTP_MAX_TOTAL_CONNECTIONS)) {
            setHttpMaxTotalConnections(getIntProperty(props, prefix, HTTP_MAX_TOTAL_CONNECTIONS));
        }
        if (notNull(props, prefix, HTTP_DEFAULT_MAX_PER_ROUTE)) {
            setHttpDefaultMaxPerRoute(getIntProperty(props, prefix, HTTP_DEFAULT_MAX_PER_ROUTE));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_KEY)) {
            setOAuthConsumerKey(getString(props, prefix, OAUTH_CONSUMER_KEY));
        }
        if (notNull(props, prefix, OAUTH_CONSUMER_SECRET)) {
            setOAuthConsumerSecret(getString(props, prefix, OAUTH_CONSUMER_SECRET));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN)) {
            setOAuthAccessToken(getString(props, prefix, OAUTH_ACCESS_TOKEN));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_SECRET)) {
            setOAuthAccessTokenSecret(getString(props, prefix, OAUTH_ACCESS_TOKEN_SECRET));
        }
        if (notNull(props, prefix, ASYNC_NUM_THREADS)) {
            setAsyncNumThreads(getIntProperty(props, prefix, ASYNC_NUM_THREADS));
        }
        if (notNull(props, prefix, CONTRIBUTING_TO)) {
            setContributingTo(getLongProperty(props, prefix, CONTRIBUTING_TO));
        }
        if (notNull(props, prefix, ASYNC_DISPATCHER_IMPL)) {
            setDispatcherImpl(getString(props, prefix, ASYNC_DISPATCHER_IMPL));
        }
        if (notNull(props, prefix, CLIENT_VERSION)) {
            setClientVersion(getString(props, prefix, CLIENT_VERSION));
        }
        if (notNull(props, prefix, CLIENT_URL)) {
            setClientURL(getString(props, prefix, CLIENT_URL));
        }
        if (notNull(props, prefix, HTTP_USER_AGENT)) {
            setUserAgent(getString(props, prefix, HTTP_USER_AGENT));
        }
        if (notNull(props, prefix, OAUTH_REQUEST_TOKEN_URL)) {
            setOAuthRequestTokenURL(getString(props, prefix, OAUTH_REQUEST_TOKEN_URL));
        }
        if (notNull(props, prefix, OAUTH_AUTHORIZATION_URL)) {
            setOAuthAuthorizationURL(getString(props, prefix, OAUTH_AUTHORIZATION_URL));
        }
        if (notNull(props, prefix, OAUTH_ACCESS_TOKEN_URL)) {
            setOAuthAccessTokenURL(getString(props, prefix, OAUTH_ACCESS_TOKEN_URL));
        }
        if (notNull(props, prefix, OAUTH_AUTHENTICATION_URL)) {
            setOAuthAuthenticationURL(getString(props, prefix, OAUTH_AUTHENTICATION_URL));
        }
        if (notNull(props, prefix, REST_BASE_URL)) {
            setRestBaseURL(getString(props, prefix, REST_BASE_URL));
        }
        if (notNull(props, prefix, STREAM_BASE_URL)) {
            setStreamBaseURL(getString(props, prefix, STREAM_BASE_URL));
        }
        if (notNull(props, prefix, USER_STREAM_BASE_URL)) {
            setUserStreamBaseURL(getString(props, prefix, USER_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, SITE_STREAM_BASE_URL)) {
            setSiteStreamBaseURL(getString(props, prefix, SITE_STREAM_BASE_URL));
        }
        if (notNull(props, prefix, INCLUDE_RTS)) {
            setIncludeRTsEnbled(getBoolean(props, prefix, INCLUDE_RTS));
        }
        if (notNull(props, prefix, INCLUDE_ENTITIES)) {
            setIncludeEntitiesEnbled(getBoolean(props, prefix, INCLUDE_ENTITIES));
        }
        if (notNull(props, prefix, INCLUDE_MY_RETWEET)) {
            setIncludeMyRetweetEnabled(getBoolean(props, prefix, INCLUDE_MY_RETWEET));
        }
        if (notNull(props, prefix, LOGGER_FACTORY)) {
            setLoggerFactory(getString(props, prefix, LOGGER_FACTORY));
        }
        if (notNull(props, prefix, JSON_STORE_ENABLED)) {
            setJSONStoreEnabled(getBoolean(props, prefix, JSON_STORE_ENABLED));
        }
        if (notNull(props, prefix, MBEAN_ENABLED)) {
            setMBeanEnabled(getBoolean(props, prefix, MBEAN_ENABLED));
        }
        if (notNull(props, prefix, STREAM_USER_REPLIES_ALL)) {
            setUserStreamRepliesAllEnabled(getBoolean(props, prefix, STREAM_USER_REPLIES_ALL));
        }
        if (notNull(props, prefix, STREAM_STALL_WARNINGS_ENABLED)) {
            setStallWarningsEnabled(getBoolean(props, prefix, STREAM_STALL_WARNINGS_ENABLED));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER)) {
            setMediaProvider(getString(props, prefix, MEDIA_PROVIDER));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_API_KEY)) {
            setMediaProviderAPIKey(getString(props, prefix, MEDIA_PROVIDER_API_KEY));
        }
        if (notNull(props, prefix, MEDIA_PROVIDER_PARAMETERS)) {
            String[] propsAry = z_T4JInternalStringUtil.split(getString(props, prefix, MEDIA_PROVIDER_PARAMETERS), "&");
            Properties p = new Properties();
            for (String str : propsAry) {
                String[] parameter = z_T4JInternalStringUtil.split(str, "=");
                p.setProperty(parameter[0], parameter[1]);
            }
            setMediaProviderParameters(p);
        }
        cacheInstance();
    }

    /* access modifiers changed from: protected */
    public boolean getBoolean(Properties props, String prefix, String name) {
        return Boolean.valueOf(props.getProperty(prefix + name)).booleanValue();
    }

    /* access modifiers changed from: protected */
    public int getIntProperty(Properties props, String prefix, String name) {
        try {
            return Integer.parseInt(props.getProperty(prefix + name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public long getLongProperty(Properties props, String prefix, String name) {
        try {
            return Long.parseLong(props.getProperty(prefix + name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public String getString(Properties props, String prefix, String name) {
        return props.getProperty(prefix + name);
    }

    /* access modifiers changed from: protected */
    public Object readResolve() throws ObjectStreamException {
        return super.readResolve();
    }
}
