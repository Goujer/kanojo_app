package jp.co.cybird.app.android.lib.commons.file.json;

import java.sql.SQLException;
import java.sql.Struct;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class StructFormmatter implements Formatter {
    public static final StructFormmatter INSTANCE = new StructFormmatter();

    StructFormmatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Object[] objArr;
        try {
            objArr = ((Struct) o).getAttributes();
        } catch (SQLException e) {
            objArr = null;
        }
        if (objArr == null) {
            Object[] objArr2 = new Object[0];
        }
        return ObjectArrayFormatter.INSTANCE.format(context, src, o, out);
    }
}
