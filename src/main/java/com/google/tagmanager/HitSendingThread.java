package com.google.tagmanager;

interface HitSendingThread {
    void queueToThread(Runnable runnable);

    void sendHit(String str);
}
