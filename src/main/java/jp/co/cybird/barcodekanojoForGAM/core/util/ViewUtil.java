package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ViewUtil {
    public static final void cleanupView(View view) {
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
