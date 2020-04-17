package jp.co.cybird.barcodekanojoForGAM.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import jp.co.cybird.barcodekanojoForGAM.adapter.KanojoAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.SeparatedListHeaderAdapter;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.SearchResult;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.FirstbootUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.provider.KanojoSearchSuggestionsProvider;
import jp.co.cybird.barcodekanojoForGAM.view.MoreBtnView;
import jp.co.cybird.barcodekanojoForGAM.view.UserProfileView;

public class KanojosActivity extends BaseKanojosActivity implements View.OnClickListener, MoreBtnView.OnMoreClickListener, KanojoAdapter.OnKanojoClickListener {
    private static final boolean DEBUG = false;
    private static final int DEFAULT_LIMIT = 6;
    private static final int FRIENDS_MAX = 100;
    private static final int RANKING_MAX = 100;
    public static final String TAG = "KanojosActivity";
    private static final int YOUR_KANOJOS_MAX = 100;
    private final int MORE_FRIENDS = 11;
    private final int MORE_KANOJOS = 10;
    private final int MORE_RANKING = 12;
    private boolean isLoading = false;
    /* access modifiers changed from: private */
    public boolean isSearch = false;
    /* access modifiers changed from: private */
    public SeparatedListHeaderAdapter mAdapter;
    private StatusHolder mFriends;
    private KanojoTask mKanojoTask;
    private ListView mKanojosListView;
    private LogInTask mLogInTask;
    private UserProfileView mProfileView;
    private StatusHolder mRanking;
    private RemoteResourceManager mRrm;
    /* access modifiers changed from: private */
    public String mSearchWord = null;
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            StatusHolder next = (StatusHolder) KanojosActivity.this.getQueue().poll();
            if (next != null) {
                KanojosActivity.this.executeKanojoTask(next);
            }
        }
    };
    private Queue<StatusHolder> mTaskQueue;
    private User mUser;
    private StatusHolder mYourKanojos;
    private Resources r;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_kanojos);
        this.r = getResources();
        this.mRrm = ((BarcodeKanojoApp) getApplication()).getRemoteResourceManager();
        this.mProfileView = (UserProfileView) findViewById(R.id.common_profile);
        this.mKanojosListView = (ListView) findViewById(R.id.kanojos_list);
        this.mYourKanojos = new StatusHolder();
        this.mYourKanojos.what = 0;
        this.mFriends = new StatusHolder();
        this.mFriends.what = 1;
        this.mRanking = new StatusHolder();
        this.mRanking.what = 2;
        this.mAdapter = new SeparatedListHeaderAdapter(this);
        this.mKanojosListView.setSmoothScrollbarEnabled(true);
        this.mKanojosListView.setDividerHeight(0);
        this.isSearch = false;
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        final RelativeLayout firstBoot = (RelativeLayout) findViewById(R.id.kanojo_firstboot);
        if (FirstbootUtil.isShowed(this, "kanojo_firstboot")) {
            firstBoot.setVisibility(8);
            cleanupView(firstBoot);
        } else {
            firstBoot.setVisibility(0);
            firstBoot.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    firstBoot.setVisibility(8);
                    KanojosActivity.this.cleanupView(firstBoot);
                }
            });
        }
        executeListTask(true);
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_kanojos, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.setBackgroundColor(-1);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Kanojo kanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            if (kanojo != null) {
                startKanojoRoomActivity(kanojo);
            }
            getIntent().getExtras().clear();
            getIntent().replaceExtras((Bundle) null);
        }
        updateProfileView();
        Log.d("NguyenTT", "Kanojo onResume " + this.isLoading);
        if (this.isSearch) {
            executeSearchTask(this.mSearchWord);
        } else {
            executeListTask(false);
        }
        this.isLoading = false;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mYourKanojos.loading || this.mFriends.loading || this.mRanking.loading) {
            this.isLoading = true;
        }
        if (isFinishing()) {
            this.mAdapter.removeObserver();
        }
        if (this.mKanojoTask != null) {
            this.mKanojoTask.cancel(true);
            this.mKanojoTask = null;
        }
        clearQueue();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 101:
            case 102:
            case 103:
            case BaseInterface.RESULT_KANOJO_GOOD_BYE:
                this.isLoading = true;
                startCheckSession();
                return;
            case BaseInterface.RESULT_KANOJO_MESSAGE_DIALOG:
                final Kanojo kanojo = new Kanojo();
                int kanojo_id = data.getIntExtra(BaseInterface.EXTRA_KANOJO_ITEM, 0);
                if (kanojo_id > 0) {
                    kanojo.setId(kanojo_id);
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            KanojosActivity.this.startKanojoRoomActivity(kanojo);
                        }
                    }, 1500);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            ((BarcodeKanojoApp) getApplication()).logged_out();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View v) {
    }

    public void onMoreClick(int id) {
        switch (id) {
            case 10:
                loadMoreList(this.mYourKanojos);
                return;
            case 11:
                loadMoreList(this.mFriends);
                return;
            case 12:
                loadMoreList(this.mRanking);
                return;
            default:
                return;
        }
    }

    public void onKanojoClick(Kanojo kanojo) {
        if (kanojo != null) {
            startKanojoRoomActivity(kanojo);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kanojos, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_kanojos_refresh:
                if (this.isSearch) {
                    executeListTask(true);
                    return true;
                }
                executeListTask(false);
                return true;
            case R.id.menu_kanojos_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String action = intent.getAction();
            String query = intent.getStringExtra("query");
            if ("android.intent.action.SEARCH".equals(action) && query != null) {
                new SearchRecentSuggestions(this, KanojoSearchSuggestionsProvider.AUTHORITY, 1).saveRecentQuery(query, (String) null);
                executeSearchTask(query);
            }
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Kanojo kanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
                if (kanojo != null) {
                    startKanojoRoomActivity(kanojo);
                }
                intent.getExtras().clear();
                getIntent().replaceExtras((Bundle) null);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserUpdated() {
        updateProfileView();
    }

    /* access modifiers changed from: private */
    public void updateProfileView() {
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        if (this.mUser == null) {
            executeLogInTask();
            return;
        }
        this.mProfileView.setUser(this.mUser, this.mRrm);
        if (!this.isSearch) {
            if (this.mYourKanojos.txtNumber != null) {
                this.mYourKanojos.txtNumber.setText(new StringBuilder().append(this.mUser.getKanojo_count()).toString());
            }
            if (this.mFriends.txtNumber != null) {
                this.mFriends.txtNumber.setText(new StringBuilder().append(this.mUser.getWish_count()).toString());
            }
        }
    }

    private void addSection(int id, String title, String key, StatusHolder list) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.row_selection, (ViewGroup) null);
        TextView txtTitle = (TextView) headerView.findViewById(R.id.row_selection_title);
        if (txtTitle != null) {
            txtTitle.setText(title);
        }
        list.txtNumber = (TextView) headerView.findViewById(R.id.row_selection_number);
        list.more = new MoreBtnView(getApplicationContext());
        list.more.setOnMoreClickListener(id, this);
        list.displayed = 0;
        list.adapter = new KanojoAdapter(getApplicationContext(), this.mRrm);
        list.adapter.setKanojosModelList(new ModelList());
        list.adapter.setOnKanojoClickListener(this);
        list.key = key;
        this.mAdapter.addSection(key, headerView, list.adapter, list.more);
    }

    /* access modifiers changed from: private */
    public synchronized void executeListTask(boolean initflg) {
        int i;
        int i2 = 6;
        synchronized (this) {
            this.isSearch = false;
            this.mSearchWord = null;
            if (this.mKanojoTask != null) {
                this.mKanojoTask.cancel(true);
                this.mKanojoTask = null;
            }
            clearQueue();
            if (initflg) {
                this.mAdapter.clear();
                this.mAdapter.notifyDataSetInvalidated();
                addSection(10, this.r.getString(R.string.kanojos_your_kanojos), "your_kanojos", this.mYourKanojos);
                addSection(11, this.r.getString(R.string.kanojos_just_friend), "just_friend", this.mFriends);
                addSection(12, this.r.getString(R.string.kanojos_ranking), "ranking", this.mRanking);
                this.mKanojosListView.setAdapter(this.mAdapter);
            }
            this.mYourKanojos.MAX = 100;
            this.mFriends.MAX = 100;
            this.mRanking.MAX = 100;
            this.mYourKanojos.index = 0;
            this.mFriends.index = 0;
            this.mRanking.index = 0;
            this.mYourKanojos.limit = this.mYourKanojos.displayed > 6 ? this.mYourKanojos.displayed : 6;
            StatusHolder statusHolder = this.mFriends;
            if (this.mFriends.displayed > 6) {
                i = this.mFriends.displayed;
            } else {
                i = 6;
            }
            statusHolder.limit = i;
            StatusHolder statusHolder2 = this.mRanking;
            if (this.mRanking.displayed > 6) {
                i2 = this.mRanking.displayed;
            }
            statusHolder2.limit = i2;
            getQueue().offer(this.mYourKanojos);
            getQueue().offer(this.mFriends);
            getQueue().offer(this.mRanking);
            this.mTaskEndHandler.sendEmptyMessage(0);
        }
    }

    private synchronized void executeSearchTask(String word) {
        int i = 6;
        synchronized (this) {
            this.isSearch = true;
            this.mSearchWord = word;
            clearQueue();
            if (this.mKanojoTask != null) {
                this.mKanojoTask.cancel(true);
                this.mKanojoTask = null;
            }
            this.mAdapter.clear();
            this.mAdapter.notifyDataSetInvalidated();
            addSection(10, this.r.getString(R.string.kanojos_search_your_kanojos), "your_kanojos", this.mYourKanojos);
            addSection(11, this.r.getString(R.string.kanojos_search_just_friend), "just_friend", this.mFriends);
            this.mKanojosListView.setAdapter(this.mAdapter);
            this.mYourKanojos.index = 0;
            this.mFriends.index = 0;
            this.mYourKanojos.limit = this.mYourKanojos.displayed > 6 ? this.mYourKanojos.displayed : 6;
            StatusHolder statusHolder = this.mFriends;
            if (this.mFriends.displayed > 6) {
                i = this.mFriends.displayed;
            }
            statusHolder.limit = i;
            getQueue().offer(this.mYourKanojos);
            getQueue().offer(this.mFriends);
            this.mTaskEndHandler.sendEmptyMessage(0);
        }
    }

    private void loadMoreList(StatusHolder list) {
        if (!isLoading(list)) {
            if (list.what == 2) {
                list.index = 0;
                list.limit = list.displayed + 6;
            } else {
                list.index = list.displayed;
                list.limit = 6;
            }
            getQueue().offer(list);
            this.mTaskEndHandler.sendEmptyMessage(0);
        }
    }

    /* access modifiers changed from: private */
    public Queue<StatusHolder> getQueue() {
        if (this.mTaskQueue == null) {
            this.mTaskQueue = new LinkedList();
        }
        return this.mTaskQueue;
    }

    /* access modifiers changed from: private */
    public synchronized void clearQueue() {
        getQueue().clear();
        this.mYourKanojos.loading = false;
        this.mFriends.loading = false;
        this.mRanking.loading = false;
        if (this.mYourKanojos.more != null) {
            this.mYourKanojos.more.setLoading(false);
        }
        if (this.mFriends.more != null) {
            this.mFriends.more.setLoading(false);
        }
        if (this.mRanking.more != null) {
            this.mRanking.more.setLoading(false);
        }
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        if (this.mKanojoTask == null || this.mKanojoTask.getStatus() == AsyncTask.Status.FINISHED || status.more == null) {
            return false;
        }
        status.more.setLoading(false);
        return false;
    }

    /* access modifiers changed from: private */
    public void executeKanojoTask(StatusHolder list) {
        if (!isLoading(list)) {
            this.mKanojoTask = new KanojoTask();
            this.mKanojoTask.setList(list);
            this.mKanojoTask.execute(new Void[0]);
        }
    }

    class KanojoTask extends AsyncTask<Void, Void, Response<?>> {
        private StatusHolder mList;
        private Exception mReason = null;

        KanojoTask() {
        }

        public void setList(StatusHolder list) {
            this.mList = list;
        }

        public void onPreExecute() {
            this.mList.loading = true;
            if (this.mList.more != null) {
                this.mList.more.setLoading(true);
            }
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mList);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            SearchResult res;
            try {
                switch (response.getCode()) {
                    case 200:
                        if (this.mList != null) {
                            if (KanojosActivity.this.isSearch && (res = (SearchResult) response.get(SearchResult.class)) != null) {
                                if (this.mList.txtNumber != null) {
                                    this.mList.txtNumber.setText(new StringBuilder().append(res.getHit_count()).toString());
                                }
                                this.mList.MAX = res.getHit_count();
                            }
                            ModelList<Kanojo> temp = response.getKanojoList();
                            if (temp != null) {
                                int size = temp.size();
                                if (this.mList.index == 0) {
                                    if (size < this.mList.limit) {
                                        KanojosActivity.this.mAdapter.removeFooter(this.mList.key);
                                    }
                                    this.mList.displayed = size;
                                    this.mList.adapter.clear();
                                    this.mList.adapter.notifyDataSetInvalidated();
                                    this.mList.adapter.addKanojosModelList(temp);
                                } else {
                                    this.mList.displayed += size;
                                    this.mList.adapter.addKanojosModelList(temp);
                                    if (size < this.mList.limit) {
                                        KanojosActivity.this.mAdapter.removeFooter(this.mList.key);
                                    }
                                }
                                if (this.mList.displayed >= this.mList.MAX) {
                                    KanojosActivity.this.mAdapter.removeFooter(this.mList.key);
                                }
                            }
                            KanojosActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            break;
                        }
                        break;
                    case 401:
                        KanojosActivity.this.clearQueue();
                        break;
                }
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.more != null) {
                        this.mList.more.setLoading(false);
                    }
                }
            } catch (Exception e) {
                KanojosActivity.this.clearQueue();
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.more != null) {
                        this.mList.more.setLoading(false);
                    }
                }
            } catch (Throwable th) {
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.more != null) {
                        this.mList.more.setLoading(false);
                    }
                }
                throw th;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            if (this.mList != null) {
                this.mList.loading = false;
                if (this.mList.more != null) {
                    this.mList.more.setLoading(false);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) KanojosActivity.this.getApplication()).getBarcodeKanojo();
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.what) {
                case 0:
                    return barcodeKanojo.my_current_kanojos(list.index, list.limit, KanojosActivity.this.mSearchWord);
                case 1:
                    return barcodeKanojo.my_friend_kanojos(list.index, list.limit, KanojosActivity.this.mSearchWord);
                case 2:
                    return barcodeKanojo.like_ranking(list.index, list.limit);
                default:
                    return null;
            }
        }
    }

    static class StatusHolder {
        public static final int FRIENDS = 1;
        public static final int RANKING = 2;
        public static final int YOUR_KANOJOS = 0;
        int MAX = 100;
        KanojoAdapter adapter;
        int displayed = 0;
        int index = 0;
        String key;
        int limit = 6;
        boolean loading = false;
        MoreBtnView more;
        TextView txtNumber;
        int what;

        StatusHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void executeLogInTask() {
        if (this.mLogInTask == null || this.mLogInTask.getStatus() == AsyncTask.Status.FINISHED || this.mLogInTask.cancel(true) || this.mLogInTask.isCancelled()) {
            this.mLogInTask = (LogInTask) new LogInTask().execute(new Void[0]);
        } else {
            Toast.makeText(this, "ttttttt", 0);
        }
    }

    class LogInTask extends AsyncTask<Void, Void, Response<?>> {
        private Exception mReason = null;

        LogInTask() {
        }

        public void onPreExecute() {
            ProgressDialog unused = KanojosActivity.this.showProgressDialog(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    KanojosActivity.this.backTab(KanojosActivity.this, DashboardActivity.class);
                }
            });
        }

        public Response<?> doInBackground(Void... params) {
            try {
                return login();
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        public void onPostExecute(Response<?> response) {
            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
                    KanojosActivity.this.dismissProgressDialog();
                } catch (Throwable th) {
                    KanojosActivity.this.dismissProgressDialog();
                    throw th;
                }
            } else {
                switch (response.getCode()) {
                    case 200:
                        KanojosActivity.this.updateProfileView();
                        KanojosActivity.this.executeListTask(true);
                        break;
                }
                KanojosActivity.this.dismissProgressDialog();
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            KanojosActivity.this.dismissProgressDialog();
        }

        /* access modifiers changed from: package-private */
        public Response<?> login() throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) KanojosActivity.this.getApplication()).getBarcodeKanojo();
            Log.d(KanojosActivity.TAG, "login() cannot be used !!!");
            barcodeKanojo.init_product_category_list();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void startCheckSession() {
        super.startCheckSession();
    }

    /* access modifiers changed from: protected */
    public void endCheckSession() {
        updateProfileView();
    }
}
