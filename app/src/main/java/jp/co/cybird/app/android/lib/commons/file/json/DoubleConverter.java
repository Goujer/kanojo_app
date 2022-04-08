package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class DoubleConverter implements Converter {
    public static final DoubleConverter INSTANCE = new DoubleConverter();

    DoubleConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        double d;
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
                d = 1.0d;
            } else {
                d = Double.NaN;
            }
            return Double.valueOf(d);
        } else if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        } else {
            if (value instanceof String) {
                String str = value.toString().trim();
                if (str.length() > 0) {
                    return Double.valueOf(str);
                }
                return PlainConverter.getDefaultValue(c);
            } else if (value == null) {
                return PlainConverter.getDefaultValue(c);
            } else {
                throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
            }
        }
    }
}
