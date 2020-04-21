package org.jsoup.select;

import java.util.List;
import java.util.regex.Pattern;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;

public abstract class Evaluator {
    public abstract boolean matches(Element element, Element element2);

    protected Evaluator() {
    }

    public static final class Tag extends Evaluator {
        private String tagName;

        public Tag(String tagName2) {
            this.tagName = tagName2;
        }

        public boolean matches(Element root, Element element) {
            return element.tagName().equals(this.tagName);
        }

        public String toString() {
            return String.format("%s", new Object[]{this.tagName});
        }
    }

    public static final class Id extends Evaluator {
        private String id;

        public Id(String id2) {
            this.id = id2;
        }

        public boolean matches(Element root, Element element) {
            return this.id.equals(element.id());
        }

        public String toString() {
            return String.format("#%s", new Object[]{this.id});
        }
    }

    public static final class Class extends Evaluator {
        private String className;

        public Class(String className2) {
            this.className = className2;
        }

        public boolean matches(Element root, Element element) {
            return element.hasClass(this.className);
        }

        public String toString() {
            return String.format(".%s", new Object[]{this.className});
        }
    }

    public static final class Attribute extends Evaluator {
        private String key;

        public Attribute(String key2) {
            this.key = key2;
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key);
        }

        public String toString() {
            return String.format("[%s]", new Object[]{this.key});
        }
    }

    public static final class AttributeStarting extends Evaluator {
        private String keyPrefix;

        public AttributeStarting(String keyPrefix2) {
            this.keyPrefix = keyPrefix2;
        }

        public boolean matches(Element root, Element element) {
            for (org.jsoup.nodes.Attribute attribute : element.attributes().asList()) {
                if (attribute.getKey().startsWith(this.keyPrefix)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return String.format("[^%s]", new Object[]{this.keyPrefix});
        }
    }

    public static final class AttributeWithValue extends AttributeKeyPair {
        public AttributeWithValue(String key, String value) {
            super(key, value);
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key) && this.value.equalsIgnoreCase(element.attr(this.key));
        }

        public String toString() {
            return String.format("[%s=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueNot extends AttributeKeyPair {
        public AttributeWithValueNot(String key, String value) {
            super(key, value);
        }

        public boolean matches(Element root, Element element) {
            return !this.value.equalsIgnoreCase(element.attr(this.key));
        }

        public String toString() {
            return String.format("[%s!=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueStarting extends AttributeKeyPair {
        public AttributeWithValueStarting(String key, String value) {
            super(key, value);
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key) && element.attr(this.key).toLowerCase().startsWith(this.value);
        }

        public String toString() {
            return String.format("[%s^=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueEnding extends AttributeKeyPair {
        public AttributeWithValueEnding(String key, String value) {
            super(key, value);
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key) && element.attr(this.key).toLowerCase().endsWith(this.value);
        }

        public String toString() {
            return String.format("[%s$=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueContaining extends AttributeKeyPair {
        public AttributeWithValueContaining(String key, String value) {
            super(key, value);
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key) && element.attr(this.key).toLowerCase().contains(this.value);
        }

        public String toString() {
            return String.format("[%s*=%s]", new Object[]{this.key, this.value});
        }
    }

    public static final class AttributeWithValueMatching extends Evaluator {
        String key;
        Pattern pattern;

        public AttributeWithValueMatching(String key2, Pattern pattern2) {
            this.key = key2.trim().toLowerCase();
            this.pattern = pattern2;
        }

        public boolean matches(Element root, Element element) {
            return element.hasAttr(this.key) && this.pattern.matcher(element.attr(this.key)).find();
        }

        public String toString() {
            return String.format("[%s~=%s]", new Object[]{this.key, this.pattern.toString()});
        }
    }

    public static abstract class AttributeKeyPair extends Evaluator {
        String key;
        String value;

        public AttributeKeyPair(String key2, String value2) {
            Validate.notEmpty(key2);
            Validate.notEmpty(value2);
            this.key = key2.trim().toLowerCase();
            this.value = value2.trim().toLowerCase();
        }
    }

    public static final class AllElements extends Evaluator {
        public boolean matches(Element root, Element element) {
            return true;
        }

        public String toString() {
            return "*";
        }
    }

    public static final class IndexLessThan extends IndexEvaluator {
        public IndexLessThan(int index) {
            super(index);
        }

        public boolean matches(Element root, Element element) {
            return element.elementSiblingIndex().intValue() < this.index;
        }

        public String toString() {
            return String.format(":lt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexGreaterThan extends IndexEvaluator {
        public IndexGreaterThan(int index) {
            super(index);
        }

        public boolean matches(Element root, Element element) {
            return element.elementSiblingIndex().intValue() > this.index;
        }

        public String toString() {
            return String.format(":gt(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IndexEquals extends IndexEvaluator {
        public IndexEquals(int index) {
            super(index);
        }

        public boolean matches(Element root, Element element) {
            return element.elementSiblingIndex().intValue() == this.index;
        }

        public String toString() {
            return String.format(":eq(%d)", new Object[]{Integer.valueOf(this.index)});
        }
    }

    public static final class IsLastChild extends Evaluator {
        public boolean matches(Element root, Element element) {
            Element p = element.parent();
            return p != null && !(p instanceof Document) && element.elementSiblingIndex().intValue() == p.children().size() + -1;
        }

        public String toString() {
            return ":last-child";
        }
    }

    public static final class IsFirstOfType extends IsNthOfType {
        public IsFirstOfType() {
            super(0, 1);
        }

        public String toString() {
            return ":first-of-type";
        }
    }

    public static final class IsLastOfType extends IsNthLastOfType {
        public IsLastOfType() {
            super(0, 1);
        }

        public String toString() {
            return ":last-of-type";
        }
    }

    public static abstract class CssNthEvaluator extends Evaluator {
        protected final int a;
        protected final int b;

        /* access modifiers changed from: protected */
        public abstract int calculatePosition(Element element, Element element2);

        /* access modifiers changed from: protected */
        public abstract String getPseudoClass();

        public CssNthEvaluator(int a2, int b2) {
            this.a = a2;
            this.b = b2;
        }

        public CssNthEvaluator(int b2) {
            this(0, b2);
        }

        public boolean matches(Element root, Element element) {
            Element p = element.parent();
            if (p == null || (p instanceof Document)) {
                return false;
            }
            int pos = calculatePosition(root, element);
            if (this.a == 0) {
                if (pos != this.b) {
                    return false;
                }
                return true;
            } else if ((pos - this.b) * this.a < 0 || (pos - this.b) % this.a != 0) {
                return false;
            } else {
                return true;
            }
        }

        public String toString() {
            if (this.a == 0) {
                return String.format(":%s(%d)", new Object[]{getPseudoClass(), Integer.valueOf(this.b)});
            } else if (this.b == 0) {
                return String.format(":%s(%dn)", new Object[]{getPseudoClass(), Integer.valueOf(this.a)});
            } else {
                return String.format(":%s(%dn%+d)", new Object[]{getPseudoClass(), Integer.valueOf(this.a), Integer.valueOf(this.b)});
            }
        }
    }

    public static final class IsNthChild extends CssNthEvaluator {
        public IsNthChild(int a, int b) {
            super(a, b);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element root, Element element) {
            return element.elementSiblingIndex().intValue() + 1;
        }

        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-child";
        }
    }

    public static final class IsNthLastChild extends CssNthEvaluator {
        public IsNthLastChild(int a, int b) {
            super(a, b);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element root, Element element) {
            return element.parent().children().size() - element.elementSiblingIndex().intValue();
        }

        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-last-child";
        }
    }

    public static class IsNthOfType extends CssNthEvaluator {
        public IsNthOfType(int a, int b) {
            super(a, b);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element root, Element element) {
            int pos = 0;
            Elements family = element.parent().children();
            for (int i = 0; i < family.size(); i++) {
                if (family.get(i).tag() == element.tag()) {
                    pos++;
                }
                if (family.get(i) == element) {
                    break;
                }
            }
            return pos;
        }

        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-of-type";
        }
    }

    public static class IsNthLastOfType extends CssNthEvaluator {
        public IsNthLastOfType(int a, int b) {
            super(a, b);
        }

        /* access modifiers changed from: protected */
        public int calculatePosition(Element root, Element element) {
            int pos = 0;
            Elements family = element.parent().children();
            for (int i = element.elementSiblingIndex().intValue(); i < family.size(); i++) {
                if (family.get(i).tag() == element.tag()) {
                    pos++;
                }
            }
            return pos;
        }

        /* access modifiers changed from: protected */
        public String getPseudoClass() {
            return "nth-last-of-type";
        }
    }

    public static final class IsFirstChild extends Evaluator {
        public boolean matches(Element root, Element element) {
            Element p = element.parent();
            return p != null && !(p instanceof Document) && element.elementSiblingIndex().intValue() == 0;
        }

        public String toString() {
            return ":first-child";
        }
    }

    public static final class IsRoot extends Evaluator {
        public boolean matches(Element root, Element element) {
            Element r;
            if (root instanceof Document) {
                r = root.child(0);
            } else {
                r = root;
            }
            if (element == r) {
                return true;
            }
            return false;
        }

        public String toString() {
            return ":root";
        }
    }

    public static final class IsOnlyChild extends Evaluator {
        public boolean matches(Element root, Element element) {
            Element p = element.parent();
            return p != null && !(p instanceof Document) && element.siblingElements().size() == 0;
        }

        public String toString() {
            return ":only-child";
        }
    }

    public static final class IsOnlyOfType extends Evaluator {
        public boolean matches(Element root, Element element) {
            Element p = element.parent();
            if (p == null || (p instanceof Document)) {
                return false;
            }
            int pos = 0;
            Elements family = p.children();
            for (int i = 0; i < family.size(); i++) {
                if (family.get(i).tag().equals(element.tag())) {
                    pos++;
                }
            }
            if (pos != 1) {
                return false;
            }
            return true;
        }

        public String toString() {
            return ":only-of-type";
        }
    }

    public static final class IsEmpty extends Evaluator {
        public boolean matches(Element root, Element element) {
            List<Node> family = element.childNodes();
            for (int i = 0; i < family.size(); i++) {
                Node n = family.get(i);
                if (!(n instanceof Comment) && !(n instanceof XmlDeclaration) && !(n instanceof DocumentType)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            return ":empty";
        }
    }

    public static abstract class IndexEvaluator extends Evaluator {
        int index;

        public IndexEvaluator(int index2) {
            this.index = index2;
        }
    }

    public static final class ContainsText extends Evaluator {
        private String searchText;

        public ContainsText(String searchText2) {
            this.searchText = searchText2.toLowerCase();
        }

        public boolean matches(Element root, Element element) {
            return element.text().toLowerCase().contains(this.searchText);
        }

        public String toString() {
            return String.format(":contains(%s", new Object[]{this.searchText});
        }
    }

    public static final class ContainsOwnText extends Evaluator {
        private String searchText;

        public ContainsOwnText(String searchText2) {
            this.searchText = searchText2.toLowerCase();
        }

        public boolean matches(Element root, Element element) {
            return element.ownText().toLowerCase().contains(this.searchText);
        }

        public String toString() {
            return String.format(":containsOwn(%s", new Object[]{this.searchText});
        }
    }

    public static final class Matches extends Evaluator {
        private Pattern pattern;

        public Matches(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element root, Element element) {
            return this.pattern.matcher(element.text()).find();
        }

        public String toString() {
            return String.format(":matches(%s", new Object[]{this.pattern});
        }
    }

    public static final class MatchesOwn extends Evaluator {
        private Pattern pattern;

        public MatchesOwn(Pattern pattern2) {
            this.pattern = pattern2;
        }

        public boolean matches(Element root, Element element) {
            return this.pattern.matcher(element.ownText()).find();
        }

        public String toString() {
            return String.format(":matchesOwn(%s", new Object[]{this.pattern});
        }
    }
}
