package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class EnumConverter implements Converter {
    public static final EnumConverter INSTANCE = new EnumConverter();

    EnumConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        Enum[] enums = (Enum[]) c.getEnumConstants();
        if (value instanceof Number) {
            return enums[((Number) value).intValue()];
        }
        if (value instanceof Boolean) {
            return enums[((Boolean) value).booleanValue() ? (char) 1 : 0];
        } else if (value == null) {
            return null;
        } else {
            String str = value.toString().trim();
            if (str.length() == 0) {
                return null;
            }
            if (Character.isDigit(str.charAt(0))) {
                return enums[Integer.parseInt(str)];
            }
            for (Enum e : enums) {
                if (str.equals(e.name())) {
                    return e;
                }
            }
            if (context.getEnumStyle() != null) {
                for (Enum e2 : enums) {
                    if (str.equals(context.getEnumStyle().to(e2.name()))) {
                        return e2;
                    }
                }
            }
            throw new IllegalArgumentException(String.valueOf(str) + " is not " + c);
        }
    }
}
