package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class NullFormatter implements Formatter {
    public static final NullFormatter INSTANCE = new NullFormatter();

    NullFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        out.append("null");
        return false;
    }
}
