package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KanojoItem implements BarcodeKanojoModel, Parcelable {
    public static final Parcelable.Creator<KanojoItem> CREATOR = new Parcelable.Creator<KanojoItem>() {
        public KanojoItem createFromParcel(Parcel in) {
            return new KanojoItem(in, (KanojoItem) null);
        }

        public KanojoItem[] newArray(int size) {
            return new KanojoItem[size];
        }
    };
    public static final int DATE_ITEM_CLASS = 2;
    public static final int GIFT_ITEM_CLASS = 1;
    public static final String TAG = "KanojoItems";
    public static final int TICKET_ITEM_CLASS = 3;
    private boolean category;
    private String confirm_purchase_message;
    private String confirm_use_message;
    private String description;
    private boolean expand_flag;
    private String has_units;
    private String image_thumbnail_url;
    private String image_url;
    private int item_category_id;
    private int item_class;
    private int item_id;
    private String price;
    private String purchasable_level;
    private String purchase_product_id;
    private String title;

    public KanojoItem(int item_class2) {
        this.category = false;
        this.expand_flag = false;
        this.item_class = item_class2;
    }

    public int getItem_class() {
        return this.item_class;
    }

    public int getItem_category_id() {
        return this.item_category_id;
    }

    public void setItem_category_id(int itemCategoryId) {
        this.item_category_id = itemCategoryId;
    }

    public int getItem_id() {
        return this.item_id;
    }

    public void setItem_id(int itemId) {
        this.item_id = itemId;
    }

    public boolean isCategory() {
        return this.category;
    }

    public boolean isExpand_flag() {
        return this.expand_flag;
    }

    public void setExpand_flag(boolean expand_flag2) {
        this.expand_flag = expand_flag2;
    }

    public boolean isHas() {
        return this.has_units != null && !this.has_units.equals("");
    }

    public void setCategory(boolean category2) {
        this.category = category2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getImage_thumbnail_url() {
        return this.image_thumbnail_url;
    }

    public void setImage_thumbnail_url(String imageThumbnailUrl) {
        this.image_thumbnail_url = imageThumbnailUrl;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String imageUrl) {
        this.image_url = imageUrl;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getConfirm_purchase_message() {
        return this.confirm_purchase_message;
    }

    public void setConfirm_purchase_message(String confirmPurchaseMessage) {
        this.confirm_purchase_message = confirmPurchaseMessage;
    }

    public String getConfirm_use_message() {
        return this.confirm_use_message;
    }

    public void setConfirm_use_message(String confirmUseMessage) {
        this.confirm_use_message = confirmUseMessage;
    }

    public String getHas_units() {
        return this.has_units;
    }

    public void setHas_units(String hasUnits) {
        this.has_units = hasUnits;
    }

    public String getPurchasable_level() {
        return this.purchasable_level;
    }

    public void setPurchasable_level(String purchasable_level2) {
        this.purchasable_level = purchasable_level2;
    }

    public int describeContents() {
        return 0;
    }

    public String getItem_purchase_product_id() {
        return this.purchase_product_id;
    }

    public boolean hasItem() {
        try {
            String[] t = getTitle().split(" ");
            Integer.parseInt(t[t.length - 1].replace("(", "").replace(")", "").replace(" ", ""));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setItem_purchase_product_id(String purchaseProductId) {
        this.purchase_product_id = purchaseProductId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.item_category_id);
        dest.writeInt(this.item_id);
        dest.writeBooleanArray(new boolean[]{this.category});
        dest.writeInt(this.item_class);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.image_thumbnail_url);
        dest.writeString(this.image_url);
        dest.writeString(this.price);
        dest.writeString(this.confirm_purchase_message);
        dest.writeString(this.confirm_use_message);
        dest.writeString(this.has_units);
        dest.writeString(this.purchase_product_id);
    }

    private KanojoItem(Parcel in) {
        this.category = false;
        this.expand_flag = false;
        this.item_category_id = in.readInt();
        this.item_id = in.readInt();
        boolean[] b = new boolean[1];
        in.readBooleanArray(b);
        this.category = b[0];
        this.item_class = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.image_thumbnail_url = in.readString();
        this.image_url = in.readString();
        this.price = in.readString();
        this.confirm_purchase_message = in.readString();
        this.confirm_use_message = in.readString();
        this.has_units = in.readString();
        this.purchase_product_id = in.readString();
    }

    /* synthetic */ KanojoItem(Parcel parcel, KanojoItem kanojoItem) {
        this(parcel);
    }
}
