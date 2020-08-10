package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.result.URIParsedResult;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import jp.co.cybird.barcodekanojoForGAM.R;

final class URIResultInfoRetriever extends SupplementalInfoRetriever {
    private static final int MAX_REDIRECTS = 5;
    private final String redirectString;
    private final URIParsedResult result;

    URIResultInfoRetriever(TextView textView, URIParsedResult result2, HistoryManager historyManager, Context context) {
        super(textView, historyManager);
        this.redirectString = context.getString(R.string.msg_redirect);
        this.result = result2;
    }

    void retrieveSupplementalInfo() throws IOException {
        try {
            URI oldURI = new URI(this.result.getURI());
            URI newURI = HttpHelper.unredirect(oldURI);
            int count = 0;
            while (true) {
                int count2 = count;
                count = count2 + 1;
                if (count2 < 5 && !oldURI.equals(newURI)) {
                    append(this.result.getDisplayResult(), (String) null, new String[]{String.valueOf(this.redirectString) + " : " + newURI}, newURI.toString());
                    oldURI = newURI;
                    newURI = HttpHelper.unredirect(newURI);
                } else {
                    return;
                }
            }
        } catch (URISyntaxException e) {
        }
    }
}
