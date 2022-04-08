package com.google.zxing.client.android.common.executor;

import android.annotation.TargetApi;
import android.os.AsyncTask;

@TargetApi(11)
public final class HoneycombAsyncTaskExecInterface implements AsyncTaskExecInterface {
    public <T> void execute(AsyncTask<T, ?, ?> task, T... args) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
    }
}
