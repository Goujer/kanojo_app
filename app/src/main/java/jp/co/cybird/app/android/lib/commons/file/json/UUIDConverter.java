package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class UUIDConverter implements Converter {
    public static final UUIDConverter INSTANCE = new UUIDConverter();

    UUIDConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof String) {
            return UUID.fromString(value.toString().trim());
        }
        if (value == null) {
            return null;
        }
        throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
    }
}
