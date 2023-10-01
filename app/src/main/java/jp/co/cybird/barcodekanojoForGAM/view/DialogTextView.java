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
    private ImageButton mCloseButton;
	private Button mNextButton;
    private TextView mTextView;
	private OnDismissListener listener;
	private String[] mTextStrings;
	private int messageIndex = 0;

    public interface OnDismissListener {
        void OnCloseClick();

		void onNextClick();
    }

    public DialogTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
		setGravity(16);
        setPadding(10, 0, 10, 0);
        LayoutInflater.from(context).inflate(R.layout.view_dialog_frame, this, true);
        getControls();
    }

    public void getControls() {
        this.mTextView = findViewById(R.id.dialog_view_message);
        this.mCloseButton = findViewById(R.id.dialog_close_btn);
        this.mNextButton = findViewById(R.id.dialog_site_btn);
    }

    public void setListener(OnDismissListener listener2) {
        this.listener = listener2;
    }

	public void initDialogMessage(String[] messages) {
		this.mTextStrings = messages;
		this.messageIndex = 0;
		this.mTextView.setText(messages[0]);
		if (this.mTextStrings.length > 1) {
			this.mNextButton.setVisibility(View.VISIBLE);
		} else {
			this.mNextButton.setVisibility(View.INVISIBLE);
		}

		this.mNextButton.setOnClickListener(v -> {
			messageIndex++;
			this.mTextView.setText(this.mTextStrings[this.messageIndex]);
			if (messageIndex+1 >= messages.length) {
				this.mNextButton.setVisibility(View.INVISIBLE);
			}
            if (DialogTextView.this.listener != null) {
				DialogTextView.this.listener.onNextClick();
            }
        });

        this.mCloseButton.setOnClickListener(v -> {
			this.mTextStrings = null;
			this.messageIndex = 0;
            if (DialogTextView.this.listener != null) {
                DialogTextView.this.listener.OnCloseClick();
            }
        });
    }

	public String[] getMessages() {
        return this.mTextStrings;
    }

    public boolean isEmpty() {
		return this.mTextView == null || this.mTextView.getText() == null || this.mTextView.getText().equals("");
	}
}
