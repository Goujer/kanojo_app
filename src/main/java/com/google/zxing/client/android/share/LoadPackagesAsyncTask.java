package com.google.zxing.client.android.share;

import android.app.ListActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.R;

final class LoadPackagesAsyncTask extends AsyncTask<Void, Void, List<AppInfo>> {
    private static final String[] PKG_PREFIX_BLACKLIST = {"com.android.", "android", "com.google.android.", "com.htc"};
    private static final String[] PKG_PREFIX_WHITELIST = {"com.google.android.apps."};
    private final ListActivity activity;

    LoadPackagesAsyncTask(ListActivity activity2) {
        this.activity = activity2;
    }

    /* access modifiers changed from: protected */
    public List<AppInfo> doInBackground(Void... objects) {
        List<AppInfo> labelsPackages = new ArrayList<>();
        PackageManager packageManager = this.activity.getPackageManager();
        for (ApplicationInfo appInfo : packageManager.getInstalledApplications(0)) {
            String packageName = appInfo.packageName;
            if (!isHidden(packageName)) {
                CharSequence label = appInfo.loadLabel(packageManager);
                Drawable icon = appInfo.loadIcon(packageManager);
                if (label != null) {
                    labelsPackages.add(new AppInfo(packageName, label.toString(), icon));
                }
            }
        }
        Collections.sort(labelsPackages);
        return labelsPackages;
    }

    private static boolean isHidden(String packageName) {
        if (packageName == null) {
            return true;
        }
        for (String prefix : PKG_PREFIX_WHITELIST) {
            if (packageName.startsWith(prefix)) {
                return false;
            }
        }
        for (String prefix2 : PKG_PREFIX_BLACKLIST) {
            if (packageName.startsWith(prefix2)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(List<AppInfo> results) {
        final List<AppInfo> list = results;
        this.activity.setListAdapter(new ArrayAdapter<AppInfo>(this.activity, R.layout.app_picker_list_item, R.id.app_picker_list_item_label, results) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Drawable icon = ((AppInfo) list.get(position)).getIcon();
                if (icon != null) {
                    ((ImageView) view.findViewById(R.id.app_picker_list_item_icon)).setImageDrawable(icon);
                }
                return view;
            }
        });
    }
}
