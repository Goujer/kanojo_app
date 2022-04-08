package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class BooleanArrayFormatter implements Formatter {
    public static final BooleanArrayFormatter INSTANCE = new BooleanArrayFormatter();

    BooleanArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        out.append('[');
        boolean[] array = (boolean[]) o;
        for (int i = 0; i < array.length; i++) {
            out.append(String.valueOf(array[i]));
            if (i != array.length - 1) {
                out.append(',');
                if (context.isPrettyPrint()) {
                    out.append(' ');
                }
            }
        }
        out.append(']');
        return true;
    }
}
