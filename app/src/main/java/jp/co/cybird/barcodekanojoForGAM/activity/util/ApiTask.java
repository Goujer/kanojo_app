package jp.co.cybird.barcodekanojoForGAM.activity.util;

import jp.co.cybird.barcodekanojoForGAM.core.model.Product;

public class ApiTask {
    public static final int API_SCAN = 1;
    public static final int API_UPDATE = 2;
    public static final int RESULT_NG = 0;
    public static final int RESULT_OK = 1;
    public static final String TAG = "ApiTask";
    public String barcode;
    public String companyNameTextId;
    public Product product;
    public String productCommentTextId;
    public String productNameTextId;
    public int result = 0;
    public int what;

    public ApiTask(int what2, String barcode2, Product product2) {
        this.what = what2;
        this.barcode = barcode2;
        this.product = product2;
    }
}
