package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class KanojoView extends RelativeLayout {
    private ProgressBar barLoveGauge = ((ProgressBar) findViewById(R.id.view_kanojo_bar));
    private ImageView imgBarCover = ((ImageView) findViewById(R.id.view_kanojo_bar_cover));
    private ImageView imgKanojo = ((ImageView) findViewById(R.id.view_kanojo_img));
    private ImageView imgKanojoCover = ((ImageView) findViewById(R.id.view_kanojo_img_cover));
    private ImageView imgRate = ((ImageView) findViewById(R.id.view_kanojo_rate));
    private TextView txtName = ((TextView) findViewById(R.id.view_kanojo_name));
    private View viewCover = findViewById(R.id.view_kanojo_cover);

    public KanojoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(16);
        setBackgroundResource(R.drawable.row_kanojos_bg);
        LayoutInflater.from(context).inflate(R.layout.view_kanojo, this, true);
    }

    public void clear() {
        this.imgKanojo.setImageDrawable((Drawable) null);
        this.imgKanojo.setBackgroundDrawable((Drawable) null);
        this.imgKanojoCover.setImageDrawable((Drawable) null);
        this.imgKanojoCover.setBackgroundDrawable((Drawable) null);
        this.imgRate.setImageDrawable((Drawable) null);
        this.imgRate.setBackgroundDrawable((Drawable) null);
        this.imgBarCover.setImageDrawable((Drawable) null);
        this.imgBarCover.setBackgroundDrawable((Drawable) null);
        this.barLoveGauge.setProgressDrawable((Drawable) null);
        this.barLoveGauge.setBackgroundDrawable((Drawable) null);
        this.txtName.setBackgroundDrawable((Drawable) null);
        this.viewCover.setBackgroundDrawable((Drawable) null);
        setBackgroundDrawable((Drawable) null);
    }

    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        this.imgKanojoCover.setPressed(pressed);
        this.imgBarCover.setPressed(pressed);
    }

    public void setSelected(boolean pressed) {
        super.setSelected(pressed);
        this.imgKanojoCover.setSelected(pressed);
        this.imgBarCover.setSelected(pressed);
    }

    public void setKanojo(Kanojo kanojo, RemoteResourceManager rrm) {
        if (kanojo != null) {
            if (this.viewCover != null) {
                this.viewCover.setVisibility(8);
            }
            if (this.imgKanojo != null) {
                ImageCache.setImage(this.imgKanojo, kanojo.getProfile_image_url(), rrm, R.drawable.common_noimage);
            }
            if (this.txtName != null) {
                this.txtName.setText(kanojo.getName());
            }
            if (this.barLoveGauge != null) {
                int lovegauge = kanojo.getLove_gauge();
                this.barLoveGauge.setProgress(0);
                Rect bounds = this.barLoveGauge.getProgressDrawable().getBounds();
                if (lovegauge <= 30) {
                    this.barLoveGauge.setProgressDrawable(getResources().getDrawable(R.drawable.secondary_progress_red));
                } else {
                    this.barLoveGauge.setProgressDrawable(getResources().getDrawable(R.drawable.secondary_progress_blue));
                }
                this.barLoveGauge.getProgressDrawable().setBounds(bounds);
                this.barLoveGauge.setProgress(lovegauge);
            }
            setRateImg(this.imgRate, kanojo.getLike_rate());
            if (kanojo.getMascotEnable() == 1) {
                setBackgroundResource(R.drawable.row_kanojos_permanent_bg);
            } else {
                setBackgroundResource(R.drawable.row_kanojos_bg);
            }
        } else if (this.viewCover != null) {
            this.viewCover.setVisibility(0);
        }
    }

    private void setRateImg(ImageView img, int rate) {
        if (img != null) {
            switch (rate) {
                case 1:
                    img.setImageResource(R.drawable.row_kanojos_star1);
                    return;
                case 2:
                    img.setImageResource(R.drawable.row_kanojos_star2);
                    return;
                case 3:
                    img.setImageResource(R.drawable.row_kanojos_star3);
                    return;
                case 4:
                    img.setImageResource(R.drawable.row_kanojos_star4);
                    return;
                case 5:
                    img.setImageResource(R.drawable.row_kanojos_star5);
                    return;
                default:
                    img.setImageResource(R.drawable.row_kanojos_star0);
                    return;
            }
        }
    }
}
