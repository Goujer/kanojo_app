package com.google.zxing.client.android.result;

import android.app.Activity;
import android.view.View;
import com.google.zxing.Result;
import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.goujer.barcodekanojo.R;

public final class ProductResultHandler extends ResultHandler {
    private static final int[] buttons = {R.string.button_product_search, R.string.button_web_search, R.string.button_custom_product_search};

    public ProductResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        super(activity, result, rawResult);
        showGoogleShopperButton(new View.OnClickListener() {
            public void onClick(View view) {
                ProductResultHandler.this.openGoogleShopper(((ProductParsedResult) ProductResultHandler.this.getResult()).getNormalizedProductID());
            }
        });
    }

    public int getButtonCount() {
        return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
    }

    public int getButtonText(int index) {
        return buttons[index];
    }

    public void handleButtonPress(int index) {
        String productID;
        ParsedResult rawResult = getResult();
        if (rawResult instanceof ProductParsedResult) {
            productID = ((ProductParsedResult) rawResult).getNormalizedProductID();
        } else if (rawResult instanceof ExpandedProductParsedResult) {
            productID = ((ExpandedProductParsedResult) rawResult).getRawText();
        } else {
            throw new IllegalArgumentException(rawResult.getClass().toString());
        }
        switch (index) {
            case 0:
                openProductSearch(productID);
                return;
            case 1:
                webSearch(productID);
                return;
            case 2:
                openURL(fillInCustomSearchURL(productID));
                return;
            default:
                return;
        }
    }

    public int getDisplayTitle() {
        return R.string.result_product;
    }
}
