package com.google.ads;

import android.webkit.WebView;
import com.google.ads.AdActivity;
import com.google.ads.internal.d;
import com.google.ads.internal.e;
import com.google.ads.util.b;
import java.util.HashMap;

public class z implements o {
    private AdActivity.StaticMethodWrapper a;

    public z() {
        this(new AdActivity.StaticMethodWrapper());
    }

    public z(AdActivity.StaticMethodWrapper staticMethodWrapper) {
        this.a = staticMethodWrapper;
    }

    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("a");
        if (str == null) {
            b.a("Could not get the action parameter for open GMSG.");
        } else if (str.equals("webapp")) {
            this.a.launchAdActivity(dVar, new e("webapp", hashMap));
        } else if (str.equals("expand")) {
            this.a.launchAdActivity(dVar, new e("expand", hashMap));
        } else {
            this.a.launchAdActivity(dVar, new e("intent", hashMap));
        }
    }
}
