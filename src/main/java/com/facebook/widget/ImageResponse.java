package com.facebook.widget;

import android.graphics.Bitmap;

class ImageResponse {
    private Bitmap bitmap;
    private Exception error;
    private boolean isCachedRedirect;
    private ImageRequest request;

    ImageResponse(ImageRequest request2, Exception error2, boolean isCachedRedirect2, Bitmap bitmap2) {
        this.request = request2;
        this.error = error2;
        this.bitmap = bitmap2;
        this.isCachedRedirect = isCachedRedirect2;
    }

    /* access modifiers changed from: package-private */
    public ImageRequest getRequest() {
        return this.request;
    }

    /* access modifiers changed from: package-private */
    public Exception getError() {
        return this.error;
    }

    /* access modifiers changed from: package-private */
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    /* access modifiers changed from: package-private */
    public boolean isCachedRedirect() {
        return this.isCachedRedirect;
    }
}
