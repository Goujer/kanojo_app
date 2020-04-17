package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public abstract class GreeBaseActivity extends Activity {
    private static final boolean DEBUG = false;
    public static final String FLG_NO_ANIMATION = "flg_no_animation";
    private static final String PREF_KEY = "BarcodeKanojoGree";
    private static final String PREF_KEY_GREE_ID = "GreeUserId";
    private static final String TAG = "GreeBaseActivity";
    private boolean backTabFlg = false;
    private boolean isReUsed = true;
    private boolean layoutFinished;
    private int layoutResID;
    private Bundle savedInstanceState;

    /* access modifiers changed from: protected */
    public void onStop() {
        ViewGroup root;
        if (!(this.isReUsed || (root = (ViewGroup) getWindow().getDecorView().findViewById(16908290)) == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!this.isReUsed) {
            close();
        }
    }

    /* access modifiers changed from: protected */
    public void stop() {
        this.isReUsed = false;
    }

    /* access modifiers changed from: protected */
    public void close() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void changeTab(Context packageContext, Class<?> cls) {
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        intent.putExtra(FLG_NO_ANIMATION, true);
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void backTab(Context packageContext, Class<?> cls) {
        this.backTabFlg = true;
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.setFlags(AccessibilityEventCompat.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY);
        intent.addFlags(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        intent.putExtra(FLG_NO_ANIMATION, true);
        startActivity(intent);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.backTabFlg = false;
        if (intent != null && intent.getBooleanExtra(FLG_NO_ANIMATION, false)) {
            overridePendingTransition(0, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void clearHistory() {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.savedInstanceState != null) {
            this.savedInstanceState.clear();
            this.savedInstanceState = null;
        }
        super.onDestroy();
    }

    public void onCreate(Bundle savedInstanceState2) {
        this.savedInstanceState = savedInstanceState2;
        super.onCreate(savedInstanceState2);
    }

    public void setContentView(int layoutResID2) {
        this.layoutResID = layoutResID2;
        super.setContentView(layoutResID2);
    }

    /* access modifiers changed from: protected */
    public View getClientView() {
        ViewGroup root = (ViewGroup) getWindow().getDecorView().findViewById(16908290);
        if (root == null || root.getChildCount() == 0) {
            return null;
        }
        return root.getChildAt(0);
    }

    private void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback((Drawable.Callback) null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable((Drawable) null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable((Drawable) null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable((Drawable) null);
            sb.setThumb((Drawable) null);
        } else if (view instanceof TextView) {
            ((TextView) view).setBackgroundDrawable((Drawable) null);
        } else if (view instanceof Button) {
            ((Button) view).setBackgroundDrawable((Drawable) null);
        }
        view.setBackgroundDrawable((Drawable) null);
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int size = vg.getChildCount();
            for (int i = 0; i < size; i++) {
                cleanupView(vg.getChildAt(i));
            }
        }
    }
}
