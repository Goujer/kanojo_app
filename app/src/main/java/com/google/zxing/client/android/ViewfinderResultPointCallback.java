package com.google.zxing.client.android;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

final class ViewfinderResultPointCallback implements ResultPointCallback {
    private final ViewfinderView viewfinderView;

    ViewfinderResultPointCallback(ViewfinderView viewfinderView2) {
        this.viewfinderView = viewfinderView2;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        this.viewfinderView.addPossibleResultPoint(point);
    }
}
