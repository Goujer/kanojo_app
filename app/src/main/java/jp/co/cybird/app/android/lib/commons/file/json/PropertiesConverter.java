package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

/* compiled from: Converter */
final class PropertiesConverter implements Converter {
    public static final PropertiesConverter INSTANCE = new PropertiesConverter();

    PropertiesConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        Properties prop = (Properties) context.createInternal(c);
        if ((value instanceof Map) || (value instanceof List)) {
            flattenProperties(context.getLocalCache().getCachedBuffer(), value, prop);
        } else if (value != null) {
            prop.setProperty(value.toString(), (String) null);
        }
        return prop;
    }

    private static void flattenProperties(StringBuilder key, Object value, Properties props) {
        if (value instanceof Map) {
            for (Object entry : ((Map) value).entrySet()) {
                int pos = key.length();
                if (pos > 0) {
                    key.append('.');
                }
                key.append(((Map.Entry)entry).getKey());
                flattenProperties(key, ((Map.Entry)entry).getValue(), props);
                key.setLength(pos);
            }
        } else if (value instanceof List) {
            List<?> list = (List) value;
            for (int i = 0; i < list.size(); i++) {
                int pos2 = key.length();
                if (pos2 > 0) {
                    key.append('.');
                }
                key.append(i);
                flattenProperties(key, list.get(i), props);
                key.setLength(pos2);
            }
        } else {
            props.setProperty(key.toString(), value.toString());
        }
    }
}
