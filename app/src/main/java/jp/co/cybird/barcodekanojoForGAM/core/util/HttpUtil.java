package jp.co.cybird.barcodekanojoForGAM.core.util;

public class HttpUtil {
    public static boolean isUrl(String url) {
        if (url == null) {
            return false;
        }
        if (url.contains("http://") || url.contains("https://")) {
            return true;
        }
        return false;
    }
}
