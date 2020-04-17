package com.google.zxing.client.android.share;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.zxing.client.android.common.executor.AsyncTaskExecInterface;
import com.google.zxing.client.android.common.executor.AsyncTaskExecManager;

public final class AppPickerActivity extends ListActivity {
    private LoadPackagesAsyncTask backgroundTask;
    private final AsyncTaskExecInterface taskExec = ((AsyncTaskExecInterface) new AsyncTaskExecManager().build());

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.backgroundTask = new LoadPackagesAsyncTask(this);
        this.taskExec.execute(this.backgroundTask, new Void[0]);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LoadPackagesAsyncTask task = this.backgroundTask;
        if (task != null) {
            task.cancel(true);
            this.backgroundTask = null;
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View view, int position, long id) {
        ListAdapter adapter = getListAdapter();
        if (position < 0 || position >= adapter.getCount()) {
            setResult(0);
        } else {
            String packageName = ((AppInfo) adapter.getItem(position)).getPackageName();
            Intent intent = new Intent();
            intent.addFlags(524288);
            intent.putExtra("url", "market://details?id=" + packageName);
            setResult(-1, intent);
        }
        finish();
    }
}
