package jp.co.cybird.app.android.lib.commons.file.json.util;

import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class Unicode {
    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        char[] charValue = value.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char ch : charValue) {
            if (ch == '_' || ((ch >= '0' && '9' >= ch) || ((ch >= 'a' && 'z' >= ch) || (ch >= 'A' && 'Z' >= ch)))) {
                result.append(ch);
            } else {
                String unicodeCh = Integer.toHexString(ch);
                result.append("\\u");
                for (int i = 0; i < 4 - unicodeCh.length(); i++) {
                    result.append(GreeDefs.BARCODE);
                }
                result.append(unicodeCh);
            }
        }
        return result.toString();
    }
}
