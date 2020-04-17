package net.nend.android;

interface AdParameter {

    public enum ViewType {
        NONE,
        ADVIEW,
        WEBVIEW
    }

    String getClickUrl();

    int getHeight();

    String getImageUrl();

    int getReloadIntervalInSeconds();

    ViewType getViewType();

    String getWebViewUrl();

    int getWidth();
}
