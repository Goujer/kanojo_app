package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in, (Category) null);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    public static final String TAG = "Category";
    private int id;
    private String name;

    public Category() {
        this.name = "";
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    private Category(Parcel in) {
        this.name = "";
        this.id = in.readInt();
        this.name = in.readString();
    }

    /* synthetic */ Category(Parcel parcel, Category category) {
        this(parcel);
    }
}
