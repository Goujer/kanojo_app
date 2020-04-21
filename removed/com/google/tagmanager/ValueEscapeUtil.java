package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

class ValueEscapeUtil {
    private ValueEscapeUtil() {
    }

    static ObjectAndStatic<TypeSystem.Value> applyEscapings(ObjectAndStatic<TypeSystem.Value> value, List<TypeSystem.Value.Escaping> escapings) {
        ObjectAndStatic<TypeSystem.Value> escapedValue = value;
        for (TypeSystem.Value.Escaping escaping : escapings) {
            escapedValue = applyEscaping(escapedValue, escaping);
        }
        return escapedValue;
    }

    static String urlEncode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8").replaceAll("\\+", "%20");
    }

    private static ObjectAndStatic<TypeSystem.Value> applyEscaping(ObjectAndStatic<TypeSystem.Value> value, TypeSystem.Value.Escaping escaping) {
        if (!isValidStringType(value.getObject())) {
            Log.e("Escaping can only be applied to strings.");
            return value;
        }
        switch (escaping) {
            case ESCAPE_URI:
                return escapeUri(value);
            default:
                Log.e("Unsupported Value Escaping: " + escaping);
                return value;
        }
    }

    private static ObjectAndStatic<TypeSystem.Value> escapeUri(ObjectAndStatic<TypeSystem.Value> value) {
        try {
            return new ObjectAndStatic<>(Types.objectToValue(urlEncode(value.getObject().getString())), value.isStatic());
        } catch (UnsupportedEncodingException e) {
            Log.e("Escape URI: unsupported encoding", e);
            return value;
        }
    }

    private static boolean isValidStringType(TypeSystem.Value value) {
        return value.hasType() && value.getType().equals(TypeSystem.Value.Type.STRING) && value.hasString();
    }
}
