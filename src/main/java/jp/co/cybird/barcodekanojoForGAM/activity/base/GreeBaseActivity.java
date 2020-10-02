package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import jp.co.cybird.barcodekanojoForGAM.R;

public abstract class GreeBaseActivity extends Activity {

    public static final String FLG_NO_ANIMATION = "flg_no_animation";
    private static final String PREF_KEY = "BarcodeKanojoGree";
    private static final String PREF_KEY_GREE_ID = "GreeUserId";
    private static final String TAG = "GreeBaseActivity";
    private boolean backTabFlg = false;
    private boolean isReUsed = true;
    private boolean layoutFinished;
    private int layoutResID;
    private Bundle savedInstanceState;

    @Override
    protected void onStop() {
        ViewGroup root = getWindow().getDecorView().findViewById(R.id.common_top_menu_root);
        if (!(this.isReUsed || root == null || root.getChildCount() == 0)) {
            cleanupView(root.getChildAt(0));
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!this.isReUsed) {
            finish();
        }
    }

    protected void stop() {
        this.isReUsed = false;
    }

    protected void close() {
        finish();
    }

    protected void changeTab(Context packageContext, Class<?> cls) {
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(FLG_NO_ANIMATION, true);
        startActivity(intent);
    }

    protected void backTab(Context packageContext, Class<?> cls) {
        this.backTabFlg = true;
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(FLG_NO_ANIMATION, true);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.backTabFlg = false;
        if (intent != null && intent.getBooleanExtra(FLG_NO_ANIMATION, false)) {
            overridePendingTransition(0, 0);
        }
    }

    protected void clearHistory() {
    }

    @Override
    protected void onDestroy() {
        if (this.savedInstanceState != null) {
            this.savedInstanceState.clear();
            this.savedInstanceState = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        this.savedInstanceState = savedInstanceState2;
        super.onCreate(savedInstanceState2);
    }

    protected View getClientView() {
        ViewGroup root = getWindow().getDecorView().findViewById(R.id.common_top_menu_root);
        if (root == null || root.getChildCount() == 0) {
            return null;
        }
        return root.getChildAt(0);
    }

    private void cleanupView(View view) {
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback(null);
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable(null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        } else if (view instanceof SeekBar) {
            SeekBar sb = (SeekBar) view;
            sb.setProgressDrawable(null);
            sb.setThumb(null);
        } else if (view instanceof TextView) {	//Also include Button
        	if (Build.VERSION.SDK_INT >= 16) {
				view.setBackground(null);
			} else {
				view.setBackgroundDrawable(null);
			}
        }
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(null);
		} else {
			view.setBackgroundDrawable(null);
		}
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int size = vg.getChildCount();
            for (int i = 0; i < size; i++) {
                cleanupView(vg.getChildAt(i));
            }
        }
    }
}
