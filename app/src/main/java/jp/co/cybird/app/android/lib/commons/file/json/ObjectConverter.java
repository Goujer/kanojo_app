package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.util.BeanInfo;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;
import jp.co.cybird.app.android.lib.commons.file.json.util.PropertyInfo;

/* compiled from: Converter */
final class ObjectConverter implements Converter {
    private Class<?> cls;
    private transient Map<String, PropertyInfo> props;

    public ObjectConverter(Class<?> cls2) {
        this.cls = cls2;
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        if (this.props == null) {
            this.props = getSetProperties(context, this.cls);
        }
        if (value instanceof Map) {
            Object o = context.createInternal(c);
            if (o == null) {
                return null;
            }
            for (Object entry : ((Map) value).entrySet()) {
                String name = ((Map.Entry)entry).getKey().toString();
                PropertyInfo target = this.props.get(name);
                if (target == null) {
                    target = this.props.get(toLowerCamel(context, name));
                }
                if (target != null) {
                    context.enter(name, (JSONHint) target.getWriteAnnotation(JSONHint.class));
                    Class<?> cls2 = target.getWriteType();
                    Type gtype = target.getWriteGenericType();
                    if ((gtype instanceof TypeVariable) && (t instanceof ParameterizedType)) {
                        gtype = resolveTypeVariable((TypeVariable) gtype, (ParameterizedType) t);
                        cls2 = ClassUtil.getRawType(gtype);
                    }
                    target.set(o, context.postparseInternal(((Map.Entry)entry).getValue(), cls2, gtype));
                    context.exit();
                }
            }
            return o;
        } else if (value instanceof List) {
            throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
        } else {
            JSONHint hint = context.getHint();
            if (hint == null || hint.anonym().length() <= 0) {
                throw new UnsupportedOperationException("Cannot convert " + value.getClass() + " to " + t);
            }
            PropertyInfo target2 = this.props.get(hint.anonym());
            if (target2 == null) {
                return null;
            }
            Object o2 = context.createInternal(c);
            if (o2 == null) {
                return null;
            }
            context.enter(hint.anonym(), (JSONHint) target2.getWriteAnnotation(JSONHint.class));
            Class<?> cls3 = target2.getWriteType();
            Type gtype2 = target2.getWriteGenericType();
            if ((gtype2 instanceof TypeVariable) && (t instanceof ParameterizedType)) {
                gtype2 = resolveTypeVariable((TypeVariable) gtype2, (ParameterizedType) t);
                cls3 = ClassUtil.getRawType(gtype2);
            }
            target2.set(o2, context.postparseInternal(value, cls3, gtype2));
            context.exit();
            return o2;
        }
    }

    private static Map<String, PropertyInfo> getSetProperties(JSON.JSONContext context, Class<?> c) {
        Map<String, PropertyInfo> props2 = new HashMap<>();
        for (PropertyInfo prop : BeanInfo.get(c).getProperties()) {
            Field f = prop.getField();
            if (f != null && !Modifier.isFinal(f.getModifiers()) && !context.ignoreInternal(c, f)) {
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
                if (name.equals(prop.getName()) && ordinal == prop.getOrdinal() && f == prop.getWriteMember()) {
                    props2.put(name, prop);
                } else {
                    props2.put(name, new PropertyInfo(prop.getBeanClass(), name, prop.getField(), (Method) null, (Method) null, prop.isStatic(), ordinal));
                }
            }
        }
        for (PropertyInfo prop2 : BeanInfo.get(c).getProperties()) {
            Method m = prop2.getWriteMethod();
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
                    props2.put(name2, new PropertyInfo(prop2.getBeanClass(), name2, (Field) null, (Method) null, prop2.getWriteMethod(), prop2.isStatic(), ordinal2));
                } else {
                    props2.put(name2, prop2);
                }
            }
        }
        return props2;
    }

    private static String toLowerCamel(JSON.JSONContext context, String name) {
        StringBuilder sb = context.getLocalCache().getCachedBuffer();
        boolean toUpperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == ' ' || c == '_' || c == '-') {
                toUpperCase = true;
            } else if (toUpperCase) {
                sb.append(Character.toUpperCase(c));
                toUpperCase = false;
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 1 && Character.isUpperCase(sb.charAt(0)) && !Character.isUpperCase(sb.charAt(1))) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
        return context.getLocalCache().getString(sb);
    }

    private static Type resolveTypeVariable(TypeVariable<?> type, ParameterizedType parent) {
        Class<?> rawType = ClassUtil.getRawType(parent);
        if (rawType.equals(type.getGenericDeclaration())) {
            String tvName = type.getName();
            TypeVariable[] rtypes = rawType.getTypeParameters();
            Type[] atypes = parent.getActualTypeArguments();
            for (int i = 0; i < rtypes.length; i++) {
                if (tvName.equals(rtypes[i].getName())) {
                    return atypes[i];
                }
            }
        }
        return type.getBounds()[0];
    }
}
