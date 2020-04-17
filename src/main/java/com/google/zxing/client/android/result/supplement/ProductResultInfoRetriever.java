package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.history.HistoryManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jp.co.cybird.barcodekanojoForGAM.R;

final class ProductResultInfoRetriever extends SupplementalInfoRetriever {
    private static final Pattern[] PRODUCT_NAME_PRICE_PATTERNS = {Pattern.compile(",event\\)\">([^<]+)</a></h3>.+<span class=psrp>([^<]+)</span>"), Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)")};
    private final Context context;
    private final String productID;
    private final String source;

    ProductResultInfoRetriever(TextView textView, String productID2, HistoryManager historyManager, Context context2) {
        super(textView, historyManager);
        this.productID = productID2;
        this.source = context2.getString(R.string.msg_google_product);
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public void retrieveSupplementalInfo() throws IOException {
        String uri = "http://www.google." + LocaleManager.getProductSearchCountryTLD(this.context) + "/m/products?ie=utf8&oe=utf8&scoring=p&source=zxing&q=" + URLEncoder.encode(this.productID, "UTF-8");
        CharSequence content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.HTML);
        for (Pattern p : PRODUCT_NAME_PRICE_PATTERNS) {
            Matcher matcher = p.matcher(content);
            if (matcher.find()) {
                append(this.productID, this.source, new String[]{unescapeHTML(matcher.group(1)), unescapeHTML(matcher.group(2))}, uri);
                return;
            }
        }
    }

    private static String unescapeHTML(String raw) {
        return Html.fromHtml(raw).toString();
    }
}
