package org.jsoup.examples;

import com.google.ads.AdActivity;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class HtmlToPlainText {
    public static void main(String... args) throws IOException {
        boolean z = true;
        if (args.length != 1) {
            z = false;
        }
        Validate.isTrue(z, "usage: supply url to fetch");
        System.out.println(new HtmlToPlainText().getPlainText(Jsoup.connect(args[0]).get()));
    }

    public String getPlainText(Element element) {
        FormattingVisitor formatter = new FormattingVisitor();
        new NodeTraversor(formatter).traverse(element);
        return formatter.toString();
    }

    private class FormattingVisitor implements NodeVisitor {
        private static final int maxWidth = 80;
        private StringBuilder accum;
        private int width;

        private FormattingVisitor() {
            this.width = 0;
            this.accum = new StringBuilder();
        }

        public void head(Node node, int depth) {
            String name = node.nodeName();
            if (node instanceof TextNode) {
                append(((TextNode) node).text());
            } else if (name.equals("li")) {
                append("\n * ");
            }
        }

        public void tail(Node node, int depth) {
            String name = node.nodeName();
            if (name.equals("br")) {
                append("\n");
                return;
            }
            if (StringUtil.in(name, AdActivity.PACKAGE_NAME_PARAM, "h1", "h2", "h3", "h4", "h5")) {
                append("\n\n");
            } else if (name.equals("a")) {
                append(String.format(" <%s>", new Object[]{node.absUrl("href")}));
            }
        }

        private void append(String text) {
            boolean last;
            if (text.startsWith("\n")) {
                this.width = 0;
            }
            if (text.equals(" ")) {
                if (this.accum.length() != 0) {
                    if (StringUtil.in(this.accum.substring(this.accum.length() - 1), " ", "\n")) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (text.length() + this.width > maxWidth) {
                String[] words = text.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (i == words.length - 1) {
                        last = true;
                    } else {
                        last = false;
                    }
                    if (!last) {
                        word = word + " ";
                    }
                    if (word.length() + this.width > maxWidth) {
                        this.accum.append("\n").append(word);
                        this.width = word.length();
                    } else {
                        this.accum.append(word);
                        this.width += word.length();
                    }
                }
                return;
            }
            this.accum.append(text);
            this.width += text.length();
        }

        public String toString() {
            return this.accum.toString();
        }
    }
}
