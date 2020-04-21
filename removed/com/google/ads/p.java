package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class p implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("name");
        if (str == null) {
            b.b("Error: App event with no name parameter.");
        } else {
            dVar.a(str, hashMap.get("info"));
        }
    }
}
