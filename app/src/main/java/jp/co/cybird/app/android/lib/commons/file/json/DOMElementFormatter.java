package jp.co.cybird.app.android.lib.commons.file.json;

import java.io.Flushable;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* compiled from: Formatter */
final class DOMElementFormatter implements Formatter {
    public static final DOMElementFormatter INSTANCE = new DOMElementFormatter();

    DOMElementFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Element elem = (Element) o;
        out.append('[');
        StringFormatter.serialize(context, elem.getTagName(), out);
        out.append(',');
        if (context.isPrettyPrint()) {
            out.append('\n');
            int indent = context.getInitialIndent() + context.getDepth() + 1;
            for (int j = 0; j < indent; j++) {
                out.append(context.getIndentText());
            }
        }
        out.append('{');
        if (elem.hasAttributes()) {
            NamedNodeMap names = elem.getAttributes();
            for (int i = 0; i < names.getLength(); i++) {
                if (i != 0) {
                    out.append(',');
                }
                if (context.isPrettyPrint() && names.getLength() > 1) {
                    out.append('\n');
                    for (int j2 = 0; j2 < context.getDepth() + 2; j2++) {
                        out.append('\t');
                    }
                }
                Node node = names.item(i);
                if (node instanceof Attr) {
                    StringFormatter.serialize(context, node.getNodeName(), out);
                    out.append(':');
                    if (context.isPrettyPrint()) {
                        out.append(' ');
                    }
                    StringFormatter.serialize(context, node.getNodeValue(), out);
                }
            }
            if (context.isPrettyPrint() && names.getLength() > 1) {
                out.append('\n');
                int indent2 = context.getInitialIndent() + context.getDepth() + 1;
                for (int j3 = 0; j3 < indent2; j3++) {
                    out.append(context.getIndentText());
                }
            }
        }
        out.append('}');
        if (elem.hasChildNodes()) {
            NodeList nodes = elem.getChildNodes();
            JSONHint hint = context.getHint();
            for (int i2 = 0; i2 < nodes.getLength(); i2++) {
                Node value = nodes.item(i2);
                if ((value instanceof Element) || ((value instanceof CharacterData) && !(value instanceof Comment))) {
                    out.append(',');
                    if (context.isPrettyPrint()) {
                        out.append('\n');
                        int indent3 = context.getInitialIndent() + context.getDepth() + 1;
                        for (int j4 = 0; j4 < indent3; j4++) {
                            out.append(context.getIndentText());
                        }
                    }
                    context.enter(Integer.valueOf(i2 + 2), hint);
                    context.formatInternal(context.preformatInternal(value), out);
                    context.exit();
                    if (out instanceof Flushable) {
                        ((Flushable) out).flush();
                    }
                }
            }
        }
        if (context.isPrettyPrint()) {
            out.append('\n');
            int indent4 = context.getInitialIndent() + context.getDepth();
            for (int j5 = 0; j5 < indent4; j5++) {
                out.append(context.getIndentText());
            }
        }
        out.append(']');
        return true;
    }
}
