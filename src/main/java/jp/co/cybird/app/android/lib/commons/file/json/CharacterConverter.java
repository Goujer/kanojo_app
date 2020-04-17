package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class CharacterConverter implements Converter {
    public static final CharacterConverter INSTANCE = new CharacterConverter();

    CharacterConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        char c2;
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                c2 = '1';
            } else {
                c2 = '0';
            }
            return Character.valueOf(c2);
        } else if (value instanceof BigDecimal) {
            return Character.valueOf((char) ((BigDecimal) value).intValueExact());
        } else {
            if (value instanceof String) {
                String s = value.toString();
                if (s.length() > 0) {
                    return Character.valueOf(s.charAt(0));
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
