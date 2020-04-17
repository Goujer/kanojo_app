package com.google.zxing.client.android.book;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.zxing.client.android.HttpHelper;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.LocaleManager;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import jp.co.cybird.barcodekanojoForGAM.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class SearchBookContentsActivity extends Activity {
    /* access modifiers changed from: private */
    public static final Pattern GT_ENTITY_PATTERN = Pattern.compile("&gt;");
    /* access modifiers changed from: private */
    public static final Pattern LT_ENTITY_PATTERN = Pattern.compile("&lt;");
    /* access modifiers changed from: private */
    public static final Pattern QUOTE_ENTITY_PATTERN = Pattern.compile("&#39;");
    /* access modifiers changed from: private */
    public static final Pattern QUOT_ENTITY_PATTERN = Pattern.compile("&quot;");
    /* access modifiers changed from: private */
    public static final String TAG = SearchBookContentsActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final Pattern TAG_PATTERN = Pattern.compile("\\<.*?\\>");
    private final View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View view) {
            SearchBookContentsActivity.this.launchSearch();
        }
    };
    /* access modifiers changed from: private */
    public TextView headerView;
    private String isbn;
    private final View.OnKeyListener keyListener = new View.OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (keyCode != 66 || event.getAction() != 0) {
                return false;
            }
            SearchBookContentsActivity.this.launchSearch();
            return true;
        }
    };
    private NetworkTask networkTask;
    /* access modifiers changed from: private */
    public Button queryButton;
    /* access modifiers changed from: private */
    public EditText queryTextView;
    /* access modifiers changed from: private */
    public ListView resultListView;
    private final AsyncTaskExecInterface taskExec = ((AsyncTaskExecInterface) new AsyncTaskExecManager().build());

    /* access modifiers changed from: package-private */
    public String getISBN() {
        return this.isbn;
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeExpiredCookie();
        Intent intent = getIntent();
        if (intent == null || !intent.getAction().equals(Intents.SearchBookContents.ACTION)) {
            finish();
            return;
        }
        this.isbn = intent.getStringExtra(Intents.SearchBookContents.ISBN);
        if (LocaleManager.isBookSearchUrl(this.isbn)) {
            setTitle(getString(R.string.sbc_name));
        } else {
            setTitle(String.valueOf(getString(R.string.sbc_name)) + ": ISBN " + this.isbn);
        }
        setContentView(R.layout.search_book_contents);
        this.queryTextView = (EditText) findViewById(R.id.query_text_view);
        String initialQuery = intent.getStringExtra(Intents.SearchBookContents.QUERY);
        if (initialQuery != null && initialQuery.length() > 0) {
            this.queryTextView.setText(initialQuery);
        }
        this.queryTextView.setOnKeyListener(this.keyListener);
        this.queryButton = (Button) findViewById(R.id.query_button);
        this.queryButton.setOnClickListener(this.buttonListener);
        this.resultListView = (ListView) findViewById(R.id.result_list_view);
        this.headerView = (TextView) LayoutInflater.from(this).inflate(R.layout.search_book_contents_header, this.resultListView, false);
        this.resultListView.addHeaderView(this.headerView);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.queryTextView.selectAll();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        NetworkTask oldTask = this.networkTask;
        if (oldTask != null) {
            oldTask.cancel(true);
            this.networkTask = null;
        }
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void launchSearch() {
        String query = this.queryTextView.getText().toString();
        if (query != null && query.length() > 0) {
            NetworkTask oldTask = this.networkTask;
            if (oldTask != null) {
                oldTask.cancel(true);
            }
            this.networkTask = new NetworkTask(this, (NetworkTask) null);
            this.taskExec.execute(this.networkTask, query, this.isbn);
            this.headerView.setText(R.string.msg_sbc_searching_book);
            this.resultListView.setAdapter((ListAdapter) null);
            this.queryTextView.setEnabled(false);
            this.queryButton.setEnabled(false);
        }
    }

    private final class NetworkTask extends AsyncTask<String, Object, JSONObject> {
        private NetworkTask() {
        }

        /* synthetic */ NetworkTask(SearchBookContentsActivity searchBookContentsActivity, NetworkTask networkTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public JSONObject doInBackground(String... args) {
            String uri;
            try {
                String theQuery = args[0];
                String theIsbn = args[1];
                if (LocaleManager.isBookSearchUrl(theIsbn)) {
                    uri = "http://www.google.com/books?id=" + theIsbn.substring(theIsbn.indexOf(61) + 1) + "&jscmd=SearchWithinVolume2&q=" + theQuery;
                } else {
                    uri = "http://www.google.com/books?vid=isbn" + theIsbn + "&jscmd=SearchWithinVolume2&q=" + theQuery;
                }
                return new JSONObject(HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.JSON).toString());
            } catch (IOException ioe) {
                Log.w(SearchBookContentsActivity.TAG, "Error accessing book search", ioe);
                return null;
            } catch (JSONException je) {
                Log.w(SearchBookContentsActivity.TAG, "Error accessing book search", je);
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(JSONObject result) {
            if (result == null) {
                SearchBookContentsActivity.this.headerView.setText(R.string.msg_sbc_failed);
            } else {
                handleSearchResults(result);
            }
            SearchBookContentsActivity.this.queryTextView.setEnabled(true);
            SearchBookContentsActivity.this.queryTextView.selectAll();
            SearchBookContentsActivity.this.queryButton.setEnabled(true);
        }

        private void handleSearchResults(JSONObject json) {
            try {
                int count = json.getInt("number_of_results");
                SearchBookContentsActivity.this.headerView.setText(String.valueOf(SearchBookContentsActivity.this.getString(R.string.msg_sbc_results)) + " : " + count);
                if (count > 0) {
                    JSONArray results = json.getJSONArray("search_results");
                    SearchBookContentsResult.setQuery(SearchBookContentsActivity.this.queryTextView.getText().toString());
                    List<SearchBookContentsResult> items = new ArrayList<>(count);
                    for (int x = 0; x < count; x++) {
                        items.add(parseResult(results.getJSONObject(x)));
                    }
                    SearchBookContentsActivity.this.resultListView.setOnItemClickListener(new BrowseBookListener(SearchBookContentsActivity.this, items));
                    SearchBookContentsActivity.this.resultListView.setAdapter(new SearchBookContentsAdapter(SearchBookContentsActivity.this, items));
                    return;
                }
                if ("false".equals(json.optString("searchable"))) {
                    SearchBookContentsActivity.this.headerView.setText(R.string.msg_sbc_book_not_searchable);
                }
                SearchBookContentsActivity.this.resultListView.setAdapter((ListAdapter) null);
            } catch (JSONException e) {
                Log.w(SearchBookContentsActivity.TAG, "Bad JSON from book search", e);
                SearchBookContentsActivity.this.resultListView.setAdapter((ListAdapter) null);
                SearchBookContentsActivity.this.headerView.setText(R.string.msg_sbc_failed);
            }
        }

        private SearchBookContentsResult parseResult(JSONObject json) {
            String pageNumber;
            String snippet;
            try {
                String pageId = json.getString("page_id");
                String pageNumber2 = json.getString("page_number");
                if (pageNumber2.length() > 0) {
                    pageNumber = String.valueOf(SearchBookContentsActivity.this.getString(R.string.msg_sbc_page)) + ' ' + pageNumber2;
                } else {
                    pageNumber = SearchBookContentsActivity.this.getString(R.string.msg_sbc_unknown_page);
                }
                String snippet2 = json.optString("snippet_text");
                boolean valid = true;
                if (snippet2.length() > 0) {
                    snippet = SearchBookContentsActivity.QUOT_ENTITY_PATTERN.matcher(SearchBookContentsActivity.QUOTE_ENTITY_PATTERN.matcher(SearchBookContentsActivity.GT_ENTITY_PATTERN.matcher(SearchBookContentsActivity.LT_ENTITY_PATTERN.matcher(SearchBookContentsActivity.TAG_PATTERN.matcher(snippet2).replaceAll("")).replaceAll("<")).replaceAll(">")).replaceAll("'")).replaceAll("\"");
                } else {
                    snippet = String.valueOf('(') + SearchBookContentsActivity.this.getString(R.string.msg_sbc_snippet_unavailable) + ')';
                    valid = false;
                }
                return new SearchBookContentsResult(pageId, pageNumber, snippet, valid);
            } catch (JSONException e) {
                return new SearchBookContentsResult(SearchBookContentsActivity.this.getString(R.string.msg_sbc_no_page_returned), "", "", false);
            }
        }
    }
}
