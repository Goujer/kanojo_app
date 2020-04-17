package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

public class EditItemView extends LinearLayout {
    private ImageView mAdd = ((ImageView) findViewById(R.id.edit_item_add));
    private ImageView mArrow = ((ImageView) findViewById(R.id.edit_item_arrow));
    private boolean mHoverInital;
    private String mHoverText;
    private ImageView mIcon = ((ImageView) findViewById(R.id.edit_item_icon));
    private ImageView mImageView = ((ImageView) findViewById(R.id.edit_item_avarta));
    private EditItemViewCallback mListener;
    private String mTextString;
    private TextView mTextView;
    private int mtxtColor;
    private Boolean needMask;

    public interface EditItemViewCallback {
        void onTextChange(View view, String str);
    }

    public EditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(0);
        setGravity(16);
        setPadding(10, 0, 10, 0);
        LayoutInflater.from(context).inflate(R.layout.view_edit_item, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditItemView, 0, 0);
        Drawable iconDrawable = array.getDrawable(3);
        if (iconDrawable != null) {
            this.mIcon.setBackgroundDrawable(iconDrawable);
            this.mIcon.setVisibility(0);
        }
        Drawable arrowDrawable = array.getDrawable(4);
        if (arrowDrawable != null) {
            this.mArrow.setBackgroundDrawable(arrowDrawable);
            this.mArrow.setVisibility(0);
            this.mAdd.setVisibility(8);
        }
        Drawable addDrawable = array.getDrawable(5);
        if (addDrawable != null) {
            this.mAdd.setBackgroundDrawable(addDrawable);
            this.mAdd.setVisibility(0);
            this.mArrow.setVisibility(8);
        }
        this.needMask = false;
        this.mTextString = array.getString(0);
        this.mHoverText = array.getString(6);
        if (this.mTextString == null) {
            this.mTextString = "";
        }
        ((TextView) findViewById(R.id.edit_item_key)).setText(this.mTextString);
        this.mTextView = (TextView) findViewById(R.id.edit_item_value);
        this.mTextString = array.getString(1);
        this.mtxtColor = array.getColor(2, 0);
        if (this.mHoverText == null) {
            this.mHoverText = "";
        }
        if (this.mTextString == null) {
            this.mTextString = this.mHoverText;
            this.mHoverInital = true;
            this.mTextView.setTextColor(getResources().getColor(R.color.hover_initial));
        } else {
            this.mHoverInital = false;
            if (this.mtxtColor != 0) {
                this.mTextView.setTextColor(this.mtxtColor);
            }
        }
        this.mTextView.setText(this.mTextString);
        if (this.mtxtColor != 0) {
            this.mTextView.setTextColor(this.mtxtColor);
        }
        array.recycle();
    }

    public void setTextChangeListner(EditItemViewCallback listener) {
        this.mListener = listener;
    }

    public ImageView getAvarta() {
        return this.mImageView;
    }

    public void showText() {
        this.needMask = false;
    }

    public void hideText() {
        this.needMask = true;
    }

    public void showArrow() {
        this.mArrow.setVisibility(0);
    }

    public void hideArrow() {
        this.mArrow.setVisibility(4);
    }

    public ImageView getIcon() {
        return this.mIcon;
    }

    public void showIcon() {
        this.mIcon.setVisibility(0);
    }

    public void hideIcon() {
        this.mIcon.setVisibility(4);
    }

    public ImageView getRightIcon() {
        return this.mAdd;
    }

    public void showIconAdd() {
        this.mAdd.setVisibility(0);
    }

    public void hideIconAdd() {
        this.mAdd.setVisibility(4);
    }

    public void setValue(String value) {
        if (this.mTextView != null && value != null) {
            this.mTextString = value;
            hideHoverDescription();
            if (this.mListener != null) {
                this.mListener.onTextChange(this.mTextView, value);
            }
            if (this.needMask.booleanValue()) {
                this.mTextView.setText("********");
            } else {
                this.mTextView.setText(this.mTextString);
            }
        }
    }

    public void setKey(String value) {
        ((TextView) findViewById(R.id.edit_item_key)).setText(value);
    }

    public String getValue() {
        if (this.mHoverInital) {
            return "";
        }
        return this.mTextString;
    }

    public boolean isEmpty() {
        if (this.mTextView == null || this.mTextView.getText() == null || this.mTextView.getText().equals("")) {
            return true;
        }
        return false;
    }

    public void showHoverDescription() {
        this.mHoverInital = true;
        this.mTextView.setTextColor(getResources().getColor(R.color.hover_initial));
    }

    public void hideHoverDescription() {
        this.mHoverInital = false;
        if (this.mtxtColor != 0) {
            this.mTextView.setTextColor(this.mtxtColor);
        }
    }

    public void setHoverDescription(String value) {
        this.mTextView.setText(value);
        showHoverDescription();
    }
}
