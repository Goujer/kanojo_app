package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MethodInfo implements Iterable<Method>, Comparable<MethodInfo> {
    private Class<?> beanClass;
    private boolean isStatic;
    List<Method> methods = new ArrayList();
    private String name;

    public MethodInfo(Class<?> beanClass2, String name2, Collection<Method> methods2, boolean isStatic2) {
        this.beanClass = beanClass2;
        this.name = name2;
        this.isStatic = isStatic2;
        if (methods2 != null) {
            this.methods.addAll(methods2);
        }
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

    public Object invoke(Object instance, Object... args) {
        try {
            return findMethod(args).invoke(instance, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new IllegalStateException(e2);
        }
    }

    public int compareTo(MethodInfo method) {
        if (!this.beanClass.equals(method.beanClass)) {
            return this.beanClass.getName().compareTo(method.beanClass.getName());
        }
        return this.name.compareTo(method.name);
    }

    public Method findMethod(Object... args) {
        Method method = null;
        Class[] types = null;
        Method vmethod = null;
        Class[] vtypes = null;
        for (Method cmethod : this.methods) {
            Class[] cparams = cmethod.getParameterTypes();
            if (cmethod.isVarArgs()) {
                if (args.length >= cparams.length - 1) {
                    if (vmethod == null) {
                        Class<?> vtype = cparams[cparams.length - 1].getComponentType();
                        Class[] tmp = new Class[args.length];
                        System.arraycopy(cparams, 0, tmp, 0, cparams.length - 1);
                        for (int i = cparams.length - 1; i < tmp.length; i++) {
                            tmp[i] = vtype;
                        }
                        vmethod = cmethod;
                        vtypes = tmp;
                    } else {
                        int vpoint = BeanInfo.calcurateDistance(vtypes, args);
                        int cpoint = BeanInfo.calcurateDistance(cparams, args);
                        if (cpoint > vpoint) {
                            vmethod = cmethod;
                            vtypes = cparams;
                        } else if (cpoint == vpoint) {
                            Class[] cparams2 = null;
                        }
                    }
                }
            } else if (args.length == cparams.length) {
                if (method == null) {
                    method = cmethod;
                    types = cparams;
                } else {
                    int point = BeanInfo.calcurateDistance(types, args);
                    int cpoint2 = BeanInfo.calcurateDistance(cparams, args);
                    if (cpoint2 > point) {
                        method = cmethod;
                        types = cparams;
                    } else if (cpoint2 == point) {
                        Class[] clsArr = null;
                    }
                }
            }
        }
        if (vmethod != null) {
            if (method == null) {
                method = vmethod;
            } else {
                if (BeanInfo.calcurateDistance(vtypes, args) > BeanInfo.calcurateDistance(types, args)) {
                    method = vmethod;
                }
            }
        }
        if (method != null) {
            return method;
        }
        throw new IllegalStateException("suitable method is not found: " + this.name);
    }

    public Iterator<Method> iterator() {
        return this.methods.iterator();
    }

    public int size() {
        return this.methods.size();
    }
}
