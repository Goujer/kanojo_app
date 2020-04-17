package jp.co.cybird.app.android.lib.commons.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class PersistentCookieStore implements CookieStore {
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private final SharedPreferences cookiePrefs;
    private final ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap<>();

    public PersistentCookieStore(Context context) {
        Cookie decodedCookie;
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        String storedCookieNames = this.cookiePrefs.getString(COOKIE_NAME_STORE, (String) null);
        if (storedCookieNames != null) {
            for (String name : TextUtils.split(storedCookieNames, ",")) {
                String encodedCookie = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + name, (String) null);
                if (!(encodedCookie == null || (decodedCookie = decodeCookie(encodedCookie)) == null)) {
                    this.cookies.put(name, decodedCookie);
                }
            }
            clearExpired(new Date());
        }
    }

    public void addCookie(Cookie cookie) {
        String name = cookie.getName();
        if (!cookie.isExpired(new Date())) {
            this.cookies.put(name, cookie);
        } else {
            this.cookies.remove(name);
        }
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
        prefsWriter.commit();
    }

    public void clear() {
        this.cookies.clear();
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        for (String name : this.cookies.keySet()) {
            prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        }
        prefsWriter.remove(COOKIE_NAME_STORE);
        prefsWriter.commit();
    }

    public boolean clearExpired(Date date) {
        boolean clearedAny = false;
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        for (Map.Entry<String, Cookie> entry : this.cookies.entrySet()) {
            String name = entry.getKey();
            if (entry.getValue().isExpired(date)) {
                this.cookies.remove(name);
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
                clearedAny = true;
            }
        }
        if (clearedAny) {
            prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", this.cookies.keySet()));
        }
        prefsWriter.commit();
        return clearedAny;
    }

    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    /* access modifiers changed from: protected */
    public String encodeCookie(SerializableCookie cookie) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(os).writeObject(cookie);
            return byteArrayToHexString(os.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Cookie decodeCookie(String cookieStr) {
        try {
            return ((SerializableCookie) new ObjectInputStream(new ByteArrayInputStream(hexStringToByteArray(cookieStr))).readObject()).getCookie();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte element : b) {
            int v = element & MotionEventCompat.ACTION_MASK;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /* access modifiers changed from: protected */
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
