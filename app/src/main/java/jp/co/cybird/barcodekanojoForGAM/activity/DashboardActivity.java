package jp.co.cybird.barcodekanojoForGAM.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.goujer.barcodekanojo.core.cache.DynamicImageCache;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.GreeBaseActivity;

import com.goujer.barcodekanojo.adapter.DashboardAdapter;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.view.DashboardHeaderView;
import com.goujer.barcodekanojo.view.UserProfileView;

public class DashboardActivity extends BaseKanojosActivity implements View.OnClickListener, AbsListView.OnScrollListener, DashboardAdapter.OnKanojoClickListener {
    private static final int DEFAULT_LIMIT = 6;
    protected static final String TAG = "DashboardActivity";
    private int code;
    private boolean isFinishedLoading = true;
    private boolean isVisible = false;
    private int mActivityCount = 0;
    private DashboardAdapter mDashboardAdapter;
    private View mFooter;
    private DashboardHeaderView mHeader;
    private int mLimit = 6;
    private ListView mListView;
    private LogInTask mLogInTask;
    private UserProfileView mProfileView;
    private ReadActivitiesTask mReadActivitiesTask;
    private RemoteResourceManagerObserver mResourcesObserver;
    private DynamicImageCache mDic;
    private boolean readAllFlg = false;
    private boolean test = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_dashboard);
		//Toolbar stuff
		if (Build.VERSION.SDK_INT >= 21) {
			setActionBar((Toolbar) findViewById(R.id.toolbar_primary));
			getActionBar().setDisplayShowTitleEnabled(false);
		}
        this.mDic = ((BarcodeKanojoApp) getApplication()).getImageCache();
		this.mProfileView = findViewById(R.id.common_profile);
        this.mListView = findViewById(R.id.list_activities);
        this.mFooter = getLayoutInflater().inflate(R.layout.row_footer, null, false);
        this.mHeader = new DashboardHeaderView(this);
        this.mResourcesObserver = new RemoteResourceManagerObserver(this, null);
        this.mDashboardAdapter = new DashboardAdapter(this, this.mDic, this.mResourcesObserver);
        this.mDashboardAdapter.setOnKanojoClickListener(this);
        this.mListView.addHeaderView(this.mHeader);
        this.mListView.addFooterView(this.mFooter);
        this.mListView.setAdapter(this.mDashboardAdapter);
        Log.d(TAG, "Start DashBoard Activity");
    }

    public View getClientView() {
        View layout = getLayoutInflater().inflate(R.layout.activity_dashboard, null);
        FrameLayout appLayoutRoot = new FrameLayout(this);
        appLayoutRoot.addView(layout);
        return appLayoutRoot;
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        this.mListView.setOnScrollListener(this);
        this.mDashboardAdapter.setOnKanojoClickListener(this);
        this.isVisible = true;
        updateProfileView();
        visibleFooter();
        this.isFinishedLoading = true;
        executeReadActivitiesTask();
    }

    @Override
    protected void onPause() {
        this.isVisible = false;
        if (this.mReadActivitiesTask != null) {
            this.mReadActivitiesTask.cancel(true);
            this.mReadActivitiesTask = null;
        }
        if (this.mLogInTask != null) {
            this.mLogInTask.cancel(true);
            this.mLogInTask = null;
        }
        if (isFinishing()) {
            this.mDashboardAdapter.removeObserver();
        }
        this.readAllFlg = false;
        this.isFinishedLoading = true;
        super.onPause();
    }

    @Override
    public void onStop() {
        this.readAllFlg = false;
        super.onStop();
    }

    @Override
	public void onDestroy() {
    	super.onDestroy();
		mDashboardAdapter.destroy();
		mProfileView.destroy();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseInterface.REQUEST_OPTION_ACTIVITY && resultCode == BaseInterface.RESULT_LOG_OUT) {
            setResult(BaseInterface.RESULT_LOG_OUT, null);
            finish();
        } else if (resultCode == 213) {
            final Kanojo kanojo = new Kanojo();
            int kanojo_id = data.getIntExtra(BaseInterface.EXTRA_KANOJO_ITEM, 0);
            if (kanojo_id > 0) {
                kanojo.setId(kanojo_id);
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        DashboardActivity.this.startKanojoRoomActivity(kanojo);
                    }
                }, 1500);
            }
        }
    }

    @Override
    protected void onUserUpdated() {
        updateProfileView();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void changeTab(Context packageContext, Class<?> cls) {
        Intent intent = new Intent().setClass(packageContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(GreeBaseActivity.FLG_NO_ANIMATION, true);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_dashboard_refresh) {
			refreshActivitiesTask();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public void onKanojoClick(Kanojo kanojo) {
        this.mDashboardAdapter.setOnKanojoClickListener(null);
        startKanojoRoomActivity(kanojo);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            executeReadActivitiesTask();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private void visibleFooter() {
        this.mListView.addFooterView(this.mFooter);
    }

    private void invisibleFooter() {
        this.mListView.removeFooterView(this.mFooter);
    }

    private void refreshActivitiesTask() {
        if (this.isVisible) {
            if (this.mReadActivitiesTask == null || this.mReadActivitiesTask.getStatus() == AsyncTask.Status.FINISHED) {
                this.mReadActivitiesTask = (ReadActivitiesTask) new ReadActivitiesTask().execute(new Integer[]{1});
            }
        }
    }

    private void updateProfileView() {
        User mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        if (mUser == null) {
            executeLogInTask();
            return;
        }
        this.mProfileView.setUser(mUser, this.mDic);
        this.mHeader.setUser(mUser);
    }

    private void executeReadActivitiesTask() {
        if (!this.isVisible || this.readAllFlg) {
            return;
        }
        if ((this.mReadActivitiesTask == null || this.mReadActivitiesTask.getStatus() == AsyncTask.Status.FINISHED) && this.isFinishedLoading) {
            this.mLimit = 6;
            this.mReadActivitiesTask = (ReadActivitiesTask) new ReadActivitiesTask().execute(new Integer[0]);
        }
    }

    class ReadActivitiesTask extends AsyncTask<Integer, Void, Response<?>> {
        private static final int PARAM_REFRESH = 1;
        private Exception mReason = null;
        private boolean refreshFlg = false;

        public void onPreExecute() {
            DashboardActivity.this.isFinishedLoading = false;
        }

        public Response<?> doInBackground(Integer... params) {
            boolean z = true;
            if (!(params == null || params.length == 0)) {
                if (params[0] != PARAM_REFRESH) {
                    z = false;
                }
                this.refreshFlg = z;
            }
            try {
                BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) DashboardActivity.this.getApplication()).getBarcodeKanojo();
                if (this.refreshFlg) {
                    return barcodeKanojo.my_user_timeline(0, 0, DashboardActivity.this.mActivityCount);
                }
                return barcodeKanojo.my_user_timeline(0, DashboardActivity.this.mActivityCount, DashboardActivity.this.mLimit);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            try {
                switch (DashboardActivity.this.getCodeAndShowDialog(response, this.mReason)) {
					case Response.CODE_SUCCESS:
                        ModelList<ActivityModel> temp = response.getActivityModelList();
                        if (!this.refreshFlg) {
                            if (temp != null) {
                                int size = temp.size();
                                if (size != 0) {
                                    if (size < DashboardActivity.this.mLimit) {
                                        DashboardActivity.this.readAllFlg = true;
                                        DashboardActivity.this.invisibleFooter();
                                    }
                                    DashboardActivity dashboardActivity = DashboardActivity.this;
                                    dashboardActivity.mActivityCount = dashboardActivity.mActivityCount + size;
                                    DashboardActivity.this.mDashboardAdapter.addModelList(temp);
                                } else {
                                    DashboardActivity.this.readAllFlg = true;
                                    DashboardActivity.this.invisibleFooter();
                                }
                            } else {
                                DashboardActivity.this.readAllFlg = true;
                                DashboardActivity.this.invisibleFooter();
                            }
                            DashboardActivity.this.isFinishedLoading = true;
                            break;
                        } else {
                            DashboardActivity.this.mDashboardAdapter.setModelList(temp);
                            DashboardActivity.this.isFinishedLoading = true;
                            break;
                        }
                    case Response.CODE_ERROR_NETWORK:
                        DashboardActivity.this.showToast(DashboardActivity.this.getResources().getString(R.string.error_internet));
                        DashboardActivity.this.invisibleFooter();
                        break;
                }
            } catch (BarcodeKanojoException e) {
            } finally {
                DashboardActivity.this.invisibleFooter();
            }
        }
    }

    private class RemoteResourceManagerObserver implements Observer {
        private RemoteResourceManagerObserver() {
        }

        /* synthetic */ RemoteResourceManagerObserver(DashboardActivity dashboardActivity, RemoteResourceManagerObserver remoteResourceManagerObserver) {
            this();
        }

        public void update(Observable observable, Object data) {
        }
    }

    protected void executeLogInTask() {
        if (this.mLogInTask == null || this.mLogInTask.getStatus() == AsyncTask.Status.FINISHED || this.mLogInTask.cancel(true) || this.mLogInTask.isCancelled()) {
            this.mLogInTask = (LogInTask) new LogInTask().execute(new Void[0]);
        } else {
            Toast.makeText(this, "ttttttt", Toast.LENGTH_SHORT).show();
        }
    }

    class LogInTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        LogInTask() {
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(dialog -> {
				DashboardActivity.this.clearHistory();
				((BarcodeKanojoApp) DashboardActivity.this.getApplication()).logged_out();
			});
        }

        @Override
        protected Response<?> doInBackground(Void... params) {
            try {
				BarcodeKanojoApp barcodeKanojoApp = ((BarcodeKanojoApp) DashboardActivity.this.getApplication());
				BarcodeKanojo barcodeKanojo = barcodeKanojoApp.getBarcodeKanojo();

				Response<BarcodeKanojoModel> verification = barcodeKanojo.verify(barcodeKanojoApp.getSettings().getUUID(), barcodeKanojoApp.getSettings().getEmail(), barcodeKanojoApp.getSettings().getPassword());
				barcodeKanojo.init_product_category_list();
				return verification;
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<?> response) {
            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
                    DashboardActivity.this.dismissProgressDialog();
                } catch (Throwable th) {
                    DashboardActivity.this.dismissProgressDialog();
                    throw th;
                }
            } else {
				if (response.getCode() == Response.CODE_SUCCESS) {
					DashboardActivity.this.updateProfileView();
					DashboardActivity.this.executeReadActivitiesTask();
				}
                DashboardActivity.this.dismissProgressDialog();
            }
        }

        @Override
        protected void onCancelled() {
            DashboardActivity.this.dismissProgressDialog();
        }
    }

    private void cleanUpList() {
        for (int i = 1; i < this.mActivityCount; i++) {
            if (this.mListView.getChildAt(i) != null) {
                cleanupView(this.mListView.getChildAt(i));
            }
        }
    }

    private void init() {
        if (this.test) {
            this.mListView = findViewById(R.id.list_activities);
            this.mListView.setAdapter(this.mDashboardAdapter);
        }
    }

    protected int getCodeAndShowDialog(Response<?> response, Exception e) throws BarcodeKanojoException {
        if (response != null) {
            return getCodeAndShowAlert(response, e);
        }
        this.code = Response.CODE_ERROR_NETWORK;
        return this.code;
    }
}
