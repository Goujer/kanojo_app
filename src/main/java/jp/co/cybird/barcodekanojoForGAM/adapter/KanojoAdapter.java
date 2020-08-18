package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.BaseKanojoPairAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.ObservableAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoPair;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.view.KanojoView;

public class KanojoAdapter extends BaseKanojoPairAdapter implements ObservableAdapter {

    private static final String TAG = "KanojoAdapter";
    private Handler mHandler;
    private LayoutInflater mInflater;
    private OnKanojoClickListener mListener;
    private int mLoadedPhotoIndex = 0;
    private final Runnable mNotifyThread = new Runnable() {
        public void run() {
            KanojoAdapter.this.notifyDataSetChanged();
        }
    };
    private RemoteResourceManagerObserver mResourcesObserver;
    private RemoteResourceManager mRrm;
    private Runnable mRunnableLoadPhotos = new Runnable() {
        public void run() {
            if (KanojoAdapter.this.mLoadedPhotoIndex < KanojoAdapter.this.getCount()) {
                KanojoAdapter kanojoAdapter = KanojoAdapter.this;
                KanojoAdapter kanojoAdapter2 = KanojoAdapter.this;
                int access$0 = kanojoAdapter2.mLoadedPhotoIndex;
                kanojoAdapter2.mLoadedPhotoIndex = access$0 + 1;
                ActivityModel a = (ActivityModel) kanojoAdapter.getItem(access$0);
                ImageCache.requestImage(a.getLeftImgUrl(), KanojoAdapter.this.mRrm);
                ImageCache.requestImage(a.getRightImgUrl(), KanojoAdapter.this.mRrm);
                KanojoAdapter.this.mHandler.postDelayed(KanojoAdapter.this.mRunnableLoadPhotos, 200);
            }
        }
    };

    public interface OnKanojoClickListener {
        void onKanojoClick(Kanojo kanojo);
    }

    public KanojoAdapter(Context context, RemoteResourceManager rrm) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
        this.mRrm = rrm;
        this.mHandler = new Handler();
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, null);
        if (this.mRrm != null) {
            this.mRrm.addObserver(this.mResourcesObserver);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.row_kanojos, null);
            holder = new ViewHolder(null);
            holder.left = view.findViewById(R.id.row_kanojos_left);
            holder.right = view.findViewById(R.id.row_kanojos_right);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final KanojoPair kanojoPair = (KanojoPair) getItem(position);
        holder.left.setKanojo(kanojoPair.getLeft(), this.mRrm);
        holder.right.setKanojo(kanojoPair.getRight(), this.mRrm);
        holder.left.setSelected(false);
        holder.left.setPressed(false);
        holder.right.setSelected(false);
        holder.right.setPressed(false);
        holder.left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KanojoAdapter.this.mListener.onKanojoClick(kanojoPair.getLeft());
            }
        });
        holder.right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                KanojoAdapter.this.mListener.onKanojoClick(kanojoPair.getRight());
            }
        });
        return view;
    }

    public void setOnKanojoClickListener(OnKanojoClickListener l) {
        this.mListener = l;
    }

    public void setModelList(ModelList<KanojoPair> l) {
        super.setModelList(l);
    }

    public void setKanojosModelList(ModelList<Kanojo> kanojos) {
        setModelList(KanojoToPair(kanojos));
    }

    public void addModelList(ModelList<KanojoPair> l) {
        super.addModelList(l);
		for (KanojoPair pair : l) {
			Kanojo kanojo = pair.getLeft();
			if (kanojo != null) {
				ImageCache.requestImage(kanojo.getProfile_image_url(), this.mRrm);
			}
			Kanojo kanojo2 = pair.getRight();
			if (kanojo2 != null) {
				ImageCache.requestImage(kanojo2.getProfile_image_url(), this.mRrm);
			}
		}
    }

    public void addKanojosModelList(ModelList<Kanojo> kanojos) {
        if (kanojos != null) {
            addModelList(KanojoToPair(kanojos));
        }
    }

    private ModelList<KanojoPair> KanojoToPair(ModelList<Kanojo> kanojos) {
        ModelList<KanojoPair> pairList = new ModelList<>();
        int size = kanojos.size();
        for (int i = 0; i < size; i += 2) {
            if (i + 1 < size) {
                pairList.add(new KanojoPair(kanojos.get(i), kanojos.get(i + 1)));
            } else {
                pairList.add(new KanojoPair(kanojos.get(i), null));
            }
        }
        return pairList;
    }

    public void removeObserver() {
        this.mHandler.removeCallbacks(this.mNotifyThread);
        this.mHandler.removeCallbacks(this.mRunnableLoadPhotos);
        this.mRrm.deleteObserver(this.mResourcesObserver);
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(KanojoAdapter kanojoAdapter, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
            if (data != null) {
                KanojoAdapter.this.mHandler.removeCallbacks(KanojoAdapter.this.mNotifyThread);
                KanojoAdapter.this.mHandler.postDelayed(KanojoAdapter.this.mNotifyThread, 400);
            }
        }
    }

    private static class ViewHolder {
        KanojoView left;
        KanojoView right;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(ViewHolder viewHolder) {
            this();
        }
    }
}
