package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebView;

final class NendAdWebView extends WebView {
    @SuppressLint({"SetJavaScriptEnabled"})
    NendAdWebView(Context context) {
        super(context);
        getSettings().setJavaScriptEnabled(true);
        setBackgroundColor(0);
    }
}
