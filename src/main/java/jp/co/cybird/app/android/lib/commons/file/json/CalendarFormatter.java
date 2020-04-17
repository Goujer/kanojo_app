package jp.co.cybird.app.android.lib.commons.file.json;

import java.util.Calendar;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class CalendarFormatter implements Formatter {
    public static final CalendarFormatter INSTANCE = new CalendarFormatter();

    CalendarFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return DateFormatter.INSTANCE.format(context, src, ((Calendar) o).getTime(), out);
    }
}
