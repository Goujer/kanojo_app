package com.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreferencesTokenCachingStrategy extends TokenCachingStrategy {
    private static final String DEFAULT_CACHE_KEY = "com.facebook.SharedPreferencesTokenCachingStrategy.DEFAULT_KEY";
    private static final String JSON_VALUE = "value";
    private static final String JSON_VALUE_ENUM_TYPE = "enumType";
    private static final String JSON_VALUE_TYPE = "valueType";
    private static final String TAG = SharedPreferencesTokenCachingStrategy.class.getSimpleName();
    private static final String TYPE_BOOLEAN = "bool";
    private static final String TYPE_BOOLEAN_ARRAY = "bool[]";
    private static final String TYPE_BYTE = "byte";
    private static final String TYPE_BYTE_ARRAY = "byte[]";
    private static final String TYPE_CHAR = "char";
    private static final String TYPE_CHAR_ARRAY = "char[]";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_DOUBLE_ARRAY = "double[]";
    private static final String TYPE_ENUM = "enum";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_FLOAT_ARRAY = "float[]";
    private static final String TYPE_INTEGER = "int";
    private static final String TYPE_INTEGER_ARRAY = "int[]";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_LONG_ARRAY = "long[]";
    private static final String TYPE_SHORT = "short";
    private static final String TYPE_SHORT_ARRAY = "short[]";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_STRING_LIST = "stringList";
    private SharedPreferences cache;
    private String cacheKey;

    public SharedPreferencesTokenCachingStrategy(Context context) {
        this(context, (String) null);
    }

    public SharedPreferencesTokenCachingStrategy(Context context, String cacheKey2) {
        Validate.notNull(context, "context");
        this.cacheKey = Utility.isNullOrEmpty(cacheKey2) ? DEFAULT_CACHE_KEY : cacheKey2;
        Context applicationContext = context.getApplicationContext();
        this.cache = (applicationContext != null ? applicationContext : context).getSharedPreferences(this.cacheKey, 0);
    }

    public Bundle load() {
        Bundle settings = new Bundle();
        for (String key : this.cache.getAll().keySet()) {
            try {
                deserializeKey(key, settings);
            } catch (JSONException e) {
                Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error reading cached value for key: '" + key + "' -- " + e);
                return null;
            }
        }
        return settings;
    }

    public void save(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        SharedPreferences.Editor editor = this.cache.edit();
        for (String key : bundle.keySet()) {
            try {
                serializeKey(key, bundle, editor);
            } catch (JSONException e) {
                Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error processing value for key: '" + key + "' -- " + e);
                return;
            }
        }
        if (!editor.commit()) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "SharedPreferences.Editor.commit() was not successful");
        }
    }

    public void clear() {
        this.cache.edit().clear().commit();
    }

    private void serializeKey(String key, Bundle bundle, SharedPreferences.Editor editor) throws JSONException {
        int i = 0;
        Object value = bundle.get(key);
        if (value != null) {
            String supportedType = null;
            JSONArray jsonArray = null;
            JSONObject json = new JSONObject();
            if (value instanceof Byte) {
                supportedType = TYPE_BYTE;
                json.put(JSON_VALUE, ((Byte) value).intValue());
            } else if (value instanceof Short) {
                supportedType = TYPE_SHORT;
                json.put(JSON_VALUE, ((Short) value).intValue());
            } else if (value instanceof Integer) {
                supportedType = TYPE_INTEGER;
                json.put(JSON_VALUE, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                supportedType = TYPE_LONG;
                json.put(JSON_VALUE, ((Long) value).longValue());
            } else if (value instanceof Float) {
                supportedType = TYPE_FLOAT;
                json.put(JSON_VALUE, ((Float) value).doubleValue());
            } else if (value instanceof Double) {
                supportedType = TYPE_DOUBLE;
                json.put(JSON_VALUE, ((Double) value).doubleValue());
            } else if (value instanceof Boolean) {
                supportedType = TYPE_BOOLEAN;
                json.put(JSON_VALUE, ((Boolean) value).booleanValue());
            } else if (value instanceof Character) {
                supportedType = TYPE_CHAR;
                json.put(JSON_VALUE, value.toString());
            } else if (value instanceof String) {
                supportedType = TYPE_STRING;
                json.put(JSON_VALUE, (String) value);
            } else if (value instanceof Enum) {
                supportedType = TYPE_ENUM;
                json.put(JSON_VALUE, value.toString());
                json.put(JSON_VALUE_ENUM_TYPE, value.getClass().getName());
            } else {
                jsonArray = new JSONArray();
                if (value instanceof byte[]) {
                    supportedType = TYPE_BYTE_ARRAY;
                    byte[] value2 = (byte[]) value;
                    int length = value2.length;
                    while (i < length) {
                        jsonArray.put(value2[i]);
                        i++;
                    }
                } else if (value instanceof short[]) {
                    supportedType = TYPE_SHORT_ARRAY;
                    short[] value3 = (short[]) value;
                    int length2 = value3.length;
                    while (i < length2) {
                        jsonArray.put(value3[i]);
                        i++;
                    }
                } else if (value instanceof int[]) {
                    supportedType = TYPE_INTEGER_ARRAY;
                    int[] iArr = (int[]) value;
                    int length3 = iArr.length;
                    while (i < length3) {
                        jsonArray.put(iArr[i]);
                        i++;
                    }
                } else if (value instanceof long[]) {
                    supportedType = TYPE_LONG_ARRAY;
                    long[] jArr = (long[]) value;
                    int length4 = jArr.length;
                    while (i < length4) {
                        jsonArray.put(jArr[i]);
                        i++;
                    }
                } else if (value instanceof float[]) {
                    supportedType = TYPE_FLOAT_ARRAY;
                    float[] fArr = (float[]) value;
                    int length5 = fArr.length;
                    while (i < length5) {
                        jsonArray.put((double) fArr[i]);
                        i++;
                    }
                } else if (value instanceof double[]) {
                    supportedType = TYPE_DOUBLE_ARRAY;
                    double[] dArr = (double[]) value;
                    int length6 = dArr.length;
                    while (i < length6) {
                        jsonArray.put(dArr[i]);
                        i++;
                    }
                } else if (value instanceof boolean[]) {
                    supportedType = TYPE_BOOLEAN_ARRAY;
                    boolean[] value4 = (boolean[]) value;
                    int length7 = value4.length;
                    while (i < length7) {
                        jsonArray.put(value4[i]);
                        i++;
                    }
                } else if (value instanceof char[]) {
                    supportedType = TYPE_CHAR_ARRAY;
                    char[] value5 = (char[]) value;
                    int length8 = value5.length;
                    while (i < length8) {
                        jsonArray.put(String.valueOf(value5[i]));
                        i++;
                    }
                } else if (value instanceof List) {
                    supportedType = TYPE_STRING_LIST;
                    for (Object next : (List) value) {
                        if (next == null) {
                            next = JSONObject.NULL;
                        }
                        jsonArray.put(next);
                    }
                } else {
                    jsonArray = null;
                }
            }
            if (supportedType != null) {
                json.put(JSON_VALUE_TYPE, supportedType);
                if (jsonArray != null) {
                    json.putOpt(JSON_VALUE, jsonArray);
                }
                editor.putString(key, json.toString());
            }
        }
    }

    private void deserializeKey(String key, Bundle bundle) throws JSONException {
        JSONObject json = new JSONObject(this.cache.getString(key, "{}"));
        String valueType = json.getString(JSON_VALUE_TYPE);
        if (valueType.equals(TYPE_BOOLEAN)) {
            bundle.putBoolean(key, json.getBoolean(JSON_VALUE));
        } else if (valueType.equals(TYPE_BOOLEAN_ARRAY)) {
            JSONArray jsonArray = json.getJSONArray(JSON_VALUE);
            boolean[] array = new boolean[jsonArray.length()];
            for (int i = 0; i < array.length; i++) {
                array[i] = jsonArray.getBoolean(i);
            }
            bundle.putBooleanArray(key, array);
        } else if (valueType.equals(TYPE_BYTE)) {
            bundle.putByte(key, (byte) json.getInt(JSON_VALUE));
        } else if (valueType.equals(TYPE_BYTE_ARRAY)) {
            JSONArray jsonArray2 = json.getJSONArray(JSON_VALUE);
            byte[] array2 = new byte[jsonArray2.length()];
            for (int i2 = 0; i2 < array2.length; i2++) {
                array2[i2] = (byte) jsonArray2.getInt(i2);
            }
            bundle.putByteArray(key, array2);
        } else if (valueType.equals(TYPE_SHORT)) {
            bundle.putShort(key, (short) json.getInt(JSON_VALUE));
        } else if (valueType.equals(TYPE_SHORT_ARRAY)) {
            JSONArray jsonArray3 = json.getJSONArray(JSON_VALUE);
            short[] array3 = new short[jsonArray3.length()];
            for (int i3 = 0; i3 < array3.length; i3++) {
                array3[i3] = (short) jsonArray3.getInt(i3);
            }
            bundle.putShortArray(key, array3);
        } else if (valueType.equals(TYPE_INTEGER)) {
            bundle.putInt(key, json.getInt(JSON_VALUE));
        } else if (valueType.equals(TYPE_INTEGER_ARRAY)) {
            JSONArray jsonArray4 = json.getJSONArray(JSON_VALUE);
            int[] array4 = new int[jsonArray4.length()];
            for (int i4 = 0; i4 < array4.length; i4++) {
                array4[i4] = jsonArray4.getInt(i4);
            }
            bundle.putIntArray(key, array4);
        } else if (valueType.equals(TYPE_LONG)) {
            bundle.putLong(key, json.getLong(JSON_VALUE));
        } else if (valueType.equals(TYPE_LONG_ARRAY)) {
            JSONArray jsonArray5 = json.getJSONArray(JSON_VALUE);
            long[] array5 = new long[jsonArray5.length()];
            for (int i5 = 0; i5 < array5.length; i5++) {
                array5[i5] = jsonArray5.getLong(i5);
            }
            bundle.putLongArray(key, array5);
        } else if (valueType.equals(TYPE_FLOAT)) {
            bundle.putFloat(key, (float) json.getDouble(JSON_VALUE));
        } else if (valueType.equals(TYPE_FLOAT_ARRAY)) {
            JSONArray jsonArray6 = json.getJSONArray(JSON_VALUE);
            float[] array6 = new float[jsonArray6.length()];
            for (int i6 = 0; i6 < array6.length; i6++) {
                array6[i6] = (float) jsonArray6.getDouble(i6);
            }
            bundle.putFloatArray(key, array6);
        } else if (valueType.equals(TYPE_DOUBLE)) {
            bundle.putDouble(key, json.getDouble(JSON_VALUE));
        } else if (valueType.equals(TYPE_DOUBLE_ARRAY)) {
            JSONArray jsonArray7 = json.getJSONArray(JSON_VALUE);
            double[] array7 = new double[jsonArray7.length()];
            for (int i7 = 0; i7 < array7.length; i7++) {
                array7[i7] = jsonArray7.getDouble(i7);
            }
            bundle.putDoubleArray(key, array7);
        } else if (valueType.equals(TYPE_CHAR)) {
            String charString = json.getString(JSON_VALUE);
            if (charString != null && charString.length() == 1) {
                bundle.putChar(key, charString.charAt(0));
            }
        } else if (valueType.equals(TYPE_CHAR_ARRAY)) {
            JSONArray jsonArray8 = json.getJSONArray(JSON_VALUE);
            char[] array8 = new char[jsonArray8.length()];
            for (int i8 = 0; i8 < array8.length; i8++) {
                String charString2 = jsonArray8.getString(i8);
                if (charString2 != null && charString2.length() == 1) {
                    array8[i8] = charString2.charAt(0);
                }
            }
            bundle.putCharArray(key, array8);
        } else if (valueType.equals(TYPE_STRING)) {
            bundle.putString(key, json.getString(JSON_VALUE));
        } else if (valueType.equals(TYPE_STRING_LIST)) {
            JSONArray jsonArray9 = json.getJSONArray(JSON_VALUE);
            int numStrings = jsonArray9.length();
            ArrayList<String> stringList = new ArrayList<>(numStrings);
            for (int i9 = 0; i9 < numStrings; i9++) {
                Object jsonStringValue = jsonArray9.get(i9);
                stringList.add(i9, jsonStringValue == JSONObject.NULL ? null : (String) jsonStringValue);
            }
            bundle.putStringArrayList(key, stringList);
        } else if (valueType.equals(TYPE_ENUM)) {
            try {
                bundle.putSerializable(key, Enum.valueOf(Class.forName(json.getString(JSON_VALUE_ENUM_TYPE)), json.getString(JSON_VALUE)));
            } catch (ClassNotFoundException | IllegalArgumentException e) {
            }
        }
    }
}
