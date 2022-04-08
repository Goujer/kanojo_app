package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.BeanInfo;
import jp.co.cybird.app.android.lib.commons.file.json.util.PropertyInfo;

/* compiled from: Formatter */
final class ObjectFormatter implements Formatter {
    private Class<?> cls;
    private transient List<PropertyInfo> props;

    public ObjectFormatter(Class<?> cls2) {
        this.cls = cls2;
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        if (this.props == null) {
            this.props = getGetProperties(context, this.cls);
        }
        out.append('{');
        int count = 0;
        int length = this.props.size();
        for (int p = 0; p < length; p++) {
            PropertyInfo prop = this.props.get(p);
            Object value = null;
            Exception cause = null;
            try {
                value = prop.get(o);
                if (value != src && (!context.isSuppressNull() || value != null)) {
                    if (count != 0) {
                        out.append(',');
                    }
                    if (context.isPrettyPrint()) {
                        out.append('\n');
                        int indent = context.getInitialIndent() + context.getDepth() + 1;
                        for (int j = 0; j < indent; j++) {
                            out.append(context.getIndentText());
                        }
                    }
                    StringFormatter.serialize(context, prop.getName(), out);
                    out.append(':');
                    if (context.isPrettyPrint()) {
                        out.append(' ');
                    }
                    context.enter(prop.getName(), (JSONHint) prop.getReadAnnotation(JSONHint.class));
                    if (cause != null) {
                        throw cause;
                    }
                    context.formatInternal(context.preformatInternal(value), out);
                    context.exit();
                    count++;
                }
            } catch (Exception e) {
                cause = e;
            }
        }
        if (context.isPrettyPrint() && count > 0) {
            out.append('\n');
            int indent2 = context.getInitialIndent() + context.getDepth();
            for (int j2 = 0; j2 < indent2; j2++) {
                out.append(context.getIndentText());
            }
        }
        out.append('}');
        return true;
    }

    static List<PropertyInfo> getGetProperties(JSON.JSONContext context, Class<?> c) {
        Map<String, PropertyInfo> props2 = new HashMap<>();
        for (PropertyInfo prop : BeanInfo.get(c).getProperties()) {
            Field f = prop.getField();
            if (f != null && !context.ignoreInternal(c, f)) {
                JSONHint hint = (JSONHint) f.getAnnotation(JSONHint.class);
                String name = null;
                int ordinal = prop.getOrdinal();
                if (hint != null) {
                    if (!hint.ignore()) {
                        ordinal = hint.ordinal();
                        if (hint.name().length() != 0) {
                            name = hint.name();
                        }
                    }
                }
                if (name == null) {
                    name = context.normalizeInternal(prop.getName());
                    if (context.getPropertyStyle() != null) {
                        name = context.getPropertyStyle().to(name);
                    }
                }
                if (name.equals(prop.getName()) && ordinal == prop.getOrdinal() && f == prop.getReadMember()) {
                    props2.put(name, prop);
                } else {
                    props2.put(name, new PropertyInfo(prop.getBeanClass(), name, prop.getField(), (Method) null, (Method) null, prop.isStatic(), ordinal));
                }
            }
        }
        for (PropertyInfo prop2 : BeanInfo.get(c).getProperties()) {
            Method m = prop2.getReadMethod();
            if (m != null && !context.ignoreInternal(c, m)) {
                JSONHint hint2 = (JSONHint) m.getAnnotation(JSONHint.class);
                String name2 = null;
                int ordinal2 = prop2.getOrdinal();
                if (hint2 != null) {
                    if (!hint2.ignore()) {
                        ordinal2 = hint2.ordinal();
                        if (hint2.name().length() != 0) {
                            name2 = hint2.name();
                        }
                    }
                }
                if (name2 == null) {
                    name2 = context.normalizeInternal(prop2.getName());
                    if (context.getPropertyStyle() != null) {
                        name2 = context.getPropertyStyle().to(name2);
                    }
                }
                if (!name2.equals(prop2.getName()) || ordinal2 != prop2.getOrdinal()) {
                    props2.put(name2, new PropertyInfo(prop2.getBeanClass(), name2, (Field) null, prop2.getReadMethod(), (Method) null, prop2.isStatic(), ordinal2));
                } else {
                    props2.put(name2, prop2);
                }
            }
        }
        List<PropertyInfo> list = new ArrayList<>(props2.values());
        Collections.sort(list);
        return list;
    }
}
