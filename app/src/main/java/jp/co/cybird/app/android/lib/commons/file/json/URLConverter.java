package jp.co.cybird.app.android.lib.commons.file.json;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class URLConverter implements Converter {
    public static final URLConverter INSTANCE = new URLConverter();

    URLConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof String) {
            if (value instanceof File) {
                return ((File) value).toURI().toURL();
            }
            if (value instanceof URI) {
                return ((URI) value).toURL();
            }
            return new URL(value.toString().trim());
        } else if (value == null) {
            return null;
        } else {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        }
    }
}
