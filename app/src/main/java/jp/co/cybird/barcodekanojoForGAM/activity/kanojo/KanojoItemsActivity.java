package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.adapter.KanojoItemAdapter;
import jp.co.cybird.barcodekanojoForGAM.adapter.base.SeparatedListAdapter;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Inventory;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Purchase;
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi;
import com.goujer.barcodekanojo.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Alert;
import com.goujer.barcodekanojo.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItemCategory;
import jp.co.cybird.barcodekanojoForGAM.core.model.ModelList;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import com.goujer.barcodekanojo.core.model.User;

import com.goujer.barcodekanojo.activity.kanojo.KanojoItemDetailActivity;
import com.goujer.barcodekanojo.core.cache.DynamicImageCache;
import com.goujer.barcodekanojo.preferences.ApplicationSetting;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;

public class KanojoItemsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
	private static final String TAG = "KanojoItemsActivity";

    private static final boolean DEBUG_PURCHASE = false;
    public static final int MODE_DATE = 1;
	public static final int MODE_GIFT = 2;
	public static final int MODE_TICKET = 3;
    public static final int MODE_EXTEND_DATE = 4;
    public static final int MODE_EXTEND_GIFT = 5;
    public static final int MODE_PERMANENT_ITEM_GIFT = 6;
    public static final int MODE_PERMANENT_SUB_ITEM_GIFT = 7;

    public static final int TYPE_STORE = 1;
	public static final int TYPE_ITEM_LIST = 2;

    private Button buttonTabItemsList;
    private Button buttonTabItemsStore;
    private List<String> lstProductId;
    private SeparatedListAdapter mAdapter;
    private String mAlertMsgLst = "";
    private ConsumeTask mConsumeTask;
    private ModelList<KanojoItemCategory> mCurrentCategory;
    private Inventory mInventory;
    private Kanojo mKanojo;
    private KanojoItem mKanojoItem;
    private ListView mListView;
    private PurchaseApi.OnPurchaseListener mListener;
    private LoadItemsTask mLoadItemsTask;
    private boolean mLoadingFinshed = false;
    private CustomLoadingView mLoadingView;
    private PurchaseApi mPurchaseAPI;
    private int mRequestCode;
    private DynamicImageCache mDic;
    final Handler mTaskEndHandler = new Handler() {
    	@Override
        public void handleMessage(Message msg) {
            TicketHolder next = KanojoItemsActivity.this.getQueue().poll();
            if (next != null) {
                KanojoItemsActivity.this.showProgressDialog();
                KanojoItemsActivity.this.executeConsumeTicketTask(next);
            }
        }
    };
    private Queue<TicketHolder> mTaskQueue;
    private User mUser;
    private int mUserLevel = 0;
    private int mode;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_items);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            this.mKanojoItem = (KanojoItem) bundle.get(BaseInterface.EXTRA_KANOJO_ITEM);
            this.mode = bundle.getInt(BaseInterface.EXTRA_KANOJO_ITEM_MODE);
            this.mUserLevel = bundle.getInt(BaseInterface.EXTRA_LEVEL, 0);
            this.mRequestCode = bundle.getInt(BaseInterface.EXTRA_REQUEST_CODE, 0);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dWidth = displayMetrics.widthPixels;
        Resources r = getResources();
        this.mDic = ((BarcodeKanojoApp) getApplication()).getImageCache();
        this.mPurchaseAPI = ((BarcodeKanojoApp) getApplication()).getMPurchaseApi();
        Button btnClose = findViewById(R.id.kanojo_items_close);
        btnClose.setOnClickListener(this);
        btnClose.setText(this.mKanojo.getName());
        TextView txtTitle = findViewById(R.id.kanojo_items_title);
        this.mListView = findViewById(R.id.kanojo_items_list);
        this.mListView.setOnItemClickListener(this);
        this.mAdapter = new SeparatedListAdapter(this, R.layout.view_list_header);
        RelativeLayout mLayoutTab = findViewById(R.id.layoutTab);
        this.buttonTabItemsStore = findViewById(R.id.kanojo_tab_items_store);
        this.buttonTabItemsStore.getLayoutParams().width = (dWidth / 2) + 10;
        this.buttonTabItemsStore.setOnClickListener(this);
        this.buttonTabItemsList = findViewById(R.id.kanojo_tab_items_list);
        this.buttonTabItemsList.getLayoutParams().width = (dWidth / 2) + 10;
        this.buttonTabItemsList.setOnClickListener(this);
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        this.mLoadingView = findViewById(R.id.loadingView);
        if (this.mUser != null) {
            switch (this.mode) {
                case MODE_DATE:
                    txtTitle.setText(r.getString(R.string.kanojo_items_date));
                    mLayoutTab.setVisibility(View.VISIBLE);
                    onTabType(TYPE_STORE);
                    break;
                case MODE_GIFT:
                    txtTitle.setText(r.getString(R.string.kanojo_items_gift));
                    mLayoutTab.setVisibility(View.VISIBLE);
                    onTabType(TYPE_STORE);
                    break;
				case MODE_TICKET:
                    setAutoRefreshSession(false);
                    if (this.mKanojoItem == null) {
                        txtTitle.setText(r.getString(R.string.kanojo_items_store));
					} else {
                        txtTitle.setText(this.mKanojoItem.getTitle());
					}
					break;
				case MODE_EXTEND_DATE:
				case MODE_EXTEND_GIFT:
				case MODE_PERMANENT_ITEM_GIFT:
				case MODE_PERMANENT_SUB_ITEM_GIFT:
                    if (this.mKanojoItem != null) {
                        txtTitle.setText(this.mKanojoItem.getTitle());
                        break;
                    }
                    break;
            }
            this.lstProductId = new ArrayList();
            this.mListener = new PurchaseApi.OnPurchaseListener() {
                public void onSetUpFailed(String message) {
                    Log.d(TAG, "setUp Purchase failed " + message);
                    KanojoItemsActivity.this.showCustomNoticeDialog(KanojoItemsActivity.this.getString(R.string.purchase_setup_failed));
                }

                public void onSetUpDone(Inventory inventory) {
                    KanojoItemsActivity.this.showProgressDialog();
                    KanojoItemsActivity.this.mInventory = inventory;
                    if (KanojoItemsActivity.this.mCurrentCategory != null) {
						for (KanojoItemCategory category : KanojoItemsActivity.this.mCurrentCategory) {
							ModelList<KanojoItem> modelItem = category.getItems();
							for (KanojoItem item : modelItem) {
								try {
									item.setPrice(inventory.getSkuDetails(item.getItem_purchase_product_id()).getPrice());
								} catch (Exception e) {
									item.setPrice("0 US$");
								}
								KanojoItemsActivity.this.clearQueue();
								if (inventory.hasPurchase(item.getItem_purchase_product_id())) {
									TicketHolder mConsumetHolder = new TicketHolder();
									Purchase p = inventory.getPurchase(item.getItem_purchase_product_id());
									mConsumetHolder.key = item.getItem_purchase_product_id();
									mConsumetHolder.what = TicketHolder.CONSUME_PURCHASED_TASK;
									mConsumetHolder.orderId = p.getOrderId();
									String[] payLoadValue = p.getDeveloperPayload().split("-");
									if (payLoadValue.length == 2) {
										mConsumetHolder.store_item_id = Integer.parseInt(payLoadValue[0]);
										mConsumetHolder.google_transaction_id = Integer.parseInt(payLoadValue[1]);
									}
									TicketHolder mVerifyHolder = mConsumetHolder.clone();
									mVerifyHolder.what = TicketHolder.VERIFY_PURCHASED_TASK;
									KanojoItemsActivity.this.getQueue().offer(mVerifyHolder);
									KanojoItemsActivity.this.getQueue().offer(mConsumetHolder);
								}
							}
							boolean flag = false;
							if (category.getFlag() != null) {
								flag = true;
							}
							KanojoItemsActivity.this.addSection(category.getTitle(), flag, modelItem);
						}
                        if (!KanojoItemsActivity.this.isQueueEmpty()) {
                            TicketHolder mUpdateHodler = new TicketHolder();
                            mUpdateHodler.what = TicketHolder.UPDATE_DATA_TASK;
                            KanojoItemsActivity.this.getQueue().offer(mUpdateHodler);
                            KanojoItemsActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        }
                        KanojoItemsActivity.this.dismissProgressDialog();
                        KanojoItemsActivity.this.mListView.setAdapter(KanojoItemsActivity.this.mAdapter);
                    }
                }

                public void onPurchaseDone(Purchase purchase) {
                }

                public void onPurchaseFailed(String message) {
                }

                public void onConsumeDone(Purchase purchase, String message) {
                    if (!KanojoItemsActivity.this.isQueueEmpty()) {
                        KanojoItemsActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                        return;
                    }
                    KanojoItemsActivity.this.dismissProgressDialog();
                    KanojoItemsActivity.this.mListView.setAdapter(KanojoItemsActivity.this.mAdapter);
                }

                public void onConsumeFail(Purchase purchase, String message) {
                    if (purchase != null) {
                    }
                    KanojoItemsActivity.this.clearQueue();
                    KanojoItemsActivity.this.dismissProgressDialog();
                }
            };
        }
    }

    public View getClientView() {
        View layout = getLayoutInflater().inflate(R.layout.activity_kanojo_items, null);
        FrameLayout appLayoutRoot = new FrameLayout(this);
        appLayoutRoot.addView(layout);
        return appLayoutRoot;
    }

    protected void onPause() {
        super.onPause();
        if (this.mLoadItemsTask != null) {
            this.mLoadItemsTask.cancel(true);
            this.mLoadItemsTask = null;
        }
        if (isFinishing()) {
            this.mAdapter.removeObserver();
        }
    }

    public void onResume() {
        super.onResume();
        String uuid = new ApplicationSetting(this).getUUID();
        if (!PurchaseApi.isBillingAvailable(getApplicationContext()) && this.mode == MODE_TICKET) {
            showNoticeDialog(getString(R.string.no_support_billing_v3));
        } else if (!this.mLoadingFinshed) {
            executeLoadItemsTask();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroying helper.");
        if (this.mPurchaseAPI.getHelper() != null) {
            this.mPurchaseAPI.disposeHelper();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        KanojoItem item = (KanojoItem) this.mAdapter.getItem(position);
        if (item != null) {
            if (item.isCategory()) {
                int next_mode = this.mode;
                switch (this.mode) {
					case MODE_DATE:
					case MODE_EXTEND_DATE:
						next_mode = MODE_EXTEND_DATE;
                        break;
					case MODE_GIFT:
					case MODE_EXTEND_GIFT:
						next_mode = MODE_EXTEND_GIFT;
						if (this.type == TYPE_STORE && item.isExpand_flag()) {
							next_mode = MODE_PERMANENT_ITEM_GIFT;
                            break;
                        }
					case MODE_TICKET:
						next_mode = MODE_TICKET;
                        break;
					case MODE_PERMANENT_ITEM_GIFT:
						next_mode = MODE_PERMANENT_SUB_ITEM_GIFT;
                        break;
					case MODE_PERMANENT_SUB_ITEM_GIFT:
						next_mode = MODE_EXTEND_GIFT;
                        break;
                }
                Intent intent = new Intent(this, KanojoItemsActivity.class);
                if (this.mKanojo != null) {
                    intent.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                if (item != null) {
                    intent.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, item);
                }
                if (this.mUserLevel != 0) {
                    intent.putExtra(BaseInterface.EXTRA_LEVEL, this.mUserLevel);
                }
                intent.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, next_mode);
                if (next_mode == MODE_TICKET) {
                    startActivityForResult(intent, BaseInterface.REQUEST_KANOJO_TICKETS);
                } else {
                    startActivityForResult(intent, BaseInterface.REQUEST_KANOJO_ITEMS);
                }
            } else {
                int temp_mode = this.mode;
                if (temp_mode == MODE_PERMANENT_SUB_ITEM_GIFT) {
                    temp_mode = MODE_EXTEND_GIFT;
                }
                Intent intent2 = new Intent(this, KanojoItemDetailActivity.class);
                if (this.mKanojo != null) {
                    intent2.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                }
                if (item != null) {
                    intent2.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, item);
                }
                intent2.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, temp_mode);
                if (this.mRequestCode > 0) {
                    startActivityForResult(intent2, this.mRequestCode);
                } else {
                    startActivityForResult(intent2, BaseInterface.REQUEST_KANOJO_ITEMS);
                }
            }
        }
    }

    private void updateListItem(ModelList<KanojoItemCategory> list) {
        boolean flag;
        showLayoutNoitemView(list);
        if (list != null) {
            this.mAdapter.clear();
			for (KanojoItemCategory category : list) {
				ModelList<KanojoItem> modelItem = category.getItems();
				flag = category.getFlag() != null;
				if (category.getLevel() != null) {
					this.mUserLevel = Integer.parseInt(category.getLevel());
				}
				addSection(category.getTitle(), flag, modelItem);
			}
            this.mListView.setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void showLayoutNoitemView(ModelList<KanojoItemCategory> list) {
        LinearLayout layoutNoitem = (LinearLayout) findViewById(R.id.layout_no_item);
        if (list == null) {
            layoutNoitem.setVisibility(View.VISIBLE);
        } else if (list.size() == 0) {
            layoutNoitem.setVisibility(View.VISIBLE);
        } else {
            layoutNoitem.setVisibility(View.GONE);
        }
    }

    //private void updateListItem(Response<?> response) {
    //    updateListItem(response.getKanojoItemCategoryModelList());
    //}

    private void addSection(String title, boolean flag, ModelList<KanojoItem> list) {
        KanojoItemAdapter adapter = new KanojoItemAdapter(this, this.mDic);
        adapter.setUserLevel(this.mUserLevel);
        adapter.setModelList(list);
        adapter.setMode(this.mode);
        this.mAdapter.setUserLevel(this.mUserLevel);
        this.mAdapter.setMode(this.mode);
        this.mAdapter.addSection(title, flag, adapter);
    }

    private void onTabType(int selectType) {
        if (selectType == TYPE_STORE) {
            this.type = TYPE_STORE;
            this.buttonTabItemsStore.bringToFront();
            this.buttonTabItemsList.setEnabled(true);
            this.buttonTabItemsList.setBackgroundResource(R.drawable.button_list_item_tab);
            this.buttonTabItemsStore.setEnabled(false);
            this.buttonTabItemsStore.setBackgroundResource(R.drawable.button_store_tab_select);
            executeLoadItemsTask();
        } else if (selectType == TYPE_ITEM_LIST) {
            this.type = TYPE_ITEM_LIST;
            this.buttonTabItemsList.bringToFront();
            this.buttonTabItemsList.setEnabled(false);
            this.buttonTabItemsList.setBackgroundResource(R.drawable.button_list_item_tab_select);
            this.buttonTabItemsStore.setEnabled(true);
            this.buttonTabItemsStore.setBackgroundResource(R.drawable.button_store_tab);
            executeLoadItemsTask();
        }
    }

    public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.kanojo_items_close) { /*2131296322*/
			close();
		} else if (id == R.id.kanojo_tab_items_store) { /*2131296326*/
			onTabType(TYPE_STORE);
		} else if (id == R.id.kanojo_tab_items_list) { /*2131296327*/
			onTabType(TYPE_ITEM_LIST);
		}
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseInterface.RESULT_KANOJO_ITEM_USED) {
            setResult(BaseInterface.RESULT_KANOJO_ITEM_USED, data);
            close();
        }
        if (requestCode == BaseInterface.REQUEST_KANOJO_TICKETS || requestCode == BaseInterface.REQUEST_SYNC_SETTING) {
            this.mAdapter.clear();
            executeLoadItemsTask();
        }
        if (requestCode == BaseInterface.REQUEST_BUY_TICKET && resultCode == BaseInterface.RESULT_BUY_TICKET_SUCCESS) {
            close();
        }
    }

    private void executeLoadItemsTask() {
        if (this.mLoadItemsTask == null || this.mLoadItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mLoadItemsTask = (LoadItemsTask) new LoadItemsTask().execute(new Integer[0]);
        }
    }

    class LoadItemsTask extends AsyncTask<Integer, Void, Response<?>> {
        private Exception mReason = null;

        LoadItemsTask() {
        }

		@Override
        public void onPreExecute() {
            showProgressDialog();
            mLoadingFinshed = false;
        }

		@Override
        public Response<?> doInBackground(Integer... params) {
            try {
                BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo();
                switch (mode) {
					case MODE_DATE:
                        if (mKanojo != null) {
                            return barcodeKanojo.date_menu(mKanojo.getId(), type);
                        }
                        return null;
                    case MODE_GIFT:
                        if (mKanojo != null) {
                            return barcodeKanojo.gift_menu(mKanojo.getId(), type);
                        }
                        return null;
					case MODE_TICKET:
                        if (mKanojoItem != null) {
                            return barcodeKanojo.store_items(KanojoItem.TICKET_ITEM_CLASS, 0);
                        }
                        return null;
					case MODE_EXTEND_DATE:
					case MODE_EXTEND_GIFT:
                        if (mKanojoItem == null) {
                            return null;
                        }
                        if (mKanojoItem.hasItem()) {
                            return barcodeKanojo.has_items(mKanojoItem.getItem_class(), mKanojoItem.getItem_category_id());
                        }
                        return barcodeKanojo.store_items(mKanojoItem.getItem_class(), mKanojoItem.getItem_category_id());
					case MODE_PERMANENT_ITEM_GIFT:
                        if (KanojoItemsActivity.this.mKanojo != null) {
                            return barcodeKanojo.permanent_item_gift_menu(mKanojoItem.getItem_class(), mKanojoItem.getItem_category_id());
                        }
                        return null;
					case MODE_PERMANENT_SUB_ITEM_GIFT:
                        if (KanojoItemsActivity.this.mKanojo != null) {
                            return barcodeKanojo.permanent_sub_item_gift_menu(mKanojoItem.getItem_class(), mKanojoItem.getItem_category_id());
                        }
                        return null;
                    default:
                        return null;
                }
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

		@Override
        public void onPostExecute(Response<?> responseStore) {
            try {
				if (responseStore == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
				if (KanojoItemsActivity.this.getCodeAndShowAlert(responseStore, this.mReason) == Response.CODE_SUCCESS) {
					if (KanojoItemsActivity.this.mode != MODE_TICKET) {
						KanojoItemsActivity.this.updateListItem(responseStore.getKanojoItemCategoryModelList());
					} else {
						KanojoItemsActivity.this.mCurrentCategory = responseStore.getKanojoItemCategoryModelList();
						KanojoItemsActivity.this.getListProduct(KanojoItemsActivity.this.mCurrentCategory);
						KanojoItemsActivity.this.requestGetProductPrice();
					}
				}
            } catch (BarcodeKanojoException e) {
                KanojoItemsActivity.this.showToast(KanojoItemsActivity.this.getString(R.string.error_internet));
            } catch (Exception e2) {
                KanojoItemsActivity.this.showToast(KanojoItemsActivity.this.getString(R.string.error_internet));
            } finally {
                KanojoItemsActivity.this.mLoadingFinshed = true;
                KanojoItemsActivity.this.dismissProgressDialog();
            }
        }

        public void onCancelled() {
            KanojoItemsActivity.this.mLoadingFinshed = false;
            KanojoItemsActivity.this.dismissProgressDialog();
        }
    }

    public void requestGetProductPrice() {
        if (this.mPurchaseAPI == null) {
            this.mPurchaseAPI = ((BarcodeKanojoApp) getApplication()).getMPurchaseApi();
        }
        this.mPurchaseAPI.setListener(this.mListener);
        this.mPurchaseAPI.setUpAndgetPrice(this.lstProductId);
    }

    public List<String> getListProduct(ModelList<KanojoItemCategory> list) {
        if (list == null) {
            return null;
        }
		for (KanojoItemCategory kanojoItemCategory : list) {
			for (KanojoItem kanojoItem : kanojoItemCategory.getItems()) {
				this.lstProductId.add(kanojoItem.getItem_purchase_product_id());
			}
		}
        return this.lstProductId;
    }

    protected void showCustomNoticeDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.app_name).setIcon(R.drawable.icon_72).setMessage(message).setPositiveButton(R.string.common_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                KanojoItemsActivity.this.startActivityForResult(new Intent("android.settings.SYNC_SETTINGS"), BaseInterface.REQUEST_SYNC_SETTING);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.common_dialog_cancel, (dialog1, which) -> {
			dialog1.dismiss();
			KanojoItemsActivity.this.close();
		}).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void executeConsumeTicketTask(TicketHolder list) {
        if (isLoading(list)) {
            Log.d(TAG, "task " + list.key + " is running ");
        } else if (list.what == TicketHolder.CONSUME_PURCHASED_TASK) {
            consumeTicket(list.key);
        } else {
            this.mConsumeTask = new ConsumeTask();
            this.mConsumeTask.setList(list);
            showProgressDialog();
            this.mConsumeTask.execute();
        }
    }

    public void consumeTicket(String productId) {
        this.mPurchaseAPI.consumeItem(this.mInventory.getPurchase(productId));
    }

    private boolean isLoading(TicketHolder status) {
		return status.loading;
	}

    public class TicketHolder {
		static final int VERIFY_PURCHASED_TASK = 0;
        static final int CONSUME_PURCHASED_TASK = 1;
        static final int UPDATE_DATA_TASK = 2;
        int google_transaction_id = 0;
        String key;
        boolean loading = false;
        String orderId = "";
        int store_item_id = 0;
        int what;

        public TicketHolder() {
        }

		@Override
        public TicketHolder clone() {
            TicketHolder mNew = new TicketHolder();
            mNew.what = this.what;
            mNew.loading = this.loading;
            mNew.key = this.key;
            mNew.store_item_id = this.store_item_id;
            mNew.google_transaction_id = this.google_transaction_id;
            mNew.orderId = this.orderId;
            return mNew;
        }
    }

    private Queue<TicketHolder> getQueue() {
        if (this.mTaskQueue == null) {
            this.mTaskQueue = new LinkedList();
        }
        return this.mTaskQueue;
    }

    private synchronized void clearQueue() {
        getQueue().clear();
    }

    private synchronized boolean isQueueEmpty() {
        return this.mTaskQueue.isEmpty();
    }

    class ConsumeTask extends AsyncTask<Void, Void, Response<?>> {
        private TicketHolder mList;
        private Exception mReason = null;

        ConsumeTask() {
        }

        public void setList(TicketHolder list) {
            this.mList = list;
        }

		@Override
        public void onPreExecute() {
            this.mList.loading = true;
        }

		@Override
        public Response<?> doInBackground(Void... params) {
            try {
                return process(this.mList);
            } catch (Exception e) {
                this.mReason = e;
                return null;
            }
        }

		@Override
        public void onPostExecute(Response<?> response) {
            int code;
            try {
                if (this.mReason != null) {
                }
                if (response == null && this.mList.what == TicketHolder.UPDATE_DATA_TASK) {
                    code = Response.CODE_FINISHED_CONSUME_TICKET;
                    KanojoItemsActivity.this.mListView.setAdapter(KanojoItemsActivity.this.mAdapter);
                } else {
                    code = response.getCode();
                }
                switch (code) {
					case Response.CODE_SUCCESS:
                        Alert alert = response.getAlert();
                        KanojoItemsActivity kanojoItemsActivity = KanojoItemsActivity.this;
                        kanojoItemsActivity.mAlertMsgLst = kanojoItemsActivity.mAlertMsgLst + alert.getBody() + "\n";
                        if (!KanojoItemsActivity.this.isQueueEmpty()) {
                            KanojoItemsActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        }
                        return;
					case Response.CODE_ERROR_SERVER:
                        if (!KanojoItemsActivity.this.isQueueEmpty() && this.mList.what == TicketHolder.VERIFY_PURCHASED_TASK) {
                            KanojoItemsActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                KanojoItemsActivity.this.showAlertDialog(new Alert(KanojoItemsActivity.this.getResources().getString(R.string.error_internet)), new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
            }
            KanojoItemsActivity.this.showAlertDialog(new Alert(KanojoItemsActivity.this.getResources().getString(R.string.error_internet)), new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }

		@Override
        protected void onCancelled() {
        }

        Response<?> process(TicketHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) KanojoItemsActivity.this.getApplication()).getBarcodeKanojo();
            User user = barcodeKanojo.getUser();
            new ApplicationSetting(KanojoItemsActivity.this);
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.what) {
				case TicketHolder.VERIFY_PURCHASED_TASK:
                    return barcodeKanojo.android_verify_purchased(list.store_item_id, list.google_transaction_id, list.orderId);
				case TicketHolder.CONSUME_PURCHASED_TASK:
                    KanojoItemsActivity.this.consumeTicket(list.key);
                    return null;
                default:
                    return null;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK || !this.mLoadingView.isShow()) {
            return super.onKeyDown(keyCode, event);
        }
        this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    public void showProgressDialog() {
        this.mLoadingView.show();
    }

    protected void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }
}
