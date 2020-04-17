package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Converter */
final class MapConverter implements Converter {
    public static final MapConverter INSTANCE = new MapConverter();

    MapConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        Map<Object, Object> map = (Map) context.createInternal(c);
        Type t2 = ClassUtil.resolveParameterizedType(t, Map.class);
        Class pt0 = Object.class;
        Class pt1 = Object.class;
        Class cls = Object.class;
        Class cls2 = Object.class;
        if (t2 instanceof ParameterizedType) {
            Type[] pts = ((ParameterizedType) t2).getActualTypeArguments();
            pt0 = (pts == null || pts.length <= 0) ? Object.class : pts[0];
            pt1 = (pts == null || pts.length <= 1) ? Object.class : pts[1];
            cls = ClassUtil.getRawType(pt0);
            cls2 = ClassUtil.getRawType(pt1);
        }
        if (value instanceof Map) {
            if (!Object.class.equals(cls) || !Object.class.equals(cls2)) {
                JSONHint hint = context.getHint();
                for (Map.Entry<?, ?> entry : ((Map) value).entrySet()) {
                    context.enter('.', hint);
                    Object key = context.postparseInternal(entry.getKey(), cls, pt0);
                    context.exit();
                    context.enter(entry.getKey(), hint);
                    map.put(key, context.postparseInternal(entry.getValue(), cls2, pt1));
                    context.exit();
                }
            } else {
                map.putAll((Map) value);
            }
        } else if (!(value instanceof List)) {
            JSONHint hint2 = context.getHint();
            String key2 = (hint2 == null || hint2.anonym().length() <= 0) ? null : hint2.anonym();
            if (!Object.class.equals(cls) || !Object.class.equals(cls2)) {
                context.enter('.', hint2);
                Object key3 = context.postparseInternal(key2, cls, pt0);
                context.exit();
                context.enter(key3, hint2);
                map.put(key3, context.postparseInternal(value, cls2, pt1));
                context.exit();
            } else {
                map.put(value, (Object) null);
            }
        } else if (!Object.class.equals(cls) || !Object.class.equals(cls2)) {
            List<?> src = (List) value;
            JSONHint hint3 = context.getHint();
            for (int i = 0; i < src.size(); i++) {
                context.enter('.', hint3);
                Object key4 = context.postparseInternal(Integer.valueOf(i), cls, pt0);
                context.exit();
                context.enter(Integer.valueOf(i), hint3);
                map.put(key4, context.postparseInternal(src.get(i), cls2, pt1));
                context.exit();
            }
        } else {
            List<?> src2 = (List) value;
            for (int i2 = 0; i2 < src2.size(); i2++) {
                map.put(Integer.valueOf(i2), src2.get(i2));
            }
        }
        return map;
    }
}
