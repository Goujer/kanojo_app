package com.google.tagmanager;

interface HitSender {
    boolean sendHit(String str);

    void setUrlWrapModeForTesting(String str, String str2);
}
