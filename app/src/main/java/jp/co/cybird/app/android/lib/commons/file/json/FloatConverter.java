package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class FloatConverter implements Converter {
    public static final FloatConverter INSTANCE = new FloatConverter();

    FloatConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        float f;
        NumberFormat f2;
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if ((value instanceof String) && (f2 = context.getNumberFormat()) != null) {
            value = f2.parse((String) value);
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                f = 1.0f;
            } else {
                f = Float.NaN;
            }
            return Float.valueOf(f);
        } else if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        } else {
            if (value instanceof String) {
                String str = value.toString().trim();
                if (str.length() > 0) {
                    return Float.valueOf(str);
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
