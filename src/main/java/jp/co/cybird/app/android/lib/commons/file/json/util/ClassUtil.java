package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class ClassUtil {
    private static final Map<ClassLoader, Map<String, Class<?>>> cache = new WeakHashMap();

    public static Class<?> findClass(String name) {
        ClassLoader cl;
        Map<String, Class<?>> map;
        Class<?> cls;
        Class<?> target;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException e) {
            cl = null;
        }
        synchronized (cache) {
            map = cache.get(cl);
            if (map == null) {
                map = new LinkedHashMap<String, Class<?>>(16, 0.75f, true) {
                    /* access modifiers changed from: protected */
                    public boolean removeEldestEntry(Map.Entry<String, Class<?>> entry) {
                        return size() > 1024;
                    }
                };
                cache.put(cl, map);
            }
        }
        synchronized (map) {
            if (!map.containsKey(name)) {
                if (cl != null) {
                    try {
                        target = cl.loadClass(name);
                    } catch (ClassNotFoundException e2) {
                        target = null;
                    }
                } else {
                    target = Class.forName(name);
                }
                map.put(name, target);
            }
            cls = map.get(name);
        }
        return cls;
    }

    public static void clear() {
        synchronized (cache) {
            cache.clear();
        }
    }

    public static String toUpperCamel(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        boolean toUpperCase = true;
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
        return sb.toString();
    }

    public static String toLowerCamel(String name) {
        StringBuilder sb = new StringBuilder(name.length());
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
        return sb.toString();
    }

    public static Class<?> getRawType(Type t) {
        if (t instanceof Class) {
            return (Class) t;
        }
        if (t instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) t).getRawType();
        }
        if (t instanceof GenericArrayType) {
            try {
                return Array.newInstance(getRawType(((GenericArrayType) t).getGenericComponentType()), 0).getClass();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (!(t instanceof WildcardType)) {
            return Object.class;
        } else {
            Type[] types = ((WildcardType) t).getUpperBounds();
            return types.length > 0 ? getRawType(types[0]) : Object.class;
        }
    }

    public static ParameterizedType resolveParameterizedType(Type t, Class<?> baseClass) {
        ParameterizedType pt;
        Class<?> raw = getRawType(t);
        if ((t instanceof ParameterizedType) && baseClass.isAssignableFrom(raw)) {
            return (ParameterizedType) t;
        }
        if (raw.getSuperclass() != null && raw.getSuperclass() != Object.class && (pt = resolveParameterizedType(raw.getGenericSuperclass(), baseClass)) != null) {
            return pt;
        }
        if (!raw.isInterface()) {
            for (Type ifs : raw.getGenericInterfaces()) {
                ParameterizedType pt2 = resolveParameterizedType(ifs, baseClass);
                if (pt2 != null) {
                    return pt2;
                }
            }
        }
        return null;
    }

    public static byte[] serialize(Object o) throws ObjectStreamException {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(array);
            try {
                out.writeObject(o);
                out.close();
                ObjectOutputStream objectOutputStream = out;
            } catch (ObjectStreamException e) {
                e = e;
                ObjectOutputStream objectOutputStream2 = out;
                throw e;
            } catch (IOException e2) {
                ObjectOutputStream objectOutputStream3 = out;
            }
        } catch (ObjectStreamException e3) {
            e = e3;
            throw e;
        } catch (IOException e4) {
        }
        return array.toByteArray();
    }

    public static Object deserialize(byte[] data) throws ObjectStreamException, ClassNotFoundException {
        Object ret = null;
        try {
            ContextObjectInputStream contextObjectInputStream = new ContextObjectInputStream(new ByteArrayInputStream(data));
            try {
                ret = contextObjectInputStream.readObject();
                contextObjectInputStream.close();
                ContextObjectInputStream contextObjectInputStream2 = contextObjectInputStream;
            } catch (ObjectStreamException e) {
                e = e;
                ContextObjectInputStream contextObjectInputStream3 = contextObjectInputStream;
                throw e;
            } catch (IOException e2) {
                ContextObjectInputStream contextObjectInputStream4 = contextObjectInputStream;
            }
        } catch (ObjectStreamException e3) {
            e = e3;
            throw e;
        } catch (IOException e4) {
        }
        return ret;
    }

    public static int hashCode(Object target) {
        if (target == null) {
            return 0;
        }
        int result = 1;
        Class cls = target.getClass();
        do {
            for (Field f : cls.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !f.isSynthetic()) {
                    try {
                        f.setAccessible(true);
                        Object self = f.get(target);
                        if (self == null) {
                            result = (result * 31) + 0;
                        } else if (!self.getClass().isArray()) {
                            result = (result * 31) + self.hashCode();
                        } else if (self.getClass().equals(boolean[].class)) {
                            result = (result * 31) + Arrays.hashCode((boolean[]) self);
                        } else if (self.getClass().equals(char[].class)) {
                            result = (result * 31) + Arrays.hashCode((char[]) self);
                        } else if (self.getClass().equals(byte[].class)) {
                            result = (result * 31) + Arrays.hashCode((byte[]) self);
                        } else if (self.getClass().equals(short[].class)) {
                            result = (result * 31) + Arrays.hashCode((short[]) self);
                        } else if (self.getClass().equals(int[].class)) {
                            result = (result * 31) + Arrays.hashCode((int[]) self);
                        } else if (self.getClass().equals(long[].class)) {
                            result = (result * 31) + Arrays.hashCode((long[]) self);
                        } else if (self.getClass().equals(float[].class)) {
                            result = (result * 31) + Arrays.hashCode((float[]) self);
                        } else if (self.getClass().equals(double[].class)) {
                            result = (result * 31) + Arrays.hashCode((double[]) self);
                        } else {
                            result = (result * 31) + Arrays.hashCode((Object[]) self);
                        }
                        System.out.println(String.valueOf(f.getName()) + ": " + result);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
            cls = cls.getSuperclass();
        } while (!Object.class.equals(cls));
        return result;
    }

    public static boolean equals(Object target, Object o) {
        if (target == o) {
            return true;
        }
        if (target == null || o == null) {
            return false;
        }
        if (!target.getClass().equals(o.getClass())) {
            return false;
        }
        Class cls = target.getClass();
        do {
            for (Field f : cls.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers()) && !f.isSynthetic()) {
                    try {
                        f.setAccessible(true);
                        Object self = f.get(target);
                        Object other = f.get(o);
                        if (self == null) {
                            if (other != null) {
                                return false;
                            }
                        } else if (self.getClass().isArray()) {
                            if (self.getClass().equals(boolean[].class)) {
                                if (!Arrays.equals((boolean[]) self, (boolean[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(char[].class)) {
                                if (!Arrays.equals((char[]) self, (char[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(byte[].class)) {
                                if (!Arrays.equals((byte[]) self, (byte[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(short[].class)) {
                                if (!Arrays.equals((short[]) self, (short[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(int[].class)) {
                                if (!Arrays.equals((int[]) self, (int[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(long[].class)) {
                                if (!Arrays.equals((long[]) self, (long[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(float[].class)) {
                                if (!Arrays.equals((float[]) self, (float[]) other)) {
                                    return false;
                                }
                            } else if (self.getClass().equals(double[].class)) {
                                if (!Arrays.equals((double[]) self, (double[]) other)) {
                                    return false;
                                }
                            } else if (!Arrays.equals((Object[]) self, (Object[]) other)) {
                                return false;
                            }
                        } else if (!self.equals(other)) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
            cls = cls.getSuperclass();
        } while (!Object.class.equals(cls));
        return true;
    }

    public static String toString(Object target) {
        if (target == null) {
            return "null";
        }
        BeanInfo info = BeanInfo.get(target.getClass());
        StringBuilder sb = new StringBuilder((info.getProperties().size() * 10) + 20);
        sb.append(target.getClass().getSimpleName()).append(" [");
        boolean first = true;
        for (PropertyInfo prop : info.getProperties()) {
            if (prop.isReadable() && !prop.getName().equals("class")) {
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }
                sb.append(prop.getName()).append("=");
                try {
                    Object value = prop.get(target);
                    if (!value.getClass().isArray()) {
                        sb.append(value);
                    } else if (value instanceof boolean[]) {
                        Arrays.toString((boolean[]) value);
                    } else if (value instanceof char[]) {
                        Arrays.toString((char[]) value);
                    } else if (value instanceof byte[]) {
                        Arrays.toString((byte[]) value);
                    } else if (value instanceof short[]) {
                        Arrays.toString((short[]) value);
                    } else if (value instanceof int[]) {
                        Arrays.toString((int[]) value);
                    } else if (value instanceof long[]) {
                        Arrays.toString((long[]) value);
                    } else if (value instanceof float[]) {
                        Arrays.toString((float[]) value);
                    } else if (value instanceof double[]) {
                        Arrays.toString((double[]) value);
                    } else {
                        Arrays.toString((Object[]) value);
                    }
                } catch (Exception e) {
                    sb.append("?");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private ClassUtil() {
    }

    private static class ContextObjectInputStream extends ObjectInputStream {
        public ContextObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        /* access modifiers changed from: protected */
        public Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            try {
                return Class.forName(desc.getName(), true, Thread.currentThread().getContextClassLoader());
            } catch (Exception e) {
                return super.resolveClass(desc);
            }
        }
    }
}
