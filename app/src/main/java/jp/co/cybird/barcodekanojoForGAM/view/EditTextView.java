package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.goujer.barcodekanojo.R;

public class EditTextView extends LinearLayout {
    private EditText mTextView;

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(16);
        setPadding(10, 0, 10, 0);
        LayoutInflater.from(context).inflate(R.layout.view_edit_text, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextView, 0, 0);
        String text = array.getString(R.styleable.EditTextView_tkey);
        ((TextView) findViewById(R.id.edit_text_key)).setText(text == null ? "" : text);
        this.mTextView = (EditText) findViewById(R.id.edit_text_value);
        String text2 = array.getString(R.styleable.EditTextView_tvalue);
        this.mTextView.setText(text2 == null ? "" : text2);
        int color = array.getColor(R.styleable.EditTextView_ttextColor, 0);
        if (color != 0) {
            this.mTextView.setTextColor(color);
        }
        array.recycle();
    }

    public void setTypeToPassword() {
        if (this.mTextView != null) {
            this.mTextView.setInputType(129);
        }
    }

    public void setValue(String value) {
        if (this.mTextView != null && value != null) {
            this.mTextView.setText(value);
        }
    }

    public String getValue() {
        return this.mTextView.getText().toString();
    }

    public boolean isEmpty() {
        if (this.mTextView == null || this.mTextView.getText() == null || this.mTextView.getText().equals("")) {
            return true;
        }
        return false;
    }
}
