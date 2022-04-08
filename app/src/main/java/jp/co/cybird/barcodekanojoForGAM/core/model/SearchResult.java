package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchResult implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in, (SearchResult) null);
        }

        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
    public static final String TAG = "SearchResult";
    private int hit_count;

    public SearchResult() {
    }

    public int getHit_count() {
        return this.hit_count;
    }

    public void setHit_count(int hitCount) {
        this.hit_count = hitCount;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hit_count);
    }

    private SearchResult(Parcel in) {
        this.hit_count = in.readInt();
    }

    /* synthetic */ SearchResult(Parcel parcel, SearchResult searchResult) {
        this(parcel);
    }
}
