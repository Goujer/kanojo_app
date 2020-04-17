package com.google.ads.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.webkit.WebView;
import com.google.ads.AdActivity;
import com.google.ads.AdRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;
import jp.co.cybird.barcodekanojoForGAM.preferences.Preferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class AdUtil {
    public static final int a = a(Build.VERSION.SDK);
    private static Boolean b = null;
    private static String c = null;
    private static String d;
    private static String e = null;
    private static AudioManager f;
    private static boolean g = true;
    private static boolean h = false;
    private static String i = null;

    public enum a {
        INVALID,
        SPEAKER,
        HEADPHONES,
        VIBRATE,
        EMULATOR,
        OTHER
    }

    public static boolean a(Intent intent, Context context) {
        return context.getPackageManager().resolveActivity(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) != null;
    }

    public static String a(Readable readable) throws IOException {
        StringBuilder sb = new StringBuilder();
        CharBuffer allocate = CharBuffer.allocate(2048);
        while (true) {
            int read = readable.read(allocate);
            if (read == -1) {
                return sb.toString();
            }
            allocate.flip();
            sb.append(allocate, 0, read);
        }
    }

    public static int a(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            b.e("The Android SDK version couldn't be parsed to an int: " + Build.VERSION.SDK);
            b.e("Defaulting to Android SDK version 3.");
            return 3;
        }
    }

    public static String a(Context context) {
        String str;
        if (c == null) {
            String string = Settings.Secure.getString(context.getContentResolver(), Preferences.PREFERENCE_ANDROID_ID);
            if (string == null || c()) {
                str = b("emulator");
            } else {
                str = b(string);
            }
            if (str == null) {
                return null;
            }
            c = str.toUpperCase(Locale.US);
        }
        return c;
    }

    public static int a() {
        if (a >= 9) {
            return 6;
        }
        return 0;
    }

    public static int b() {
        if (a >= 9) {
            return 7;
        }
        return 1;
    }

    public static int a(Context context, DisplayMetrics displayMetrics) {
        if (a >= 4) {
            return e.a(context, displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

    public static int b(Context context, DisplayMetrics displayMetrics) {
        if (a >= 4) {
            return e.b(context, displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    public static boolean b(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        if (packageManager.checkPermission("android.permission.INTERNET", packageName) == -1) {
            b.b("INTERNET permissions must be enabled in AndroidManifest.xml.");
            return false;
        } else if (packageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", packageName) != -1) {
            return true;
        } else {
            b.b("ACCESS_NETWORK_STATE permissions must be enabled in AndroidManifest.xml.");
            return false;
        }
    }

    public static boolean a(int i2, int i3, String str) {
        if ((i2 & i3) != 0) {
            return true;
        }
        b.b("The android:configChanges value of the com.google.ads.AdActivity must include " + str + ".");
        return false;
    }

    public static boolean c(Context context) {
        if (b != null) {
            return b.booleanValue();
        }
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(new Intent(context, AdActivity.class), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        b = true;
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            b.b("Could not find com.google.ads.AdActivity, please make sure it is registered in AndroidManifest.xml.");
            b = false;
        } else {
            if (!a(resolveActivity.activityInfo.configChanges, 16, "keyboard")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 32, "keyboardHidden")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 128, "orientation")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 256, "screenLayout")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 512, "uiMode")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 1024, "screenSize")) {
                b = false;
            }
            if (!a(resolveActivity.activityInfo.configChanges, 2048, "smallestScreenSize")) {
                b = false;
            }
        }
        return b.booleanValue();
    }

    public static boolean c() {
        return a((d) null);
    }

    static boolean a(d dVar) {
        if (dVar == null) {
            dVar = d.d;
        }
        return dVar.equals(d.e) || dVar.equals(d.f);
    }

    public static boolean a(Uri uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();
        if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
            return true;
        }
        return false;
    }

    public static String b(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(Digest.MD5);
            instance.update(str.getBytes(), 0, str.length());
            return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, instance.digest())});
        } catch (NoSuchAlgorithmException e2) {
            return str.substring(0, 32);
        }
    }

    public static String d(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return null;
        }
        switch (activeNetworkInfo.getType()) {
            case 0:
                return "ed";
            case 1:
                return "wi";
            default:
                return "unknown";
        }
    }

    public static String e(Context context) {
        if (d == null) {
            StringBuilder sb = new StringBuilder();
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=donuts")), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
                sb.append(AdActivity.TYPE_PARAM);
            }
            List<ResolveInfo> queryIntentActivities2 = packageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pname:com.google")), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            if (queryIntentActivities2 == null || queryIntentActivities2.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append("a");
            }
            d = sb.toString();
        }
        return d;
    }

    public static boolean a(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e2) {
            return false;
        }
    }

    public static String f(Context context) {
        ActivityInfo activityInfo;
        PackageInfo packageInfo;
        if (e != null) {
            return e;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            ResolveInfo resolveActivity = packageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.ads")), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null || (packageInfo = packageManager.getPackageInfo(activityInfo.packageName, 0)) == null) {
                return null;
            }
            e = packageInfo.versionCode + "." + activityInfo.packageName;
            return e;
        } catch (PackageManager.NameNotFoundException e2) {
            return null;
        }
    }

    public static a g(Context context) {
        if (f == null) {
            f = (AudioManager) context.getSystemService("audio");
        }
        a aVar = a.OTHER;
        int mode = f.getMode();
        if (c()) {
            return a.EMULATOR;
        }
        if (f.isMusicActive() || f.isSpeakerphoneOn() || mode == 2 || mode == 1) {
            return a.VIBRATE;
        }
        int ringerMode = f.getRingerMode();
        if (ringerMode == 0 || ringerMode == 1) {
            return a.VIBRATE;
        }
        return a.SPEAKER;
    }

    public static DisplayMetrics a(Activity activity) {
        if (activity.getWindowManager() == null) {
            return null;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static HashMap<String, Object> a(Location location) {
        if (location == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", Long.valueOf(location.getTime() * 1000));
        hashMap.put("lat", Long.valueOf((long) (location.getLatitude() * 1.0E7d)));
        hashMap.put("long", Long.valueOf((long) (location.getLongitude() * 1.0E7d)));
        hashMap.put("radius", Long.valueOf((long) (location.getAccuracy() * 1000.0f)));
        return hashMap;
    }

    public static HashMap<String, String> b(Uri uri) {
        if (uri == null) {
            return null;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery != null) {
            for (String str : encodedQuery.split("&")) {
                int indexOf = str.indexOf("=");
                if (indexOf < 0) {
                    hashMap.put(Uri.decode(str), (Object) null);
                } else {
                    hashMap.put(Uri.decode(str.substring(0, indexOf)), Uri.decode(str.substring(indexOf + 1, str.length())));
                }
            }
        }
        return hashMap;
    }

    public static boolean d() {
        return g;
    }

    public static void a(boolean z) {
        g = z;
    }

    public static void h(Context context) {
        if (!h) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            context.registerReceiver(new UserActivityReceiver(), intentFilter);
            h = true;
        }
    }

    public static class UserActivityReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                AdUtil.a(true);
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                AdUtil.a(false);
            }
        }
    }

    public static String i(Context context) {
        if (i == null) {
            String userAgentString = new WebView(context).getSettings().getUserAgentString();
            if (userAgentString == null || userAgentString.length() == 0 || userAgentString.equals("Java0")) {
                String property = System.getProperty("os.name", "Linux");
                String str = "Android " + Build.VERSION.RELEASE;
                Locale locale = Locale.getDefault();
                String lowerCase = locale.getLanguage().toLowerCase(Locale.US);
                if (lowerCase.length() == 0) {
                    lowerCase = "en";
                }
                String lowerCase2 = locale.getCountry().toLowerCase(Locale.US);
                if (lowerCase2.length() > 0) {
                    lowerCase = lowerCase + "-" + lowerCase2;
                }
                userAgentString = "Mozilla/5.0 (" + property + "; U; " + str + "; " + lowerCase + "; " + (Build.MODEL + " Build/" + Build.ID) + ") AppleWebKit/0.0 (KHTML, like " + "Gecko) Version/0.0 Mobile Safari/0.0";
            }
            i = userAgentString + " (Mobile; " + "afma-sdk-a-v" + AdRequest.VERSION + ")";
        }
        return i;
    }

    public static void a(WebView webView) {
        webView.getSettings().setUserAgentString(i(webView.getContext().getApplicationContext()));
    }

    public static void a(HttpURLConnection httpURLConnection, Context context) {
        httpURLConnection.setRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, i(context));
    }

    public static String a(Map<String, Object> map) {
        try {
            return b(map).toString();
        } catch (JSONException e2) {
            b.d("JsonException in serialization: ", e2);
            return null;
        }
    }

    public static JSONObject b(Map<String, Object> map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (map == null || map.isEmpty()) {
            return jSONObject;
        }
        for (String next : map.keySet()) {
            Object obj = map.get(next);
            if ((obj instanceof String) || (obj instanceof Integer) || (obj instanceof Double) || (obj instanceof Long) || (obj instanceof Float)) {
                jSONObject.put(next, obj);
            } else if (obj instanceof Map) {
                try {
                    jSONObject.put(next, b((Map<String, Object>) (Map) obj));
                } catch (ClassCastException e2) {
                    b.d("Unknown map type in json serialization: ", e2);
                }
            } else if (obj instanceof Set) {
                try {
                    jSONObject.put(next, a((Set<Object>) (Set) obj));
                } catch (ClassCastException e3) {
                    b.d("Unknown map type in json serialization: ", e3);
                }
            } else {
                b.e("Unknown value in json serialization: " + obj);
            }
        }
        return jSONObject;
    }

    public static JSONArray a(Set<Object> set) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        if (set == null || set.isEmpty()) {
            return jSONArray;
        }
        for (Object next : set) {
            if ((next instanceof String) || (next instanceof Integer) || (next instanceof Double) || (next instanceof Long) || (next instanceof Float)) {
                jSONArray.put(next);
            } else if (next instanceof Map) {
                try {
                    jSONArray.put(b((Map<String, Object>) (Map) next));
                } catch (ClassCastException e2) {
                    b.d("Unknown map type in json serialization: ", e2);
                }
            } else if (next instanceof Set) {
                try {
                    jSONArray.put(a((Set<Object>) (Set) next));
                } catch (ClassCastException e3) {
                    b.d("Unknown map type in json serialization: ", e3);
                }
            } else {
                b.e("Unknown value in json serialization: " + next);
            }
        }
        return jSONArray;
    }

    public static int a(Context context, int i2) {
        return (int) TypedValue.applyDimension(1, (float) i2, context.getResources().getDisplayMetrics());
    }
}
