package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class t implements o {
    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        if (webView instanceof AdWebView) {
            ((AdWebView) webView).setCustomClose(GreeDefs.KANOJO_NAME.equals(hashMap.get(AdActivity.CUSTOM_CLOSE_PARAM)));
        } else {
            b.b("Trying to set a custom close icon on a WebView that isn't an AdWebView");
        }
    }
}
