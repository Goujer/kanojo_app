package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.Base64;

/* compiled from: Converter */
final class ArrayConverter implements Converter {
    public static final ArrayConverter INSTANCE = new ArrayConverter();

    ArrayConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        Type pt;
        Type pt2;
        if (value instanceof Map) {
            Map<?, ?> src = (Map) value;
            if (!(src instanceof SortedMap)) {
                src = new TreeMap<>(src);
            }
            value = src.values();
        }
        if (value instanceof Collection) {
            Collection<?> src2 = (Collection) value;
            Object array = Array.newInstance(c.getComponentType(), src2.size());
            Class<?> pc = c.getComponentType();
            if (t instanceof GenericArrayType) {
                pt2 = ((GenericArrayType) t).getGenericComponentType();
            } else {
                pt2 = pc;
            }
            JSONHint hint = context.getHint();
            int i = 0;
            for (Object postparseInternal : src2) {
                context.enter(Integer.valueOf(i), hint);
                Array.set(array, i, context.postparseInternal(postparseInternal, pc, pt2));
                context.exit();
                i++;
            }
            return array;
        }
        Class<?> ctype = c.getComponentType();
        if (value instanceof String) {
            if (Byte.TYPE.equals(ctype)) {
                return Base64.decode((String) value);
            }
            if (Character.TYPE.equals(ctype)) {
                return ((String) value).toCharArray();
            }
        }
        Object array2 = Array.newInstance(ctype, 1);
        Class<?> pc2 = ctype;
        if (t instanceof GenericArrayType) {
            pt = ((GenericArrayType) t).getGenericComponentType();
        } else {
            pt = pc2;
        }
        context.enter(0, context.getHint());
        Array.set(array2, 0, context.postparseInternal(value, pc2, pt));
        context.exit();
        return array2;
    }
}
