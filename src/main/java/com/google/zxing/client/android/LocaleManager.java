package com.google.zxing.client.android;

import android.content.Context;
import android.preference.PreferenceManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleManager {
    private static final String DEFAULT_COUNTRY = "US";
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_TLD = "com";
    private static final Map<String, String> GOOGLE_BOOK_SEARCH_COUNTRY_TLD = GOOGLE_COUNTRY_TLD;
    private static final Map<String, String> GOOGLE_COUNTRY_TLD = new HashMap();
    private static final Map<String, String> GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD = new HashMap();
    private static final Collection<String> TRANSLATED_HELP_ASSET_LANGUAGES = Arrays.asList(new String[]{"de", DEFAULT_LANGUAGE, "es", "fr", "it", "ja", "ko", "nl", "pt", "ru", "zh-rCN", "zh-rTW"});

    static {
        GOOGLE_COUNTRY_TLD.put("AR", "com.ar");
        GOOGLE_COUNTRY_TLD.put("AU", "com.au");
        GOOGLE_COUNTRY_TLD.put("BR", "com.br");
        GOOGLE_COUNTRY_TLD.put("BG", "bg");
        GOOGLE_COUNTRY_TLD.put(Locale.CANADA.getCountry(), "ca");
        GOOGLE_COUNTRY_TLD.put(Locale.CHINA.getCountry(), "cn");
        GOOGLE_COUNTRY_TLD.put("CZ", "cz");
        GOOGLE_COUNTRY_TLD.put("DK", "dk");
        GOOGLE_COUNTRY_TLD.put("FI", "fi");
        GOOGLE_COUNTRY_TLD.put(Locale.FRANCE.getCountry(), "fr");
        GOOGLE_COUNTRY_TLD.put(Locale.GERMANY.getCountry(), "de");
        GOOGLE_COUNTRY_TLD.put("GR", "gr");
        GOOGLE_COUNTRY_TLD.put("HU", "hu");
        GOOGLE_COUNTRY_TLD.put("ID", "co.id");
        GOOGLE_COUNTRY_TLD.put("IL", "co.il");
        GOOGLE_COUNTRY_TLD.put(Locale.ITALY.getCountry(), "it");
        GOOGLE_COUNTRY_TLD.put(Locale.JAPAN.getCountry(), "co.jp");
        GOOGLE_COUNTRY_TLD.put(Locale.KOREA.getCountry(), "co.kr");
        GOOGLE_COUNTRY_TLD.put("NL", "nl");
        GOOGLE_COUNTRY_TLD.put("PL", "pl");
        GOOGLE_COUNTRY_TLD.put("PT", "pt");
        GOOGLE_COUNTRY_TLD.put("RU", "ru");
        GOOGLE_COUNTRY_TLD.put("SK", "sk");
        GOOGLE_COUNTRY_TLD.put("SI", "si");
        GOOGLE_COUNTRY_TLD.put("ES", "es");
        GOOGLE_COUNTRY_TLD.put("SE", "se");
        GOOGLE_COUNTRY_TLD.put("CH", "ch");
        GOOGLE_COUNTRY_TLD.put(Locale.TAIWAN.getCountry(), "tw");
        GOOGLE_COUNTRY_TLD.put("TR", "com.tr");
        GOOGLE_COUNTRY_TLD.put(Locale.UK.getCountry(), "co.uk");
        GOOGLE_COUNTRY_TLD.put(Locale.US.getCountry(), DEFAULT_TLD);
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put("AU", "com.au");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.FRANCE.getCountry(), "fr");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.GERMANY.getCountry(), "de");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.ITALY.getCountry(), "it");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.JAPAN.getCountry(), "co.jp");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put("NL", "nl");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put("ES", "es");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put("CH", "ch");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.UK.getCountry(), "co.uk");
        GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD.put(Locale.US.getCountry(), DEFAULT_TLD);
    }

    private LocaleManager() {
    }

    public static String getCountryTLD(Context context) {
        return doGetTLD(GOOGLE_COUNTRY_TLD, context);
    }

    public static String getProductSearchCountryTLD(Context context) {
        return doGetTLD(GOOGLE_PRODUCT_SEARCH_COUNTRY_TLD, context);
    }

    public static String getBookSearchCountryTLD(Context context) {
        return doGetTLD(GOOGLE_BOOK_SEARCH_COUNTRY_TLD, context);
    }

    public static boolean isBookSearchUrl(String url) {
        return url.startsWith("http://google.com/books") || url.startsWith("http://books.google.");
    }

    private static String getSystemCountry() {
        Locale locale = Locale.getDefault();
        return locale == null ? DEFAULT_COUNTRY : locale.getCountry();
    }

    private static String getSystemLanguage() {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return DEFAULT_LANGUAGE;
        }
        String language = locale.getLanguage();
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(language)) {
            return String.valueOf(language) + "-r" + getSystemCountry();
        }
        return language;
    }

    public static String getTranslatedAssetLanguage() {
        String language = getSystemLanguage();
        return TRANSLATED_HELP_ASSET_LANGUAGES.contains(language) ? language : DEFAULT_LANGUAGE;
    }

    private static String doGetTLD(Map<String, String> map, Context context) {
        String tld = map.get(getCountry(context));
        return tld == null ? DEFAULT_TLD : tld;
    }

    public static String getCountry(Context context) {
        String countryOverride = PreferenceManager.getDefaultSharedPreferences(context).getString(PreferencesActivity.KEY_SEARCH_COUNTRY, (String) null);
        return (countryOverride == null || countryOverride.length() <= 0 || "-".equals(countryOverride)) ? getSystemCountry() : countryOverride;
    }
}
