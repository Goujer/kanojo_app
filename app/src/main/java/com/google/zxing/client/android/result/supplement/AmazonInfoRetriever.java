package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.history.HistoryManager;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

final class AmazonInfoRetriever extends SupplementalInfoRetriever {
    private final String country;
    private final String productID;
    private final String type;

    AmazonInfoRetriever(TextView textView, String type2, String productID2, HistoryManager historyManager, Context context) {
        super(textView, historyManager);
        String country2 = LocaleManager.getCountry(context);
        if (Intents.SearchBookContents.ISBN.equals(type2) && !Locale.US.getCountry().equals(country2)) {
            type2 = "EAN";
        }
        this.type = type2;
        this.productID = productID2;
        this.country = country2;
    }

    void retrieveSupplementalInfo() throws IOException {
        CharSequence contents = HttpHelper.downloadViaHttp("https://bsplus.srowen.com/ss?c=" + this.country + "&t=" + this.type + "&i=" + this.productID, HttpHelper.ContentType.XML);
        String detailPageURL = null;
        Collection<String> authors = new ArrayList<>();
        String title = null;
        String formattedPrice = null;
        boolean error = false;
        try {
            XmlPullParser xpp = buildParser(contents);
            boolean seenItem = false;
            boolean seenLowestNewPrice = false;
            int eventType = xpp.getEventType();
            while (true) {
                if (eventType == 1) {
                    break;
                }
                if (eventType == 2) {
                    String name = xpp.getName();
                    if ("Item".equals(name)) {
                        if (seenItem) {
                            break;
                        }
                        seenItem = true;
                    } else if ("DetailPageURL".equals(name)) {
                        assertTextNext(xpp);
                        detailPageURL = xpp.getText();
                    } else if ("Author".equals(name)) {
                        assertTextNext(xpp);
                        authors.add(xpp.getText());
                    } else if ("Title".equals(name)) {
                        assertTextNext(xpp);
                        title = xpp.getText();
                    } else if ("LowestNewPrice".equals(name)) {
                        seenLowestNewPrice = true;
                    } else if ("FormattedPrice".equals(name)) {
                        if (seenLowestNewPrice) {
                            assertTextNext(xpp);
                            formattedPrice = xpp.getText();
                            seenLowestNewPrice = false;
                        }
                    } else if ("Errors".equals(name)) {
                        error = true;
                        break;
                    }
                }
                eventType = xpp.next();
            }
            if (!error && detailPageURL != null) {
                Collection<String> newTexts = new ArrayList<>();
                maybeAddText(title, newTexts);
                maybeAddTextSeries(authors, newTexts);
                maybeAddText(formattedPrice, newTexts);
                append(this.productID, "Amazon", (String[]) newTexts.toArray(new String[newTexts.size()]), detailPageURL);
            }
        } catch (XmlPullParserException xppe) {
            throw new IOException(xppe.toString());
        }
    }

    private static void assertTextNext(XmlPullParser xpp) throws XmlPullParserException, IOException {
        if (xpp.next() != 4) {
            throw new IOException();
        }
    }

    private static XmlPullParser buildParser(CharSequence contents) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(contents.toString()));
        return xpp;
    }
}
