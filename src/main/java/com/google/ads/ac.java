package com.google.ads;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.webkit.WebView;
import com.google.ads.internal.AdVideoView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.a;
import com.google.ads.internal.d;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import java.util.HashMap;

public class ac implements o {
    private static final a a = a.a.b();

    /* access modifiers changed from: protected */
    public int a(HashMap<String, String> hashMap, String str, int i, DisplayMetrics displayMetrics) {
        String str2 = hashMap.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return (int) TypedValue.applyDimension(1, (float) Integer.parseInt(str2), displayMetrics);
        } catch (NumberFormatException e) {
            b.a("Could not parse \"" + str + "\" in a video gmsg: " + str2);
            return i;
        }
    }

    public void a(d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("action");
        if (str == null) {
            b.a("No \"action\" parameter in a video gmsg.");
        } else if (webView instanceof AdWebView) {
            AdWebView adWebView = (AdWebView) webView;
            AdActivity i = adWebView.i();
            if (i == null) {
                b.a("Could not get adActivity for a video gmsg.");
                return;
            }
            boolean equals = str.equals("new");
            boolean equals2 = str.equals("position");
            if (equals || equals2) {
                DisplayMetrics a2 = AdUtil.a((Activity) i);
                int a3 = a(hashMap, "x", 0, a2);
                int a4 = a(hashMap, "y", 0, a2);
                int a5 = a(hashMap, "w", -1, a2);
                int a6 = a(hashMap, "h", -1, a2);
                if (!equals || i.getAdVideoView() != null) {
                    i.moveAdVideoView(a3, a4, a5, a6);
                } else {
                    i.newAdVideoView(a3, a4, a5, a6);
                }
            } else {
                AdVideoView adVideoView = i.getAdVideoView();
                if (adVideoView == null) {
                    a.a(adWebView, "onVideoEvent", "{'event': 'error', 'what': 'no_video_view'}");
                } else if (str.equals("click")) {
                    DisplayMetrics a7 = AdUtil.a((Activity) i);
                    int a8 = a(hashMap, "x", 0, a7);
                    int a9 = a(hashMap, "y", 0, a7);
                    long uptimeMillis = SystemClock.uptimeMillis();
                    adVideoView.a(MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, (float) a8, (float) a9, 0));
                } else if (str.equals("controls")) {
                    String str2 = hashMap.get("enabled");
                    if (str2 == null) {
                        b.a("No \"enabled\" parameter in a controls video gmsg.");
                    } else if (str2.equals("true")) {
                        adVideoView.setMediaControllerEnabled(true);
                    } else {
                        adVideoView.setMediaControllerEnabled(false);
                    }
                } else if (str.equals("currentTime")) {
                    String str3 = hashMap.get("time");
                    if (str3 == null) {
                        b.a("No \"time\" parameter in a currentTime video gmsg.");
                        return;
                    }
                    try {
                        adVideoView.a((int) (Float.parseFloat(str3) * 1000.0f));
                    } catch (NumberFormatException e) {
                        b.a("Could not parse \"time\" parameter: " + str3);
                    }
                } else if (str.equals("hide")) {
                    adVideoView.setVisibility(4);
                } else if (str.equals("load")) {
                    adVideoView.b();
                } else if (str.equals("pause")) {
                    adVideoView.c();
                } else if (str.equals("play")) {
                    adVideoView.d();
                } else if (str.equals("show")) {
                    adVideoView.setVisibility(0);
                } else if (str.equals("src")) {
                    adVideoView.setSrc(hashMap.get("src"));
                } else {
                    b.a("Unknown video action: " + str);
                }
            }
        } else {
            b.a("Could not get adWebView for a video gmsg.");
        }
    }
}
