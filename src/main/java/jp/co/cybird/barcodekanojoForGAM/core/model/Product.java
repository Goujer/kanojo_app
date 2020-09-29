package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;
import jp.co.cybird.barcodekanojoForGAM.core.util.StringUtil;

public class Product implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in, (Product) null);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    public static final String TAG = "Product";
    private String barcode;
    private String category;
    private int category_id;
    private String comment;
    private String company_name;
    private String country;
    private LatLng geo;
    private String location;
    private String name;
    private String price;
    private String product;
    private String product_image_url;
    private int scan_count;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.barcode);
        dest.writeString(this.name);
        dest.writeInt(this.category_id);
        dest.writeString(this.category);
        dest.writeString(this.comment);
        dest.writeDouble(GeoUtil.getLatitudeE6(this.geo));
        dest.writeDouble(GeoUtil.getLongitudeE6(this.geo));
        dest.writeString(this.location);
        dest.writeString(this.product_image_url);
        dest.writeInt(this.scan_count);
        dest.writeString(this.company_name);
        dest.writeString(this.country);
        dest.writeString(this.price);
        dest.writeString(this.product);
    }

    private Product(Parcel in) {
        this.category_id = 1;
        this.barcode = in.readString();
        this.name = in.readString();
        this.category_id = in.readInt();
        this.category = in.readString();
        this.comment = in.readString();
        this.geo = new LatLng(in.readDouble(), in.readDouble());
        this.location = in.readString();
        this.product_image_url = in.readString();
        this.scan_count = in.readInt();
        this.company_name = in.readString();
        this.country = in.readString();
        this.price = in.readString();
        this.product = in.readString();
    }

	private Product(Parcel parcel, Product product2) {
        this(parcel);
    }

    public Product() {
        this.category_id = 1;
        this.geo = new LatLng(0.0, 0.0);
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode2) {
        this.barcode = barcode2;
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

    public LatLng getGeo() {
        return this.geo;
    }

    public void setGeo(LatLng geo2) {
        this.geo = geo2;
    }

    public void setGeo(String geo) {
        this.geo = GeoUtil.stringToGeo(geo);
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getProduct_image_url() {
        return this.product_image_url;
    }

    public void setProduct_image_url(String productImageUrl) {
        this.product_image_url = productImageUrl;
    }

    public int getScan_count() {
        return this.scan_count;
    }

    public void setScan_count(int scanCount) {
        this.scan_count = scanCount;
    }

    public String getCompany_name() {
        return this.company_name;
    }

    public void setCompany_name(String companyName) {
        this.company_name = companyName;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country2) {
        this.country = country2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product2) {
        this.product = product2;
    }
}
