package jp.co.cybird.barcodekanojoForGAM.billing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.billing.util.IabHelper;
import jp.co.cybird.barcodekanojoForGAM.billing.util.IabResult;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Inventory;
import jp.co.cybird.barcodekanojoForGAM.billing.util.Purchase;

public class TestBillingActivity extends Activity {
    static final int RC_REQUEST = 10001;
    static final String SKU_GAS = "android.test.purchased";
    static final String SKU_PREMIUM = "premium";
    static final String TAG = "TrivialDrive";
    static final int TANK_MAX = 4;
    static int[] TANK_RES_IDS = {R.drawable.gas0, R.drawable.gas1, R.drawable.gas2, R.drawable.gas3, R.drawable.gas4};
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            int i = 4;
            Log.d(TestBillingActivity.TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (result.isSuccess()) {
                Log.d(TestBillingActivity.TAG, "Consumption successful. Provisioning.");
                TestBillingActivity testBillingActivity = TestBillingActivity.this;
                if (TestBillingActivity.this.mTank != 4) {
                    i = TestBillingActivity.this.mTank + 1;
                }
                testBillingActivity.mTank = i;
                TestBillingActivity.this.saveData();
                TestBillingActivity.this.alert("You filled 1/4 tank. Your tank is now " + String.valueOf(TestBillingActivity.this.mTank) + "/4 full!");
            } else {
                TestBillingActivity.this.complain("Error while consuming: " + result);
            }
            TestBillingActivity.this.updateUi();
            TestBillingActivity.this.setWaitScreen(false);
            Log.d(TestBillingActivity.TAG, "End consumption flow.");
        }
    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TestBillingActivity.TAG, "Query inventory finished.");
            if (result.isFailure()) {
                TestBillingActivity.this.complain("Failed to query inventory: " + result);
                return;
            }
            Log.d(TestBillingActivity.TAG, "Query inventory was successful.");
            Log.d("NguyenTT", "jp.co.cybird.dev1.zensho.bck.tickets30: " + inventory.getSkuDetails("jp.co.cybird.dev1.zensho.bck.tickets30").getPrice());
            TestBillingActivity.this.mIsPremium = inventory.hasPurchase(TestBillingActivity.SKU_PREMIUM);
            Log.d(TestBillingActivity.TAG, "User is " + (TestBillingActivity.this.mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            if (inventory.hasPurchase(TestBillingActivity.SKU_GAS)) {
                Log.d(TestBillingActivity.TAG, "We have gas. Consuming it.");
                TestBillingActivity.this.mHelper.consumeAsync(inventory.getPurchase(TestBillingActivity.SKU_GAS), TestBillingActivity.this.mConsumeFinishedListener);
                return;
            }
            TestBillingActivity.this.updateUi();
            TestBillingActivity.this.setWaitScreen(false);
            Log.d(TestBillingActivity.TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    IabHelper mHelper;
    boolean mIsPremium = false;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TestBillingActivity.TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                TestBillingActivity.this.complain("Error purchasing: " + result);
                TestBillingActivity.this.setWaitScreen(false);
                return;
            }
            Log.d(TestBillingActivity.TAG, "Purchase successful.");
            if (purchase.getSku().equals(TestBillingActivity.SKU_GAS)) {
                Log.d(TestBillingActivity.TAG, "Purchase is gas. Starting gas consumption.");
                TestBillingActivity.this.mHelper.consumeAsync(purchase, TestBillingActivity.this.mConsumeFinishedListener);
            } else if (purchase.getSku().equals(TestBillingActivity.SKU_PREMIUM)) {
                Log.d(TestBillingActivity.TAG, "Purchase is premium upgrade. Congratulating user.");
                TestBillingActivity.this.alert("Thank you for upgrading to premium!");
                TestBillingActivity.this.mIsPremium = true;
                TestBillingActivity.this.updateUi();
                TestBillingActivity.this.setWaitScreen(false);
            }
        }
    };
    int mTank;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        if ("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijnLNSrvGrJlzcxM5ZzGFlhfosK4+yjuFX8F3K67wruxiIjlGNuKpztbHZmYiU9xBZ3lHJ9Al+yXeBjDC8/4w9B4L+K2FfSQOaCRhxUSgh9OZKmxGNgny1tGN/MzbknUO14ClPEITbh7pMxKmKJKRmInm3HIMRKPBEwB7Xszj/UI6XQnoosnxvc4/+LyNUP9NkoxRJqW0JtT6TZu+kl4DYuNJPjJ8hVYZ21r3vlVLnuj0QwUtvnsbltTF597NHXsIHiq7F/kM2Yg9mpXUhLIcd04iIurx0HqfcghsdRojjgvhLVfODV7u00L3ZOVbTWgcjUtx47Lq1KBD2eHdnapwQIDAQAB".contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        Log.d(TAG, "Creating IAB helper.");
        this.mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijnLNSrvGrJlzcxM5ZzGFlhfosK4+yjuFX8F3K67wruxiIjlGNuKpztbHZmYiU9xBZ3lHJ9Al+yXeBjDC8/4w9B4L+K2FfSQOaCRhxUSgh9OZKmxGNgny1tGN/MzbknUO14ClPEITbh7pMxKmKJKRmInm3HIMRKPBEwB7Xszj/UI6XQnoosnxvc4/+LyNUP9NkoxRJqW0JtT6TZu+kl4DYuNJPjJ8hVYZ21r3vlVLnuj0QwUtvnsbltTF597NHXsIHiq7F/kM2Yg9mpXUhLIcd04iIurx0HqfcghsdRojjgvhLVfODV7u00L3ZOVbTWgcjUtx47Lq1KBD2eHdnapwQIDAQAB");
        this.mHelper.enableDebugLogging(true);
        Log.d(TAG, "Starting setup.");
        final List<String> moreSkus = new ArrayList<>();
        moreSkus.add("jp.co.cybird.dev1.zensho.bck.tickets10");
        moreSkus.add("jp.co.cybird.dev1.zensho.bck.tickets30");
        moreSkus.add("jp.co.cybird.dev1.zensho.bck.tickets50");
        moreSkus.add("jp.co.cybird.dev1.zensho.bck.tickets100");
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TestBillingActivity.TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    TestBillingActivity.this.complain("Problem setting up in-app billing: " + result);
                    return;
                }
                Log.d(TestBillingActivity.TAG, "Setup successful. Querying inventory.");
                TestBillingActivity.this.mHelper.queryInventoryAsync(true, moreSkus, TestBillingActivity.this.mGotInventoryListener);
            }
        });
    }

    public void onBuyGasButtonClicked(View arg0) {
        Log.d(TAG, "Buy gas button clicked.");
        if (this.mTank >= 4) {
            complain("Your tank is full. Drive around a bit!");
            return;
        }
        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas.");
        this.mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST, this.mPurchaseFinishedListener);
    }

    public void onUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);
        this.mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST, this.mPurchaseFinishedListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (!this.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    public void onDriveButtonClicked(View arg0) {
        Log.d(TAG, "Drive button clicked.");
        if (this.mTank <= 0) {
            alert("Oh, no! You are out of gas! Try buying some!");
            return;
        }
        this.mTank--;
        saveData();
        alert("Vroooom, you drove a few miles.");
        updateUi();
        Log.d(TAG, "Vrooom. Tank is now " + this.mTank);
    }

    @Override
    protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroying helper.");
		if (this.mHelper != null) {
			this.mHelper.dispose();
		}
		this.mHelper = null;
	}

    public void updateUi() {
        ((ImageView) findViewById(R.id.free_or_premium)).setImageResource(this.mIsPremium ? R.drawable.premium : R.drawable.free);
        findViewById(R.id.upgrade_button).setVisibility(this.mIsPremium ? 8 : 0);
        ((ImageView) findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[this.mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : this.mTank]);
    }

    void setWaitScreen(boolean set) {
        int i;
        int i2 = 0;
        View findViewById = findViewById(R.id.screen_main);
        if (set) {
            i = 8;
        } else {
            i = 0;
        }
        findViewById.setVisibility(i);
        View findViewById2 = findViewById(R.id.screen_wait);
        if (!set) {
            i2 = 8;
        }
        findViewById2.setVisibility(i2);
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", (DialogInterface.OnClickListener) null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData() {
        SharedPreferences.Editor spe = getPreferences(0).edit();
        spe.putInt("tank", this.mTank);
        spe.apply();
        Log.d(TAG, "Saved data: tank = " + this.mTank);
    }

    void loadData() {
        this.mTank = getPreferences(0).getInt("tank", 2);
        Log.d(TAG, "Loaded data: tank = " + this.mTank);
    }
}
