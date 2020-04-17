package jp.co.cybird.app.android.lib.commons.file.json;

import java.sql.Array;
import java.sql.SQLException;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class SQLArrayFormatter implements Formatter {
    public static final SQLArrayFormatter INSTANCE = new SQLArrayFormatter();

    SQLArrayFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Object obj;
        try {
            obj = ((Array) o).getArray();
        } catch (SQLException e) {
            obj = null;
        }
        if (obj == null) {
            obj = new Object[0];
        }
        return ObjectArrayFormatter.INSTANCE.format(context, src, obj, out);
    }
}
