package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class s implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        if (webView instanceof AdWebView) {
            ((AdWebView) webView).f();
        } else {
            b.b("Trying to close WebView that isn't an AdWebView");
        }
    }
}
