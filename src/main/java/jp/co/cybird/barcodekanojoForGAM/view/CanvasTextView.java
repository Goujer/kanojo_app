package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

public class CanvasTextView extends TextView {
    private static final String TAG = "CanvasTextView";
    private String mText1;
    private String mText2;
    private Resources r;

    public CanvasTextView(Context context) {
        super(context);
        initialize(context);
    }

    public CanvasTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CanvasTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        this.r = context.getResources();
        setDefaultText();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);
        paint.setTextSize(this.r.getDimension(R.dimen.canvas_text_size));
        float posX = (float) ((-getHeight()) / 2);
        float posY = (float) ((getWidth() / 5) * 2);
        canvas.drawText(this.mText1, posX - (paint.measureText(this.mText1) / 2.0f), posY, paint);
        canvas.drawText(this.mText2, posX - (paint.measureText(this.mText2) / 2.0f), 2.0f * posY, paint);
        canvas.rotate(90.0f);
    }

    public void setFoundText() {
        this.mText1 = "";
        this.mText2 = "";
    }

    public void setDefaultText() {
        this.mText1 = this.r.getString(R.string.scan_notice_1);
        this.mText2 = this.r.getString(R.string.scan_notice_2);
    }
}
