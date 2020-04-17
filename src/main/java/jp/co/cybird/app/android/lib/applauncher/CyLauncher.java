package jp.co.cybird.app.android.lib.applauncher;

import android.app.Activity;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;

public class CyLauncher {
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public AppLauncher mLauncher;
    Runnable runInit = new Runnable() {
        public void run() {
            if (CyLauncher.this.mActivity != null) {
                AppLauncher.setUserAgent(CyLauncher.this.mActivity);
                AppLauncher.prepareSchemeData(CyLauncher.this.mActivity, AppLauncherConsts.LAUNCHER_TYPE.list, (String) null);
                CyLauncher.this.mLauncher = new AppLauncher(CyLauncher.this.mActivity, AppLauncherConsts.LAUNCHER_TYPE.list, (String) null);
            }
        }
    };
    Runnable runShowLauncher = new Runnable() {
        public void run() {
            CyLauncher.this.mLauncher.showLauncher();
        }
    };

    public CyLauncher() {
    }

    public CyLauncher(Activity activity) {
        init(activity);
    }

    public void init(Activity activity) {
        setActivity(activity);
        activity.runOnUiThread(this.runInit);
    }

    private void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void onResume() {
        if (this.mLauncher != null) {
            this.mLauncher.onResume();
        }
    }

    public void onPause() {
        if (this.mLauncher != null) {
            this.mLauncher.onPause();
        }
    }

    public void showLauncher() {
        if (this.mLauncher != null) {
            this.mActivity.runOnUiThread(this.runShowLauncher);
        }
    }
}
