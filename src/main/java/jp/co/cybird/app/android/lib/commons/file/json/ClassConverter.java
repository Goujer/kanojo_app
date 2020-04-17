package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class ClassConverter implements Converter {
    public static final ClassConverter INSTANCE = new ClassConverter();

    ClassConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof String) {
            String s = value.toString().trim();
            if (s.equals("boolean")) {
                return Boolean.TYPE;
            }
            if (s.equals("byte")) {
                return Byte.TYPE;
            }
            if (s.equals("short")) {
                return Short.TYPE;
            }
            if (s.equals("int")) {
                return Integer.TYPE;
            }
            if (s.equals("long")) {
                return Long.TYPE;
            }
            if (s.equals("float")) {
                return Float.TYPE;
            }
            if (s.equals("double")) {
                return Double.TYPE;
            }
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(value.toString());
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else if (value == null) {
            return null;
        } else {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        }
    }
}
