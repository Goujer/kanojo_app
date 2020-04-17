package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

public class MoreBtnView extends FrameLayout {
    public static final String TAG = "MoreBtnView";
    /* access modifiers changed from: private */
    public int Id;
    /* access modifiers changed from: private */
    public OnMoreClickListener listener;
    private ProgressBar progress = ((ProgressBar) findViewById(R.id.row_more_progressbar));
    private Resources r = getResources();
    private TextView txt = ((TextView) findViewById(R.id.row_more_txt));
    private ImageView ya = ((ImageView) findViewById(R.id.row_more_ya));

    public interface OnMoreClickListener {
        void onMoreClick(int i);
    }

    public MoreBtnView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_more, this, true);
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MoreBtnView.this.setLoading(true);
            }
        });
    }

    public void setLoading(boolean loading) {
        if (loading) {
            if (this.txt != null) {
                this.txt.setText(this.r.getString(R.string.row_more_loading));
            }
            if (this.ya != null) {
                this.ya.setVisibility(8);
            }
            if (this.progress != null) {
                this.progress.setVisibility(0);
                return;
            }
            return;
        }
        if (this.txt != null) {
            this.txt.setText(this.r.getString(R.string.row_more));
        }
        if (this.ya != null) {
            this.ya.setVisibility(0);
        }
        if (this.progress != null) {
            this.progress.setVisibility(4);
        }
    }

    public void setOnMoreClickListener(int id, OnMoreClickListener l) {
        this.Id = id;
        setOnMoreClickListener(l);
    }

    public void setOnMoreClickListener(OnMoreClickListener l) {
        this.listener = l;
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MoreBtnView.this.setLoading(true);
                if (MoreBtnView.this.listener != null) {
                    MoreBtnView.this.listener.onMoreClick(MoreBtnView.this.Id);
                }
            }
        });
    }
}
