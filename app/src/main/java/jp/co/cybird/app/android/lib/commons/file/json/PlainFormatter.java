package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class PlainFormatter implements Formatter {
    public static final PlainFormatter INSTANCE = new PlainFormatter();

    PlainFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        out.append(o.toString());
        return false;
    }
}
