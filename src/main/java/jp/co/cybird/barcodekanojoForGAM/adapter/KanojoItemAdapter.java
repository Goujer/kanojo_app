package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goujer.barcodekanojo.core.util.DynamicImageCache;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseKanojoItemAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;

public class KanojoItemAdapter extends BaseKanojoItemAdapter implements ObservableAdapter {

    public static final int MODE_EXTEND_DATE = 4;
    public static final int MODE_EXTEND_GIFT = 5;
    private static final String TAG = "KanojoAdapter";
    private Handler mHandler;
    private LayoutInflater mInflater;
    private int mLoadedPhotoIndex = 0;
    private final Runnable mNotifyThread = new Runnable() {
        public void run() {
            KanojoItemAdapter.this.notifyDataSetChanged();
        }
    };
    private RemoteResourceManagerObserver mResourcesObserver;
    private DynamicImageCache mDic;
    private Runnable mRunnableLoadPhotos = new Runnable() {
        public void run() {
            if (KanojoItemAdapter.this.mLoadedPhotoIndex < KanojoItemAdapter.this.getCount()) {
                KanojoItemAdapter kanojoItemAdapter = KanojoItemAdapter.this;
                KanojoItemAdapter kanojoItemAdapter2 = KanojoItemAdapter.this;
                int access$0 = kanojoItemAdapter2.mLoadedPhotoIndex;
                kanojoItemAdapter2.mLoadedPhotoIndex = access$0 + 1;
                ActivityModel a = (ActivityModel) kanojoItemAdapter.getItem(access$0);
				mDic.requestBitmap(a.getLeftImgUrl());
				mDic.requestBitmap(a.getRightImgUrl());
                KanojoItemAdapter.this.mHandler.postDelayed(KanojoItemAdapter.this.mRunnableLoadPhotos, 200);
            }
        }
    };
    private int mode;
    private int userLevel;

    public KanojoItemAdapter(Context context, DynamicImageCache dic) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mDic = dic;
        this.mHandler = new Handler();
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, (RemoteResourceManagerObserver) null);
        this.mDic.addObserver(this.mResourcesObserver);
    }

    public void setUserLevel(int userLevel2) {
        this.userLevel = userLevel2;
    }

    public void setMode(int mode2) {
        this.mode = mode2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.row_kanojo_items, null);
            holder = new ViewHolder(null);
            holder.img = (ImageView) view.findViewById(R.id.row_kanojo_items_img);
            holder.title = (TextView) view.findViewById(R.id.row_kanojo_items_title);
            holder.description = (TextView) view.findViewById(R.id.row_kanojo_items_description);
            holder.layoutUplevel = (LinearLayout) view.findViewById(R.id.layout_hide_up_level);
            holder.textViewPurchasableLevel = (TextView) view.findViewById(R.id.textViewPurchasableLevel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        KanojoItem item = (KanojoItem) getItem(position);
        holder.title.setText(item.getTitle());
        if (item.isCategory()) {
            holder.description.setText(item.getDescription());
        } else if (item.isHas()) {
            holder.description.setText(item.getHas_units());
        } else {
            holder.description.setText(item.getPrice());
        }
        if (this.mode != 4 && this.mode != 5) {
            holder.layoutUplevel.setVisibility(View.GONE);
        } else if (item.getPurchasable_level() == null) {
            holder.layoutUplevel.setVisibility(View.GONE);
        } else if (Integer.parseInt(item.getPurchasable_level()) > this.userLevel) {
            holder.layoutUplevel.setVisibility(View.VISIBLE);
            holder.textViewPurchasableLevel.setText("Level " + item.getPurchasable_level());
        } else {
            holder.layoutUplevel.setVisibility(View.GONE);
        }
        mDic.loadBitmapASync(holder.img, item.getImage_thumbnail_url(), R.drawable.common_noimage_product);
        return view;
    }

    public void setModelList(ModelList<KanojoItem> l) {
        super.setModelList(l);
		for (KanojoItem kanojoItem : l) {
			mDic.requestBitmap(kanojoItem.getImage_thumbnail_url());
		}
    }

    public void addModelList(ModelList<KanojoItem> l) {
        super.addModelList(l);
    }

    public void removeObserver() {
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.removeCallbacks(this.mRunnableLoadPhotos);
        this.mDic.deleteObserver(this.mResourcesObserver);
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(KanojoItemAdapter kanojoItemAdapter, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
            if (data != null) {
                KanojoItemAdapter.this.mHandler.removeCallbacks(KanojoItemAdapter.this.mNotifyThread);
                KanojoItemAdapter.this.mHandler.postDelayed(KanojoItemAdapter.this.mNotifyThread, 200);
            }
        }
    }

    private static class ViewHolder {
        TextView description;
        ImageView img;
        LinearLayout layoutUplevel;
        TextView textViewPurchasableLevel;
        TextView title;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(ViewHolder viewHolder) {
            this();
        }
    }
}
