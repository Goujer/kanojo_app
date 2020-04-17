package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

public class MoreView extends FrameLayout {
    public static final String TAG = "MoreView";
    /* access modifiers changed from: private */
    public int Id;
    /* access modifiers changed from: private */
    public OnMoreClickListener listener;
    /* access modifiers changed from: private */
    public ProgressBar progress;
    /* access modifiers changed from: private */
    public Resources r;
    /* access modifiers changed from: private */
    public TextView txt;
    /* access modifiers changed from: private */
    public ImageView ya;

    public interface OnMoreClickListener {
        void onMoreClick(int i);
    }

    public MoreView(Context context) {
        super(context);
        initView(context);
    }

    public MoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.r = getResources();
        this.txt = (TextView) findViewById(R.id.row_more_txt);
        this.progress = (ProgressBar) findViewById(R.id.row_more_progressbar);
        this.ya = (ImageView) findViewById(R.id.row_more_ya);
        Log.d(TAG, "IDs : " + this.txt.getId() + "," + this.progress.getId() + "," + this.ya.getId());
    }

    private void initView(Context context) {
    }

    public void initView(View root) {
        this.r = getResources();
        this.txt = (TextView) root.findViewById(R.id.row_more_txt);
        this.progress = (ProgressBar) root.findViewById(R.id.row_more_progressbar);
        this.ya = (ImageView) root.findViewById(R.id.row_more_ya);
    }

    public void setId(int id, OnMoreClickListener l) {
        this.Id = id;
        setOnMoreClickListener(l);
    }

    public void setOnMoreClickListener(OnMoreClickListener l) {
        this.listener = l;
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MoreView.this.txt != null) {
                    MoreView.this.txt.setText(MoreView.this.r.getString(R.string.row_more_loading));
                }
                if (MoreView.this.ya != null) {
                    MoreView.this.ya.setVisibility(8);
                }
                if (MoreView.this.progress != null) {
                    MoreView.this.progress.setVisibility(0);
                }
                MoreView.this.listener.onMoreClick(MoreView.this.Id);
            }
        });
    }
}
