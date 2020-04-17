package jp.gree.reward.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.AdActivity;
import java.util.SortedMap;
import java.util.TreeMap;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import jp.gree.reward.compress.d;
import jp.gree.reward.compress.e;
import jp.gree.reward.compress.f;
import jp.gree.reward.compress.g;
import jp.gree.reward.compress.i;
import twitter4j.conf.PropertyConfiguration;

public class GreeRewardPromotionLayout extends LinearLayout {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private Context f5a;

    /* renamed from: a  reason: collision with other field name */
    private View f6a;

    /* renamed from: a  reason: collision with other field name */
    private WebView f7a;

    /* renamed from: a  reason: collision with other field name */
    private Button f8a;

    /* renamed from: a  reason: collision with other field name */
    private RelativeLayout f9a;

    /* renamed from: a  reason: collision with other field name */
    private TextView f10a;

    /* renamed from: a  reason: collision with other field name */
    private String f11a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f12a = false;
    private int b = 0;

    /* renamed from: b  reason: collision with other field name */
    private View f13b;

    /* renamed from: b  reason: collision with other field name */
    private WebView f14b;

    /* renamed from: b  reason: collision with other field name */
    private RelativeLayout f15b;

    /* renamed from: b  reason: collision with other field name */
    private TextView f16b;

    /* renamed from: b  reason: collision with other field name */
    private String f17b;
    private int c = 0;

    /* renamed from: c  reason: collision with other field name */
    private String f18c = "";
    private String d = "";
    private String e = "";
    private String f = "";

    public GreeRewardPromotionLayout(Context context) {
        super(context);
        a(context);
    }

    public GreeRewardPromotionLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
    }

    /* access modifiers changed from: private */
    public String a(String str) {
        TreeMap treeMap = new TreeMap();
        String a2 = i.a((TelephonyManager) this.f5a.getSystemService("phone"));
        treeMap.put("IDENTIFIER".toLowerCase(), this.f18c);
        treeMap.put("MEDIA_ID".toLowerCase(), String.valueOf(this.a));
        treeMap.put(PropertyConfiguration.USER, i.a(a2));
        treeMap.put("model", Build.MODEL);
        treeMap.put("system", Build.VERSION.RELEASE);
        if (this.d.length() != 0 && this.b > 0) {
            treeMap.put("ITEM_IDENTIFIER".toLowerCase(), this.d);
            treeMap.put("ITEM_PRICE".toLowerCase(), String.valueOf(this.b));
            treeMap.put("ITEM_NAME".toLowerCase(), this.e);
            treeMap.put("ITEM_IMAGE".toLowerCase(), this.f);
        }
        if (this.c != 0 && (str.equals(AdActivity.INTENT_ACTION_PARAM) || str.equals(AdActivity.COMPONENT_NAME_PARAM))) {
            treeMap.put("CAMPAIGN_ID".toLowerCase(), String.valueOf(this.c));
            if (str.equals(AdActivity.INTENT_ACTION_PARAM)) {
                str = "r";
            }
        }
        if (i.a()) {
            treeMap.put("privileged", GreeDefs.KANOJO_NAME);
        }
        return String.valueOf(i.a()) + GreeDefs.COMPANY_NAME + "." + GreeDefs.BARCODE + "." + this.f11a + str + "?" + i.a((SortedMap) treeMap, this.f17b);
    }

    private void a(Context context) {
        setOrientation(1);
        this.f5a = context;
        float f2 = context.getResources().getDisplayMetrics().density;
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, (int) ((42.0f * f2) + 0.5f));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams((int) ((60.0f * f2) + 0.5f), -1);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-1, -1);
        this.f9a = new RelativeLayout(context);
        this.f9a.setBackgroundColor(-16777216);
        this.f9a.setPadding(0, (int) ((2.0f * f2) + 0.5f), 0, 0);
        Button button = new Button(context);
        button.setText("Back");
        button.setOnClickListener(new f(this));
        this.f10a = new TextView(context);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(layoutParams3);
        layoutParams6.addRule(13, 1);
        layoutParams6.addRule(15, 1);
        this.f8a = new Button(context);
        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(layoutParams4);
        this.f8a.setText("問合せ");
        this.f8a.setVisibility(8);
        layoutParams7.addRule(11, 1);
        this.f8a.setOnClickListener(new g(this));
        this.f6a = new ProgressBar(context, (AttributeSet) null, 16842873);
        RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(layoutParams3);
        this.f6a.setPadding(0, 0, (int) ((2.0f * f2) + 0.5f), 0);
        layoutParams8.addRule(15, 1);
        layoutParams8.addRule(11, 1);
        addView(this.f9a, layoutParams);
        this.f9a.addView(button, layoutParams2);
        this.f9a.addView(this.f10a, layoutParams6);
        this.f9a.addView(this.f8a, layoutParams7);
        this.f9a.addView(this.f6a, layoutParams8);
        this.f7a = new WebView(context);
        addView(this.f7a, layoutParams5);
        this.f15b = new RelativeLayout(context);
        this.f15b.setBackgroundColor(-16777216);
        this.f15b.setPadding(0, (int) ((2.0f * f2) + 0.5f), 0, 0);
        this.f15b.setVisibility(8);
        this.f16b = new TextView(context);
        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(layoutParams3);
        this.f16b.setTextColor(-1);
        this.f16b.setTypeface(Typeface.DEFAULT_BOLD);
        layoutParams9.addRule(13, 1);
        layoutParams9.addRule(15, 1);
        Button button2 = new Button(context);
        button2.setText("Back");
        button2.setOnClickListener(new e(this));
        this.f13b = new ProgressBar(context, (AttributeSet) null, 16842873);
        RelativeLayout.LayoutParams layoutParams10 = new RelativeLayout.LayoutParams(layoutParams3);
        this.f13b.setPadding(0, 0, (int) ((f2 * 2.0f) + 0.5f), 0);
        layoutParams10.addRule(15, 1);
        layoutParams10.addRule(11, 1);
        addView(this.f15b, layoutParams);
        this.f15b.addView(this.f16b, layoutParams9);
        this.f15b.addView(button2, layoutParams2);
        this.f15b.addView(this.f13b, layoutParams10);
        this.f14b = new WebView(context);
        addView(this.f14b, layoutParams5);
    }

    private static void a(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            i.a("GreeRewardPromotionLayout", String.valueOf(str) + " is null or empty.");
        }
    }

    public boolean getClickCampaign() {
        return this.f12a;
    }

    public void hidePromotionView() {
        super.setVisibility(8);
    }

    public void hidePromotionView(Animation animation) {
        hidePromotionView();
        startAnimation(animation);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.f7a == null || !this.f7a.canGoBack()) {
            return super.onKeyDown(i, keyEvent);
        }
        this.f7a.goBack();
        return false;
    }

    public void setCampaignId(int i) {
        this.c = i;
    }

    public void setClickCampaign(boolean z) {
        this.f12a = z;
    }

    public void setIdentifier(String str) {
        this.f18c = str;
    }

    public void setItemIdentifier(String str) {
        this.d = str;
    }

    public void setItemImage(String str) {
        this.f = str;
    }

    public void setItemName(String str) {
        this.e = str;
    }

    public void setItemPrice(int i) {
        this.b = i;
    }

    public void setMediaId(int i) {
        this.a = i;
    }

    public void showPromotionView() {
        super.setVisibility(0);
        try {
            ApplicationInfo applicationInfo = this.f5a.getPackageManager().getApplicationInfo(this.f5a.getPackageName(), 128);
            i.b(i.a(applicationInfo, "DEVELOP_MODE"));
            i.a(i.a(applicationInfo, "TEST_MODE"));
            this.f17b = i.a(applicationInfo, "SITE_KEY");
            this.f11a = i.a(applicationInfo, "SITE_ID");
            a("SITE_KEY", this.f17b);
            a("SITE_ID", this.f11a);
            a("IDENTIFIER", this.f18c);
        } catch (PackageManager.NameNotFoundException e2) {
            i.a("GreeRewardPromotionLayout", "init", e2);
        }
        if (this.f12a) {
            String a2 = a(AdActivity.COMPONENT_NAME_PARAM);
            i.a("clickUrl: ", a2);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(a2));
            this.f5a.startActivity(intent);
            return;
        }
        this.f7a.setWebViewClient(new d(this));
        this.f7a.getSettings().setBuiltInZoomControls(true);
        this.f7a.getSettings().setJavaScriptEnabled(true);
        String a3 = a(AdActivity.INTENT_ACTION_PARAM);
        i.a("promotionUrl: ", a3);
        this.f7a.loadUrl(a3);
        this.f7a.setScrollBarStyle(33554432);
    }

    public void showPromotionView(int i, String str) {
        setMediaId(i);
        setIdentifier(str);
        showPromotionView();
    }

    public void showPromotionView(Animation animation) {
        showPromotionView();
        startAnimation(animation);
    }
}
