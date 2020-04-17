package com.google.tagmanager;

interface LoadCallback<T> {

    public enum Failure {
        NOT_AVAILABLE,
        IO_ERROR,
        SERVER_ERROR
    }

    void onFailure(Failure failure);

    void onSuccess(T t);

    void startLoad();
}
