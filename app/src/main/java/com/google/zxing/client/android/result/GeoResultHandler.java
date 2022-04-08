package com.google.zxing.client.android.result;

import android.app.Activity;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import jp.co.cybird.barcodekanojoForGAM.R;

public final class GeoResultHandler extends ResultHandler {
    private static final int[] buttons = {R.string.button_show_map, R.string.button_get_directions};

    public GeoResultHandler(Activity activity, ParsedResult result) {
        super(activity, result);
    }

    public int getButtonCount() {
        return buttons.length;
    }

    public int getButtonText(int index) {
        return buttons[index];
    }

    public void handleButtonPress(int index) {
        GeoParsedResult geoResult = (GeoParsedResult) getResult();
        switch (index) {
            case 0:
                openMap(geoResult.getGeoURI());
                return;
            case 1:
                getDirections(geoResult.getLatitude(), geoResult.getLongitude());
                return;
            default:
                return;
        }
    }

    public int getDisplayTitle() {
        return R.string.result_geo;
    }
}
