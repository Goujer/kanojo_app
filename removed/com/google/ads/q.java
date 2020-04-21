package com.google.ads;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.webkit.WebView;
import com.google.ads.internal.a;
import com.google.ads.internal.d;
import com.google.ads.util.b;
import java.util.HashMap;
import java.util.Map;

public class q implements o {
    private static final a a = a.a.b();

    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        boolean z;
        String str = hashMap.get("urls");
        if (str == null) {
            b.e("Could not get the urls param from canOpenURLs gmsg.");
            return;
        }
        String[] split = str.split(",");
        HashMap hashMap2 = new HashMap();
        PackageManager packageManager = webView.getContext().getPackageManager();
        for (String str2 : split) {
            String[] split2 = str2.split(";", 2);
            if (packageManager.resolveActivity(new Intent(split2.length >= 2 ? split2[1] : "android.intent.action.VIEW", Uri.parse(split2[0])), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) != null) {
                z = true;
            } else {
                z = false;
            }
            hashMap2.put(str2, Boolean.valueOf(z));
        }
        a.a(webView, (Map<String, Boolean>) hashMap2);
    }
}
