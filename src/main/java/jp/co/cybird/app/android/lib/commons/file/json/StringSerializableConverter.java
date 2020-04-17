package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class StringSerializableConverter implements Converter {
    public static final StringSerializableConverter INSTANCE = new StringSerializableConverter();

    StringSerializableConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (value instanceof String) {
            try {
                Constructor<?> con = c.getConstructor(new Class[]{String.class});
                con.setAccessible(true);
                return con.newInstance(new Object[]{value.toString()});
            } catch (NoSuchMethodException e) {
                return null;
            }
        } else if (value == null) {
            return null;
        } else {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        }
    }
}
