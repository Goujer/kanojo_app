package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class CharArrayFormatter implements Formatter {
    public static final CharArrayFormatter INSTANCE = new CharArrayFormatter();

    CharArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, String.valueOf((char[]) o), out);
    }
}
