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
    private int Id;
    private OnMoreClickListener listener;
    private ProgressBar progress = findViewById(R.id.row_more_progressbar);
    private Resources r = getResources();
    private TextView txt = findViewById(R.id.row_more_txt);
    private ImageView ya = findViewById(R.id.row_more_ya);

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
                this.ya.setVisibility(View.GONE);
            }
            if (this.progress != null) {
                this.progress.setVisibility(View.VISIBLE);
                return;
            }
            return;
        }
        if (this.txt != null) {
            this.txt.setText(this.r.getString(R.string.row_more));
        }
        if (this.ya != null) {
            this.ya.setVisibility(View.VISIBLE);
        }
        if (this.progress != null) {
            this.progress.setVisibility(View.INVISIBLE);
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
