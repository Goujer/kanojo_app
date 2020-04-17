package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

public class DocumentType extends Node {
    public DocumentType(String name, String publicId, String systemId, String baseUri) {
        super(baseUri);
        Validate.notEmpty(name);
        attr("name", name);
        attr("publicId", publicId);
        attr("systemId", systemId);
    }

    public String nodeName() {
        return "#doctype";
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("<!DOCTYPE ").append(attr("name"));
        if (!StringUtil.isBlank(attr("publicId"))) {
            accum.append(" PUBLIC \"").append(attr("publicId")).append("\"");
        }
        if (!StringUtil.isBlank(attr("systemId"))) {
            accum.append(" \"").append(attr("systemId")).append("\"");
        }
        accum.append('>');
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
    }
}
