package com.google.tagmanager;

import com.google.tagmanager.LoadCallback;

class TypeOrFailure<T> {
    private LoadCallback.Failure mFailure;
    private T mType;

    public TypeOrFailure(T type) {
        this.mType = type;
    }

    public TypeOrFailure(LoadCallback.Failure failure) {
        this.mFailure = failure;
    }

    public T getType() {
        return this.mType;
    }

    public LoadCallback.Failure getFailure() {
        return this.mFailure;
    }
}
