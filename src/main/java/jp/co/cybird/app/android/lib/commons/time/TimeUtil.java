package jp.co.cybird.app.android.lib.commons.time;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TimeUtil {
    public static long getLongTimeMillsAddedDays(long targetTimeMills, int addDays) {
        return (((long) addDays) * 86400000) + targetTimeMills;
    }

    public static String formatTimeMillis(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(millis));
    }

    public static String formatTimeMills(String millis) {
        try {
            return formatTimeMillis(Long.parseLong(millis));
        } catch (Exception e) {
            return millis;
        }
    }
}
