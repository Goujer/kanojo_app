package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class ObjectArrayFormatter implements Formatter {
    public static final ObjectArrayFormatter INSTANCE = new ObjectArrayFormatter();

    ObjectArrayFormatter() {
    }

    /* JADX WARNING: type inference failed for: r14v0, types: [java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    public boolean format(JSON.JSONContext context, Object src, Object r14, OutputSource out) throws Exception {
        Object[] array = r14;
        JSONHint hint = context.getHint();
        Class<?> lastClass = null;
        Formatter lastFormatter = null;
        out.append('[');
        int i = 0;
        while (i < array.length) {
            Object item = array[i];
            if (item == src) {
                item = null;
            }
            if (i != 0) {
                out.append(',');
            }
            if (context.isPrettyPrint()) {
                out.append(10);
                int indent = context.getInitialIndent() + context.getDepth() + 1;
                for (int j = 0; j < indent; j++) {
                    out.append(context.getIndentText());
                }
            }
            context.enter(Integer.valueOf(i), hint);
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
            i++;
        }
        if (context.isPrettyPrint() && i > 0) {
            out.append(10);
            int indent2 = context.getInitialIndent() + context.getDepth();
            for (int j2 = 0; j2 < indent2; j2++) {
                out.append(context.getIndentText());
            }
        }
        out.append(']');
        return true;
    }
}
