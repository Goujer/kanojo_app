package com.google.zxing.client.android.result.supplement;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.TextView;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;
import com.google.zxing.client.android.history.HistoryManager;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

public abstract class SupplementalInfoRetriever extends AsyncTask<Object, Object, Object> {
    private static final String TAG = "SupplementalInfo";
    private final WeakReference<HistoryManager> historyManagerRef;
    private final List<Spannable> newContents = new ArrayList();
    private final List<String[]> newHistories = new ArrayList();
    private final WeakReference<TextView> textViewRef;

    abstract void retrieveSupplementalInfo() throws IOException;

    SupplementalInfoRetriever(TextView textView, HistoryManager historyManager) {
        this.textViewRef = new WeakReference<>(textView);
        this.historyManagerRef = new WeakReference<>(historyManager);
    }

    public final Object doInBackground(Object... args) {
        try {
            retrieveSupplementalInfo();
            return null;
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    protected final void onPostExecute(Object arg) {
        TextView textView = (TextView) this.textViewRef.get();
        if (textView != null) {
            for (Spannable content : this.newContents) {
                textView.append(content);
            }
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        HistoryManager historyManager = (HistoryManager) this.historyManagerRef.get();
        if (historyManager != null) {
            for (String[] text : this.newHistories) {
                historyManager.addHistoryItemDetails(text[0], text[1]);
            }
        }
    }

    final void append(String itemID, String source, String[] newTexts, String linkURL) {
        StringBuilder newTextCombined = new StringBuilder();
        if (source != null) {
            newTextCombined.append(source).append(' ');
        }
        int linkStart = newTextCombined.length();
        boolean first = true;
        for (String newText : newTexts) {
            if (first) {
                newTextCombined.append(newText);
                first = false;
            } else {
                newTextCombined.append(" [");
                newTextCombined.append(newText);
                newTextCombined.append(']');
            }
        }
        int linkEnd = newTextCombined.length();
        String newText2 = newTextCombined.toString();
        Spannable content = new SpannableString(String.valueOf(newText2) + "\n\n");
        if (linkURL != null) {
            if (linkURL.startsWith("HTTP://")) {
                linkURL = "http" + linkURL.substring(4);
            } else if (linkURL.startsWith("HTTPS://")) {
                linkURL = "https" + linkURL.substring(5);
            }
            content.setSpan(new URLSpan(linkURL), linkStart, linkEnd, 33);
        }
        this.newContents.add(content);
        this.newHistories.add(new String[]{itemID, newText2});
    }

    static void maybeAddText(String text, Collection<String> texts) {
        if (text != null && text.length() > 0) {
            texts.add(text);
        }
    }

    static void maybeAddTextSeries(Collection<String> textSeries, Collection<String> texts) {
        if (textSeries != null && !textSeries.isEmpty()) {
            boolean first = true;
            StringBuilder authorsText = new StringBuilder();
            for (String author : textSeries) {
                if (first) {
                    first = false;
                } else {
                    authorsText.append(", ");
                }
                authorsText.append(author);
            }
            texts.add(authorsText.toString());
        }
    }
}
