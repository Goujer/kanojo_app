package net.nend.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import java.util.Locale;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import net.nend.android.NendConstants;

final class NendAdRequest {
    static final /* synthetic */ boolean $assertionsDisabled = (!NendAdRequest.class.desiredAssertionStatus());
    private final String mApiKey;
    private final String mDomain;
    private final String mPath;
    private final String mProtocol;
    private final int mSpotId;

    NendAdRequest(Context context, int spotId, String apiKey) {
        if (context == null) {
            throw new NullPointerException("Context is null.");
        } else if (spotId <= 0) {
            throw new IllegalArgumentException("Spot id is invalid. spot id : " + spotId);
        } else if (apiKey == null || apiKey.length() == 0) {
            throw new IllegalArgumentException("Api key is invalid. api key : " + apiKey);
        } else {
            this.mSpotId = spotId;
            this.mApiKey = apiKey;
            String protocol = "http";
            String domain = "ad1.nend.net";
            String path = "na.php";
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (applicationInfo.metaData != null) {
                    protocol = applicationInfo.metaData.getString(NendConstants.MetaData.ADSCHEME.getName()) != null ? applicationInfo.metaData.getString(NendConstants.MetaData.ADSCHEME.getName()) : protocol;
                    domain = applicationInfo.metaData.getString(NendConstants.MetaData.ADAUTHORITY.getName()) != null ? applicationInfo.metaData.getString(NendConstants.MetaData.ADAUTHORITY.getName()) : domain;
                    if (applicationInfo.metaData.getString(NendConstants.MetaData.ADPATH.getName()) != null) {
                        path = applicationInfo.metaData.getString(NendConstants.MetaData.ADPATH.getName());
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_UNEXPECTED, (Throwable) e);
            } finally {
                this.mProtocol = protocol;
                this.mDomain = domain;
                this.mPath = path;
            }
        }
    }

    private String getOS() {
        return "android";
    }

    private String getSDKVersion() {
        return "2.2.2";
    }

    private String getModel() {
        return Build.MODEL;
    }

    private String getDevice() {
        return Build.DEVICE;
    }

    private String getLocale() {
        return Locale.getDefault().toString();
    }

    private String getVersion() {
        return Build.VERSION.RELEASE;
    }

    /* access modifiers changed from: package-private */
    public String getRequestUrl(String uid) {
        if (uid != null && uid.length() != 0) {
            return new Uri.Builder().scheme(this.mProtocol).authority(this.mDomain).path(this.mPath).appendQueryParameter("apikey", this.mApiKey).appendQueryParameter("spot", String.valueOf(this.mSpotId)).appendQueryParameter("uid", uid).appendQueryParameter("os", getOS()).appendQueryParameter("version", getVersion()).appendQueryParameter("model", getModel()).appendQueryParameter("device", getDevice()).appendQueryParameter("localize", getLocale()).appendQueryParameter("sdkver", getSDKVersion()).appendQueryParameter("type", GreeDefs.PRODUCT_NAME_TEXTID).toString();
        }
        throw new IllegalArgumentException("UID is invalid. uid : " + uid);
    }
}
