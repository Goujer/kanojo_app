package com.google.zxing.client.android.common;

import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class PlatformSupportManager<T> {
    private static final String TAG = PlatformSupportManager.class.getSimpleName();
    private final T defaultImplementation;
    private final SortedMap<Integer, String> implementations;
    private final Class<T> managedInterface;

    protected PlatformSupportManager(Class<T> managedInterface2, T defaultImplementation2) {
        if (!managedInterface2.isInterface()) {
            throw new IllegalArgumentException();
        } else if (!managedInterface2.isInstance(defaultImplementation2)) {
            throw new IllegalArgumentException();
        } else {
            this.managedInterface = managedInterface2;
            this.defaultImplementation = defaultImplementation2;
            this.implementations = new TreeMap(Collections.reverseOrder());
        }
    }

    /* access modifiers changed from: protected */
    public final void addImplementationClass(int minVersion, String className) {
        this.implementations.put(Integer.valueOf(minVersion), className);
    }

    public final T build() {
        Iterator<Integer> it = this.implementations.keySet().iterator();
        while (it.hasNext()) {
            Integer minVersion = it.next();
            if (Build.VERSION.SDK_INT >= minVersion.intValue()) {
                try {
                    Class<? extends U> asSubclass = Class.forName((String) this.implementations.get(minVersion)).asSubclass(this.managedInterface);
                    Log.i(TAG, "Using implementation " + asSubclass + " of " + this.managedInterface + " for SDK " + minVersion);
                    return asSubclass.getConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (ClassNotFoundException cnfe) {
                    Log.w(TAG, cnfe);
                } catch (IllegalAccessException iae) {
                    Log.w(TAG, iae);
                } catch (InstantiationException ie) {
                    Log.w(TAG, ie);
                } catch (NoSuchMethodException nsme) {
                    Log.w(TAG, nsme);
                } catch (InvocationTargetException ite) {
                    Log.w(TAG, ite);
                }
            }
        }
        Log.i(TAG, "Using default implementation " + this.defaultImplementation.getClass() + " of " + this.managedInterface);
        return this.defaultImplementation;
    }
}
