package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KanojoMessage implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<KanojoMessage> CREATOR = new Parcelable.Creator<KanojoMessage>() {
        public KanojoMessage createFromParcel(Parcel in) {
            return new KanojoMessage(in, (KanojoMessage) null);
        }

        public KanojoMessage[] newArray(int size) {
            return new KanojoMessage[size];
        }
    };
    public static final String TAG = "KanojoMessage";
    private String btn_text;
    private int kanojo_id;
    private String next_screen;
    private String text;
    private String url;

    public KanojoMessage() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.btn_text);
        dest.writeString(this.url);
        dest.writeString(this.next_screen);
    }

    private KanojoMessage(Parcel in) {
        this.text = in.readString();
        this.btn_text = in.readString();
        this.url = in.readString();
        this.next_screen = in.readString();
    }

    /* synthetic */ KanojoMessage(Parcel parcel, KanojoMessage kanojoMessage) {
        this(parcel);
    }

    public String getMessage() {
        return this.text;
    }

    public String getButtonText() {
        return this.btn_text;
    }

    public String getSiteURL() {
        return this.url;
    }

    public void setMessage(String value) {
        this.text = value;
    }

    public void setButtonText(String value) {
        this.btn_text = value;
    }

    public void setSiteURL(String value) {
        this.url = value;
    }

    public void setNextScreen(String value) {
        this.next_screen = value;
    }

    public String getNextScreen() {
        return this.next_screen;
    }

    public int getKanojo_id() {
        return this.kanojo_id;
    }

    public void setKanojo_id(int kanojo_id2) {
        this.kanojo_id = kanojo_id2;
    }
}
