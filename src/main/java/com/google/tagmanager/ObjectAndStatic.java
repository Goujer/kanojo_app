package com.google.tagmanager;

class ObjectAndStatic<T> {
    private final boolean mIsStatic;
    private final T mObject;

    ObjectAndStatic(T object, boolean isStatic) {
        this.mObject = object;
        this.mIsStatic = isStatic;
    }

    public T getObject() {
        return this.mObject;
    }

    public boolean isStatic() {
        return this.mIsStatic;
    }
}
