package jp.co.cybird.app.android.lib.commons.file.json;

import java.util.Locale;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class LocaleFormatter implements Formatter {
    public static final LocaleFormatter INSTANCE = new LocaleFormatter();

    LocaleFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, ((Locale) o).toString().replace('_', '-'), out);
    }
}
