package jp.co.cybird.app.android.lib.commons.file.json;

import java.util.TimeZone;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class TimeZoneFormatter implements Formatter {
    public static final TimeZoneFormatter INSTANCE = new TimeZoneFormatter();

    TimeZoneFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, ((TimeZone) o).getID(), out);
    }
}
