package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Scroller;
import java.io.IOException;
import java.lang.ref.WeakReference;
import net.nend.android.DownloadTask;
import net.nend.android.NendConstants;
import org.apache.http.HttpEntity;

@SuppressLint({"ViewConstructor"})
final class OptOutImageView extends ImageView implements View.OnClickListener, DownloadTask.Downloadable<Bitmap> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int MAX_RETRY_CNT = 3;
    private static final int MESSAGE_CODE = 718;
    private static Bitmap optOutImage;
    private final float mDensity;
    private final Handler mHandler;
    private final String mOptOutImageUrl;
    private final String mOptOutUrl;
    private int mRetryCnt = 0;
    private final Scroller mScroller;

    static {
        boolean z;
        if (!OptOutImageView.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    private static class OptOutHandler extends Handler {
        private WeakReference<OptOutImageView> weakReference;

        OptOutHandler(Looper looper, OptOutImageView view) {
            super(looper);
            this.weakReference = new WeakReference<>(view);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            OptOutImageView view = (OptOutImageView) this.weakReference.get();
            if (view != null) {
                view.scrollRight();
            }
        }
    }

    private static final class ScrollParams {
        private static final int SCROLL_LENGTH = 78;
        private static final int SCROLL_TIME_IN_SECOND = 1;
        private static final int WAIT_TIME_IN_SECOND = 1;

        private ScrollParams() {
        }
    }

    private static final class TapMargin {
        private static final int BOTTOM = 18;
        private static final int LEFT = 18;

        private TapMargin() {
        }
    }

    OptOutImageView(Context context, String uid) {
        super(context);
        String str;
        this.mScroller = new Scroller(context);
        this.mHandler = new OptOutHandler(Looper.getMainLooper(), this);
        this.mDensity = getContext().getResources().getDisplayMetrics().density;
        String optOutUrl = "http://nend.net/privacy/optsdkgate";
        String optOutImageUrl = "http://img1.nend.net/img/common/optout/icon.png";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null) {
                optOutUrl = applicationInfo.metaData.getString(NendConstants.MetaData.OPT_OUT_URL.getName()) != null ? applicationInfo.metaData.getString(NendConstants.MetaData.OPT_OUT_URL.getName()) : optOutUrl;
                if (applicationInfo.metaData.getString(NendConstants.MetaData.OPT_OUT_IMAGE_URL.getName()) != null) {
                    optOutImageUrl = applicationInfo.metaData.getString(NendConstants.MetaData.OPT_OUT_IMAGE_URL.getName());
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            NendLog.d(NendStatus.ERR_UNEXPECTED, (Throwable) e);
        } finally {
            str = "?uid=";
            this.mOptOutUrl = String.valueOf(optOutUrl) + str + uid;
            this.mOptOutImageUrl = optOutImageUrl;
        }
        setPadding(realScrollLength(18), 0, realScrollLength(78) * -1, realScrollLength(18));
        setOnClickListener(this);
    }

    /* access modifiers changed from: package-private */
    public void loadImage() {
        if (optOutImage == null) {
            new DownloadTask(this).execute(new Void[0]);
        } else {
            setImageBitmap(optOutImage);
        }
    }

    public String getRequestUrl() {
        return this.mOptOutImageUrl;
    }

    public Bitmap makeResponse(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(entity.getContent());
            if (bitmap != null) {
                return resizeBitmap(bitmap);
            }
            return null;
        } catch (IllegalStateException e) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            NendLog.d(NendStatus.ERR_HTTP_REQUEST, (Throwable) e);
            return null;
        } catch (IOException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            NendLog.d(NendStatus.ERR_HTTP_REQUEST, (Throwable) e2);
            return null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        if ($assertionsDisabled || bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setScale(this.mDensity, this.mDensity);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        throw new AssertionError();
    }

    public void onDownload(Bitmap response) {
        if (response != null) {
            optOutImage = response;
            setImageBitmap(response);
            return;
        }
        this.mRetryCnt++;
        if (this.mRetryCnt < 3) {
            loadImage();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasDrawable() {
        return getDrawable() != null;
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            setPadding(this.mScroller.getCurrX() + ((realScrollLength(18) * (realScrollLength(78) - this.mScroller.getCurrX())) / realScrollLength(78)), 0, realScrollLength(78) * -1, realScrollLength(18));
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public void scrollLeft() {
        this.mScroller.forceFinished(true);
        this.mScroller.startScroll(this.mScroller.getCurrX(), this.mScroller.getCurrY(), realScrollLength(78) - this.mScroller.getCurrX(), 0, 1000);
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void scrollRight() {
        this.mScroller.startScroll(this.mScroller.getCurrX(), this.mScroller.getCurrY(), this.mScroller.getCurrX() * -1, 0, 1000);
        invalidate();
    }

    public void onClick(View v) {
        if (this.mScroller.getCurrX() == ((int) (78.0f * this.mDensity))) {
            NendHelper.startBrowser(v, this.mOptOutUrl);
            return;
        }
        scrollLeft();
        this.mHandler.removeMessages(MESSAGE_CODE);
        this.mHandler.sendEmptyMessageDelayed(MESSAGE_CODE, 2000);
    }

    private int realScrollLength(int scrollLength) {
        return (int) (((float) scrollLength) * this.mDensity);
    }
}
