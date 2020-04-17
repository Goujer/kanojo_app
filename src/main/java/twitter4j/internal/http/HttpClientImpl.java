package twitter4j.internal.http;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.james.mime4j.util.CharsetUtil;
import twitter4j.TwitterException;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.internal.util.z_T4JInternalStringUtil;

public class HttpClientImpl extends HttpClientBase implements HttpResponseCode, Serializable {
    private static final Map<HttpClientConfiguration, HttpClient> instanceMap = new HashMap(1);
    private static boolean isJDK14orEarlier = false;
    private static final Logger logger = Logger.getLogger(HttpClientImpl.class);
    private static final long serialVersionUID = -8819171414069621503L;

    static {
        boolean z = false;
        isJDK14orEarlier = false;
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (versionStr != null) {
                if (1.5d > Double.parseDouble(versionStr)) {
                    z = true;
                }
                isJDK14orEarlier = z;
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                isJDK14orEarlier = false;
                System.setProperty("http.keepAlive", "false");
            }
        } catch (SecurityException e) {
            isJDK14orEarlier = true;
        }
    }

    public HttpClientImpl() {
        super(ConfigurationContext.getInstance());
    }

    public HttpClientImpl(HttpClientConfiguration conf) {
        super(conf);
        if (isProxyConfigured() && isJDK14orEarlier) {
            logger.warn("HTTP Proxy is not supported on JDK1.4 or earlier. Try twitter4j-httpclient-supoprt artifact");
        }
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        HttpClient client = instanceMap.get(conf);
        if (client != null) {
            return client;
        }
        HttpClient client2 = new HttpClientImpl(conf);
        instanceMap.put(conf, client2);
        return client2;
    }

    public HttpResponse get(String url) throws TwitterException {
        return request(new HttpRequest(RequestMethod.GET, url, (HttpParameter[]) null, (Authorization) null, (Map<String, String>) null));
    }

    public HttpResponse post(String url, HttpParameter[] params) throws TwitterException {
        return request(new HttpRequest(RequestMethod.POST, url, params, (Authorization) null, (Map<String, String>) null));
    }

    public HttpResponse request(HttpRequest req) throws TwitterException {
        int retry = this.CONF.getHttpRetryCount() + 1;
        HttpResponseImpl httpResponseImpl = null;
        int retriedCount = 0;
        while (true) {
            HttpResponse res = httpResponseImpl;
            if (retriedCount >= retry) {
                return res;
            }
            int responseCode = -1;
            OutputStream os = null;
            try {
                HttpURLConnection con = getConnection(req.getURL());
                con.setDoInput(true);
                setHeaders(req, con);
                con.setRequestMethod(req.getMethod().name());
                if (req.getMethod() == RequestMethod.POST) {
                    if (HttpParameter.containsFile(req.getParameters())) {
                        String boundary = "----Twitter4J-upload" + System.currentTimeMillis();
                        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                        String boundary2 = "--" + boundary;
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(os);
                        for (HttpParameter param : req.getParameters()) {
                            if (param.isFile()) {
                                write(dataOutputStream, boundary2 + CharsetUtil.CRLF);
                                write(dataOutputStream, "Content-Disposition: form-data; name=\"" + param.getName() + "\"; filename=\"" + param.getFile().getName() + "\"\r\n");
                                write(dataOutputStream, "Content-Type: " + param.getContentType() + "\r\n\r\n");
                                BufferedInputStream in = new BufferedInputStream(param.hasFileBody() ? param.getFileBody() : new FileInputStream(param.getFile()));
                                byte[] buff = new byte[1024];
                                while (true) {
                                    int length = in.read(buff);
                                    if (length == -1) {
                                        break;
                                    }
                                    dataOutputStream.write(buff, 0, length);
                                }
                                write(dataOutputStream, CharsetUtil.CRLF);
                                in.close();
                            } else {
                                write(dataOutputStream, boundary2 + CharsetUtil.CRLF);
                                write(dataOutputStream, "Content-Disposition: form-data; name=\"" + param.getName() + "\"\r\n");
                                write(dataOutputStream, "Content-Type: text/plain; charset=UTF-8\r\n\r\n");
                                logger.debug(param.getValue());
                                dataOutputStream.write(param.getValue().getBytes("UTF-8"));
                                write(dataOutputStream, CharsetUtil.CRLF);
                            }
                        }
                        write(dataOutputStream, boundary2 + "--\r\n");
                        write(dataOutputStream, CharsetUtil.CRLF);
                    } else {
                        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        String postParam = HttpParameter.encodeParameters(req.getParameters());
                        logger.debug("Post Params: ", postParam);
                        byte[] bytes = postParam.getBytes("UTF-8");
                        con.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                        con.setDoOutput(true);
                        os = con.getOutputStream();
                        os.write(bytes);
                    }
                    os.flush();
                    os.close();
                }
                httpResponseImpl = new HttpResponseImpl(con, this.CONF);
                try {
                    responseCode = con.getResponseCode();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response: ");
                        Map<String, List<String>> responseHeaders = con.getHeaderFields();
                        for (String key : responseHeaders.keySet()) {
                            for (String value : responseHeaders.get(key)) {
                                if (key != null) {
                                    logger.debug(key + ": " + value);
                                } else {
                                    logger.debug(value);
                                }
                            }
                        }
                    }
                    if (responseCode >= 200 && (responseCode == 302 || 300 > responseCode)) {
                        try {
                            os.close();
                            return httpResponseImpl;
                        } catch (Exception e) {
                            return httpResponseImpl;
                        }
                    } else if (responseCode != 420 && responseCode != 400 && responseCode >= 500 && retriedCount != this.CONF.getHttpRetryCount()) {
                        try {
                            os.close();
                        } catch (Exception e2) {
                        }
                        try {
                            if (logger.isDebugEnabled() && httpResponseImpl != null) {
                                httpResponseImpl.asString();
                            }
                            logger.debug("Sleeping " + this.CONF.getHttpRetryIntervalSeconds() + " seconds until the next retry.");
                            Thread.sleep((long) (this.CONF.getHttpRetryIntervalSeconds() * 1000));
                        } catch (InterruptedException e3) {
                        }
                        retriedCount++;
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        os.close();
                    } catch (Exception e4) {
                    }
                    try {
                        throw th;
                    } catch (IOException ioe) {
                        if (retriedCount == this.CONF.getHttpRetryCount()) {
                            throw new TwitterException(ioe.getMessage(), ioe, responseCode);
                        }
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                httpResponseImpl = res;
            }
        }
        throw new TwitterException(httpResponseImpl.asString(), (HttpResponse) httpResponseImpl);
    }

    private void setHeaders(HttpRequest req, HttpURLConnection connection) {
        String authorizationHeader;
        if (logger.isDebugEnabled()) {
            logger.debug("Request: ");
            logger.debug(req.getMethod().name() + " ", req.getURL());
        }
        if (!(req.getAuthorization() == null || (authorizationHeader = req.getAuthorization().getAuthorizationHeader(req)) == null)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization: ", z_T4JInternalStringUtil.maskString(authorizationHeader));
            }
            connection.addRequestProperty("Authorization", authorizationHeader);
        }
        if (req.getRequestHeaders() != null) {
            for (String key : req.getRequestHeaders().keySet()) {
                connection.addRequestProperty(key, req.getRequestHeaders().get(key));
                logger.debug(key + ": " + req.getRequestHeaders().get(key));
            }
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con;
        if (!isProxyConfigured() || isJDK14orEarlier) {
            con = (HttpURLConnection) new URL(url).openConnection();
        } else {
            if (this.CONF.getHttpProxyUser() != null && !this.CONF.getHttpProxyUser().equals("")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Proxy AuthUser: " + this.CONF.getHttpProxyUser());
                    logger.debug("Proxy AuthPassword: " + z_T4JInternalStringUtil.maskString(this.CONF.getHttpProxyPassword()));
                }
                Authenticator.setDefault(new Authenticator() {
                    /* access modifiers changed from: protected */
                    public PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType().equals(Authenticator.RequestorType.PROXY)) {
                            return new PasswordAuthentication(HttpClientImpl.this.CONF.getHttpProxyUser(), HttpClientImpl.this.CONF.getHttpProxyPassword().toCharArray());
                        }
                        return null;
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(this.CONF.getHttpProxyHost(), this.CONF.getHttpProxyPort()));
            if (logger.isDebugEnabled()) {
                logger.debug("Opening proxied connection(" + this.CONF.getHttpProxyHost() + ":" + this.CONF.getHttpProxyPort() + ")");
            }
            con = (HttpURLConnection) new URL(url).openConnection(proxy);
        }
        if (this.CONF.getHttpConnectionTimeout() > 0 && !isJDK14orEarlier) {
            con.setConnectTimeout(this.CONF.getHttpConnectionTimeout());
        }
        if (this.CONF.getHttpReadTimeout() > 0 && !isJDK14orEarlier) {
            con.setReadTimeout(this.CONF.getHttpReadTimeout());
        }
        con.setInstanceFollowRedirects(false);
        return con;
    }
}
