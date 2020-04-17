package org.jsoup.nodes;

import org.jsoup.nodes.Document;

public class Comment extends Node {
    private static final String COMMENT_KEY = "comment";

    public Comment(String data, String baseUri) {
        super(baseUri);
        this.attributes.put(COMMENT_KEY, data);
    }

    public String nodeName() {
        return "#comment";
    }

    public String getData() {
        return this.attributes.get(COMMENT_KEY);
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        if (out.prettyPrint()) {
            indent(accum, depth, out);
        }
        accum.append("<!--").append(getData()).append("-->");
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
    }

    public String toString() {
        return outerHtml();
    }
}
