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
    private LinearLayout mDashboard;
    private LinearLayout mKanojos;
    private LinearLayout mScan;
    private LinearLayout mSetting;
    private LinearLayout mWebView;

    public CustomToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mContext.getClass().getName();
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(16);
        LayoutInflater.from(context).inflate(R.layout.common_top_menu, this, true);
        this.mDashboard = findViewById(R.id.top_menu_dashboard);
        this.mDashboard.setOnClickListener(this);
        this.mKanojos = findViewById(R.id.top_menu_kanojos);
        this.mKanojos.setOnClickListener(this);
        this.mScan = findViewById(R.id.top_menu_scan);
        this.mScan.setOnClickListener(this);
        this.mWebView = findViewById(R.id.top_menu_webview);
        this.mWebView.setOnClickListener(this);
        this.mSetting = findViewById(R.id.top_menu_setting);
        this.mSetting.setOnClickListener(this);
        setActive();
    }

    @Override
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

    protected void changeTab(Context packageContext, Class<?> cls) {
        if (packageContext.getClass().getName().equalsIgnoreCase(cls.getName())) {
            unlock();
            return;
        }
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        packageContext.startActivity(intent);
        ((Activity) packageContext).overridePendingTransition(0, 0);
    }

    protected void changeTabForResult(Context packageContext, Class<?> cls, int requestCode) {
        if (packageContext.getClass().getName().equalsIgnoreCase(cls.getName())) {
            unlock();
            return;
        }
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ((Activity) packageContext).startActivityForResult(intent, requestCode);
        ((Activity) packageContext).overridePendingTransition(0, 0);
    }

    protected void setActive() {
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

    protected void unlock() {
        this.mDashboard.setOnClickListener(this);
        this.mKanojos.setOnClickListener(this);
        this.mScan.setOnClickListener(this);
        this.mWebView.setOnClickListener(this);
        this.mSetting.setOnClickListener(this);
    }

    protected void lock() {
        this.mDashboard.setOnClickListener(null);
        this.mKanojos.setOnClickListener(null);
        this.mScan.setOnClickListener(null);
        this.mWebView.setOnClickListener(null);
        this.mSetting.setOnClickListener(null);
    }
}
