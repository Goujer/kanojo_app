package twitter4j.internal.util;

import java.util.ArrayList;
import java.util.List;

public class z_T4JInternalStringUtil {
    private z_T4JInternalStringUtil() {
        throw new AssertionError();
    }

    public static String maskString(String str) {
        StringBuilder buf = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            buf.append("*");
        }
        return buf.toString();
    }

    public static String[] split(String str, String separator) {
        int index = str.indexOf(separator);
        if (index == -1) {
            return new String[]{str};
        }
        List<String> strList = new ArrayList<>();
        int oldIndex = 0;
        while (index != -1) {
            strList.add(str.substring(oldIndex, index));
            oldIndex = index + separator.length();
            index = str.indexOf(separator, oldIndex);
        }
        if (oldIndex != str.length()) {
            strList.add(str.substring(oldIndex));
        }
        return (String[]) strList.toArray(new String[strList.size()]);
    }

    public static String join(int[] follows) {
        StringBuilder buf = new StringBuilder(follows.length * 11);
        for (int follow : follows) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    public static String join(long[] follows) {
        StringBuilder buf = new StringBuilder(follows.length * 11);
        for (long follow : follows) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(follow);
        }
        return buf.toString();
    }

    public static String join(String[] track) {
        StringBuilder buf = new StringBuilder(track.length * 11);
        for (String str : track) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(str);
        }
        return buf.toString();
    }

    public static String join(List<String> strs) {
        StringBuilder buf = new StringBuilder(strs.size() * 11);
        for (String str : strs) {
            if (buf.length() != 0) {
                buf.append(",");
            }
            buf.append(str);
        }
        return buf.toString();
    }
}
