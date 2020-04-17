package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class BeanInfo {
    private static final Map<ClassLoader, Map<Class<?>, BeanInfo>> cache = new WeakHashMap();
    private ConstructorInfo ci;
    private Map<String, MethodInfo> methods;
    private Map<String, PropertyInfo> props;
    private Map<String, MethodInfo> smethods;
    private Map<String, PropertyInfo> sprops;
    private Class<?> type;

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0031, code lost:
        return r1;
     */
    public static BeanInfo get(Class<?> cls) {
        BeanInfo info;
        BeanInfo info2;
        synchronized (cache) {
            try {
                Map<Class<?>, BeanInfo> map = cache.get(cls.getClassLoader());
                if (map == null) {
                    map = new LinkedHashMap<Class<?>, BeanInfo>(16, 0.75f, true) {
                        /* access modifiers changed from: protected */
                        public boolean removeEldestEntry(Map.Entry<Class<?>, BeanInfo> entry) {
                            return size() > 1024;
                        }
                    };
                    cache.put(cls.getClassLoader(), map);
                    info = null;
                } else {
                    info = map.get(cls);
                }
                if (info == null) {
                    try {
                        info2 = new BeanInfo(cls);
                        map.put(cls, info2);
                    } catch (Throwable th) {
                        th = th;
                        BeanInfo beanInfo = info;
                        throw th;
                    }
                } else {
                    info2 = info;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public static void clear() {
        synchronized (cache) {
            cache.clear();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:105:0x028c  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x02c2  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01b3  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01ec  */
    private BeanInfo(Class<?> cls) {
        MethodInfo mi;
        int type2;
        String name;
        PropertyInfo prop;
        this.type = cls;
        for (Constructor<?> con : cls.getConstructors()) {
            if (!con.isSynthetic()) {
                if (this.ci == null) {
                    this.ci = new ConstructorInfo(cls, (Collection<Constructor<?>>) null);
                }
                con.setAccessible(true);
                this.ci.constructors.add(con);
            }
        }
        for (Field f : cls.getFields()) {
            if (!f.isSynthetic()) {
                boolean isStatic = Modifier.isStatic(f.getModifiers());
                String name2 = f.getName();
                f.setAccessible(true);
                if (isStatic) {
                    if (this.sprops == null) {
                        this.sprops = Collections.synchronizedMap(new LinkedHashMap());
                    }
                    this.sprops.put(name2, new PropertyInfo(cls, name2, f, (Method) null, (Method) null, isStatic, -1));
                } else {
                    if (this.props == null) {
                        this.props = Collections.synchronizedMap(new LinkedHashMap());
                    }
                    this.props.put(name2, new PropertyInfo(cls, name2, f, (Method) null, (Method) null, isStatic, -1));
                }
            }
        }
        for (Method m : cls.getMethods()) {
            if (!m.isSynthetic() && !m.isBridge()) {
                String name3 = m.getName();
                Class[] paramTypes = m.getParameterTypes();
                Class<?> returnType = m.getReturnType();
                boolean isStatic2 = Modifier.isStatic(m.getModifiers());
                if (isStatic2) {
                    if (this.smethods == null) {
                        this.smethods = Collections.synchronizedMap(new LinkedHashMap());
                    }
                    mi = this.smethods.get(name3);
                    if (mi == null) {
                        mi = new MethodInfo(cls, name3, (Collection<Method>) null, isStatic2);
                        this.smethods.put(name3, mi);
                    }
                } else {
                    if (this.methods == null) {
                        this.methods = Collections.synchronizedMap(new LinkedHashMap());
                    }
                    mi = this.methods.get(name3);
                    if (mi == null) {
                        mi = new MethodInfo(cls, name3, (Collection<Method>) null, isStatic2);
                        this.methods.put(name3, mi);
                    }
                }
                m.setAccessible(true);
                mi.methods.add(m);
                if (name3.startsWith("get") && name3.length() > 3 && !Character.isLowerCase(name3.charAt(3)) && paramTypes.length == 0) {
                    if (!returnType.equals(Void.TYPE)) {
                        type2 = 1;
                        name = name3.substring(3);
                        if (name.length() < 2 || !Character.isUpperCase(name.charAt(1))) {
                            char[] chars = name.toCharArray();
                            chars[0] = Character.toLowerCase(chars[0]);
                            name = String.valueOf(chars);
                        }
                        if (isStatic2) {
                            if (this.sprops == null) {
                                this.sprops = Collections.synchronizedMap(new LinkedHashMap());
                            }
                            prop = this.sprops.get(name);
                            if (prop == null) {
                                prop = new PropertyInfo(cls, name, (Field) null, (Method) null, (Method) null, isStatic2, -1);
                                this.sprops.put(name, prop);
                            }
                        } else {
                            if (this.props == null) {
                                this.props = Collections.synchronizedMap(new LinkedHashMap());
                            }
                            prop = this.props.get(name);
                            if (prop == null) {
                                prop = new PropertyInfo(cls, name, (Field) null, (Method) null, (Method) null, isStatic2, -1);
                                this.props.put(name, prop);
                            }
                        }
                        if (type2 == 1) {
                            prop.readMethod = m;
                        } else {
                            prop.writeMethod = m;
                        }
                    }
                }
                if (name3.startsWith("is") && name3.length() > 2 && !Character.isLowerCase(name3.charAt(2)) && paramTypes.length == 0) {
                    if (!returnType.equals(Void.TYPE)) {
                        type2 = 1;
                        name = name3.substring(2);
                        char[] chars2 = name.toCharArray();
                        chars2[0] = Character.toLowerCase(chars2[0]);
                        name = String.valueOf(chars2);
                        if (isStatic2) {
                        }
                        if (type2 == 1) {
                        }
                    }
                }
                if (name3.startsWith("set") && name3.length() > 3 && !Character.isLowerCase(name3.charAt(3)) && paramTypes.length == 1 && !paramTypes[0].equals(Void.TYPE)) {
                    type2 = 2;
                    name = name3.substring(3);
                    char[] chars22 = name.toCharArray();
                    chars22[0] = Character.toLowerCase(chars22[0]);
                    name = String.valueOf(chars22);
                    if (isStatic2) {
                    }
                    if (type2 == 1) {
                    }
                }
            }
        }
        if (this.sprops == null) {
            this.sprops = Collections.emptyMap();
        }
        if (this.smethods == null) {
            this.smethods = Collections.emptyMap();
        }
        if (this.props == null) {
            this.props = Collections.emptyMap();
        }
        if (this.methods == null) {
            this.methods = Collections.emptyMap();
        }
    }

    public Object newInstance() {
        try {
            Constructor<?> target = this.type.getConstructor(new Class[0]);
            target.setAccessible(true);
            return target.newInstance(new Object[0]);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Class<?> getType() {
        return this.type;
    }

    public ConstructorInfo getConstructor() {
        return this.ci;
    }

    public PropertyInfo getStaticProperty(String name) {
        return this.sprops.get(name);
    }

    public MethodInfo getStaticMethod(String name) {
        return this.smethods.get(name);
    }

    public Collection<PropertyInfo> getStaticProperties() {
        return this.sprops.values();
    }

    public Collection<MethodInfo> getStaticMethods() {
        return this.smethods.values();
    }

    public PropertyInfo getProperty(String name) {
        return this.props.get(name);
    }

    public MethodInfo getMethod(String name) {
        return this.methods.get(name);
    }

    public Collection<PropertyInfo> getProperties() {
        return this.props.values();
    }

    public Collection<MethodInfo> getMethods() {
        return this.methods.values();
    }

    public int hashCode() {
        if (this.type == null) {
            return 0;
        }
        return this.type.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BeanInfo other = (BeanInfo) obj;
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        } else if (!this.type.equals(other.type)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return "BeanInfo [static properties = " + this.sprops + ", static methods = " + this.smethods + ", properties = " + this.props + ", methods = " + this.methods + "]";
    }

    static int calcurateDistance(Class<?>[] params, Object[] args) {
        int point = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                if (!params[i].isPrimitive()) {
                    point += 5;
                }
            } else if (params[i].equals(args[i].getClass())) {
                point += 10;
            } else if (params[i].isAssignableFrom(args[i].getClass())) {
                point += 8;
            } else if (Boolean.TYPE.equals(args[i].getClass()) || Boolean.class.equals(args[i].getClass())) {
                if (Boolean.TYPE.equals(params[i]) || Boolean.class.equals(params[i].getClass())) {
                    point += 10;
                }
            } else if (Byte.TYPE.equals(args[i].getClass()) || Byte.class.equals(args[i].getClass())) {
                if (Byte.TYPE.equals(params[i]) || Short.TYPE.equals(params[i]) || Character.TYPE.equals(params[i]) || Integer.TYPE.equals(params[i]) || Long.TYPE.equals(params[i]) || Float.TYPE.equals(params[i]) || Double.TYPE.equals(params[i]) || Byte.class.equals(params[i]) || Short.class.equals(params[i]) || Character.class.equals(params[i]) || Integer.class.equals(params[i]) || Long.class.equals(params[i]) || Float.class.equals(params[i]) || Double.class.equals(params[i])) {
                    point += 10;
                }
            } else if (Short.TYPE.equals(args[i].getClass()) || Short.class.equals(args[i].getClass()) || Character.TYPE.equals(args[i].getClass()) || Character.class.equals(args[i].getClass())) {
                if (Short.TYPE.equals(params[i]) || Character.TYPE.equals(params[i]) || Integer.TYPE.equals(params[i]) || Long.TYPE.equals(params[i]) || Float.TYPE.equals(params[i]) || Double.TYPE.equals(params[i]) || Short.class.equals(params[i]) || Character.class.equals(params[i]) || Integer.class.equals(params[i]) || Long.class.equals(params[i]) || Float.class.equals(params[i]) || Double.class.equals(params[i])) {
                    point += 10;
                }
            } else if (Integer.TYPE.equals(args[i].getClass()) || Integer.class.equals(args[i].getClass())) {
                if (Integer.TYPE.equals(params[i]) || Long.TYPE.equals(params[i]) || Float.TYPE.equals(params[i]) || Double.TYPE.equals(params[i]) || Integer.class.equals(params[i]) || Long.class.equals(params[i]) || Float.class.equals(params[i]) || Double.class.equals(params[i])) {
                    point += 10;
                }
            } else if (Long.TYPE.equals(args[i].getClass()) || Long.class.equals(args[i].getClass())) {
                if (Long.TYPE.equals(params[i]) || Float.TYPE.equals(params[i]) || Double.TYPE.equals(params[i]) || Long.class.equals(params[i]) || Float.class.equals(params[i]) || Double.class.equals(params[i])) {
                    point += 10;
                }
            } else if (Float.TYPE.equals(args[i].getClass()) || Float.class.equals(args[i].getClass())) {
                if (Float.TYPE.equals(params[i]) || Double.TYPE.equals(params[i]) || Float.class.equals(params[i]) || Double.class.equals(params[i])) {
                    point += 10;
                }
            } else if ((Double.TYPE.equals(args[i].getClass()) || Double.class.equals(args[i].getClass())) && (Double.TYPE.equals(params[i]) || Double.class.equals(params[i]))) {
                point += 10;
            }
        }
        return point;
    }
}
