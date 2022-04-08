package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class LongConverter implements Converter {
    public static final LongConverter INSTANCE = new LongConverter();

    LongConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        long j;
        NumberFormat f;
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if ((value instanceof String) && (f = context.getNumberFormat()) != null) {
            value = f.parse((String) value);
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                j = 1;
            } else {
                j = 0;
            }
            return Long.valueOf(j);
        } else if (value instanceof BigDecimal) {
            return Long.valueOf(((BigDecimal) value).longValueExact());
        } else {
            if (value instanceof Number) {
                return Long.valueOf(((Number) value).longValue());
            }
            if (value instanceof String) {
                String str = value.toString().trim();
                if (str.length() <= 0) {
                    return PlainConverter.getDefaultValue(c);
                }
                int start = 0;
                if (str.charAt(0) == '+') {
                    start = 0 + 1;
                }
                if (str.startsWith("0x", start)) {
                    return Long.valueOf(Long.parseLong(str.substring(start + 2), 16));
                }
                return Long.valueOf(Long.parseLong(str.substring(start)));
            } else if (value == null) {
                return PlainConverter.getDefaultValue(c);
            } else {
                throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
            }
        }
    }
}
