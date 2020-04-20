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
        t = ClassUtil.resolveParameterizedType(t, Map.class);
        Type pt0 = Object.class;
        Type pt1 = Object.class;
        Class<?> pc0 = Object.class;
        Class<?> pc1 = Object.class;
        if (t instanceof ParameterizedType) {
            Type[] pts = ((ParameterizedType) t).getActualTypeArguments();
            pt0 = (pts == null || pts.length <= 0) ? Object.class : pts[0];
            pt1 = (pts == null || pts.length <= 1) ? Object.class : pts[1];
            pc0 = ClassUtil.getRawType(pt0);
            pc1 = ClassUtil.getRawType(pt1);
        }
        JSONHint hint;
        Object key;
        if (value instanceof Map) {
            if (Object.class.equals(pc0) && Object.class.equals(pc1)) {
                map.putAll((Map) value);
            } else {
                hint = context.getHint();
                for (Object entry : ((Map) value).entrySet()) {
                    context.enter(Character.valueOf('.'), hint);
                    key = context.postparseInternal(((Map.Entry)entry).getKey(), pc0, pt0);
                    context.exit();
                    context.enter(((Map.Entry)entry).getKey(), hint);
                    map.put(key, context.postparseInternal(((Map.Entry)entry).getValue(), pc1, pt1));
                    context.exit();
                }
            }
        } else if (!(value instanceof List)) {
            hint = context.getHint();
            key = (hint == null || hint.anonym().length() <= 0) ? null : hint.anonym();
            if (Object.class.equals(pc0) && Object.class.equals(pc1)) {
                map.put(value, null);
            } else {
                context.enter(Character.valueOf('.'), hint);
                key = context.postparseInternal(key, pc0, pt0);
                context.exit();
                context.enter(key, hint);
                map.put(key, context.postparseInternal(value, pc1, pt1));
                context.exit();
            }
        } else if (Object.class.equals(pc0) && Object.class.equals(pc1)) {
            List src = (List) value;
            for (int i = 0; i < src.size(); i++) {
                map.put(Integer.valueOf(i), src.get(i));
            }
        } else {
            List src = (List) value;
            hint = context.getHint();
            for (int i = 0; i < src.size(); i++) {
                context.enter(Character.valueOf('.'), hint);
                key = context.postparseInternal(Integer.valueOf(i), pc0, pt0);
                context.exit();
                context.enter(Integer.valueOf(i), hint);
                map.put(key, context.postparseInternal(src.get(i), pc1, pt1));
                context.exit();
            }
        }
        return map;
    }
}
