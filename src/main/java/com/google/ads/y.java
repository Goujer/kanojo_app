package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class y implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        b.c("Received log message: <\"string\": \"" + hashMap.get("string") + "\", \"afmaNotifyDt\": \"" + hashMap.get("afma_notify_dt") + "\">");
    }
}
