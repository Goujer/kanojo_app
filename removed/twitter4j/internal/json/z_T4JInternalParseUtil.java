package twitter4j.internal.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;
import twitter4j.TwitterException;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

public final class z_T4JInternalParseUtil {
    private static final Map<String, LinkedBlockingQueue<SimpleDateFormat>> formatMapQueue = new HashMap();

    private z_T4JInternalParseUtil() {
        throw new AssertionError();
    }

    static String getUnescapedString(String str, JSONObject json) {
        return HTMLEntity.unescape(getRawString(str, json));
    }

    public static String getRawString(String name, JSONObject json) {
        try {
            if (json.isNull(name)) {
                return null;
            }
            return json.getString(name);
        } catch (JSONException e) {
            return null;
        }
    }

    static String getURLDecodedString(String name, JSONObject json) {
        String returnValue = getRawString(name, json);
        if (returnValue == null) {
            return returnValue;
        }
        try {
            return URLDecoder.decode(returnValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return returnValue;
        }
    }

    public static Date parseTrendsDate(String asOfStr) throws TwitterException {
        switch (asOfStr.length()) {
            case 10:
                return new Date(Long.parseLong(asOfStr) * 1000);
            case 20:
                return getDate(asOfStr, "yyyy-MM-dd'T'HH:mm:ss'Z'");
            default:
                return getDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
        }
    }

    public static Date getDate(String name, JSONObject json) throws TwitterException {
        return getDate(name, json, "EEE MMM d HH:mm:ss z yyyy");
    }

    public static Date getDate(String name, JSONObject json, String format) throws TwitterException {
        String dateStr = getUnescapedString(name, json);
        if ("null".equals(dateStr) || dateStr == null) {
            return null;
        }
        return getDate(dateStr, format);
    }

    public static Date getDate(String dateString, String format) throws TwitterException {
        LinkedBlockingQueue<SimpleDateFormat> simpleDateFormats = formatMapQueue.get(format);
        if (simpleDateFormats == null) {
            simpleDateFormats = new LinkedBlockingQueue<>();
            formatMapQueue.put(format, simpleDateFormats);
        }
        SimpleDateFormat sdf = simpleDateFormats.poll();
        if (sdf == null) {
            sdf = new SimpleDateFormat(format, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        try {
            Date parse = sdf.parse(dateString);
            try {
                simpleDateFormats.put(sdf);
            } catch (InterruptedException e) {
            }
            return parse;
        } catch (ParseException pe) {
            throw new TwitterException("Unexpected date format(" + dateString + ") returned from twitter.com", (Throwable) pe);
        } catch (Throwable th) {
            try {
                simpleDateFormats.put(sdf);
            } catch (InterruptedException e2) {
            }
            throw th;
        }
    }

    public static int getInt(String name, JSONObject json) {
        return getInt(getRawString(name, json));
    }

    public static int getInt(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return -1;
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static long getLong(String name, JSONObject json) {
        return getLong(getRawString(name, json));
    }

    public static long getLong(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return -1;
        }
        if (str.endsWith("+")) {
            return Long.valueOf(str.substring(0, str.length() - 1)).longValue() + 1;
        }
        return Long.valueOf(str).longValue();
    }

    public static double getDouble(String name, JSONObject json) {
        String str2 = getRawString(name, json);
        if (str2 == null || "".equals(str2) || "null".equals(str2)) {
            return -1.0d;
        }
        return Double.valueOf(str2).doubleValue();
    }

    public static boolean getBoolean(String name, JSONObject json) {
        String str = getRawString(name, json);
        if (str == null || "null".equals(str)) {
            return false;
        }
        return Boolean.valueOf(str).booleanValue();
    }

    public static int toAccessLevel(HttpResponse res) {
        if (res == null) {
            return -1;
        }
        String xAccessLevel = res.getResponseHeader("X-Access-Level");
        if (xAccessLevel == null) {
            return 0;
        }
        switch (xAccessLevel.length()) {
            case 4:
                return 1;
            case 10:
                return 2;
            case 25:
                return 3;
            case 26:
                return 3;
            default:
                return 0;
        }
    }
}
