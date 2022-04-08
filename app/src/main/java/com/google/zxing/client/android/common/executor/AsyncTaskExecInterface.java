package com.google.zxing.client.android.common.executor;

import android.os.AsyncTask;

public interface AsyncTaskExecInterface {
    <T> void execute(AsyncTask<T, ?, ?> asyncTask, T... tArr);
}
