package com.google.ads;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.internal.AdWebView;
import com.google.ads.internal.a;
import com.google.ads.internal.b;
import com.google.ads.internal.d;
import com.google.ads.internal.k;
import com.google.ads.util.AdUtil;
import java.util.HashSet;
import java.util.Set;

public class AdView extends RelativeLayout implements Ad {
    private static final a b = a.a.b();
    protected d a;

    public AdView(Activity activity, AdSize adSize, String adUnitId) {
        super(activity.getApplicationContext());
        try {
            a((Context) activity, adSize, (AttributeSet) null);
            b(activity, adSize, (AttributeSet) null);
            a(activity, adSize, adUnitId);
        } catch (b e) {
            a((Context) activity, e.c("Could not initialize AdView"), adSize, (AttributeSet) null);
            e.a("Could not initialize AdView");
        }
    }

    protected AdView(Activity activity, AdSize[] adSizes, String adUnitId) {
        this(activity, new AdSize(0, 0), adUnitId);
        a(adSizes);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a(context, attrs);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    /* access modifiers changed from: package-private */
    public void a(Context context, String str, int i, AdSize adSize, AttributeSet attributeSet) {
        if (adSize == null) {
            adSize = AdSize.BANNER;
        }
        AdSize createAdSize = AdSize.createAdSize(adSize, context.getApplicationContext());
        if (getChildCount() == 0) {
            TextView textView = attributeSet == null ? new TextView(context) : new TextView(context, attributeSet);
            textView.setGravity(17);
            textView.setText(str);
            textView.setTextColor(i);
            textView.setBackgroundColor(-16777216);
            LinearLayout linearLayout = attributeSet == null ? new LinearLayout(context) : new LinearLayout(context, attributeSet);
            linearLayout.setGravity(17);
            LinearLayout linearLayout2 = attributeSet == null ? new LinearLayout(context) : new LinearLayout(context, attributeSet);
            linearLayout2.setGravity(17);
            linearLayout2.setBackgroundColor(i);
            int a2 = AdUtil.a(context, createAdSize.getWidth());
            int a3 = AdUtil.a(context, createAdSize.getHeight());
            linearLayout.addView(textView, a2 - 2, a3 - 2);
            linearLayout2.addView(linearLayout);
            addView(linearLayout2, a2, a3);
        }
    }

    private boolean a(Context context, AdSize adSize, AttributeSet attributeSet) {
        if (AdUtil.c(context)) {
            return true;
        }
        a(context, "You must have AdActivity declared in AndroidManifest.xml with configChanges.", adSize, attributeSet);
        return false;
    }

    private boolean b(Context context, AdSize adSize, AttributeSet attributeSet) {
        if (AdUtil.b(context)) {
            return true;
        }
        a(context, "You must have INTERNET and ACCESS_NETWORK_STATE permissions in AndroidManifest.xml.", adSize, attributeSet);
        return false;
    }

    public void destroy() {
        this.a.b();
    }

    private void a(Context context, String str, AdSize adSize, AttributeSet attributeSet) {
        com.google.ads.util.b.b(str);
        a(context, str, -65536, adSize, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public AdSize[] a(String str) {
        AdSize adSize;
        String[] split = str.split(",");
        AdSize[] adSizeArr = new AdSize[split.length];
        for (int i = 0; i < split.length; i++) {
            String trim = split[i].trim();
            if (trim.matches("^(\\d+|FULL_WIDTH)\\s*[xX]\\s*(\\d+|AUTO_HEIGHT)$")) {
                String[] split2 = trim.split("[xX]");
                split2[0] = split2[0].trim();
                split2[1] = split2[1].trim();
                try {
                    adSize = new AdSize("FULL_WIDTH".equals(split2[0]) ? -1 : Integer.parseInt(split2[0]), "AUTO_HEIGHT".equals(split2[1]) ? -2 : Integer.parseInt(split2[1]));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                adSize = "BANNER".equals(trim) ? AdSize.BANNER : "SMART_BANNER".equals(trim) ? AdSize.SMART_BANNER : "IAB_MRECT".equals(trim) ? AdSize.IAB_MRECT : "IAB_BANNER".equals(trim) ? AdSize.IAB_BANNER : "IAB_LEADERBOARD".equals(trim) ? AdSize.IAB_LEADERBOARD : "IAB_WIDE_SKYSCRAPER".equals(trim) ? AdSize.IAB_WIDE_SKYSCRAPER : null;
            }
            if (adSize == null) {
                return null;
            }
            adSizeArr[i] = adSize;
        }
        return adSizeArr;
    }

    private void a(Context context, AttributeSet attributeSet) {
        AdSize[] adSizeArr;
        b bVar;
        if (attributeSet != null) {
            try {
                String b2 = b("adSize", context, attributeSet, true);
                AdSize[] a2 = a(b2);
                if (a2 != null) {
                    try {
                        if (a2.length != 0) {
                            if (!a("adUnitId", attributeSet)) {
                                throw new b("Required XML attribute \"adUnitId\" missing", true);
                            } else if (isInEditMode()) {
                                a(context, "Ads by Google", -1, a2[0], attributeSet);
                                return;
                            } else {
                                String b3 = b("adUnitId", context, attributeSet, true);
                                boolean a3 = a("loadAdOnCreate", context, attributeSet, false);
                                if (context instanceof Activity) {
                                    Activity activity = (Activity) context;
                                    a((Context) activity, a2[0], attributeSet);
                                    b(activity, a2[0], attributeSet);
                                    if (a2.length == 1) {
                                        a(activity, a2[0], b3);
                                    } else {
                                        a(activity, new AdSize(0, 0), b3);
                                        a(a2);
                                    }
                                    if (a3) {
                                        Set<String> c = c("testDevices", context, attributeSet, false);
                                        if (c.contains("TEST_EMULATOR")) {
                                            c.remove("TEST_EMULATOR");
                                            c.add(AdRequest.TEST_EMULATOR);
                                        }
                                        loadAd(new AdRequest().setTestDevices(c).setKeywords(c("keywords", context, attributeSet, false)));
                                        return;
                                    }
                                    return;
                                }
                                throw new b("AdView was initialized with a Context that wasn't an Activity.", true);
                            }
                        }
                    } catch (b e) {
                        bVar = e;
                        adSizeArr = a2;
                        a(context, bVar.c("Could not initialize AdView"), (adSizeArr == null || adSizeArr.length <= 0) ? AdSize.BANNER : adSizeArr[0], attributeSet);
                        bVar.a("Could not initialize AdView");
                        if (!isInEditMode()) {
                            bVar.b("Could not initialize AdView");
                            return;
                        }
                        return;
                    }
                }
                throw new b("Attribute \"adSize\" invalid: " + b2, true);
            } catch (b e2) {
                b bVar2 = e2;
                adSizeArr = null;
                bVar = bVar2;
            }
        }
    }

    private boolean a(String str, Context context, AttributeSet attributeSet, boolean z) throws b {
        String attributeValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/lib/com.google.ads", str);
        boolean attributeBooleanValue = attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/lib/com.google.ads", str, z);
        if (attributeValue != null) {
            String packageName = context.getPackageName();
            if (attributeValue.matches("^@([^:]+)\\:(.*)$")) {
                packageName = attributeValue.replaceFirst("^@([^:]+)\\:(.*)$", "$1");
                attributeValue = attributeValue.replaceFirst("^@([^:]+)\\:(.*)$", "@$2");
            }
            if (attributeValue.startsWith("@bool/")) {
                String substring = attributeValue.substring("@bool/".length());
                TypedValue typedValue = new TypedValue();
                try {
                    getResources().getValue(packageName + ":bool/" + substring, typedValue, true);
                    if (typedValue.type == 18) {
                        return typedValue.data != 0;
                    }
                    throw new b("Resource " + str + " was not a boolean: " + typedValue, true);
                } catch (Resources.NotFoundException e) {
                    throw new b("Could not find resource for " + str + ": " + attributeValue, true, e);
                }
            }
        }
        return attributeBooleanValue;
    }

    private String b(String str, Context context, AttributeSet attributeSet, boolean z) throws b {
        String attributeValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/lib/com.google.ads", str);
        if (attributeValue != null) {
            String packageName = context.getPackageName();
            if (attributeValue.matches("^@([^:]+)\\:(.*)$")) {
                packageName = attributeValue.replaceFirst("^@([^:]+)\\:(.*)$", "$1");
                attributeValue = attributeValue.replaceFirst("^@([^:]+)\\:(.*)$", "@$2");
            }
            if (attributeValue.startsWith("@string/")) {
                String substring = attributeValue.substring("@string/".length());
                TypedValue typedValue = new TypedValue();
                try {
                    getResources().getValue(packageName + ":string/" + substring, typedValue, true);
                    if (typedValue.string != null) {
                        attributeValue = typedValue.string.toString();
                    } else {
                        throw new b("Resource " + str + " was not a string: " + typedValue, true);
                    }
                } catch (Resources.NotFoundException e) {
                    throw new b("Could not find resource for " + str + ": " + attributeValue, true, e);
                }
            }
        }
        if (!z || attributeValue != null) {
            return attributeValue;
        }
        throw new b("Required XML attribute \"" + str + "\" missing", true);
    }

    private Set<String> c(String str, Context context, AttributeSet attributeSet, boolean z) throws b {
        String b2 = b(str, context, attributeSet, z);
        HashSet hashSet = new HashSet();
        if (b2 != null) {
            for (String trim : b2.split(",")) {
                String trim2 = trim.trim();
                if (trim2.length() != 0) {
                    hashSet.add(trim2);
                }
            }
        }
        return hashSet;
    }

    private boolean a(String str, AttributeSet attributeSet) {
        return attributeSet.getAttributeValue("http://schemas.android.com/apk/lib/com.google.ads", str) != null;
    }

    private void a(Activity activity, AdSize adSize, String str) throws b {
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setFocusable(false);
        this.a = new d(this, activity, adSize, str, frameLayout, false);
        setGravity(17);
        try {
            ViewGroup a2 = k.a(activity, this.a);
            if (a2 != null) {
                a2.addView(frameLayout, -2, -2);
                addView(a2, -2, -2);
                return;
            }
            addView(frameLayout, -2, -2);
        } catch (VerifyError e) {
            com.google.ads.util.b.a("Gestures disabled: Not supported on this version of Android.", (Throwable) e);
            addView(frameLayout, -2, -2);
        }
    }

    public boolean isReady() {
        if (this.a == null) {
            return false;
        }
        return this.a.s();
    }

    public boolean isRefreshing() {
        if (this.a == null) {
            return false;
        }
        return this.a.t();
    }

    public void loadAd(AdRequest adRequest) {
        if (this.a != null) {
            if (isRefreshing()) {
                this.a.f();
            }
            this.a.a(adRequest);
        }
    }

    public void setAdListener(AdListener adListener) {
        this.a.i().o.a(adListener);
    }

    /* access modifiers changed from: protected */
    public void setAppEventListener(AppEventListener appEventListener) {
        this.a.i().p.a(appEventListener);
    }

    /* access modifiers changed from: protected */
    public void setSwipeableEventListener(SwipeableAdListener swipeableEventListener) {
        this.a.i().q.a(swipeableEventListener);
    }

    /* access modifiers changed from: protected */
    public void setSupportedAdSizes(AdSize... adSizes) {
        if (this.a.i().n.a() == null) {
            com.google.ads.util.b.e("Warning: Tried to set supported ad sizes on a single-size AdView. AdSizes ignored. To create a multi-sized AdView, use an AdView constructor that takes in an AdSize[] array.");
        } else {
            a(adSizes);
        }
    }

    private void a(AdSize... adSizeArr) {
        AdSize[] adSizeArr2 = new AdSize[adSizeArr.length];
        for (int i = 0; i < adSizeArr.length; i++) {
            adSizeArr2[i] = AdSize.createAdSize(adSizeArr[i], getContext());
        }
        this.a.i().n.a(adSizeArr2);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        AdWebView l;
        if (!isInEditMode() && (l = this.a.l()) != null) {
            l.setVisibility(0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void stopLoading() {
        if (this.a != null) {
            this.a.C();
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (!isInEditMode() && this.a.i().g.a().b() && visibility != 0 && this.a.i().l.a() != null && this.a.i().e.a() != null) {
            if (!AdActivity.isShowing() || AdActivity.leftApplication()) {
                b.a(this.a.i().e.a(), "onleaveapp", (String) null);
            } else {
                b.a(this.a.i().e.a(), "onopeninapp", (String) null);
            }
        }
    }
}
