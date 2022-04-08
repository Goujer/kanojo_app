package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class BigDecimalConverter implements Converter {
    public static final BigDecimalConverter INSTANCE = new BigDecimalConverter();

    BigDecimalConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof BigDecimal) {
            return value;
        }
        if (value instanceof String) {
            NumberFormat f = context.getNumberFormat();
            if (f != null) {
                value = f.parse((String) value);
            }
            String str = value.toString().trim();
            if (str.length() <= 0) {
                return null;
            }
            if (str.charAt(0) == '+') {
                return new BigDecimal(str.substring(1));
            }
            return new BigDecimal(str);
        } else if (value == null) {
            return null;
        } else {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        }
    }
}
