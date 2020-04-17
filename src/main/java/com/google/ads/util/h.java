package com.google.ads.util;

import android.annotation.TargetApi;
import android.webkit.WebSettings;
import com.google.ads.n;

@TargetApi(17)
public final class h {
    public static void a(WebSettings webSettings, n nVar) {
        g.a(webSettings, nVar);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }
}
