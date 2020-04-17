package net.nend.android;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import java.io.IOException;
import net.nend.android.AdParameter;
import net.nend.android.DownloadTask;
import net.nend.android.NendAdImageView;
import net.nend.android.NendConstants;
import org.apache.http.HttpEntity;

public final class NendAdView extends RelativeLayout implements AdListener, DownloadTask.Downloadable<Bitmap>, NendAdImageView.OnAdImageClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
    static final /* synthetic */ boolean $assertionsDisabled = (!NendAdView.class.desiredAssertionStatus());
    private Ad mAd;
    private String mApiKey;
    private NendAdController mController;
    private float mDensity;
    private boolean mHasWindowFocus;
    private NendAdImageView mImageView;
    private boolean mIsClicked;
    private RelativeLayout mLayout;
    private NendAdListener mListener;
    private DisplayMetrics mMetrics;
    private NendError mNendError;
    private OptOutImageView mOptOutImageView;
    private int mSpotId;
    private DownloadTask<Bitmap> mTask;
    private WebView mWebView;

    static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType() {
        int[] iArr = $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
        if (iArr == null) {
            iArr = new int[AdParameter.ViewType.values().length];
            try {
                iArr[AdParameter.ViewType.ADVIEW.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AdParameter.ViewType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AdParameter.ViewType.WEBVIEW.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$net$nend$android$AdParameter$ViewType = iArr;
        }
        return iArr;
    }

    public NendAdView(Context context, int spotId, String apiKey) {
        super(context, (AttributeSet) null, 0);
        this.mDensity = 1.0f;
        this.mAd = null;
        this.mController = null;
        this.mListener = null;
        this.mTask = null;
        this.mHasWindowFocus = false;
        this.mLayout = null;
        this.mOptOutImageView = null;
        this.mImageView = null;
        this.mWebView = null;
        this.mIsClicked = false;
        init(context, spotId, apiKey);
    }

    public NendAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NendAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mDensity = 1.0f;
        this.mAd = null;
        this.mController = null;
        this.mListener = null;
        this.mTask = null;
        this.mHasWindowFocus = false;
        this.mLayout = null;
        this.mOptOutImageView = null;
        this.mImageView = null;
        this.mWebView = null;
        this.mIsClicked = false;
        if (attrs == null) {
            throw new NullPointerException(NendStatus.ERR_INVALID_ATTRIBUTE_SET.getMsg());
        }
        init(context, Integer.parseInt(attrs.getAttributeValue((String) null, NendConstants.Attribute.SPOT_ID.getName())), attrs.getAttributeValue((String) null, NendConstants.Attribute.API_KEY.getName()));
        if (!attrs.getAttributeBooleanValue((String) null, NendConstants.Attribute.RELOADABLE.getName(), true)) {
            pause();
        }
        loadAd();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        deallocateField();
        init(getContext(), this.mSpotId, this.mApiKey);
        loadAd();
    }

    private void init(Context context, int spotId, String apiKey) {
        if (spotId <= 0) {
            throw new IllegalArgumentException(NendStatus.ERR_INVALID_SPOT_ID.getMsg("spot id : " + spotId));
        } else if (apiKey == null || apiKey.length() == 0) {
            throw new IllegalArgumentException(NendStatus.ERR_INVALID_API_KEY.getMsg("api key : " + apiKey));
        } else {
            NendHelper.setDebuggable(context);
            this.mMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(this.mMetrics);
            this.mDensity = this.mMetrics.density;
            this.mSpotId = spotId;
            this.mApiKey = apiKey;
            this.mAd = new NendAd(context, spotId, apiKey, this.mMetrics);
            this.mAd.setListener(this);
            this.mController = new NendAdController(this.mAd);
        }
    }

    public void loadAd() {
        restartController();
        this.mController.requestAd();
    }

    public void setListener(NendAdListener listener) {
        this.mListener = listener;
    }

    public void removeListener() {
        this.mListener = null;
    }

    public void resume() {
        NendLog.d("resume!");
        restartController();
        this.mController.setReloadable(true);
        if (this.mAd.getViewType() == AdParameter.ViewType.WEBVIEW) {
            setWebView();
        }
    }

    public void pause() {
        NendLog.d("pause!");
        restartController();
        this.mController.setReloadable(false);
        if (this.mAd.getViewType() == AdParameter.ViewType.WEBVIEW) {
            deallocateWebView();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mAd == null) {
            this.mAd = new NendAd(getContext(), this.mSpotId, this.mApiKey, this.mMetrics);
            this.mAd.setListener(this);
            this.mController = new NendAdController(this.mAd);
            loadAd();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.mController.onWindowFocusChanged(true);
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        NendLog.d("onWindowFocusChanged!" + String.valueOf(hasWindowFocus));
        super.onWindowFocusChanged(hasWindowFocus);
        this.mHasWindowFocus = hasWindowFocus;
        if (this.mController != null) {
            this.mController.onWindowFocusChanged(hasWindowFocus);
            if (hasWindowFocus && this.mIsClicked) {
                this.mIsClicked = false;
                if (this.mListener != null) {
                    this.mListener.onDismissScreen(this);
                }
            }
        }
    }

    public void onReceiveAd() {
        NendLog.d("onReceive!");
        if (!$assertionsDisabled && this.mAd == null) {
            throw new AssertionError();
        } else if (!isDeallocate()) {
            this.mNendError = null;
            if (getWidth() != 0 && getHeight() != 0 && !isShown()) {
                this.mController.onWindowFocusChanged(false);
            } else if (NendHelper.isConnected(getContext())) {
                switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[this.mAd.getViewType().ordinal()]) {
                    case 2:
                        this.mTask = new DownloadTask<>(this);
                        this.mTask.execute(new Void[0]);
                        return;
                    case 3:
                        if (this.mHasWindowFocus) {
                            setWebView();
                        }
                        if (this.mListener != null) {
                            this.mListener.onReceiveAd(this);
                            return;
                        }
                        return;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        onFailedToReceiveAd(NendError.INVALID_RESPONSE_TYPE);
                        return;
                }
            } else {
                onFailedToReceiveAd(NendError.FAILED_AD_REQUEST);
            }
        }
    }

    public void onFailedToReceiveAd(NendError nendError) {
        NendLog.d("onFailedToReceive!");
        if (!$assertionsDisabled && this.mController == null) {
            throw new AssertionError();
        } else if (!isDeallocate() && this.mController != null) {
            if (!this.mController.reloadAd()) {
                NendLog.d("Failed to reload.");
            }
            if (this.mListener != null) {
                this.mNendError = nendError;
                this.mListener.onFailedToReceiveAd(this);
            }
        }
    }

    public String getRequestUrl() {
        return this.mAd.getImageUrl();
    }

    public Bitmap makeResponse(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        try {
            return BitmapFactory.decodeStream(entity.getContent());
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

    public void onDownload(Bitmap response) {
        NendLog.d("onImageDownload!");
        if (response == null || this.mController == null) {
            onFailedToReceiveAd(NendError.FAILED_AD_DOWNLOAD);
        } else if (!isSizeAppropriate(response)) {
            onFailedToReceiveAd(NendError.AD_SIZE_DIFFERENCES);
        } else {
            setAdView(response);
            this.mController.reloadAd();
            if (this.mListener != null) {
                this.mListener.onReceiveAd(this);
            }
        }
    }

    private boolean isSizeAppropriate(Bitmap bitmap) {
        int height = this.mAd.getHeight();
        int width = this.mAd.getWidth();
        int adHeight = bitmap.getHeight();
        int adWidth = bitmap.getWidth();
        if (adWidth == 320 && adHeight == 48) {
            adHeight = 50;
        }
        return (height == adHeight && width == adWidth) || (height * 2 == adHeight && width * 2 == adWidth);
    }

    private void setAdView(Bitmap bitmap) {
        if (!$assertionsDisabled && bitmap == null) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && this.mAd == null) {
            throw new AssertionError();
        } else if (this.mAd != null) {
            removeAllViews();
            deallocateWebView();
            RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams((int) (((float) this.mAd.getWidth()) * this.mDensity), (int) (((float) this.mAd.getHeight()) * this.mDensity));
            if (this.mLayout == null || this.mImageView == null || this.mOptOutImageView == null || !this.mOptOutImageView.hasDrawable()) {
                this.mLayout = new RelativeLayout(getContext());
                this.mImageView = new NendAdImageView(getContext());
                this.mImageView.setAdInfo(bitmap, this.mAd.getClickUrl());
                this.mImageView.setOnAdImageClickListener(this);
                this.mLayout.addView(this.mImageView, imageLayoutParams);
                this.mOptOutImageView = new OptOutImageView(getContext(), this.mAd.getUid());
                this.mOptOutImageView.loadImage();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(11);
                this.mLayout.addView(this.mOptOutImageView, layoutParams);
            } else {
                this.mImageView.setAdInfo(bitmap, this.mAd.getClickUrl());
            }
            this.mOptOutImageView.bringToFront();
            addView(this.mLayout, imageLayoutParams);
        }
    }

    private void setWebView() {
        if ($assertionsDisabled || this.mAd != null) {
            removeAllViews();
            deallocateAdView();
            this.mWebView = new NendAdWebView(getContext());
            addView(this.mWebView, new RelativeLayout.LayoutParams((int) (((float) this.mAd.getWidth()) * this.mDensity), (int) (((float) this.mAd.getHeight()) * this.mDensity)));
            this.mWebView.resumeTimers();
            this.mWebView.loadUrl(this.mAd.getWebViewUrl());
            return;
        }
        throw new AssertionError();
    }

    public void onAdImageClick(View v) {
        this.mIsClicked = true;
        if (this.mListener != null) {
            this.mListener.onClick(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        NendLog.d("onDetachedFromWindow!");
        deallocateField();
        super.onDetachedFromWindow();
    }

    private void deallocateField() {
        deallocateController();
        deallocateTask();
        deallocateAd();
        removeListener();
        deallocateChildView();
    }

    private void deallocateController() {
        if (this.mController != null) {
            this.mController.cancelRequest();
            this.mController = null;
        }
    }

    private void deallocateAd() {
        if (this.mAd != null) {
            this.mAd.removeListener();
            this.mAd = null;
        }
    }

    private void deallocateTask() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
            this.mTask = null;
        }
    }

    private void deallocateChildView() {
        removeAllViews();
        deallocateAdView();
        deallocateWebView();
    }

    private void deallocateAdView() {
        if (this.mLayout != null) {
            this.mLayout = null;
        }
        if (this.mOptOutImageView != null) {
            this.mOptOutImageView.setImageDrawable((Drawable) null);
            this.mOptOutImageView = null;
        }
        if (this.mImageView != null) {
            this.mImageView = null;
        }
    }

    private void deallocateWebView() {
        if (this.mWebView != null) {
            this.mWebView.stopLoading();
            this.mWebView.pauseTimers();
            this.mWebView.getSettings().setJavaScriptEnabled(false);
            this.mWebView.setWebChromeClient((WebChromeClient) null);
            this.mWebView.setWebViewClient((WebViewClient) null);
            removeView(this.mWebView);
            this.mWebView.removeAllViews();
            this.mWebView.destroy();
            this.mWebView = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasView(View view) {
        if (indexOfChild(view) >= 0) {
            return true;
        }
        return false;
    }

    private boolean isDeallocate() {
        return this.mAd == null;
    }

    private void restartController() {
        if (this.mController == null) {
            if (this.mAd == null) {
                this.mAd = new NendAd(getContext(), this.mSpotId, this.mApiKey, this.mMetrics);
                this.mAd.setListener(this);
            }
            this.mController = new NendAdController(this.mAd);
        }
    }

    public enum NendError {
        AD_SIZE_TOO_LARGE(840, "Ad size will not fit on screen."),
        INVALID_RESPONSE_TYPE(841, "Response type is invalid."),
        FAILED_AD_REQUEST(842, "Failed to Ad request."),
        FAILED_AD_DOWNLOAD(843, "Failed to Ad download."),
        AD_SIZE_DIFFERENCES(844, "Ad size differences.");
        
        private final int code;
        private final String message;

        private NendError(int code2, String message2) {
            this.code = code2;
            this.message = message2;
        }

        public String getMessage() {
            return this.message;
        }

        public int getCode() {
            return this.code;
        }
    }

    public NendError getNendError() {
        return this.mNendError;
    }
}
