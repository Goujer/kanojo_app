package twitter4j.internal.org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JSONArray {
    private ArrayList myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList();
    }

    public JSONArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        } else if (x.nextClean() != ']') {
            x.back();
            while (true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(JSONObject.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',':
                    case ';':
                        if (x.nextClean() != ']') {
                            x.back();
                        } else {
                            return;
                        }
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    public JSONArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONArray(Collection collection) {
        this.myArrayList = new ArrayList();
        if (collection != null) {
            for (Object aCollection : collection) {
                this.myArrayList.add(JSONObject.wrap(aCollection));
            }
        }
    }

    public JSONArray(Object array) throws JSONException {
        this();
        if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                put(JSONObject.wrap(Array.get(array, i)));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }

    public Object get(int index) throws JSONException {
        Object object = opt(index);
        if (object != null) {
            return object;
        }
        throw new JSONException("JSONArray[" + index + "] not found.");
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = get(index);
        if (object.equals(Boolean.FALSE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONArray[" + index + "] is not a boolean.");
    }

    public double getDouble(int index) throws JSONException {
        Object object = get(index);
        try {
            return object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = get(index);
        try {
            return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public JSONArray getJSONArray(int index) throws JSONException {
        Object object = get(index);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(int index) throws JSONException {
        Object object = get(index);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }

    public long getLong(int index) throws JSONException {
        Object object = get(index);
        try {
            return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.");
        }
    }

    public String getString(int index) throws JSONException {
        Object object = get(index);
        if (object == JSONObject.NULL) {
            return null;
        }
        return object.toString();
    }

    public boolean isNull(int index) {
        return JSONObject.NULL.equals(opt(index));
    }

    public String join(String separator) throws JSONException {
        int len = length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int index) {
        if (index < 0 || index >= length()) {
            return null;
        }
        return this.myArrayList.get(index);
    }

    public JSONArray put(boolean value) {
        put((Object) value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(Collection value) {
        put((Object) new JSONArray(value));
        return this;
    }

    public JSONArray put(int value) {
        put((Object) new Integer(value));
        return this;
    }

    public JSONArray put(long value) {
        put((Object) new Long(value));
        return this;
    }

    public JSONArray put(Map value) {
        put((Object) new JSONObject(value));
        return this;
    }

    public JSONArray put(Object value) {
        this.myArrayList.add(value);
        return this;
    }

    public JSONArray put(int index, boolean value) throws JSONException {
        put(index, (Object) value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(int index, Collection value) throws JSONException {
        put(index, (Object) new JSONArray(value));
        return this;
    }

    public JSONArray put(int index, double value) throws JSONException {
        put(index, (Object) new Double(value));
        return this;
    }

    public JSONArray put(int index, int value) throws JSONException {
        put(index, (Object) new Integer(value));
        return this;
    }

    public JSONArray put(int index, long value) throws JSONException {
        put(index, (Object) new Long(value));
        return this;
    }

    public JSONArray put(int index, Map value) throws JSONException {
        put(index, (Object) new JSONObject(value));
        return this;
    }

    public JSONArray put(int index, Object value) throws JSONException {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < length()) {
            this.myArrayList.set(index, value);
        } else {
            while (index != length()) {
                put(JSONObject.NULL);
            }
            put(value);
        }
        return this;
    }

    public String toString() {
        try {
            return '[' + join(",") + ']';
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        return toString(indentFactor, 0);
    }

    /* access modifiers changed from: package-private */
    public String toString(int indentFactor, int indent) throws JSONException {
        int len = length();
        if (len == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        if (len == 1) {
            sb.append(JSONObject.valueToString(this.myArrayList.get(0), indentFactor, indent));
        } else {
            int newindent = indent + indentFactor;
            sb.append(10);
            for (int i = 0; i < len; i++) {
                if (i > 0) {
                    sb.append(",\n");
                }
                for (int j = 0; j < newindent; j++) {
                    sb.append(' ');
                }
                sb.append(JSONObject.valueToString(this.myArrayList.get(i), indentFactor, newindent));
            }
            sb.append(10);
            for (int i2 = 0; i2 < indent; i2++) {
                sb.append(' ');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public Writer write(Writer writer) throws JSONException {
        boolean b = false;
        try {
            int len = length();
            writer.write(91);
            for (int i = 0; i < len; i++) {
                if (b) {
                    writer.write(44);
                }
                Object v = this.myArrayList.get(i);
                if (v instanceof JSONObject) {
                    ((JSONObject) v).write(writer);
                } else if (v instanceof JSONArray) {
                    ((JSONArray) v).write(writer);
                } else {
                    writer.write(JSONObject.valueToString(v));
                }
                b = true;
            }
            writer.write(93);
            return writer;
        } catch (IOException e) {
            throw new JSONException((Throwable) e);
        }
    }
}
