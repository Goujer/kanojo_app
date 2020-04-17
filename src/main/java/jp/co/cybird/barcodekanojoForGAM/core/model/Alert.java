package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
        public Alert createFromParcel(Parcel in) {
            return new Alert(in, (Alert) null);
        }

        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
    public static final String TAG = "Alert";
    private String body;
    private String title;

    public Alert() {
        this.title = "";
        this.body = "";
    }

    public Alert(String body2) {
        this.title = "";
        this.body = "";
        this.body = body2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body2) {
        this.body = body2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.body);
    }

    private Alert(Parcel in) {
        this.title = "";
        this.body = "";
        this.title = in.readString();
        this.body = in.readString();
    }

    /* synthetic */ Alert(Parcel parcel, Alert alert) {
        this(parcel);
    }
}
