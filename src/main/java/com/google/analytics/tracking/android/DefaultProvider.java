package com.google.analytics.tracking.android;

interface DefaultProvider {
    String getValue(String str);

    boolean providesField(String str);
}
