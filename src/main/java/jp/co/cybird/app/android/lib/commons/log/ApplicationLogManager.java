package jp.co.cybird.app.android.lib.commons.log;

import android.content.Context;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import jp.co.cybird.app.android.lib.commons.file.json.util.Unicode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationLogManager {
    public static synchronized void addLog(Context context, String tag, String message) {
        synchronized (ApplicationLogManager.class) {
            ApplicationLog appLog = new ApplicationLog(tag, message);
            ApplicationLogDB db = new ApplicationLogDB(context);
            db.insert(appLog);
            db.close();
        }
    }

    public static synchronized List<ApplicationLog> getAllLogs(Context context) {
        List<ApplicationLog> resultArray;
        synchronized (ApplicationLogManager.class) {
            ApplicationLogDB db = new ApplicationLogDB(context);
            resultArray = db.getAllLogs();
            db.close();
        }
        return resultArray;
    }

    public static synchronized String getAllLogsJsonString(Context context) {
        String str;
        synchronized (ApplicationLogManager.class) {
            List<ApplicationLog> appLogs = getAllLogs(context);
            if (appLogs == null) {
                str = null;
            } else {
                int size = appLogs.size();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = new JSONObject();
                    ApplicationLog tmpAppLog = appLogs.get(i);
                    if (tmpAppLog != null) {
                        long timestamp = tmpAppLog.getTimestamp();
                        Object tag = tmpAppLog.getTag();
                        String message = tmpAppLog.getMessage();
                        if (!(timestamp == 0 || tag == null || message == null)) {
                            jsonObject.put("timestamp", timestamp);
                            jsonObject.put("tag", tag);
                            jsonObject.put("message", Unicode.escape(message));
                            jsonArray.put(jsonObject);
                        }
                    }
                }
                try {
                    str = jsonArray.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    str = null;
                }
            }
        }
        return str;
    }

    public static synchronized String getAllLogsJsonStringBase64Encoded(Context context) {
        String str = null;
        synchronized (ApplicationLogManager.class) {
            String jsonArrayString = getAllLogsJsonString(context);
            if (jsonArrayString != null) {
                byte[] encode = Base64.encode(jsonArrayString.getBytes(), 0);
                if (encode != null) {
                    try {
                        str = new String(encode, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return str;
    }

    public static synchronized String getAllLogsJsonStringURLEncoded(Context context) {
        String str = null;
        synchronized (ApplicationLogManager.class) {
            String jsonArrayString = getAllLogsJsonString(context);
            if (jsonArrayString != null) {
                try {
                    str = URLEncoder.encode(jsonArrayString, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}
