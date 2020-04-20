package jp.co.cybird.barcodekanojoForGAM.billing.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.vending.billing.IInAppBillingService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;

public class IabHelper {
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final boolean DEBUG = false;
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    boolean mAsyncInProgress = false;
    String mAsyncOperation = "";
    Context mContext;
    boolean mDebugLog = true;
    String mDebugTag = "IabHelper";
    boolean mDisposed = false;
    OnIabPurchaseFinishedListener mPurchaseListener;
    String mPurchasingItemType;
    int mRequestCode;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    boolean mSetupDone = false;
    String mSignatureBase64 = null;
    boolean mSubscriptionsSupported = false;

    public interface OnConsumeFinishedListener {
        void onConsumeFinished(Purchase purchase, IabResult iabResult);
    }

    public interface OnConsumeMultiFinishedListener {
        void onConsumeMultiFinished(List<Purchase> list, List<IabResult> list2);
    }

    public interface OnIabPurchaseFinishedListener {
        void onIabPurchaseFinished(IabResult iabResult, Purchase purchase);
    }

    public interface OnIabSetupFinishedListener {
        void onIabSetupFinished(IabResult iabResult);
    }

    public interface QueryInventoryFinishedListener {
        void onQueryInventoryFinished(IabResult iabResult, Inventory inventory);
    }

    public IabHelper(Context ctx, String base64PublicKey) {
        this.mContext = ctx.getApplicationContext();
        this.mSignatureBase64 = base64PublicKey;
        logDebug("IAB helper created.");
    }

    public void enableDebugLogging(boolean enable, String tag) {
        checkNotDisposed();
        this.mDebugLog = enable;
        this.mDebugTag = tag;
    }

    public void enableDebugLogging(boolean enable) {
        checkNotDisposed();
        this.mDebugLog = enable;
    }

    public void startSetup(final OnIabSetupFinishedListener listener) {
        checkNotDisposed();
        if (this.mSetupDone) {
            throw new IllegalStateException("IAB helper is already set up.");
        }
        logDebug("Starting in-app billing setup.");
        this.mServiceConn = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                IabHelper.this.logDebug("Billing service disconnected.");
                IabHelper.this.mService = null;
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                if (!IabHelper.this.mDisposed) {
                    IabHelper.this.logDebug("Billing service connected.");
                    IabHelper.this.mService = IInAppBillingService.Stub.asInterface(service);
                    String packageName = IabHelper.this.mContext.getPackageName();
                    try {
                        IabHelper.this.logDebug("Checking for in-app billing 3 support.");
                        int response = IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_INAPP);
                        if (response != 0) {
                            if (listener != null) {
                                listener.onIabSetupFinished(new IabResult(response, "Error checking for billing v3 support."));
                            }
                            IabHelper.this.mSubscriptionsSupported = false;
                            return;
                        }
                        IabHelper.this.logDebug("In-app billing version 3 supported for " + packageName);
                        int response2 = IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_SUBS);
                        if (response2 == 0) {
                            IabHelper.this.logDebug("Subscriptions AVAILABLE.");
                            IabHelper.this.mSubscriptionsSupported = true;
                        } else {
                            IabHelper.this.logDebug("Subscriptions NOT AVAILABLE. Response: " + response2);
                        }
                        IabHelper.this.mSetupDone = true;
                        if (listener != null) {
                            listener.onIabSetupFinished(new IabResult(0, "Setup successful."));
                        }
                    } catch (RemoteException e) {
                        if (listener != null) {
                            listener.onIabSetupFinished(new IabResult(IabHelper.IABHELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."));
                        }
                        e.printStackTrace();
                    }
                }
            }
        };
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        if (!this.mContext.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
            this.mContext.bindService(serviceIntent, this.mServiceConn, Context.BIND_AUTO_CREATE);
        } else if (listener != null) {
            listener.onIabSetupFinished(new IabResult(3, "Billing service unavailable on device."));
        }
    }

    public void dispose() {
        logDebug("Disposing.");
        this.mSetupDone = false;
        if (this.mServiceConn != null) {
            logDebug("Unbinding from service.");
            if (this.mContext != null) {
                this.mContext.unbindService(this.mServiceConn);
            }
        }
        this.mDisposed = true;
        this.mContext = null;
        this.mServiceConn = null;
        this.mService = null;
        this.mPurchaseListener = null;
    }

    private void checkNotDisposed() {
        if (this.mDisposed) {
            throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
        }
    }

    public boolean subscriptionsSupported() {
        checkNotDisposed();
        return this.mSubscriptionsSupported;
    }

    public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener) {
        launchPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_INAPP, requestCode, listener, extraData);
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener) {
        launchSubscriptionPurchaseFlow(act, sku, requestCode, listener, "");
    }

    public void launchSubscriptionPurchaseFlow(Activity act, String sku, int requestCode, OnIabPurchaseFinishedListener listener, String extraData) {
        launchPurchaseFlow(act, sku, ITEM_TYPE_SUBS, requestCode, listener, extraData);
    }

    public void launchPurchaseFlow(Activity act, String sku, String itemType, int requestCode, OnIabPurchaseFinishedListener listener, String extraData) {
        checkNotDisposed();
        checkSetupDone("launchPurchaseFlow");
        flagStartAsync("launchPurchaseFlow");
        if (!itemType.equals(ITEM_TYPE_SUBS) || this.mSubscriptionsSupported) {
            try {
                logDebug("Constructing buy intent for " + sku + ", item type: " + itemType);
                Bundle buyIntentBundle = this.mService.getBuyIntent(3, this.mContext.getPackageName(), sku, itemType, extraData);
                int response = getResponseCodeFromBundle(buyIntentBundle);
                if (response != 0) {
                    logError("Unable to buy item, Error response: " + getResponseDesc(response));
                    flagEndAsync();
                    IabResult result = new IabResult(response, "Unable to buy item");
                    if (listener != null) {
                        listener.onIabPurchaseFinished(result, (Purchase) null);
                        return;
                    }
                    return;
                }
                logDebug("Launching buy intent for " + sku + ". Request code: " + requestCode);
                this.mRequestCode = requestCode;
                this.mPurchaseListener = listener;
                this.mPurchasingItemType = itemType;
                IntentSender intentSender = ((PendingIntent) buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT)).getIntentSender();
                Intent intent = new Intent();
                Integer num = 0;
                int intValue = num.intValue();
                Integer num2 = 0;
                int intValue2 = num2.intValue();
                Integer num3 = 0;
                act.startIntentSenderForResult(intentSender, requestCode, intent, intValue, intValue2, num3.intValue());
            } catch (IntentSender.SendIntentException e) {
                logError("SendIntentException while launching purchase flow for sku " + sku);
                e.printStackTrace();
                flagEndAsync();
                IabResult result2 = new IabResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
                if (listener != null) {
                    listener.onIabPurchaseFinished(result2, (Purchase) null);
                }
            } catch (RemoteException e2) {
                logError("RemoteException while launching purchase flow for sku " + sku);
                e2.printStackTrace();
                flagEndAsync();
                IabResult result3 = new IabResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
                if (listener != null) {
                    listener.onIabPurchaseFinished(result3, (Purchase) null);
                }
            }
        } else {
            IabResult r = new IabResult(IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE, "Subscriptions are not available.");
            flagEndAsync();
            if (listener != null) {
                listener.onIabPurchaseFinished(r, (Purchase) null);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0145  */
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != this.mRequestCode) {
            return false;
        }
        checkNotDisposed();
        checkSetupDone("handleActivityResult");
        flagEndAsync();
        if (data == null) {
            logError("Null data in IAB activity result.");
            IabResult result = new IabResult(IABHELPER_BAD_RESPONSE, "Null data in IAB result");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(result, (Purchase) null);
            }
            return true;
        }
        int responseCode = getResponseCodeFromIntent(data);
        String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
        if (resultCode == -1 && responseCode == 0) {
            logDebug("Successful resultcode from purchase activity.");
            logDebug("Purchase data: " + purchaseData);
            logDebug("Data signature: " + dataSignature);
            logDebug("Extras: " + data.getExtras());
            logDebug("Expected item type: " + this.mPurchasingItemType);
            if (purchaseData == null || dataSignature == null) {
                logError("BUG: either purchaseData or dataSignature is null.");
                logDebug("Extras: " + data.getExtras().toString());
                IabResult result2 = new IabResult(IABHELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature");
                if (this.mPurchaseListener != null) {
                    this.mPurchaseListener.onIabPurchaseFinished(result2, (Purchase) null);
                }
                return true;
            }
            try {
                Purchase purchase = new Purchase(this.mPurchasingItemType, purchaseData, dataSignature);
                try {
                    String sku = purchase.getSku();
                    if (!Security.verifyPurchase(this.mSignatureBase64, purchaseData, dataSignature)) {
                        logError("Purchase signature verification FAILED for sku " + sku);
                        IabResult result3 = new IabResult(IABHELPER_VERIFICATION_FAILED, "Signature verification failed for sku " + sku);
                        if (this.mPurchaseListener != null) {
                            this.mPurchaseListener.onIabPurchaseFinished(result3, purchase);
                        }
                        return true;
                    }
                    logDebug("Purchase signature successfully verified.");
                    if (this.mPurchaseListener != null) {
                        this.mPurchaseListener.onIabPurchaseFinished(new IabResult(0, "Success"), purchase);
                    }
                } catch (JSONException e) {
                    e = e;
                    Purchase purchase2 = purchase;
                    logError("Failed to parse purchase data.");
                    e.printStackTrace();
                    IabResult result4 = new IabResult(IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.");
                    if (this.mPurchaseListener != null) {
                        this.mPurchaseListener.onIabPurchaseFinished(result4, (Purchase) null);
                    }
                    return true;
                }
            } catch (JSONException e2) {
                e = e2;
                logError("Failed to parse purchase data.");
                e.printStackTrace();
                IabResult result42 = new IabResult(IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.");
                if (this.mPurchaseListener != null) {
                }
                return true;
            }
        } else if (resultCode == -1) {
            logDebug("Result code was OK but in-app billing response was not OK: " + getResponseDesc(responseCode));
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(new IabResult(responseCode, "Problem purchashing item."), (Purchase) null);
            }
        } else if (resultCode == 0) {
            logDebug("Purchase canceled - Response: " + getResponseDesc(responseCode));
            IabResult result5 = new IabResult(IABHELPER_USER_CANCELLED, "User canceled.");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(result5, (Purchase) null);
            }
        } else {
            logError("Purchase failed. Result code: " + Integer.toString(resultCode) + ". Response: " + getResponseDesc(responseCode));
            IabResult result6 = new IabResult(IABHELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response.");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(result6, (Purchase) null);
            }
        }
        return true;
    }

    public Inventory queryInventory(boolean querySkuDetails, List<String> moreSkus) throws IabException {
        return queryInventory(querySkuDetails, moreSkus, (List<String>) null);
    }

    public Inventory queryInventory(boolean querySkuDetails, List<String> moreItemSkus, List<String> list) throws IabException, NullPointerException {
        int r;
        checkNotDisposed();
        checkSetupDone("queryInventory");
        try {
            Inventory inv = new Inventory();
            int r2 = queryPurchases(inv, ITEM_TYPE_INAPP);
            if (r2 != 0) {
                throw new IabException(r2, "Error refreshing inventory (querying owned items).");
            }
            if (querySkuDetails) {
                int r3 = querySkuDetails(ITEM_TYPE_INAPP, inv, moreItemSkus);
                if (r3 != 0) {
                    throw new IabException(r3, "Error refreshing inventory (querying prices of items).");
                }
            }
            if (this.mSubscriptionsSupported) {
                int r4 = queryPurchases(inv, ITEM_TYPE_SUBS);
                if (r4 != 0) {
                    throw new IabException(r4, "Error refreshing inventory (querying owned subscriptions).");
                } else if (querySkuDetails && (r = querySkuDetails(ITEM_TYPE_SUBS, inv, moreItemSkus)) != 0) {
                    throw new IabException(r, "Error refreshing inventory (querying prices of subscriptions).");
                }
            }
            return inv;
        } catch (RemoteException e) {
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.", e);
        } catch (JSONException e2) {
            throw new IabException(IABHELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.", e2);
        }
    }

    public void queryInventoryAsync(boolean querySkuDetails, List<String> moreSkus, QueryInventoryFinishedListener listener) {
        final Handler handler = new Handler();
        checkNotDisposed();
        checkSetupDone("queryInventory");
        flagStartAsync("refresh inventory");
        final boolean z = querySkuDetails;
        final List<String> list = moreSkus;
        final QueryInventoryFinishedListener queryInventoryFinishedListener = listener;
        new Thread(new Runnable() {
            public void run() {
                IabResult result = new IabResult(0, "Inventory refresh successful.");
                Inventory inv = null;
                try {
                    inv = IabHelper.this.queryInventory(z, list);
                } catch (IabException ex) {
                    result = ex.getResult();
                }
                IabHelper.this.flagEndAsync();
                final IabResult result_f = result;
                final Inventory inv_f = inv;
                if (!IabHelper.this.mDisposed && queryInventoryFinishedListener != null) {
                    Handler handler = handler;
                    final QueryInventoryFinishedListener queryInventoryFinishedListener = queryInventoryFinishedListener;
                    handler.post(new Runnable() {
                        public void run() {
                            queryInventoryFinishedListener.onQueryInventoryFinished(result_f, inv_f);
                        }
                    });
                }
            }
        }).start();
    }

    public void queryInventoryAsync(QueryInventoryFinishedListener listener) {
        queryInventoryAsync(true, (List<String>) null, listener);
    }

    public void queryInventoryAsync(boolean querySkuDetails, QueryInventoryFinishedListener listener) {
        queryInventoryAsync(querySkuDetails, (List<String>) null, listener);
    }

    /* access modifiers changed from: package-private */
    public void consume(Purchase itemInfo) throws IabException {
        checkNotDisposed();
        checkSetupDone("consume");
        if (!itemInfo.mItemType.equals(ITEM_TYPE_INAPP)) {
            throw new IabException((int) IABHELPER_INVALID_CONSUMPTION, "Items of type '" + itemInfo.mItemType + "' can't be consumed.");
        }
        try {
            String token = itemInfo.getToken();
            String sku = itemInfo.getSku();
            if (token == null || token.equals("")) {
                logError("Can't consume " + sku + ". No token.");
                throw new IabException((int) IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: " + sku + " " + itemInfo);
            }
            logDebug("Consuming sku: " + sku + ", token: " + token);
            int response = this.mService.consumePurchase(3, this.mContext.getPackageName(), token);
            if (response == 0) {
                logDebug("Successfully consumed sku: " + sku);
            } else {
                logDebug("Error consuming consuming sku " + sku + ". " + getResponseDesc(response));
                throw new IabException(response, "Error consuming sku " + sku);
            }
        } catch (RemoteException e) {
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
        }
    }

    public void consumeAsync(Purchase purchase, OnConsumeFinishedListener listener) {
        checkNotDisposed();
        checkSetupDone("consume");
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);
        consumeAsyncInternal(purchases, listener, (OnConsumeMultiFinishedListener) null);
    }

    public void consumeAsync(List<Purchase> purchases, OnConsumeMultiFinishedListener listener) {
        checkNotDisposed();
        checkSetupDone("consume");
        consumeAsyncInternal(purchases, (OnConsumeFinishedListener) null, listener);
    }

    public static String getResponseDesc(int code) {
        String[] iab_msgs = "0:OK/1:User Canceled/2:Unknown/3:Billing Unavailable/4:Item unavailable/5:Developer Error/6:Error/7:Item Already Owned/8:Item not owned".split("/");
        String[] iabhelper_msgs = "0:OK/-1001:Remote exception during initialization/-1002:Bad response received/-1003:Purchase signature verification failed/-1004:Send intent failed/-1005:User cancelled/-1006:Unknown purchase response/-1007:Missing token/-1008:Unknown error/-1009:Subscriptions not available/-1010:Invalid consumption attempt".split("/");
        if (code <= -1000) {
            int index = -1000 - code;
            if (index < 0 || index >= iabhelper_msgs.length) {
                return String.valueOf(String.valueOf(code)) + ":Unknown IAB Helper Error";
            }
            return iabhelper_msgs[index];
        } else if (code < 0 || code >= iab_msgs.length) {
            return String.valueOf(String.valueOf(code)) + ":Unknown";
        } else {
            return iab_msgs[code];
        }
    }

    /* access modifiers changed from: package-private */
    public void checkSetupDone(String operation) {
        if (!this.mSetupDone) {
            logError("Illegal state for operation (" + operation + "): IAB helper is not set up.");
            throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + operation);
        }
    }

    /* access modifiers changed from: package-private */
    public int getResponseCodeFromBundle(Bundle b) {
        Object o = b.get(RESPONSE_CODE);
        if (o == null) {
            logDebug("Bundle with null response code, assuming OK (known issue)");
            return 0;
        } else if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else {
            if (o instanceof Long) {
                return (int) ((Long) o).longValue();
            }
            logError("Unexpected type for bundle response code.");
            logError(o.getClass().getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }
    }

    /* access modifiers changed from: package-private */
    public int getResponseCodeFromIntent(Intent i) {
        Object o = i.getExtras().get(RESPONSE_CODE);
        if (o == null) {
            logError("Intent with no response code, assuming OK (known issue)");
            return 0;
        } else if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else {
            if (o instanceof Long) {
                return (int) ((Long) o).longValue();
            }
            logError("Unexpected type for intent response code.");
            logError(o.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + o.getClass().getName());
        }
    }

    /* access modifiers changed from: package-private */
    public void flagStartAsync(String operation) {
        if (this.mAsyncInProgress) {
            throw new IllegalStateException("Can't start async operation (" + operation + ") because another async operation(" + this.mAsyncOperation + ") is in progress.");
        }
        this.mAsyncOperation = operation;
        this.mAsyncInProgress = true;
        logDebug("Starting async operation: " + operation);
    }

    /* access modifiers changed from: package-private */
    public void flagEndAsync() {
        logDebug("Ending async operation: " + this.mAsyncOperation);
        this.mAsyncOperation = "";
        this.mAsyncInProgress = false;
    }

    /* access modifiers changed from: package-private */
    public int queryPurchases(Inventory inv, String itemType) throws JSONException, RemoteException, NullPointerException {
        int response;
        logDebug("Querying owned items, item type: " + itemType);
        boolean verificationFailed = false;
        String continueToken = null;
        do {
            logDebug("Calling getPurchases with continuation token: " + continueToken);
            Bundle ownedItems = this.mService.getPurchases(3, this.mContext.getPackageName(), itemType, continueToken);
            int response2 = getResponseCodeFromBundle(ownedItems);
            logDebug("Owned items response: " + String.valueOf(response2));
            if (response2 != 0) {
                logDebug("getPurchases() failed: " + getResponseDesc(response2));
                return response2;
            } else if (!ownedItems.containsKey(RESPONSE_INAPP_ITEM_LIST) || !ownedItems.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST) || !ownedItems.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                logError("Bundle returned from getPurchases() doesn't contain required fields.");
                return IABHELPER_BAD_RESPONSE;
            } else {
                ArrayList<String> ownedSkus = ownedItems.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
                ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = ownedItems.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
                for (int i = 0; i < purchaseDataList.size(); i++) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    if (Security.verifyPurchase(this.mSignatureBase64, purchaseData, signature)) {
                        logDebug("Sku is owned: " + sku);
                        Purchase purchase = new Purchase(itemType, purchaseData, signature);
                        if (TextUtils.isEmpty(purchase.getToken())) {
                            logWarn("BUG: empty/null token!");
                            logDebug("Purchase data: " + purchaseData);
                        }
                        inv.addPurchase(purchase);
                    } else {
                        logWarn("Purchase signature verification **FAILED**. Not adding item.");
                        logDebug("   Purchase data: " + purchaseData);
                        logDebug("   Signature: " + signature);
                        verificationFailed = true;
                    }
                }
                continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
                logDebug("Continuation token: " + continueToken);
            }
        } while (!TextUtils.isEmpty(continueToken));
        if (verificationFailed) {
            response = IABHELPER_VERIFICATION_FAILED;
        } else {
            response = 0;
        }
        return response;
    }

    /* access modifiers changed from: package-private */
    public int querySkuDetails(String itemType, Inventory inv, List<String> moreSkus) throws RemoteException, JSONException {
        logDebug("Querying SKU details.");
        ArrayList<String> skuList = new ArrayList<>();
        skuList.addAll(inv.getAllOwnedSkus(itemType));
        if (moreSkus != null) {
            for (String sku : moreSkus) {
                if (!skuList.contains(sku)) {
                    skuList.add(sku);
                }
            }
        }
        if (skuList.size() == 0) {
            logDebug("queryPrices: nothing to do because there are no SKUs.");
            return 0;
        }
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuList);
        Bundle skuDetails = this.mService.getSkuDetails(3, this.mContext.getPackageName(), itemType, querySkus);
        if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
            int response = getResponseCodeFromBundle(skuDetails);
            if (response != 0) {
                logDebug("getSkuDetails() failed: " + getResponseDesc(response));
                return response;
            }
            logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
            return IABHELPER_BAD_RESPONSE;
        }
        Iterator<String> it = skuDetails.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST).iterator();
        while (it.hasNext()) {
            SkuDetails d = new SkuDetails(itemType, it.next());
            logDebug("Got sku details: " + d);
            inv.addSkuDetails(d);
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void consumeAsyncInternal(List<Purchase> purchases, OnConsumeFinishedListener singleListener, OnConsumeMultiFinishedListener multiListener) {
        final Handler handler = new Handler();
        flagStartAsync("consume");
        final List<Purchase> list = purchases;
        final OnConsumeFinishedListener onConsumeFinishedListener = singleListener;
        final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener = multiListener;
        new Thread(new Runnable() {
            public void run() {
                final List<IabResult> results = new ArrayList<>();
                for (Purchase purchase : list) {
                    try {
                        IabHelper.this.consume(purchase);
                        results.add(new IabResult(0, "Successful consume of sku " + purchase.getSku()));
                    } catch (IabException ex) {
                        results.add(ex.getResult());
                    }
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && onConsumeFinishedListener != null) {
                    Handler handler = handler;
                    final OnConsumeFinishedListener onConsumeFinishedListener = onConsumeFinishedListener;
                    final List list = list;
                    handler.post(new Runnable() {
                        public void run() {
                            onConsumeFinishedListener.onConsumeFinished((Purchase) list.get(0), (IabResult) results.get(0));
                        }
                    });
                }
                if (!IabHelper.this.mDisposed && onConsumeMultiFinishedListener != null) {
                    Handler handler2 = handler;
                    final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener = onConsumeMultiFinishedListener;
                    final List list2 = list;
                    handler2.post(new Runnable() {
                        public void run() {
                            onConsumeMultiFinishedListener.onConsumeMultiFinished(list2, results);
                        }
                    });
                }
            }
        }).start();
    }

    /* access modifiers changed from: package-private */
    public void logDebug(String msg) {
        if (this.mDebugLog) {
            Log.d(this.mDebugTag, msg);
        }
    }

    /* access modifiers changed from: package-private */
    public void logError(String msg) {
        Log.e(this.mDebugTag, "In-app billing error: " + msg);
    }

    /* access modifiers changed from: package-private */
    public void logWarn(String msg) {
        Log.w(this.mDebugTag, "In-app billing warning: " + msg);
    }
}
