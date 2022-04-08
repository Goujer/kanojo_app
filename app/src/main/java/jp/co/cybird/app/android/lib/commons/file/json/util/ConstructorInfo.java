package jp.co.cybird.app.android.lib.commons.file.json.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstructorInfo implements Comparable<ConstructorInfo> {
    private Class<?> beanClass;
    List<Constructor<?>> constructors = new ArrayList();

    public ConstructorInfo(Class<?> beanClass2, Collection<Constructor<?>> constructors2) {
        this.beanClass = beanClass2;
        if (constructors2 != null) {
            this.constructors.addAll(constructors2);
        }
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    public Object newInstance(Object... args) {
        try {
            return findConstructor(args).newInstance(args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new IllegalStateException(e2);
        }
    }

    public int compareTo(ConstructorInfo constructor) {
        if (!this.beanClass.equals(constructor.beanClass)) {
            return this.beanClass.getName().compareTo(constructor.beanClass.getName());
        }
        return 0;
    }

    public Constructor<?> findConstructor(Object... args) {
        Constructor<?> constructor = null;
        Class[] types = null;
        Constructor<?> vconstructor = null;
        Class[] vtypes = null;
        for (Constructor<?> cconstructor : this.constructors) {
            Class[] cparams = cconstructor.getParameterTypes();
            if (cconstructor.isVarArgs()) {
                if (args.length >= cparams.length - 1) {
                    if (vconstructor == null) {
                        Class<?> vtype = cparams[cparams.length - 1].getComponentType();
                        Class[] tmp = new Class[args.length];
                        System.arraycopy(cparams, 0, tmp, 0, cparams.length - 1);
                        for (int i = cparams.length - 1; i < tmp.length; i++) {
                            tmp[i] = vtype;
                        }
                        vconstructor = cconstructor;
                        vtypes = tmp;
                    } else {
                        int vpoint = BeanInfo.calcurateDistance(vtypes, args);
                        int cpoint = BeanInfo.calcurateDistance(cparams, args);
                        if (cpoint > vpoint) {
                            vconstructor = cconstructor;
                            vtypes = cparams;
                        } else if (cpoint == vpoint) {
                            Class[] cparams2 = null;
                        }
                    }
                }
            } else if (args.length == cparams.length) {
                if (constructor == null) {
                    constructor = cconstructor;
                    types = cparams;
                } else {
                    int point = BeanInfo.calcurateDistance(types, args);
                    int cpoint2 = BeanInfo.calcurateDistance(cparams, args);
                    if (cpoint2 > point) {
                        constructor = cconstructor;
                        types = cparams;
                    } else if (cpoint2 == point) {
                        Class[] clsArr = null;
                    }
                }
            }
        }
        if (vconstructor != null) {
            if (constructor == null) {
                constructor = vconstructor;
            } else {
                if (BeanInfo.calcurateDistance(vtypes, args) > BeanInfo.calcurateDistance(types, args)) {
                    constructor = vconstructor;
                }
            }
        }
        if (constructor != null) {
            return constructor;
        }
        throw new IllegalStateException("suitable constructor is not found.");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.beanClass == null ? 0 : this.beanClass.hashCode()) + 31) * 31;
        if (this.constructors != null) {
            i = this.constructors.hashCode();
        }
        return hashCode + i;
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
        ConstructorInfo other = (ConstructorInfo) obj;
        if (this.beanClass == null) {
            if (other.beanClass != null) {
                return false;
            }
        } else if (!this.beanClass.equals(other.beanClass)) {
            return false;
        }
        if (this.constructors == null) {
            if (other.constructors != null) {
                return false;
            }
            return true;
        } else if (!this.constructors.equals(other.constructors)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return "ConstructorInfo [beanClass=" + this.beanClass + ", constructors=" + this.constructors + "]";
    }
}
