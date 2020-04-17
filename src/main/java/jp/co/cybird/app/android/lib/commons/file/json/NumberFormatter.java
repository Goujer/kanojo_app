package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.NumberFormat;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class NumberFormatter implements Formatter {
    public static final NumberFormatter INSTANCE = new NumberFormatter();

    NumberFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        NumberFormat f = context.getNumberFormat();
        if (f != null) {
            StringFormatter.serialize(context, f.format(o), out);
            return false;
        }
        out.append(o.toString());
        return false;
    }
}
