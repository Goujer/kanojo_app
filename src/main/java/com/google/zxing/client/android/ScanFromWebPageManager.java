package com.google.zxing.client.android;

import android.net.Uri;
import com.google.zxing.Result;
import com.google.zxing.client.android.result.ResultHandler;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

final class ScanFromWebPageManager {
    private static final String CODE_PLACEHOLDER = "{CODE}";
    private static final String FORMAT_PLACEHOLDER = "{FORMAT}";
    private static final String META_PLACEHOLDER = "{META}";
    private static final String RAW_CODE_PLACEHOLDER = "{RAWCODE}";
    private static final String RAW_PARAM = "raw";
    private static final String RETURN_URL_PARAM = "ret";
    private static final String TYPE_PLACEHOLDER = "{TYPE}";
    private final boolean returnRaw;
    private final String returnUrlTemplate;

    ScanFromWebPageManager(Uri inputUri) {
        this.returnUrlTemplate = inputUri.getQueryParameter(RETURN_URL_PARAM);
        this.returnRaw = inputUri.getQueryParameter(RAW_PARAM) != null;
    }

    /* access modifiers changed from: package-private */
    public boolean isScanFromWebPage() {
        return this.returnUrlTemplate != null;
    }

    /* access modifiers changed from: package-private */
    public String buildReplyURL(Result rawResult, ResultHandler resultHandler) {
        return replace(META_PLACEHOLDER, String.valueOf(rawResult.getResultMetadata()), replace(TYPE_PLACEHOLDER, resultHandler.getType().toString(), replace(FORMAT_PLACEHOLDER, rawResult.getBarcodeFormat().toString(), replace(RAW_CODE_PLACEHOLDER, rawResult.getText(), replace(CODE_PLACEHOLDER, this.returnRaw ? rawResult.getText() : resultHandler.getDisplayContents(), this.returnUrlTemplate)))));
    }

    private static String replace(CharSequence placeholder, CharSequence with, String pattern) {
        CharSequence escapedWith;
        if (with == null) {
            escapedWith = "";
        } else {
            escapedWith = with;
        }
        try {
            escapedWith = URLEncoder.encode(escapedWith.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return pattern.replace(placeholder, escapedWith);
    }
}
