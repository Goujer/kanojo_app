package com.facebook.android;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.Utility;
import com.google.android.gcm.GCMConstants;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import org.apache.james.mime4j.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;

public final class Util {
    private static final String UTF8 = "UTF-8";

    @Deprecated
    public static String encodePostBody(Bundle parameters, String boundary) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : parameters.keySet()) {
            Object parameter = parameters.get(key);
            if (parameter instanceof String) {
                sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + ((String) parameter));
                sb.append("\r\n--" + boundary + CharsetUtil.CRLF);
            }
        }
        return sb.toString();
    }

    @Deprecated
    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (parameters.get(key) instanceof String) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(String.valueOf(URLEncoder.encode(key)) + "=" + URLEncoder.encode(parameters.getString(key)));
            }
        }
        return sb.toString();
    }

    @Deprecated
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            for (String parameter : s.split("&")) {
                String[] v = parameter.split("=");
                try {
                    if (v.length == 2) {
                        params.putString(URLDecoder.decode(v[0], UTF8), URLDecoder.decode(v[1], UTF8));
                    } else if (v.length == 1) {
                        params.putString(URLDecoder.decode(v[0], UTF8), "");
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return params;
    }

    @Deprecated
    public static Bundle parseUrl(String url) {
        try {
            URL u = new URL(url.replace("fbconnect", "http"));
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    @Deprecated
    public static String openUrl(String url, String method, Bundle params) throws MalformedURLException, IOException, UnknownHostException, SSLException {
        if (method.equals(DownloadHelper.REQUEST_METHOD_GET)) {
            url = String.valueOf(url) + "?" + encodeUrl(params);
        }
        Utility.logd("Facebook-Util", String.valueOf(method) + " URL: " + url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, String.valueOf(System.getProperties().getProperty("http.agent")) + " FacebookAndroidSDK");
        if (!method.equals(DownloadHelper.REQUEST_METHOD_GET)) {
            Bundle dataparams = new Bundle();
            for (String key : params.keySet()) {
                Object parameter = params.get(key);
                if (parameter instanceof byte[]) {
                    dataparams.putByteArray(key, (byte[]) parameter);
                }
            }
            if (!params.containsKey("method")) {
                params.putString("method", method);
            }
            if (params.containsKey("access_token")) {
                params.putString("access_token", URLDecoder.decode(params.getString("access_token")));
            }
            conn.setRequestMethod(DownloadHelper.REQUEST_METHOD_POST);
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(("--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + CharsetUtil.CRLF).getBytes());
            os.write(encodePostBody(params, "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f").getBytes());
            os.write((String.valueOf(CharsetUtil.CRLF) + "--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + CharsetUtil.CRLF).getBytes());
            if (!dataparams.isEmpty()) {
                for (String key2 : dataparams.keySet()) {
                    os.write(("Content-Disposition: form-data; filename=\"" + key2 + "\"" + CharsetUtil.CRLF).getBytes());
                    os.write(("Content-Type: content/unknown" + CharsetUtil.CRLF + CharsetUtil.CRLF).getBytes());
                    os.write(dataparams.getByteArray(key2));
                    os.write((String.valueOf(CharsetUtil.CRLF) + "--" + "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f" + CharsetUtil.CRLF).getBytes());
                }
            }
            os.flush();
        }
        try {
            return read(conn.getInputStream());
        } catch (FileNotFoundException e) {
            return read(conn.getErrorStream());
        }
    }

    @Deprecated
    private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    @Deprecated
    public static JSONObject parseJson(String response) throws JSONException, FacebookError {
        if (response.equals("false")) {
            throw new FacebookError("request failed");
        }
        if (response.equals("true")) {
            response = "{value : true}";
        }
        JSONObject json = new JSONObject(response);
        if (json.has(GCMConstants.EXTRA_ERROR)) {
            JSONObject error = json.getJSONObject(GCMConstants.EXTRA_ERROR);
            throw new FacebookError(error.getString("message"), error.getString("type"), 0);
        } else if (json.has("error_code") && json.has("error_msg")) {
            throw new FacebookError(json.getString("error_msg"), "", Integer.parseInt(json.getString("error_code")));
        } else if (json.has("error_code")) {
            throw new FacebookError("request failed", "", Integer.parseInt(json.getString("error_code")));
        } else if (json.has("error_msg")) {
            throw new FacebookError(json.getString("error_msg"));
        } else if (!json.has("error_reason")) {
            return json;
        } else {
            throw new FacebookError(json.getString("error_reason"));
        }
    }

    @Deprecated
    public static void showAlert(Context context, String title, String text) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(text);
        alertBuilder.create().show();
    }
}
