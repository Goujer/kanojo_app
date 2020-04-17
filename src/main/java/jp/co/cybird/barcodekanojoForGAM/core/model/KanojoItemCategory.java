package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KanojoItemCategory implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<KanojoItemCategory> CREATOR = new Parcelable.Creator<KanojoItemCategory>() {
        public KanojoItemCategory createFromParcel(Parcel in) {
            return new KanojoItemCategory(in, (KanojoItemCategory) null);
        }

        public KanojoItemCategory[] newArray(int size) {
            return new KanojoItemCategory[size];
        }
    };
    public static final String TAG = "KanojoItemCategory";
    private String flag;
    private ModelList<KanojoItem> items;
    private String level;
    private String title;

    public KanojoItemCategory() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public ModelList<KanojoItem> getItems() {
        return this.items;
    }

    public void setItems(ModelList<KanojoItem> items2) {
        this.items = items2;
    }

    public void setFlag(String flag2) {
        this.flag = flag2;
    }

    public String getFlag() {
        return this.flag;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level2) {
        this.level = level2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    private KanojoItemCategory(Parcel in) {
    }

    /* synthetic */ KanojoItemCategory(Parcel parcel, KanojoItemCategory kanojoItemCategory) {
        this(parcel);
    }
}
