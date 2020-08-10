package jp.co.cybird.barcodekanojoForGAM.billing.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.billing.util.IabHelper;

public class PurchaseApi {

    static final int RC_REQUEST = 10001;
    private static final String TAG = "PurchaseAPI";
    private static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5udnu3LugSzmvoDiGY5lLISow6hBAkHvGdcAcrG4Rcx8AqUMEgl9jNkhJil0ZLyN33ii9g7D/LPI+Nt0UnanLNH3R6XUMlLiXUH8kVc15k12CGD2X4BXOGvvYoal6b2ej8O6nJlxh7kXlhNppaPaTJT+lNQw5/6l6gvLmH7byxpma8ILh9CftP+eY4UFxYvUviBuWNxkH+kJwlVrec2DW8QJ0ljEeEZpJDFZF7Yyrqhwib8y1DeIfvR0P4+WxheehZrAivz51E5fUkMjjOpp487zUriaOOxeJ7/7LmTck5hJAu6cGJtVNajClHjAee7yYiqhTKhMI8bZ90FH4hrQ3QIDAQAB";
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(PurchaseApi.TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (PurchaseApi.this.mHelper != null) {
                if (result.isSuccess()) {
                    Log.d(PurchaseApi.TAG, "Consumption successful. Provisioning.");
                    if (PurchaseApi.this.mListener != null) {
                        PurchaseApi.this.mListener.onConsumeDone(purchase, result.getMessage());
                    }
                } else if (PurchaseApi.this.mListener != null) {
                    PurchaseApi.this.mListener.onConsumeFail(purchase, result.getMessage());
                }
                Log.d(PurchaseApi.TAG, "End consumption flow.");
            }
        }
    };
    private Context mContext;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (PurchaseApi.this.mHelper != null) {
                if (result.isFailure()) {
                    if (PurchaseApi.this.mListener != null) {
                        PurchaseApi.this.mListener.onSetUpFailed(result.getMessage());
                    }
                } else if (PurchaseApi.this.mListener != null) {
                    PurchaseApi.this.mListener.onSetUpDone(inventory);
                }
            }
        }
    };
    private IabHelper mHelper;
    private OnPurchaseListener mListener;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(PurchaseApi.TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (PurchaseApi.this.mHelper != null) {
                if (result.isFailure()) {
                    if (PurchaseApi.this.mListener != null) {
                        PurchaseApi.this.mListener.onPurchaseFailed(result.getMessage());
                    }
                } else if (PurchaseApi.this.mListener != null) {
                    PurchaseApi.this.mListener.onPurchaseDone(purchase);
                }
            }
        }
    };

    public interface OnPurchaseListener {
        void onConsumeDone(Purchase purchase, String str);

        void onConsumeFail(Purchase purchase, String str);

        void onPurchaseDone(Purchase purchase);

        void onPurchaseFailed(String str);

        void onSetUpDone(Inventory inventory);

        void onSetUpFailed(String str);
    }

    public PurchaseApi(Context context) {
        this.mContext = context;
    }

    public void setListener(OnPurchaseListener listener) {
        this.mListener = listener;
    }

    public void setUpAndgetPrice(final List<String> lstProductId) {
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        this.mHelper = new IabHelper(this.mContext, base64EncodedPublicKey);
        this.mHelper.enableDebugLogging(false);
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(PurchaseApi.TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    if (PurchaseApi.this.mListener != null) {
                        PurchaseApi.this.mListener.onSetUpFailed(result.getMessage());
                    }
                } else if (PurchaseApi.this.mHelper != null) {
                    try {
                        PurchaseApi.this.mHelper.queryInventoryAsync(true, lstProductId, PurchaseApi.this.mGotInventoryListener);
                    } catch (Exception e) {
                        if (PurchaseApi.this.mListener != null) {
                            PurchaseApi.this.mListener.onSetUpFailed("Unknow Problem");
                        }
                    }
                }
            }
        });
    }

    public void BuyProduct(Activity activity, String ProductId, String payLoad) {
        this.mHelper.launchPurchaseFlow(activity, ProductId, RC_REQUEST, this.mPurchaseFinishedListener, payLoad);
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return this.mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    public void consumeItem(Purchase purchase) {
        Log.d(TAG, "Purchase is " + purchase.getSku() + ". Starting consumption.");
        this.mHelper.consumeAsync(purchase, this.mConsumeFinishedListener);
    }

    public static boolean isBillingAvailable(Context context) {
        if (context.getPackageManager().queryIntentServices(new Intent("com.android.vending.billing.InAppBillingService.BIND"), 0).size() > 0) {
            return true;
        }
        return false;
    }

    public IabHelper getHelper() {
        return this.mHelper;
    }

    public void disposeHelper() {
        this.mHelper.dispose();
        this.mHelper = null;
    }
}
