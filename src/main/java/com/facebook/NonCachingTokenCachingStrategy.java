package com.facebook;

import android.os.Bundle;

public class NonCachingTokenCachingStrategy extends TokenCachingStrategy {
    public Bundle load() {
        return null;
    }

    public void save(Bundle bundle) {
    }

    public void clear() {
    }
}
