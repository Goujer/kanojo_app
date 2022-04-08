package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.NumberFormat;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class FloatArrayFormatter implements Formatter {
    public static final FloatArrayFormatter INSTANCE = new FloatArrayFormatter();

    FloatArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        NumberFormat f = context.getNumberFormat();
        float[] array = (float[]) o;
        out.append('[');
        for (int i = 0; i < array.length; i++) {
            if (Float.isNaN(array[i]) || Float.isInfinite(array[i])) {
                if (context.getMode() != JSON.Mode.SCRIPT) {
                    out.append('\"');
                    out.append(Float.toString(array[i]));
                    out.append('\"');
                } else if (Double.isNaN((double) array[i])) {
                    out.append("Number.NaN");
                } else {
                    out.append("Number.");
                    out.append(array[i] > 0.0f ? "POSITIVE" : "NEGATIVE");
                    out.append("_INFINITY");
                }
            } else if (f != null) {
                StringFormatter.serialize(context, f.format((double) array[i]), out);
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
