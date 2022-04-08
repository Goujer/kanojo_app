package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class PlainConverter implements Converter {
    public static final PlainConverter INSTANCE = new PlainConverter();
    private static final Map<Class<?>, Object> PRIMITIVE_MAP = new HashMap(8);

    PlainConverter() {
    }

    static {
        PRIMITIVE_MAP.put(Boolean.TYPE, false);
        PRIMITIVE_MAP.put(Byte.TYPE, (byte) 0);
        PRIMITIVE_MAP.put(Short.TYPE, (short) 0);
        PRIMITIVE_MAP.put(Integer.TYPE, 0);
        PRIMITIVE_MAP.put(Long.TYPE, 0L);
        PRIMITIVE_MAP.put(Float.TYPE, Float.valueOf(0.0f));
        PRIMITIVE_MAP.put(Double.TYPE, Double.valueOf(0.0d));
        PRIMITIVE_MAP.put(Character.TYPE, 0);
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) {
        return value;
    }

    public static Object getDefaultValue(Class<?> cls) {
        return PRIMITIVE_MAP.get(cls);
    }
}
