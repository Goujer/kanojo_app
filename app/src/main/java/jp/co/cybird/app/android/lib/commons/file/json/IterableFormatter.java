package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class IterableFormatter implements Formatter {
    public static final IterableFormatter INSTANCE = new IterableFormatter();

    IterableFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return IteratorFormatter.INSTANCE.format(context, src, ((Iterable) o).iterator(), out);
    }
}
