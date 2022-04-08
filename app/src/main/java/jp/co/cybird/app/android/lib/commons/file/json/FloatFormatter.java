package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.NumberFormat;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class FloatFormatter implements Formatter {
    public static final FloatFormatter INSTANCE = new FloatFormatter();

    FloatFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        NumberFormat f = context.getNumberFormat();
        if (f != null) {
            StringFormatter.serialize(context, f.format(o), out);
            return false;
        }
        double d = ((Number) o).doubleValue();
        if (!Double.isNaN(d) && !Double.isInfinite(d)) {
            out.append(o.toString());
            return false;
        } else if (context.getMode() != JSON.Mode.SCRIPT) {
            out.append('\"');
            out.append(o.toString());
            out.append('\"');
            return false;
        } else if (Double.isNaN(d)) {
            out.append("Number.NaN");
            return false;
        } else {
            out.append("Number.");
            out.append(d > 0.0d ? "POSITIVE" : "NEGATIVE");
            out.append("_INFINITY");
            return false;
        }
    }
}
