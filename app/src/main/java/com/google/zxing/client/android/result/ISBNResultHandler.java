package com.google.zxing.client.android.result;

import android.app.Activity;
import android.view.View;
import com.google.zxing.Result;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.goujer.barcodekanojo.R;

public final class ISBNResultHandler extends ResultHandler {
    private static final int[] buttons = {R.string.button_product_search, R.string.button_book_search, R.string.button_search_book_contents, R.string.button_custom_product_search};

    public ISBNResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        super(activity, result, rawResult);
        showGoogleShopperButton(new View.OnClickListener() {
            public void onClick(View view) {
                ISBNResultHandler.this.openGoogleShopper(((ISBNParsedResult) ISBNResultHandler.this.getResult()).getISBN());
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
        ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
        switch (index) {
            case 0:
                openProductSearch(isbnResult.getISBN());
                return;
            case 1:
                openBookSearch(isbnResult.getISBN());
                return;
            case 2:
                searchBookContents(isbnResult.getISBN());
                return;
            case 3:
                openURL(fillInCustomSearchURL(isbnResult.getISBN()));
                return;
            default:
                return;
        }
    }

    public int getDisplayTitle() {
        return R.string.result_isbn;
    }
}
