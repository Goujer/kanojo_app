package net.nend.android;

import android.content.Context;
import android.util.DisplayMetrics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.UUID;
import net.nend.android.AdParameter;
import net.nend.android.DownloadTask;
import net.nend.android.NendAdView;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

final class NendAd implements Ad, DownloadTask.Downloadable<AdParameter> {
    private static /* synthetic */ int[] $SWITCH_TABLE$net$nend$android$AdParameter$ViewType = null;
    static final /* synthetic */ boolean $assertionsDisabled = (!NendAd.class.desiredAssertionStatus());
    private static final String NEND_UID_FILE_NAME = "NENDUUID";
    private String mClickUrl = null;
    private final Context mContext;
    private int mHeight = 50;
    private String mImageUrl = null;
    private WeakReference<AdListener> mListenerReference = null;
    private DisplayMetrics mMetrics;
    private int mReloadIntervalInSeconds = 60;
    private final NendAdRequest mRequest;
    private DownloadTask<AdParameter> mTask = null;
    private final String mUid;
    private AdParameter.ViewType mViewType = AdParameter.ViewType.NONE;
    private String mWebViewUrl = null;
    private int mWidth = 320;

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

    NendAd(Context context, int spotId, String apiKey, DisplayMetrics metrics) {
        if (context == null) {
            throw new NullPointerException("Context is null.");
        } else if (spotId <= 0) {
            throw new IllegalArgumentException("Spot id is invalid. spot id : " + spotId);
        } else if (apiKey == null || apiKey.length() == 0) {
            throw new IllegalArgumentException("Api key is invalid. api key : " + apiKey);
        } else {
            this.mContext = context;
            this.mMetrics = metrics;
            this.mRequest = new NendAdRequest(context, spotId, apiKey);
            this.mUid = makeUid(context);
        }
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

    private void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        if (reloadIntervalInSeconds > 99999) {
            this.mReloadIntervalInSeconds = 99999;
        } else if (NendHelper.isDev() || reloadIntervalInSeconds > 30) {
            this.mReloadIntervalInSeconds = reloadIntervalInSeconds;
        } else {
            this.mReloadIntervalInSeconds = 30;
        }
    }

    public int getReloadIntervalInSeconds() {
        return this.mReloadIntervalInSeconds;
    }

    public String getUid() {
        return this.mUid;
    }

    public boolean isRequestable() {
        return this.mTask == null || this.mTask.isFinished();
    }

    public boolean requestAd() {
        if (!isRequestable()) {
            return false;
        }
        this.mTask = new DownloadTask<>(this);
        this.mTask.execute(new Void[0]);
        return true;
    }

    public void cancelRequest() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
    }

    public void setListener(AdListener listener) {
        this.mListenerReference = new WeakReference<>(listener);
    }

    public AdListener getListener() {
        if (this.mListenerReference != null) {
            return (AdListener) this.mListenerReference.get();
        }
        return null;
    }

    public void removeListener() {
        this.mListenerReference = null;
    }

    public String getRequestUrl() {
        return this.mRequest.getRequestUrl(this.mUid);
    }

    public AdParameter makeResponse(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        try {
            return new NendAdResponseParser(this.mContext).parseResponse(EntityUtils.toString(entity));
        } catch (ParseException e) {
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

    public void onDownload(AdParameter response) {
        boolean isEnableDisplay = true;
        AdListener listener = getListener();
        if (response != null) {
            float density = this.mMetrics.density;
            float width = ((float) response.getWidth()) * density;
            float height = ((float) response.getHeight()) * density;
            if (width / 2.0f > ((float) this.mMetrics.widthPixels) || height / 2.0f > ((float) this.mMetrics.heightPixels) || width > ((float) this.mMetrics.widthPixels) || height > ((float) this.mMetrics.heightPixels)) {
                isEnableDisplay = false;
                listener.onFailedToReceiveAd(NendAdView.NendError.AD_SIZE_TOO_LARGE);
            }
            switch ($SWITCH_TABLE$net$nend$android$AdParameter$ViewType()[response.getViewType().ordinal()]) {
                case 2:
                    setAdViewParam(response);
                    break;
                case 3:
                    setWebViewParam(response);
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    } else if (listener != null) {
                        listener.onFailedToReceiveAd(NendAdView.NendError.INVALID_RESPONSE_TYPE);
                        return;
                    } else {
                        return;
                    }
            }
            if (listener != null && isEnableDisplay) {
                listener.onReceiveAd();
            }
        } else if (listener != null) {
            listener.onFailedToReceiveAd(NendAdView.NendError.FAILED_AD_REQUEST);
        }
    }

    private void setAdViewParam(AdParameter response) {
        if ($assertionsDisabled || response != null) {
            this.mViewType = AdParameter.ViewType.ADVIEW;
            setReloadIntervalInSeconds(response.getReloadIntervalInSeconds());
            this.mImageUrl = response.getImageUrl();
            this.mClickUrl = response.getClickUrl();
            this.mHeight = response.getHeight();
            this.mWidth = response.getWidth();
            this.mWebViewUrl = null;
            return;
        }
        throw new AssertionError();
    }

    private void setWebViewParam(AdParameter response) {
        if ($assertionsDisabled || response != null) {
            this.mViewType = AdParameter.ViewType.WEBVIEW;
            this.mWebViewUrl = response.getWebViewUrl();
            this.mImageUrl = null;
            this.mClickUrl = null;
            this.mHeight = response.getHeight();
            this.mWidth = response.getWidth();
            return;
        }
        throw new AssertionError();
    }

    private String makeUid(Context context) {
        if ($assertionsDisabled || context != null) {
            String uid = NendHelper.md5String(UUID.randomUUID().toString());
            if (!new File(context.getFilesDir(), NEND_UID_FILE_NAME).exists()) {
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(NEND_UID_FILE_NAME, 0);
                    fileOutputStream.write(uid.getBytes());
                    fileOutputStream.close();
                    return uid;
                } catch (Exception e) {
                    return uid;
                }
            } else {
                try {
                    return new BufferedReader(new InputStreamReader(context.openFileInput(NEND_UID_FILE_NAME))).readLine();
                } catch (Exception e2) {
                    return uid;
                }
            }
        } else {
            throw new AssertionError();
        }
    }
}
