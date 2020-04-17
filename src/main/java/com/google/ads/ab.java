package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;

public class ab implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        if (dVar.i().c.a() == null) {
            b.e("Activity was null while responding to touch gmsg.");
            return;
        }
        String str = hashMap.get("tx");
        String str2 = hashMap.get("ty");
        String str3 = hashMap.get("td");
        try {
            int parseInt = Integer.parseInt(str);
            int parseInt2 = Integer.parseInt(str2);
            int parseInt3 = Integer.parseInt(str3);
            ak a = dVar.i().r.a();
            if (a != null) {
                a.a(parseInt, parseInt2, parseInt3);
            }
        } catch (NumberFormatException e) {
            b.e("Could not parse touch parameters from gmsg.");
        }
    }
}
