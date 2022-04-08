package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ScanHistory implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<ScanHistory> CREATOR = new Parcelable.Creator<ScanHistory>() {
        public ScanHistory createFromParcel(Parcel in) {
            return new ScanHistory(in, (ScanHistory) null);
        }

        public ScanHistory[] newArray(int size) {
            return new ScanHistory[size];
        }
    };
    public static final String TAG = "Scanned";
    private String barcode;
    private int friend_count;
    private int kanojo_count;
    private int total_count;

    public ScanHistory() {
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode2) {
        this.barcode = barcode2;
    }

    public int getTotal_count() {
        return this.total_count;
    }

    public void setTotal_count(int totalCount) {
        this.total_count = totalCount;
    }

    public int getKanojo_count() {
        return this.kanojo_count;
    }

    public void setKanojo_count(int kanojoCount) {
        this.kanojo_count = kanojoCount;
    }

    public int getFriend_count() {
        return this.friend_count;
    }

    public void setFriend_count(int friendCount) {
        this.friend_count = friendCount;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.barcode);
        dest.writeInt(this.total_count);
        dest.writeInt(this.kanojo_count);
        dest.writeInt(this.friend_count);
    }

    private ScanHistory(Parcel in) {
        this.barcode = in.readString();
        this.total_count = in.readInt();
        this.kanojo_count = in.readInt();
        this.friend_count = in.readInt();
    }

    /* synthetic */ ScanHistory(Parcel parcel, ScanHistory scanHistory) {
        this(parcel);
    }
}
