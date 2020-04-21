package com.google.ads;

import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.ActivationOverlay;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class aa implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        int i;
        int i2;
        int i3;
        int i4 = -1;
        if (webView instanceof ActivationOverlay) {
            try {
                if (!TextUtils.isEmpty(hashMap.get("w"))) {
                    i = Integer.parseInt(hashMap.get("w"));
                } else {
                    i = -1;
                }
                if (!TextUtils.isEmpty(hashMap.get("h"))) {
                    i2 = Integer.parseInt(hashMap.get("h"));
                } else {
                    i2 = -1;
                }
                if (!TextUtils.isEmpty(hashMap.get("x"))) {
                    i3 = Integer.parseInt(hashMap.get("x"));
                } else {
                    i3 = -1;
                }
                if (!TextUtils.isEmpty(hashMap.get("y"))) {
                    i4 = Integer.parseInt(hashMap.get("y"));
                }
                if (hashMap.get("a") != null && hashMap.get("a").equals(GreeDefs.KANOJO_NAME)) {
                    dVar.a((l) null, true, i3, i4, i, i2);
                } else if (hashMap.get("a") == null || !hashMap.get("a").equals(GreeDefs.BARCODE)) {
                    dVar.a(i3, i4, i, i2);
                } else {
                    dVar.a((l) null, false, i3, i4, i, i2);
                }
            } catch (NumberFormatException e) {
                b.d("Invalid number format in activation overlay response.", e);
            }
        } else {
            b.b("Trying to activate an overlay when this is not an overlay.");
        }
    }
}
