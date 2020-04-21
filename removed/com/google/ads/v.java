package com.google.ads;

import android.webkit.WebView;
import com.google.ads.AdRequest;
import com.google.ads.internal.c;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class v implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        b.e("Invalid " + hashMap.get("type") + " request error: " + hashMap.get("errors"));
        c k = dVar.k();
        if (k != null) {
            k.a(AdRequest.ErrorCode.INVALID_REQUEST);
        }
    }
}
