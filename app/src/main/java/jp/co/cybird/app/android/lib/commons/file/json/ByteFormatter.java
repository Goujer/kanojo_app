package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class ByteFormatter implements Formatter {
    public static final ByteFormatter INSTANCE = new ByteFormatter();

    ByteFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        out.append(Integer.toString(((Byte) o).byteValue() & 255));
        return false;
    }
}
