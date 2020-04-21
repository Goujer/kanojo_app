package org.jsoup.parser;

import com.google.ads.AdActivity;
import java.util.Iterator;
import org.jsoup.helper.DescendableLinkedList;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token;

enum HtmlTreeBuilderState {
    Initial {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                Token.Doctype d = t.asDoctype();
                tb.getDocument().appendChild(new DocumentType(d.getName(), d.getPublicIdentifier(), d.getSystemIdentifier(), tb.getBaseUri()));
                if (d.isForceQuirks()) {
                    tb.getDocument().quirksMode(Document.QuirksMode.quirks);
                }
                tb.transition(BeforeHtml);
                return true;
            } else {
                tb.transition(BeforeHtml);
                return tb.process(t);
            }
        }
    },
    BeforeHtml {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isDoctype()) {
                tb.error(this);
                return false;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            } else {
                if (!t.isStartTag() || !t.asStartTag().name().equals(AdActivity.HTML_PARAM)) {
                    if (t.isEndTag()) {
                        if (StringUtil.in(t.asEndTag().name(), "head", "body", AdActivity.HTML_PARAM, "br")) {
                            return anythingElse(t, tb);
                        }
                    }
                    if (!t.isEndTag()) {
                        return anythingElse(t, tb);
                    }
                    tb.error(this);
                    return false;
                }
                tb.insert(t.asStartTag());
                tb.transition(BeforeHead);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.insert(AdActivity.HTML_PARAM);
            tb.transition(BeforeHead);
            return tb.process(t);
        }
    },
    BeforeHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return true;
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM)) {
                return InBody.process(t, tb);
            } else {
                if (!t.isStartTag() || !t.asStartTag().name().equals("head")) {
                    if (t.isEndTag()) {
                        if (StringUtil.in(t.asEndTag().name(), "head", "body", AdActivity.HTML_PARAM, "br")) {
                            tb.process(new Token.StartTag("head"));
                            return tb.process(t);
                        }
                    }
                    if (t.isEndTag()) {
                        tb.error(this);
                        return false;
                    }
                    tb.process(new Token.StartTag("head"));
                    return tb.process(t);
                }
                tb.setHeadElement(tb.insert(t.asStartTag()));
                tb.transition(InHead);
                return true;
            }
        }
    },
    InHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
                return true;
            }
            switch (t.type) {
                case Comment:
                    tb.insert(t.asComment());
                    return true;
                case Doctype:
                    tb.error(this);
                    return false;
                case StartTag:
                    Token.StartTag start = t.asStartTag();
                    String name = start.name();
                    if (name.equals(AdActivity.HTML_PARAM)) {
                        return InBody.process(t, tb);
                    }
                    if (StringUtil.in(name, "base", "basefont", "bgsound", "command", "link")) {
                        Element el = tb.insertEmpty(start);
                        if (!name.equals("base") || !el.hasAttr("href")) {
                            return true;
                        }
                        tb.maybeSetBaseUri(el);
                        return true;
                    } else if (name.equals("meta")) {
                        tb.insertEmpty(start);
                        return true;
                    } else if (name.equals("title")) {
                        HtmlTreeBuilderState.handleRcData(start, tb);
                        return true;
                    } else {
                        if (StringUtil.in(name, "noframes", "style")) {
                            HtmlTreeBuilderState.handleRawtext(start, tb);
                            return true;
                        } else if (name.equals("noscript")) {
                            tb.insert(start);
                            tb.transition(InHeadNoscript);
                            return true;
                        } else if (name.equals("script")) {
                            tb.insert(start);
                            tb.tokeniser.transition(TokeniserState.ScriptData);
                            tb.markInsertionMode();
                            tb.transition(Text);
                            return true;
                        } else if (!name.equals("head")) {
                            return anythingElse(t, tb);
                        } else {
                            tb.error(this);
                            return false;
                        }
                    }
                case EndTag:
                    String name2 = t.asEndTag().name();
                    if (name2.equals("head")) {
                        tb.pop();
                        tb.transition(AfterHead);
                        return true;
                    }
                    if (StringUtil.in(name2, "body", AdActivity.HTML_PARAM, "br")) {
                        return anythingElse(t, tb);
                    }
                    tb.error(this);
                    return false;
                default:
                    return anythingElse(t, tb);
            }
        }

        private boolean anythingElse(Token t, TreeBuilder tb) {
            tb.process(new Token.EndTag("head"));
            return tb.process(t);
        }
    },
    InHeadNoscript {
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0086, code lost:
            if (org.jsoup.helper.StringUtil.in(r8.asStartTag().name(), "basefont", "bgsound", "link", "meta", "noframes", "style") != false) goto L_0x0088;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00c8, code lost:
            if (org.jsoup.helper.StringUtil.in(r8.asStartTag().name(), "head", "noscript") == false) goto L_0x00ca;
         */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isDoctype()) {
                tb.error(this);
            } else if (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM)) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEndTag() || !t.asEndTag().name().equals("noscript")) {
                    if (!HtmlTreeBuilderState.isWhitespace(t) && !t.isComment()) {
                        if (t.isStartTag()) {
                        }
                        if (t.isEndTag() && t.asEndTag().name().equals("br")) {
                            return anythingElse(t, tb);
                        }
                        if (t.isStartTag()) {
                        }
                        if (!t.isEndTag()) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    }
                    return tb.process(t, InHead);
                }
                tb.pop();
                tb.transition(InHead);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            tb.process(new Token.EndTag("noscript"));
            return tb.process(t);
        }
    },
    AfterHead {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
            } else if (t.isStartTag()) {
                Token.StartTag startTag = t.asStartTag();
                String name = startTag.name();
                if (name.equals(AdActivity.HTML_PARAM)) {
                    return tb.process(t, InBody);
                }
                if (name.equals("body")) {
                    tb.insert(startTag);
                    tb.framesetOk(false);
                    tb.transition(InBody);
                } else if (name.equals("frameset")) {
                    tb.insert(startTag);
                    tb.transition(InFrameset);
                } else {
                    if (StringUtil.in(name, "base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "title")) {
                        tb.error(this);
                        Element head = tb.getHeadElement();
                        tb.push(head);
                        tb.process(t, InHead);
                        tb.removeFromStack(head);
                    } else if (name.equals("head")) {
                        tb.error(this);
                        return false;
                    } else {
                        anythingElse(t, tb);
                    }
                }
            } else if (t.isEndTag()) {
                if (StringUtil.in(t.asEndTag().name(), "body", AdActivity.HTML_PARAM)) {
                    anythingElse(t, tb);
                } else {
                    tb.error(this);
                    return false;
                }
            } else {
                anythingElse(t, tb);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.process(new Token.StartTag("body"));
            tb.framesetOk(true);
            return tb.process(t);
        }
    },
    InBody {
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:367:0x10f4  */
        /* JADX WARNING: Removed duplicated region for block: B:374:0x1135 A[LOOP:9: B:372:0x112f->B:374:0x1135, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:381:0x1182  */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            int len$;
            int i$;
            switch (t.type) {
                case Comment:
                    tb.insert(t.asComment());
                    break;
                case Doctype:
                    tb.error(this);
                    return false;
                case StartTag:
                    Token.StartTag startTag = t.asStartTag();
                    String name = startTag.name();
                    if (!name.equals(AdActivity.HTML_PARAM)) {
                        if (!StringUtil.in(name, "base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "title")) {
                            if (!name.equals("body")) {
                                if (!name.equals("frameset")) {
                                    if (!StringUtil.in(name, "address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", AdActivity.PACKAGE_NAME_PARAM, "section", "summary", "ul")) {
                                        if (!StringUtil.in(name, "h1", "h2", "h3", "h4", "h5", "h6")) {
                                            if (!StringUtil.in(name, "pre", "listing")) {
                                                if (!name.equals("form")) {
                                                    if (!name.equals("li")) {
                                                        if (!StringUtil.in(name, "dd", "dt")) {
                                                            if (!name.equals("plaintext")) {
                                                                if (!name.equals("button")) {
                                                                    if (!name.equals("a")) {
                                                                        if (!StringUtil.in(name, "b", "big", "code", "em", "font", AdActivity.INTENT_ACTION_PARAM, "s", "small", "strike", "strong", "tt", AdActivity.URL_PARAM)) {
                                                                            if (!name.equals("nobr")) {
                                                                                if (!StringUtil.in(name, "applet", "marquee", "object")) {
                                                                                    if (!name.equals("table")) {
                                                                                        if (!StringUtil.in(name, "area", "br", "embed", "img", "keygen", "wbr")) {
                                                                                            if (!name.equals("input")) {
                                                                                                if (!StringUtil.in(name, "param", "source", "track")) {
                                                                                                    if (!name.equals("hr")) {
                                                                                                        if (!name.equals("image")) {
                                                                                                            if (!name.equals("isindex")) {
                                                                                                                if (!name.equals("textarea")) {
                                                                                                                    if (!name.equals("xmp")) {
                                                                                                                        if (!name.equals("iframe")) {
                                                                                                                            if (!name.equals("noembed")) {
                                                                                                                                if (!name.equals("select")) {
                                                                                                                                    if (!StringUtil.in("optgroup", "option")) {
                                                                                                                                        if (!StringUtil.in("rp", "rt")) {
                                                                                                                                            if (!name.equals("math")) {
                                                                                                                                                if (!name.equals("svg")) {
                                                                                                                                                    if (!StringUtil.in(name, "caption", "col", "colgroup", "frame", "head", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                                                                                                                                                        tb.reconstructFormattingElements();
                                                                                                                                                        tb.insert(startTag);
                                                                                                                                                        break;
                                                                                                                                                    } else {
                                                                                                                                                        tb.error(this);
                                                                                                                                                        return false;
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    tb.reconstructFormattingElements();
                                                                                                                                                    tb.insert(startTag);
                                                                                                                                                    tb.tokeniser.acknowledgeSelfClosingFlag();
                                                                                                                                                    break;
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                tb.reconstructFormattingElements();
                                                                                                                                                tb.insert(startTag);
                                                                                                                                                tb.tokeniser.acknowledgeSelfClosingFlag();
                                                                                                                                                break;
                                                                                                                                            }
                                                                                                                                        } else if (tb.inScope("ruby")) {
                                                                                                                                            tb.generateImpliedEndTags();
                                                                                                                                            if (!tb.currentElement().nodeName().equals("ruby")) {
                                                                                                                                                tb.error(this);
                                                                                                                                                tb.popStackToBefore("ruby");
                                                                                                                                            }
                                                                                                                                            tb.insert(startTag);
                                                                                                                                            break;
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        if (tb.currentElement().nodeName().equals("option")) {
                                                                                                                                            tb.process(new Token.EndTag("option"));
                                                                                                                                        }
                                                                                                                                        tb.reconstructFormattingElements();
                                                                                                                                        tb.insert(startTag);
                                                                                                                                        break;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    tb.reconstructFormattingElements();
                                                                                                                                    tb.insert(startTag);
                                                                                                                                    tb.framesetOk(false);
                                                                                                                                    HtmlTreeBuilderState state = tb.state();
                                                                                                                                    if (!state.equals(InTable) && !state.equals(InCaption) && !state.equals(InTableBody) && !state.equals(InRow) && !state.equals(InCell)) {
                                                                                                                                        tb.transition(InSelect);
                                                                                                                                        break;
                                                                                                                                    } else {
                                                                                                                                        tb.transition(InSelectInTable);
                                                                                                                                        break;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                HtmlTreeBuilderState.handleRawtext(startTag, tb);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            tb.framesetOk(false);
                                                                                                                            HtmlTreeBuilderState.handleRawtext(startTag, tb);
                                                                                                                            break;
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                                                                                            tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                                                                                        }
                                                                                                                        tb.reconstructFormattingElements();
                                                                                                                        tb.framesetOk(false);
                                                                                                                        HtmlTreeBuilderState.handleRawtext(startTag, tb);
                                                                                                                        break;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    tb.insert(startTag);
                                                                                                                    tb.tokeniser.transition(TokeniserState.Rcdata);
                                                                                                                    tb.markInsertionMode();
                                                                                                                    tb.framesetOk(false);
                                                                                                                    tb.transition(Text);
                                                                                                                    break;
                                                                                                                }
                                                                                                            } else {
                                                                                                                tb.error(this);
                                                                                                                if (tb.getFormElement() == null) {
                                                                                                                    tb.tokeniser.acknowledgeSelfClosingFlag();
                                                                                                                    tb.process(new Token.StartTag("form"));
                                                                                                                    if (startTag.attributes.hasKey("action")) {
                                                                                                                        tb.getFormElement().attr("action", startTag.attributes.get("action"));
                                                                                                                    }
                                                                                                                    tb.process(new Token.StartTag("hr"));
                                                                                                                    tb.process(new Token.StartTag("label"));
                                                                                                                    tb.process(new Token.Character(startTag.attributes.hasKey("prompt") ? startTag.attributes.get("prompt") : "This is a searchable index. Enter search keywords: "));
                                                                                                                    Attributes inputAttribs = new Attributes();
                                                                                                                    Iterator i$2 = startTag.attributes.iterator();
                                                                                                                    while (i$2.hasNext()) {
                                                                                                                        Attribute attr = i$2.next();
                                                                                                                        if (!StringUtil.in(attr.getKey(), "name", "action", "prompt")) {
                                                                                                                            inputAttribs.put(attr);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    inputAttribs.put("name", "isindex");
                                                                                                                    tb.process(new Token.StartTag("input", inputAttribs));
                                                                                                                    tb.process(new Token.EndTag("label"));
                                                                                                                    tb.process(new Token.StartTag("hr"));
                                                                                                                    tb.process(new Token.EndTag("form"));
                                                                                                                    break;
                                                                                                                } else {
                                                                                                                    return false;
                                                                                                                }
                                                                                                            }
                                                                                                        } else {
                                                                                                            startTag.name("img");
                                                                                                            return tb.process(startTag);
                                                                                                        }
                                                                                                    } else {
                                                                                                        if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                                                                            tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                                                                        }
                                                                                                        tb.insertEmpty(startTag);
                                                                                                        tb.framesetOk(false);
                                                                                                        break;
                                                                                                    }
                                                                                                } else {
                                                                                                    tb.insertEmpty(startTag);
                                                                                                    break;
                                                                                                }
                                                                                            } else {
                                                                                                tb.reconstructFormattingElements();
                                                                                                if (!tb.insertEmpty(startTag).attr("type").equalsIgnoreCase("hidden")) {
                                                                                                    tb.framesetOk(false);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            tb.reconstructFormattingElements();
                                                                                            tb.insertEmpty(startTag);
                                                                                            tb.framesetOk(false);
                                                                                            break;
                                                                                        }
                                                                                    } else {
                                                                                        if (tb.getDocument().quirksMode() != Document.QuirksMode.quirks && tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                                                            tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                                                        }
                                                                                        tb.insert(startTag);
                                                                                        tb.framesetOk(false);
                                                                                        tb.transition(InTable);
                                                                                        break;
                                                                                    }
                                                                                } else {
                                                                                    tb.reconstructFormattingElements();
                                                                                    tb.insert(startTag);
                                                                                    tb.insertMarkerToFormattingElements();
                                                                                    tb.framesetOk(false);
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                tb.reconstructFormattingElements();
                                                                                if (tb.inScope("nobr")) {
                                                                                    tb.error(this);
                                                                                    tb.process(new Token.EndTag("nobr"));
                                                                                    tb.reconstructFormattingElements();
                                                                                }
                                                                                tb.pushActiveFormattingElements(tb.insert(startTag));
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            tb.reconstructFormattingElements();
                                                                            tb.pushActiveFormattingElements(tb.insert(startTag));
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        if (tb.getActiveFormattingElement("a") != null) {
                                                                            tb.error(this);
                                                                            tb.process(new Token.EndTag("a"));
                                                                            Element remainingA = tb.getFromStack("a");
                                                                            if (remainingA != null) {
                                                                                tb.removeFromActiveFormattingElements(remainingA);
                                                                                tb.removeFromStack(remainingA);
                                                                            }
                                                                        }
                                                                        tb.reconstructFormattingElements();
                                                                        tb.pushActiveFormattingElements(tb.insert(startTag));
                                                                        break;
                                                                    }
                                                                } else if (!tb.inButtonScope("button")) {
                                                                    tb.reconstructFormattingElements();
                                                                    tb.insert(startTag);
                                                                    tb.framesetOk(false);
                                                                    break;
                                                                } else {
                                                                    tb.error(this);
                                                                    tb.process(new Token.EndTag("button"));
                                                                    tb.process(startTag);
                                                                    break;
                                                                }
                                                            } else {
                                                                if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                                    tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                                }
                                                                tb.insert(startTag);
                                                                tb.tokeniser.transition(TokeniserState.PLAINTEXT);
                                                                break;
                                                            }
                                                        } else {
                                                            tb.framesetOk(false);
                                                            DescendableLinkedList<Element> stack = tb.getStack();
                                                            int i = stack.size() - 1;
                                                            while (true) {
                                                                if (i > 0) {
                                                                    Element el = stack.get(i);
                                                                    if (StringUtil.in(el.nodeName(), "dd", "dt")) {
                                                                        tb.process(new Token.EndTag(el.nodeName()));
                                                                    } else {
                                                                        if (tb.isSpecial(el)) {
                                                                            if (StringUtil.in(el.nodeName(), "address", "div", AdActivity.PACKAGE_NAME_PARAM)) {
                                                                            }
                                                                        }
                                                                        i--;
                                                                    }
                                                                }
                                                            }
                                                            if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                                tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                            }
                                                            tb.insert(startTag);
                                                            break;
                                                        }
                                                    } else {
                                                        tb.framesetOk(false);
                                                        DescendableLinkedList<Element> stack2 = tb.getStack();
                                                        int i2 = stack2.size() - 1;
                                                        while (true) {
                                                            if (i2 > 0) {
                                                                Element el2 = stack2.get(i2);
                                                                if (el2.nodeName().equals("li")) {
                                                                    tb.process(new Token.EndTag("li"));
                                                                } else {
                                                                    if (tb.isSpecial(el2)) {
                                                                        if (StringUtil.in(el2.nodeName(), "address", "div", AdActivity.PACKAGE_NAME_PARAM)) {
                                                                        }
                                                                    }
                                                                    i2--;
                                                                }
                                                            }
                                                        }
                                                        if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                            tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                        }
                                                        tb.insert(startTag);
                                                        break;
                                                    }
                                                } else if (tb.getFormElement() == null) {
                                                    if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                        tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                    }
                                                    tb.setFormElement(tb.insert(startTag));
                                                    break;
                                                } else {
                                                    tb.error(this);
                                                    return false;
                                                }
                                            } else {
                                                if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                    tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                                }
                                                tb.insert(startTag);
                                                tb.framesetOk(false);
                                                break;
                                            }
                                        } else {
                                            if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                                tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                            }
                                            if (StringUtil.in(tb.currentElement().nodeName(), "h1", "h2", "h3", "h4", "h5", "h6")) {
                                                tb.error(this);
                                                tb.pop();
                                            }
                                            tb.insert(startTag);
                                            break;
                                        }
                                    } else {
                                        if (tb.inButtonScope(AdActivity.PACKAGE_NAME_PARAM)) {
                                            tb.process(new Token.EndTag(AdActivity.PACKAGE_NAME_PARAM));
                                        }
                                        tb.insert(startTag);
                                        break;
                                    }
                                } else {
                                    tb.error(this);
                                    DescendableLinkedList<Element> stack3 = tb.getStack();
                                    if (stack3.size() != 1 && ((stack3.size() <= 2 || stack3.get(1).nodeName().equals("body")) && tb.framesetOk())) {
                                        Element second = stack3.get(1);
                                        if (second.parent() != null) {
                                            second.remove();
                                        }
                                        while (stack3.size() > 1) {
                                            stack3.removeLast();
                                        }
                                        tb.insert(startTag);
                                        tb.transition(InFrameset);
                                        break;
                                    } else {
                                        return false;
                                    }
                                }
                            } else {
                                tb.error(this);
                                DescendableLinkedList<Element> stack4 = tb.getStack();
                                if (stack4.size() != 1 && (stack4.size() <= 2 || stack4.get(1).nodeName().equals("body"))) {
                                    tb.framesetOk(false);
                                    Element body = stack4.get(1);
                                    Iterator i$3 = startTag.getAttributes().iterator();
                                    while (i$3.hasNext()) {
                                        Attribute attribute = i$3.next();
                                        if (!body.hasAttr(attribute.getKey())) {
                                            body.attributes().put(attribute);
                                        }
                                    }
                                    break;
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            return tb.process(t, InHead);
                        }
                    } else {
                        tb.error(this);
                        Element html = (Element) tb.getStack().getFirst();
                        Iterator i$4 = startTag.getAttributes().iterator();
                        while (i$4.hasNext()) {
                            Attribute attribute2 = i$4.next();
                            if (!html.hasAttr(attribute2.getKey())) {
                                html.attributes().put(attribute2);
                            }
                        }
                        break;
                    }
                    break;
                case EndTag:
                    Token.EndTag endTag = t.asEndTag();
                    String name2 = endTag.name();
                    if (name2.equals("body")) {
                        if (tb.inScope("body")) {
                            tb.transition(AfterBody);
                            break;
                        } else {
                            tb.error(this);
                            return false;
                        }
                    } else if (!name2.equals(AdActivity.HTML_PARAM)) {
                        if (StringUtil.in(name2, "address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul")) {
                            if (tb.inScope(name2)) {
                                tb.generateImpliedEndTags();
                                if (!tb.currentElement().nodeName().equals(name2)) {
                                    tb.error(this);
                                }
                                tb.popStackToClose(name2);
                                break;
                            } else {
                                tb.error(this);
                                return false;
                            }
                        } else if (name2.equals("form")) {
                            Element currentForm = tb.getFormElement();
                            tb.setFormElement((Element) null);
                            if (currentForm != null && tb.inScope(name2)) {
                                tb.generateImpliedEndTags();
                                if (!tb.currentElement().nodeName().equals(name2)) {
                                    tb.error(this);
                                }
                                tb.removeFromStack(currentForm);
                                break;
                            } else {
                                tb.error(this);
                                return false;
                            }
                        } else if (name2.equals(AdActivity.PACKAGE_NAME_PARAM)) {
                            if (tb.inButtonScope(name2)) {
                                tb.generateImpliedEndTags(name2);
                                if (!tb.currentElement().nodeName().equals(name2)) {
                                    tb.error(this);
                                }
                                tb.popStackToClose(name2);
                                break;
                            } else {
                                tb.error(this);
                                tb.process(new Token.StartTag(name2));
                                return tb.process(endTag);
                            }
                        } else if (name2.equals("li")) {
                            if (tb.inListItemScope(name2)) {
                                tb.generateImpliedEndTags(name2);
                                if (!tb.currentElement().nodeName().equals(name2)) {
                                    tb.error(this);
                                }
                                tb.popStackToClose(name2);
                                break;
                            } else {
                                tb.error(this);
                                return false;
                            }
                        } else {
                            if (StringUtil.in(name2, "dd", "dt")) {
                                if (tb.inScope(name2)) {
                                    tb.generateImpliedEndTags(name2);
                                    if (!tb.currentElement().nodeName().equals(name2)) {
                                        tb.error(this);
                                    }
                                    tb.popStackToClose(name2);
                                    break;
                                } else {
                                    tb.error(this);
                                    return false;
                                }
                            } else {
                                if (StringUtil.in(name2, "h1", "h2", "h3", "h4", "h5", "h6")) {
                                    if (tb.inScope(new String[]{"h1", "h2", "h3", "h4", "h5", "h6"})) {
                                        tb.generateImpliedEndTags(name2);
                                        if (!tb.currentElement().nodeName().equals(name2)) {
                                            tb.error(this);
                                        }
                                        tb.popStackToClose("h1", "h2", "h3", "h4", "h5", "h6");
                                        break;
                                    } else {
                                        tb.error(this);
                                        return false;
                                    }
                                } else if (name2.equals("sarcasm")) {
                                    return anyOtherEndTag(t, tb);
                                } else {
                                    if (StringUtil.in(name2, "a", "b", "big", "code", "em", "font", AdActivity.INTENT_ACTION_PARAM, "nobr", "s", "small", "strike", "strong", "tt", AdActivity.URL_PARAM)) {
                                        int i3 = 0;
                                        while (i3 < 8) {
                                            Element formatEl = tb.getActiveFormattingElement(name2);
                                            if (formatEl == null) {
                                                return anyOtherEndTag(t, tb);
                                            }
                                            if (!tb.onStack(formatEl)) {
                                                tb.error(this);
                                                tb.removeFromActiveFormattingElements(formatEl);
                                                return true;
                                            } else if (!tb.inScope(formatEl.nodeName())) {
                                                tb.error(this);
                                                return false;
                                            } else {
                                                if (tb.currentElement() != formatEl) {
                                                    tb.error(this);
                                                }
                                                Element furthestBlock = null;
                                                Element commonAncestor = null;
                                                boolean seenFormattingElement = false;
                                                DescendableLinkedList<Element> stack5 = tb.getStack();
                                                int si = 0;
                                                while (true) {
                                                    if (si < stack5.size() && si < 64) {
                                                        Element el3 = stack5.get(si);
                                                        if (el3 == formatEl) {
                                                            commonAncestor = stack5.get(si - 1);
                                                            seenFormattingElement = true;
                                                        } else if (seenFormattingElement && tb.isSpecial(el3)) {
                                                            furthestBlock = el3;
                                                        }
                                                        si++;
                                                    }
                                                }
                                                if (furthestBlock == null) {
                                                    tb.popStackToClose(formatEl.nodeName());
                                                    tb.removeFromActiveFormattingElements(formatEl);
                                                    return true;
                                                }
                                                Element node = furthestBlock;
                                                Element lastNode = furthestBlock;
                                                for (int j = 0; j < 3; j++) {
                                                    if (tb.onStack(node)) {
                                                        node = tb.aboveOnStack(node);
                                                    }
                                                    if (!tb.isInActiveFormattingElements(node)) {
                                                        tb.removeFromStack(node);
                                                    } else if (node == formatEl) {
                                                        if (!StringUtil.in(commonAncestor.nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                                                            if (lastNode.parent() != null) {
                                                                lastNode.remove();
                                                            }
                                                            tb.insertInFosterParent(lastNode);
                                                        } else {
                                                            if (lastNode.parent() != null) {
                                                                lastNode.remove();
                                                            }
                                                            commonAncestor.appendChild(lastNode);
                                                        }
                                                        Element adopter = new Element(Tag.valueOf(name2), tb.getBaseUri());
                                                        Node[] arr$ = (Node[]) furthestBlock.childNodes().toArray(new Node[furthestBlock.childNodeSize()]);
                                                        len$ = arr$.length;
                                                        for (i$ = 0; i$ < len$; i$++) {
                                                            adopter.appendChild(arr$[i$]);
                                                        }
                                                        furthestBlock.appendChild(adopter);
                                                        tb.removeFromActiveFormattingElements(formatEl);
                                                        tb.removeFromStack(formatEl);
                                                        tb.insertOnStackAfter(furthestBlock, adopter);
                                                        i3++;
                                                    } else {
                                                        Element element = new Element(Tag.valueOf(node.nodeName()), tb.getBaseUri());
                                                        tb.replaceActiveFormattingElement(node, element);
                                                        tb.replaceOnStack(node, element);
                                                        node = element;
                                                        if (lastNode == furthestBlock) {
                                                        }
                                                        if (lastNode.parent() != null) {
                                                            lastNode.remove();
                                                        }
                                                        node.appendChild(lastNode);
                                                        lastNode = node;
                                                    }
                                                }
                                                if (!StringUtil.in(commonAncestor.nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                                                }
                                                Element adopter2 = new Element(Tag.valueOf(name2), tb.getBaseUri());
                                                Node[] arr$2 = (Node[]) furthestBlock.childNodes().toArray(new Node[furthestBlock.childNodeSize()]);
                                                len$ = arr$2.length;
                                                while (i$ < len$) {
                                                }
                                                furthestBlock.appendChild(adopter2);
                                                tb.removeFromActiveFormattingElements(formatEl);
                                                tb.removeFromStack(formatEl);
                                                tb.insertOnStackAfter(furthestBlock, adopter2);
                                                i3++;
                                            }
                                        }
                                        break;
                                    } else {
                                        if (StringUtil.in(name2, "applet", "marquee", "object")) {
                                            if (!tb.inScope("name")) {
                                                if (tb.inScope(name2)) {
                                                    tb.generateImpliedEndTags();
                                                    if (!tb.currentElement().nodeName().equals(name2)) {
                                                        tb.error(this);
                                                    }
                                                    tb.popStackToClose(name2);
                                                    tb.clearFormattingElementsToLastMarker();
                                                    break;
                                                } else {
                                                    tb.error(this);
                                                    return false;
                                                }
                                            }
                                        } else if (!name2.equals("br")) {
                                            return anyOtherEndTag(t, tb);
                                        } else {
                                            tb.error(this);
                                            tb.process(new Token.StartTag("br"));
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (tb.process(new Token.EndTag("body"))) {
                        return tb.process(endTag);
                    }
                    break;
                case Character:
                    Token.Character c = t.asCharacter();
                    if (!c.getData().equals(HtmlTreeBuilderState.nullString)) {
                        if (!HtmlTreeBuilderState.isWhitespace(c)) {
                            tb.reconstructFormattingElements();
                            tb.insert(c);
                            tb.framesetOk(false);
                            break;
                        } else {
                            tb.reconstructFormattingElements();
                            tb.insert(c);
                            break;
                        }
                    } else {
                        tb.error(this);
                        return false;
                    }
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
            Element node;
            String name = t.asEndTag().name();
            Iterator<Element> it = tb.getStack().descendingIterator();
            do {
                if (it.hasNext()) {
                    node = it.next();
                    if (node.nodeName().equals(name)) {
                        tb.generateImpliedEndTags(name);
                        if (!name.equals(tb.currentElement().nodeName())) {
                            tb.error(this);
                        }
                        tb.popStackToClose(name);
                    }
                }
                return true;
            } while (!tb.isSpecial(node));
            tb.error(this);
            return false;
        }
    },
    Text {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isCharacter()) {
                tb.insert(t.asCharacter());
            } else if (t.isEOF()) {
                tb.error(this);
                tb.pop();
                tb.transition(tb.originalState());
                return tb.process(t);
            } else if (t.isEndTag()) {
                tb.pop();
                tb.transition(tb.originalState());
            }
            return true;
        }
    },
    InTable {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isCharacter()) {
                tb.newPendingTableCharacters();
                tb.markInsertionMode();
                tb.transition(InTableText);
                return tb.process(t);
            } else if (t.isComment()) {
                tb.insert(t.asComment());
                return true;
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else {
                if (t.isStartTag()) {
                    Token.StartTag startTag = t.asStartTag();
                    String name = startTag.name();
                    if (name.equals("caption")) {
                        tb.clearStackToTableContext();
                        tb.insertMarkerToFormattingElements();
                        tb.insert(startTag);
                        tb.transition(InCaption);
                    } else if (name.equals("colgroup")) {
                        tb.clearStackToTableContext();
                        tb.insert(startTag);
                        tb.transition(InColumnGroup);
                    } else if (name.equals("col")) {
                        tb.process(new Token.StartTag("colgroup"));
                        return tb.process(t);
                    } else {
                        if (StringUtil.in(name, "tbody", "tfoot", "thead")) {
                            tb.clearStackToTableContext();
                            tb.insert(startTag);
                            tb.transition(InTableBody);
                        } else {
                            if (StringUtil.in(name, "td", "th", "tr")) {
                                tb.process(new Token.StartTag("tbody"));
                                return tb.process(t);
                            } else if (name.equals("table")) {
                                tb.error(this);
                                if (tb.process(new Token.EndTag("table"))) {
                                    return tb.process(t);
                                }
                            } else {
                                if (StringUtil.in(name, "style", "script")) {
                                    return tb.process(t, InHead);
                                }
                                if (name.equals("input")) {
                                    if (!startTag.attributes.get("type").equalsIgnoreCase("hidden")) {
                                        return anythingElse(t, tb);
                                    }
                                    tb.insertEmpty(startTag);
                                } else if (!name.equals("form")) {
                                    return anythingElse(t, tb);
                                } else {
                                    tb.error(this);
                                    if (tb.getFormElement() != null) {
                                        return false;
                                    }
                                    tb.setFormElement(tb.insertEmpty(startTag));
                                }
                            }
                        }
                    }
                } else if (t.isEndTag()) {
                    String name2 = t.asEndTag().name();
                    if (!name2.equals("table")) {
                        if (!StringUtil.in(name2, "body", "caption", "col", "colgroup", AdActivity.HTML_PARAM, "tbody", "td", "tfoot", "th", "thead", "tr")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    } else if (!tb.inTableScope(name2)) {
                        tb.error(this);
                        return false;
                    } else {
                        tb.popStackToClose("table");
                        tb.resetInsertionMode();
                    }
                } else if (t.isEOF()) {
                    if (!tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                        return true;
                    }
                    tb.error(this);
                    return true;
                }
                return anythingElse(t, tb);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            if (!StringUtil.in(tb.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                return tb.process(t, InBody);
            }
            tb.setFosterInserts(true);
            boolean processed = tb.process(t, InBody);
            tb.setFosterInserts(false);
            return processed;
        }
    },
    InTableText {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            switch (AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[t.type.ordinal()]) {
                case 5:
                    Token.Character c = t.asCharacter();
                    if (c.getData().equals(HtmlTreeBuilderState.nullString)) {
                        tb.error(this);
                        return false;
                    }
                    tb.getPendingTableCharacters().add(c);
                    return true;
                default:
                    if (tb.getPendingTableCharacters().size() > 0) {
                        for (Token.Character character : tb.getPendingTableCharacters()) {
                            if (!HtmlTreeBuilderState.isWhitespace(character)) {
                                tb.error(this);
                                if (StringUtil.in(tb.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                                    tb.setFosterInserts(true);
                                    tb.process(character, InBody);
                                    tb.setFosterInserts(false);
                                } else {
                                    tb.process(character, InBody);
                                }
                            } else {
                                tb.insert(character);
                            }
                        }
                        tb.newPendingTableCharacters();
                    }
                    tb.transition(tb.originalState());
                    return tb.process(t);
            }
        }
    },
    InCaption {
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0091, code lost:
            if (org.jsoup.helper.StringUtil.in(r13.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr") == false) goto L_0x0093;
         */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (!t.isEndTag() || !t.asEndTag().name().equals("caption")) {
                if (t.isStartTag()) {
                }
                if (!t.isEndTag() || !t.asEndTag().name().equals("table")) {
                    if (t.isEndTag()) {
                        if (StringUtil.in(t.asEndTag().name(), "body", "col", "colgroup", AdActivity.HTML_PARAM, "tbody", "td", "tfoot", "th", "thead", "tr")) {
                            tb.error(this);
                            return false;
                        }
                    }
                    return tb.process(t, InBody);
                }
                tb.error(this);
                if (tb.process(new Token.EndTag("caption"))) {
                    return tb.process(t);
                }
            } else if (!tb.inTableScope(t.asEndTag().name())) {
                tb.error(this);
                return false;
            } else {
                tb.generateImpliedEndTags();
                if (!tb.currentElement().nodeName().equals("caption")) {
                    tb.error(this);
                }
                tb.popStackToClose("caption");
                tb.clearFormattingElementsToLastMarker();
                tb.transition(InTable);
            }
            return true;
        }
    },
    InColumnGroup {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
                return true;
            }
            switch (AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[t.type.ordinal()]) {
                case 1:
                    tb.insert(t.asComment());
                    return true;
                case 2:
                    tb.error(this);
                    return true;
                case 3:
                    Token.StartTag startTag = t.asStartTag();
                    String name = startTag.name();
                    if (name.equals(AdActivity.HTML_PARAM)) {
                        return tb.process(t, InBody);
                    }
                    if (!name.equals("col")) {
                        return anythingElse(t, tb);
                    }
                    tb.insertEmpty(startTag);
                    return true;
                case 4:
                    if (!t.asEndTag().name().equals("colgroup")) {
                        return anythingElse(t, tb);
                    }
                    if (tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                        tb.error(this);
                        return false;
                    }
                    tb.pop();
                    tb.transition(InTable);
                    return true;
                case 6:
                    if (!tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                        return anythingElse(t, tb);
                    }
                    return true;
                default:
                    return anythingElse(t, tb);
            }
        }

        private boolean anythingElse(Token t, TreeBuilder tb) {
            if (tb.process(new Token.EndTag("colgroup"))) {
                return tb.process(t);
            }
            return true;
        }
    },
    InTableBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            switch (AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[t.type.ordinal()]) {
                case 3:
                    Token.StartTag startTag = t.asStartTag();
                    String name = startTag.name();
                    if (name.equals("tr")) {
                        tb.clearStackToTableBodyContext();
                        tb.insert(startTag);
                        tb.transition(InRow);
                        break;
                    } else {
                        if (StringUtil.in(name, "th", "td")) {
                            tb.error(this);
                            tb.process(new Token.StartTag("tr"));
                            return tb.process(startTag);
                        }
                        if (StringUtil.in(name, "caption", "col", "colgroup", "tbody", "tfoot", "thead")) {
                            return exitTableBody(t, tb);
                        }
                        return anythingElse(t, tb);
                    }
                case 4:
                    String name2 = t.asEndTag().name();
                    if (StringUtil.in(name2, "tbody", "tfoot", "thead")) {
                        if (tb.inTableScope(name2)) {
                            tb.clearStackToTableBodyContext();
                            tb.pop();
                            tb.transition(InTable);
                            break;
                        } else {
                            tb.error(this);
                            return false;
                        }
                    } else if (name2.equals("table")) {
                        return exitTableBody(t, tb);
                    } else {
                        if (!StringUtil.in(name2, "body", "caption", "col", "colgroup", AdActivity.HTML_PARAM, "td", "th", "tr")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    }
                default:
                    return anythingElse(t, tb);
            }
            return true;
        }

        private boolean exitTableBody(Token t, HtmlTreeBuilder tb) {
            if (tb.inTableScope("tbody") || tb.inTableScope("thead") || tb.inScope("tfoot")) {
                tb.clearStackToTableBodyContext();
                tb.process(new Token.EndTag(tb.currentElement().nodeName()));
                return tb.process(t);
            }
            tb.error(this);
            return false;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InTable);
        }
    },
    InRow {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isStartTag()) {
                Token.StartTag startTag = t.asStartTag();
                String name = startTag.name();
                if (StringUtil.in(name, "th", "td")) {
                    tb.clearStackToTableRowContext();
                    tb.insert(startTag);
                    tb.transition(InCell);
                    tb.insertMarkerToFormattingElements();
                } else {
                    if (StringUtil.in(name, "caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr")) {
                        return handleMissingTr(t, tb);
                    }
                    return anythingElse(t, tb);
                }
            } else if (!t.isEndTag()) {
                return anythingElse(t, tb);
            } else {
                String name2 = t.asEndTag().name();
                if (name2.equals("tr")) {
                    if (!tb.inTableScope(name2)) {
                        tb.error(this);
                        return false;
                    }
                    tb.clearStackToTableRowContext();
                    tb.pop();
                    tb.transition(InTableBody);
                } else if (name2.equals("table")) {
                    return handleMissingTr(t, tb);
                } else {
                    if (!StringUtil.in(name2, "tbody", "tfoot", "thead")) {
                        if (!StringUtil.in(name2, "body", "caption", "col", "colgroup", AdActivity.HTML_PARAM, "td", "th")) {
                            return anythingElse(t, tb);
                        }
                        tb.error(this);
                        return false;
                    } else if (!tb.inTableScope(name2)) {
                        tb.error(this);
                        return false;
                    } else {
                        tb.process(new Token.EndTag("tr"));
                        return tb.process(t);
                    }
                }
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InTable);
        }

        private boolean handleMissingTr(Token t, TreeBuilder tb) {
            if (tb.process(new Token.EndTag("tr"))) {
                return tb.process(t);
            }
            return false;
        }
    },
    InCell {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isEndTag()) {
                String name = t.asEndTag().name();
                if (!StringUtil.in(name, "td", "th")) {
                    if (StringUtil.in(name, "body", "caption", "col", "colgroup", AdActivity.HTML_PARAM)) {
                        tb.error(this);
                        return false;
                    }
                    if (!StringUtil.in(name, "table", "tbody", "tfoot", "thead", "tr")) {
                        return anythingElse(t, tb);
                    }
                    if (!tb.inTableScope(name)) {
                        tb.error(this);
                        return false;
                    }
                    closeCell(tb);
                    return tb.process(t);
                } else if (!tb.inTableScope(name)) {
                    tb.error(this);
                    tb.transition(InRow);
                    return false;
                } else {
                    tb.generateImpliedEndTags();
                    if (!tb.currentElement().nodeName().equals(name)) {
                        tb.error(this);
                    }
                    tb.popStackToClose(name);
                    tb.clearFormattingElementsToLastMarker();
                    tb.transition(InRow);
                    return true;
                }
            } else {
                if (t.isStartTag()) {
                    if (StringUtil.in(t.asStartTag().name(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                        if (tb.inTableScope("td") || tb.inTableScope("th")) {
                            closeCell(tb);
                            return tb.process(t);
                        }
                        tb.error(this);
                        return false;
                    }
                }
                return anythingElse(t, tb);
            }
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            return tb.process(t, InBody);
        }

        private void closeCell(HtmlTreeBuilder tb) {
            if (tb.inTableScope("td")) {
                tb.process(new Token.EndTag("td"));
            } else {
                tb.process(new Token.EndTag("th"));
            }
        }
    },
    InSelect {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            switch (AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[t.type.ordinal()]) {
                case 1:
                    tb.insert(t.asComment());
                    break;
                case 2:
                    tb.error(this);
                    return false;
                case 3:
                    Token.StartTag start = t.asStartTag();
                    String name = start.name();
                    if (name.equals(AdActivity.HTML_PARAM)) {
                        return tb.process(start, InBody);
                    }
                    if (name.equals("option")) {
                        tb.process(new Token.EndTag("option"));
                        tb.insert(start);
                        break;
                    } else if (name.equals("optgroup")) {
                        if (tb.currentElement().nodeName().equals("option")) {
                            tb.process(new Token.EndTag("option"));
                        } else if (tb.currentElement().nodeName().equals("optgroup")) {
                            tb.process(new Token.EndTag("optgroup"));
                        }
                        tb.insert(start);
                        break;
                    } else if (name.equals("select")) {
                        tb.error(this);
                        return tb.process(new Token.EndTag("select"));
                    } else {
                        if (StringUtil.in(name, "input", "keygen", "textarea")) {
                            tb.error(this);
                            if (!tb.inSelectScope("select")) {
                                return false;
                            }
                            tb.process(new Token.EndTag("select"));
                            return tb.process(start);
                        } else if (name.equals("script")) {
                            return tb.process(t, InHead);
                        } else {
                            return anythingElse(t, tb);
                        }
                    }
                case 4:
                    String name2 = t.asEndTag().name();
                    if (name2.equals("optgroup")) {
                        if (tb.currentElement().nodeName().equals("option") && tb.aboveOnStack(tb.currentElement()) != null && tb.aboveOnStack(tb.currentElement()).nodeName().equals("optgroup")) {
                            tb.process(new Token.EndTag("option"));
                        }
                        if (!tb.currentElement().nodeName().equals("optgroup")) {
                            tb.error(this);
                            break;
                        } else {
                            tb.pop();
                            break;
                        }
                    } else if (name2.equals("option")) {
                        if (!tb.currentElement().nodeName().equals("option")) {
                            tb.error(this);
                            break;
                        } else {
                            tb.pop();
                            break;
                        }
                    } else if (name2.equals("select")) {
                        if (tb.inSelectScope(name2)) {
                            tb.popStackToClose(name2);
                            tb.resetInsertionMode();
                            break;
                        } else {
                            tb.error(this);
                            return false;
                        }
                    } else {
                        return anythingElse(t, tb);
                    }
                case 5:
                    Token.Character c = t.asCharacter();
                    if (!c.getData().equals(HtmlTreeBuilderState.nullString)) {
                        tb.insert(c);
                        break;
                    } else {
                        tb.error(this);
                        return false;
                    }
                case 6:
                    if (!tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                        tb.error(this);
                        break;
                    }
                    break;
                default:
                    return anythingElse(t, tb);
            }
            return true;
        }

        private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
            tb.error(this);
            return false;
        }
    },
    InSelectInTable {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isStartTag()) {
                if (StringUtil.in(t.asStartTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                    tb.error(this);
                    tb.process(new Token.EndTag("select"));
                    return tb.process(t);
                }
            }
            if (t.isEndTag()) {
                if (StringUtil.in(t.asEndTag().name(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                    tb.error(this);
                    if (!tb.inTableScope(t.asEndTag().name())) {
                        return false;
                    }
                    tb.process(new Token.EndTag("select"));
                    return tb.process(t);
                }
            }
            return tb.process(t, InSelect);
        }
    },
    AfterBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                return tb.process(t, InBody);
            }
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM)) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEndTag() || !t.asEndTag().name().equals(AdActivity.HTML_PARAM)) {
                    if (!t.isEOF()) {
                        tb.error(this);
                        tb.transition(InBody);
                        return tb.process(t);
                    }
                } else if (tb.isFragmentParsing()) {
                    tb.error(this);
                    return false;
                } else {
                    tb.transition(AfterAfterBody);
                }
            }
            return true;
        }
    },
    InFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag()) {
                Token.StartTag start = t.asStartTag();
                String name = start.name();
                if (name.equals(AdActivity.HTML_PARAM)) {
                    return tb.process(start, InBody);
                }
                if (name.equals("frameset")) {
                    tb.insert(start);
                } else if (name.equals("frame")) {
                    tb.insertEmpty(start);
                } else if (name.equals("noframes")) {
                    return tb.process(start, InHead);
                } else {
                    tb.error(this);
                    return false;
                }
            } else if (!t.isEndTag() || !t.asEndTag().name().equals("frameset")) {
                if (!t.isEOF()) {
                    tb.error(this);
                    return false;
                } else if (!tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                    tb.error(this);
                    return true;
                }
            } else if (tb.currentElement().nodeName().equals(AdActivity.HTML_PARAM)) {
                tb.error(this);
                return false;
            } else {
                tb.pop();
                if (!tb.isFragmentParsing() && !tb.currentElement().nodeName().equals("frameset")) {
                    tb.transition(AfterFrameset);
                }
            }
            return true;
        }
    },
    AfterFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (HtmlTreeBuilderState.isWhitespace(t)) {
                tb.insert(t.asCharacter());
            } else if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype()) {
                tb.error(this);
                return false;
            } else if (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM)) {
                return tb.process(t, InBody);
            } else {
                if (t.isEndTag() && t.asEndTag().name().equals(AdActivity.HTML_PARAM)) {
                    tb.transition(AfterAfterFrameset);
                } else if (t.isStartTag() && t.asStartTag().name().equals("noframes")) {
                    return tb.process(t, InHead);
                } else {
                    if (!t.isEOF()) {
                        tb.error(this);
                        return false;
                    }
                }
            }
            return true;
        }
    },
    AfterAfterBody {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype() || HtmlTreeBuilderState.isWhitespace(t) || (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM))) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEOF()) {
                    tb.error(this);
                    tb.transition(InBody);
                    return tb.process(t);
                }
            }
            return true;
        }
    },
    AfterAfterFrameset {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            if (t.isComment()) {
                tb.insert(t.asComment());
            } else if (t.isDoctype() || HtmlTreeBuilderState.isWhitespace(t) || (t.isStartTag() && t.asStartTag().name().equals(AdActivity.HTML_PARAM))) {
                return tb.process(t, InBody);
            } else {
                if (!t.isEOF()) {
                    if (t.isStartTag() && t.asStartTag().name().equals("noframes")) {
                        return tb.process(t, InHead);
                    }
                    tb.error(this);
                    return false;
                }
            }
            return true;
        }
    },
    ForeignContent {
        /* access modifiers changed from: package-private */
        public boolean process(Token t, HtmlTreeBuilder tb) {
            return true;
        }
    };
    
    /* access modifiers changed from: private */
    public static String nullString;

    /* access modifiers changed from: package-private */
    public abstract boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder);

    static {
        nullString = String.valueOf(0);
    }

    /* access modifiers changed from: private */
    public static boolean isWhitespace(Token t) {
        if (!t.isCharacter()) {
            return false;
        }
        String data = t.asCharacter().getData();
        for (int i = 0; i < data.length(); i++) {
            if (!StringUtil.isWhitespace(data.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static void handleRcData(Token.StartTag startTag, HtmlTreeBuilder tb) {
        tb.insert(startTag);
        tb.tokeniser.transition(TokeniserState.Rcdata);
        tb.markInsertionMode();
        tb.transition(Text);
    }

    /* access modifiers changed from: private */
    public static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder tb) {
        tb.insert(startTag);
        tb.tokeniser.transition(TokeniserState.Rawtext);
        tb.markInsertionMode();
        tb.transition(Text);
    }
}
