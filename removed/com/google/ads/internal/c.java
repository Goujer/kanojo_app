package com.google.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import com.google.ads.AdActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.ak;
import com.google.ads.al;
import com.google.ads.l;
import com.google.ads.m;
import com.google.ads.n;
import com.google.ads.searchads.SearchAdRequest;
import com.google.ads.util.AdUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;

public class c implements Runnable {
    boolean a;
    private String b;
    private String c;
    private String d;
    private String e;
    private boolean f;
    private f g;
    private AdRequest h;
    /* access modifiers changed from: private */
    public WebView i;
    /* access modifiers changed from: private */
    public l j;
    private String k;
    /* access modifiers changed from: private */
    public String l;
    private LinkedList<String> m;
    /* access modifiers changed from: private */
    public String n;
    /* access modifiers changed from: private */
    public AdSize o;
    /* access modifiers changed from: private */
    public boolean p = false;
    private volatile boolean q;
    private boolean r;
    private AdRequest.ErrorCode s;
    private boolean t;
    private int u;
    private Thread v;
    private boolean w;
    private d x = d.ONLINE_SERVER_REQUEST;

    private class b extends Exception {
        public b(String str) {
            super(str);
        }
    }

    private static class a implements Runnable {
        private final d a;
        private final WebView b;
        private final f c;
        private final AdRequest.ErrorCode d;
        private final boolean e;

        public a(d dVar, WebView webView, f fVar, AdRequest.ErrorCode errorCode, boolean z) {
            this.a = dVar;
            this.b = webView;
            this.c = fVar;
            this.d = errorCode;
            this.e = z;
        }

        public void run() {
            if (this.b != null) {
                this.b.stopLoading();
                this.b.destroy();
            }
            if (this.c != null) {
                this.c.a();
            }
            if (this.e) {
                this.a.l().stopLoading();
                if (this.a.i().i.a() != null) {
                    this.a.i().i.a().setVisibility(8);
                }
            }
            this.a.a(this.d);
        }
    }

    /* renamed from: com.google.ads.internal.c$c  reason: collision with other inner class name */
    private class C0000c implements Runnable {
        private final String b;
        private final String c;
        private final WebView d;

        public C0000c(WebView webView, String str, String str2) {
            this.d = webView;
            this.b = str;
            this.c = str2;
        }

        public void run() {
            c.this.j.c.a(Boolean.valueOf(c.this.p));
            c.this.j.a.a().b.a().l().a(c.this.p);
            if (c.this.j.a.a().e.a() != null) {
                c.this.j.a.a().e.a().setOverlayEnabled(!c.this.p);
            }
            if (this.c != null) {
                this.d.loadDataWithBaseURL(this.b, this.c, "text/html", "utf-8", (String) null);
            } else {
                this.d.loadUrl(this.b);
            }
        }
    }

    private class e implements Runnable {
        private final d b;
        private final WebView c;
        private final LinkedList<String> d;
        private final int e;
        private final boolean f;
        private final String g;
        private final AdSize h;

        public e(d dVar, WebView webView, LinkedList<String> linkedList, int i, boolean z, String str, AdSize adSize) {
            this.b = dVar;
            this.c = webView;
            this.d = linkedList;
            this.e = i;
            this.f = z;
            this.g = str;
            this.h = adSize;
        }

        public void run() {
            if (this.c != null) {
                this.c.stopLoading();
                this.c.destroy();
            }
            this.b.a(this.d);
            this.b.a(this.e);
            this.b.a(this.f);
            this.b.a(this.g);
            if (this.h != null) {
                c.this.j.a.a().g.a().b(this.h);
                this.b.l().setAdSize(this.h);
            }
            this.b.E();
        }
    }

    public enum d {
        ONLINE_USING_BUFFERED_ADS("online_buffered"),
        ONLINE_SERVER_REQUEST("online_request"),
        OFFLINE_USING_BUFFERED_ADS("offline_buffered"),
        OFFLINE_EMPTY("offline_empty");
        
        public String e;

        private d(String str) {
            this.e = str;
        }
    }

    public synchronized void a(boolean z) {
        this.p = z;
    }

    protected c() {
    }

    public c(l lVar) {
        this.j = lVar;
        this.k = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.m = new LinkedList<>();
        this.s = null;
        this.t = false;
        this.u = -1;
        this.f = false;
        this.r = false;
        this.n = null;
        this.o = null;
        if (lVar.a.a().c.a() != null) {
            this.i = new AdWebView(lVar.a.a(), (AdSize) null);
            this.i.setWebViewClient(i.a(lVar.a.a().b.a(), a.b, false, false));
            this.i.setVisibility(8);
            this.i.setWillNotDraw(true);
            this.g = new f(lVar);
            return;
        }
        this.i = null;
        this.g = null;
        com.google.ads.util.b.e("activity was null while trying to create an AdLoader.");
    }

    /* access modifiers changed from: protected */
    public synchronized void a(String str) {
        this.m.add(str);
    }

    /* access modifiers changed from: protected */
    public void a() {
        com.google.ads.util.b.a("AdLoader cancelled.");
        if (this.i != null) {
            this.i.stopLoading();
            this.i.destroy();
        }
        if (this.v != null) {
            this.v.interrupt();
            this.v = null;
        }
        if (this.g != null) {
            this.g.a();
        }
        this.q = true;
    }

    /* access modifiers changed from: protected */
    public void a(AdRequest adRequest) {
        this.h = adRequest;
        this.q = false;
        this.v = new Thread(this);
        this.v.start();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x01e9 A[Catch:{ Throwable -> 0x0115 }] */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x02f2 A[SYNTHETIC, Splitter:B:147:0x02f2] */
    public void run() {
        i iVar;
        i iVar2;
        synchronized (this) {
            if (this.i == null || this.g == null) {
                com.google.ads.util.b.e("adRequestWebView was null while trying to load an ad.");
                a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                return;
            }
            Activity a2 = this.j.a.a().c.a();
            if (a2 == null) {
                com.google.ads.util.b.e("activity was null while forming an ad request.");
                a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                return;
            }
            long p2 = this.j.a.a().b.a().p();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Map<String, Object> requestMap = this.h.getRequestMap(this.j.a.a().f.a());
            Object obj = requestMap.get("extras");
            if (obj instanceof Map) {
                Map map = (Map) obj;
                Object obj2 = map.get("_adUrl");
                if (obj2 instanceof String) {
                    this.b = (String) obj2;
                }
                Object obj3 = map.get("_requestUrl");
                if (obj3 instanceof String) {
                    this.k = (String) obj3;
                }
                Object obj4 = map.get("_activationOverlayUrl");
                if (obj4 instanceof String) {
                    this.l = (String) obj4;
                }
                Object obj5 = map.get("_orientation");
                if (obj5 instanceof String) {
                    if (obj5.equals(AdActivity.PACKAGE_NAME_PARAM)) {
                        this.u = 1;
                    } else if (obj5.equals("l")) {
                        this.u = 0;
                    }
                }
                Object obj6 = map.get("_norefresh");
                if ((obj6 instanceof String) && obj6.equals("t")) {
                    this.j.a.a().b.a().e();
                }
            }
            if (this.b == null) {
                if (this.k == null) {
                    try {
                        b(a(requestMap, a2), f());
                        long elapsedRealtime2 = p2 - (SystemClock.elapsedRealtime() - elapsedRealtime);
                        if (elapsedRealtime2 > 0) {
                            try {
                                wait(elapsedRealtime2);
                            } catch (InterruptedException e2) {
                                com.google.ads.util.b.a("AdLoader InterruptedException while getting the URL: " + e2);
                                return;
                            }
                        }
                        try {
                            if (!this.q) {
                                if (this.s != null) {
                                    a(this.s, false);
                                    return;
                                } else if (this.k == null) {
                                    com.google.ads.util.b.c("AdLoader timed out after " + p2 + "ms while getting the URL.");
                                    a(AdRequest.ErrorCode.NETWORK_ERROR, false);
                                    return;
                                } else if (this.j.a.a().g.a().b() && TextUtils.isEmpty(this.l)) {
                                    com.google.ads.util.b.c("AdLoader doesn't have a URL for the activation overlay");
                                    a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable th) {
                            com.google.ads.util.b.b("An unknown error occurred in AdLoader.", th);
                            a(AdRequest.ErrorCode.INTERNAL_ERROR, true);
                        }
                    } catch (b e3) {
                        com.google.ads.util.b.c("Caught internal exception: " + e3);
                        a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                        return;
                    }
                }
                g n2 = this.j.a.a().b.a().n();
                switch (this.x) {
                    case ONLINE_SERVER_REQUEST:
                        n2.r();
                        n2.u();
                        n2.x();
                        com.google.ads.util.b.c("Request scenario: Online server request.");
                    case ONLINE_USING_BUFFERED_ADS:
                        n2.t();
                        com.google.ads.util.b.c("Request scenario: Online using buffered ads.");
                        if (this.a) {
                            this.b = this.k;
                            com.google.ads.util.b.a("Using loadUrl.  adBaseUrl: " + this.b);
                            break;
                        } else {
                            com.google.ads.util.b.a("Not using loadUrl().");
                            this.g.a(this.w);
                            if (this.j.a.a().g.a().b()) {
                                i e4 = this.j.a.a().e.a().e();
                                e4.c(true);
                                m.a().c.a().post(new Runnable() {
                                    public void run() {
                                        c.this.j.a.a().e.a().loadUrl(c.this.l);
                                    }
                                });
                                iVar2 = e4;
                            } else {
                                iVar2 = null;
                            }
                            this.g.a(this.k);
                            while (!this.q && this.s == null && this.c == null) {
                                try {
                                    long elapsedRealtime3 = p2 - (SystemClock.elapsedRealtime() - elapsedRealtime);
                                    if (elapsedRealtime3 > 0) {
                                        wait(elapsedRealtime3);
                                    }
                                } catch (InterruptedException e5) {
                                    com.google.ads.util.b.a("AdLoader InterruptedException while getting the ad server's response: " + e5);
                                    return;
                                }
                            }
                            if (!this.q) {
                                if (this.s == null) {
                                    if (this.c != null) {
                                        iVar = iVar2;
                                        break;
                                    } else {
                                        com.google.ads.util.b.c("AdLoader timed out after " + p2 + "ms while waiting for the ad server's response.");
                                        a(AdRequest.ErrorCode.NETWORK_ERROR, false);
                                        return;
                                    }
                                } else {
                                    a(this.s, false);
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        break;
                    case OFFLINE_USING_BUFFERED_ADS:
                        n2.w();
                        n2.q();
                        com.google.ads.util.b.c("Request scenario: Offline using buffered ads.");
                        if (this.a) {
                        }
                        break;
                    case OFFLINE_EMPTY:
                        n2.q();
                        com.google.ads.util.b.c("Request scenario: Offline with no buffered ads.");
                        com.google.ads.util.b.c("Network is unavailable.  Aborting ad request.");
                        a(AdRequest.ErrorCode.NETWORK_ERROR, false);
                        return;
                }
                if (this.a) {
                }
            }
            iVar = null;
            if (!this.a) {
                if (this.f) {
                    this.j.a.a().b.a().b(true);
                    b();
                    return;
                } else if (this.e != null && (this.e.startsWith("application/json") || this.e.startsWith("text/javascript"))) {
                    com.google.ads.util.b.b("Expected HTML but received " + this.e + ".");
                    a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                    return;
                } else if (this.j.a.a().n.a() != null) {
                    if (this.o == null) {
                        com.google.ads.util.b.b("Multiple supported ad sizes were specified, but the server did not respond with a size.");
                        a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                        return;
                    } else if (!Arrays.asList((Object[]) this.j.a.a().n.a()).contains(this.o)) {
                        com.google.ads.util.b.b("The ad server did not respond with a supported AdSize: " + this.o);
                        a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
                        return;
                    }
                } else if (this.o != null) {
                    com.google.ads.util.b.e("adSize was expected to be null in AdLoader.");
                    this.o = null;
                }
            }
            this.j.a.a().b.a().b(false);
            i();
            while (!this.q && (!this.t || (this.j.a.a().g.a().b() && iVar.a()))) {
                try {
                    long elapsedRealtime4 = p2 - (SystemClock.elapsedRealtime() - elapsedRealtime);
                    if (elapsedRealtime4 > 0) {
                        wait(elapsedRealtime4);
                    }
                } catch (InterruptedException e6) {
                    com.google.ads.util.b.a("AdLoader InterruptedException while loading the HTML: " + e6);
                    return;
                }
            }
            if (this.t) {
                j();
            } else {
                com.google.ads.util.b.c("AdLoader timed out after " + p2 + "ms while loading the HTML.");
                a(AdRequest.ErrorCode.NETWORK_ERROR, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void b() {
        try {
            if (TextUtils.isEmpty(this.e)) {
                com.google.ads.util.b.b("Got a mediation response with no content type. Aborting mediation.");
                a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
            } else if (!this.e.startsWith("application/json")) {
                com.google.ads.util.b.b("Got a mediation response with a content type: '" + this.e + "'. Expected something starting with 'application/json'. Aborting mediation.");
                a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
            } else {
                final com.google.ads.c a2 = com.google.ads.c.a(this.c);
                a(this.d, a2, this.j.a.a().b.a().j());
                m.a().c.a().post(new Runnable() {
                    public void run() {
                        if (c.this.i != null) {
                            c.this.i.stopLoading();
                            c.this.i.destroy();
                        }
                        c.this.j.a.a().b.a().a(c.this.n);
                        if (c.this.o != null) {
                            c.this.j.a.a().g.a().b(c.this.o);
                        }
                        c.this.j.a.a().b.a().a(a2);
                    }
                });
            }
        } catch (JSONException e2) {
            com.google.ads.util.b.b("AdLoader can't parse gWhirl server configuration.", e2);
            a(AdRequest.ErrorCode.INTERNAL_ERROR, false);
        }
    }

    static void a(String str, com.google.ads.c cVar, com.google.ads.d dVar) {
        if (str != null && !str.contains("no-store") && !str.contains("no-cache")) {
            Matcher matcher = Pattern.compile("max-age\\s*=\\s*(\\d+)").matcher(str);
            if (matcher.find()) {
                try {
                    int parseInt = Integer.parseInt(matcher.group(1));
                    dVar.a(cVar, parseInt);
                    com.google.ads.util.b.c(String.format(Locale.US, "Caching gWhirl configuration for: %d seconds", new Object[]{Integer.valueOf(parseInt)}));
                } catch (NumberFormatException e2) {
                    com.google.ads.util.b.b("Caught exception trying to parse cache control directive. Overflow?", e2);
                }
            } else {
                com.google.ads.util.b.c("Unrecognized cacheControlDirective: '" + str + "'. Not caching configuration.");
            }
        }
    }

    public String a(Map<String, Object> map, Activity activity) throws b {
        int i2;
        Context applicationContext = activity.getApplicationContext();
        g n2 = this.j.a.a().b.a().n();
        long m2 = n2.m();
        if (m2 > 0) {
            map.put("prl", Long.valueOf(m2));
        }
        long n3 = n2.n();
        if (n3 > 0) {
            map.put("prnl", Long.valueOf(n3));
        }
        String l2 = n2.l();
        if (l2 != null) {
            map.put("ppcl", l2);
        }
        String k2 = n2.k();
        if (k2 != null) {
            map.put("pcl", k2);
        }
        long j2 = n2.j();
        if (j2 > 0) {
            map.put("pcc", Long.valueOf(j2));
        }
        map.put("preqs", Long.valueOf(n2.o()));
        map.put("oar", Long.valueOf(n2.p()));
        map.put("bas_on", Long.valueOf(n2.s()));
        map.put("bas_off", Long.valueOf(n2.v()));
        if (n2.y()) {
            map.put("aoi_timeout", "true");
        }
        if (n2.A()) {
            map.put("aoi_nofill", "true");
        }
        String D = n2.D();
        if (D != null) {
            map.put("pit", D);
        }
        map.put("ptime", Long.valueOf(g.E()));
        n2.a();
        n2.i();
        if (this.j.a.a().b()) {
            map.put("format", "interstitial_mb");
        } else {
            AdSize c2 = this.j.a.a().g.a().c();
            if (c2.isFullWidth()) {
                map.put("smart_w", "full");
            }
            if (c2.isAutoHeight()) {
                map.put("smart_h", "auto");
            }
            if (!c2.isCustomAdSize()) {
                map.put("format", c2.toString());
            } else {
                HashMap hashMap = new HashMap();
                hashMap.put("w", Integer.valueOf(c2.getWidth()));
                hashMap.put("h", Integer.valueOf(c2.getHeight()));
                map.put("ad_frame", hashMap);
            }
        }
        map.put("slotname", this.j.a.a().h.a());
        map.put("js", "afma-sdk-a-v6.4.1");
        try {
            int i3 = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionCode;
            String f2 = AdUtil.f(applicationContext);
            if (!TextUtils.isEmpty(f2)) {
                map.put("mv", f2);
            }
            String a2 = m.a().a.a();
            if (!TextUtils.isEmpty(a2)) {
                map.put("imbf", a2);
            }
            map.put("msid", applicationContext.getPackageName());
            map.put("app_name", i3 + ".android." + applicationContext.getPackageName());
            map.put("isu", AdUtil.a(applicationContext));
            String d2 = AdUtil.d(applicationContext);
            if (d2 == null) {
                d2 = "null";
            }
            map.put("net", d2);
            String e2 = AdUtil.e(applicationContext);
            if (!(e2 == null || e2.length() == 0)) {
                map.put("cap", e2);
            }
            map.put("u_audio", Integer.valueOf(AdUtil.g(applicationContext).ordinal()));
            DisplayMetrics a3 = AdUtil.a(activity);
            map.put("u_sd", Float.valueOf(a3.density));
            map.put("u_h", Integer.valueOf(AdUtil.a(applicationContext, a3)));
            map.put("u_w", Integer.valueOf(AdUtil.b(applicationContext, a3)));
            map.put("hl", Locale.getDefault().getLanguage());
            n a4 = this.j.a.a();
            ak a5 = a4.r.a();
            if (a5 == null) {
                a5 = ak.a("afma-sdk-a-v6.4.1", (Context) activity);
                a4.r.a(a5);
                a4.s.a(new al(a5));
            }
            map.put("ms", a5.a(applicationContext));
            if (!(this.j.a.a().j == null || this.j.a.a().j.a() == null)) {
                AdView a6 = this.j.a.a().j.a();
                if (a6.getParent() != null) {
                    int[] iArr = new int[2];
                    a6.getLocationOnScreen(iArr);
                    int i4 = iArr[0];
                    int i5 = iArr[1];
                    DisplayMetrics displayMetrics = this.j.a.a().f.a().getResources().getDisplayMetrics();
                    int i6 = displayMetrics.widthPixels;
                    int i7 = displayMetrics.heightPixels;
                    if (!a6.isShown() || a6.getWidth() + i4 <= 0 || a6.getHeight() + i5 <= 0 || i4 > i6 || i5 > i7) {
                        i2 = 0;
                    } else {
                        i2 = 1;
                    }
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("x", Integer.valueOf(i4));
                    hashMap2.put("y", Integer.valueOf(i5));
                    hashMap2.put("width", Integer.valueOf(a6.getWidth()));
                    hashMap2.put("height", Integer.valueOf(a6.getHeight()));
                    hashMap2.put("visible", Integer.valueOf(i2));
                    map.put("ad_pos", hashMap2);
                }
            }
            StringBuilder sb = new StringBuilder();
            AdSize[] a7 = this.j.a.a().n.a();
            if (a7 != null) {
                for (AdSize adSize : a7) {
                    if (sb.length() != 0) {
                        sb.append("|");
                    }
                    sb.append(adSize.getWidth() + "x" + adSize.getHeight());
                }
                map.put("sz", sb.toString());
            }
            TelephonyManager telephonyManager = (TelephonyManager) applicationContext.getSystemService("phone");
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator)) {
                map.put("carrier", networkOperator);
            }
            map.put("pt", Integer.valueOf(telephonyManager.getPhoneType()));
            map.put("gnt", Integer.valueOf(telephonyManager.getNetworkType()));
            if (AdUtil.c()) {
                map.put("simulator", 1);
            }
            map.put("session_id", com.google.ads.b.a().b().toString());
            map.put("seq_num", com.google.ads.b.a().c().toString());
            if (this.j.a.a().g.a().b()) {
                map.put("swipeable", 1);
            }
            if (this.j.a.a().t.a().booleanValue()) {
                map.put("d_imp_hdr", 1);
            }
            String a8 = AdUtil.a(map);
            String str = this.j.a.a().d.a().b.a().o.a().booleanValue() ? g() + d() + "(" + a8 + ");" + h() : g() + e() + d() + "(" + a8 + ");" + h();
            com.google.ads.util.b.c("adRequestUrlHtml: " + str);
            return str;
        } catch (PackageManager.NameNotFoundException e3) {
            throw new b("NameNotFoundException");
        }
    }

    private String d() {
        if (this.h instanceof SearchAdRequest) {
            return "AFMA_buildAdURL";
        }
        return "AFMA_buildAdURL";
    }

    private String e() {
        if (this.h instanceof SearchAdRequest) {
            return "AFMA_getSdkConstants();";
        }
        return "AFMA_getSdkConstants();";
    }

    private String f() {
        if (this.h instanceof SearchAdRequest) {
            return "http://www.gstatic.com/safa/";
        }
        return "http://media.admob.com/";
    }

    private String g() {
        if (this.h instanceof SearchAdRequest) {
            return "<html><head><script src=\"http://www.gstatic.com/safa/sdk-core-v40.js\"></script><script>";
        }
        return "<html><head><script src=\"http://media.admob.com/sdk-core-v40.js\"></script><script>";
    }

    private String h() {
        if (this.h instanceof SearchAdRequest) {
            return "</script></head><body></body></html>";
        }
        return "</script></head><body></body></html>";
    }

    /* access modifiers changed from: protected */
    public void a(AdRequest.ErrorCode errorCode, boolean z) {
        m.a().c.a().post(new a(this.j.a.a().b.a(), this.i, this.g, errorCode, z));
    }

    private void b(String str, String str2) {
        m.a().c.a().post(new C0000c(this.i, str2, str));
    }

    private void i() {
        AdWebView l2 = this.j.a.a().b.a().l();
        this.j.a.a().b.a().m().c(true);
        this.j.a.a().b.a().n().h();
        m.a().c.a().post(new C0000c(l2, this.b, this.c));
    }

    private void j() {
        m.a().c.a().post(new e(this.j.a.a().b.a(), this.i, this.m, this.u, this.r, this.n, this.o));
    }

    /* access modifiers changed from: protected */
    public synchronized void b(boolean z) {
        this.f = z;
    }

    /* access modifiers changed from: protected */
    public synchronized void b(String str) {
        this.e = str;
    }

    /* access modifiers changed from: protected */
    public synchronized void a(String str, String str2) {
        this.b = str2;
        this.c = str;
        notify();
    }

    /* access modifiers changed from: protected */
    public synchronized void c(String str) {
        this.d = str;
    }

    public synchronized void d(String str) {
        this.k = str;
        notify();
    }

    public synchronized void e(String str) {
        this.l = str;
    }

    public synchronized void f(String str) {
        this.n = str;
    }

    public synchronized void a(AdSize adSize) {
        this.o = adSize;
    }

    public synchronized void a(AdRequest.ErrorCode errorCode) {
        this.s = errorCode;
        notify();
    }

    /* access modifiers changed from: protected */
    public synchronized void c() {
        this.t = true;
        notify();
    }

    public synchronized void c(boolean z) {
        this.r = z;
    }

    public synchronized void a(int i2) {
        this.u = i2;
    }

    public synchronized void d(boolean z) {
        this.w = z;
    }

    public synchronized void a(d dVar) {
        this.x = dVar;
    }

    public synchronized void e(boolean z) {
        this.a = z;
    }
}
