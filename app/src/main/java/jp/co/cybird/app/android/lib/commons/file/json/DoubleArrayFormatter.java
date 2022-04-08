package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.NumberFormat;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class DoubleArrayFormatter implements Formatter {
    public static final DoubleArrayFormatter INSTANCE = new DoubleArrayFormatter();

    DoubleArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        NumberFormat f = context.getNumberFormat();
        double[] array = (double[]) o;
        out.append('[');
        for (int i = 0; i < array.length; i++) {
            if (Double.isNaN(array[i]) || Double.isInfinite(array[i])) {
                if (context.getMode() != JSON.Mode.SCRIPT) {
                    out.append('\"');
                    out.append(Double.toString(array[i]));
                    out.append('\"');
                } else if (Double.isNaN(array[i])) {
                    out.append("Number.NaN");
                } else {
                    out.append("Number.");
                    out.append(array[i] > 0.0d ? "POSITIVE" : "NEGATIVE");
                    out.append("_INFINITY");
                }
            } else if (f != null) {
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
