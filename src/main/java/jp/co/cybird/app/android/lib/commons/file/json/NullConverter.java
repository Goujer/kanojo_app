package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class NullConverter implements Converter {
    public static final NullConverter INSTANCE = new NullConverter();

    NullConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> cls, Type t) {
        return null;
    }
}
