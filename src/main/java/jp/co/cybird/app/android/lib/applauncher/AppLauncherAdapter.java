package jp.co.cybird.app.android.lib.applauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class AppLauncherAdapter extends BaseAdapter {
    private HashMap<String, Boolean> mAppInstalled = new HashMap<>();
    private List<Scheme> mApplist;
    private Context mContext;
    private Drawable mDownloadImage;
    private HashMap<String, Drawable> mIconCache = new HashMap<>();
    private LayoutInflater mIflater;
    private Drawable mLoading;
    private Drawable mRunImage;

    public AppLauncherAdapter(Context context, List<Scheme> applist) {
        this.mContext = context;
        this.mApplist = applist;
        loadImages();
    }

    private void loadImages() {
        if (this.mLoading == null) {
            this.mLoading = this.mContext.getResources().getDrawable(ParamLoader.getResourceIdForType("lib_launcher_loading", "drawable", this.mContext));
            this.mLoading.setBounds(0, 0, this.mLoading.getIntrinsicWidth(), this.mLoading.getIntrinsicHeight());
        }
        if (this.mDownloadImage == null) {
            this.mDownloadImage = this.mContext.getResources().getDrawable(ParamLoader.getResourceIdForType("lib_launcher_button_download", "drawable", this.mContext));
            this.mDownloadImage.setBounds(0, 0, this.mDownloadImage.getIntrinsicWidth(), this.mLoading.getIntrinsicHeight());
        }
        if (this.mRunImage == null) {
            this.mRunImage = this.mContext.getResources().getDrawable(ParamLoader.getResourceIdForType("lib_launcher_button_run", "drawable", this.mContext));
            this.mRunImage.setBounds(0, 0, this.mRunImage.getIntrinsicWidth(), this.mLoading.getIntrinsicHeight());
        }
    }

    public int getCount() {
        if (this.mApplist == null) {
            return 0;
        }
        return this.mApplist.size();
    }

    public Scheme getItem(int position) {
        if (this.mApplist == null) {
            return null;
        }
        return this.mApplist.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            if (this.mIflater == null) {
                this.mIflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
            }
            convertView = this.mIflater.inflate(ParamLoader.getResourceIdForType("lib_launcher_row", "layout", this.mContext), (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.textview = (TextView) convertView.findViewById(ParamLoader.getResourceIdForType("lib_launcher_row_item", "id", this.mContext));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Scheme item = getItem(position);
        viewHolder.textview.setText(item.getTitle());
        viewHolder.textview.setTag(ParamLoader.getResourceIdForType("lib_launcher_row_item", "id", this.mContext), item.getProduct_id());
        if (this.mIconCache.containsKey(item.getProduct_id())) {
            viewHolder.textview.setCompoundDrawables(this.mIconCache.get(item.getProduct_id()), (Drawable) null, this.mAppInstalled == null ? null : this.mAppInstalled.get(item.getProduct_id()).booleanValue() ? this.mRunImage : this.mDownloadImage, (Drawable) null);
        } else if (this.mAppInstalled.containsKey(item.getProduct_id())) {
            viewHolder.textview.setCompoundDrawables(this.mLoading, (Drawable) null, this.mAppInstalled == null ? null : this.mAppInstalled.get(item.getProduct_id()).booleanValue() ? this.mRunImage : this.mDownloadImage, (Drawable) null);
            loadIcon(viewHolder, item);
        } else {
            viewHolder.textview.setCompoundDrawables(this.mLoading, (Drawable) null, (Drawable) null, (Drawable) null);
            loadIcon(viewHolder, item);
        }
        return convertView;
    }

    private void loadIcon(ViewHolder viewHolder, Scheme item) {
        new AsyncLoadIconTask(this.mContext, viewHolder.textview, this.mDownloadImage, this.mRunImage, this.mIconCache, this.mAppInstalled, this.mLoading).execute(new String[]{item.getProduct_id(), item.getIconUrl()});
    }

    static class ViewHolder {
        TextView textview;

        ViewHolder() {
        }
    }
}
