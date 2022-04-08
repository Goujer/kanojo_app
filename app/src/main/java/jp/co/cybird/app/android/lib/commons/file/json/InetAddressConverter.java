package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Converter */
final class InetAddressConverter implements Converter {
    public static final InetAddressConverter INSTANCE = new InetAddressConverter();

    InetAddressConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value == null) {
            return null;
        }
        return ClassUtil.findClass("java.net.InetAddress").getMethod("getByName", new Class[]{String.class}).invoke((Object) null, new Object[]{value.toString().trim()});
    }
}
