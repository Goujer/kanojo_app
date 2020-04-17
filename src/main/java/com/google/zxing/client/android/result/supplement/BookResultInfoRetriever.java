package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.history.HistoryManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import jp.co.cybird.barcodekanojoForGAM.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

final class BookResultInfoRetriever extends SupplementalInfoRetriever {
    private final Context context;
    private final String isbn;
    private final String source;

    BookResultInfoRetriever(TextView textView, String isbn2, HistoryManager historyManager, Context context2) {
        super(textView, historyManager);
        this.isbn = isbn2;
        this.source = context2.getString(R.string.msg_google_books);
        this.context = context2;
    }

    /* access modifiers changed from: package-private */
    public void retrieveSupplementalInfo() throws IOException {
        JSONObject volumeInfo;
        String str;
        CharSequence contents = HttpHelper.downloadViaHttp("https://www.googleapis.com/books/v1/volumes?q=isbn:" + this.isbn, HttpHelper.ContentType.JSON);
        if (contents.length() != 0) {
            Collection<String> authors = null;
            try {
                JSONArray items = ((JSONObject) new JSONTokener(contents.toString()).nextValue()).optJSONArray("items");
                if (items != null && !items.isNull(0) && (volumeInfo = ((JSONObject) items.get(0)).getJSONObject("volumeInfo")) != null) {
                    String title = volumeInfo.optString("title");
                    String pages = volumeInfo.optString("pageCount");
                    JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                    if (authorsArray != null && !authorsArray.isNull(0)) {
                        Collection<String> authors2 = new ArrayList<>(authorsArray.length());
                        int i = 0;
                        while (i < authorsArray.length()) {
                            try {
                                authors2.add(authorsArray.getString(i));
                                i++;
                            } catch (JSONException e) {
                                e = e;
                                Collection<String> collection = authors2;
                                throw new IOException(e.toString());
                            }
                        }
                        authors = authors2;
                    }
                    Collection<String> newTexts = new ArrayList<>();
                    maybeAddText(title, newTexts);
                    maybeAddTextSeries(authors, newTexts);
                    if (pages == null || pages.length() == 0) {
                        str = null;
                    } else {
                        str = String.valueOf(pages) + "pp.";
                    }
                    maybeAddText(str, newTexts);
                    String baseBookUri = "http://www.google." + LocaleManager.getBookSearchCountryTLD(this.context) + "/search?tbm=bks&source=zxing&q=";
                    append(this.isbn, this.source, (String[]) newTexts.toArray(new String[newTexts.size()]), String.valueOf(baseBookUri) + this.isbn);
                }
            } catch (JSONException e2) {
                e = e2;
                throw new IOException(e.toString());
            }
        }
    }
}
