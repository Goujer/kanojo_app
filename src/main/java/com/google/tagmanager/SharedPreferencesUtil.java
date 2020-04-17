package com.google.tagmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

class SharedPreferencesUtil {
    SharedPreferencesUtil() {
    }

    static void saveEditorAsync(final SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            new Thread(new Runnable() {
                public void run() {
                    editor.commit();
                }
            }).start();
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    static void saveAsync(Context context, String sharedPreferencesName, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(sharedPreferencesName, 0).edit();
        editor.putString(key, value);
        saveEditorAsync(editor);
    }
}
