package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import jp.co.cybird.barcodekanojoForGAM.R;

public class GroupFieldControls extends LinearLayout {
    public static final int HOUR_SELECT_WITH_IMAGE_TYPE = 2;
    public static final int HOUR_SELECT_WITH_NO_IMAGE_TYPE = 3;
    public static final int NORMAL_WITH_IMAGE_TYPE = 0;
    public static final int NORMAL_WITH_NO_IMAGE_TYPE = 1;
    public static final int SWITCH_ON_OFF_WITH_IMAGE_TYPE = 4;
    public static final int SWITCH_ON_OFF_WITH_NO_IMAGE_TYPE = 5;
    private Context _ctx;
    private ArrayList<ImageView> lstImageLeft;
    private ArrayList<View> lstObjRight;
    private ArrayList<TextView> lstTxtTitle;
    private int numFields = 0;

    public interface onClickCallBack {
        void onClick(View view);
    }

    public GroupFieldControls(Context context) {
        super(context, (AttributeSet) null);
    }

    public GroupFieldControls(Context context, AttributeSet attribute) {
        super(context, attribute);
        this._ctx = context;
        setBackgroundResource(R.drawable.bg_item_setting);
        setPadding(0, 0, 0, 0);
        setOrientation(1);
    }

    public void initControls() {
        AddDivineLine();
    }

    public void AddDivineLine() {
        TextView v = new TextView(this._ctx);
        v.setBackgroundColor(-16711936);
        v.setHeight(1);
        addView(v, new LinearLayout.LayoutParams(-1, -2));
    }

    public void AddNewFieldControl(int imgLeft, String text, int type) {
        RelativeLayout rl = new RelativeLayout(this._ctx);
        rl.setPadding(7, 3, 7, 3);
        View firstView = MakeFirstView(rl, imgLeft, type);
        View MakeSecondView = MakeSecondView(rl, firstView, text, type);
        View MakeThirdView = MakeThirdView(rl, type);
        switch (type) {
            case 1:
                firstView.setVisibility(View.GONE);
                break;
        }
        addView(rl, new LinearLayout.LayoutParams(-1, -2));
    }

    private void validate() {
    }

    private View MakeFirstView(RelativeLayout parent, int imgLeft, int type) {
        ImageView img = new ImageView(this._ctx);
        if (imgLeft == 0) {
            img.setBackgroundResource(R.drawable.icon);
        } else {
            img.setBackgroundResource(imgLeft);
        }
        img.setId(1);
        img.setPadding(5, 0, 5, 0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);
        params.addRule(9, 1);
        params.addRule(10, 1);
        params.addRule(15, 1);
        parent.addView(img, params);
        return img;
    }

    private View MakeSecondView(RelativeLayout parent, View leftView, String title, int type) {
        TextView txt = new TextView(this._ctx);
        txt.setText(title);
        txt.setId(2);
        txt.setPadding(5, 0, 5, 0);
        txt.setTextColor(-16777216);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(-2, -2);
        params1.addRule(10, 1);
        params1.addRule(1, leftView.getId());
        params1.addRule(15, 1);
        parent.addView(txt, params1);
        return txt;
    }

    private View MakeThirdView(RelativeLayout parent, int type) {
        switch (type) {
            case 0:
            case 1:
                ImageView ctlView = new ImageView(this._ctx);
                ctlView.setBackgroundResource(R.drawable.arrow_setting);
                ctlView.setPadding(5, 0, 5, 0);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(30, 30);
                params2.addRule(11, 1);
                params2.addRule(10, 1);
                params2.addRule(15, 1);
                parent.addView(ctlView, params2);
                return ctlView;
            case 2:
            case 3:
                TextView txt = new TextView(this._ctx);
                txt.setText("12:00");
                txt.setId(2);
                txt.setPadding(5, 0, 5, 0);
                txt.setTextColor(-16711936);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(-2, -2);
                params1.addRule(11, 1);
                params1.addRule(10, 1);
                params1.addRule(15, 1);
                parent.addView(txt, params1);
                return txt;
            default:
                return null;
        }
    }

    public class GroupFieldException extends Exception {
        private static final long serialVersionUID = -4301616041818324267L;

        public GroupFieldException(String message) {
            super(message);
        }
    }

    public class GroupFieldControlItem extends RelativeLayout {
        public GroupFieldControlItem(Context context) {
            super(context);
        }
    }

    public abstract class BaseColumnField extends RelativeLayout implements View.OnClickListener {
        onClickCallBack mListener;

        public BaseColumnField(Context context) {
            super(context);
        }

        public void setListener(onClickCallBack listener) {
            this.mListener = listener;
        }

        public void onClick(View v) {
            this.mListener.onClick(v);
        }
    }

    public class OneColulmnField extends BaseColumnField {
        protected int mString;
        protected int mTxtFontColor;
        protected float mTxtFontSize;
        protected TextView mTxtView;

        public OneColulmnField(Context context) {
            super(context);
            this.mTxtView = new TextView(context);
            this.mTxtView.setTextColor(this.mTxtFontColor);
            this.mTxtView.setText(this.mString);
            this.mTxtView.setTextSize(this.mTxtFontSize);
            this.mTxtView.setOnClickListener(this);
        }

        public void onClick(View v) {
            super.onClick(v);
        }
    }

    public class TwoColumnField extends OneColulmnField {
        protected int mRightDrawableOn;

        public TwoColumnField(Context context) {
            super(context);
        }
    }

    public class ToggleColumnField extends TwoColumnField {
        protected int mRightDrawableOff;
        protected boolean mStatus;

        public ToggleColumnField(Context context) {
            super(context);
        }
    }
}
