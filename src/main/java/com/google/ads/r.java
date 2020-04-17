package com.google.ads;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebView;
import com.google.ads.internal.d;
import com.google.ads.internal.g;
import com.google.ads.util.b;
import java.util.HashMap;
import java.util.Locale;

public class r implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        Uri uri;
        Uri parse;
        String host;
        String str = hashMap.get(AdActivity.URL_PARAM);
        if (str == null) {
            b.e("Could not get URL from click gmsg.");
            return;
        }
        g n = dVar.n();
        if (!(n == null || (host = parse.getHost()) == null || !host.toLowerCase(Locale.US).endsWith(".admob.com"))) {
            String str2 = null;
            String path = (parse = Uri.parse(str)).getPath();
            if (path != null) {
                String[] split = path.split("/");
                if (split.length >= 4) {
                    str2 = split[2] + "/" + split[3];
                }
            }
            n.a(str2);
        }
        n i = dVar.i();
        Context a = i.f.a();
        Uri parse2 = Uri.parse(str);
        try {
            al a2 = i.s.a();
            if (a2 != null && a2.a(parse2)) {
                uri = a2.a(parse2, a);
                new Thread(new ae(uri.toString(), a)).start();
            }
        } catch (am e) {
            b.e("Unable to append parameter to URL: " + str);
        }
        uri = parse2;
        new Thread(new ae(uri.toString(), a)).start();
    }
}
