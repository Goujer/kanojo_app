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
        int indent;
        int j;
        Map<?, ?> map = (Map) o;
        JSONHint hint = context.getHint();
        Class<?> lastClass = null;
        Formatter lastFormatter = null;
        out.append('{');
        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            if (key != null) {
                Object value = entry.getValue();
                if (value != src) {
                    if (count != 0) {
                        out.append(',');
                    }
                    if (context.isPrettyPrint()) {
                        out.append('\n');
                        indent = (context.getInitialIndent() + context.getDepth()) + 1;
                        for (j = 0; j < indent; j++) {
                            out.append(context.getIndentText());
                        }
                    }
                    StringFormatter.serialize(context, key.toString(), out);
                    out.append(':');
                    if (context.isPrettyPrint()) {
                        out.append(' ');
                    }
                    context.enter(key, hint);
                    value = context.preformatInternal(value);
                    if (value == null) {
                        NullFormatter.INSTANCE.format(context, src, value, out);
                    } else if (hint != null) {
                        context.formatInternal(value, out);
                    } else if (value.getClass().equals(lastClass)) {
                        lastFormatter.format(context, src, value, out);
                    } else {
                        lastFormatter = context.formatInternal(value, out);
                        lastClass = value.getClass();
                    }
                    context.exit();
                    count++;
                }
            }
        }
        if (context.isPrettyPrint() && count > 0) {
            out.append('\n');
            indent = context.getInitialIndent() + context.getDepth();
            for (j = 0; j < indent; j++) {
                out.append(context.getIndentText());
            }
        }
        out.append('}');
        return true;
    }
}
