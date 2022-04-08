package com.google.zxing.client.android.pref;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;

public final class BSPlusPreference extends Preference {
    private static final String MARKET_URL = "market://details?id=com.srowen.bs.android";

    public BSPlusPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        configureClickListener();
    }

    public BSPlusPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureClickListener();
    }

    public BSPlusPreference(Context context) {
        super(context);
        configureClickListener();
    }

    private void configureClickListener() {
        setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(BSPlusPreference.MARKET_URL));
                intent.addFlags(524288);
                BSPlusPreference.this.getContext().startActivity(intent);
                return true;
            }
        });
    }
}
