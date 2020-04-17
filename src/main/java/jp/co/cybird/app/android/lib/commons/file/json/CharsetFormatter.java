package jp.co.cybird.app.android.lib.commons.file.json;

import java.nio.charset.Charset;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class CharsetFormatter implements Formatter {
    public static final CharsetFormatter INSTANCE = new CharsetFormatter();

    CharsetFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, ((Charset) o).name(), out);
    }
}
