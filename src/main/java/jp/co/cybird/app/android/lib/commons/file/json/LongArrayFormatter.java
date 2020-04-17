package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.NumberFormat;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class LongArrayFormatter implements Formatter {
    public static final LongArrayFormatter INSTANCE = new LongArrayFormatter();

    LongArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        NumberFormat f = context.getNumberFormat();
        long[] array = (long[]) o;
        out.append('[');
        for (int i = 0; i < array.length; i++) {
            if (f != null) {
                StringFormatter.serialize(context, f.format(array[i]), out);
            } else {
                out.append(String.valueOf(array[i]));
            }
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
