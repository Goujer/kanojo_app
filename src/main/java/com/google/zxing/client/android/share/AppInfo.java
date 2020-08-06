package com.google.zxing.client.android.share;

import android.graphics.drawable.Drawable;

final class AppInfo implements Comparable<AppInfo> {
    private final Drawable icon;
    private final String label;
    private final String packageName;

    AppInfo(String packageName2, String label2, Drawable icon2) {
        this.packageName = packageName2;
        this.label = label2;
        this.icon = icon2;
    }

    String getPackageName() {
        return this.packageName;
    }

    String getLabel() {
        return this.label;
    }

    Drawable getIcon() {
        return this.icon;
    }

    public String toString() {
        return this.label;
    }

    public int compareTo(AppInfo another) {
        return this.label.compareTo(another.label);
    }

    public int hashCode() {
        return this.label.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof AppInfo)) {
            return false;
        }
        return this.label.equals(((AppInfo) other).label);
    }
}
