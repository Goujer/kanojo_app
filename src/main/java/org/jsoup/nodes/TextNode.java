package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

public class TextNode extends Node {
    private static final String TEXT_KEY = "text";
    String text;

    public TextNode(String text2, String baseUri) {
        this.baseUri = baseUri;
        this.text = text2;
    }

    public String nodeName() {
        return "#text";
    }

    public String text() {
        return normaliseWhitespace(getWholeText());
    }

    public TextNode text(String text2) {
        this.text = text2;
        if (this.attributes != null) {
            this.attributes.put(TEXT_KEY, text2);
        }
        return this;
    }

    public String getWholeText() {
        return this.attributes == null ? this.text : this.attributes.get(TEXT_KEY);
    }

    public boolean isBlank() {
        return StringUtil.isBlank(getWholeText());
    }

    public TextNode splitText(int offset) {
        boolean z;
        boolean z2;
        if (offset >= 0) {
            z = true;
        } else {
            z = false;
        }
        Validate.isTrue(z, "Split offset must be not be negative");
        if (offset < this.text.length()) {
            z2 = true;
        } else {
            z2 = false;
        }
        Validate.isTrue(z2, "Split offset must not be greater than current text length");
        String head = getWholeText().substring(0, offset);
        String tail = getWholeText().substring(offset);
        text(head);
        TextNode tailNode = new TextNode(tail, baseUri());
        if (parent() != null) {
            parent().addChildren(siblingIndex() + 1, tailNode);
        }
        return tailNode;
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        String html = Entities.escape(getWholeText(), out);
        if (out.prettyPrint() && (parent() instanceof Element) && !Element.preserveWhitespace((Element) parent())) {
            html = normaliseWhitespace(html);
        }
        if (out.prettyPrint() && ((siblingIndex() == 0 && (this.parentNode instanceof Element) && ((Element) this.parentNode).tag().formatAsBlock() && !isBlank()) || (out.outline() && siblingNodes().size() > 0 && !isBlank()))) {
            indent(accum, depth, out);
        }
        accum.append(html);
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
    }

    public String toString() {
        return outerHtml();
    }

    public static TextNode createFromEncoded(String encodedText, String baseUri) {
        return new TextNode(Entities.unescape(encodedText), baseUri);
    }

    static String normaliseWhitespace(String text2) {
        return StringUtil.normaliseWhitespace(text2);
    }

    static String stripLeadingWhitespace(String text2) {
        return text2.replaceFirst("^\\s+", "");
    }

    static boolean lastCharIsWhitespace(StringBuilder sb) {
        return sb.length() != 0 && sb.charAt(sb.length() + -1) == ' ';
    }

    private void ensureAttributes() {
        if (this.attributes == null) {
            this.attributes = new Attributes();
            this.attributes.put(TEXT_KEY, this.text);
        }
    }

    public String attr(String attributeKey) {
        ensureAttributes();
        return super.attr(attributeKey);
    }

    public Attributes attributes() {
        ensureAttributes();
        return super.attributes();
    }

    public Node attr(String attributeKey, String attributeValue) {
        ensureAttributes();
        return super.attr(attributeKey, attributeValue);
    }

    public boolean hasAttr(String attributeKey) {
        ensureAttributes();
        return super.hasAttr(attributeKey);
    }

    public Node removeAttr(String attributeKey) {
        ensureAttributes();
        return super.removeAttr(attributeKey);
    }

    public String absUrl(String attributeKey) {
        ensureAttributes();
        return super.absUrl(attributeKey);
    }
}
