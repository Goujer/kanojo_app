package org.jsoup.parser;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Parser {
    private static final int DEFAULT_MAX_ERRORS = 0;
    private ParseErrorList errors;
    private int maxErrors = 0;
    private TreeBuilder treeBuilder;

    public Parser(TreeBuilder treeBuilder2) {
        this.treeBuilder = treeBuilder2;
    }

    public Document parseInput(String html, String baseUri) {
        this.errors = isTrackErrors() ? ParseErrorList.tracking(this.maxErrors) : ParseErrorList.noTracking();
        return this.treeBuilder.parse(html, baseUri, this.errors);
    }

    public TreeBuilder getTreeBuilder() {
        return this.treeBuilder;
    }

    public Parser setTreeBuilder(TreeBuilder treeBuilder2) {
        this.treeBuilder = treeBuilder2;
        return this;
    }

    public boolean isTrackErrors() {
        return this.maxErrors > 0;
    }

    public Parser setTrackErrors(int maxErrors2) {
        this.maxErrors = maxErrors2;
        return this;
    }

    public List<ParseError> getErrors() {
        return this.errors;
    }

    public static Document parse(String html, String baseUri) {
        return new HtmlTreeBuilder().parse(html, baseUri, ParseErrorList.noTracking());
    }

    public static List<Node> parseFragment(String fragmentHtml, Element context, String baseUri) {
        return new HtmlTreeBuilder().parseFragment(fragmentHtml, context, baseUri, ParseErrorList.noTracking());
    }

    public static List<Node> parseXmlFragment(String fragmentXml, String baseUri) {
        return new XmlTreeBuilder().parseFragment(fragmentXml, baseUri, ParseErrorList.noTracking());
    }

    public static Document parseBodyFragment(String bodyHtml, String baseUri) {
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        List<Node> nodeList = parseFragment(bodyHtml, body, baseUri);
        for (Node node : (Node[]) nodeList.toArray(new Node[nodeList.size()])) {
            body.appendChild(node);
        }
        return doc;
    }

    public static String unescapeEntities(String string, boolean inAttribute) {
        return new Tokeniser(new CharacterReader(string), ParseErrorList.noTracking()).unescapeEntities(inAttribute);
    }

    public static Document parseBodyFragmentRelaxed(String bodyHtml, String baseUri) {
        return parse(bodyHtml, baseUri);
    }

    public static Parser htmlParser() {
        return new Parser(new HtmlTreeBuilder());
    }

    public static Parser xmlParser() {
        return new Parser(new XmlTreeBuilder());
    }
}
