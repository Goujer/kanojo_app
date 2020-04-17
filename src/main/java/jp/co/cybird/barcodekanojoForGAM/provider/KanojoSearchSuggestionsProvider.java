package jp.co.cybird.barcodekanojoForGAM.provider;

import android.content.SearchRecentSuggestionsProvider;

public class KanojoSearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "jp.co.cybird.barcodekanojoForAU.provider.KanojoSearchSuggestionsProvider";
    public static final int MODE = 1;

    public KanojoSearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, 1);
    }
}
