package jp.co.cybird.app.android.lib.applauncher;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import jp.co.cybird.app.android.lib.commons.file.DownloadHelper;
import jp.co.cybird.app.android.lib.commons.file.FileUtil;
import jp.co.cybird.app.android.lib.commons.log.DLog;
import jp.co.cybird.app.android.lib.commons.misc.PackageUtil;
import org.apache.http.HttpException;

public class AsyncLoadIconTask extends AsyncTask<String, Void, Drawable> {
    private Context mContext;
    private Drawable mDownloadImage;
    private HashMap<String, Drawable> mIconMap;
    private HashMap<String, Boolean> mInstalled;
    private boolean mIsInstalled;
    private Drawable mLoading;
	private Drawable mRunImage;
    private String mTag;
    private TextView mTextView;

    AsyncLoadIconTask(Context context, TextView textView, Drawable downloadImage, Drawable runImage, HashMap<String, Drawable> iconMap, HashMap<String, Boolean> installed, Drawable loading) {
        this.mTextView = textView;
        this.mContext = context;
        this.mTag = textView.getTag(ParamLoader.getResourceIdForType("lib_launcher_row_item", "id", this.mContext)).toString();
        this.mDownloadImage = downloadImage;
        this.mRunImage = runImage;
        this.mIconMap = iconMap;
        this.mInstalled = installed;
        this.mLoading = loading;
    }

    @Override
    protected Drawable doInBackground(String... params) {
		String mPackageName = params[0];
        String iconFileUrl = params[1];
        this.mIsInstalled = PackageUtil.isInstalled(this.mContext, mPackageName);
        this.mInstalled.put(mPackageName, this.mIsInstalled);
        DLog.i(AppLauncherConsts.TAG, "iconFileUrl: " + iconFileUrl);
        if (iconFileUrl == null) {
            return null;
        }
        String[] iconUrlSep = iconFileUrl.split("/");
        String iconFileName = iconUrlSep[iconUrlSep.length - 1];
        DLog.i(AppLauncherConsts.TAG, "iconFileName: " + iconFileName);
        StringBuilder filePath = new StringBuilder();
        filePath.append(FileUtil.getExternalStorageDataDirPath());
        filePath.append(File.separator);
        filePath.append(AppLauncherConsts.APPLAUNCHER_ICON_DIRECTORY);
        filePath.append(File.separator);
        filePath.append(iconFileName);
        DLog.d(AppLauncherConsts.TAG, "Icon file path:".concat(filePath.toString()));
        if (!new File(filePath.toString()).exists()) {
            DownloadHelper helper = new DownloadHelper(this.mContext);
            helper.setSaveDir(DownloadHelper.SaveDir.SAVE_DIR_EXTERNAL);
            try {
                DLog.i(AppLauncherConsts.TAG, "iconFileUrl: " + iconFileUrl);
                helper.setUrl(iconFileUrl);
                helper.setFilePath(filePath.toString());
                helper.addRequestProperty(DownloadHelper.REQUEST_HEADER_USERAGENT, AppLauncherConsts.getUserAgent());
                helper.setUserAgent(AppLauncherConsts.getUserAgent());
                helper.download();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (HttpException e3) {
                e3.printStackTrace();
            }
        }
        DLog.i(AppLauncherConsts.TAG, "filePath.toString(): " + filePath.toString());
        Drawable iconDrawable = null;
        if (new File(filePath.toString()).exists()) {
            iconDrawable = BitmapDrawable.createFromPath(filePath.toString());
            if (iconDrawable != null) {
                iconDrawable.setBounds(0, 0, this.mLoading.getIntrinsicWidth(), this.mLoading.getIntrinsicHeight());
            } else {
                DLog.e(AppLauncherConsts.TAG, "iconDrawable is null ");
            }
        }
        if (iconDrawable == null) {
            return iconDrawable;
        }
        this.mIconMap.put(mPackageName, iconDrawable);
        return iconDrawable;
    }

    @Override
    protected void onPostExecute(Drawable iconDrawable) {
        if (!this.mTag.equals(this.mTextView.getTag(ParamLoader.getResourceIdForType("lib_launcher_row_item", "id", this.mContext)).toString())) {
            return;
        }
        if (iconDrawable != null) {
            this.mTextView.setCompoundDrawables(iconDrawable, null, this.mIsInstalled ? this.mRunImage : this.mDownloadImage, null);
        } else {
            this.mTextView.setCompoundDrawables(this.mLoading, null, this.mIsInstalled ? this.mRunImage : this.mDownloadImage, null);
        }
    }
}
