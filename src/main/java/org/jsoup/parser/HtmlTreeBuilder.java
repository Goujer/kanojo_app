package org.jsoup.parser;

import com.google.ads.AdActivity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.helper.DescendableLinkedList;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;

class HtmlTreeBuilder extends TreeBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = (!HtmlTreeBuilder.class.desiredAssertionStatus());
    private boolean baseUriSetFromDoc = false;
    private Element contextElement;
    private Element formElement;
    private DescendableLinkedList<Element> formattingElements = new DescendableLinkedList<>();
    private boolean fosterInserts = false;
    private boolean fragmentParsing = false;
    private boolean framesetOk = true;
    private Element headElement;
    private HtmlTreeBuilderState originalState;
    private List<Token.Character> pendingTableCharacters = new ArrayList();
    private HtmlTreeBuilderState state;

    HtmlTreeBuilder() {
    }

    /* access modifiers changed from: package-private */
    public Document parse(String input, String baseUri, ParseErrorList errors) {
        this.state = HtmlTreeBuilderState.Initial;
        return super.parse(input, baseUri, errors);
    }

    /* access modifiers changed from: package-private */
    public List<Node> parseFragment(String inputFragment, Element context, String baseUri, ParseErrorList errors) {
        this.state = HtmlTreeBuilderState.Initial;
        initialiseParse(inputFragment, baseUri, errors);
        this.contextElement = context;
        this.fragmentParsing = true;
        Element root = null;
        if (context != null) {
            if (context.ownerDocument() != null) {
                this.doc.quirksMode(context.ownerDocument().quirksMode());
            }
            String contextTag = context.tagName();
            if (StringUtil.in(contextTag, "title", "textarea")) {
                this.tokeniser.transition(TokeniserState.Rcdata);
            } else {
                if (StringUtil.in(contextTag, "iframe", "noembed", "noframes", "style", "xmp")) {
                    this.tokeniser.transition(TokeniserState.Rawtext);
                } else if (contextTag.equals("script")) {
                    this.tokeniser.transition(TokeniserState.ScriptData);
                } else if (contextTag.equals("noscript")) {
                    this.tokeniser.transition(TokeniserState.Data);
                } else if (contextTag.equals("plaintext")) {
                    this.tokeniser.transition(TokeniserState.Data);
                } else {
                    this.tokeniser.transition(TokeniserState.Data);
                }
            }
            root = new Element(Tag.valueOf(AdActivity.HTML_PARAM), baseUri);
            this.doc.appendChild(root);
            this.stack.push(root);
            resetInsertionMode();
        }
        runParser();
        if (context != null) {
            return root.childNodes();
        }
        return this.doc.childNodes();
    }

    /* access modifiers changed from: protected */
    public boolean process(Token token) {
        this.currentToken = token;
        return this.state.process(token, this);
    }

    /* access modifiers changed from: package-private */
    public boolean process(Token token, HtmlTreeBuilderState state2) {
        this.currentToken = token;
        return state2.process(token, this);
    }

    /* access modifiers changed from: package-private */
    public void transition(HtmlTreeBuilderState state2) {
        this.state = state2;
    }

    /* access modifiers changed from: package-private */
    public HtmlTreeBuilderState state() {
        return this.state;
    }

    /* access modifiers changed from: package-private */
    public void markInsertionMode() {
        this.originalState = this.state;
    }

    /* access modifiers changed from: package-private */
    public HtmlTreeBuilderState originalState() {
        return this.originalState;
    }

    /* access modifiers changed from: package-private */
    public void framesetOk(boolean framesetOk2) {
        this.framesetOk = framesetOk2;
    }

    /* access modifiers changed from: package-private */
    public boolean framesetOk() {
        return this.framesetOk;
    }

    /* access modifiers changed from: package-private */
    public Document getDocument() {
        return this.doc;
    }

    /* access modifiers changed from: package-private */
    public String getBaseUri() {
        return this.baseUri;
    }

    /* access modifiers changed from: package-private */
    public void maybeSetBaseUri(Element base) {
        if (!this.baseUriSetFromDoc) {
            String href = base.absUrl("href");
            if (href.length() != 0) {
                this.baseUri = href;
                this.baseUriSetFromDoc = true;
                this.doc.setBaseUri(href);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isFragmentParsing() {
        return this.fragmentParsing;
    }

    /* access modifiers changed from: package-private */
    public void error(HtmlTreeBuilderState state2) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpected token [%s] when in state [%s]", this.currentToken.tokenType(), state2));
        }
    }

    /* access modifiers changed from: package-private */
    public Element insert(Token.StartTag startTag) {
        if (startTag.isSelfClosing()) {
            Element el = insertEmpty(startTag);
            this.stack.add(el);
            this.tokeniser.emit((Token) new Token.EndTag(el.tagName()));
            return el;
        }
        Element el2 = new Element(Tag.valueOf(startTag.name()), this.baseUri, startTag.attributes);
        insert(el2);
        return el2;
    }

    /* access modifiers changed from: package-private */
    public Element insert(String startTagName) {
        Element el = new Element(Tag.valueOf(startTagName), this.baseUri);
        insert(el);
        return el;
    }

    /* access modifiers changed from: package-private */
    public void insert(Element el) {
        insertNode(el);
        this.stack.add(el);
    }

    /* access modifiers changed from: package-private */
    public Element insertEmpty(Token.StartTag startTag) {
        Tag tag = Tag.valueOf(startTag.name());
        Element el = new Element(tag, this.baseUri, startTag.attributes);
        insertNode(el);
        if (startTag.isSelfClosing()) {
            if (!tag.isKnownTag()) {
                tag.setSelfClosing();
                this.tokeniser.acknowledgeSelfClosingFlag();
            } else if (tag.isSelfClosing()) {
                this.tokeniser.acknowledgeSelfClosingFlag();
            }
        }
        return el;
    }

    /* access modifiers changed from: package-private */
    public void insert(Token.Comment commentToken) {
        insertNode(new Comment(commentToken.getData(), this.baseUri));
    }

    /* access modifiers changed from: package-private */
    public void insert(Token.Character characterToken) {
        Node node;
        if (StringUtil.in(currentElement().tagName(), "script", "style")) {
            node = new DataNode(characterToken.getData(), this.baseUri);
        } else {
            node = new TextNode(characterToken.getData(), this.baseUri);
        }
        currentElement().appendChild(node);
    }

    private void insertNode(Node node) {
        if (this.stack.size() == 0) {
            this.doc.appendChild(node);
        } else if (isFosterInserts()) {
            insertInFosterParent(node);
        } else {
            currentElement().appendChild(node);
        }
    }

    /* access modifiers changed from: package-private */
    public Element pop() {
        if (((Element) this.stack.peekLast()).nodeName().equals("td") && !this.state.name().equals("InCell")) {
            Validate.isFalse(true, "pop td not in cell");
        }
        if (((Element) this.stack.peekLast()).nodeName().equals(AdActivity.HTML_PARAM)) {
            Validate.isFalse(true, "popping html!");
        }
        return (Element) this.stack.pollLast();
    }

    /* access modifiers changed from: package-private */
    public void push(Element element) {
        this.stack.add(element);
    }

    /* access modifiers changed from: package-private */
    public DescendableLinkedList<Element> getStack() {
        return this.stack;
    }

    /* access modifiers changed from: package-private */
    public boolean onStack(Element el) {
        return isElementInQueue(this.stack, el);
    }

    private boolean isElementInQueue(DescendableLinkedList<Element> queue, Element element) {
        Iterator<Element> it = queue.descendingIterator();
        while (it.hasNext()) {
            if (it.next() == element) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Element getFromStack(String elName) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            Element next = it.next();
            if (next.nodeName().equals(elName)) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean removeFromStack(Element el) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            if (it.next() == el) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void popStackToClose(String elName) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            if (it.next().nodeName().equals(elName)) {
                it.remove();
                return;
            }
            it.remove();
        }
    }

    /* access modifiers changed from: package-private */
    public void popStackToClose(String... elNames) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            if (StringUtil.in(it.next().nodeName(), elNames)) {
                it.remove();
                return;
            }
            it.remove();
        }
    }

    /* access modifiers changed from: package-private */
    public void popStackToBefore(String elName) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext() && !it.next().nodeName().equals(elName)) {
            it.remove();
        }
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableContext() {
        clearStackToContext("table");
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableBodyContext() {
        clearStackToContext("tbody", "tfoot", "thead");
    }

    /* access modifiers changed from: package-private */
    public void clearStackToTableRowContext() {
        clearStackToContext("tr");
    }

    private void clearStackToContext(String... nodeNames) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            Element next = it.next();
            if (!StringUtil.in(next.nodeName(), nodeNames) && !next.nodeName().equals(AdActivity.HTML_PARAM)) {
                it.remove();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Element aboveOnStack(Element el) {
        if ($assertionsDisabled || onStack(el)) {
            Iterator<Element> it = this.stack.descendingIterator();
            while (it.hasNext()) {
                if (it.next() == el) {
                    return it.next();
                }
            }
            return null;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: package-private */
    public void insertOnStackAfter(Element after, Element in) {
        int i = this.stack.lastIndexOf(after);
        Validate.isTrue(i != -1);
        this.stack.add(i + 1, in);
    }

    /* access modifiers changed from: package-private */
    public void replaceOnStack(Element out, Element in) {
        replaceInQueue(this.stack, out, in);
    }

    private void replaceInQueue(LinkedList<Element> queue, Element out, Element in) {
        int i = queue.lastIndexOf(out);
        Validate.isTrue(i != -1);
        queue.remove(i);
        queue.add(i, in);
    }

    /* access modifiers changed from: package-private */
    public void resetInsertionMode() {
        boolean last = false;
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            Element node = it.next();
            if (!it.hasNext()) {
                last = true;
                node = this.contextElement;
            }
            String name = node.nodeName();
            if ("select".equals(name)) {
                transition(HtmlTreeBuilderState.InSelect);
                return;
            } else if ("td".equals(name) || ("td".equals(name) && !last)) {
                transition(HtmlTreeBuilderState.InCell);
                return;
            } else if ("tr".equals(name)) {
                transition(HtmlTreeBuilderState.InRow);
                return;
            } else if ("tbody".equals(name) || "thead".equals(name) || "tfoot".equals(name)) {
                transition(HtmlTreeBuilderState.InTableBody);
                return;
            } else if ("caption".equals(name)) {
                transition(HtmlTreeBuilderState.InCaption);
                return;
            } else if ("colgroup".equals(name)) {
                transition(HtmlTreeBuilderState.InColumnGroup);
                return;
            } else if ("table".equals(name)) {
                transition(HtmlTreeBuilderState.InTable);
                return;
            } else if ("head".equals(name)) {
                transition(HtmlTreeBuilderState.InBody);
                return;
            } else if ("body".equals(name)) {
                transition(HtmlTreeBuilderState.InBody);
                return;
            } else if ("frameset".equals(name)) {
                transition(HtmlTreeBuilderState.InFrameset);
                return;
            } else if (AdActivity.HTML_PARAM.equals(name)) {
                transition(HtmlTreeBuilderState.BeforeHead);
                return;
            } else if (last) {
                transition(HtmlTreeBuilderState.InBody);
                return;
            }
        }
    }

    private boolean inSpecificScope(String targetName, String[] baseTypes, String[] extraTypes) {
        return inSpecificScope(new String[]{targetName}, baseTypes, extraTypes);
    }

    private boolean inSpecificScope(String[] targetNames, String[] baseTypes, String[] extraTypes) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            String elName = it.next().nodeName();
            if (StringUtil.in(elName, targetNames)) {
                return true;
            }
            if (StringUtil.in(elName, baseTypes)) {
                return false;
            }
            if (extraTypes != null && StringUtil.in(elName, extraTypes)) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String[] targetNames) {
        return inSpecificScope(targetNames, new String[]{"applet", "caption", AdActivity.HTML_PARAM, "table", "td", "th", "marquee", "object"}, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String targetName) {
        return inScope(targetName, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inScope(String targetName, String[] extras) {
        return inSpecificScope(targetName, new String[]{"applet", "caption", AdActivity.HTML_PARAM, "table", "td", "th", "marquee", "object"}, extras);
    }

    /* access modifiers changed from: package-private */
    public boolean inListItemScope(String targetName) {
        return inScope(targetName, new String[]{"ol", "ul"});
    }

    /* access modifiers changed from: package-private */
    public boolean inButtonScope(String targetName) {
        return inScope(targetName, new String[]{"button"});
    }

    /* access modifiers changed from: package-private */
    public boolean inTableScope(String targetName) {
        return inSpecificScope(targetName, new String[]{AdActivity.HTML_PARAM, "table"}, (String[]) null);
    }

    /* access modifiers changed from: package-private */
    public boolean inSelectScope(String targetName) {
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            String elName = it.next().nodeName();
            if (elName.equals(targetName)) {
                return true;
            }
            if (!StringUtil.in(elName, "optgroup", "option")) {
                return false;
            }
        }
        Validate.fail("Should not be reachable");
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setHeadElement(Element headElement2) {
        this.headElement = headElement2;
    }

    /* access modifiers changed from: package-private */
    public Element getHeadElement() {
        return this.headElement;
    }

    /* access modifiers changed from: package-private */
    public boolean isFosterInserts() {
        return this.fosterInserts;
    }

    /* access modifiers changed from: package-private */
    public void setFosterInserts(boolean fosterInserts2) {
        this.fosterInserts = fosterInserts2;
    }

    /* access modifiers changed from: package-private */
    public Element getFormElement() {
        return this.formElement;
    }

    /* access modifiers changed from: package-private */
    public void setFormElement(Element formElement2) {
        this.formElement = formElement2;
    }

    /* access modifiers changed from: package-private */
    public void newPendingTableCharacters() {
        this.pendingTableCharacters = new ArrayList();
    }

    /* access modifiers changed from: package-private */
    public List<Token.Character> getPendingTableCharacters() {
        return this.pendingTableCharacters;
    }

    /* access modifiers changed from: package-private */
    public void setPendingTableCharacters(List<Token.Character> pendingTableCharacters2) {
        this.pendingTableCharacters = pendingTableCharacters2;
    }

    /* access modifiers changed from: package-private */
    public void generateImpliedEndTags(String excludeTag) {
        while (excludeTag != null && !currentElement().nodeName().equals(excludeTag)) {
            if (StringUtil.in(currentElement().nodeName(), "dd", "dt", "li", "option", "optgroup", AdActivity.PACKAGE_NAME_PARAM, "rp", "rt")) {
                pop();
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void generateImpliedEndTags() {
        generateImpliedEndTags((String) null);
    }

    /* access modifiers changed from: package-private */
    public boolean isSpecial(Element el) {
        return StringUtil.in(el.nodeName(), "address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", AdActivity.HTML_PARAM, "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", AdActivity.PACKAGE_NAME_PARAM, "param", "plaintext", "pre", "script", "section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp");
    }

    /* access modifiers changed from: package-private */
    public void pushActiveFormattingElements(Element in) {
        Element el;
        int numSeen = 0;
        Iterator<Element> iter = this.formattingElements.descendingIterator();
        while (true) {
            if (!iter.hasNext() || (el = iter.next()) == null) {
                break;
            }
            if (isSameFormattingElement(in, el)) {
                numSeen++;
            }
            if (numSeen == 3) {
                iter.remove();
                break;
            }
        }
        this.formattingElements.add(in);
    }

    private boolean isSameFormattingElement(Element a, Element b) {
        return a.nodeName().equals(b.nodeName()) && a.attributes().equals(b.attributes());
    }

    /* access modifiers changed from: package-private */
    public void reconstructFormattingElements() {
        int size = this.formattingElements.size();
        if (size != 0 && this.formattingElements.getLast() != null && !onStack((Element) this.formattingElements.getLast())) {
            Element entry = (Element) this.formattingElements.getLast();
            int pos = size - 1;
            boolean skip = false;
            while (true) {
                if (pos != 0) {
                    pos--;
                    entry = (Element) this.formattingElements.get(pos);
                    if (entry != null) {
                        if (onStack(entry)) {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    skip = true;
                    break;
                }
            }
            do {
                if (!skip) {
                    pos++;
                    entry = (Element) this.formattingElements.get(pos);
                }
                Validate.notNull(entry);
                skip = false;
                Element newEl = insert(entry.nodeName());
                newEl.attributes().addAll(entry.attributes());
                this.formattingElements.add(pos, newEl);
                this.formattingElements.remove(pos + 1);
            } while (pos != size - 1);
        }
    }

    /* access modifiers changed from: package-private */
    public void clearFormattingElementsToLastMarker() {
        while (!this.formattingElements.isEmpty()) {
            Element el = this.formattingElements.peekLast();
            this.formattingElements.removeLast();
            if (el == null) {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void removeFromActiveFormattingElements(Element el) {
        Iterator<Element> it = this.formattingElements.descendingIterator();
        while (it.hasNext()) {
            if (it.next() == el) {
                it.remove();
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isInActiveFormattingElements(Element el) {
        return isElementInQueue(this.formattingElements, el);
    }

    /* access modifiers changed from: package-private */
    public Element getActiveFormattingElement(String nodeName) {
        Element next;
        Iterator<Element> it = this.formattingElements.descendingIterator();
        while (it.hasNext() && (next = it.next()) != null) {
            if (next.nodeName().equals(nodeName)) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void replaceActiveFormattingElement(Element out, Element in) {
        replaceInQueue(this.formattingElements, out, in);
    }

    /* access modifiers changed from: package-private */
    public void insertMarkerToFormattingElements() {
        this.formattingElements.add((Object) null);
    }

    /* access modifiers changed from: package-private */
    public void insertInFosterParent(Node in) {
        Element fosterParent;
        Element lastTable = getFromStack("table");
        boolean isLastTableParent = false;
        if (lastTable == null) {
            fosterParent = (Element) this.stack.get(0);
        } else if (lastTable.parent() != null) {
            fosterParent = lastTable.parent();
            isLastTableParent = true;
        } else {
            fosterParent = aboveOnStack(lastTable);
        }
        if (isLastTableParent) {
            Validate.notNull(lastTable);
            lastTable.before(in);
            return;
        }
        fosterParent.appendChild(in);
    }

    public String toString() {
        return "TreeBuilder{currentToken=" + this.currentToken + ", state=" + this.state + ", currentElement=" + currentElement() + '}';
    }
}
