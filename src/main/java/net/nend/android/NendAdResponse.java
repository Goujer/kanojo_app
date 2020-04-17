package net.nend.android;

import net.nend.android.AdParameter;

final class NendAdResponse implements AdParameter {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType;
    private final String mClickUrl;
    private final int mHeight;
    private final String mImageUrl;
    private final int mReloadIntervalInSeconds;
    private final AdParameter.ViewType mViewType;
    private final String mWebViewUrl;
    private final int mWidth;

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

    static final class Builder {
        static final /* synthetic */ boolean $assertionsDisabled = (!NendAdResponse.class.desiredAssertionStatus());
        /* access modifiers changed from: private */
        public String mClickUrl;
        /* access modifiers changed from: private */
        public int mHeight;
        /* access modifiers changed from: private */
        public String mImageUrl;
        /* access modifiers changed from: private */
        public int mReloadIntervalInSeconds;
        /* access modifiers changed from: private */
        public AdParameter.ViewType mViewType = AdParameter.ViewType.NONE;
        /* access modifiers changed from: private */
        public String mWebViewUrl;
        /* access modifiers changed from: private */
        public int mWidth;

        Builder() {
        }

        /* access modifiers changed from: package-private */
        public Builder setViewType(AdParameter.ViewType viewType) {
            if ($assertionsDisabled || viewType != null) {
                this.mViewType = viewType;
                return this;
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: package-private */
        public Builder setImageUrl(String imageUrl) {
            if (imageUrl != null) {
                this.mImageUrl = imageUrl.replaceAll(" ", "%20");
            } else {
                this.mImageUrl = null;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setClickUrl(String clickUrl) {
            if (clickUrl != null) {
                this.mClickUrl = clickUrl.replaceAll(" ", "%20");
            } else {
                this.mClickUrl = null;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setWebViewUrl(String webViewUrl) {
            if (webViewUrl != null) {
                this.mWebViewUrl = webViewUrl.replaceAll(" ", "%20");
            } else {
                this.mWebViewUrl = null;
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
            this.mReloadIntervalInSeconds = reloadIntervalInSeconds;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setHeight(int height) {
            this.mHeight = height;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setWidth(int width) {
            this.mWidth = width;
            return this;
        }

        /* access modifiers changed from: package-private */
        public NendAdResponse build() {
            return new NendAdResponse(this, (NendAdResponse) null);
        }
    }

    private NendAdResponse(Builder builder) {
        switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[builder.mViewType.ordinal()]) {
            case 2:
                if (builder.mImageUrl == null || builder.mImageUrl.length() == 0) {
                    throw new IllegalArgumentException("Image url is invalid.");
                } else if (builder.mClickUrl == null || builder.mClickUrl.length() == 0) {
                    throw new IllegalArgumentException("Click url is invalid");
                } else {
                    this.mViewType = AdParameter.ViewType.ADVIEW;
                    this.mImageUrl = builder.mImageUrl;
                    this.mClickUrl = builder.mClickUrl;
                    this.mWebViewUrl = null;
                    this.mReloadIntervalInSeconds = builder.mReloadIntervalInSeconds;
                    this.mHeight = builder.mHeight;
                    this.mWidth = builder.mWidth;
                    return;
                }
            case 3:
                if (builder.mWebViewUrl == null || builder.mWebViewUrl.length() == 0) {
                    throw new IllegalArgumentException("Web view url is invalid");
                }
                this.mViewType = AdParameter.ViewType.WEBVIEW;
                this.mImageUrl = null;
                this.mClickUrl = null;
                this.mWebViewUrl = builder.mWebViewUrl;
                this.mReloadIntervalInSeconds = 0;
                this.mHeight = builder.mHeight;
                this.mWidth = builder.mWidth;
                return;
            default:
                throw new IllegalArgumentException("Uknown view type.");
        }
    }

    /* synthetic */ NendAdResponse(Builder builder, NendAdResponse nendAdResponse) {
        this(builder);
    }

    public AdParameter.ViewType getViewType() {
        return this.mViewType;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public String getClickUrl() {
        return this.mClickUrl;
    }

    public String getWebViewUrl() {
        return this.mWebViewUrl;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getReloadIntervalInSeconds() {
        return this.mReloadIntervalInSeconds;
    }
}
