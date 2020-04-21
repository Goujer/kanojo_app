package org.jsoup.nodes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public abstract class Node implements Cloneable {
    Attributes attributes;
    String baseUri;
    List<Node> childNodes;
    Node parentNode;
    int siblingIndex;

    public abstract String nodeName();

    /* access modifiers changed from: package-private */
    public abstract void outerHtmlHead(StringBuilder sb, int i, Document.OutputSettings outputSettings);

    /* access modifiers changed from: package-private */
    public abstract void outerHtmlTail(StringBuilder sb, int i, Document.OutputSettings outputSettings);

    protected Node(String baseUri2, Attributes attributes2) {
        Validate.notNull(baseUri2);
        Validate.notNull(attributes2);
        this.childNodes = new ArrayList(4);
        this.baseUri = baseUri2.trim();
        this.attributes = attributes2;
    }

    protected Node(String baseUri2) {
        this(baseUri2, new Attributes());
    }

    protected Node() {
        this.childNodes = Collections.emptyList();
        this.attributes = null;
    }

    public String attr(String attributeKey) {
        Validate.notNull(attributeKey);
        if (this.attributes.hasKey(attributeKey)) {
            return this.attributes.get(attributeKey);
        }
        if (attributeKey.toLowerCase().startsWith("abs:")) {
            return absUrl(attributeKey.substring("abs:".length()));
        }
        return "";
    }

    public Attributes attributes() {
        return this.attributes;
    }

    public Node attr(String attributeKey, String attributeValue) {
        this.attributes.put(attributeKey, attributeValue);
        return this;
    }

    public boolean hasAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        if (attributeKey.toLowerCase().startsWith("abs:")) {
            String key = attributeKey.substring("abs:".length());
            if (this.attributes.hasKey(key) && !absUrl(key).equals("")) {
                return true;
            }
        }
        return this.attributes.hasKey(attributeKey);
    }

    public Node removeAttr(String attributeKey) {
        Validate.notNull(attributeKey);
        this.attributes.remove(attributeKey);
        return this;
    }

    public String baseUri() {
        return this.baseUri;
    }

    public void setBaseUri(final String baseUri2) {
        Validate.notNull(baseUri2);
        traverse(new NodeVisitor() {
            public void head(Node node, int depth) {
                node.baseUri = baseUri2;
            }

            public void tail(Node node, int depth) {
            }
        });
    }

    public String absUrl(String attributeKey) {
        Validate.notEmpty(attributeKey);
        String relUrl = attr(attributeKey);
        if (!hasAttr(attributeKey)) {
            return "";
        }
        try {
            URL base = new URL(this.baseUri);
            try {
                if (relUrl.startsWith("?")) {
                    relUrl = base.getPath() + relUrl;
                }
                return new URL(base, relUrl).toExternalForm();
            } catch (MalformedURLException e) {
                return "";
            }
        } catch (MalformedURLException e2) {
            return new URL(relUrl).toExternalForm();
        }
    }

    public Node childNode(int index) {
        return this.childNodes.get(index);
    }

    public List<Node> childNodes() {
        return Collections.unmodifiableList(this.childNodes);
    }

    public List<Node> childNodesCopy() {
        List<Node> children = new ArrayList<>(this.childNodes.size());
        for (Node node : this.childNodes) {
            children.add(node.clone());
        }
        return children;
    }

    public final int childNodeSize() {
        return this.childNodes.size();
    }

    /* access modifiers changed from: protected */
    public Node[] childNodesAsArray() {
        return (Node[]) this.childNodes.toArray(new Node[childNodeSize()]);
    }

    public Node parent() {
        return this.parentNode;
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public Document ownerDocument() {
        if (this instanceof Document) {
            return (Document) this;
        }
        if (this.parentNode == null) {
            return null;
        }
        return this.parentNode.ownerDocument();
    }

    public void remove() {
        Validate.notNull(this.parentNode);
        this.parentNode.removeChild(this);
    }

    public Node before(String html) {
        addSiblingHtml(siblingIndex(), html);
        return this;
    }

    public Node before(Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(siblingIndex(), node);
        return this;
    }

    public Node after(String html) {
        addSiblingHtml(siblingIndex() + 1, html);
        return this;
    }

    public Node after(Node node) {
        Validate.notNull(node);
        Validate.notNull(this.parentNode);
        this.parentNode.addChildren(siblingIndex() + 1, node);
        return this;
    }

    private void addSiblingHtml(int index, String html) {
        Validate.notNull(html);
        Validate.notNull(this.parentNode);
        List<Node> nodes = Parser.parseFragment(html, parent() instanceof Element ? (Element) parent() : null, baseUri());
        this.parentNode.addChildren(index, (Node[]) nodes.toArray(new Node[nodes.size()]));
    }

    /* Debug info: failed to restart local var, previous not found, register: 10 */
    public Node wrap(String html) {
        Element context;
        Validate.notEmpty(html);
        if (parent() instanceof Element) {
            context = (Element) parent();
        } else {
            context = null;
        }
        List<Node> wrapChildren = Parser.parseFragment(html, context, baseUri());
        Node wrapNode = wrapChildren.get(0);
        if (wrapNode == null || !(wrapNode instanceof Element)) {
            return null;
        }
        Element wrap = (Element) wrapNode;
        Element deepest = getDeepChild(wrap);
        this.parentNode.replaceChild(this, wrap);
        deepest.addChildren(this);
        if (wrapChildren.size() <= 0) {
            return this;
        }
        for (int i = 0; i < wrapChildren.size(); i++) {
            Node remainder = wrapChildren.get(i);
            remainder.parentNode.removeChild(remainder);
            wrap.appendChild(remainder);
        }
        return this;
    }

    public Node unwrap() {
        Validate.notNull(this.parentNode);
        int index = this.siblingIndex;
        Node firstChild = this.childNodes.size() > 0 ? this.childNodes.get(0) : null;
        this.parentNode.addChildren(index, childNodesAsArray());
        remove();
        return firstChild;
    }

    private Element getDeepChild(Element el) {
        List<Element> children = el.children();
        if (children.size() > 0) {
            return getDeepChild(children.get(0));
        }
        return el;
    }

    public void replaceWith(Node in) {
        Validate.notNull(in);
        Validate.notNull(this.parentNode);
        this.parentNode.replaceChild(this, in);
    }

    /* access modifiers changed from: protected */
    public void setParentNode(Node parentNode2) {
        if (this.parentNode != null) {
            this.parentNode.removeChild(this);
        }
        this.parentNode = parentNode2;
    }

    /* access modifiers changed from: protected */
    public void replaceChild(Node out, Node in) {
        Validate.isTrue(out.parentNode == this);
        Validate.notNull(in);
        if (in.parentNode != null) {
            in.parentNode.removeChild(in);
        }
        Integer index = Integer.valueOf(out.siblingIndex());
        this.childNodes.set(index.intValue(), in);
        in.parentNode = this;
        in.setSiblingIndex(index.intValue());
        out.parentNode = null;
    }

    /* access modifiers changed from: protected */
    public void removeChild(Node out) {
        Validate.isTrue(out.parentNode == this);
        this.childNodes.remove(out.siblingIndex());
        reindexChildren();
        out.parentNode = null;
    }

    /* access modifiers changed from: protected */
    public void addChildren(Node... children) {
        for (Node child : children) {
            reparentChild(child);
            this.childNodes.add(child);
            child.setSiblingIndex(this.childNodes.size() - 1);
        }
    }

    /* access modifiers changed from: protected */
    public void addChildren(int index, Node... children) {
        Validate.noNullElements(children);
        for (int i = children.length - 1; i >= 0; i--) {
            Node in = children[i];
            reparentChild(in);
            this.childNodes.add(index, in);
        }
        reindexChildren();
    }

    private void reparentChild(Node child) {
        if (child.parentNode != null) {
            child.parentNode.removeChild(child);
        }
        child.setParentNode(this);
    }

    private void reindexChildren() {
        for (int i = 0; i < this.childNodes.size(); i++) {
            this.childNodes.get(i).setSiblingIndex(i);
        }
    }

    public List<Node> siblingNodes() {
        if (this.parentNode == null) {
            return Collections.emptyList();
        }
        List<Node> nodes = this.parentNode.childNodes;
        List<Node> siblings = new ArrayList<>(nodes.size() - 1);
        for (Node node : nodes) {
            if (node != this) {
                siblings.add(node);
            }
        }
        return siblings;
    }

    public Node nextSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List<Node> siblings = this.parentNode.childNodes;
        Integer index = Integer.valueOf(siblingIndex());
        Validate.notNull(index);
        if (siblings.size() > index.intValue() + 1) {
            return siblings.get(index.intValue() + 1);
        }
        return null;
    }

    public Node previousSibling() {
        if (this.parentNode == null) {
            return null;
        }
        List<Node> siblings = this.parentNode.childNodes;
        Integer index = Integer.valueOf(siblingIndex());
        Validate.notNull(index);
        if (index.intValue() > 0) {
            return siblings.get(index.intValue() - 1);
        }
        return null;
    }

    public int siblingIndex() {
        return this.siblingIndex;
    }

    /* access modifiers changed from: protected */
    public void setSiblingIndex(int siblingIndex2) {
        this.siblingIndex = siblingIndex2;
    }

    public Node traverse(NodeVisitor nodeVisitor) {
        Validate.notNull(nodeVisitor);
        new NodeTraversor(nodeVisitor).traverse(this);
        return this;
    }

    public String outerHtml() {
        StringBuilder accum = new StringBuilder(128);
        outerHtml(accum);
        return accum.toString();
    }

    /* access modifiers changed from: protected */
    public void outerHtml(StringBuilder accum) {
        new NodeTraversor(new OuterHtmlVisitor(accum, getOutputSettings())).traverse(this);
    }

    private Document.OutputSettings getOutputSettings() {
        return ownerDocument() != null ? ownerDocument().outputSettings() : new Document("").outputSettings();
    }

    public String toString() {
        return outerHtml();
    }

    /* access modifiers changed from: protected */
    public void indent(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("\n").append(StringUtil.padding(out.indentAmount() * depth));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.parentNode != null) {
            result = this.parentNode.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.attributes != null) {
            i = this.attributes.hashCode();
        }
        return i2 + i;
    }

    public Node clone() {
        return doClone((Node) null);
    }

    /* access modifiers changed from: protected */
    public Node doClone(Node parent) {
        try {
            Node clone = (Node) super.clone();
            clone.parentNode = parent;
            clone.siblingIndex = parent == null ? 0 : this.siblingIndex;
            clone.attributes = this.attributes != null ? this.attributes.clone() : null;
            clone.baseUri = this.baseUri;
            clone.childNodes = new ArrayList(this.childNodes.size());
            for (Node child : this.childNodes) {
                clone.childNodes.add(child.doClone(clone));
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class OuterHtmlVisitor implements NodeVisitor {
        private StringBuilder accum;
        private Document.OutputSettings out;

        OuterHtmlVisitor(StringBuilder accum2, Document.OutputSettings out2) {
            this.accum = accum2;
            this.out = out2;
        }

        public void head(Node node, int depth) {
            node.outerHtmlHead(this.accum, depth, this.out);
        }

        public void tail(Node node, int depth) {
            if (!node.nodeName().equals("#text")) {
                node.outerHtmlTail(this.accum, depth, this.out);
            }
        }
    }
}
