package jp.co.cybird.app.android.lib.commons.file;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import org.apache.http.HttpException;

public class DownloadHelper {
    private static final String MSG_NOT_SET_DOWNLOAD_URL = "Not set download URL.";
    private static final String REGULAR_EXPRESSION = File.separator;
    public static final String REQUEST_HEADER_USERAGENT = "User-Agent";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    static final String TAG = "DownloadHelper";
    private URLConnection mConn;
    private Context mContext;
    private String mFileName;
    private String mFilePath;
    private String mRequestMethod = REQUEST_METHOD_GET;
    private Map<String, String> mRequestProperty;
    private SaveDir mSaveDir = SaveDir.SAVE_DIR_APP;
    private URL mURL;
    private String mUserAgent;

    public enum SaveDir {
        SAVE_DIR_APP,
        SAVE_DIR_EXTERNAL
    }

    public void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
    }

    public DownloadHelper(Context context) {
        this.mContext = context;
    }

    public void setUrl(String url) throws MalformedURLException {
        this.mURL = new URL(url);
    }

    public URL getUrl() {
        return this.mURL;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
        String[] splitPath = filePath.split(REGULAR_EXPRESSION);
        setFileName(splitPath[splitPath.length - 1]);
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public void setSaveDir(SaveDir saveDir) {
        this.mSaveDir = saveDir;
    }

    public void setRequestMethod(String method) {
        if (method == null) {
            return;
        }
        if (method.equals(REQUEST_METHOD_GET) || method.equals(REQUEST_METHOD_POST)) {
            this.mRequestMethod = method;
        }
    }

    public void addRequestProperty(String field, String value) {
        if (this.mRequestProperty == null) {
            this.mRequestProperty = new HashMap();
        }
        this.mRequestProperty.put(field, value);
    }

    public void download() throws IOException, HttpException {
        if (this.mURL == null) {
            throw new DownloadHelperException(MSG_NOT_SET_DOWNLOAD_URL);
        }
        if (this.mConn == null) {
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Download().execute();
    }

    private void connect() throws IOException {
        this.mConn = this.mURL.openConnection();
        this.mConn.setAllowUserInteraction(false);
    }

    public void cancel() {
        ((HttpURLConnection) this.mConn).disconnect();
    }

    class Download {
        Download() {
        }

        private void execute() {
            DLog.d("DHP", "Download#execute()");
            HttpURLConnection httpURLConn = (HttpURLConnection) DownloadHelper.this.mConn;
            try {
                httpURLConn.setInstanceFollowRedirects(true);
                httpURLConn.setRequestMethod(DownloadHelper.this.mRequestMethod);
                httpURLConn.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, DownloadHelper.this.mUserAgent);
                httpURLConn.connect();
                int resCode = httpURLConn.getResponseCode();
                DLog.d("CAC", "HTTP response code:".concat(String.valueOf(resCode)));
                if (resCode != 200) {
                    throw new HttpException();
                }
                InputStream is = httpURLConn.getInputStream();
                if (is == null) {
                    DLog.i("CAC", "Http response InputStream is null");
                }
                if (SaveDir.SAVE_DIR_APP.equals(DownloadHelper.this.mSaveDir)) {
                    FileUtil.saveFileInAppDirectory(DownloadHelper.this.mContext, is, DownloadHelper.this.mFileName);
                } else if (SaveDir.SAVE_DIR_EXTERNAL.equals(DownloadHelper.this.mSaveDir)) {
                    saveFileInExternalStorage(is);
                }
            } catch (ProtocolException pex) {
                DLog.e(DownloadHelper.TAG, pex.toString());
            } catch (UnsupportedEncodingException insex) {
                DLog.e(DownloadHelper.TAG, insex.toString());
            } catch (IOException ioex) {
                DLog.e(DownloadHelper.TAG, ioex.toString());
            } catch (HttpException httpex) {
                DLog.e(DownloadHelper.TAG, httpex.toString());
            } finally {
                httpURLConn.disconnect();
            }
        }

        private void saveFileInExternalStorage(InputStream is) throws FileNotFoundException, IOException {
            FileUtil.makeCommonIconCacheDir(DownloadHelper.this.mFilePath);
            FileOutputStream os = new FileOutputStream(DownloadHelper.this.mFilePath);
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(is);
            while (true) {
                int bufferLength = bis.read(buffer);
                if (bufferLength <= 0) {
                    bis.close();
                    is.close();
                    os.close();
                    return;
                }
                os.write(buffer, 0, bufferLength);
            }
        }
    }
}
