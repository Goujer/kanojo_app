package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KanojoMessage implements BarcodeKanojoModel, Parcelable {
	public static final String TAG = "KanojoMessage";
    public static final Parcelable.Creator<KanojoMessage> CREATOR = new Parcelable.Creator<KanojoMessage>() {
        public KanojoMessage createFromParcel(Parcel in) {
            return new KanojoMessage(in, (KanojoMessage) null);
        }

        public KanojoMessage[] newArray(int size) {
            return new KanojoMessage[size];
        }
    };
    private String[] text;

    public KanojoMessage() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.text);
    }

    private KanojoMessage(Parcel in) {
        this.text = in.createStringArray();
    }

    KanojoMessage(Parcel parcel, KanojoMessage kanojoMessage) {
        this(parcel);
    }

    public String[] getMessages() {
        return this.text;
    }

    public void setMessages(String[] value) {
        this.text = value;
    }
}
