package com.google.zxing.client.android.book;

final class SearchBookContentsResult {
    private static String query = null;
    private final String pageId;
    private final String pageNumber;
    private final String snippet;
    private final boolean validSnippet;

    SearchBookContentsResult(String pageId2, String pageNumber2, String snippet2, boolean validSnippet2) {
        this.pageId = pageId2;
        this.pageNumber = pageNumber2;
        this.snippet = snippet2;
        this.validSnippet = validSnippet2;
    }

    public static void setQuery(String query2) {
        query = query2;
    }

    public String getPageId() {
        return this.pageId;
    }

    public String getPageNumber() {
        return this.pageNumber;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public boolean getValidSnippet() {
        return this.validSnippet;
    }

    public static String getQuery() {
        return query;
    }
}
