package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseActivityModelAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class DashboardAdapter extends BaseActivityModelAdapter implements ObservableAdapter {
    private static final boolean DEBUG = false;
    private static final String TAG = "DashboardAdapter";
    private boolean isFirst = false;
    private Handler mHandler;
    private LayoutInflater mInflater;
    private OnKanojoClickListener mListener;
    private int mLoadedPhotoIndex = 0;
    private final Runnable mNotifyThread = new Runnable() {
        public void run() {
            DashboardAdapter.this.superNotifyDataSetChanged();
        }
    };
    private RemoteResourceManagerObserver mResourcesObserver;
    private RemoteResourceManager mRrm;
    private Runnable mRunnableLoadPhotos = new Runnable() {
        public void run() {
            if (DashboardAdapter.this.mLoadedPhotoIndex < DashboardAdapter.this.getCount()) {
                DashboardAdapter dashboardAdapter = DashboardAdapter.this;
                DashboardAdapter dashboardAdapter2 = DashboardAdapter.this;
                int access$0 = dashboardAdapter2.mLoadedPhotoIndex;
                dashboardAdapter2.mLoadedPhotoIndex = access$0 + 1;
                ActivityModel a = (ActivityModel) dashboardAdapter.getItem(access$0);
                ImageCache.requestImage(a.getLeftImgUrl(), DashboardAdapter.this.mRrm);
                ImageCache.requestImage(a.getRightImgUrl(), DashboardAdapter.this.mRrm);
                DashboardAdapter.this.mHandler.postDelayed(DashboardAdapter.this.mRunnableLoadPhotos, 200);
            }
        }
    };

    public interface OnKanojoClickListener {
        void onKanojoClick(Kanojo kanojo);
    }

    public DashboardAdapter(Context context, RemoteResourceManager rrm, Observer observer) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mRrm = rrm;
        this.mHandler = new Handler();
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, (RemoteResourceManagerObserver) null);
        this.mRrm.addObserver(this.mResourcesObserver);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.row_activities, null);
            holder = new ViewHolder(this, null);
            holder.imgLeft = view.findViewById(R.id.row_activities_left_img);
            holder.imgRight = view.findViewById(R.id.row_activities_right_img);
            holder.imgRightCover = view.findViewById(R.id.row_activities_right_img_cover);
            holder.txtActivity = view.findViewById(R.id.row_activities_txt);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ActivityModel act = (ActivityModel) getItem(position);
        if (act != null) {
            ImageCache.setImage(holder.imgLeft, act.getLeftImgUrl(), this.mRrm, R.drawable.common_noimage_product);
            ImageCache.setImage(holder.imgRight, act.getRightImgUrl(), this.mRrm, R.drawable.common_noimage_product);
            if (holder.txtActivity != null) {
                holder.txtActivity.setText(act.getActivity());
            }
            final Kanojo kanojo = act.getKanojo();
            holder.imgLeft.setOnClickListener(null);
            holder.imgRight.setOnClickListener(null);
            holder.imgRight.setVisibility(View.VISIBLE);
            holder.imgRightCover.setVisibility(View.VISIBLE);
            switch (act.getActivity_type()) {
                case 2:
                case 9:
                    holder.imgLeft.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (DashboardAdapter.this.mListener != null) {
                                DashboardAdapter.this.mListener.onKanojoClick(kanojo);
                            }
                        }
                    });
                    break;
                case 5:
                case 7:
                case 8:
                case 10:
                    holder.imgRight.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (DashboardAdapter.this.mListener != null) {
                                DashboardAdapter.this.mListener.onKanojoClick(kanojo);
                            }
                        }
                    });
                    break;
                case 11:
                    holder.imgRight.setVisibility(View.GONE);
                    holder.imgRightCover.setVisibility(View.GONE);
                    break;
            }
        }
        return view;
    }

    public void setOnKanojoClickListener(OnKanojoClickListener l) {
        this.mListener = l;
    }

    public void setModelList(ModelList<ActivityModel> l) {
        super.setModelList(l);
        this.mHandler.removeCallbacks(this.mRunnableLoadPhotos);
        this.mHandler.postDelayed(this.mRunnableLoadPhotos, 100);
    }

    public void addModelList(ModelList<ActivityModel> l) {
        super.addModelList(l);
        Iterator it = l.iterator();
        while (it.hasNext()) {
            ActivityModel a = (ActivityModel) it.next();
            ImageCache.requestImage(a.getLeftImgUrl(), this.mRrm);
            ImageCache.requestImage(a.getRightImgUrl(), this.mRrm);
        }
    }

    public void removeObserver() {
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.removeCallbacks(this.mRunnableLoadPhotos);
        this.mRrm.deleteObserver(this.mResourcesObserver);
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(DashboardAdapter dashboardAdapter, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
            if (data != null) {
                DashboardAdapter.this.mHandler.removeCallbacks(DashboardAdapter.this.mNotifyThread);
                DashboardAdapter.this.mHandler.postDelayed(DashboardAdapter.this.mNotifyThread, 400);
            }
        }
    }

    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.postDelayed(this.mNotifyThread, 400);
    }

    public void superNotifyDataSetChanged() {
        if (this.isFirst) {
            super.notifyDataSetInvalidated();
        } else {
            super.notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView imgLeft;
        ImageView imgRight;
        ImageView imgRightCover;
        TextView txtActivity;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(DashboardAdapter dashboardAdapter, ViewHolder viewHolder) {
            this();
        }
    }
}
