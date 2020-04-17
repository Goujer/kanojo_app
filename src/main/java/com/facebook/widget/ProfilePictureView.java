package com.facebook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.facebook.FacebookException;
import com.facebook.LoggingBehavior;
import com.facebook.android.R;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.widget.ImageRequest;
import java.net.MalformedURLException;

public class ProfilePictureView extends FrameLayout {
    private static final String BITMAP_HEIGHT_KEY = "ProfilePictureView_height";
    private static final String BITMAP_KEY = "ProfilePictureView_bitmap";
    private static final String BITMAP_WIDTH_KEY = "ProfilePictureView_width";
    public static final int CUSTOM = -1;
    private static final boolean IS_CROPPED_DEFAULT_VALUE = true;
    private static final String IS_CROPPED_KEY = "ProfilePictureView_isCropped";
    public static final int LARGE = -4;
    private static final int MIN_SIZE = 1;
    public static final int NORMAL = -3;
    private static final String PENDING_REFRESH_KEY = "ProfilePictureView_refresh";
    private static final String PRESET_SIZE_KEY = "ProfilePictureView_presetSize";
    private static final String PROFILE_ID_KEY = "ProfilePictureView_profileId";
    public static final int SMALL = -2;
    private static final String SUPER_STATE_KEY = "ProfilePictureView_superState";
    public static final String TAG = ProfilePictureView.class.getSimpleName();
    private ImageView image;
    private Bitmap imageContents;
    private boolean isCropped = true;
    private ImageRequest lastRequest;
    private OnErrorListener onErrorListener;
    private int presetSizeType = -1;
    private String profileId;
    private int queryHeight = 0;
    private int queryWidth = 0;

    public interface OnErrorListener {
        void onError(FacebookException facebookException);
    }

    public ProfilePictureView(Context context) {
        super(context);
        initialize(context);
    }

    public ProfilePictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        parseAttributes(attrs);
    }

    public ProfilePictureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
        parseAttributes(attrs);
    }

    public final int getPresetSize() {
        return this.presetSizeType;
    }

    public final void setPresetSize(int sizeType) {
        switch (sizeType) {
            case LARGE /*-4*/:
            case NORMAL /*-3*/:
            case -2:
            case -1:
                this.presetSizeType = sizeType;
                requestLayout();
                return;
            default:
                throw new IllegalArgumentException("Must use a predefined preset size");
        }
    }

    public final boolean isCropped() {
        return this.isCropped;
    }

    public final void setCropped(boolean showCroppedVersion) {
        this.isCropped = showCroppedVersion;
        refreshImage(false);
    }

    public final String getProfileId() {
        return this.profileId;
    }

    public final void setProfileId(String profileId2) {
        boolean force = false;
        if (Utility.isNullOrEmpty(this.profileId) || !this.profileId.equalsIgnoreCase(profileId2)) {
            setBlankProfilePicture();
            force = true;
        }
        this.profileId = profileId2;
        refreshImage(force);
    }

    public final OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public final void setOnErrorListener(OnErrorListener onErrorListener2) {
        this.onErrorListener = onErrorListener2;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams params = getLayoutParams();
        boolean customMeasure = false;
        int newHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int newWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) != 1073741824 && params.height == -2) {
            newHeight = getPresetSizeInPixels(true);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(newHeight, 1073741824);
            customMeasure = true;
        }
        if (View.MeasureSpec.getMode(widthMeasureSpec) != 1073741824 && params.width == -2) {
            newWidth = getPresetSizeInPixels(true);
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(newWidth, 1073741824);
            customMeasure = true;
        }
        if (customMeasure) {
            setMeasuredDimension(newWidth, newHeight);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        refreshImage(false);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle instanceState = new Bundle();
        instanceState.putParcelable(SUPER_STATE_KEY, superState);
        instanceState.putString(PROFILE_ID_KEY, this.profileId);
        instanceState.putInt(PRESET_SIZE_KEY, this.presetSizeType);
        instanceState.putBoolean(IS_CROPPED_KEY, this.isCropped);
        instanceState.putParcelable(BITMAP_KEY, this.imageContents);
        instanceState.putInt(BITMAP_WIDTH_KEY, this.queryWidth);
        instanceState.putInt(BITMAP_HEIGHT_KEY, this.queryHeight);
        instanceState.putBoolean(PENDING_REFRESH_KEY, this.lastRequest != null);
        return instanceState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (state.getClass() != Bundle.class) {
            super.onRestoreInstanceState(state);
            return;
        }
        Bundle instanceState = (Bundle) state;
        super.onRestoreInstanceState(instanceState.getParcelable(SUPER_STATE_KEY));
        this.profileId = instanceState.getString(PROFILE_ID_KEY);
        this.presetSizeType = instanceState.getInt(PRESET_SIZE_KEY);
        this.isCropped = instanceState.getBoolean(IS_CROPPED_KEY);
        this.queryWidth = instanceState.getInt(BITMAP_WIDTH_KEY);
        this.queryHeight = instanceState.getInt(BITMAP_HEIGHT_KEY);
        setImageBitmap((Bitmap) instanceState.getParcelable(BITMAP_KEY));
        if (instanceState.getBoolean(PENDING_REFRESH_KEY)) {
            refreshImage(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.lastRequest = null;
    }

    private void initialize(Context context) {
        removeAllViews();
        this.image = new ImageView(context);
        this.image.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(this.image);
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.com_facebook_profile_picture_view);
        setPresetSize(a.getInt(0, -1));
        this.isCropped = a.getBoolean(1, true);
        a.recycle();
    }

    private void refreshImage(boolean force) {
        boolean changed = updateImageQueryParameters();
        if (this.profileId == null || this.profileId.length() == 0 || (this.queryWidth == 0 && this.queryHeight == 0)) {
            setBlankProfilePicture();
        } else if (changed || force) {
            sendImageRequest(true);
        }
    }

    private void setBlankProfilePicture() {
        int blankImageResource;
        if (isCropped()) {
            blankImageResource = R.drawable.com_facebook_profile_picture_blank_square;
        } else {
            blankImageResource = R.drawable.com_facebook_profile_picture_blank_portrait;
        }
        setImageBitmap(BitmapFactory.decodeResource(getResources(), blankImageResource));
    }

    private void setImageBitmap(Bitmap imageBitmap) {
        if (this.image != null && imageBitmap != null) {
            this.imageContents = imageBitmap;
            this.image.setImageBitmap(imageBitmap);
        }
    }

    private void sendImageRequest(boolean allowCachedResponse) {
        try {
            ImageRequest request = new ImageRequest.Builder(getContext(), ImageRequest.getProfilePictureUrl(this.profileId, this.queryWidth, this.queryHeight)).setAllowCachedRedirects(allowCachedResponse).setCallerTag(this).setCallback(new ImageRequest.Callback() {
                public void onCompleted(ImageResponse response) {
                    ProfilePictureView.this.processResponse(response);
                }
            }).build();
            if (this.lastRequest != null) {
                ImageDownloader.cancelRequest(this.lastRequest);
            }
            this.lastRequest = request;
            ImageDownloader.downloadAsync(request);
        } catch (MalformedURLException e) {
            Logger.log(LoggingBehavior.REQUESTS, 6, TAG, e.toString());
        }
    }

    /* access modifiers changed from: private */
    public void processResponse(ImageResponse response) {
        if (response.getRequest() == this.lastRequest) {
            this.lastRequest = null;
            Bitmap responseImage = response.getBitmap();
            Exception error = response.getError();
            if (error != null) {
                OnErrorListener listener = this.onErrorListener;
                if (listener != null) {
                    listener.onError(new FacebookException("Error in downloading profile picture for profileId: " + getProfileId(), error));
                } else {
                    Logger.log(LoggingBehavior.REQUESTS, 6, TAG, error.toString());
                }
            } else if (responseImage != null) {
                setImageBitmap(responseImage);
                if (response.isCachedRedirect()) {
                    sendImageRequest(false);
                }
            }
        }
    }

    private boolean updateImageQueryParameters() {
        boolean changed = true;
        int newHeightPx = getHeight();
        int newWidthPx = getWidth();
        if (newWidthPx < 1 || newHeightPx < 1) {
            return false;
        }
        int presetSize = getPresetSizeInPixels(false);
        if (presetSize != 0) {
            newWidthPx = presetSize;
            newHeightPx = presetSize;
        }
        if (newWidthPx > newHeightPx) {
            newWidthPx = isCropped() ? newHeightPx : 0;
        } else if (isCropped()) {
            newHeightPx = newWidthPx;
        } else {
            newHeightPx = 0;
        }
        if (newWidthPx == this.queryWidth && newHeightPx == this.queryHeight) {
            changed = false;
        }
        this.queryWidth = newWidthPx;
        this.queryHeight = newHeightPx;
        return changed;
    }

    private int getPresetSizeInPixels(boolean forcePreset) {
        int dimensionId;
        switch (this.presetSizeType) {
            case LARGE /*-4*/:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_large;
                break;
            case NORMAL /*-3*/:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_normal;
                break;
            case -2:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_small;
                break;
            case -1:
                if (forcePreset) {
                    dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_normal;
                    break;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
        return getResources().getDimensionPixelSize(dimensionId);
    }
}
