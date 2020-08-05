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
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class KanojoInfoAdapter extends BaseActivityModelAdapter {

    private static final String TAG = "KanojoInfoAdapter";
    /* access modifiers changed from: private */
    public Handler mHandler;
    private LayoutInflater mInflater;
    /* access modifiers changed from: private */
    public final Runnable mNotifyThread = new Runnable() {
        public void run() {
            KanojoInfoAdapter.this.notifyDataSetChanged();
        }
    };
    private RemoteResourceManagerObserver mResourcesObserver;
    private RemoteResourceManager mRrm;

    public KanojoInfoAdapter(Context context, RemoteResourceManager rrm) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mRrm = rrm;
        this.mHandler = new Handler();
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, (RemoteResourceManagerObserver) null);
        if (this.mRrm != null) {
            this.mRrm.addObserver(this.mResourcesObserver);
        }
    }

    public void removeObserver() {
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mRrm.deleteObserver(this.mResourcesObserver);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.row_kanojo_info_activities, (ViewGroup) null);
            holder = new ViewHolder((ViewHolder) null);
            holder.imgLeft = (ImageView) view.findViewById(R.id.row_kanojo_info_activities_img);
            holder.txtActivity = (TextView) view.findViewById(R.id.row_kanojo_info_activities_txt);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ActivityModel act = (ActivityModel) getItem(position);
        if (act != null) {
            User user = act.getUser();
            if (user != null) {
                ImageCache.setImage(holder.imgLeft, user.getProfile_image_url(), this.mRrm, R.drawable.common_noimage);
            }
            if (holder.txtActivity != null) {
                holder.txtActivity.setText(act.getActivity());
            }
        }
        return view;
    }

    public void setModelList(ModelList<ActivityModel> l) {
        super.setModelList(l);
        Iterator it = l.iterator();
        while (it.hasNext()) {
            User user = ((ActivityModel) it.next()).getUser();
            if (user != null) {
                ImageCache.requestImage(user.getProfile_image_url(), this.mRrm);
            }
        }
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(KanojoInfoAdapter kanojoInfoAdapter, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
            if (data != null) {
                KanojoInfoAdapter.this.mHandler.removeCallbacks(KanojoInfoAdapter.this.mNotifyThread);
                KanojoInfoAdapter.this.mHandler.postDelayed(KanojoInfoAdapter.this.mNotifyThread, 400);
            }
        }
    }

    private static class ViewHolder {
        ImageView imgLeft;
        TextView txtActivity;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(ViewHolder viewHolder) {
            this();
        }
    }
}
