package net.nend.android;

interface Ad extends AdParameter {
    void cancelRequest();

    String getUid();

    boolean isRequestable();

    void removeListener();

    boolean requestAd();

    void setListener(AdListener adListener);
}
