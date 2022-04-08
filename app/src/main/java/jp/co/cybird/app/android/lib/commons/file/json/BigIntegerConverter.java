package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class BigIntegerConverter implements Converter {
    public static final BigIntegerConverter INSTANCE = new BigIntegerConverter();

    BigIntegerConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
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
                return BigInteger.ONE;
            }
            return BigInteger.ZERO;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toBigIntegerExact();
        } else {
            if (value instanceof BigInteger) {
                return value;
            }
            if (value instanceof Number) {
                return BigInteger.valueOf(((Number) value).longValue());
            }
            if (value instanceof String) {
                String str = value.toString().trim();
                if (str.length() <= 0) {
                    return null;
                }
                int start = 0;
                if (str.charAt(0) == '+') {
                    start = 0 + 1;
                }
                if (str.startsWith("0x", start)) {
                    return new BigInteger(str.substring(start + 2), 16);
                }
                return new BigInteger(str.substring(start));
            } else if (value == null) {
                return null;
            } else {
                throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
            }
        }
    }
}
