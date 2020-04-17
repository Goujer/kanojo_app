package jp.co.cybird.app.android.lib.commons.file.json;

import java.io.IOException;
import java.lang.reflect.Type;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.StringBuilderOutputSource;

/* compiled from: Converter */
final class FormatConverter implements Converter {
    public static final FormatConverter INSTANCE = new FormatConverter();

    FormatConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        JSON.JSONContext context2 = context.copy();
        context2.skipHint = context.getHint();
        Object value2 = context2.preformatInternal(value);
        StringBuilderOutputSource fs = new StringBuilderOutputSource(new StringBuilder(200));
        try {
            context2.formatInternal(value2, fs);
        } catch (IOException e) {
        }
        fs.flush();
        context.skipHint = context2.skipHint;
        Object ret = context.postparseInternal(fs.toString(), c, t);
        context.skipHint = null;
        return ret;
    }
}
