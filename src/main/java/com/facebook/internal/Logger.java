package com.facebook.internal;

import android.util.Log;
import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    public static final String LOG_TAG_BASE = "FacebookSDK.";
    private static final HashMap<String, String> stringsToReplace = new HashMap<>();
    private final LoggingBehavior behavior;
    private StringBuilder contents;
    private int priority = 3;
    private final String tag;

    public static synchronized void registerStringToReplace(String original, String replace) {
        synchronized (Logger.class) {
            stringsToReplace.put(original, replace);
        }
    }

    public static synchronized void registerAccessToken(String accessToken) {
        synchronized (Logger.class) {
            if (!Settings.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
                registerStringToReplace(accessToken, "ACCESS_TOKEN_REMOVED");
            }
        }
    }

    public static void log(LoggingBehavior behavior2, String tag2, String string) {
        log(behavior2, 3, tag2, string);
    }

    public static void log(LoggingBehavior behavior2, String tag2, String format, Object... args) {
        if (Settings.isLoggingBehaviorEnabled(behavior2)) {
            log(behavior2, 3, tag2, String.format(format, args));
        }
    }

    public static void log(LoggingBehavior behavior2, int priority2, String tag2, String string) {
        if (Settings.isLoggingBehaviorEnabled(behavior2)) {
            String string2 = replaceStrings(string);
            if (!tag2.startsWith(LOG_TAG_BASE)) {
                tag2 = LOG_TAG_BASE + tag2;
            }
            Log.println(priority2, tag2, string2);
        }
    }

    private static synchronized String replaceStrings(String string) {
        synchronized (Logger.class) {
            for (Map.Entry<String, String> entry : stringsToReplace.entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
            }
        }
        return string;
    }

    public Logger(LoggingBehavior behavior2, String tag2) {
        Validate.notNullOrEmpty(tag2, "tag");
        this.behavior = behavior2;
        this.tag = LOG_TAG_BASE + tag2;
        this.contents = new StringBuilder();
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int value) {
        Validate.oneOf(Integer.valueOf(value), "value", 7, 3, 6, 4, 2, 5);
        this.priority = value;
    }

    public String getContents() {
        return replaceStrings(this.contents.toString());
    }

    public void log() {
        logString(this.contents.toString());
        this.contents = new StringBuilder();
    }

    public void logString(String string) {
        log(this.behavior, this.priority, this.tag, string);
    }

    public void append(StringBuilder stringBuilder) {
        if (shouldLog()) {
            this.contents.append(stringBuilder);
        }
    }

    public void append(String string) {
        if (shouldLog()) {
            this.contents.append(string);
        }
    }

    public void append(String format, Object... args) {
        if (shouldLog()) {
            this.contents.append(String.format(format, args));
        }
    }

    public void appendKeyValue(String key, Object value) {
        append("  %s:\t%s\n", key, value);
    }

    private boolean shouldLog() {
        return Settings.isLoggingBehaviorEnabled(this.behavior);
    }
}
