package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.Base64;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Formatter */
final class SerializableFormatter implements Formatter {
    public static final SerializableFormatter INSTANCE = new SerializableFormatter();

    SerializableFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, Base64.encode(ClassUtil.serialize(o)), out);
    }
}
