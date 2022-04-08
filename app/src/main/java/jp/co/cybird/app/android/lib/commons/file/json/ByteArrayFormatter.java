package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.Base64;

/* compiled from: Formatter */
final class ByteArrayFormatter implements Formatter {
    public static final ByteArrayFormatter INSTANCE = new ByteArrayFormatter();

    ByteArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        StringFormatter.serialize(context, Base64.encode((byte[]) o), out);
        return false;
    }
}
