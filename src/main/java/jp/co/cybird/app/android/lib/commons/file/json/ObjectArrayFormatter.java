package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class ObjectArrayFormatter implements Formatter {
    public static final ObjectArrayFormatter INSTANCE = new ObjectArrayFormatter();

    ObjectArrayFormatter() {
    }

    //JADX had an error
    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Object[] array = (Object[]) o;
        JSONHint hint = context.getHint();
        Class<?> lastClass = null;
        Formatter lastFormatter = null;
        out.append('[');
        int i = 0;
        while (i < array.length) {
            int indent;
            int j;
            Object item = array[i];
            if (item == src) {
                item = null;
            }
            if (i != 0) {
                out.append(',');
            }
            if (context.isPrettyPrint()) {
                out.append('\n');
                indent = (context.getInitialIndent() + context.getDepth()) + 1;
                for (j = 0; j < indent; j++) {
                    out.append(context.getIndentText());
                }
            }
            context.enter(Integer.valueOf(i), hint);
            item = context.preformatInternal(item);
            if (item == null) {
                NullFormatter.INSTANCE.format(context, src, item, out);
            } else if (hint != null) {
                context.formatInternal(item, out);
            } else if (item.getClass().equals(lastClass)) {
                lastFormatter.format(context, src, item, out);
            } else {
                lastFormatter = context.formatInternal(item, out);
                lastClass = item.getClass();
            }
            context.exit();
            i++;
        }
        if (context.isPrettyPrint() && i > 0) {
            out.append('\n');
            int indent = context.getInitialIndent() + context.getDepth();
            for (int j = 0; j < indent; j++) {
                out.append(context.getIndentText());
            }
        }
        out.append(']');
        return true;
    }
}
