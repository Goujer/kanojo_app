package com.google.ads.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.ads.AdActivity;
import com.google.ads.AdRequest;
import com.google.ads.al;
import com.google.ads.am;
import com.google.ads.n;
import com.google.ads.o;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import java.util.HashMap;
import java.util.Map;

public class i extends WebViewClient {
    private static final a c = a.a.b();
    protected d a;
    protected boolean b = false;
    private final Map<String, o> d;
    private final boolean e;
    private boolean f;
    private boolean g;
    private boolean h = false;
    private boolean i = false;

    public i(d dVar, Map<String, o> map, boolean z, boolean z2) {
        this.a = dVar;
        this.d = map;
        this.e = z;
        this.g = z2;
    }

    public static i a(d dVar, Map<String, o> map, boolean z, boolean z2) {
        if (AdUtil.a >= 11) {
            return new g.b(dVar, map, z, z2);
        }
        return new i(dVar, map, z, z2);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Uri uri;
        try {
            b.a("shouldOverrideUrlLoading(\"" + url + "\")");
            Uri parse = Uri.parse(url);
            if (c.a(parse)) {
                c.a(this.a, this.d, parse, webView);
                return true;
            } else if (this.g) {
                if (AdUtil.a(parse)) {
                    return super.shouldOverrideUrlLoading(webView, url);
                }
                HashMap hashMap = new HashMap();
                hashMap.put(AdActivity.URL_PARAM, url);
                AdActivity.launchAdActivity(this.a, new e("intent", hashMap));
                return true;
            } else if (this.e) {
                n i2 = this.a.i();
                Context a2 = i2.f.a();
                al a3 = i2.s.a();
                if (a3 != null && a3.a(parse)) {
                    uri = a3.a(parse, a2);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(AdActivity.URL_PARAM, uri.toString());
                    AdActivity.launchAdActivity(this.a, new e("intent", hashMap2));
                    return true;
                }
                uri = parse;
                HashMap hashMap22 = new HashMap();
                hashMap22.put(AdActivity.URL_PARAM, uri.toString());
                AdActivity.launchAdActivity(this.a, new e("intent", hashMap22));
                return true;
            } else {
                b.e("URL is not a GMSG and can't handle URL: " + url);
                return true;
            }
        } catch (am e2) {
            b.e("Unable to append parameter to URL: " + url);
        } catch (Throwable th) {
            b.d("An unknown error occurred in shouldOverrideUrlLoading.", th);
        }
    }

    public void onPageStarted(WebView webView, String url, Bitmap favicon) {
        this.f = true;
    }

    public void onPageFinished(WebView view, String url) {
        this.f = false;
        if (this.h) {
            c k = this.a.k();
            if (k != null) {
                k.c();
            } else {
                b.a("adLoader was null while trying to setFinishedLoadingHtml().");
            }
            this.h = false;
        }
        if (this.i) {
            c.a(view);
            this.i = false;
        }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        this.f = false;
        c k = this.a.k();
        if (k != null) {
            k.a(AdRequest.ErrorCode.NETWORK_ERROR);
        }
    }

    public void a(boolean z) {
        this.b = z;
    }

    public void b(boolean z) {
        this.g = z;
    }

    public void c(boolean z) {
        this.h = z;
    }

    public void d(boolean z) {
        this.i = z;
    }

    public boolean a() {
        return this.f;
    }
}
