package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.maps.GeoPoint;
import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.StringUtil;

public class Scanned implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Scanned> CREATOR = new Parcelable.Creator<Scanned>() {
        public Scanned createFromParcel(Parcel in) {
            return new Scanned(in, (Scanned) null);
        }

        public Scanned[] newArray(int size) {
            return new Scanned[size];
        }
    };
    public static final String TAG = "Scanned";
    private String barcode;
    private String category;
    private int category_id;
    private String comment;
    private GeoPoint geo;
    private int id;
    private String location;
    private String name;
    private String nationality;
    private String product_image_url;
    private int user_id;

    public Scanned() {
        this.geo = new GeoPoint(0, 0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.barcode);
        dest.writeInt(this.user_id);
        dest.writeInt(GeoUtil.getLatitudeE6(this.geo));
        dest.writeInt(GeoUtil.getLongitudeE6(this.geo));
        dest.writeString(this.location);
        dest.writeString(this.name);
        dest.writeInt(this.category_id);
        dest.writeString(this.category);
        dest.writeString(this.comment);
        dest.writeString(this.product_image_url);
        dest.writeString(this.nationality);
    }

    private Scanned(Parcel in) {
        this.id = in.readInt();
        this.barcode = in.readString();
        this.user_id = in.readInt();
        this.geo = new GeoPoint(in.readInt(), in.readInt());
        this.location = in.readString();
        this.name = in.readString();
        this.category_id = in.readInt();
        this.category = in.readString();
        this.comment = in.readString();
        this.product_image_url = in.readString();
        this.nationality = in.readString();
    }

    /* synthetic */ Scanned(Parcel parcel, Scanned scanned) {
        this(parcel);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode2) {
        this.barcode = barcode2;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int userId) {
        this.user_id = userId;
    }

    public GeoPoint getGeo() {
        return this.geo;
    }

    public void setGeo(GeoPoint geo2) {
        this.geo = geo2;
    }

    public void setGeo(String geo2) {
        setGeo(GeoUtil.stringToGeo(geo2));
    }

    public String getGeoString() {
        return GeoUtil.geoToString(this.geo);
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(int categoryId) {
        this.category_id = categoryId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment2) {
        this.comment = StringUtil.comment(comment2);
    }

    public String getProduct_image_url() {
        return this.product_image_url;
    }

    public void setProduct_image_url(String productImageUrl) {
        this.product_image_url = productImageUrl;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality2) {
        this.nationality = nationality2;
    }
}
