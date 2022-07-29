package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.goujer.barcodekanojo.R;

public class CustomLoadingView extends LinearLayout implements View.OnClickListener {

    private final String HIDE_TAG = "hide";
    private final String SHOW_TAG = "show";
    private Context mContext;
    private TextView mLoadingViewMessage;
    private TextView mLoadingViewStatus;
    private LinearLayout mLoadingview;
    private String mbkStatus;

    public CustomLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mContext.getClass().getName();
        LayoutInflater.from(context).inflate(R.layout.view_custom_loading_view, this, true);
        this.mLoadingview = findViewById(R.id.progressbar);
        this.mLoadingview.setOnClickListener(this);
        this.mLoadingview.setVisibility(View.INVISIBLE);
        this.mLoadingview.setTag(HIDE_TAG);
        this.mLoadingViewStatus = findViewById(R.id.loadingViewStatus);
        this.mLoadingViewStatus.setText("Loading...");
        this.mLoadingViewMessage = findViewById(R.id.loadingViewMessage);
    }

    public void onClick(View v) {
        v.getId();
    }

    public void show() {
        this.mLoadingview.setVisibility(View.VISIBLE);
        this.mLoadingview.setTag("show");
    }

    public void dismiss() {
        this.mLoadingview.setVisibility(View.INVISIBLE);
        setStatus(this.mContext.getResources().getString(R.string.common_progress_dialog_message));
        setMessage("");
        this.mLoadingview.setTag("hide");
    }

    public boolean isShow() {
        return this.mLoadingview.getTag().toString().equals("show");
    }

    public void setStatus(String status) {
        this.mLoadingViewStatus.setText(status);
    }

    public void backupStatus() {
        this.mbkStatus = this.mLoadingViewStatus.getText().toString();
    }

    public void restoreStatus() {
        this.mLoadingViewStatus.setText(this.mbkStatus);
    }

    public void setMessage(String message) {
        this.mLoadingViewMessage.setText(message);
    }
}
