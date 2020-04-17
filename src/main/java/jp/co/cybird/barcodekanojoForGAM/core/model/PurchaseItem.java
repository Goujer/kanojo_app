package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PurchaseItem implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<PurchaseItem> CREATOR = new Parcelable.Creator<PurchaseItem>() {
        public PurchaseItem createFromParcel(Parcel in) {
            return new PurchaseItem(in, (PurchaseItem) null);
        }

        public PurchaseItem[] newArray(int size) {
            return new PurchaseItem[size];
        }
    };
    public static final String TAG = "KanojoMessage";
    private int apple_transaction_id;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apple_transaction_id);
    }

    public PurchaseItem() {
    }

    private PurchaseItem(Parcel in) {
        this.apple_transaction_id = in.readInt();
    }

    /* synthetic */ PurchaseItem(Parcel parcel, PurchaseItem purchaseItem) {
        this(parcel);
    }

    public int getTransactiontId() {
        return this.apple_transaction_id;
    }

    public void setTransactionId(int value) {
        this.apple_transaction_id = value;
    }

    public int describeContents() {
        return 0;
    }
}
