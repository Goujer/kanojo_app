package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class ProductAndKanojoView extends RelativeLayout {

    private static final String TAG = "ProductAndKanojoView";
    private ImageView imgLeft = findViewById(R.id.product_and_kanojo_left);
    private ImageView imgRight = findViewById(R.id.product_and_kanojo_right);
    private Context mContext;
    private LoadImgTask mLoadImgTask;

    public ProductAndKanojoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setGravity(16);
        setPadding(0, 8, 0, 8);
        LayoutInflater.from(context).inflate(R.layout.view_product_and_kanojo, this, true);
    }

    public void clear() {
        if (this.mLoadImgTask != null) {
            this.mLoadImgTask.cancel(true);
            this.mLoadImgTask = null;
        }
        Bitmap bm = ImageCache.getImage("kanojo");
        if (bm != null) {
            bm.recycle();
        }
        this.mContext = null;
    }

    public void setBitmap(Bitmap bitmap) {
        if (this.imgLeft != null) {
            this.imgLeft.setImageBitmap(bitmap);
            invalidate();
        }
    }

    public void executeLoadImgTask(RemoteResourceManager rrm, String leftImgUrl, Kanojo kanojo) {
        if (this.mLoadImgTask == null || this.mLoadImgTask.getStatus() == AsyncTask.Status.FINISHED) {
            ImageCache.hasImageAndRequest(leftImgUrl, rrm);
            this.mLoadImgTask = new LoadImgTask();
            this.mLoadImgTask.setSources(rrm, leftImgUrl, kanojo);
            this.mLoadImgTask.execute();
        }
    }

    class LoadImgTask extends AsyncTask<Void, Void, Void> {
        private Kanojo mKanojo;
        private String mLeftImgUrl;
        private Exception mReason = null;
        private RemoteResourceManager mRrm;

        LoadImgTask() {
        }

        public void setSources(RemoteResourceManager rrm, String leftimgurl, Kanojo kanojo) {
            this.mRrm = rrm;
            this.mKanojo = kanojo;
            this.mLeftImgUrl = leftimgurl;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void params) {
            if (this.mReason != null) {
            }
            if (ProductAndKanojoView.this.imgRight != null && ImageCache.hasImage("kanojo")) {
                ProductAndKanojoView.this.imgRight.setImageBitmap(ImageCache.getImage("kanojo"));
            }
            if (ProductAndKanojoView.this.imgLeft != null) {
                ImageCache.setImage(ProductAndKanojoView.this.imgLeft, this.mLeftImgUrl, this.mRrm, R.drawable.common_noimage_product);
                ProductAndKanojoView.this.invalidate();
            }
        }

        @Override
        protected void onCancelled() {
        }

        private Void process() throws Exception {
            if (this.mKanojo == null) {
                return null;
            }
            try {
                ImageCache.setImage("kanojo", Live2dUtil.createNormalIcon(ProductAndKanojoView.this.mContext, this.mKanojo));
                return null;
            } catch (OutOfMemoryError e) {
                throw new Exception(e);
            }
        }
    }
}
