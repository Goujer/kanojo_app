package jp.co.cybird.barcodekanojoForGAM.core.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import jp.co.cybird.barcodekanojoForGAM.core.parser.AbstractJSONParser;
import jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.james.mime4j.field.ContentTypeField;

public class HttpApi {
    private static final String BOUNDARY = "0xKhTmLbOuNdArY";
    private static final String CLIENT_LANGUAGE_HEADER = "Accept-Language";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final boolean DEBUG = false;
    private static final String DEFAULT_CLIENT_LANGUAGE = "en";
    private static final String DEFAULT_CLIENT_VERSION = "jp.co.cybrid.barcodekanojo";
    private static final String TAG = "HttpApi";
    private static final int TIMEOUT = 30;
    private final String mClientLanguage;
    private final String mClientVersion;
    private final DefaultHttpClient mHttpClient;

    public HttpApi(DefaultHttpClient httpClient, String clientVersion, String clientLanguage) {
        this.mHttpClient = httpClient;
        if (clientVersion != null) {
            this.mClientVersion = clientVersion;
        } else {
            this.mClientVersion = DEFAULT_CLIENT_VERSION;
        }
        if (clientLanguage != null) {
            this.mClientLanguage = clientLanguage;
        } else {
            this.mClientLanguage = DEFAULT_CLIENT_LANGUAGE;
        }
    }

    /* JADX WARNING: type inference failed for: r11v0, types: [jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser<? extends jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel>, jp.co.cybird.barcodekanojoForGAM.core.parser.JSONParser] */
    /* JADX WARNING: Unknown variable types count: 1 */
    public BarcodeKanojoModel executeHttpRequest(HttpRequestBase httpRequest, JSONParser<? extends BarcodeKanojoModel> r11) throws BarcodeKanojoException, IllegalStateException, IOException {
        HttpResponse response = executeHttpRequest(httpRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200:
                InputStream is = response.getEntity().getContent();
                try {
                    return r11.parse(AbstractJSONParser.createJSONObject(is));
                } finally {
                    is.close();
                }
            case 400:
                throw new BarcodeKanojoException(response.getStatusLine().toString());
            case 401:
                response.getEntity().consumeContent();
                break;
            case 404:
                break;
            case 500:
                response.getEntity().consumeContent();
                do {
                } while (new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine() != null);
                throw new BarcodeKanojoException("Server is down. Try again later.");
            default:
                response.getEntity().consumeContent();
                throw new BarcodeKanojoException("Error connecting to Server: " + statusCode + ". Try again later.");
        }
        response.getEntity().consumeContent();
        throw new BarcodeKanojoException(response.getStatusLine().toString());
    }

    public String doHttpPost(String url, NameValuePair... nameValuePairs) throws BarcodeKanojoException, IOException {
        HttpResponse response = executeHttpRequest(createHttpPost(url, nameValuePairs));
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                try {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e) {
                    throw new BarcodeKanojoException(e.getMessage());
                }
            case 401:
                response.getEntity().consumeContent();
                throw new BarcodeKanojoException(response.getStatusLine().toString());
            case 404:
                response.getEntity().consumeContent();
                throw new BarcodeKanojoException(response.getStatusLine().toString());
            default:
                response.getEntity().consumeContent();
                throw new BarcodeKanojoException(response.getStatusLine().toString());
        }
    }

    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        try {
            this.mHttpClient.getConnectionManager().closeExpiredConnections();
            return this.mHttpClient.execute(httpRequest);
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        }
    }

    public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
        HttpGet httpGet;
        if (nameValuePairs == null || nameValuePairs.length == 0) {
            httpGet = new HttpGet(url);
        } else {
            httpGet = new HttpGet(String.valueOf(url) + "?" + URLEncodedUtils.format(stripNulls(nameValuePairs), "UTF-8"));
        }
        httpGet.addHeader("User-Agent", this.mClientVersion);
        httpGet.addHeader(CLIENT_LANGUAGE_HEADER, this.mClientLanguage);
        return httpGet;
    }

    public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("User-Agent", this.mClientVersion);
        httpPost.addHeader(CLIENT_LANGUAGE_HEADER, this.mClientLanguage);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), "UTF-8"));
            return httpPost;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
    }

    public HttpPost createHttpMultipartPost(String url, NameValueOrFilePair... namveValueOrFilePairs) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("User-Agent", this.mClientVersion);
        httpPost.addHeader(CLIENT_LANGUAGE_HEADER, this.mClientLanguage);
        try {
            httpPost.setEntity(createMultipartEntity(namveValueOrFilePairs));
            return httpPost;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
    }

    public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(30000);
        conn.setRequestMethod(DownloadHelper.REQUEST_METHOD_POST);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        return conn;
    }

    private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
        List<NameValuePair> params = new ArrayList<>();
        for (NameValuePair param : nameValuePairs) {
            if (param.getValue() != null) {
                params.add(param);
            }
        }
        return params;
    }

    private MultipartEntity createMultipartEntity(NameValueOrFilePair... nameValueOrFilePairs) throws UnsupportedEncodingException {
        File file;
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY, (Charset) null);
        for (NameValueOrFilePair param : nameValueOrFilePairs) {
            if (param != null && param.getValue() != null) {
                entity.addPart(param.getName(), new StringBody(param.getValue(), ContentTypeField.TYPE_TEXT_PLAIN, Charset.forName("UTF-8")));
            } else if (!(param == null || (file = param.getFile()) == null || !file.exists())) {
                entity.addPart(param.getName(), new FileBody(file, "application/octet-stream"));
            }
        }
        return entity;
    }

    public static final DefaultHttpClient createHttpClient() {
        SchemeRegistry supportedSchemes = new SchemeRegistry();
        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);
        return new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, supportedSchemes), httpParams);
    }

    private static final HttpParams createHttpParams() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 30000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        return params;
    }
}
