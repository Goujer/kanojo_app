package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class KanojoInfoImgAdapter extends BaseAdapter implements ObservableAdapter {

    private static final String TAG = "KanojoInfoImgAdapter";
    private Context context;
    private int galleryIconSize;
    private List<String> imgUrls;
    private Handler mHandler;
    private final Runnable mNotifyThread = new Runnable() {
        public void run() {
            KanojoInfoImgAdapter.this.notifyDataSetChanged();
        }
    };
    private RemoteResourceManagerObserver mResourcesObserver;
    private RemoteResourceManager mRrm;

    public KanojoInfoImgAdapter(Context context2, RemoteResourceManager rrm) {
        this.context = context2;
        context2.obtainStyledAttributes(R.styleable.KanojoInfoGallery).recycle();
        this.imgUrls = new ArrayList();
        this.galleryIconSize = (int) context2.getResources().getDimension(R.dimen.kanojo_info_gallery_height);
        this.mHandler = new Handler();
        this.mRrm = rrm;
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, (RemoteResourceManagerObserver) null);
        this.mRrm.addObserver(this.mResourcesObserver);
    }

    public void setImgUrls(List<String> l) {
        this.imgUrls = l;
        for (String url : l) {
            ImageCache.requestImage(url, this.mRrm);
        }
        notifyDataSetChanged();
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.postDelayed(this.mNotifyThread, 200);
    }

    public void addImgUrl(String l) {
        if (this.imgUrls == null) {
            this.imgUrls = new ArrayList();
        }
        this.imgUrls.add(l);
        notifyDataSetChanged();
        ImageCache.requestImage(l, this.mRrm);
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.postDelayed(this.mNotifyThread, 200);
    }

    public void clear() {
        this.imgUrls.clear();
    }

    public int getCount() {
        if (this.imgUrls == null) {
            return 0;
        }
        return this.imgUrls.size();
    }

    public Object getItem(int position) {
        return this.imgUrls.get(position);
    }

    public boolean hasStableIds() {
        return true;
    }

    public String getItemUrl(int position) {
        return this.imgUrls.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(this.context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new Gallery.LayoutParams(this.galleryIconSize, this.galleryIconSize));
        } else {
            imageView = (ImageView) view;
        }
        ImageCache.setImage(imageView, this.imgUrls.get(position), this.mRrm, R.drawable.common_noimage_product);
        return imageView;
    }

    public void removeObserver() {
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mRrm.deleteObserver(this.mResourcesObserver);
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(KanojoInfoImgAdapter kanojoInfoImgAdapter, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
            if (data != null) {
                KanojoInfoImgAdapter.this.mHandler.removeCallbacks(KanojoInfoImgAdapter.this.mNotifyThread);
                KanojoInfoImgAdapter.this.mHandler.postDelayed(KanojoInfoImgAdapter.this.mNotifyThread, 300);
            }
        }
    }
}
