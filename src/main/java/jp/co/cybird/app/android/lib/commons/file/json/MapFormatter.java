package jp.co.cybird.app.android.lib.commons.file.json;

import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class MapFormatter implements Formatter {
    public static final MapFormatter INSTANCE = new MapFormatter();

    MapFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Object value;
        JSONHint hint = context.getHint();
        Class<?> lastClass = null;
        Formatter lastFormatter = null;
        out.append('{');
        int count = 0;
        for (Map.Entry<?, ?> entry : ((Map) o).entrySet()) {
            Object key = entry.getKey();
            if (!(key == null || (value = entry.getValue()) == src)) {
                if (count != 0) {
                    out.append(',');
                }
                if (context.isPrettyPrint()) {
                    out.append(10);
                    int indent = context.getInitialIndent() + context.getDepth() + 1;
                    for (int j = 0; j < indent; j++) {
                        out.append(context.getIndentText());
                    }
                }
                StringFormatter.serialize(context, key.toString(), out);
                out.append(':');
                if (context.isPrettyPrint()) {
                    out.append(' ');
                }
                context.enter(key, hint);
                Object value2 = context.preformatInternal(value);
                if (value2 == null) {
                    NullFormatter.INSTANCE.format(context, src, value2, out);
                } else if (hint != null) {
                    context.formatInternal(value2, out);
                } else if (value2.getClass().equals(lastClass)) {
                    lastFormatter.format(context, src, value2, out);
                } else {
                    lastFormatter = context.formatInternal(value2, out);
                    lastClass = value2.getClass();
                }
                context.exit();
                count++;
            }
        }
        if (context.isPrettyPrint() && count > 0) {
            out.append(10);
            int indent2 = context.getInitialIndent() + context.getDepth();
            for (int j2 = 0; j2 < indent2; j2++) {
                out.append(context.getIndentText());
            }
        }
        out.append('}');
        return true;
    }
}
