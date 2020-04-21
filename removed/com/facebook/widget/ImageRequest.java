package com.facebook.widget;

import android.content.Context;
import android.net.Uri;
import com.facebook.internal.Validate;
import java.net.MalformedURLException;
import java.net.URL;

class ImageRequest {
    private static final String HEIGHT_PARAM = "height";
    private static final String MIGRATION_PARAM = "migration_overrides";
    private static final String MIGRATION_VALUE = "{october_2012:true}";
    private static final String PROFILEPIC_URL_FORMAT = "https://graph.facebook.com/%s/picture";
    static final int UNSPECIFIED_DIMENSION = 0;
    private static final String WIDTH_PARAM = "width";
    private boolean allowCachedRedirects;
    private Callback callback;
    private Object callerTag;
    private Context context;
    private URL imageUrl;

    interface Callback {
        void onCompleted(ImageResponse imageResponse);
    }

    static URL getProfilePictureUrl(String userId, int width, int height) throws MalformedURLException {
        Validate.notNullOrEmpty(userId, "userId");
        int width2 = Math.max(width, 0);
        int height2 = Math.max(height, 0);
        if (width2 == 0 && height2 == 0) {
            throw new IllegalArgumentException("Either width or height must be greater than 0");
        }
        Uri.Builder builder = new Uri.Builder().encodedPath(String.format(PROFILEPIC_URL_FORMAT, new Object[]{userId}));
        if (height2 != 0) {
            builder.appendQueryParameter(HEIGHT_PARAM, String.valueOf(height2));
        }
        if (width2 != 0) {
            builder.appendQueryParameter(WIDTH_PARAM, String.valueOf(width2));
        }
        builder.appendQueryParameter(MIGRATION_PARAM, MIGRATION_VALUE);
        return new URL(builder.toString());
    }

    private ImageRequest(Builder builder) {
        this.context = builder.context;
        this.imageUrl = builder.imageUrl;
        this.callback = builder.callback;
        this.allowCachedRedirects = builder.allowCachedRedirects;
        this.callerTag = builder.callerTag == null ? new Object() : builder.callerTag;
    }

    /* synthetic */ ImageRequest(Builder builder, ImageRequest imageRequest) {
        this(builder);
    }

    /* access modifiers changed from: package-private */
    public Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: package-private */
    public URL getImageUrl() {
        return this.imageUrl;
    }

    /* access modifiers changed from: package-private */
    public Callback getCallback() {
        return this.callback;
    }

    /* access modifiers changed from: package-private */
    public boolean isCachedRedirectAllowed() {
        return this.allowCachedRedirects;
    }

    /* access modifiers changed from: package-private */
    public Object getCallerTag() {
        return this.callerTag;
    }

    static class Builder {
        /* access modifiers changed from: private */
        public boolean allowCachedRedirects;
        /* access modifiers changed from: private */
        public Callback callback;
        /* access modifiers changed from: private */
        public Object callerTag;
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public URL imageUrl;

        Builder(Context context2, URL imageUrl2) {
            Validate.notNull(imageUrl2, "imageUrl");
            this.context = context2;
            this.imageUrl = imageUrl2;
        }

        /* access modifiers changed from: package-private */
        public Builder setCallback(Callback callback2) {
            this.callback = callback2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setCallerTag(Object callerTag2) {
            this.callerTag = callerTag2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setAllowCachedRedirects(boolean allowCachedRedirects2) {
            this.allowCachedRedirects = allowCachedRedirects2;
            return this;
        }

        /* access modifiers changed from: package-private */
        public ImageRequest build() {
            return new ImageRequest(this, (ImageRequest) null);
        }
    }
}
