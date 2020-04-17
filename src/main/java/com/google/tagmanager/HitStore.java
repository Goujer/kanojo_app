package com.google.tagmanager;

interface HitStore {
    void close();

    void dispatch();

    Dispatcher getDispatcher();

    void putHit(long j, String str);
}
