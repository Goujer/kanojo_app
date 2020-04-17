package jp.co.cybird.app.android.lib.commons.file.json.util;

import android.support.v4.view.MotionEventCompat;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LocalCache {
    private static final int CACHE_SIZE = 256;
    private StringBuilder builderCache;
    private Map<String, DateFormat> dateFormatCache;
    private Locale locale;
    private BigDecimal[] numberCache;
    private Map<String, NumberFormat> numberFormatCache;
    private ResourceBundle resources;
    private String[] stringCache;
    private TimeZone timeZone;

    public LocalCache(String bundle, Locale locale2, TimeZone timeZone2) {
        this.locale = locale2;
        this.timeZone = timeZone2;
    }

    public StringBuilder getCachedBuffer() {
        if (this.builderCache == null) {
            this.builderCache = new StringBuilder();
        } else {
            this.builderCache.setLength(0);
        }
        return this.builderCache;
    }

    public String getString(StringBuilder sb) {
        if (sb.length() == 0) {
            return "";
        }
        if (sb.length() >= 32) {
            return sb.toString();
        }
        int index = getCacheIndex(sb);
        if (index < 0) {
            return sb.toString();
        }
        if (this.stringCache == null) {
            this.stringCache = new String[256];
        }
        if (this.numberCache == null) {
            this.numberCache = new BigDecimal[256];
        }
        String str = this.stringCache[index];
        if (str == null || str.length() != sb.length()) {
            String str2 = sb.toString();
            this.stringCache[index] = str2;
            this.numberCache[index] = null;
            return str2;
        }
        for (int i = 0; i < sb.length(); i++) {
            if (str.charAt(i) != sb.charAt(i)) {
                String str3 = sb.toString();
                this.stringCache[index] = str3;
                this.numberCache[index] = null;
                return str3;
            }
        }
        return str;
    }

    public BigDecimal getBigDecimal(StringBuilder sb) {
        if (sb.length() == 1) {
            if (sb.charAt(0) == '0') {
                return BigDecimal.ZERO;
            }
            if (sb.charAt(0) == '1') {
                return BigDecimal.ONE;
            }
        }
        if (sb.length() >= 32) {
            return new BigDecimal(sb.toString());
        }
        int index = getCacheIndex(sb);
        if (index < 0) {
            return new BigDecimal(sb.toString());
        }
        if (this.stringCache == null) {
            this.stringCache = new String[256];
        }
        if (this.numberCache == null) {
            this.numberCache = new BigDecimal[256];
        }
        String str = this.stringCache[index];
        BigDecimal num = this.numberCache[index];
        if (str == null || str.length() != sb.length()) {
            String str2 = sb.toString();
            BigDecimal num2 = new BigDecimal(str2);
            this.stringCache[index] = str2;
            this.numberCache[index] = num2;
            return num2;
        }
        for (int i = 0; i < sb.length(); i++) {
            if (str.charAt(i) != sb.charAt(i)) {
                String str3 = sb.toString();
                BigDecimal num3 = new BigDecimal(str3);
                this.stringCache[index] = str3;
                this.numberCache[index] = num3;
                return num3;
            }
        }
        if (num != null) {
            return num;
        }
        BigDecimal num4 = new BigDecimal(str);
        this.numberCache[index] = num4;
        return num4;
    }

    private int getCacheIndex(StringBuilder sb) {
        int h = 0;
        int max = Math.min(16, sb.length());
        for (int i = 0; i < max; i++) {
            h = (h * 31) + sb.charAt(i);
        }
        return h & MotionEventCompat.ACTION_MASK;
    }

    public NumberFormat getNumberFormat(String format) {
        NumberFormat nformat = null;
        if (this.numberFormatCache == null) {
            this.numberFormatCache = new HashMap();
        } else {
            nformat = this.numberFormatCache.get(format);
        }
        if (nformat != null) {
            return nformat;
        }
        NumberFormat nformat2 = new DecimalFormat(format, new DecimalFormatSymbols(this.locale));
        this.numberFormatCache.put(format, nformat2);
        return nformat2;
    }

    public DateFormat getDateFormat(String format) {
        DateFormat dformat = null;
        if (this.dateFormatCache == null) {
            this.dateFormatCache = new HashMap();
        } else {
            dformat = this.dateFormatCache.get(format);
        }
        if (dformat != null) {
            return dformat;
        }
        DateFormat dformat2 = new ExtendedDateFormat(format, this.locale);
        dformat2.setTimeZone(this.timeZone);
        this.dateFormatCache.put(format, dformat2);
        return dformat2;
    }

    public String getMessage(String id) {
        return getMessage(id, (Object[]) null);
    }

    public String getMessage(String id, Object... args) {
        if (args == null || args.length <= 0) {
            return this.resources.getString(id);
        }
        return MessageFormat.format(this.resources.getString(id), args);
    }
}
