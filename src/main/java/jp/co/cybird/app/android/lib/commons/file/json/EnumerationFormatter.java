package jp.co.cybird.app.android.lib.commons.file.json;

import java.util.Enumeration;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class EnumerationFormatter implements Formatter {
    public static final EnumerationFormatter INSTANCE = new EnumerationFormatter();

    EnumerationFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Enumeration<?> e = (Enumeration) o;
        JSONHint hint = context.getHint();
        out.append('[');
        int count = 0;
        Class<?> lastClass = null;
        Formatter lastFormatter = null;
        while (e.hasMoreElements()) {
            Object item = e.nextElement();
            if (item == src) {
                item = null;
            }
            if (count != 0) {
                out.append(',');
            }
            if (context.isPrettyPrint()) {
                out.append('\n');
                int indent = context.getInitialIndent() + context.getDepth() + 1;
                for (int j = 0; j < indent; j++) {
                    out.append(context.getIndentText());
                }
            }
            context.enter(Integer.valueOf(count), hint);
            Object item2 = context.preformatInternal(item);
            if (item2 == null) {
                NullFormatter.INSTANCE.format(context, src, item2, out);
            } else if (hint != null) {
                context.formatInternal(item2, out);
            } else if (item2.getClass().equals(lastClass)) {
                lastFormatter.format(context, src, item2, out);
            } else {
                lastFormatter = context.formatInternal(item2, out);
                lastClass = item2.getClass();
            }
            context.exit();
            count++;
        }
        if (context.isPrettyPrint() && count > 0) {
            out.append('\n');
            int indent2 = context.getInitialIndent() + context.getDepth();
            for (int j2 = 0; j2 < indent2; j2++) {
                out.append(context.getIndentText());
            }
        }
        out.append(']');
        return true;
    }
}
