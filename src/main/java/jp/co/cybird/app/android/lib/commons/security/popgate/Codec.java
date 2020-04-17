package jp.co.cybird.app.android.lib.commons.security.popgate;

public class Codec {
    public static native String decode(String str);

    public static native String encode(String str);

    static {
        System.loadLibrary("popgate");
    }
}
