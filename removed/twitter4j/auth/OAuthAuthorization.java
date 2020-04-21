package twitter4j.auth;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.BASE64Encoder;
import twitter4j.internal.http.HttpClientWrapper;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpRequest;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

public class OAuthAuthorization implements Authorization, Serializable, OAuthSupport {
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final HttpParameter OAUTH_SIGNATURE_METHOD = new HttpParameter("oauth_signature_method", "HMAC-SHA1");
    private static Random RAND = new Random();
    private static transient HttpClientWrapper http = null;
    private static final Logger logger = Logger.getLogger(OAuthAuthorization.class);
    private static final long serialVersionUID = -4368426677157998618L;
    private final Configuration conf;
    private String consumerKey = "";
    private String consumerSecret;
    private OAuthToken oauthToken = null;
    private String realm = null;

    public OAuthAuthorization(Configuration conf2) {
        this.conf = conf2;
        http = new HttpClientWrapper(conf2);
        setOAuthConsumer(conf2.getOAuthConsumerKey(), conf2.getOAuthConsumerSecret());
        if (conf2.getOAuthAccessToken() != null && conf2.getOAuthAccessTokenSecret() != null) {
            setOAuthAccessToken(new AccessToken(conf2.getOAuthAccessToken(), conf2.getOAuthAccessTokenSecret()));
        }
    }

    public String getAuthorizationHeader(HttpRequest req) {
        return generateAuthorizationHeader(req.getMethod().name(), req.getURL(), req.getParameters(), this.oauthToken);
    }

    private void ensureTokenIsAvailable() {
        if (this.oauthToken == null) {
            throw new IllegalStateException("No Token available.");
        }
    }

    public boolean isEnabled() {
        return this.oauthToken != null && (this.oauthToken instanceof AccessToken);
    }

    public RequestToken getOAuthRequestToken() throws TwitterException {
        return getOAuthRequestToken((String) null, (String) null);
    }

    public RequestToken getOAuthRequestToken(String callbackURL) throws TwitterException {
        return getOAuthRequestToken(callbackURL, (String) null);
    }

    public RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType) throws TwitterException {
        if (this.oauthToken instanceof AccessToken) {
            throw new IllegalStateException("Access token already available.");
        }
        List<HttpParameter> params = new ArrayList<>();
        if (callbackURL != null) {
            params.add(new HttpParameter("oauth_callback", callbackURL));
        }
        if (xAuthAccessType != null) {
            params.add(new HttpParameter("x_auth_access_type", xAuthAccessType));
        }
        this.oauthToken = new RequestToken(http.post(this.conf.getOAuthRequestTokenURL(), (HttpParameter[]) params.toArray(new HttpParameter[params.size()]), (Authorization) this), (OAuthSupport) this);
        return (RequestToken) this.oauthToken;
    }

    public AccessToken getOAuthAccessToken() throws TwitterException {
        ensureTokenIsAvailable();
        if (this.oauthToken instanceof AccessToken) {
            return (AccessToken) this.oauthToken;
        }
        this.oauthToken = new AccessToken(http.post(this.conf.getOAuthAccessTokenURL(), (Authorization) this));
        return (AccessToken) this.oauthToken;
    }

    public AccessToken getOAuthAccessToken(String oauthVerifier) throws TwitterException {
        ensureTokenIsAvailable();
        this.oauthToken = new AccessToken(http.post(this.conf.getOAuthAccessTokenURL(), new HttpParameter[]{new HttpParameter("oauth_verifier", oauthVerifier)}, (Authorization) this));
        return (AccessToken) this.oauthToken;
    }

    public AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken();
    }

    public AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws TwitterException {
        this.oauthToken = requestToken;
        return getOAuthAccessToken(oauthVerifier);
    }

    public AccessToken getOAuthAccessToken(String screenName, String password) throws TwitterException {
        try {
            String url = this.conf.getOAuthAccessTokenURL();
            if (url.indexOf("http://") == 0) {
                url = "https://" + url.substring(7);
            }
            this.oauthToken = new AccessToken(http.post(url, new HttpParameter[]{new HttpParameter("x_auth_username", screenName), new HttpParameter("x_auth_password", password), new HttpParameter("x_auth_mode", "client_auth")}, (Authorization) this));
            return (AccessToken) this.oauthToken;
        } catch (TwitterException te) {
            throw new TwitterException("The screen name / password combination seems to be invalid.", te, te.getStatusCode());
        }
    }

    public void setOAuthAccessToken(AccessToken accessToken) {
        this.oauthToken = accessToken;
    }

    public void setOAuthRealm(String realm2) {
        this.realm = realm2;
    }

    /* access modifiers changed from: package-private */
    public String generateAuthorizationHeader(String method, String url, HttpParameter[] params, String nonce, String timestamp, OAuthToken otoken) {
        if (params == null) {
            params = new HttpParameter[0];
        }
        List<HttpParameter> oauthHeaderParams = new ArrayList<>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", this.consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (otoken != null) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", otoken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<>(oauthHeaderParams.size() + params.length);
        signatureBaseParams.addAll(oauthHeaderParams);
        if (!HttpParameter.containsFile(params)) {
            signatureBaseParams.addAll(toParamList(params));
        }
        parseGetParameters(url, signatureBaseParams);
        StringBuilder base = new StringBuilder(method).append("&").append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));
        String oauthBaseString = base.toString();
        logger.debug("OAuth base string: ", oauthBaseString);
        String signature = generateSignature(oauthBaseString, otoken);
        logger.debug("OAuth signature: ", signature);
        oauthHeaderParams.add(new HttpParameter("oauth_signature", signature));
        if (this.realm != null) {
            oauthHeaderParams.add(new HttpParameter("realm", this.realm));
        }
        return "OAuth " + encodeParameters(oauthHeaderParams, ",", true);
    }

    private void parseGetParameters(String url, List<HttpParameter> signatureBaseParams) {
        int queryStart = url.indexOf("?");
        if (-1 != queryStart) {
            try {
                for (String query : z_T4JInternalStringUtil.split(url.substring(queryStart + 1), "&")) {
                    String[] split = z_T4JInternalStringUtil.split(query, "=");
                    if (split.length == 2) {
                        signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], "UTF-8"), URLDecoder.decode(split[1], "UTF-8")));
                    } else {
                        signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], "UTF-8"), ""));
                    }
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String generateAuthorizationHeader(String method, String url, HttpParameter[] params, OAuthToken token) {
        long timestamp = System.currentTimeMillis() / 1000;
        return generateAuthorizationHeader(method, url, params, String.valueOf(timestamp + ((long) RAND.nextInt())), String.valueOf(timestamp), token);
    }

    public List<HttpParameter> generateOAuthSignatureHttpParams(String method, String url) {
        long timestamp = System.currentTimeMillis() / 1000;
        long nonce = timestamp + ((long) RAND.nextInt());
        List<HttpParameter> oauthHeaderParams = new ArrayList<>(5);
        oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", this.consumerKey));
        oauthHeaderParams.add(OAUTH_SIGNATURE_METHOD);
        oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
        oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
        oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));
        if (this.oauthToken != null) {
            oauthHeaderParams.add(new HttpParameter("oauth_token", this.oauthToken.getToken()));
        }
        List<HttpParameter> signatureBaseParams = new ArrayList<>(oauthHeaderParams.size());
        signatureBaseParams.addAll(oauthHeaderParams);
        parseGetParameters(url, signatureBaseParams);
        StringBuilder base = new StringBuilder(method).append("&").append(HttpParameter.encode(constructRequestURL(url))).append("&");
        base.append(HttpParameter.encode(normalizeRequestParameters(signatureBaseParams)));
        oauthHeaderParams.add(new HttpParameter("oauth_signature", generateSignature(base.toString(), this.oauthToken)));
        return oauthHeaderParams;
    }

    /* access modifiers changed from: package-private */
    public String generateSignature(String data, OAuthToken token) {
        SecretKeySpec spec;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            if (token == null) {
                spec = new SecretKeySpec((HttpParameter.encode(this.consumerSecret) + "&").getBytes(), HMAC_SHA1);
            } else {
                spec = token.getSecretKeySpec();
                if (spec == null) {
                    spec = new SecretKeySpec((HttpParameter.encode(this.consumerSecret) + "&" + HttpParameter.encode(token.getTokenSecret())).getBytes(), HMAC_SHA1);
                    token.setSecretKeySpec(spec);
                }
            }
            mac.init(spec);
            return BASE64Encoder.encode(mac.doFinal(data.getBytes()));
        } catch (InvalidKeyException ike) {
            logger.error("Failed initialize \"Message Authentication Code\" (MAC)", ike);
            throw new AssertionError(ike);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Failed to get HmacSHA1 \"Message Authentication Code\" (MAC)", nsae);
            throw new AssertionError(nsae);
        }
    }

    /* access modifiers changed from: package-private */
    public String generateSignature(String data) {
        return generateSignature(data, (OAuthToken) null);
    }

    static String normalizeRequestParameters(HttpParameter[] params) {
        return normalizeRequestParameters(toParamList(params));
    }

    static String normalizeRequestParameters(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    static String normalizeAuthorizationHeaders(List<HttpParameter> params) {
        Collections.sort(params);
        return encodeParameters(params);
    }

    static List<HttpParameter> toParamList(HttpParameter[] params) {
        List<HttpParameter> paramList = new ArrayList<>(params.length);
        paramList.addAll(Arrays.asList(params));
        return paramList;
    }

    public static String encodeParameters(List<HttpParameter> httpParams) {
        return encodeParameters(httpParams, "&", false);
    }

    public static String encodeParameters(List<HttpParameter> httpParams, String splitter, boolean quot) {
        StringBuilder buf = new StringBuilder();
        for (HttpParameter param : httpParams) {
            if (!param.isFile()) {
                if (buf.length() != 0) {
                    if (quot) {
                        buf.append("\"");
                    }
                    buf.append(splitter);
                }
                buf.append(HttpParameter.encode(param.getName())).append("=");
                if (quot) {
                    buf.append("\"");
                }
                buf.append(HttpParameter.encode(param.getValue()));
            }
        }
        if (buf.length() != 0 && quot) {
            buf.append("\"");
        }
        return buf.toString();
    }

    static String constructRequestURL(String url) {
        int index = url.indexOf("?");
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        return baseURL + url.substring(slashIndex);
    }

    public void setOAuthConsumer(String consumerKey2, String consumerSecret2) {
        if (consumerKey2 == null) {
            consumerKey2 = "";
        }
        this.consumerKey = consumerKey2;
        if (consumerSecret2 == null) {
            consumerSecret2 = "";
        }
        this.consumerSecret = consumerSecret2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OAuthSupport)) {
            return false;
        }
        OAuthAuthorization that = (OAuthAuthorization) o;
        if (this.consumerKey == null ? that.consumerKey != null : !this.consumerKey.equals(that.consumerKey)) {
            return false;
        }
        if (this.consumerSecret == null ? that.consumerSecret != null : !this.consumerSecret.equals(that.consumerSecret)) {
            return false;
        }
        if (this.oauthToken != null) {
            if (this.oauthToken.equals(that.oauthToken)) {
                return true;
            }
        } else if (that.oauthToken == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.consumerKey != null) {
            result = this.consumerKey.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.consumerSecret != null) {
            i = this.consumerSecret.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.oauthToken != null) {
            i2 = this.oauthToken.hashCode();
        }
        return i4 + i2;
    }

    public String toString() {
        return "OAuthAuthorization{consumerKey='" + this.consumerKey + '\'' + ", consumerSecret='******************************************'" + ", oauthToken=" + this.oauthToken + '}';
    }
}
