package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class CharsetConverter implements Converter {
    public static final CharsetConverter INSTANCE = new CharsetConverter();

    CharsetConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof String) {
            return Charset.forName(value.toString().trim());
        }
        if (value == null) {
            return null;
        }
        throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
    }
}
