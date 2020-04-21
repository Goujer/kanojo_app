package org.jsoup.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.select.Selector;

public class Element extends Node {
    private Set<String> classNames;
    /* access modifiers changed from: private */
    public Tag tag;

    public Element(Tag tag2, String baseUri, Attributes attributes) {
        super(baseUri, attributes);
        Validate.notNull(tag2);
        this.tag = tag2;
    }

    public Element(Tag tag2, String baseUri) {
        this(tag2, baseUri, new Attributes());
    }

    public String nodeName() {
        return this.tag.getName();
    }

    public String tagName() {
        return this.tag.getName();
    }

    public Element tagName(String tagName) {
        Validate.notEmpty(tagName, "Tag name must not be empty.");
        this.tag = Tag.valueOf(tagName);
        return this;
    }

    public Tag tag() {
        return this.tag;
    }

    public boolean isBlock() {
        return this.tag.isBlock();
    }

    public String id() {
        String id = attr("id");
        return id == null ? "" : id;
    }

    public Element attr(String attributeKey, String attributeValue) {
        super.attr(attributeKey, attributeValue);
        return this;
    }

    public Map<String, String> dataset() {
        return this.attributes.dataset();
    }

    public final Element parent() {
        return (Element) this.parentNode;
    }

    public Elements parents() {
        Elements parents = new Elements();
        accumulateParents(this, parents);
        return parents;
    }

    private static void accumulateParents(Element el, Elements parents) {
        Element parent = el.parent();
        if (parent != null && !parent.tagName().equals("#root")) {
            parents.add(parent);
            accumulateParents(parent, parents);
        }
    }

    public Element child(int index) {
        return children().get(index);
    }

    public Elements children() {
        List<Element> elements = new ArrayList<>();
        for (Node node : this.childNodes) {
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        return new Elements(elements);
    }

    public List<TextNode> textNodes() {
        List<TextNode> textNodes = new ArrayList<>();
        for (Node node : this.childNodes) {
            if (node instanceof TextNode) {
                textNodes.add((TextNode) node);
            }
        }
        return Collections.unmodifiableList(textNodes);
    }

    public List<DataNode> dataNodes() {
        List<DataNode> dataNodes = new ArrayList<>();
        for (Node node : this.childNodes) {
            if (node instanceof DataNode) {
                dataNodes.add((DataNode) node);
            }
        }
        return Collections.unmodifiableList(dataNodes);
    }

    public Elements select(String cssQuery) {
        return Selector.select(cssQuery, this);
    }

    public Element appendChild(Node child) {
        Validate.notNull(child);
        addChildren(child);
        return this;
    }

    public Element prependChild(Node child) {
        Validate.notNull(child);
        addChildren(0, child);
        return this;
    }

    public Element insertChildren(int index, Collection<? extends Node> children) {
        Validate.notNull(children, "Children collection to be inserted must not be null.");
        int currentSize = childNodeSize();
        if (index < 0) {
            index += currentSize + 1;
        }
        Validate.isTrue(index >= 0 && index <= currentSize, "Insert position out of bounds.");
        ArrayList<Node> nodes = new ArrayList<>(children);
        addChildren(index, (Node[]) nodes.toArray(new Node[nodes.size()]));
        return this;
    }

    public Element appendElement(String tagName) {
        Element child = new Element(Tag.valueOf(tagName), baseUri());
        appendChild(child);
        return child;
    }

    public Element prependElement(String tagName) {
        Element child = new Element(Tag.valueOf(tagName), baseUri());
        prependChild(child);
        return child;
    }

    public Element appendText(String text) {
        appendChild(new TextNode(text, baseUri()));
        return this;
    }

    public Element prependText(String text) {
        prependChild(new TextNode(text, baseUri()));
        return this;
    }

    public Element append(String html) {
        Validate.notNull(html);
        List<Node> nodes = Parser.parseFragment(html, this, baseUri());
        addChildren((Node[]) nodes.toArray(new Node[nodes.size()]));
        return this;
    }

    public Element prepend(String html) {
        Validate.notNull(html);
        List<Node> nodes = Parser.parseFragment(html, this, baseUri());
        addChildren(0, (Node[]) nodes.toArray(new Node[nodes.size()]));
        return this;
    }

    public Element before(String html) {
        return (Element) super.before(html);
    }

    public Element before(Node node) {
        return (Element) super.before(node);
    }

    public Element after(String html) {
        return (Element) super.after(html);
    }

    public Element after(Node node) {
        return (Element) super.after(node);
    }

    public Element empty() {
        this.childNodes.clear();
        return this;
    }

    public Element wrap(String html) {
        return (Element) super.wrap(html);
    }

    public Elements siblingElements() {
        if (this.parentNode == null) {
            return new Elements(0);
        }
        List<Element> elements = parent().children();
        Elements siblings = new Elements(elements.size() - 1);
        for (Element el : elements) {
            if (el != this) {
                siblings.add(el);
            }
        }
        return siblings;
    }

    public Element nextElementSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List<Element> siblings = parent().children();
        Integer index = indexInList(this, siblings);
        Validate.notNull(index);
        if (siblings.size() > index.intValue() + 1) {
            return siblings.get(index.intValue() + 1);
        }
        return null;
    }

    public Element previousElementSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List<Element> siblings = parent().children();
        Integer index = indexInList(this, siblings);
        Validate.notNull(index);
        if (index.intValue() > 0) {
            return siblings.get(index.intValue() - 1);
        }
        return null;
    }

    public Element firstElementSibling() {
        List<Element> siblings = parent().children();
        if (siblings.size() > 1) {
            return siblings.get(0);
        }
        return null;
    }

    public Integer elementSiblingIndex() {
        if (parent() == null) {
            return 0;
        }
        return indexInList(this, parent().children());
    }

    public Element lastElementSibling() {
        List<Element> siblings = parent().children();
        if (siblings.size() > 1) {
            return siblings.get(siblings.size() - 1);
        }
        return null;
    }

    private static <E extends Element> Integer indexInList(Element search, List<E> elements) {
        Validate.notNull(search);
        Validate.notNull(elements);
        for (int i = 0; i < elements.size(); i++) {
            if (((Element) elements.get(i)).equals(search)) {
                return Integer.valueOf(i);
            }
        }
        return null;
    }

    public Elements getElementsByTag(String tagName) {
        Validate.notEmpty(tagName);
        return Collector.collect(new Evaluator.Tag(tagName.toLowerCase().trim()), this);
    }

    public Element getElementById(String id) {
        Validate.notEmpty(id);
        Elements elements = Collector.collect(new Evaluator.Id(id), this);
        if (elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }

    public Elements getElementsByClass(String className) {
        Validate.notEmpty(className);
        return Collector.collect(new Evaluator.Class(className), this);
    }

    public Elements getElementsByAttribute(String key) {
        Validate.notEmpty(key);
        return Collector.collect(new Evaluator.Attribute(key.trim().toLowerCase()), this);
    }

    public Elements getElementsByAttributeStarting(String keyPrefix) {
        Validate.notEmpty(keyPrefix);
        return Collector.collect(new Evaluator.AttributeStarting(keyPrefix.trim().toLowerCase()), this);
    }

    public Elements getElementsByAttributeValue(String key, String value) {
        return Collector.collect(new Evaluator.AttributeWithValue(key, value), this);
    }

    public Elements getElementsByAttributeValueNot(String key, String value) {
        return Collector.collect(new Evaluator.AttributeWithValueNot(key, value), this);
    }

    public Elements getElementsByAttributeValueStarting(String key, String valuePrefix) {
        return Collector.collect(new Evaluator.AttributeWithValueStarting(key, valuePrefix), this);
    }

    public Elements getElementsByAttributeValueEnding(String key, String valueSuffix) {
        return Collector.collect(new Evaluator.AttributeWithValueEnding(key, valueSuffix), this);
    }

    public Elements getElementsByAttributeValueContaining(String key, String match) {
        return Collector.collect(new Evaluator.AttributeWithValueContaining(key, match), this);
    }

    public Elements getElementsByAttributeValueMatching(String key, Pattern pattern) {
        return Collector.collect(new Evaluator.AttributeWithValueMatching(key, pattern), this);
    }

    public Elements getElementsByAttributeValueMatching(String key, String regex) {
        try {
            return getElementsByAttributeValueMatching(key, Pattern.compile(regex));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
        }
    }

    public Elements getElementsByIndexLessThan(int index) {
        return Collector.collect(new Evaluator.IndexLessThan(index), this);
    }

    public Elements getElementsByIndexGreaterThan(int index) {
        return Collector.collect(new Evaluator.IndexGreaterThan(index), this);
    }

    public Elements getElementsByIndexEquals(int index) {
        return Collector.collect(new Evaluator.IndexEquals(index), this);
    }

    public Elements getElementsContainingText(String searchText) {
        return Collector.collect(new Evaluator.ContainsText(searchText), this);
    }

    public Elements getElementsContainingOwnText(String searchText) {
        return Collector.collect(new Evaluator.ContainsOwnText(searchText), this);
    }

    public Elements getElementsMatchingText(Pattern pattern) {
        return Collector.collect(new Evaluator.Matches(pattern), this);
    }

    public Elements getElementsMatchingText(String regex) {
        try {
            return getElementsMatchingText(Pattern.compile(regex));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
        }
    }

    public Elements getElementsMatchingOwnText(Pattern pattern) {
        return Collector.collect(new Evaluator.MatchesOwn(pattern), this);
    }

    public Elements getElementsMatchingOwnText(String regex) {
        try {
            return getElementsMatchingOwnText(Pattern.compile(regex));
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
        }
    }

    public Elements getAllElements() {
        return Collector.collect(new Evaluator.AllElements(), this);
    }

    public String text() {
        final StringBuilder accum = new StringBuilder();
        new NodeTraversor(new NodeVisitor() {
            public void head(Node node, int depth) {
                if (node instanceof TextNode) {
                    Element.appendNormalisedText(accum, (TextNode) node);
                } else if (node instanceof Element) {
                    Element element = (Element) node;
                    if (accum.length() <= 0) {
                        return;
                    }
                    if ((element.isBlock() || element.tag.getName().equals("br")) && !TextNode.lastCharIsWhitespace(accum)) {
                        accum.append(" ");
                    }
                }
            }

            public void tail(Node node, int depth) {
            }
        }).traverse(this);
        return accum.toString().trim();
    }

    public String ownText() {
        StringBuilder sb = new StringBuilder();
        ownText(sb);
        return sb.toString().trim();
    }

    private void ownText(StringBuilder accum) {
        for (Node child : this.childNodes) {
            if (child instanceof TextNode) {
                appendNormalisedText(accum, (TextNode) child);
            } else if (child instanceof Element) {
                appendWhitespaceIfBr((Element) child, accum);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void appendNormalisedText(StringBuilder accum, TextNode textNode) {
        String text = textNode.getWholeText();
        if (!preserveWhitespace(textNode.parent())) {
            text = TextNode.normaliseWhitespace(text);
            if (TextNode.lastCharIsWhitespace(accum)) {
                text = TextNode.stripLeadingWhitespace(text);
            }
        }
        accum.append(text);
    }

    private static void appendWhitespaceIfBr(Element element, StringBuilder accum) {
        if (element.tag.getName().equals("br") && !TextNode.lastCharIsWhitespace(accum)) {
            accum.append(" ");
        }
    }

    static boolean preserveWhitespace(Node node) {
        if (node == null || !(node instanceof Element)) {
            return false;
        }
        Element element = (Element) node;
        if (element.tag.preserveWhitespace() || (element.parent() != null && element.parent().tag.preserveWhitespace())) {
            return true;
        }
        return false;
    }

    public Element text(String text) {
        Validate.notNull(text);
        empty();
        appendChild(new TextNode(text, this.baseUri));
        return this;
    }

    public boolean hasText() {
        for (Node child : this.childNodes) {
            if (child instanceof TextNode) {
                if (!((TextNode) child).isBlank()) {
                    return true;
                }
            } else if ((child instanceof Element) && ((Element) child).hasText()) {
                return true;
            }
        }
        return false;
    }

    public String data() {
        StringBuilder sb = new StringBuilder();
        for (Node childNode : this.childNodes) {
            if (childNode instanceof DataNode) {
                sb.append(((DataNode) childNode).getWholeData());
            } else if (childNode instanceof Element) {
                sb.append(((Element) childNode).data());
            }
        }
        return sb.toString();
    }

    public String className() {
        return attr("class");
    }

    public Set<String> classNames() {
        if (this.classNames == null) {
            this.classNames = new LinkedHashSet(Arrays.asList(className().split("\\s+")));
        }
        return this.classNames;
    }

    public Element classNames(Set<String> classNames2) {
        Validate.notNull(classNames2);
        this.attributes.put("class", StringUtil.join((Collection) classNames2, " "));
        return this;
    }

    public boolean hasClass(String className) {
        for (String name : classNames()) {
            if (className.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Element addClass(String className) {
        Validate.notNull(className);
        Set<String> classes = classNames();
        classes.add(className);
        classNames(classes);
        return this;
    }

    public Element removeClass(String className) {
        Validate.notNull(className);
        Set<String> classes = classNames();
        classes.remove(className);
        classNames(classes);
        return this;
    }

    public Element toggleClass(String className) {
        Validate.notNull(className);
        Set<String> classes = classNames();
        if (classes.contains(className)) {
            classes.remove(className);
        } else {
            classes.add(className);
        }
        classNames(classes);
        return this;
    }

    public String val() {
        if (tagName().equals("textarea")) {
            return text();
        }
        return attr("value");
    }

    public Element val(String value) {
        if (tagName().equals("textarea")) {
            text(value);
        } else {
            attr("value", value);
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        if (accum.length() > 0 && out.prettyPrint() && (this.tag.formatAsBlock() || ((parent() != null && parent().tag().formatAsBlock()) || out.outline()))) {
            indent(accum, depth, out);
        }
        accum.append("<").append(tagName());
        this.attributes.html(accum, out);
        if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
            accum.append(">");
        } else {
            accum.append(" />");
        }
    }

    /* access modifiers changed from: package-private */
    public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
        if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
            if (out.prettyPrint() && !this.childNodes.isEmpty() && (this.tag.formatAsBlock() || (out.outline() && (this.childNodes.size() > 1 || (this.childNodes.size() == 1 && !(this.childNodes.get(0) instanceof TextNode)))))) {
                indent(accum, depth, out);
            }
            accum.append("</").append(tagName()).append(">");
        }
    }

    public String html() {
        StringBuilder accum = new StringBuilder();
        html(accum);
        return accum.toString().trim();
    }

    private void html(StringBuilder accum) {
        for (Node node : this.childNodes) {
            node.outerHtml(accum);
        }
    }

    public Element html(String html) {
        empty();
        append(html);
        return this;
    }

    public String toString() {
        return outerHtml();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + (this.tag != null ? this.tag.hashCode() : 0);
    }

    public Element clone() {
        Element clone = (Element) super.clone();
        clone.classNames = null;
        return clone;
    }
}
