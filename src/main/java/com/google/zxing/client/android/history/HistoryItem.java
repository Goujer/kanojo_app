package com.google.zxing.client.android.history;

import com.google.zxing.Result;

public final class HistoryItem {
    private final String details;
    private final String display;
    private final Result result;

    HistoryItem(Result result2, String display2, String details2) {
        this.result = result2;
        this.display = display2;
        this.details = details2;
    }

    public Result getResult() {
        return this.result;
    }

    public String getDisplayAndDetails() {
        StringBuilder displayResult = new StringBuilder();
        if (this.display == null || this.display.length() == 0) {
            displayResult.append(this.result.getText());
        } else {
            displayResult.append(this.display);
        }
        if (this.details != null && this.details.length() > 0) {
            displayResult.append(" : ").append(this.details);
        }
        return displayResult.toString();
    }
}
