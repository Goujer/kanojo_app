package com.google.ads;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.c;
import com.google.ads.internal.d;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class w implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        c.d dVar2;
        String str = hashMap.get("url");
        String str2 = hashMap.get("type");
        String str3 = hashMap.get("afma_notify_dt");
        String str4 = hashMap.get("activation_overlay_url");
        String str5 = hashMap.get("check_packages");
        boolean equals = GreeDefs.KANOJO_NAME.equals(hashMap.get("drt_include"));
        String str6 = hashMap.get("request_scenario");
        boolean equals2 = GreeDefs.KANOJO_NAME.equals(hashMap.get("use_webview_loadurl"));
        if (c.d.OFFLINE_EMPTY.e.equals(str6)) {
            dVar2 = c.d.OFFLINE_EMPTY;
        } else if (c.d.OFFLINE_USING_BUFFERED_ADS.e.equals(str6)) {
            dVar2 = c.d.OFFLINE_USING_BUFFERED_ADS;
        } else if (c.d.ONLINE_USING_BUFFERED_ADS.e.equals(str6)) {
            dVar2 = c.d.ONLINE_USING_BUFFERED_ADS;
        } else {
            dVar2 = c.d.ONLINE_SERVER_REQUEST;
        }
        b.c("Received ad url: <url: \"" + str + "\" type: \"" + str2 + "\" afmaNotifyDt: \"" + str3 + "\" activationOverlayUrl: \"" + str4 + "\" useWebViewLoadUrl: \"" + equals2 + "\">");
        if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str)) {
            BigInteger bigInteger = new BigInteger(new byte[1]);
            String[] split = str5.split(",");
            BigInteger bigInteger2 = bigInteger;
            for (int i = 0; i < split.length; i++) {
                if (AdUtil.a((Context) dVar.i().c.a(), split[i])) {
                    bigInteger2 = bigInteger2.setBit(i);
                }
            }
            String format = String.format(Locale.US, "%X", new Object[]{bigInteger2});
            str = str.replaceAll("%40installed_markets%40", format);
            m.a().a.a(format);
            b.c("Ad url modified to " + str);
        }
        c k = dVar.k();
        if (k != null) {
            k.d(equals);
            k.a(dVar2);
            k.e(equals2);
            k.e(str4);
            k.d(str);
        }
    }
}
