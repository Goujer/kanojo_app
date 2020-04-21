package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Types {
    private static Boolean DEFAULT_BOOLEAN = new Boolean(false);
    private static Double DEFAULT_DOUBLE = new Double(0.0d);
    private static Long DEFAULT_INT64 = new Long(0);
    private static List<Object> DEFAULT_LIST = new ArrayList(0);
    private static Map<Object, Object> DEFAULT_MAP = new HashMap();
    private static TypedNumber DEFAULT_NUMBER = TypedNumber.numberWithInt64(0);
    private static final Object DEFAULT_OBJECT = null;
    private static String DEFAULT_STRING = new String("");
    private static TypeSystem.Value DEFAULT_VALUE = objectToValue(DEFAULT_STRING);

    private Types() {
    }

    public static Object getDefaultObject() {
        return DEFAULT_OBJECT;
    }

    public static Long getDefaultInt64() {
        return DEFAULT_INT64;
    }

    public static Double getDefaultDouble() {
        return DEFAULT_DOUBLE;
    }

    public static Boolean getDefaultBoolean() {
        return DEFAULT_BOOLEAN;
    }

    public static TypedNumber getDefaultNumber() {
        return DEFAULT_NUMBER;
    }

    public static String getDefaultString() {
        return DEFAULT_STRING;
    }

    public static List<Object> getDefaultList() {
        return DEFAULT_LIST;
    }

    public static Map<Object, Object> getDefaultMap() {
        return DEFAULT_MAP;
    }

    public static TypeSystem.Value getDefaultValue() {
        return DEFAULT_VALUE;
    }

    public static String objectToString(Object o) {
        return o == null ? DEFAULT_STRING : o.toString();
    }

    public static TypedNumber objectToNumber(Object o) {
        if (o instanceof TypedNumber) {
            return (TypedNumber) o;
        }
        if (isInt64ableNumber(o)) {
            return TypedNumber.numberWithInt64(getInt64(o));
        }
        if (isDoubleableNumber(o)) {
            return TypedNumber.numberWithDouble(Double.valueOf(getDouble(o)));
        }
        return parseNumber(objectToString(o));
    }

    public static Long objectToInt64(Object o) {
        return isInt64ableNumber(o) ? Long.valueOf(getInt64(o)) : parseInt64(objectToString(o));
    }

    public static Double objectToDouble(Object o) {
        return isDoubleableNumber(o) ? Double.valueOf(getDouble(o)) : parseDouble(objectToString(o));
    }

    public static Boolean objectToBoolean(Object o) {
        return o instanceof Boolean ? (Boolean) o : parseBoolean(objectToString(o));
    }

    public static String valueToString(TypeSystem.Value v) {
        return objectToString(valueToObject(v));
    }

    public static TypedNumber valueToNumber(TypeSystem.Value v) {
        return objectToNumber(valueToObject(v));
    }

    public static Long valueToInt64(TypeSystem.Value v) {
        return objectToInt64(valueToObject(v));
    }

    public static Double valueToDouble(TypeSystem.Value v) {
        return objectToDouble(valueToObject(v));
    }

    public static Boolean valueToBoolean(TypeSystem.Value v) {
        return objectToBoolean(valueToObject(v));
    }

    public static TypeSystem.Value objectToValue(Object o) {
        boolean containsRef;
        TypeSystem.Value.Builder builder = TypeSystem.Value.newBuilder();
        boolean containsRef2 = false;
        if (o instanceof TypeSystem.Value) {
            return (TypeSystem.Value) o;
        }
        if (o instanceof String) {
            builder.setType(TypeSystem.Value.Type.STRING).setString((String) o);
        } else if (o instanceof List) {
            builder.setType(TypeSystem.Value.Type.LIST);
            for (Object listObject : (List) o) {
                TypeSystem.Value listValue = objectToValue(listObject);
                if (listValue == DEFAULT_VALUE) {
                    return DEFAULT_VALUE;
                }
                if (containsRef2 || listValue.getContainsReferences()) {
                    containsRef2 = true;
                } else {
                    containsRef2 = false;
                }
                builder.addListItem(listValue);
            }
        } else if (o instanceof Map) {
            builder.setType(TypeSystem.Value.Type.MAP);
            for (Map.Entry<Object, Object> entry : ((Map) o).entrySet()) {
                TypeSystem.Value key = objectToValue(entry.getKey());
                TypeSystem.Value value = objectToValue(entry.getValue());
                if (key == DEFAULT_VALUE || value == DEFAULT_VALUE) {
                    return DEFAULT_VALUE;
                }
                if (containsRef2 || key.getContainsReferences() || value.getContainsReferences()) {
                    containsRef = true;
                } else {
                    containsRef = false;
                }
                builder.addMapKey(key);
                builder.addMapValue(value);
            }
        } else if (isDoubleableNumber(o)) {
            builder.setType(TypeSystem.Value.Type.STRING).setString(o.toString());
        } else if (isInt64ableNumber(o)) {
            builder.setType(TypeSystem.Value.Type.INTEGER).setInteger(getInt64(o));
        } else if (o instanceof Boolean) {
            builder.setType(TypeSystem.Value.Type.BOOLEAN).setBoolean(((Boolean) o).booleanValue());
        } else {
            Log.e("Converting to Value from unknown object type: " + (o == null ? "null" : o.getClass().toString()));
            return DEFAULT_VALUE;
        }
        if (containsRef2) {
            builder.setContainsReferences(true);
        }
        return builder.build();
    }

    public static TypeSystem.Value functionIdToValue(String id) {
        return TypeSystem.Value.newBuilder().setType(TypeSystem.Value.Type.FUNCTION_ID).setFunctionId(id).build();
    }

    public static TypeSystem.Value macroReferenceToValue(String macroName, TypeSystem.Value.Escaping... escapings) {
        TypeSystem.Value.Builder builder = TypeSystem.Value.newBuilder().setType(TypeSystem.Value.Type.MACRO_REFERENCE).setMacroReference(macroName).setContainsReferences(true);
        for (TypeSystem.Value.Escaping escaping : escapings) {
            builder.addEscaping(escaping);
        }
        return builder.build();
    }

    public static TypeSystem.Value templateToValue(TypeSystem.Value... tokens) {
        TypeSystem.Value.Builder builder = TypeSystem.Value.newBuilder().setType(TypeSystem.Value.Type.TEMPLATE);
        boolean containsRef = false;
        for (TypeSystem.Value token : tokens) {
            builder.addTemplateToken(token);
            containsRef = containsRef || token.getContainsReferences();
        }
        if (containsRef) {
            builder.setContainsReferences(true);
        }
        return builder.build();
    }

    private static boolean isDoubleableNumber(Object o) {
        return (o instanceof Double) || (o instanceof Float) || ((o instanceof TypedNumber) && ((TypedNumber) o).isDouble());
    }

    private static double getDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        Log.e("getDouble received non-Number");
        return 0.0d;
    }

    private static boolean isInt64ableNumber(Object o) {
        return (o instanceof Byte) || (o instanceof Short) || (o instanceof Integer) || (o instanceof Long) || ((o instanceof TypedNumber) && ((TypedNumber) o).isInt64());
    }

    private static long getInt64(Object o) {
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        Log.e("getInt64 received non-Number");
        return 0;
    }

    private static TypedNumber parseNumber(String s) {
        try {
            return TypedNumber.numberWithString(s);
        } catch (NumberFormatException e) {
            Log.e("Failed to convert '" + s + "' to a number.");
            return DEFAULT_NUMBER;
        }
    }

    private static Long parseInt64(String s) {
        TypedNumber result = parseNumber(s);
        return result == DEFAULT_NUMBER ? DEFAULT_INT64 : Long.valueOf(result.longValue());
    }

    private static Double parseDouble(String s) {
        TypedNumber result = parseNumber(s);
        return result == DEFAULT_NUMBER ? DEFAULT_DOUBLE : Double.valueOf(result.doubleValue());
    }

    private static Boolean parseBoolean(String s) {
        if ("true".equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(s)) {
            return Boolean.FALSE;
        }
        return DEFAULT_BOOLEAN;
    }

    public static Object valueToObject(TypeSystem.Value v) {
        if (v == null) {
            return DEFAULT_OBJECT;
        }
        switch (v.getType()) {
            case STRING:
                return v.getString();
            case LIST:
                ArrayList<Object> result = new ArrayList<>(v.getListItemCount());
                for (TypeSystem.Value val : v.getListItemList()) {
                    Object o = valueToObject(val);
                    if (o == DEFAULT_OBJECT) {
                        return DEFAULT_OBJECT;
                    }
                    result.add(o);
                }
                return result;
            case MAP:
                if (v.getMapKeyCount() != v.getMapValueCount()) {
                    Log.e("Converting an invalid value to object: " + v.toString());
                    return DEFAULT_OBJECT;
                }
                Map<Object, Object> result2 = new HashMap<>(v.getMapValueCount());
                for (int i = 0; i < v.getMapKeyCount(); i++) {
                    Object key = valueToObject(v.getMapKey(i));
                    Object value = valueToObject(v.getMapValue(i));
                    if (key == DEFAULT_OBJECT || value == DEFAULT_OBJECT) {
                        return DEFAULT_OBJECT;
                    }
                    result2.put(key, value);
                }
                return result2;
            case MACRO_REFERENCE:
                Log.e("Trying to convert a macro reference to object");
                return DEFAULT_OBJECT;
            case FUNCTION_ID:
                Log.e("Trying to convert a function id to object");
                return DEFAULT_OBJECT;
            case INTEGER:
                return Long.valueOf(v.getInteger());
            case TEMPLATE:
                StringBuffer result3 = new StringBuffer();
                for (TypeSystem.Value val2 : v.getTemplateTokenList()) {
                    String s = valueToString(val2);
                    if (s == DEFAULT_STRING) {
                        return DEFAULT_OBJECT;
                    }
                    result3.append(s);
                }
                return result3.toString();
            case BOOLEAN:
                return Boolean.valueOf(v.getBoolean());
            default:
                Log.e("Failed to convert a value of type: " + v.getType());
                return DEFAULT_OBJECT;
        }
    }
}
