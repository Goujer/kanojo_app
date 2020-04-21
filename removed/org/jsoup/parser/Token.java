package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

abstract class Token {
    TokenType type;

    enum TokenType {
        Doctype,
        StartTag,
        EndTag,
        Comment,
        Character,
        EOF
    }

    private Token() {
    }

    /* access modifiers changed from: package-private */
    public String tokenType() {
        return getClass().getSimpleName();
    }

    static class Doctype extends Token {
        boolean forceQuirks = false;
        final StringBuilder name = new StringBuilder();
        final StringBuilder publicIdentifier = new StringBuilder();
        final StringBuilder systemIdentifier = new StringBuilder();

        Doctype() {
            super();
            this.type = TokenType.Doctype;
        }

        /* access modifiers changed from: package-private */
        public String getName() {
            return this.name.toString();
        }

        /* access modifiers changed from: package-private */
        public String getPublicIdentifier() {
            return this.publicIdentifier.toString();
        }

        public String getSystemIdentifier() {
            return this.systemIdentifier.toString();
        }

        public boolean isForceQuirks() {
            return this.forceQuirks;
        }
    }

    static abstract class Tag extends Token {
        Attributes attributes;
        private String pendingAttributeName;
        private StringBuilder pendingAttributeValue;
        boolean selfClosing = false;
        protected String tagName;

        Tag() {
            super();
        }

        /* access modifiers changed from: package-private */
        public void newAttribute() {
            Attribute attribute;
            if (this.attributes == null) {
                this.attributes = new Attributes();
            }
            if (this.pendingAttributeName != null) {
                if (this.pendingAttributeValue == null) {
                    attribute = new Attribute(this.pendingAttributeName, "");
                } else {
                    attribute = new Attribute(this.pendingAttributeName, this.pendingAttributeValue.toString());
                }
                this.attributes.put(attribute);
            }
            this.pendingAttributeName = null;
            if (this.pendingAttributeValue != null) {
                this.pendingAttributeValue.delete(0, this.pendingAttributeValue.length());
            }
        }

        /* access modifiers changed from: package-private */
        public void finaliseTag() {
            if (this.pendingAttributeName != null) {
                newAttribute();
            }
        }

        /* access modifiers changed from: package-private */
        public String name() {
            Validate.isFalse(this.tagName.length() == 0);
            return this.tagName;
        }

        /* access modifiers changed from: package-private */
        public Tag name(String name) {
            this.tagName = name;
            return this;
        }

        /* access modifiers changed from: package-private */
        public boolean isSelfClosing() {
            return this.selfClosing;
        }

        /* access modifiers changed from: package-private */
        public Attributes getAttributes() {
            return this.attributes;
        }

        /* access modifiers changed from: package-private */
        public void appendTagName(String append) {
            if (this.tagName != null) {
                append = this.tagName.concat(append);
            }
            this.tagName = append;
        }

        /* access modifiers changed from: package-private */
        public void appendTagName(char append) {
            appendTagName(String.valueOf(append));
        }

        /* access modifiers changed from: package-private */
        public void appendAttributeName(String append) {
            if (this.pendingAttributeName != null) {
                append = this.pendingAttributeName.concat(append);
            }
            this.pendingAttributeName = append;
        }

        /* access modifiers changed from: package-private */
        public void appendAttributeName(char append) {
            appendAttributeName(String.valueOf(append));
        }

        /* access modifiers changed from: package-private */
        public void appendAttributeValue(String append) {
            ensureAttributeValue();
            this.pendingAttributeValue.append(append);
        }

        /* access modifiers changed from: package-private */
        public void appendAttributeValue(char append) {
            ensureAttributeValue();
            this.pendingAttributeValue.append(append);
        }

        /* access modifiers changed from: package-private */
        public void appendAttributeValue(char[] append) {
            ensureAttributeValue();
            this.pendingAttributeValue.append(append);
        }

        private final void ensureAttributeValue() {
            if (this.pendingAttributeValue == null) {
                this.pendingAttributeValue = new StringBuilder();
            }
        }
    }

    static class StartTag extends Tag {
        StartTag() {
            this.attributes = new Attributes();
            this.type = TokenType.StartTag;
        }

        StartTag(String name) {
            this();
            this.tagName = name;
        }

        StartTag(String name, Attributes attributes) {
            this();
            this.tagName = name;
            this.attributes = attributes;
        }

        public String toString() {
            if (this.attributes == null || this.attributes.size() <= 0) {
                return "<" + name() + ">";
            }
            return "<" + name() + " " + this.attributes.toString() + ">";
        }
    }

    static class EndTag extends Tag {
        EndTag() {
            this.type = TokenType.EndTag;
        }

        EndTag(String name) {
            this();
            this.tagName = name;
        }

        public String toString() {
            return "</" + name() + ">";
        }
    }

    static class Comment extends Token {
        boolean bogus = false;
        final StringBuilder data = new StringBuilder();

        Comment() {
            super();
            this.type = TokenType.Comment;
        }

        /* access modifiers changed from: package-private */
        public String getData() {
            return this.data.toString();
        }

        public String toString() {
            return "<!--" + getData() + "-->";
        }
    }

    static class Character extends Token {
        private final String data;

        Character(String data2) {
            super();
            this.type = TokenType.Character;
            this.data = data2;
        }

        /* access modifiers changed from: package-private */
        public String getData() {
            return this.data;
        }

        public String toString() {
            return getData();
        }
    }

    static class EOF extends Token {
        EOF() {
            super();
            this.type = TokenType.EOF;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDoctype() {
        return this.type == TokenType.Doctype;
    }

    /* access modifiers changed from: package-private */
    public Doctype asDoctype() {
        return (Doctype) this;
    }

    /* access modifiers changed from: package-private */
    public boolean isStartTag() {
        return this.type == TokenType.StartTag;
    }

    /* access modifiers changed from: package-private */
    public StartTag asStartTag() {
        return (StartTag) this;
    }

    /* access modifiers changed from: package-private */
    public boolean isEndTag() {
        return this.type == TokenType.EndTag;
    }

    /* access modifiers changed from: package-private */
    public EndTag asEndTag() {
        return (EndTag) this;
    }

    /* access modifiers changed from: package-private */
    public boolean isComment() {
        return this.type == TokenType.Comment;
    }

    /* access modifiers changed from: package-private */
    public Comment asComment() {
        return (Comment) this;
    }

    /* access modifiers changed from: package-private */
    public boolean isCharacter() {
        return this.type == TokenType.Character;
    }

    /* access modifiers changed from: package-private */
    public Character asCharacter() {
        return (Character) this;
    }

    /* access modifiers changed from: package-private */
    public boolean isEOF() {
        return this.type == TokenType.EOF;
    }
}
