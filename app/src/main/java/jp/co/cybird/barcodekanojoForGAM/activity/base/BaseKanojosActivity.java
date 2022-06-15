package jp.co.cybird.barcodekanojoForGAM.activity.base;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.activity.kanojo.KanojoRoomActivity;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;

public abstract class BaseKanojosActivity extends BaseActivity {

    public static final String TAG = "BaseKanojosActivity";
    private Live2dTask mLive2dTask;

    @Override
    protected void onPause() {
        if (this.mLive2dTask != null) {
            this.mLive2dTask.cancel(true);
            this.mLive2dTask = null;
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseInterface.REQUEST_KANOJO) {
            if (resultCode != BaseInterface.RESULT_KANOJO_GOOD_BYE) {
                executeLive2dTask();
            }
        } else if (resultCode == BaseInterface.RESULT_ADD_FRIEND) {
            Log.e(TAG, "resultCode == RESULT_ADD_FRIEND");
        } else if (resultCode == BaseInterface.RESULT_GENERATE_KANOJO) {
            Log.e(TAG, "resultCode == RESULT_GENERATE_KANOJO");
        }
    }

    protected void startKanojoRoomActivity(Kanojo kanojo) {
        if (kanojo != null) {
            Intent intent = new Intent().setClass(this, KanojoRoomActivity.class);
			intent.putExtra(BaseInterface.EXTRA_KANOJO, kanojo);
			startActivityForResult(intent, BaseInterface.REQUEST_KANOJO);
        }
    }

    private void executeLive2dTask() {
        if (this.mLive2dTask == null || this.mLive2dTask.getStatus() == AsyncTask.Status.FINISHED || this.mLive2dTask.cancel(true) || this.mLive2dTask.isCancelled()) {
            this.mLive2dTask = new Live2dTask();
            this.mLive2dTask.execute();
        }
    }

    class Live2dTask extends AsyncTask<Void, Void, Response<BarcodeKanojoModel>> {
        private Exception mReason = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Response<BarcodeKanojoModel> doInBackground(Void... params) {
            try {
                return process();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<BarcodeKanojoModel> response) {
            try {
                switch (BaseKanojosActivity.this.getCodeAndShowAlert(response, this.mReason)) {
                }
            } catch (BarcodeKanojoException e) {
            }
        }

        Response<BarcodeKanojoModel> process() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) BaseKanojosActivity.this.getApplication()).getBarcodeKanojo();
            Response<BarcodeKanojoModel> response = barcodeKanojo.play_on_live2d();
            Response<BarcodeKanojoModel> response2 = barcodeKanojo.vote_like();
            if (response != null && response2 != null) {
                response.addAll(response2);
                return response;
            } else if (response != null || response2 == null) {
                return response;
            } else {
                return response2;
            }
        }
    }
}
