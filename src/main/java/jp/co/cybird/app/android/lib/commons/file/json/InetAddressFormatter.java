package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Formatter */
final class InetAddressFormatter implements Formatter {
    public static final InetAddressFormatter INSTANCE = new InetAddressFormatter();

    InetAddressFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        try {
            return StringFormatter.INSTANCE.format(context, src, (String) ClassUtil.findClass("java.net.InetAddress").getMethod("getHostAddress", new Class[0]).invoke(o, new Object[0]), out);
        } catch (Exception e) {
            return NullFormatter.INSTANCE.format(context, src, (Object) null, out);
        }
    }
}
