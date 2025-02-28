package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

public class LoveIncrement implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<LoveIncrement> CREATOR = new Parcelable.Creator<LoveIncrement>() {
        public LoveIncrement createFromParcel(Parcel in) {
            return new LoveIncrement(in, (LoveIncrement) null);
        }

        public LoveIncrement[] newArray(int size) {
            return new LoveIncrement[size];
        }
    };

    private String alertShow;
    private String decrement_love;
    private String increase_love;

    public LoveIncrement() {
        this.increase_love = "0";
        this.decrement_love = "0";
        this.alertShow = "0";
    }

    public String getIncrease_love() {
        return this.increase_love;
    }

    public void setIncrease_love(String increase_love2) {
        this.increase_love = increase_love2;
    }

    public String getDecrement_love() {
        return this.decrement_love;
    }

    public void setDecrement_love(String decrement_love2) {
        this.decrement_love = decrement_love2;
    }

    public String getAlertShow() {
        return this.alertShow;
    }

    public void setAlertShow(String alertShow2) {
        this.alertShow = alertShow2;
    }

    public void clearValueAll() {
        this.increase_love = "0";
        this.decrement_love = "0";
        this.alertShow = "0";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.increase_love);
        dest.writeString(this.decrement_love);
        dest.writeString(this.alertShow);
    }

    private LoveIncrement(Parcel in) {
        this.increase_love = GreeDefs.BARCODE;
        this.decrement_love = GreeDefs.BARCODE;
        this.alertShow = GreeDefs.BARCODE;

        this.increase_love = in.readString();
        this.decrement_love = in.readString();
        this.alertShow = in.readString();
    }

    /* synthetic */ LoveIncrement(Parcel parcel, LoveIncrement loveIncrement) {
        this(parcel);
    }
}
