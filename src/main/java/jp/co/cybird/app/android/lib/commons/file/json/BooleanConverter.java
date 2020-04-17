package jp.co.cybird.app.android.lib.commons.file.json;

import com.google.ads.AdActivity;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;

/* compiled from: Converter */
final class BooleanConverter implements Converter {
    public static final BooleanConverter INSTANCE = new BooleanConverter();

    BooleanConverter() {
    }

    public Object convert(JSON.JSONContext context, Object value, Class<?> c, Type t) throws Exception {
        boolean z = false;
        if (value instanceof Map) {
            value = ((Map) value).get((Object) null);
        } else if (value instanceof List) {
            List<?> src = (List) value;
            value = !src.isEmpty() ? src.get(0) : null;
        }
        if (value instanceof Boolean) {
            return value;
        }
        if (value instanceof BigDecimal) {
            return Boolean.valueOf(!value.equals(BigDecimal.ZERO));
        } else if (value instanceof BigInteger) {
            if (!value.equals(BigInteger.ZERO)) {
                z = true;
            }
            return Boolean.valueOf(z);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0.0d;
        } else {
            if (value == null) {
                return PlainConverter.getDefaultValue(c);
            }
            String s = value.toString().trim();
            if (s.length() == 0 || s.equalsIgnoreCase(GreeDefs.BARCODE) || s.equalsIgnoreCase(AdActivity.INTENT_FLAGS_PARAM) || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off") || s.equals("NaN")) {
                return false;
            }
            return true;
        }
    }
}
