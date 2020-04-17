package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class PatternConverter implements Converter {
    public static final PatternConverter INSTANCE = new PatternConverter();

    PatternConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof String) {
            return Pattern.compile(value.toString());
        }
        if (value == null) {
            return null;
        }
        throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
    }
}
