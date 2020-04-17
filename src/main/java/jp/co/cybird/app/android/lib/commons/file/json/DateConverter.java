package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class DateConverter implements Converter {
    public static final DateConverter INSTANCE = new DateConverter();
    private static final Pattern TIMEZONE_PATTERN = Pattern.compile("(?:GMT|UTC)([+-][0-9]{2})([0-9]{2})");

    DateConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        Date date = null;
        if (value instanceof Number) {
            date = (Date) context.createInternal(c);
            date.setTime(((Number) value).longValue());
        } else if (value != null) {
            String str = value.toString().trim();
            if (str.length() > 0) {
                DateFormat format = context.getDateFormat();
                if (format != null) {
                    date = format.parse(str);
                } else {
                    date = convertDate(context, str);
                }
                if (date != null && !c.isAssignableFrom(date.getClass())) {
                    long time = date.getTime();
                    date = (Date) context.createInternal(c);
                    date.setTime(time);
                }
            }
        }
        if (date instanceof java.sql.Date) {
            Calendar cal = Calendar.getInstance(context.getTimeZone(), context.getLocale());
            cal.setTimeInMillis(date.getTime());
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            date.setTime(cal.getTimeInMillis());
        } else if (date instanceof Time) {
            Calendar cal2 = Calendar.getInstance(context.getTimeZone(), context.getLocale());
            cal2.setTimeInMillis(date.getTime());
            cal2.set(1, 1970);
            cal2.set(2, 0);
            cal2.set(5, 1);
            date.setTime(cal2.getTimeInMillis());
        }
        return date;
    }

    static Date convertDate(JSON.JSONContext context, String value) throws ParseException {
        DateFormat format;
        String value2 = value.trim();
        if (value2.length() == 0) {
            return null;
        }
        String value3 = TIMEZONE_PATTERN.matcher(value2).replaceFirst("GMT$1:$2");
        if (Character.isDigit(value3.charAt(0))) {
            StringBuilder sb = context.getLocalCache().getCachedBuffer();
            int pos = (value3.length() <= 2 || value3.charAt(2) != ':') ? 0 : 3;
            boolean before = true;
            int count = 0;
            for (int i = 0; i < value3.length(); i++) {
                char c = value3.charAt(i);
                if ((pos == 4 || pos == 5 || pos == 6) && ((c == '+' || c == '-') && i + 1 < value3.length() && Character.isDigit(value3.charAt(i + 1)))) {
                    if (!before) {
                        sb.append('\'');
                    }
                    pos = 7;
                    count = 0;
                    before = true;
                } else if (pos != 7 || c != ':' || i + 1 >= value3.length() || !Character.isDigit(value3.charAt(i + 1))) {
                    boolean digit = Character.isDigit(c) && pos < 8;
                    if (before != digit) {
                        sb.append('\'');
                        if (digit) {
                            count = 0;
                            pos++;
                        }
                    }
                    if (digit) {
                        char type = "yMdHmsSZ".charAt(pos);
                        if (count == ((type == 'y' || type == 'Z') ? 4 : type == 'S' ? 3 : 2)) {
                            count = 0;
                            pos++;
                            type = "yMdHmsSZ".charAt(pos);
                        }
                        if (type != 'Z' || count == 0) {
                            sb.append(type);
                        }
                        count++;
                    } else {
                        sb.append(c == '\'' ? "''" : Character.valueOf(c));
                    }
                    before = digit;
                } else {
                    value3 = String.valueOf(value3.substring(0, i)) + value3.substring(i + 1);
                }
            }
            if (!before) {
                sb.append('\'');
            }
            format = new SimpleDateFormat(sb.toString(), Locale.ENGLISH);
        } else if (value3.length() <= 18) {
            format = DateFormat.getDateInstance(2, context.getLocale());
        } else if (value3.charAt(3) == ',') {
            String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
            if (value3.length() < pattern.length()) {
                pattern = pattern.substring(0, value3.length());
            }
            format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        } else if (value3.charAt(13) == ':') {
            format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        } else if (value3.charAt(18) == ':') {
            String pattern2 = "EEE MMM dd yyyy HH:mm:ss Z";
            if (value3.length() < pattern2.length()) {
                pattern2 = pattern2.substring(0, value3.length());
            }
            format = new SimpleDateFormat(pattern2, Locale.ENGLISH);
        } else {
            format = DateFormat.getDateTimeInstance(2, 2, context.getLocale());
        }
        format.setLenient(false);
        format.setTimeZone(context.getTimeZone());
        return format.parse(value3);
    }
}
