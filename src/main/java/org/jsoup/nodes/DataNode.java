package org.jsoup.nodes;

import org.jsoup.nodes.Document;

public class DataNode extends Node {
    private static final String DATA_KEY = "data";

    public DataNode(String data, String baseUri) {
        super(baseUri);
        this.attributes.put("data", data);
    }

    public String nodeName() {
        return "#data";
    }

    public String getWholeData() {
        return this.attributes.get("data");
    }

    public DataNode setWholeData(String data) {
        this.attributes.put("data", data);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append(getWholeData());
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
    }

    public String toString() {
        return outerHtml();
    }

    public static DataNode createFromEncoded(String encodedData, String baseUri) {
        return new DataNode(Entities.unescape(encodedData), baseUri);
    }
}
