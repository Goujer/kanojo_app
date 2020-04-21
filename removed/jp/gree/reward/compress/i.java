package jp.gree.reward.compress;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TimeZone;
import jp.co.cybird.barcodekanojoForGAM.core.util.Digest;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public final class i {
    private static Boolean a = false;
    private static Boolean b = false;

    public static Boolean a(ApplicationInfo applicationInfo, String str) {
        return Boolean.valueOf(applicationInfo.metaData.getBoolean("greereward_".toUpperCase() + str));
    }

    public static String a() {
        return b.booleanValue() ? "http://220.156.130.195/" : a.booleanValue() ? "http://reward-sb.developer.gree.co.jp/" : "http://reward.developer.gree.co.jp/";
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m0a(ApplicationInfo applicationInfo, String str) {
        StringBuilder sb = new StringBuilder("greereward_".toUpperCase());
        sb.append(str);
        Object obj = applicationInfo.metaData.get(sb.toString());
        return obj instanceof Integer ? String.valueOf(obj) : applicationInfo.metaData.getString(sb.toString());
    }

    public static String a(TelephonyManager telephonyManager) {
        return b(telephonyManager.getDeviceId());
    }

    public static String a(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(Digest.SHA256);
        } catch (NoSuchAlgorithmException e) {
            a("GreeRewardUtil", "sha256ToHexString", e);
        }
        StringBuilder sb = new StringBuilder();
        messageDigest.update(str.getBytes());
        byte[] digest = messageDigest.digest();
        for (byte b2 : digest) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                sb.append(GreeDefs.BARCODE);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static String a(Date date) {
        return a().format(date);
    }

    public static String a(SortedMap sortedMap, String str) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (String str2 : sortedMap.keySet()) {
            sb.append(str2);
            sb.append("=");
            try {
                sb.append(URLEncoder.encode((String) sortedMap.get(str2), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                a("GreeRewardUtil", "makeSignedParamString", e);
            }
            sb.append("&");
            sb2.append((String) sortedMap.get(str2));
            sb2.append(";");
        }
        sb2.append(str);
        return String.valueOf(sb.toString()) + "digest=" + a(sb2.toString());
    }

    /* renamed from: a  reason: collision with other method in class */
    private static DateFormat m1a() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("JST"));
        return simpleDateFormat;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static Date m2a(String str) {
        try {
            return a().parse(str);
        } catch (ParseException e) {
            a("GreeRewardUtil", "convertStringToDate", e);
            return null;
        }
    }

    public static void a(Boolean bool) {
        a = bool;
    }

    public static void a(String str, String str2) {
        if (a.booleanValue()) {
            Log.i(str, str2);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (a.booleanValue()) {
            Log.e(str, String.valueOf(str2) + ": " + th.toString());
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m3a() {
        try {
            Runtime.getRuntime().exec("busybox");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean a(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager.getActiveNetworkInfo() != null) {
            return connectivityManager.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

    public static String b() {
        return a().format(new Date());
    }

    public static String b(String str) {
        return str == null ? "" : str;
    }

    public static void b(Boolean bool) {
        b = bool;
    }

    public static void b(String str, String str2) {
        if (a.booleanValue()) {
            Log.e(str, str2);
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m4b() {
        String str = Build.VERSION.RELEASE;
        a("GreeRewardUtil", "OS version : " + str);
        return !str.equals("1.6");
    }

    public static String c() {
        return b.booleanValue() ? "development" : a.booleanValue() ? "sandbox" : "production";
    }
}
