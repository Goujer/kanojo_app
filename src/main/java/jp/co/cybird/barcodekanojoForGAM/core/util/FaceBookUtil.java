package jp.co.cybird.barcodekanojoForGAM.core.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import java.io.IOException;
import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.facebook.BaseRequestListener;
import jp.co.cybird.barcodekanojoForGAM.core.model.FaceBookProfile;
import jp.co.cybird.barcodekanojoForGAM.core.parser.FaceBookProfileParser;
import jp.co.cybird.barcodekanojoForGAM.core.util.BaseDiskCache;
import jp.co.cybird.barcodekanojoForGAM.preferences.ApplicationSetting;
import org.json.JSONException;
import org.json.JSONObject;

public class FaceBookUtil {
    public static final int AUTHORIZE_ACTIVITY_RESULT_CODE = -100;
    public static Facebook mFb;
    public static int mWaitCounter;
    /* access modifiers changed from: private */
    public FaceBookProfile fbHolder;
    /* access modifiers changed from: private */
    public Activity mActivity;
    private int mActivityCode;
    private AsyncFacebookRunner mAsyncRunner;
    private Context mContext;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public OnFaceBoookListener mListener;
    private String[] mPermissions;
    RemoteResourceManager mRrm;
    private BaseDiskCache.BaseDiskCallBack mSaveListener;
    private String[] permissions = {"publish_stream", "user_location", "user_birthday", "publish_checkins"};
    private Thread t;

    public interface OnFaceBoookListener {
        void onCancel(String str);

        void onFail(String str, int i, String str2);

        void onGetProfileFail(FaceBookProfile faceBookProfile);

        void onGetProfileSuccess(FaceBookProfile faceBookProfile);

        void onLoginSuccess();

        void onLogoutSuccess();
    }

    public FaceBookUtil(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        this.mActivityCode = -100;
        this.mPermissions = this.permissions;
        this.mHandler = new Handler();
        mFb = new Facebook(Defs.USER_FACEBOK_APP_ID());
        this.mAsyncRunner = new AsyncFacebookRunner(mFb);
        this.fbHolder = new FaceBookProfile();
        mWaitCounter = 0;
        restore();
    }

    public void setListener(OnFaceBoookListener listener) {
        this.mListener = listener;
    }

    public void login(boolean mForceLogin) {
        try {
            if (!mFb.isSessionValid() || mForceLogin) {
                Log.d("NguyenTT", "token: " + mFb.getAccessToken());
                mFb.authorize(this.mActivity, this.mPermissions, this.mActivityCode, new LoginDialogListener(this, (LoginDialogListener) null));
                return;
            }
            new AsyncFacebookRunner(mFb).logout(this.mActivity.getApplicationContext(), new LogoutRequestListener(this, (LogoutRequestListener) null));
        } catch (Exception e) {
            if (this.mListener != null) {
                this.mListener.onFail("Error network", -2222, "network");
            }
        }
    }

    public void getProfile() {
        if (!mFb.isSessionValid()) {
            mFb.authorize(this.mActivity, this.mPermissions, this.mActivityCode, new LoginDialogListener(this, (LoginDialogListener) null));
            return;
        }
        try {
            Bundle params = new Bundle();
            params.putString("fields", "name, picture,gender,birthday");
            this.mAsyncRunner.request("me", params, (AsyncFacebookRunner.RequestListener) new UserRequestListener());
        } catch (FacebookError e) {
            if (this.mListener != null) {
                this.mListener.onGetProfileFail((FaceBookProfile) null);
            }
        } catch (Exception e2) {
            if (this.mListener != null) {
                this.mListener.onGetProfileFail((FaceBookProfile) null);
            }
        }
    }

    private final class LoginDialogListener implements Facebook.DialogListener {
        private LoginDialogListener() {
        }

        /* synthetic */ LoginDialogListener(FaceBookUtil faceBookUtil, LoginDialogListener loginDialogListener) {
            this();
        }

        public void onComplete(Bundle values) {
            ApplicationSetting setting = new ApplicationSetting(FaceBookUtil.this.mActivity.getApplicationContext());
            setting.commitFaceBookToken(FaceBookUtil.mFb.getAccessToken());
            setting.commitFacebookExpired(Long.valueOf(FaceBookUtil.mFb.getAccessExpires()));
            setting.commitFacebookLastUpdate(Long.valueOf(FaceBookUtil.mFb.getLastAccessUpdate()));
            FaceBookUtil.this.getProfile();
            Log.d("NguyenTT", "facebook token: " + FaceBookUtil.mFb.getAccessToken());
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onLoginSuccess();
            }
        }

        public void onFacebookError(FacebookError error) {
            Log.d("NguyenTT", "onFacebookError: " + error.getMessage());
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onFail(error.getMessage(), error.getErrorCode(), error.getErrorType());
            }
        }

        public void onError(DialogError error) {
            Log.d("NguyenTT", "error: " + error.getMessage());
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onFail(error.getMessage(), error.getErrorCode(), error.getFailingUrl());
            }
        }

        public void onCancel() {
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onCancel("user has cancel");
            }
        }
    }

    private class LogoutRequestListener extends BaseRequestListener {
        private LogoutRequestListener() {
        }

        /* synthetic */ LogoutRequestListener(FaceBookUtil faceBookUtil, LogoutRequestListener logoutRequestListener) {
            this();
        }

        public void onComplete(String response, Object state) {
            Log.d("NguyenTT", "LogoutRequestListener: " + response);
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onLogoutSuccess();
            }
        }

        public void onFacebookError(FacebookError e, Object state) {
            Log.d("NguyenTT", "LogoutRequestListener: " + e.getMessage());
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onLogoutSuccess();
            }
        }

        public void onIOException(IOException e, Object state) {
            Log.d("NguyenTT", "onIOException: " + e.getMessage());
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onFail("", -200, "");
            }
        }
    }

    public class UserRequestListener extends BaseRequestListener {
        public UserRequestListener() {
        }

        public void onComplete(String response, Object state) {
            Log.d("NguyenTT", "UserRequestListener response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                FaceBookUtil.this.fbHolder = (FaceBookProfile) new FaceBookProfileParser().parse(jsonObject);
                new ApplicationSetting(FaceBookUtil.this.mActivity.getApplicationContext()).commitFacebookID(FaceBookUtil.this.fbHolder.getId());
                if (FaceBookUtil.this.mListener != null) {
                    FaceBookUtil.this.mListener.onGetProfileSuccess(FaceBookUtil.this.fbHolder);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (FaceBookUtil.this.mListener != null) {
                    FaceBookUtil.this.mListener.onGetProfileFail(FaceBookUtil.this.fbHolder);
                }
            } catch (BarcodeKanojoException e2) {
                if (FaceBookUtil.this.fbHolder.getMessage() == null) {
                    FaceBookUtil.this.fbHolder.setMessage(e2.getMessage());
                }
                if (FaceBookUtil.this.mListener != null) {
                    FaceBookUtil.this.mListener.onGetProfileFail(FaceBookUtil.this.fbHolder);
                }
            } catch (Exception e3) {
                Log.d("NguyenTT", e3.getMessage());
            }
        }

        public void onIOException(IOException e, Object state) {
            e.printStackTrace();
            if (FaceBookUtil.this.mListener != null) {
                FaceBookUtil.this.mListener.onFail(e.getMessage(), 100, e.getClass().getName());
            }
        }
    }

    public void restore() {
        ApplicationSetting setting = new ApplicationSetting(this.mActivity.getApplicationContext());
        mFb.setAccessToken(setting.getFaceBookToken());
        mFb.setAccessExpires(setting.getFaceBookExpired().longValue());
    }

    public void save() {
        ApplicationSetting setting = new ApplicationSetting(this.mActivity.getApplicationContext());
        setting.commitFaceBookToken(mFb.getAccessToken());
        setting.commitFacebookExpired(Long.valueOf(mFb.getAccessExpires()));
        setting.commitFacebookLastUpdate(Long.valueOf(mFb.getLastAccessUpdate()));
        setting.commitFacebookID(new StringBuilder(String.valueOf(this.fbHolder.getId())).toString());
        this.fbHolder.setPhoto(this.mRrm.getFile(Uri.parse(this.fbHolder.getUrl())));
    }

    public void clear() {
        mFb.setAccessToken((String) null);
        mFb.setAccessExpires(System.currentTimeMillis() - 1000);
    }

    public void logout() {
        CookieSyncManager.createInstance(this.mContext);
        CookieManager.getInstance().removeSessionCookie();
    }
}
