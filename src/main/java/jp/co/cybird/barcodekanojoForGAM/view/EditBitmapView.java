package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jp.co.cybird.barcodekanojoForGAM.R;

public class EditBitmapView extends View {

    private static final float MAX_SCALE = 6.0f;
    private static final float MIN_SCALE = 0.2f;
    private static final String TAG = "EditBitmapView";
    private static final float ZOOM_TOLERANCE = 0.05f;
    private Paint backgroundPaint;
    private String dirPath;
    private Rect dstRect;
    boolean first = true;
    private Rect frame;
    private int frameColor;
    private Bitmap mBitmap;
    private int maskColor;
    private Paint paint;
    private float scale = 1.0f;
    private Rect srcRect;
    private float touchX = 0.0f;
    private float touchY = 0.0f;

    public EditBitmapView(Context context) {
        super(context);
        init(context);
    }

    public EditBitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditBitmapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(-16777216);
        this.paint = new Paint();
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.viewfinder_mask);
        this.frameColor = resources.getColor(R.color.white);
        this.dirPath = String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/barcodekanojo/";
    }

    public void setDirPath(String path) {
        this.dirPath = path;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            this.srcRect = new Rect(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());
            this.dstRect = new Rect(this.srcRect);
            this.first = true;
        }
    }

    //JADX had an error
	public void saveBitmap(String filename) throws Throwable {
		Throwable th;
		setDrawingCacheEnabled(true);
		OutputStream outputStream = null;
		try {
			Bitmap source = cropBitmap(getDrawingCache(), this.frame);
			File dir = new File(this.dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream outputStream2 = new FileOutputStream(file);
			if (source != null) {
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 100, outputStream2);
				} catch (NullPointerException e3) {
					outputStream = outputStream2;
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (Throwable th4) {
							return;
						}
					}
				} catch (Throwable th5) {
					th = th5;
					outputStream = outputStream2;
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (Throwable th6) {
						}
					}
					throw th;
				}
			}
			if (outputStream2 != null) {
				try {
					outputStream2.close();
					outputStream = outputStream2;
					return;
				} catch (Throwable th7) {
					outputStream = outputStream2;
					return;
				}
			}
		} catch (FileNotFoundException e4) {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e5) {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (NullPointerException e6) {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Throwable th8) {
			th = th8;
			if (outputStream != null) {
				outputStream.close();
			}
			throw th;
		}
	}

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.frame = getSquareFrame(w, h - 40, w - 20);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (!(!this.first || this.mBitmap == null || this.srcRect == null || this.dstRect == null)) {
            this.first = false;
            this.scale = adjustRect2WindowSize(width, height);
            scale();
            this.dstRect = moveRect(this.dstRect, this.dstRect.centerX() - this.frame.centerX(), this.dstRect.centerY() - this.frame.centerY());
        }
        canvas.drawRect(0.0f, 0.0f, (float) width, (float) height, this.backgroundPaint);
        if (this.mBitmap != null) {
            canvas.drawBitmap(this.mBitmap, this.srcRect, this.dstRect, null);
        }
        this.paint.setColor(this.maskColor);
        canvas.drawRect(0.0f, 0.0f, (float) width, (float) this.frame.top, this.paint);
        canvas.drawRect(0.0f, (float) this.frame.top, (float) this.frame.left, (float) (this.frame.bottom + 1), this.paint);
        canvas.drawRect((float) (this.frame.right + 1), (float) this.frame.top, (float) width, (float) (this.frame.bottom + 1), this.paint);
        canvas.drawRect(0.0f, (float) (this.frame.bottom + 1), (float) width, (float) height, this.paint);
        this.paint.setColor(this.frameColor);
        canvas.drawRect((float) this.frame.left, (float) (this.frame.top - 2), (float) (this.frame.right + 2), (float) this.frame.top, this.paint);
        canvas.drawRect((float) (this.frame.left - 2), (float) (this.frame.top - 2), (float) this.frame.left, (float) this.frame.bottom, this.paint);
        canvas.drawRect((float) this.frame.right, (float) this.frame.top, (float) (this.frame.right + 2), (float) (this.frame.bottom + 2), this.paint);
        canvas.drawRect((float) (this.frame.left - 2), (float) this.frame.bottom, (float) (this.frame.right + 2), (float) (this.frame.bottom + 2), this.paint);
    }

    private Rect getSquareFrame(int width, int height, int length) {
        int diff = width > length ? (width - length) / 2 : 0;
        return new Rect(diff, (height - length) / 2, diff + length, ((height - length) / 2) + width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.touchX = event.getX();
                this.touchY = event.getY();
                break;
            case 2:
                float oldx = this.touchX;
                float oldy = this.touchY;
                this.touchX = event.getX();
                this.touchY = event.getY();
                this.dstRect = moveRect(this.dstRect, (int) (oldx - this.touchX), (int) (oldy - this.touchY));
                break;
        }
        invalidate();
        return true;
    }

    public void ZoomIn() {
        this.scale = (this.scale <= MAX_SCALE ? ZOOM_TOLERANCE : 0.0f) + this.scale;
        scale();
        invalidate();
    }

    public void ZoomOut() {
        this.scale -= this.scale >= 0.2f ? ZOOM_TOLERANCE : 0.0f;
        scale();
        invalidate();
    }

    private void scale() {
        int posX = this.dstRect.centerX();
        int posY = this.dstRect.centerY();
        this.dstRect.left = (int) (((float) this.srcRect.left) * this.scale);
        this.dstRect.top = (int) (((float) this.srcRect.top) * this.scale);
        this.dstRect.right = (int) (((float) this.srcRect.right) * this.scale);
        this.dstRect.bottom = (int) (((float) this.srcRect.bottom) * this.scale);
        moveRect(this.dstRect, this.dstRect.centerX() - posX, this.dstRect.centerY() - posY);
    }

    private float adjustRect2WindowSize(int windowWidth, int windowHeight) {
        int width;
        int height;
        float scale2;
        int width2 = this.dstRect.width();
        int height2 = this.dstRect.height();
        int scaledHeight = (windowWidth * height2) / width2;
        if (scaledHeight < windowHeight) {
            width = windowWidth;
            height = scaledHeight;
            scale2 = ((float) width) / ((float) this.dstRect.width());
        } else {
            width = (windowHeight * width2) / height2;
            height = windowHeight;
            scale2 = ((float) width) / ((float) this.dstRect.width());
        }
        this.dstRect.set(this.dstRect.left, this.dstRect.top, this.dstRect.left + width, this.dstRect.top + height);
        return ((float) ((int) (scale2 / ZOOM_TOLERANCE))) * ZOOM_TOLERANCE;
    }

    private Rect moveRect(Rect rect, int x, int y) {
        rect.left -= x;
        rect.top -= y;
        rect.right -= x;
        rect.bottom -= y;
        return rect;
    }

    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect) {
        Bitmap ret = Bitmap.createBitmap(rect.right - rect.left, rect.bottom - rect.top, bitmap.getConfig());
        new Canvas(ret).drawBitmap(bitmap, (float) (-rect.left), (float) (-rect.top), (Paint) null);
        return ret;
    }
}
