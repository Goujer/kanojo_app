package jp.co.cybird.barcodekanojoForGAM.activity.kanojo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import jp.co.cybird.barcodekanojoForGAM.BarcodeKanojoApp;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Inventory;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Purchase;
import jp.co.cybird.barcodekanojoForGAM.billing.util.PurchaseApi;
import jp.co.cybird.barcodekanojoForGAM.core.BarcodeKanojo;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Kanojo;
import jp.co.cybird.barcodekanojoForGAM.core.model.KanojoItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.LoveIncrement;
import jp.co.cybird.barcodekanojoForGAM.core.model.PurchaseItem;
import jp.co.cybird.barcodekanojoForGAM.core.model.Response;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;
import jp.co.cybird.barcodekanojoForGAM.view.CustomLoadingView;

public class KanojoItemDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final boolean DEBUG_PURCHASE = false;
    public static final int MODE_DATE = 1;
    public static final int MODE_EXTEND_DATE = 4;
    public static final int MODE_EXTEND_GIFT = 5;
    public static final int MODE_GIFT = 2;
    public static final int MODE_TICKET = 3;
    private static final String TAG = "KanojoItemDatailActivity";
    private Button btnOk;
    private ImageView imgView;
    private List<String> lstProductId;
    private BuyTicketTask mBuyTicketTask;
    private Inventory mInventory;
    private Kanojo mKanojo;
    private KanojoItem mKanojoItem;
    private PurchaseApi.OnPurchaseListener mListener;
    private boolean mLoadingDone = false;
    private CustomLoadingView mLoadingView;
    private LoveIncrement mLoveIncrement;
    private PurchaseApi mPurchaseAPI;
    private String mReceiptData;
    private RemoteResourceManager mRrm;
    final Handler mTaskEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            StatusHolder next = (StatusHolder) KanojoItemDetailActivity.this.getQueue().poll();
            if (next != null) {
                KanojoItemDetailActivity.this.executePurchaseTask(next);
            }
        }
    };
    private Queue<StatusHolder> mTaskQueue;
    private int mTransactionId;
    private User mUser;
    private int mode;
    private TextView txtDescription;
    private TextView txtTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanojo_item_detail);
        this.mRrm = ((BarcodeKanojoApp) getApplication()).getRemoteResourceManager();
        ((Button) findViewById(R.id.kanojo_item_detail_close)).setOnClickListener(this);
        this.txtTitle = (TextView) findViewById(R.id.kanojo_item_detail_title);
        this.imgView = (ImageView) findViewById(R.id.kanojo_item_detail_img);
        this.imgView.setVisibility(View.INVISIBLE);
        this.txtDescription = (TextView) findViewById(R.id.kanojo_item_detail_description);
        this.txtDescription.setVisibility(View.INVISIBLE);
        this.btnOk = (Button) findViewById(R.id.kanojo_item_detail_btn_01);
        this.btnOk.setVisibility(View.INVISIBLE);
        this.btnOk.setOnClickListener(this);
        Button btnCancel = (Button) findViewById(R.id.kanojo_item_detail_btn_02);
        btnCancel.setOnClickListener(this);
        this.mLoadingView = (CustomLoadingView) findViewById(R.id.loadingView);
        this.mUser = ((BarcodeKanojoApp) getApplication()).getBarcodeKanojo().getUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mKanojo = (Kanojo) bundle.get(BaseInterface.EXTRA_KANOJO);
            this.mKanojoItem = (KanojoItem) bundle.get(BaseInterface.EXTRA_KANOJO_ITEM);
            this.mode = bundle.getInt(BaseInterface.EXTRA_KANOJO_ITEM_MODE);
        }
        btnCancel.setVisibility(View.GONE);
        this.mPurchaseAPI = ((BarcodeKanojoApp) getApplication()).getPurchaseApi();
        this.mListener = new PurchaseApi.OnPurchaseListener() {
            public void onSetUpFailed(String message) {
                Log.d(TAG, "setUp Purchase failed " + message);
            }

            public void onSetUpDone(Inventory inventory) {
                KanojoItemDetailActivity.this.mInventory = inventory;
            }

            public void onPurchaseDone(Purchase purchase) {
                KanojoItemDetailActivity.this.mReceiptData = purchase.getOrderId();
                Log.d("Purchase", "start to consume item " + purchase.getSku());
                KanojoItemDetailActivity.this.mPurchaseAPI.consumeItem(purchase);
            }

            public void onPurchaseFailed(String message) {
                Log.d(TAG, "Error purchasing: " + message);
                KanojoItemDetailActivity.this.showNoticeDialog(message);
                KanojoItemDetailActivity.this.clearQueue();
            }

            public void onConsumeDone(Purchase purchase, String message) {
                if (!KanojoItemDetailActivity.this.isQueueEmpty()) {
                    KanojoItemDetailActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                }
            }

            public void onConsumeFail(Purchase purchase, String message) {
                Log.d(TAG, "Purchase onConsumeDone failed " + message);
                KanojoItemDetailActivity.this.showNoticeDialog(message);
                KanojoItemDetailActivity.this.clearQueue();
                KanojoItemDetailActivity.this.dismissProgressDialog();
            }
        };
        this.mPurchaseAPI.setListener(this.mListener);
    }

    protected void onResume() {
        super.onResume();
        if (this.mLoadingDone || !((this.mode == 4 || this.mode == 5) && this.mKanojoItem.getHas_units() == null)) {
            loadContent(0);
        } else {
            executeCheckPriceTask();
        }
    }

    public void loadContent(int btnTextRes) {
        if (this.mKanojoItem != null) {
            this.imgView.setVisibility(View.VISIBLE);
            this.txtDescription.setVisibility(View.VISIBLE);
            this.btnOk.setVisibility(View.VISIBLE);
            this.txtTitle.setText(this.mKanojoItem.getTitle());
            ImageCache.setImageAndRequest(this, this.imgView, this.mKanojoItem.getImage_url(), this.mRrm, R.drawable.common_noimage_product);
            if (this.mKanojoItem.isHas()) {
                this.txtDescription.setText(this.mKanojoItem.getDescription());
            } else {
                this.txtDescription.setText(String.valueOf(this.mKanojoItem.getPrice()) + "\n" + this.mKanojoItem.getDescription());
            }
        }
        if (this.mode == 3) {
            this.btnOk.setText(getResources().getString(R.string.kanojo_shop_buy));
        } else if (this.mode == 2 || this.mode == 1 || this.mKanojoItem.getHas_units() != null) {
            if (this.mode == 2 || this.mode == 5) {
                this.btnOk.setText(getResources().getString(R.string.item_detail_button_ok_text));
            } else if (this.mode == 1 || this.mode == 4) {
                this.btnOk.setText(getResources().getString(R.string.item_detail_date_button_ok_text));
            }
        }
        if (btnTextRes > 0) {
            this.btnOk.setText(btnTextRes);
        }
    }

    public void hideContent() {
        this.imgView.setVisibility(View.INVISIBLE);
        this.txtDescription.setVisibility(View.INVISIBLE);
        this.btnOk.setVisibility(View.INVISIBLE);
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_kanojo_item_detail, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    protected void onPause() {
        super.onPause();
        if (this.mBuyTicketTask != null) {
            this.mBuyTicketTask.cancel(true);
            this.mBuyTicketTask = null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mPurchaseAPI.getHelper() == null || !this.mPurchaseAPI.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1009 && resultCode == 207) {
                close();
            } else if (requestCode == 1106) {
                hideContent();
                this.mLoadingDone = false;
            }
        } else {
            Log.d(TAG, "resume from google play");
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kanojo_item_detail_close:
                if (this.mode == 3) {
                    setResult(BaseInterface.RESULT_BUY_TICKET_FAIL);
                }
                close();
                return;
            case R.id.kanojo_item_detail_btn_01:
                executePurchaseListTask();
                return;
            default:
                return;
        }
    }

    public void onDismiss(DialogInterface dialog, int code) {
        switch (code) {
            case 200:
                if (this.mode == 3) {
                    setResult(BaseInterface.RESULT_BUY_TICKET_SUCCESS);
                    close();
                    return;
                }
                Intent data = new Intent();
                data.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                data.putExtra(BaseInterface.EXTRA_LOVE_INCREMENT, this.mLoveIncrement);
                setResult(BaseInterface.RESULT_KANOJO_ITEM_USED, data);
                close();
                return;
            case 403:
                if (this.mode != 3) {
                    Intent data2 = new Intent();
                    data2.putExtra(BaseInterface.EXTRA_KANOJO, this.mKanojo);
                    setResult(BaseInterface.RESULT_KANOJO_ITEM_USED, data2);
                    close();
                    return;
                }
                dismissProgressDialog();
                clearQueue();
                return;
            default:
                return;
        }
    }

    private boolean isLoading(StatusHolder status) {
        if (status.loading) {
            return true;
        }
        return false;
    }

    static class StatusHolder {
        public static final int CHECK_TICKET_TASK = 3;
        public static final int COMPLETE_PURCHASE_TASK = 2;
        public static final int GET_TRANSACTION_ID_TASK = 0;
        public static final int KANOJOITEM_TASK = 4;
        public static final int REQUEST_PURCHASE_TASK = 1;
        int key;
        boolean loading = false;

        StatusHolder() {
        }
    }

    private Queue<StatusHolder> getQueue() {
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

    private synchronized void executeCheckPriceTask() {
        clearQueue();
        StatusHolder mCheckTicketHolder = new StatusHolder();
        mCheckTicketHolder.key = 3;
        getQueue().offer(mCheckTicketHolder);
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private synchronized void executePurchaseListTask() {
        clearQueue();
        StatusHolder mCheckTicketHolder = new StatusHolder();
        mCheckTicketHolder.key = 3;
        StatusHolder mKanojoHolder = new StatusHolder();
        mKanojoHolder.key = 4;
        StatusHolder mGetTransactionHolder = new StatusHolder();
        mGetTransactionHolder.key = 0;
        StatusHolder mRequestHolder = new StatusHolder();
        mRequestHolder.key = 1;
        StatusHolder mCompleteHolder = new StatusHolder();
        mCompleteHolder.key = 2;
        if (this.mode != 3) {
            if ((this.mode == 4 || this.mode == 5) && this.mKanojoItem.getHas_units() == null) {
                getQueue().offer(mCheckTicketHolder);
            }
            getQueue().offer(mKanojoHolder);
        } else {
            getQueue().offer(mGetTransactionHolder);
            getQueue().offer(mRequestHolder);
            getQueue().offer(mCompleteHolder);
        }
        this.mTaskEndHandler.sendEmptyMessage(0);
    }

    private void executePurchaseTask(StatusHolder list) {
        if (isLoading(list)) {
            Log.d(TAG, "task " + list.key + " is running ");
        } else if (list.key != 1) {
            this.mBuyTicketTask = new BuyTicketTask();
            this.mBuyTicketTask.setList(list);
            showProgressDialog();
            this.mBuyTicketTask.execute(new Void[0]);
        } else {
            dismissProgressDialog();
            try {
                this.mPurchaseAPI.BuyProduct(this, this.mKanojoItem.getItem_purchase_product_id(), String.valueOf(this.mKanojoItem.getItem_id()) + "-" + this.mTransactionId);
            } catch (Exception e) {
            }
        }
    }

    class BuyTicketTask extends AsyncTask<Void, Void, Response<?>> {
        private StatusHolder mList;
        private Exception mReason = null;

        BuyTicketTask() {
        }

        public void setList(StatusHolder list) {
            this.mList = list;
        }

        public void onPreExecute() {
            this.mList.loading = true;
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
            int code;
            try {
                if (this.mReason != null) {
                }
                if (response == null) {
                    throw new BarcodeKanojoException("response is null! \n" + this.mReason);
                }
                if (this.mList.key == 4) {
                    Kanojo kanojo = (Kanojo) response.get(Kanojo.class);
                    if (kanojo != null) {
                        KanojoItemDetailActivity.this.mKanojo = kanojo;
                    }
                    LoveIncrement loveIncrement = (LoveIncrement) response.get(LoveIncrement.class);
                    if (loveIncrement != null) {
                        KanojoItemDetailActivity.this.mLoveIncrement = loveIncrement;
                    }
                    KanojoItemDetailActivity.this.clearQueue();
                }
                if (KanojoItemDetailActivity.this.isQueueEmpty()) {
                    code = KanojoItemDetailActivity.this.getCodeAndShowAlert(response, this.mReason);
                } else {
                    code = response.getCode();
                }
                switch (code) {
                    case 200:
                        if (this.mList.key == 0) {
                            KanojoItemDetailActivity.this.mTransactionId = ((PurchaseItem) response.get(PurchaseItem.class)).getTransactiontId();
                        } else if (this.mList.key == 3) {
                            KanojoItemDetailActivity.this.loadContent(R.string.item_detail_buy_ticket_text);
                            if (!KanojoItemDetailActivity.this.mLoadingDone) {
                                KanojoItemDetailActivity.this.clearQueue();
                                KanojoItemDetailActivity.this.dismissProgressDialog();
                                KanojoItemDetailActivity.this.mLoadingDone = true;
                            }
                        }
                        if (!KanojoItemDetailActivity.this.isQueueEmpty()) {
                            KanojoItemDetailActivity.this.mTaskEndHandler.sendEmptyMessage(0);
                            return;
                        }
                        return;
                    case Response.CODE_ERROR_NOT_ENOUGH_TICKET:
                        if (this.mList.key == 3) {
                            if (!KanojoItemDetailActivity.this.mLoadingDone) {
                                KanojoItemDetailActivity.this.mLoadingDone = true;
                                KanojoItemDetailActivity.this.loadContent(R.string.item_detail_go_to_ticket_screen_text);
                            } else {
                                Intent ticket = new Intent(KanojoItemDetailActivity.this, KanojoItemsActivity.class);
                                if (KanojoItemDetailActivity.this.mKanojo != null) {
                                    ticket.putExtra(BaseInterface.EXTRA_KANOJO, KanojoItemDetailActivity.this.mKanojo);
                                }
                                KanojoItem item = new KanojoItem(3);
                                item.setTitle(KanojoItemDetailActivity.this.getResources().getString(R.string.kanojo_items_store));
                                ticket.putExtra(BaseInterface.EXTRA_KANOJO_ITEM, item);
                                ticket.putExtra(BaseInterface.EXTRA_KANOJO_ITEM_MODE, 3);
                                ticket.putExtra(BaseInterface.EXTRA_REQUEST_CODE, BaseInterface.REQUEST_BUY_TICKET);
                                KanojoItemDetailActivity.this.startActivityForResult(ticket, BaseInterface.REQUEST_BUY_TICKET);
                            }
                            KanojoItemDetailActivity.this.clearQueue();
                            KanojoItemDetailActivity.this.dismissProgressDialog();
                            return;
                        }
                        return;
                    case 400:
                    case 401:
                    case 404:
                    case 500:
                    case 503:
                        return;
                    case 403:
                        if (this.mList.key == 2) {
                            KanojoItemDetailActivity.this.dismissProgressDialog();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } catch (BarcodeKanojoException e) {
                KanojoItemDetailActivity.this.clearQueue();
                KanojoItemDetailActivity.this.dismissProgressDialog();
                KanojoItemDetailActivity.this.showToast(KanojoItemDetailActivity.this.getString(R.string.error_internet));
            }
        }

        protected void onCancelled() {
            KanojoItemDetailActivity.this.dismissProgressDialog();
        }

        Response<?> process(StatusHolder list) throws BarcodeKanojoException, IllegalStateException, IOException {
            BarcodeKanojo barcodeKanojo = ((BarcodeKanojoApp) KanojoItemDetailActivity.this.getApplication()).getBarcodeKanojo();
            if (list == null) {
                throw new BarcodeKanojoException("process:StatusHolder is null!");
            }
            switch (list.key) {
                case 0:
                    return barcodeKanojo.android_get_transaction_id(KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                case 2:
                    return barcodeKanojo.android_verify_purchased(KanojoItemDetailActivity.this.mKanojoItem.getItem_id(), KanojoItemDetailActivity.this.mTransactionId, KanojoItemDetailActivity.this.mReceiptData);
                case 3:
                    return barcodeKanojo.android_check_ticket(KanojoItemDetailActivity.this.getPriceFromString(KanojoItemDetailActivity.this.mKanojoItem.getPrice()), KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                case 4:
                    switch (KanojoItemDetailActivity.this.mode) {
                        case 1:
                            if (KanojoItemDetailActivity.this.mKanojo == null || KanojoItemDetailActivity.this.mKanojoItem == null) {
                                return null;
                            }
                            return barcodeKanojo.do_date(KanojoItemDetailActivity.this.mKanojo.getId(), KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                        case 2:
                            if (KanojoItemDetailActivity.this.mKanojo == null || KanojoItemDetailActivity.this.mKanojoItem == null) {
                                return null;
                            }
                            return barcodeKanojo.do_gift(KanojoItemDetailActivity.this.mKanojo.getId(), KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                        case 3:
                            if (KanojoItemDetailActivity.this.mKanojo == null || KanojoItemDetailActivity.this.mKanojoItem == null) {
                                return null;
                            }
                            return barcodeKanojo.do_ticket(KanojoItemDetailActivity.this.mKanojoItem.getItem_id(), KanojoItemDetailActivity.this.getPriceFromString(KanojoItemDetailActivity.this.mKanojoItem.getPrice()));
                        case 4:
                            if (KanojoItemDetailActivity.this.mKanojo == null || KanojoItemDetailActivity.this.mKanojoItem == null) {
                                return null;
                            }
                            if (KanojoItemDetailActivity.this.mKanojoItem.getHas_units() == null) {
                                return barcodeKanojo.do_ticket(KanojoItemDetailActivity.this.mKanojoItem.getItem_id(), KanojoItemDetailActivity.this.getPriceFromString(KanojoItemDetailActivity.this.mKanojoItem.getPrice()));
                            }
                            return barcodeKanojo.do_extend_date(KanojoItemDetailActivity.this.mKanojo.getId(), KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                        case 5:
                            if (KanojoItemDetailActivity.this.mKanojo == null || KanojoItemDetailActivity.this.mKanojoItem == null) {
                                return null;
                            }
                            if (KanojoItemDetailActivity.this.mKanojoItem.getHas_units() == null) {
                                return barcodeKanojo.do_ticket(KanojoItemDetailActivity.this.mKanojoItem.getItem_id(), KanojoItemDetailActivity.this.getPriceFromString(KanojoItemDetailActivity.this.mKanojoItem.getPrice()));
                            }
                            return barcodeKanojo.do_extend_gift(KanojoItemDetailActivity.this.mKanojo.getId(), KanojoItemDetailActivity.this.mKanojoItem.getItem_id());
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        }
    }

    void nextScreen(StatusHolder list) {
        int i = list.key;
        dismissProgressDialog();
    }

    public int getPriceFromString(String price) {
        return new Scanner(price).useDelimiter("[^0-9]+").nextInt();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mLoadingView.isShow()) {
            if (this.mode == 3) {
                setResult(BaseInterface.RESULT_BUY_TICKET_FAIL);
            }
            close();
            return super.onKeyDown(keyCode, event);
        }
        this.mLoadingView.setMessage(getString(R.string.requesting_cant_cancel));
        return true;
    }

    public ProgressDialog showProgressDialog() {
        this.mLoadingView.show();
        return new ProgressDialog(this);
    }

    protected void dismissProgressDialog() {
        this.mLoadingView.dismiss();
    }
}
