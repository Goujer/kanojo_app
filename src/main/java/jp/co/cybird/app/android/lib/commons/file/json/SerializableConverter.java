package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.Base64;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Converter */
final class SerializableConverter implements Converter {
    public static final SerializableConverter INSTANCE = new SerializableConverter();

    SerializableConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof String) {
            return ClassUtil.deserialize(Base64.decode((String) value));
        }
        if (value == null) {
            return null;
        }
        throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
    }
}
