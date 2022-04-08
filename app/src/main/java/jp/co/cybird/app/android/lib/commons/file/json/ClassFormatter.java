package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class ClassFormatter implements Formatter {
    public static final ClassFormatter INSTANCE = new ClassFormatter();

    ClassFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, ((Class) o).getName(), out);
    }
}
