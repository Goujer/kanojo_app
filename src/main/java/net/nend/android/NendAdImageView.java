package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

@SuppressLint({"ViewConstructor"})
final class NendAdImageView extends ImageView implements View.OnClickListener {
    private OnAdImageClickListener listener;
    private String mClickUrl = "";

    interface OnAdImageClickListener {
        void onAdImageClick(View view);
    }

    NendAdImageView(Context context) {
        super(context);
        setScaleType(ImageView.ScaleType.FIT_XY);
        setOnClickListener(this);
    }

    /* access modifiers changed from: package-private */
    public void setAdInfo(Bitmap adImage, String clickUrl) {
        setImageBitmap(adImage);
        if (clickUrl != null) {
            this.mClickUrl = clickUrl;
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnAdImageClickListener(OnAdImageClickListener listener2) {
        this.listener = listener2;
    }

    public void onClick(View v) {
        NendLog.v("click!! url: " + this.mClickUrl);
        if (this.listener != null) {
            this.listener.onAdImageClick(v);
        }
        NendHelper.startBrowser(v, this.mClickUrl);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable((Drawable) null);
    }
}
