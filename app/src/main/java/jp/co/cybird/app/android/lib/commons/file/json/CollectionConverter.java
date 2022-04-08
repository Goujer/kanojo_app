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
        Collection<?> value2 = null;
        if (value instanceof Map) {
            Map<?, ?> src = (Map) value;
            if (!(src instanceof SortedMap)) {
                src = new TreeMap(src);
            }
            value2 = src.values();
        }
        Collection<Object> collection = (Collection) context.createInternal(c);
        t = ClassUtil.resolveParameterizedType(t, Collection.class);
        Class<?> pc = Object.class;
        Type pt = Object.class;
        if (t instanceof ParameterizedType) {
            Type[] pts = ((ParameterizedType) t).getActualTypeArguments();
            pt = (pts == null || pts.length <= 0) ? Object.class : pts[0];
            pc = ClassUtil.getRawType(pt);
        }
        if (value2 instanceof Collection) {
            Collection<?> src2 = value2;
            if (Object.class.equals(pc)) {
                collection.addAll(src2);
            } else {
                JSONHint hint = context.getHint();
                int i = 0;
                for (Object postparseInternal : src2) {
                    context.enter(Integer.valueOf(i), hint);
                    collection.add(context.postparseInternal(postparseInternal, pc, pt));
                    context.exit();
                    i++;
                }
            }
        } else if (Object.class.equals(pc)) {
            collection.add(value2);
        } else {
            context.enter(Integer.valueOf(0), context.getHint());
            collection.add(context.postparseInternal(value2, pc, pt));
            context.exit();
        }
        return collection;
    }
}
