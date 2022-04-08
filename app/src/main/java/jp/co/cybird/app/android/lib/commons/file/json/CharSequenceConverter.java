package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class CharSequenceConverter implements Converter {
    public static final CharSequenceConverter INSTANCE = new CharSequenceConverter();

    CharSequenceConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value != null) {
            return value.toString();
        }
        return null;
    }
}
