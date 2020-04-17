package jp.co.cybird.app.android.lib.commons.file.json;

import java.text.DateFormat;
import java.util.Date;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class DateFormatter implements Formatter {
    public static final DateFormatter INSTANCE = new DateFormatter();

    DateFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        Date date = (Date) o;
        DateFormat f = context.getDateFormat();
        if (f != null) {
            StringFormatter.serialize(context, f.format(o), out);
            return false;
        } else if (context.getMode() == JSON.Mode.SCRIPT) {
            out.append("new Date(");
            out.append(Long.toString(date.getTime()));
            out.append(")");
            return false;
        } else {
            out.append(Long.toString(date.getTime()));
            return false;
        }
    }
}
