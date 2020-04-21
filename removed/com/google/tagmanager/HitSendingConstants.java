package com.google.tagmanager;

class HitSendingConstants {
    static final String ACTION_RADIO_POWERED = "com.google.analytics.RADIO_POWERED";
    static final long DATABASE_RECOVERY_TIMEOUT_MS = 3600000;
    static final int MAX_NUM_STORED_HITS = 2000;
    static final int MAX_REQUESTS_PER_DISPATCH = 40;
    static final long MILLISECONDS_PER_DAY = 86400000;
    static final long MILLISECONDS_PER_HOUR = 3600000;
    static final long MILLISECONDS_PER_MINUTE = 60000;
    static final long MILLISECONDS_PER_MONTH = 2592000000L;

    HitSendingConstants() {
    }
}
