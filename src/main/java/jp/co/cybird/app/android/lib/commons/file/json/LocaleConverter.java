package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class LocaleConverter implements Converter {
    public static final LocaleConverter INSTANCE = new LocaleConverter();

    LocaleConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) throws Exception {
        if (value instanceof List) {
            List<?> src = (List) value;
            if (src.size() == 1) {
                return new Locale(src.get(0).toString());
            }
            if (src.size() == 2) {
                return new Locale(src.get(0).toString(), src.get(1).toString());
            }
            if (src.size() > 2) {
                return new Locale(src.get(0).toString(), src.get(1).toString(), src.get(2).toString());
            }
            return null;
        }
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        }
        if (value instanceof String) {
            String[] array = value.toString().split("\\p{Punct}");
            if (array.length == 1) {
                return new Locale(array[0]);
            }
            if (array.length == 2) {
                return new Locale(array[0], array[1]);
            }
            if (array.length > 2) {
                return new Locale(array[0], array[1], array[2]);
            }
            return null;
        } else if (value == null) {
            return null;
        } else {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        }
    }
}
