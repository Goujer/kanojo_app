package com.google.zxing.client.android.result;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

public final class ResultHandlerFactory {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$result$ParsedResultType;

    static /* synthetic */ int[] $SWITCH_TABLE$com$google$zxing$client$result$ParsedResultType() {
        int[] iArr = $SWITCH_TABLE$com$google$zxing$client$result$ParsedResultType;
        if (iArr == null) {
            iArr = new int[ParsedResultType.values().length];
            try {
                iArr[ParsedResultType.ADDRESSBOOK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ParsedResultType.CALENDAR.ordinal()] = 9;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ParsedResultType.EMAIL_ADDRESS.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ParsedResultType.GEO.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ParsedResultType.ISBN.ordinal()] = 11;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ParsedResultType.PRODUCT.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ParsedResultType.SMS.ordinal()] = 8;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ParsedResultType.TEL.ordinal()] = 7;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[ParsedResultType.TEXT.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[ParsedResultType.URI.ordinal()] = 4;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[ParsedResultType.WIFI.ordinal()] = 10;
            } catch (NoSuchFieldError e11) {
            }
            $SWITCH_TABLE$com$google$zxing$client$result$ParsedResultType = iArr;
        }
        return iArr;
    }

    private ResultHandlerFactory() {
    }

    public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
        ParsedResult result = parseResult(rawResult);
        switch ($SWITCH_TABLE$com$google$zxing$client$result$ParsedResultType()[result.getType().ordinal()]) {
            case 1:
                return new AddressBookResultHandler(activity, result);
            case 2:
                return new EmailAddressResultHandler(activity, result);
            case 3:
                return new ProductResultHandler(activity, result, rawResult);
            case 4:
                return new URIResultHandler(activity, result);
            case 6:
                return new GeoResultHandler(activity, result);
            case 7:
                return new TelResultHandler(activity, result);
            case 8:
                return new SMSResultHandler(activity, result);
            case 9:
                return new CalendarResultHandler(activity, result);
            case 10:
                return new WifiResultHandler(activity, result);
            case 11:
                return new ISBNResultHandler(activity, result, rawResult);
            default:
                return new TextResultHandler(activity, result, rawResult);
        }
    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }
}
