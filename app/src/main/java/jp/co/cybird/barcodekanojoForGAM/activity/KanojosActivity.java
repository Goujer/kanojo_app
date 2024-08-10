package jp.co.cybird.barcodekanojoForGAM.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goujer.barcodekanojo.core.cache.DynamicImageCache;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseKanojosActivity;
import com.goujer.barcodekanojo.adapter.KanojoAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.SeparatedListHeaderAdapter;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.SearchResult;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.FirstbootUtil;
import jp.co.cybird.barcodekanojoForGAM.provider.KanojoSearchSuggestionsProvider;
import jp.co.cybird.barcodekanojoForGAM.view.MoreBtnView;
import com.goujer.barcodekanojo.view.UserProfileView;

public class KanojosActivity extends BaseKanojosActivity implements View.OnClickListener, MoreBtnView.OnMoreClickListener, KanojoAdapter.OnKanojoClickListener {
	public static final String TAG = "KanojosActivity";

	private static final int DEFAULT_LIMIT = 6;

	private static final int YOUR_KANOJOS_MAX = 100;
    private static final int FRIENDS_MAX = 100;
    private static final int RANKING_MAX = 100;

	private final int MORE_KANOJOS = 10;
    private final int MORE_FRIENDS = 11;
    private final int MORE_RANKING = 12;

    private boolean isLoading = false;
    private boolean isSearch = false;

	private StatusHolder mYourKanojos;
	private StatusHolder mFriends;
	private StatusHolder mRanking;

    private KanojoTask myKanojoTask;
	private KanojoTask friendKanojoTask;
	private KanojoTask rankingKanojoTask;

	private SeparatedListHeaderAdapter mAdapter;
    private ListView mKanojosListView;
    private UserProfileView mProfileView;
	private SwipeRefreshLayout swipeRefreshLayout;

    private DynamicImageCache mDic;
	private LogInTask mLogInTask;
    private String mSearchWord = null;
    private User mUser;
    private Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kanojos);
        this.r = getResources();

        //Toolbar stuff
        if (Build.VERSION.SDK_INT >= 21) {
			setActionBar((Toolbar) findViewById(R.id.toolbar_primary));
			getActionBar().setDisplayShowTitleEnabled(false);
		}

        this.mDic = ((BarcodeKanojoApp) getApplication()).getImageCache();
        this.mProfileView = findViewById(R.id.common_profile);
        this.mKanojosListView = findViewById(R.id.kanojos_list);
		this.swipeRefreshLayout = findViewById(R.id.kanojos_swipe_refresh);

	    swipeRefreshLayout.setOnRefreshListener(() -> {
				    // This method performs the actual data-refresh operation and calls setRefreshing(false) when it finishes.
				    refreshAction();
			    }
	    );


	    this.mYourKanojos = new StatusHolder();
		this.mYourKanojos.what = StatusHolder.YOUR_KANOJOS;
        this.mFriends = new StatusHolder();
		this.mFriends.what = StatusHolder.FRIENDS;
        this.mRanking = new StatusHolder();
		this.mRanking.what = StatusHolder.RANKING;

        this.mAdapter = new SeparatedListHeaderAdapter(this);
        this.mKanojosListView.setSmoothScrollbarEnabled(true);
        this.mKanojosListView.setDividerHeight(0);
        this.isSearch = false;
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();

        final RelativeLayout firstBoot = findViewById(R.id.kanojo_firstboot);
        if (FirstbootUtil.isShowed(this, "kanojo_firstboot")) {
            firstBoot.setVisibility(View.GONE);
            cleanupView(firstBoot);
        } else {
            firstBoot.setVisibility(View.VISIBLE);
            firstBoot.setOnClickListener(v -> {
                firstBoot.setVisibility(View.GONE);
                KanojosActivity.this.cleanupView(firstBoot);
            });
        }
        executeListTask(true);
    }

	@Override
	protected void onResume() {
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

		if (this.isSearch) {
			executeSearchTask(this.mSearchWord);
		} else {
			executeListTask(false);
		}
		this.isLoading = false;
	}

	@Override
	protected void onPause() {
		if (this.mYourKanojos.loading || this.mFriends.loading || this.mRanking.loading) {
			this.isLoading = true;
		}
		if (isFinishing()) {
			this.mAdapter.removeObserver();
		}

		resetKanojoTasks();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mProfileView.destroy();
	}

    public View getClientView() {
        View layout = getLayoutInflater().inflate(R.layout.activity_kanojos, null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.setBackgroundColor(-1);
        appLayoutRoot.addView(layout);
        return appLayoutRoot;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case BaseInterface.RESULT_GENERATE_KANOJO:
            case BaseInterface.RESULT_ADD_FRIEND:
            case BaseInterface.RESULT_KANOJO_GOOD_BYE:
				refreshAction();
	        case BaseInterface.RESULT_SAVE_PRODUCT_INFO:
                this.isLoading = true;
                startCheckSession();
                return;
            case BaseInterface.RESULT_KANOJO_MESSAGE_DIALOG:
                final Kanojo kanojo = new Kanojo();
                int kanojo_id = data.getIntExtra(BaseInterface.EXTRA_KANOJO_ITEM, 0);   //TODO Look into whatever this is doing
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
        }
    }

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ((BarcodeKanojoApp) getApplication()).logged_out();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
    }

	@Override
	public void onMoreClick(int id) {
		switch (id) {
			case MORE_KANOJOS:
				if (myKanojoTask == null || !myKanojoTask.isLoading()) {
					mYourKanojos.index = mYourKanojos.displayed;
					mYourKanojos.limit = DEFAULT_LIMIT;
					myKanojoTask = new KanojoTask(this, mYourKanojos);
					executeKanojoTask(myKanojoTask);
				}
				return;
            case MORE_FRIENDS:
	            if (friendKanojoTask == null || !friendKanojoTask.isLoading()) {
		            mFriends.index = mFriends.displayed;
		            mFriends.limit = DEFAULT_LIMIT;
					friendKanojoTask = new KanojoTask(this, mFriends);
					executeKanojoTask(friendKanojoTask);
	            }
                return;
            case MORE_RANKING:
	            if (rankingKanojoTask == null || !rankingKanojoTask.isLoading()) {
		            mRanking.index = 0;
		            mRanking.limit = mRanking.displayed + DEFAULT_LIMIT;
					rankingKanojoTask = new KanojoTask(this, mRanking);
					executeKanojoTask(rankingKanojoTask);
	            }
                return;
            default:
        }
    }

	@Override
    public void onKanojoClick(Kanojo kanojo) {
        if (kanojo != null) {
            startKanojoRoomActivity(kanojo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_kanojos, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_kanojos_refresh) {
			swipeRefreshLayout.setRefreshing(true);
			refreshAction();
			return true;
		} else if (itemId == R.id.menu_kanojos_search) {
			onSearchRequested();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String action = intent.getAction();
            String query = intent.getStringExtra("query");
            if ("android.intent.action.SEARCH".equals(action) && query != null) {
                new SearchRecentSuggestions(this, KanojoSearchSuggestionsProvider.AUTHORITY, 1).saveRecentQuery(query, null);
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

    @Override
    protected void onUserUpdated() {
        updateProfileView();
    }

	private void refreshAction() {
		executeListTask(this.isSearch); //If search is true and we refresh we should re-initialize the list
		swipeRefreshLayout.setRefreshing(false);
	}

    private void updateProfileView() {
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        if (this.mUser == null) {
            executeLogInTask();
            return;
        }
        this.mProfileView.setUser(this.mUser, this.mDic);
        if (!this.isSearch) {
            if (this.mYourKanojos.txtNumber != null) {
                this.mYourKanojos.txtNumber.setText(String.valueOf(this.mUser.getKanojo_count()));
            }
            if (this.mFriends.txtNumber != null) {
                this.mFriends.txtNumber.setText(String.valueOf(this.mUser.getWish_count()));
            }
        }
    }

    private void addSection(int id, String title, String key, StatusHolder list) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.row_selection, null);
        TextView txtTitle = headerView.findViewById(R.id.row_selection_title);
        if (txtTitle != null) {
            txtTitle.setText(title);
        }
        list.txtNumber = headerView.findViewById(R.id.row_selection_number);
        list.moreBtn = new MoreBtnView(getApplicationContext());
        list.moreBtn.setOnMoreClickListener(id, this);
        list.displayed = 0;
        list.adapter = new KanojoAdapter(getApplicationContext(), this.mDic);
        list.adapter.setKanojosModelList(new ModelList<>());
        list.adapter.setOnKanojoClickListener(this);
        list.key = key;
        this.mAdapter.addSection(key, headerView, list.adapter, list.moreBtn);
    }

    private synchronized void executeListTask(boolean initflg) {
        synchronized (this) {
            this.isSearch = false;
            this.mSearchWord = null;
			resetKanojoTasks();

            if (initflg) {  //Sets list back to default
                this.mAdapter.clear();
                this.mAdapter.notifyDataSetInvalidated();   //TODO Consider if all updates or refreshes should include this
				addSection(MORE_KANOJOS, this.r.getString(R.string.kanojos_your_kanojos), "your_kanojos", this.mYourKanojos);
				addSection(MORE_FRIENDS, this.r.getString(R.string.kanojos_just_friend), "just_friend", this.mFriends);
				addSection(MORE_RANKING, this.r.getString(R.string.kanojos_ranking), "ranking", this.mRanking);
                this.mKanojosListView.setAdapter(this.mAdapter);
            }

            this.mYourKanojos.MAX = YOUR_KANOJOS_MAX;
            this.mFriends.MAX = FRIENDS_MAX;
            this.mRanking.MAX = RANKING_MAX;

            this.mYourKanojos.index = 0;
            this.mFriends.index = 0;
            this.mRanking.index = 0;

			this.mYourKanojos.limit = Math.max(this.mYourKanojos.displayed, DEFAULT_LIMIT);
			this.mFriends.limit = Math.max(this.mFriends.displayed, DEFAULT_LIMIT);
			this.mRanking.limit = Math.max(this.mRanking.displayed, DEFAULT_LIMIT);

			myKanojoTask = new KanojoTask(this, this.mYourKanojos);
			friendKanojoTask = new KanojoTask(this, this.mFriends);
			rankingKanojoTask = new KanojoTask(this, this.mRanking);
			executeKanojoTask(myKanojoTask);
			executeKanojoTask(friendKanojoTask);
			executeKanojoTask(rankingKanojoTask);
        }
    }

    private synchronized void executeSearchTask(String word) {
        synchronized (this) {
            this.isSearch = true;
            this.mSearchWord = word;
			resetKanojoTasks();
            this.mAdapter.clear();
            this.mAdapter.notifyDataSetInvalidated();

			addSection(MORE_KANOJOS, this.r.getString(R.string.kanojos_search_your_kanojos), "your_kanojos", this.mYourKanojos);
			addSection(MORE_FRIENDS, this.r.getString(R.string.kanojos_search_just_friend), "just_friend", this.mFriends);

            this.mKanojosListView.setAdapter(this.mAdapter);

            this.mYourKanojos.index = 0;
            this.mFriends.index = 0;

			this.mYourKanojos.limit = Math.max(this.mYourKanojos.displayed, DEFAULT_LIMIT);
			this.mFriends.limit = Math.max(this.mFriends.displayed, DEFAULT_LIMIT);

			myKanojoTask = new KanojoTask(this, this.mYourKanojos);
			friendKanojoTask = new KanojoTask(this, this.mFriends);
			executeKanojoTask(myKanojoTask);
			executeKanojoTask(friendKanojoTask);
        }
    }

	private synchronized void resetKanojoTasks() {
        this.mYourKanojos.loading = false;
        this.mFriends.loading = false;
        this.mRanking.loading = false;

        if (this.mYourKanojos.moreBtn != null) {
            this.mYourKanojos.moreBtn.setLoading(false);
        }
        if (this.mFriends.moreBtn != null) {
            this.mFriends.moreBtn.setLoading(false);
        }
        if (this.mRanking.moreBtn != null) {
            this.mRanking.moreBtn.setLoading(false);
        }

		if (this.myKanojoTask != null) {
			this.myKanojoTask.cancel(true);
			this.myKanojoTask = null;
		}
		if (this.friendKanojoTask != null) {
			this.friendKanojoTask.cancel(true);
			this.friendKanojoTask = null;
		}
		if (this.rankingKanojoTask != null) {
			this.rankingKanojoTask.cancel(true);
			this.rankingKanojoTask = null;
		}
	}

	private void executeKanojoTask(KanojoTask task) {
		if (task != null) {
			if (!task.isLoading() && !isLoading) {
				task.execute();
			}
		}
    }

	static class KanojoTask extends AsyncTask<Void, Void, Response<?>> {
		private final WeakReference<KanojosActivity> activityRef;
		final StatusHolder mList;
        private Exception mReason = null;

		KanojoTask(KanojosActivity activity, StatusHolder list) {
			super();
			activityRef = new WeakReference<>(activity);
			this.mList = list;
        }

        @Override
        public void onPreExecute() {
			KanojosActivity activity = activityRef.get();
			if (activity == null || activity.isFinishing()) {
				return;
			}

            this.mList.loading = true;
            if (this.mList.moreBtn != null) {
                this.mList.moreBtn.setLoading(true);
            }
        }

        @Override
        public Response<?> doInBackground(Void... params) {
            try {
	            KanojosActivity activity = activityRef.get();
	            if (activity == null || activity.isFinishing()) {
		            return null;
	            }

	            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo();
	            if (mList == null) {
		            throw new BarcodeKanojoException("process:StatusHolder is null!");
	            }
	            switch (mList.what) {
		            case StatusHolder.YOUR_KANOJOS:
			            return barcodeKanojo.my_current_kanojos(mList.index, mList.limit, activity.mSearchWord);
		            case StatusHolder.FRIENDS:
			            return barcodeKanojo.my_friend_kanojos(mList.index, mList.limit, activity.mSearchWord);
		            case StatusHolder.RANKING:
			            return barcodeKanojo.like_ranking(mList.index, mList.limit);
		            default:
			            return null;
	            }
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        public void onPostExecute(Response<?> response) {
	        KanojosActivity activity = activityRef.get();
			if (activity == null || activity.isFinishing()) {
				return;
			}

			if (mReason != null) {
				Toast.makeText(activity, mReason.getMessage(), Toast.LENGTH_LONG).show();
			}

            SearchResult res;
            try {
                switch (response.getCode()) {
					case Response.CODE_SUCCESS:
                        if (this.mList != null) {
							if (activity.isSearch && (res = (SearchResult) response.get(SearchResult.class)) != null) {
                                if (this.mList.txtNumber != null) {
                                    this.mList.txtNumber.setText(String.valueOf(res.getHit_count()));
                                }
                                this.mList.MAX = res.getHit_count();
                            }
                            ModelList<Kanojo> kanojoList = response.getKanojoList();
                            if (kanojoList != null) {
                                int size = kanojoList.size();
                                if (this.mList.index == 0) {
                                    if (size < this.mList.limit) {
										activity.mAdapter.removeFooter(this.mList.key);
                                    }
                                    this.mList.displayed = size;
                                    this.mList.adapter.clear();
                                    this.mList.adapter.notifyDataSetInvalidated();
                                    this.mList.adapter.addKanojosModelList(kanojoList);
                                } else {
                                    this.mList.displayed += size;
                                    this.mList.adapter.addKanojosModelList(kanojoList);
                                    if (size < this.mList.limit) {
										activity.mAdapter.removeFooter(this.mList.key);
                                    }
                                }
                                if (this.mList.displayed >= this.mList.MAX) {
									activity.mAdapter.removeFooter(this.mList.key);
                                }
                            }
                            break;
                        }
                        break;
					case Response.CODE_ERROR_UNAUTHORIZED:
						activity.resetKanojoTasks();
                        break;
                }
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.moreBtn != null) {
                        this.mList.moreBtn.setLoading(false);
                    }
                }
            } catch (Exception e) {
	            activity.resetKanojoTasks();
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.moreBtn != null) {
                        this.mList.moreBtn.setLoading(false);
                    }
                }
            } catch (Throwable th) {
                if (this.mList != null) {
                    this.mList.loading = false;
                    if (this.mList.moreBtn != null) {
                        this.mList.moreBtn.setLoading(false);
                    }
                }
                throw th;
            }
        }

        @Override
        protected void onCancelled() {
	        KanojosActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            if (this.mList != null) {
                this.mList.loading = false;
                if (this.mList.moreBtn != null) {
                    this.mList.moreBtn.setLoading(false);
                }
            }
        }

	    boolean isLoading() {
		    if (getStatus() == AsyncTask.Status.FINISHED || mList.moreBtn == null) {
			    return false;
		    }
		    if (mList.loading) {
			    return true;
		    }
		    mList.moreBtn.setLoading(false);
		    return false;
	    }
    }

    static class StatusHolder {
	    public static final int YOUR_KANOJOS = 0;
        public static final int FRIENDS = 1;
        public static final int RANKING = 2;

        int MAX = 100;
        KanojoAdapter adapter;
        int displayed = 0;
        int index = 0;
        String key;
        int limit = DEFAULT_LIMIT;
        boolean loading = false;
        MoreBtnView moreBtn;
        TextView txtNumber;
        int what;
    }

    protected void executeLogInTask() {
        if (this.mLogInTask == null || this.mLogInTask.getStatus() == AsyncTask.Status.FINISHED || this.mLogInTask.cancel(true) || this.mLogInTask.isCancelled()) {
            this.mLogInTask = (LogInTask) new LogInTask(this).execute();
        } else {
            Toast.makeText(this, "ttttttt", Toast.LENGTH_SHORT).show();
        }
    }

    private static class LogInTask extends AsyncTask<Void, Void, Response<?>> {
		private final WeakReference<KanojosActivity> activityRef;
        private Exception mReason = null;

		LogInTask(KanojosActivity activity) {
			super();
			activityRef = new WeakReference<>(activity);
		}

        @Override
        public void onPreExecute() {
	        KanojosActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            activity.showProgressDialog(dialog -> activity.backTab(activity, DashboardActivity.class));
        }

        @Override
        public Response<?> doInBackground(Void... params) {
	        KanojosActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return null;
	        }

            try {
	            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) activity.getApplication()).getBarcodeKanojo();
	            Log.d(KanojosActivity.TAG, "login() cannot be used !!!");
	            barcodeKanojo.init_product_category_list();
	            return null;
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

        @Override
        public void onPostExecute(Response<?> response) {
	        KanojosActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

            if (response == null) {
                try {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                } catch (BarcodeKanojoException e) {
	                activity.dismissProgressDialog();
                } catch (Throwable th) {
	                activity.dismissProgressDialog();
                    throw th;
                }
            } else {
	            if (response.getCode() == Response.CODE_SUCCESS) {
		            activity.updateProfileView();
		            activity.executeListTask(true);
	            }
	            activity.dismissProgressDialog();
            }
        }

        @Override
        protected void onCancelled() {
	        KanojosActivity activity = activityRef.get();
	        if (activity == null || activity.isFinishing()) {
		        return;
	        }

	        activity.dismissProgressDialog();
        }
    }

    @Override
    protected void endCheckSession() {
        updateProfileView();
    }
}
