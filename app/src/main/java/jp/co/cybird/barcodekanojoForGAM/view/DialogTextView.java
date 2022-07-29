package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.goujer.barcodekanojo.R;

public class DialogTextView extends RelativeLayout {
    private OnDismissListener listener;
    private ImageButton mCloseButton;
	private Button mSiteButton;
    private String mTextString;
    private TextView mTextView;
    private Boolean needMask = false;

    public interface OnDismissListener {
        void OnCloseClick();

        void onSiteClick(String str);
    }

    public DialogTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
		setGravity(16);
        setPadding(10, 0, 10, 0);
        LayoutInflater.from(context).inflate(R.layout.view_dialog_frame, this, true);
        getControls();
        setEvent();
        init(context, attrs);
    }

    public void getControls() {
        this.mTextView = findViewById(R.id.dialog_view_message);
        this.mCloseButton = findViewById(R.id.dialog_close_btn);
        this.mSiteButton = findViewById(R.id.dialog_site_btn);
    }

    public void setListener(OnDismissListener listener2) {
        this.listener = listener2;
    }

    public void setEvent() {
    }

    public void init(Context context, AttributeSet attrs) {
    }

    public void initDialogMessage(String message, final String url, String btntext) {
        this.mTextView.setText(message);
        this.mSiteButton.setText(btntext);
        this.mSiteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DialogTextView.this.listener != null) {
                    DialogTextView.this.listener.onSiteClick(url);
                }
            }
        });
        this.mCloseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DialogTextView.this.listener != null) {
                    DialogTextView.this.listener.OnCloseClick();
                }
            }
        });
    }

    public String getMessage() {
        return this.mTextString;
    }

    public boolean isEmpty() {
		return this.mTextView == null || this.mTextView.getText() == null || this.mTextView.getText().equals("");
	}
}
