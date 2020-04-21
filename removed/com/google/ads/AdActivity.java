package com.google.ads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.google.ads.internal.AdVideoView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.a;
import com.google.ads.internal.d;
import com.google.ads.internal.e;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.google.ads.util.b;
import com.google.ads.util.g;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdActivity extends Activity implements View.OnClickListener {
    public static final String BASE_URL_PARAM = "baseurl";
    public static final String COMPONENT_NAME_PARAM = "c";
    public static final String CUSTOM_CLOSE_PARAM = "custom_close";
    public static final String HTML_PARAM = "html";
    public static final String INTENT_ACTION_PARAM = "i";
    public static final String INTENT_EXTRAS_PARAM = "e";
    public static final String INTENT_FLAGS_PARAM = "f";
    public static final String ORIENTATION_PARAM = "o";
    public static final String PACKAGE_NAME_PARAM = "p";
    public static final String TYPE_PARAM = "m";
    public static final String URL_PARAM = "u";
    private static final a a = a.a.b();
    /* access modifiers changed from: private */
    public static final Object b = new Object();
    /* access modifiers changed from: private */
    public static AdActivity c = null;
    /* access modifiers changed from: private */
    public static d d = null;
    /* access modifiers changed from: private */
    public static AdActivity e = null;
    private static AdActivity f = null;
    private static final StaticMethodWrapper g = new StaticMethodWrapper();
    private AdWebView h;
    private FrameLayout i;
    private int j;
    private ViewGroup k = null;
    private boolean l;
    private long m;
    private RelativeLayout n;
    private AdActivity o = null;
    private boolean p;
    private boolean q;
    private boolean r;
    private boolean s;
    private AdVideoView t;

    public static class StaticMethodWrapper {
        public boolean isShowing() {
            boolean z;
            synchronized (AdActivity.b) {
                z = AdActivity.e != null;
            }
            return z;
        }

        public boolean leftApplication() {
            boolean z;
            synchronized (AdActivity.b) {
                z = AdActivity.c != null;
            }
            return z;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
            r1 = new android.content.Intent(r0.getApplicationContext(), com.google.ads.AdActivity.class);
            r1.putExtra("com.google.ads.AdOpener", r6.a());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            com.google.ads.util.b.a("Launching AdActivity.");
            r0.startActivity(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0050, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0051, code lost:
            com.google.ads.util.b.b("Activity not found.", r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x000f, code lost:
            r0 = r5.i().c.a();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
            if (r0 != null) goto L_0x0033;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
            com.google.ads.util.b.e("activity was null while launching an AdActivity.");
         */
        public void launchAdActivity(d adManager, e adOpener) {
            synchronized (AdActivity.b) {
                if (AdActivity.d == null) {
                    d unused = AdActivity.d = adManager;
                } else if (AdActivity.d != adManager) {
                    b.b("Tried to launch a new AdActivity with a different AdManager.");
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public View a(int i2, boolean z) {
        this.j = (int) TypedValue.applyDimension(1, (float) i2, getResources().getDisplayMetrics());
        this.i = new FrameLayout(getApplicationContext());
        this.i.setMinimumWidth(this.j);
        this.i.setMinimumHeight(this.j);
        this.i.setOnClickListener(this);
        setCustomClose(z);
        return this.i;
    }

    private void a(String str) {
        b.b(str);
        finish();
    }

    private void a(String str, Throwable th) {
        b.b(str, th);
        finish();
    }

    public AdVideoView getAdVideoView() {
        return this.t;
    }

    public AdWebView getOpeningAdWebView() {
        if (this.o != null) {
            return this.o.h;
        }
        synchronized (b) {
            if (d == null) {
                b.e("currentAdManager was null while trying to get the opening AdWebView.");
                return null;
            }
            AdWebView l2 = d.l();
            if (l2 != this.h) {
                return l2;
            }
            return null;
        }
    }

    public static boolean isShowing() {
        return g.isShowing();
    }

    public static boolean leftApplication() {
        return g.leftApplication();
    }

    public static void launchAdActivity(d adManager, e adOpener) {
        g.launchAdActivity(adManager, adOpener);
    }

    /* access modifiers changed from: protected */
    public void a(HashMap<String, String> hashMap, d dVar) {
        boolean z;
        int i2;
        if (hashMap == null) {
            a("Could not get the paramMap in launchIntent()");
            return;
        }
        Intent intent = new Intent();
        String str = hashMap.get(URL_PARAM);
        String str2 = hashMap.get(TYPE_PARAM);
        String str3 = hashMap.get(INTENT_ACTION_PARAM);
        String str4 = hashMap.get(PACKAGE_NAME_PARAM);
        String str5 = hashMap.get(COMPONENT_NAME_PARAM);
        String str6 = hashMap.get(INTENT_FLAGS_PARAM);
        String str7 = hashMap.get(INTENT_EXTRAS_PARAM);
        boolean z2 = !TextUtils.isEmpty(str);
        if (!TextUtils.isEmpty(str2)) {
            z = true;
        } else {
            z = false;
        }
        if (z2 && z) {
            intent.setDataAndType(Uri.parse(str), str2);
        } else if (z2) {
            intent.setData(Uri.parse(str));
        } else if (z) {
            intent.setType(str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            intent.setAction(str3);
        } else if (z2) {
            intent.setAction("android.intent.action.VIEW");
        }
        if (!TextUtils.isEmpty(str4) && AdUtil.a >= 4) {
            com.google.ads.util.e.a(intent, str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            String[] split = str5.split("/");
            if (split.length < 2) {
                b.e("Warning: Could not parse component name from open GMSG: " + str5);
            }
            intent.setClassName(split[0], split[1]);
        }
        if (!TextUtils.isEmpty(str6)) {
            try {
                i2 = Integer.parseInt(str6);
            } catch (NumberFormatException e2) {
                b.e("Warning: Could not parse flags from open GMSG: " + str6);
                i2 = 0;
            }
            intent.addFlags(i2);
        }
        if (!TextUtils.isEmpty(str7)) {
            try {
                JSONObject jSONObject = new JSONObject(str7);
                JSONArray names = jSONObject.names();
                for (int i3 = 0; i3 < names.length(); i3++) {
                    String string = names.getString(i3);
                    JSONObject jSONObject2 = jSONObject.getJSONObject(string);
                    int i4 = jSONObject2.getInt("t");
                    switch (i4) {
                        case 1:
                            intent.putExtra(string, jSONObject2.getBoolean("v"));
                            break;
                        case 2:
                            intent.putExtra(string, jSONObject2.getDouble("v"));
                            break;
                        case 3:
                            intent.putExtra(string, jSONObject2.getInt("v"));
                            break;
                        case 4:
                            intent.putExtra(string, jSONObject2.getLong("v"));
                            break;
                        case 5:
                            intent.putExtra(string, jSONObject2.getString("v"));
                            break;
                        default:
                            b.e("Warning: Unknown type in extras from open GMSG: " + string + " (type: " + i4 + ")");
                            break;
                    }
                }
            } catch (JSONException e3) {
                b.e("Warning: Could not parse extras from open GMSG: " + str7);
            }
        }
        if (intent.filterEquals(new Intent())) {
            a("Tried to launch empty intent.");
            return;
        }
        try {
            b.a("Launching an intent from AdActivity: " + intent);
            startActivity(intent);
            a(dVar);
        } catch (ActivityNotFoundException e4) {
            a(e4.getMessage(), (Throwable) e4);
        }
    }

    /* access modifiers changed from: protected */
    public void a(d dVar) {
        this.h = null;
        this.m = SystemClock.elapsedRealtime();
        this.p = true;
        synchronized (b) {
            if (c == null) {
                c = this;
                dVar.w();
            }
        }
    }

    /* access modifiers changed from: protected */
    public AdVideoView a(Activity activity) {
        return new AdVideoView(activity, this.h);
    }

    public void moveAdVideoView(int x, int y, int width, int height) {
        if (this.t != null) {
            this.t.setLayoutParams(a(x, y, width, height));
            this.t.requestLayout();
        }
    }

    public void newAdVideoView(int x, int y, int width, int height) {
        if (this.t == null) {
            this.t = a((Activity) this);
            this.n.addView(this.t, 0, a(x, y, width, height));
            synchronized (b) {
                if (d == null) {
                    b.e("currentAdManager was null while trying to get the opening AdWebView.");
                } else {
                    d.m().b(false);
                }
            }
        }
    }

    private RelativeLayout.LayoutParams a(int i2, int i3, int i4, int i5) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i4, i5);
        layoutParams.setMargins(i2, i3, 0, 0);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        return layoutParams;
    }

    public void onClick(View v) {
        finish();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0088, code lost:
        r11.n = null;
        r11.p = false;
        r11.q = true;
        r11.t = null;
        r0 = getIntent().getBundleExtra("com.google.ads.AdOpener");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x009a, code lost:
        if (r0 != null) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x009c, code lost:
        a("Could not get the Bundle used to create AdActivity.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b0, code lost:
        r1 = new com.google.ads.internal.e(r0);
        r0 = r1.b();
        r10 = r1.c();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c3, code lost:
        if (r0.equals("intent") == false) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00c5, code lost:
        a(r10, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c9, code lost:
        r11.n = new android.widget.RelativeLayout(getApplicationContext());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00da, code lost:
        if (r0.equals("webapp") == false) goto L_0x017a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00dc, code lost:
        r11.h = new com.google.ads.internal.AdWebView(r8.i(), (com.google.ads.AdSize) null);
        r1 = com.google.ads.internal.a.d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00e9, code lost:
        if (r9 != false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00eb, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ec, code lost:
        r0 = com.google.ads.internal.i.a(r8, r1, true, r0);
        r0.d(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00f3, code lost:
        if (r9 == false) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f5, code lost:
        r0.a(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f8, code lost:
        r11.h.setWebViewClient(r0);
        r0 = r10.get(URL_PARAM);
        r1 = r10.get(BASE_URL_PARAM);
        r2 = r10.get(HTML_PARAM);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0115, code lost:
        if (r0 == null) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0117, code lost:
        r11.h.loadUrl(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x011c, code lost:
        r0 = r10.get(ORIENTATION_PARAM);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x012a, code lost:
        if (PACKAGE_NAME_PARAM.equals(r0) == false) goto L_0x0160;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x012c, code lost:
        r3 = com.google.ads.util.AdUtil.b();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0130, code lost:
        r1 = r11.h;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0132, code lost:
        if (r10 == null) goto L_0x0178;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0140, code lost:
        if (jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs.KANOJO_NAME.equals(r10.get(CUSTOM_CLOSE_PARAM)) == false) goto L_0x0178;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0142, code lost:
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0143, code lost:
        a(r1, false, r3, r9, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x014b, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x014d, code lost:
        if (r2 == null) goto L_0x0159;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x014f, code lost:
        r11.h.loadDataWithBaseURL(r1, r2, "text/html", "utf-8", (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0159, code lost:
        a("Could not get the URL or HTML parameter to show a web app.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0166, code lost:
        if ("l".equals(r0) == false) goto L_0x016d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0168, code lost:
        r3 = com.google.ads.util.AdUtil.a();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x016f, code lost:
        if (r11 != e) goto L_0x0176;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0171, code lost:
        r3 = r8.o();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0176, code lost:
        r3 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0178, code lost:
        r5 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0180, code lost:
        if (r0.equals("interstitial") != false) goto L_0x018a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0188, code lost:
        if (r0.equals("expand") == false) goto L_0x01d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x018a, code lost:
        r11.h = r8.l();
        r3 = r8.o();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x019a, code lost:
        if (r0.equals("expand") == false) goto L_0x01d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x019c, code lost:
        r11.h.setIsExpandedMraid(true);
        r11.q = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01a3, code lost:
        if (r10 == null) goto L_0x01b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x01b1, code lost:
        if (jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs.KANOJO_NAME.equals(r10.get(CUSTOM_CLOSE_PARAM)) == false) goto L_0x01b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01b3, code lost:
        r7 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01b6, code lost:
        if (r11.r == false) goto L_0x01f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01ba, code lost:
        if (r11.s != false) goto L_0x01f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01bc, code lost:
        com.google.ads.util.b.a("Re-enabling hardware acceleration on expanding MRAID WebView.");
        r11.h.h();
        r5 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01c7, code lost:
        a(r11.h, true, r3, r9, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x01d1, code lost:
        r5 = r11.h.j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01d9, code lost:
        a("Unknown AdOpener, <action: " + r0 + ">");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01f7, code lost:
        r5 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:?, code lost:
        return;
     */
    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        super.onCreate(savedInstanceState);
        this.l = false;
        synchronized (b) {
            if (d != null) {
                d dVar = d;
                if (e == null) {
                    e = this;
                    dVar.v();
                }
                if (this.o == null && f != null) {
                    this.o = f;
                }
                f = this;
                if ((dVar.i().a() && e == this) || (dVar.i().b() && this.o == e)) {
                    dVar.x();
                }
                boolean r2 = dVar.r();
                m.a a2 = dVar.i().d.a().b.a();
                if (AdUtil.a >= a2.b.a().intValue()) {
                    z = true;
                } else {
                    z = false;
                }
                this.s = z;
                if (AdUtil.a >= a2.d.a().intValue()) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                this.r = z2;
            } else {
                a("Could not get currentAdManager.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void a(AdWebView adWebView, boolean z, int i2, boolean z2, boolean z3) {
        requestWindowFeature(1);
        Window window = getWindow();
        window.setFlags(1024, 1024);
        if (AdUtil.a >= 11) {
            if (this.r) {
                b.a("Enabling hardware acceleration on the AdActivity window.");
                g.a(window);
            } else {
                b.a("Disabling hardware acceleration on the AdActivity WebView.");
                adWebView.g();
            }
        }
        ViewParent parent = adWebView.getParent();
        if (parent != null) {
            if (!z2) {
                a("Interstitial created with an AdWebView that has a parent.");
                return;
            } else if (parent instanceof ViewGroup) {
                this.k = (ViewGroup) parent;
                this.k.removeView(adWebView);
            } else {
                a("MRAID banner was not a child of a ViewGroup.");
                return;
            }
        }
        if (adWebView.i() != null) {
            a("Interstitial created with an AdWebView that is already in use by another AdActivity.");
            return;
        }
        setRequestedOrientation(i2);
        adWebView.setAdActivity(this);
        View a2 = a(z2 ? 50 : 32, z3);
        this.n.addView(adWebView, -1, -1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        if (z2) {
            layoutParams.addRule(10);
            layoutParams.addRule(11);
        } else {
            layoutParams.addRule(10);
            layoutParams.addRule(9);
        }
        this.n.addView(a2, layoutParams);
        this.n.setKeepScreenOn(true);
        setContentView(this.n);
        this.n.getRootView().setBackgroundColor(-16777216);
        if (z) {
            a.a((WebView) adWebView);
        }
    }

    public void onDestroy() {
        if (this.n != null) {
            this.n.removeAllViews();
        }
        if (isFinishing()) {
            e();
            if (this.q && this.h != null) {
                this.h.stopLoading();
                this.h.destroy();
                this.h = null;
            }
        }
        super.onDestroy();
    }

    public void onPause() {
        if (isFinishing()) {
            e();
        }
        super.onPause();
    }

    private void e() {
        if (!this.l) {
            if (this.h != null) {
                a.b((WebView) this.h);
                this.h.setAdActivity((AdActivity) null);
                this.h.setIsExpandedMraid(false);
                if (!(this.q || this.n == null || this.k == null)) {
                    if (this.r && !this.s) {
                        b.a("Disabling hardware acceleration on collapsing MRAID WebView.");
                        this.h.g();
                    } else if (!this.r && this.s) {
                        b.a("Re-enabling hardware acceleration on collapsing MRAID WebView.");
                        this.h.h();
                    }
                    this.n.removeView(this.h);
                    this.k.addView(this.h);
                }
            }
            if (this.t != null) {
                this.t.e();
                this.t = null;
            }
            if (this == c) {
                c = null;
            }
            f = this.o;
            synchronized (b) {
                if (!(d == null || !this.q || this.h == null)) {
                    if (this.h == d.l()) {
                        d.a();
                    }
                    this.h.stopLoading();
                }
                if (this == e) {
                    e = null;
                    if (d != null) {
                        d.u();
                        d = null;
                    } else {
                        b.e("currentAdManager is null while trying to destroy AdActivity.");
                    }
                }
            }
            this.l = true;
            b.a("AdActivity is closing.");
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.p && hasFocus && SystemClock.elapsedRealtime() - this.m > 250) {
            b.d("Launcher AdActivity got focus and is closing.");
            finish();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void setCustomClose(boolean useCustomClose) {
        if (this.i != null) {
            this.i.removeAllViews();
            if (!useCustomClose) {
                ImageButton imageButton = new ImageButton(this);
                imageButton.setImageResource(17301527);
                imageButton.setBackgroundColor(0);
                imageButton.setOnClickListener(this);
                imageButton.setPadding(0, 0, 0, 0);
                this.i.addView(imageButton, new FrameLayout.LayoutParams(this.j, this.j, 17));
            }
        }
    }
}
