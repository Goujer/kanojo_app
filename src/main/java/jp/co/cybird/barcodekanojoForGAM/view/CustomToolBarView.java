package jp.co.cybird.barcodekanojoForGAM.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.DashboardActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.KanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.ScanActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.WebViewTabActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.setting.OptionActivity;

public class CustomToolBarView extends LinearLayout implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private Context mContext;
    private LinearLayout mDashboard = ((LinearLayout) findViewById(R.id.top_menu_dashboard));
    private LinearLayout mKanojos;
    private LinearLayout mScan;
    private LinearLayout mSetting;
    private LinearLayout mWebView;

    public CustomToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mContext.getClass().getName();
        setOrientation(0);
        setGravity(16);
        LayoutInflater.from(context).inflate(R.layout.common_top_menu, this, true);
        this.mDashboard.setOnClickListener(this);
        this.mKanojos = (LinearLayout) findViewById(R.id.top_menu_kanojos);
        this.mKanojos.setOnClickListener(this);
        this.mScan = (LinearLayout) findViewById(R.id.top_menu_scan);
        this.mScan.setOnClickListener(this);
        this.mWebView = (LinearLayout) findViewById(R.id.top_menu_webview);
        this.mWebView.setOnClickListener(this);
        this.mSetting = (LinearLayout) findViewById(R.id.top_menu_setting);
        this.mSetting.setOnClickListener(this);
        setActive();
    }

    public void onClick(View v) {
        lock();
        switch (v.getId()) {
            case R.id.top_menu_kanojos:
                changeTab(this.mContext, KanojosActivity.class);
                break;
            case R.id.top_menu_webview:
                changeTab(this.mContext, WebViewTabActivity.class);
                break;
            case R.id.top_menu_scan:
                changeTabForResult(this.mContext, ScanActivity.class, BaseInterface.REQUEST_SCAN_ACTIVITY);
                break;
            case R.id.top_menu_dashboard:
                changeTab(this.mContext, DashboardActivity.class);
                break;
            case R.id.top_menu_setting:
                changeTabForResult(this.mContext, OptionActivity.class, BaseInterface.REQUEST_OPTION_ACTIVITY);
                break;
        }
        unlock();
    }

    /* access modifiers changed from: protected */
    public void changeTab(Context packageContext, Class<?> cls) {
        if (packageContext.getClass().getName().equalsIgnoreCase(cls.getName())) {
            unlock();
            return;
        }
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        packageContext.startActivity(intent);
        ((Activity) packageContext).overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: protected */
    public void changeTabForResult(Context packageContext, Class<?> cls, int requestCode) {
        if (packageContext.getClass().getName().equalsIgnoreCase(cls.getName())) {
            unlock();
            return;
        }
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        ((Activity) packageContext).startActivityForResult(intent, requestCode);
        ((Activity) packageContext).overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: protected */
    public void setActive() {
        if (this.mContext.getClass().getSimpleName().equals("DashboardActivity")) {
            this.mDashboard.setSelected(true);
        } else if (this.mContext.getClass().getSimpleName().equals(KanojosActivity.TAG)) {
            this.mKanojos.setSelected(true);
        } else if (this.mContext.getClass().getSimpleName().equals("ScanActivity")) {
            this.mScan.setSelected(true);
        } else if (this.mContext.getClass().getSimpleName().equals("OptionActivity")) {
            this.mSetting.setSelected(true);
        } else if (this.mContext.getClass().getSimpleName().equals("WebViewTabActivity")) {
            this.mWebView.setSelected(true);
        }
    }

    /* access modifiers changed from: protected */
    public void unlock() {
        this.mDashboard.setOnClickListener(this);
        this.mKanojos.setOnClickListener(this);
        this.mScan.setOnClickListener(this);
        this.mWebView.setOnClickListener(this);
        this.mSetting.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void lock() {
        this.mDashboard.setOnClickListener((View.OnClickListener) null);
        this.mKanojos.setOnClickListener((View.OnClickListener) null);
        this.mScan.setOnClickListener((View.OnClickListener) null);
        this.mWebView.setOnClickListener((View.OnClickListener) null);
        this.mSetting.setOnClickListener((View.OnClickListener) null);
    }
}
