package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

/* compiled from: Formatter */
final class EnumFormatter implements Formatter {
    public static final EnumFormatter INSTANCE = new EnumFormatter();

    EnumFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        if (context.getEnumStyle() != null) {
            return StringFormatter.INSTANCE.format(context, src, context.getEnumStyle().to(((Enum) o).name()), out);
        }
        return NumberFormatter.INSTANCE.format(context, src, Integer.valueOf(((Enum) o).ordinal()), out);
    }
}
