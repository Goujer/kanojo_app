package twitter4j.internal.org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import jp.co.cybird.barcodekanojoForGAM.gree.core.GreeDefs;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public class JSONObject {
    public static final Object NULL = new Null();
    private Map map;

    private static final class Null {
        private Null() {
        }

        /* access modifiers changed from: protected */
        public final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return "null";
        }
    }

    public JSONObject() {
        this.map = new HashMap();
    }

    public JSONObject(JSONObject jo, String[] names) {
        this();
        for (int i = 0; i < names.length; i++) {
            try {
                putOnce(names[i], jo.opt(names[i]));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{' found:" + x.nextClean());
        }
        while (true) {
            switch (x.nextClean()) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    String key = x.nextValue().toString();
                    char c = x.nextClean();
                    if (c == '=') {
                        if (x.next() != '>') {
                            x.back();
                        }
                    } else if (c != ':') {
                        throw x.syntaxError("Expected a ':' after a key");
                    }
                    putOnce(key, x.nextValue());
                    switch (x.nextClean()) {
                        case ',':
                        case ';':
                            if (x.nextClean() != '}') {
                                x.back();
                            } else {
                                return;
                            }
                        case '}':
                            return;
                        default:
                            throw x.syntaxError("Expected a ',' or '}'");
                    }
            }
        }
    }

    public JSONObject(Map map2) {
        this.map = new HashMap();
        if (map2 != null) {
            for (Map.Entry e : map2.entrySet()) {
                Object value = e.getValue();
                if (value != null) {
                    this.map.put(e.getKey(), wrap(value));
                }
            }
        }
    }

    public JSONObject(Object bean) {
        this();
        populateMap(bean);
    }

    public JSONObject(Object object, String[] names) {
        this();
        Class c = object.getClass();
        for (String name : names) {
            try {
                putOpt(name, c.getField(name).get(object));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONObject(String baseName, Locale locale) throws JSONException {
        this();
        ResourceBundle r = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        Enumeration<String> keys = r.getKeys();
        while (keys.hasMoreElements()) {
            String nextElement = keys.nextElement();
            if (nextElement instanceof String) {
                String[] path = nextElement.split("\\.");
                int last = path.length - 1;
                JSONObject target = this;
                for (int i = 0; i < last; i++) {
                    String segment = path[i];
                    Object object = target.opt(segment);
                    JSONObject nextTarget = object instanceof JSONObject ? (JSONObject) object : null;
                    if (nextTarget == null) {
                        nextTarget = new JSONObject();
                        target.put(segment, (Object) nextTarget);
                    }
                    target = nextTarget;
                }
                target.put(path[last], (Object) r.getString(nextElement));
            }
        }
    }

    public JSONObject append(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = opt(key);
        if (object == null) {
            put(key, (Object) new JSONArray().put(value));
        } else if (object instanceof JSONArray) {
            put(key, (Object) ((JSONArray) object).put(value));
        } else {
            throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
        }
        return this;
    }

    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        Object object = opt(key);
        if (object != null) {
            return object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not found.");
    }

    public boolean getBoolean(String key) throws JSONException {
        Object object = get(key);
        if (object.equals(Boolean.FALSE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
    }

    public int getInt(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
        }
    }

    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
    }

    public long getLong(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
        }
    }

    public String getString(String key) throws JSONException {
        Object object = get(key);
        if (object == NULL) {
            return null;
        }
        return object.toString();
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public boolean isNull(String key) {
        return NULL.equals(opt(key));
    }

    public Iterator keys() {
        return this.map.keySet().iterator();
    }

    public int length() {
        return this.map.size();
    }

    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);
        String string = number.toString();
        if (string.indexOf(46) <= 0 || string.indexOf(101) >= 0 || string.indexOf(69) >= 0) {
            return string;
        }
        while (string.endsWith(GreeDefs.BARCODE)) {
            string = string.substring(0, string.length() - 1);
        }
        if (string.endsWith(".")) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    public Object opt(String key) {
        if (key == null) {
            return null;
        }
        return this.map.get(key);
    }

    private void populateMap(Object bean) {
        boolean includeSuperClass = false;
        Class klass = bean.getClass();
        if (klass.getClassLoader() != null) {
            includeSuperClass = true;
        }
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if (name.equals("getClass") || name.equals("getDeclaringClass")) {
                            key = "";
                        } else {
                            key = name.substring(3);
                        }
                    } else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }
                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }
                        Object result = method.invoke(bean, (Object[]) null);
                        if (result != null) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public JSONObject put(String key, boolean value) throws JSONException {
        put(key, (Object) value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject put(String key, Collection value) throws JSONException {
        put(key, (Object) new JSONArray(value));
        return this;
    }

    public JSONObject put(String key, double value) throws JSONException {
        put(key, (Object) new Double(value));
        return this;
    }

    public JSONObject put(String key, int value) throws JSONException {
        put(key, (Object) new Integer(value));
        return this;
    }

    public JSONObject put(String key, long value) throws JSONException {
        put(key, (Object) new Long(value));
        return this;
    }

    public JSONObject put(String key, Map value) throws JSONException {
        put(key, (Object) new JSONObject(value));
        return this;
    }

    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }

    public JSONObject putOnce(String key, Object value) throws JSONException {
        if (!(key == null || value == null)) {
            if (opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }
            put(key, value);
        }
        return this;
    }

    public JSONObject putOpt(String key, Object value) throws JSONException {
        if (!(key == null || value == null)) {
            put(key, value);
        }
        return this;
    }

    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }
        char c = 0;
        int len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        sb.append('\"');
        for (int i = 0; i < len; i++) {
            char b = c;
            c = string.charAt(i);
            switch (c) {
                case 8:
                    sb.append("\\b");
                    break;
                case 9:
                    sb.append("\\t");
                    break;
                case 10:
                    sb.append("\\n");
                    break;
                case 12:
                    sb.append("\\f");
                    break;
                case 13:
                    sb.append("\\r");
                    break;
                case '\"':
                case '\\':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case DateTimeParserConstants.QUOTEDPAIR:
                    if (b == '<') {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                default:
                    if (c >= ' ' && ((c < 128 || c >= 160) && (c < 8192 || c >= 8448))) {
                        sb.append(c);
                        break;
                    } else {
                        String hhhh = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hhhh.substring(hhhh.length() - 4));
                        break;
                    }
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    public Object remove(String key) {
        return this.map.remove(key);
    }

    public Iterator sortedKeys() {
        return new TreeSet(this.map.keySet()).iterator();
    }

    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return NULL;
        }
        char b = string.charAt(0);
        if ((b < '0' || b > '9') && b != '.' && b != '-' && b != '+') {
            return string;
        }
        if (b == '0' && string.length() > 2 && (string.charAt(1) == 'x' || string.charAt(1) == 'X')) {
            try {
                return Integer.valueOf(Integer.parseInt(string.substring(2), 16));
            } catch (Exception e) {
            }
        }
        try {
            if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1) {
                return Double.valueOf(string);
            }
            Long myLong = new Long(string);
            return myLong.longValue() == ((long) myLong.intValue()) ? Integer.valueOf(myLong.intValue()) : myLong;
        } catch (Exception e2) {
            return string;
        }
    }

    public static void testValidity(Object o) throws JSONException {
        if (o == null) {
            return;
        }
        if (o instanceof Double) {
            if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        } else if (!(o instanceof Float)) {
        } else {
            if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }

    public String toString() {
        try {
            Iterator keys = keys();
            StringBuilder sb = new StringBuilder("{");
            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(this.map.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        return toString(indentFactor, 0);
    }

    /* access modifiers changed from: package-private */
    public String toString(int indentFactor, int indent) throws JSONException {
        int length = length();
        if (length == 0) {
            return "{}";
        }
        Iterator keys = sortedKeys();
        int newindent = indent + indentFactor;
        StringBuilder sb = new StringBuilder("{");
        if (length == 1) {
            Object object = keys.next();
            sb.append(quote(object.toString()));
            sb.append(": ");
            sb.append(valueToString(this.map.get(object), indentFactor, indent));
        } else {
            while (keys.hasNext()) {
                Object object2 = keys.next();
                if (sb.length() > 1) {
                    sb.append(",\n");
                } else {
                    sb.append(10);
                }
                for (int i = 0; i < newindent; i++) {
                    sb.append(' ');
                }
                sb.append(quote(object2.toString()));
                sb.append(": ");
                sb.append(valueToString(this.map.get(object2), indentFactor, newindent));
            }
            if (sb.length() > 1) {
                sb.append(10);
                for (int i2 = 0; i2 < indent; i2++) {
                    sb.append(' ');
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals((Object) null)) {
            return "null";
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if ((value instanceof Boolean) || (value instanceof JSONObject) || (value instanceof JSONArray)) {
            return value.toString();
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString();
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString();
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString();
        }
        return quote(value.toString());
    }

    static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
        if (value == null || value.equals((Object) null)) {
            return "null";
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject) value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray) {
            return ((JSONArray) value).toString(indentFactor, indent);
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString(indentFactor, indent);
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString(indentFactor, indent);
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString(indentFactor, indent);
        }
        return quote(value.toString());
    }

    public static Object wrap(Object object) {
        if (object == null) {
            try {
                return NULL;
            } catch (Exception e) {
                return null;
            }
        } else if ((object instanceof JSONObject) || (object instanceof JSONArray) || NULL.equals(object) || (object instanceof Byte) || (object instanceof Character) || (object instanceof Short) || (object instanceof Integer) || (object instanceof Long) || (object instanceof Boolean) || (object instanceof Float) || (object instanceof Double) || (object instanceof String)) {
            return object;
        } else {
            if (object instanceof Collection) {
                return new JSONArray((Collection) object);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                return new JSONObject((Map) object);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        }
    }

    public Writer write(Writer writer) throws JSONException {
        boolean commanate = false;
        try {
            Iterator keys = keys();
            writer.write(123);
            while (keys.hasNext()) {
                if (commanate) {
                    writer.write(44);
                }
                Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(58);
                Object value = this.map.get(key);
                if (value instanceof JSONObject) {
                    ((JSONObject) value).write(writer);
                } else if (value instanceof JSONArray) {
                    ((JSONArray) value).write(writer);
                } else {
                    writer.write(valueToString(value));
                }
                commanate = true;
            }
            writer.write(125);
            return writer;
        } catch (IOException exception) {
            throw new JSONException((Throwable) exception);
        }
    }
}
