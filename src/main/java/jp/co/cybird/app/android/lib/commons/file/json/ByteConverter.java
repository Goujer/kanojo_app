package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class ByteConverter implements Converter {
    public static final ByteConverter INSTANCE = new ByteConverter();

    ByteConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        int num;
        int i;
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
                i = 1;
            } else {
                i = 0;
            }
            return Integer.valueOf(i);
        } else if (value instanceof BigDecimal) {
            return Byte.valueOf(((BigDecimal) value).byteValueExact());
        } else {
            if (value instanceof Number) {
                return Byte.valueOf(((Number) value).byteValue());
            }
            if (value instanceof String) {
                String str = value.toString().trim().toLowerCase();
                if (str.length() <= 0) {
                    return PlainConverter.getDefaultValue(c);
                }
                int start = 0;
                if (str.charAt(0) == '+') {
                    start = 0 + 1;
                }
                if (str.startsWith("0x", start)) {
                    num = Integer.parseInt(str.substring(start + 2), 16);
                } else {
                    num = Integer.parseInt(str.substring(start));
                }
                if (num > 127) {
                    num -= 256;
                }
                return Byte.valueOf((byte) num);
            } else if (value == null) {
                return PlainConverter.getDefaultValue(c);
            } else {
                throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
            }
        }
    }
}
