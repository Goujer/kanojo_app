package com.google.zxing.client.android.result;

import android.view.View;

public final class ResultButtonListener implements View.OnClickListener {
    private final int index;
    private final ResultHandler resultHandler;

    public ResultButtonListener(ResultHandler resultHandler2, int index2) {
        this.resultHandler = resultHandler2;
        this.index = index2;
    }

    @Override
    public void onClick(View view) {
        this.resultHandler.handleButtonPress(this.index);
    }
}
