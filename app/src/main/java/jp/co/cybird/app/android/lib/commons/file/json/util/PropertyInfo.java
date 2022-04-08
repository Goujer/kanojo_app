package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class PropertyInfo implements Comparable<PropertyInfo> {
    Class<?> beanClass;
    Field field;
    boolean isStatic;
    String name;
    int ordinal;
    Method readMethod;
    Method writeMethod;

    public PropertyInfo(Class<?> beanClass2, String name2, Field field2, Method readMethod2, Method writeMethod2, boolean isStatic2) {
        this(beanClass2, name2, field2, readMethod2, writeMethod2, isStatic2, -1);
    }

    public PropertyInfo(Class<?> beanClass2, String name2, Field field2, Method readMethod2, Method writeMethod2, boolean isStatic2, int ordinal2) {
        this.beanClass = beanClass2;
        this.name = name2;
        this.isStatic = isStatic2;
        this.field = field2;
        this.readMethod = readMethod2;
        this.writeMethod = writeMethod2;
        this.ordinal = ordinal2;
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public Field getField() {
        return this.field;
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    public boolean isReadable() {
        return (this.readMethod == null && this.field == null) ? false : true;
    }

    public Member getReadMember() {
        if (this.readMethod != null) {
            return this.readMethod;
        }
        if (this.field != null) {
            return this.field;
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not readable.");
    }

    public Class<?> getReadType() {
        if (this.readMethod != null) {
            return this.readMethod.getReturnType();
        }
        if (this.field != null) {
            return this.field.getType();
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not readable.");
    }

    public Type getReadGenericType() {
        if (this.readMethod != null) {
            return this.readMethod.getGenericReturnType();
        }
        if (this.field != null) {
            return this.field.getGenericType();
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not readable.");
    }

    public <T extends Annotation> T getReadAnnotation(Class<T> annotationClass) {
        if (this.readMethod != null) {
            return this.readMethod.getAnnotation(annotationClass);
        }
        if (this.field != null) {
            return this.field.getAnnotation(annotationClass);
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not readable.");
    }

    public Object get(Object target) {
        try {
            if (this.readMethod != null) {
                return this.readMethod.invoke(target, new Object[0]);
            }
            if (this.field != null) {
                return this.field.get(target);
            }
            throw new IllegalStateException(String.valueOf(this.name) + " property is not readable.");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw ((Error) e.getCause());
            } else if (e.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e.getCause());
            } else {
                throw new IllegalStateException(e.getCause());
            }
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new IllegalStateException(e3);
        }
    }

    public boolean isWritable() {
        return this.writeMethod != null || (this.field != null && !Modifier.isFinal(this.field.getModifiers()));
    }

    public Member getWriteMember() {
        if (this.writeMethod != null) {
            return this.writeMethod;
        }
        if (this.field != null && !Modifier.isFinal(this.field.getModifiers())) {
            return this.field;
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not writable.");
    }

    public Class<?> getWriteType() {
        if (this.writeMethod != null) {
            return this.writeMethod.getParameterTypes()[0];
        }
        if (this.field != null && !Modifier.isFinal(this.field.getModifiers())) {
            return this.field.getType();
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not writable.");
    }

    public Type getWriteGenericType() {
        if (this.writeMethod != null) {
            return this.writeMethod.getGenericParameterTypes()[0];
        }
        if (this.field != null && !Modifier.isFinal(this.field.getModifiers())) {
            return this.field.getGenericType();
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not writable.");
    }

    public <T extends Annotation> T getWriteAnnotation(Class<T> annotationClass) {
        if (this.writeMethod != null) {
            return this.writeMethod.getAnnotation(annotationClass);
        }
        if (this.field != null && !Modifier.isFinal(this.field.getModifiers())) {
            return this.field.getAnnotation(annotationClass);
        }
        throw new IllegalStateException(String.valueOf(this.name) + " property is not writable.");
    }

    public void set(Object target, Object value) {
        try {
            if (this.writeMethod != null) {
                this.writeMethod.invoke(target, new Object[]{value});
            } else if (this.field == null || Modifier.isFinal(this.field.getModifiers())) {
                throw new IllegalStateException(String.valueOf(this.name) + " property is not writable.");
            } else {
                this.field.set(target, value);
            }
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw ((Error) e.getCause());
            } else if (e.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e.getCause());
            } else {
                throw new IllegalStateException(e.getCause());
            }
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new IllegalStateException(e3);
        }
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public int compareTo(PropertyInfo property) {
        if (!this.beanClass.equals(property.beanClass)) {
            return this.beanClass.getName().compareTo(property.beanClass.getName());
        }
        if (this.ordinal >= 0) {
            if (property.ordinal < 0) {
                return -1;
            }
            if (this.ordinal > property.ordinal) {
                return 1;
            }
            if (this.ordinal < property.ordinal) {
                return -1;
            }
        } else if (property.ordinal >= 0) {
            return 1;
        }
        return this.name.compareTo(property.name);
    }

    public String toString() {
        return "Property [beanClass=" + this.beanClass + ", name=" + this.name + ", field=" + this.field + ", readMethod=" + this.readMethod + ", writeMethod=" + this.writeMethod + "]";
    }
}
