package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Converter */
final class CollectionConverter implements Converter {
    public static final CollectionConverter INSTANCE = new CollectionConverter();

    CollectionConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (value instanceof Map) {
            Map<?, ?> src = (Map) value;
            if (!(src instanceof SortedMap)) {
                src = new TreeMap<>(src);
            }
            value = src.values();
        }
        Collection<Object> collection = (Collection) context.createInternal(c);
        Type t2 = ClassUtil.resolveParameterizedType(t, Collection.class);
        Class cls = Object.class;
        Class pt = Object.class;
        if (t2 instanceof ParameterizedType) {
            Type[] pts = ((ParameterizedType) t2).getActualTypeArguments();
            pt = (pts == null || pts.length <= 0) ? Object.class : pts[0];
            cls = ClassUtil.getRawType(pt);
        }
        if (value instanceof Collection) {
            Collection<?> src2 = (Collection) value;
            if (!Object.class.equals(cls)) {
                JSONHint hint = context.getHint();
                int i = 0;
                for (Object postparseInternal : src2) {
                    context.enter(Integer.valueOf(i), hint);
                    collection.add(context.postparseInternal(postparseInternal, cls, pt));
                    context.exit();
                    i++;
                }
            } else {
                collection.addAll(src2);
            }
        } else if (!Object.class.equals(cls)) {
            context.enter(0, context.getHint());
            collection.add(context.postparseInternal(value, cls, pt));
            context.exit();
        } else {
            collection.add(value);
        }
        return collection;
    }
}
