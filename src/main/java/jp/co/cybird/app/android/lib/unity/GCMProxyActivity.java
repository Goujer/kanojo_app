package jp.co.cybird.app.android.lib.unity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.lang.ref.WeakReference;
import jp.co.cybird.android.lib.gcm.GCMTransfer;
import jp.co.cybird.android.lib.gcm.GCMUtilities;

public class GCMProxyActivity extends Activity {
    static String[] activityArray = {"com.unity3d.player.UnityPlayerActivity", "com.unity3d.player.UnityPlayerNativeActivity"};
    static String[] customActivityArray = new String[2];
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Class<?> unityClass;
            int i = 1;
            if (GCMProxyActivity.mThisActivity.get() != null) {
                try {
                    if (Build.VERSION.SDK_INT < 9) {
                        i = 0;
                    }
                    if (GCMProxyActivity.customActivityArray[0] == null || GCMProxyActivity.customActivityArray[1] == null) {
                        unityClass = Class.forName(GCMProxyActivity.activityArray[i]);
                    } else {
                        unityClass = Class.forName(GCMProxyActivity.customActivityArray[i]);
                    }
                    Intent unityIntent = new Intent(((Activity) GCMProxyActivity.mThisActivity.get()).getApplication(), unityClass);
                    unityIntent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    ((Activity) GCMProxyActivity.mThisActivity.get()).startActivity(unityIntent);
                    ((Activity) GCMProxyActivity.mThisActivity.get()).finish();
                } catch (ClassNotFoundException e) {
                    ((Activity) GCMProxyActivity.mThisActivity.get()).finish();
                } catch (Throwable th) {
                    Throwable th2 = th;
                    ((Activity) GCMProxyActivity.mThisActivity.get()).finish();
                    throw th2;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public static WeakReference<Activity> mThisActivity;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Context context = getApplicationContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), 0).edit();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.getBoolean("push", false)) {
            editor.remove(GCMUtilities.CYLIB_GCM_PARAM_ISPUSH);
            editor.remove(GCMUtilities.CYLIB_GCM_PARAM_DATA);
            editor.commit();
            GCMTransfer.action(this);
            return;
        }
        loadActivityClassName(this);
        editor.putInt(GCMUtilities.CYLIB_GCM_PARAM_ISPUSH, 1);
        String param = GCMUtilities.parseParametersString(getIntent());
        if (!param.equals("")) {
            editor.putString(GCMUtilities.CYLIB_GCM_PARAM_DATA, param);
        }
        editor.commit();
        mThisActivity = new WeakReference<>(this);
        mHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: protected */
    public void loadActivityClassName(Context context) {
        boolean classFound;
        boolean classFound2;
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (key.equals("gcm.unity.activity")) {
                        String className = bundle.getString(key);
                        try {
                            Class.forName(className);
                            classFound2 = true;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            classFound2 = false;
                        }
                        if (classFound2) {
                            customActivityArray[0] = className;
                        } else {
                            customActivityArray[0] = null;
                        }
                    } else if (key.equals("gcm.unity.nativeactivity")) {
                        String className2 = bundle.getString(key);
                        try {
                            Class.forName(className2);
                            classFound = true;
                        } catch (ClassNotFoundException e2) {
                            e2.printStackTrace();
                            classFound = false;
                        }
                        if (classFound) {
                            customActivityArray[1] = className2;
                        } else {
                            customActivityArray[1] = null;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e3) {
            e3.printStackTrace();
        } catch (NullPointerException e4) {
            e4.printStackTrace();
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
        } catch (Exception e6) {
            e6.printStackTrace();
        }
        if (customActivityArray[1] == null && customActivityArray[0] != null) {
            customActivityArray[1] = customActivityArray[0];
        }
    }
}
