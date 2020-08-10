package jp.co.cybird.app.android.lib.commons.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;

public class ThreadHttpClient {
    private HashMap<String, String> clientHeaderMap = new HashMap<>();
    private String mUserAgent = null;

    enum Method {
        GET,
        POST
    }

    public void get(String urlStr, RequestParams params) {
        sendRequest(String.valueOf(urlStr) + "?" + params.getParamString(), (RequestParams) null, Method.GET);
    }

    public void post(String urlStr, RequestParams params) {
        sendRequest(urlStr, params, Method.POST);
    }

    public void sendRequest(final String urlStr, final RequestParams params, final Method method) {
        new Thread() {
            public void run() {
                String returnString = "";
                try {
                    URLConnection urlConnection = new URL(urlStr).openConnection();
                    if (ThreadHttpClient.this.getUserAgent() != null) {
                        urlConnection.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, ThreadHttpClient.this.getUserAgent());
                    }
                    if (ThreadHttpClient.this.clientHeaderMap.size() > 0) {
                        for (String header : ThreadHttpClient.this.clientHeaderMap.keySet()) {
                            urlConnection.setRequestProperty(header, (String) ThreadHttpClient.this.clientHeaderMap.get(header));
                        }
                    }
                    if (method == Method.POST) {
                        urlConnection.setDoOutput(true);
                        OutputStream outPutStream = urlConnection.getOutputStream();
                        String paramString = params.getParamString();
                        PrintStream ps = new PrintStream(outPutStream);
                        ps.print(paramString);
                        ps.close();
                    }
                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while (true) {
                        String str = bufferReader.readLine();
                        if (str == null) {
                            break;
                        }
                        returnString = String.valueOf(returnString) + str;
                    }
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    finalize();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }.start();
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public String getUserAgent() {
        return this.mUserAgent;
    }

    public void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
    }
}
