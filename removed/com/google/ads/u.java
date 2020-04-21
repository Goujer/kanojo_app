package com.google.ads;

import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class u implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get(AdActivity.URL_PARAM);
        if (TextUtils.isEmpty(str)) {
            b.e("Could not get URL from track gmsg.");
        } else {
            new Thread(new ae(str, dVar.i().f.a())).start();
        }
    }
}
