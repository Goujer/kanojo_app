package jp.co.cybird.barcodekanojoForGAM.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

    private Context mContext;
    private final LinearLayout mDashboard;
    private final LinearLayout mKanojos;
    private final LinearLayout mScan;
    private final LinearLayout mSetting;
    private final LinearLayout mWebView;

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
        if ((Build.VERSION.SDK_INT >= 17 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
			|| (Build.VERSION.SDK_INT >= 7 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
			|| Build.VERSION.SDK_INT < 7) {
			this.mScan.setOnClickListener(this);
		} else {
        	mScan.setVisibility(View.GONE);
		}
        this.mWebView = findViewById(R.id.top_menu_webview);
        this.mWebView.setOnClickListener(this);
        this.mSetting = findViewById(R.id.top_menu_setting);
        this.mSetting.setOnClickListener(this);
        setActive();
    }

    @Override
    public void onClick(View v) {
        lock();
		int id = v.getId();
		if (id == R.id.top_menu_kanojos) {
			changeTab(this.mContext, KanojosActivity.class);
		} else if (id == R.id.top_menu_webview) {
			changeTab(this.mContext, WebViewTabActivity.class);
		} else if (id == R.id.top_menu_scan) {
			changeTabForResult(this.mContext, ScanActivity.class, BaseInterface.REQUEST_SCAN_ACTIVITY);
		} else if (id == R.id.top_menu_dashboard) {
			changeTab(this.mContext, DashboardActivity.class);
		} else if (id == R.id.top_menu_setting) {
			changeTabForResult(this.mContext, OptionActivity.class, BaseInterface.REQUEST_OPTION_ACTIVITY);
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
		switch (this.mContext.getClass().getSimpleName()) {
			case "DashboardActivity":
				this.mDashboard.setSelected(true);
				break;
			case KanojosActivity.TAG:
				this.mKanojos.setSelected(true);
				break;
			case "ScanActivity":
				this.mScan.setSelected(true);
				break;
			case "OptionActivity":
				this.mSetting.setSelected(true);
				break;
			case "WebViewTabActivity":
				this.mWebView.setSelected(true);
				break;
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
