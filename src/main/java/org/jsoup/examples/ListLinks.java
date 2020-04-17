package org.jsoup.examples;

import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListLinks {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        print("Fetching %s...", url);
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        print("\nMedia: (%d)", Integer.valueOf(media.size()));
        Iterator i$ = media.iterator();
        while (i$.hasNext()) {
            Element src = i$.next();
            if (src.tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)", src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"), trim(src.attr("alt"), 20));
            } else {
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
            }
        }
        print("\nImports: (%d)", Integer.valueOf(imports.size()));
        Iterator i$2 = imports.iterator();
        while (i$2.hasNext()) {
            Element link = i$2.next();
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }
        print("\nLinks: (%d)", Integer.valueOf(links.size()));
        Iterator i$3 = links.iterator();
        while (i$3.hasNext()) {
            Element link2 = i$3.next();
            print(" * a: <%s>  (%s)", link2.attr("abs:href"), trim(link2.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        }
        return s;
    }
}
