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
    private ImageView mAdd;
    private ImageView mArrow;
    private boolean mHoverInital;
	private ImageView mIcon;
    private ImageView mImageView;
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
        setOrientation(HORIZONTAL);
        setGravity(16);
        setPadding(10, 0, 10, 0);
        LayoutInflater.from(context).inflate(R.layout.view_edit_item, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditItemView, 0, 0);

		mImageView = findViewById(R.id.edit_item_avartar);

		Drawable iconDrawable = array.getDrawable(R.styleable.EditItemView_iconDrawable);
		mIcon = findViewById(R.id.edit_item_icon);
		if (iconDrawable != null) {
			mIcon.setBackgroundDrawable(iconDrawable);
			mIcon.setVisibility(VISIBLE);
		}
		Drawable arrowDrawable = array.getDrawable(R.styleable.EditItemView_arrowDrawable);
		mArrow = findViewById(R.id.edit_item_arrow);
		mAdd = findViewById(R.id.edit_item_add);
		if (arrowDrawable != null) {
			mArrow.setBackgroundDrawable(arrowDrawable);
			mArrow.setVisibility(VISIBLE);
			mAdd.setVisibility(GONE);
		}
		Drawable addDrawable = array.getDrawable(R.styleable.EditItemView_addDrawable);
		if (addDrawable != null) {
			mAdd.setBackgroundDrawable(addDrawable);
			mAdd.setVisibility(VISIBLE);
			mArrow.setVisibility(GONE);
		}
        this.needMask = false;
		mTextString = array.getString(R.styleable.EditItemView_key);
		String mHoverText = array.getString(R.styleable.EditItemView_hoverText);
        if (this.mTextString == null) {
            this.mTextString = "";
        }
        ((TextView) findViewById(R.id.edit_item_key)).setText(this.mTextString);
        this.mTextView = findViewById(R.id.edit_item_value);
		this.mTextString = array.getString(R.styleable.EditItemView_value);
		this.mtxtColor = array.getColor(R.styleable.EditItemView_textColor, 0);
        if (mHoverText == null) {
            mHoverText = "";
        }
        if (this.mTextString == null) {
            this.mTextString = mHoverText;
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

    public ImageView getAvatar() {
        return this.mImageView;
    }

//    public void showText() {
//        this.needMask = false;
//    }

    public void hideText() {
        this.needMask = true;
    }

//    public void showArrow() {
//        this.mArrow.setVisibility(View.VISIBLE);
//    }
//
//    public void hideArrow() {
//        this.mArrow.setVisibility(View.INVISIBLE);
//    }

    public ImageView getIcon() {
        return this.mIcon;
    }

//    public void showIcon() {
//        this.mIcon.setVisibility(VISIBLE);
//    }
//
//    public void hideIcon() {
//        this.mIcon.setVisibility(INVISIBLE);
//    }
//
//    public ImageView getRightIcon() {
//        return this.mAdd;
//    }

	//public void showIconAdd() {
	//	this.mAdd.setVisibility(VISIBLE);
	//}

	//public void hideIconAdd() {
	//	this.mAdd.setVisibility(INVISIBLE);
	//}

    public void setValue(String value) {
        if (this.mTextView != null && value != null) {
            this.mTextString = value;
            hideHoverDescription();
            if (this.mListener != null) {
                this.mListener.onTextChange(this.mTextView, value);
            }
            if (this.needMask && !value.equals("")) {
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
        return this.mTextString.trim();
    }

    public boolean isEmpty() {
		return this.mTextView == null || this.mTextView.getText() == null || this.mTextView.getText().equals("");
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
