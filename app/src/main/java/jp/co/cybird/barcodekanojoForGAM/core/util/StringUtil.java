package jp.co.cybird.barcodekanojoForGAM.core.util;

public class StringUtil {
    public static String comment(String comment) {
        return comment;
    }

    public static String nullToBlank(String src) {
        if (src == null) {
            return "";
        }
        return src;
    }
}
