package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class CalendarConverter implements Converter {
    public static final CalendarConverter INSTANCE = new CalendarConverter();

    CalendarConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof Number) {
            Calendar cal = (Calendar) context.createInternal(c);
            cal.setTimeInMillis(((Number) value).longValue());
            return cal;
        }
        if (value != null) {
            String str = value.toString().trim();
            if (str.length() > 0) {
                Calendar cal2 = (Calendar) context.createInternal(c);
                DateFormat format = context.getDateFormat();
                if (format != null) {
                    cal2.setTime(format.parse(str));
                    return cal2;
                }
                cal2.setTime(DateConverter.convertDate(context, str));
                return cal2;
            }
        }
        return null;
    }
}
