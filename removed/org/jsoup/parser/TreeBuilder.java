package org.jsoup.parser;

import org.jsoup.helper.DescendableLinkedList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;

abstract class TreeBuilder {
    protected String baseUri;
    protected Token currentToken;
    protected Document doc;
    protected ParseErrorList errors;
    CharacterReader reader;
    protected DescendableLinkedList<Element> stack;
    Tokeniser tokeniser;

    /* access modifiers changed from: protected */
    public abstract boolean process(Token token);

    TreeBuilder() {
    }

    /* access modifiers changed from: protected */
    public void initialiseParse(String input, String baseUri2, ParseErrorList errors2) {
        Validate.notNull(input, "String input must not be null");
        Validate.notNull(baseUri2, "BaseURI must not be null");
        this.doc = new Document(baseUri2);
        this.reader = new CharacterReader(input);
        this.errors = errors2;
        this.tokeniser = new Tokeniser(this.reader, errors2);
        this.stack = new DescendableLinkedList<>();
        this.baseUri = baseUri2;
    }

    /* access modifiers changed from: package-private */
    public Document parse(String input, String baseUri2) {
        return parse(input, baseUri2, ParseErrorList.noTracking());
    }

    /* access modifiers changed from: package-private */
    public Document parse(String input, String baseUri2, ParseErrorList errors2) {
        initialiseParse(input, baseUri2, errors2);
        runParser();
        return this.doc;
    }

    /* access modifiers changed from: protected */
    public void runParser() {
        Token token;
        do {
            token = this.tokeniser.read();
            process(token);
        } while (token.type != Token.TokenType.EOF);
    }

    /* access modifiers changed from: protected */
    public Element currentElement() {
        return (Element) this.stack.getLast();
    }
}
