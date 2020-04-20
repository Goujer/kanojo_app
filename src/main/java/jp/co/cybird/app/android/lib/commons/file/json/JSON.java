package jp.co.cybird.app.android.lib.commons.file.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Array;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.RandomAccess;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Pattern;
import jp.co.cybird.app.android.lib.commons.file.json.io.AppendableOutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.CharSequenceInputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.InputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.ReaderInputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.StringBufferInputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.StringBuilderInputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.StringBuilderOutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.StringInputSource;
import jp.co.cybird.app.android.lib.commons.file.json.io.WriterOutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;
import jp.co.cybird.app.android.lib.commons.file.json.util.LocalCache;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class JSON {
    private static final Map<Class<?>, Converter> CONVERT_MAP = new HashMap(50);
    /* access modifiers changed from: private */
    public static final Map<Class<?>, Formatter> FORMAT_MAP = new HashMap(50);
    public static volatile Class<? extends JSON> prototype = JSON.class;
    Object contextObject;
    String dateFormat;
    NamingStyle enumStyle = NamingStyle.NOOP;
    String indentText = "\t";
    int initialIndent = 0;
    Locale locale = Locale.getDefault();
    int maxDepth = 32;
    Mode mode = Mode.TRADITIONAL;
    String numberFormat;
    boolean prettyPrint = false;
    NamingStyle propertyStyle = NamingStyle.NOOP;
    boolean suppressNull = false;
    TimeZone timeZone = TimeZone.getDefault();

    public enum Mode {
        TRADITIONAL,
        STRICT,
        SCRIPT
    }

    static {
        FORMAT_MAP.put(Boolean.TYPE, PlainFormatter.INSTANCE);
        FORMAT_MAP.put(Character.TYPE, StringFormatter.INSTANCE);
        FORMAT_MAP.put(Byte.TYPE, ByteFormatter.INSTANCE);
        FORMAT_MAP.put(Short.TYPE, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Integer.TYPE, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Long.TYPE, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Float.TYPE, FloatFormatter.INSTANCE);
        FORMAT_MAP.put(Double.TYPE, FloatFormatter.INSTANCE);
        FORMAT_MAP.put(boolean[].class, BooleanArrayFormatter.INSTANCE);
        FORMAT_MAP.put(char[].class, CharArrayFormatter.INSTANCE);
        FORMAT_MAP.put(byte[].class, ByteArrayFormatter.INSTANCE);
        FORMAT_MAP.put(short[].class, ShortArrayFormatter.INSTANCE);
        FORMAT_MAP.put(int[].class, IntArrayFormatter.INSTANCE);
        FORMAT_MAP.put(long[].class, LongArrayFormatter.INSTANCE);
        FORMAT_MAP.put(float[].class, FloatArrayFormatter.INSTANCE);
        FORMAT_MAP.put(double[].class, DoubleArrayFormatter.INSTANCE);
        FORMAT_MAP.put(Object[].class, ObjectArrayFormatter.INSTANCE);
        FORMAT_MAP.put(Boolean.class, PlainFormatter.INSTANCE);
        FORMAT_MAP.put(Character.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(Byte.class, ByteFormatter.INSTANCE);
        FORMAT_MAP.put(Short.class, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Integer.class, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Long.class, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(Float.class, FloatFormatter.INSTANCE);
        FORMAT_MAP.put(Double.class, FloatFormatter.INSTANCE);
        FORMAT_MAP.put(BigInteger.class, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(BigDecimal.class, NumberFormatter.INSTANCE);
        FORMAT_MAP.put(String.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(Date.class, DateFormatter.INSTANCE);
        FORMAT_MAP.put(java.sql.Date.class, DateFormatter.INSTANCE);
        FORMAT_MAP.put(Time.class, DateFormatter.INSTANCE);
        FORMAT_MAP.put(Timestamp.class, DateFormatter.INSTANCE);
        FORMAT_MAP.put(URI.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(URL.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(UUID.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(Pattern.class, StringFormatter.INSTANCE);
        FORMAT_MAP.put(Class.class, ClassFormatter.INSTANCE);
        FORMAT_MAP.put(Locale.class, LocaleFormatter.INSTANCE);
        FORMAT_MAP.put(ArrayList.class, ListFormatter.INSTANCE);
        FORMAT_MAP.put(LinkedList.class, IterableFormatter.INSTANCE);
        FORMAT_MAP.put(HashSet.class, IterableFormatter.INSTANCE);
        FORMAT_MAP.put(TreeSet.class, IterableFormatter.INSTANCE);
        FORMAT_MAP.put(LinkedHashSet.class, IterableFormatter.INSTANCE);
        FORMAT_MAP.put(HashMap.class, MapFormatter.INSTANCE);
        FORMAT_MAP.put(IdentityHashMap.class, MapFormatter.INSTANCE);
        FORMAT_MAP.put(Properties.class, MapFormatter.INSTANCE);
        FORMAT_MAP.put(TreeMap.class, MapFormatter.INSTANCE);
        FORMAT_MAP.put(LinkedHashMap.class, MapFormatter.INSTANCE);
        CONVERT_MAP.put(Boolean.TYPE, BooleanConverter.INSTANCE);
        CONVERT_MAP.put(Character.TYPE, CharacterConverter.INSTANCE);
        CONVERT_MAP.put(Byte.TYPE, ByteConverter.INSTANCE);
        CONVERT_MAP.put(Short.TYPE, ShortConverter.INSTANCE);
        CONVERT_MAP.put(Integer.TYPE, IntegerConverter.INSTANCE);
        CONVERT_MAP.put(Long.TYPE, LongConverter.INSTANCE);
        CONVERT_MAP.put(Float.TYPE, FloatConverter.INSTANCE);
        CONVERT_MAP.put(Double.TYPE, DoubleConverter.INSTANCE);
        CONVERT_MAP.put(boolean[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(char[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(byte[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(short[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(int[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(long[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(float[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(double[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(Object[].class, ArrayConverter.INSTANCE);
        CONVERT_MAP.put(Boolean.class, BooleanConverter.INSTANCE);
        CONVERT_MAP.put(Character.class, CharacterConverter.INSTANCE);
        CONVERT_MAP.put(Byte.class, ByteConverter.INSTANCE);
        CONVERT_MAP.put(Short.class, ShortConverter.INSTANCE);
        CONVERT_MAP.put(Integer.class, IntegerConverter.INSTANCE);
        CONVERT_MAP.put(Long.class, LongConverter.INSTANCE);
        CONVERT_MAP.put(Float.class, FloatConverter.INSTANCE);
        CONVERT_MAP.put(Double.class, DoubleConverter.INSTANCE);
        CONVERT_MAP.put(BigInteger.class, BigIntegerConverter.INSTANCE);
        CONVERT_MAP.put(BigDecimal.class, BigDecimalConverter.INSTANCE);
        CONVERT_MAP.put(Number.class, BigDecimalConverter.INSTANCE);
        CONVERT_MAP.put(Pattern.class, PatternConverter.INSTANCE);
        CONVERT_MAP.put(TimeZone.class, TimeZoneConverter.INSTANCE);
        CONVERT_MAP.put(Locale.class, LocaleConverter.INSTANCE);
        CONVERT_MAP.put(File.class, FileConverter.INSTANCE);
        CONVERT_MAP.put(URL.class, URLConverter.INSTANCE);
        CONVERT_MAP.put(URI.class, URIConverter.INSTANCE);
        CONVERT_MAP.put(UUID.class, UUIDConverter.INSTANCE);
        CONVERT_MAP.put(Charset.class, CharsetConverter.INSTANCE);
        CONVERT_MAP.put(Class.class, ClassConverter.INSTANCE);
        CONVERT_MAP.put(Date.class, DateConverter.INSTANCE);
        CONVERT_MAP.put(java.sql.Date.class, DateConverter.INSTANCE);
        CONVERT_MAP.put(Time.class, DateConverter.INSTANCE);
        CONVERT_MAP.put(Timestamp.class, DateConverter.INSTANCE);
        CONVERT_MAP.put(Calendar.class, CalendarConverter.INSTANCE);
        CONVERT_MAP.put(Collection.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(Set.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(List.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(SortedSet.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(LinkedList.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(HashSet.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(TreeSet.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(LinkedHashSet.class, CollectionConverter.INSTANCE);
        CONVERT_MAP.put(Map.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(SortedMap.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(HashMap.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(IdentityHashMap.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(TreeMap.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(LinkedHashMap.class, MapConverter.INSTANCE);
        CONVERT_MAP.put(Properties.class, PropertiesConverter.INSTANCE);
    }

    static JSON newInstance() {
        try {
            return (JSON) prototype.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String encode(Object source) throws JSONException {
        return encode(source, false);
    }

    public static String encode(Object source, boolean prettyPrint2) throws JSONException {
        JSON json = newInstance();
        json.setPrettyPrint(prettyPrint2);
        return json.format(source);
    }

    public static void encode(Object source, OutputStream out) throws IOException, JSONException {
        newInstance().format(source, (Appendable) new OutputStreamWriter(out, "UTF-8"));
    }

    public static void encode(Object source, OutputStream out, boolean prettyPrint2) throws IOException, JSONException {
        JSON json = newInstance();
        json.setPrettyPrint(prettyPrint2);
        json.format(source, (Appendable) new OutputStreamWriter(out, "UTF-8"));
    }

    public static void encode(Object source, Appendable appendable) throws IOException, JSONException {
        newInstance().format(source, appendable);
    }

    public static void encode(Object source, Appendable appendable, boolean prettyPrint2) throws IOException, JSONException {
        JSON json = newInstance();
        json.setPrettyPrint(prettyPrint2);
        json.format(source, appendable);
    }

    public static String escapeScript(Object source) throws JSONException {
        JSON json = newInstance();
        json.setMode(Mode.SCRIPT);
        return json.format(source);
    }

    public static void escapeScript(Object source, OutputStream out) throws IOException, JSONException {
        JSON json = newInstance();
        json.setMode(Mode.SCRIPT);
        json.format(source, out);
    }

    public static void escapeScript(Object source, Appendable appendable) throws IOException, JSONException {
        JSON json = newInstance();
        json.setMode(Mode.SCRIPT);
        json.format(source, appendable);
    }

    public static <T> T decode(String source) throws JSONException {
        return newInstance().parse((CharSequence) source);
    }

    public static <T> T decode(String source, Class<? extends T> cls) throws JSONException {
        return newInstance().parse((CharSequence) source, cls);
    }

    public static <T> T decode(String source, Type type) throws JSONException {
        return newInstance().parse((CharSequence) source, type);
    }

    public static <T> T decode(InputStream in) throws IOException, JSONException {
        return newInstance().parse(in);
    }

    public static <T> T decode(InputStream in, Class<? extends T> cls) throws IOException, JSONException {
        return newInstance().parse(in, cls);
    }

    public static <T> T decode(InputStream in, Type type) throws IOException, JSONException {
        return newInstance().parse(in, type);
    }

    public static <T> T decode(Reader reader) throws IOException, JSONException {
        return newInstance().parse(reader);
    }

    public static <T> T decode(Reader reader, Class<? extends T> cls) throws IOException, JSONException {
        return newInstance().parse(reader, cls);
    }

    public static <T> T decode(Reader reader, Type type) throws IOException, JSONException {
        return newInstance().parse(reader, type);
    }

    public static void validate(CharSequence cs) throws JSONException {
        JSON json = newInstance();
        json.setMode(Mode.STRICT);
        json.setMaxDepth(0);
        json.parse(cs);
    }

    public static void validate(InputStream in) throws IOException, JSONException {
        JSON json = newInstance();
        json.setMode(Mode.STRICT);
        json.setMaxDepth(0);
        json.parse(in);
    }

    public static void validate(Reader reader) throws IOException, JSONException {
        JSON json = newInstance();
        json.setMode(Mode.STRICT);
        json.setMaxDepth(0);
        json.parse(reader);
    }

    public JSON() {
    }

    public JSON(int maxDepth2) {
        setMaxDepth(maxDepth2);
    }

    public JSON(Mode mode2) {
        setMode(mode2);
    }

    public void setContext(Object value) {
        this.contextObject = value;
    }

    public void setLocale(Locale locale2) {
        if (locale2 == null) {
            throw new NullPointerException();
        }
        this.locale = locale2;
    }

    public void setTimeZone(TimeZone timeZone2) {
        if (timeZone2 == null) {
            throw new NullPointerException();
        }
        this.timeZone = timeZone2;
    }

    public void setPrettyPrint(boolean value) {
        this.prettyPrint = value;
    }

    public void setInitialIndent(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException(getMessage("json.TooSmallArgumentError", "initialIndent", 0));
        } else {
            this.initialIndent = indent;
        }
    }

    public void setIndentText(String text) {
        this.indentText = text;
    }

    public void setMaxDepth(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(getMessage("json.TooSmallArgumentError", "maxDepth", 0));
        } else {
            this.maxDepth = value;
        }
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public void setSuppressNull(boolean value) {
        this.suppressNull = value;
    }

    public void setMode(Mode mode2) {
        if (mode2 == null) {
            throw new NullPointerException();
        }
        this.mode = mode2;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setDateFormat(String format) {
        this.dateFormat = format;
    }

    public void setNumberFormat(String format) {
        this.numberFormat = format;
    }

    public void setPropertyStyle(NamingStyle style) {
        this.propertyStyle = style;
    }

    public void setEnumStyle(NamingStyle style) {
        this.enumStyle = style;
    }

    public String format(Object source) {
        try {
            return format(source, (Appendable) new StringBuilder(1000)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    public OutputStream format(Object source, OutputStream out) throws IOException {
        format(source, (Appendable) new BufferedWriter(new OutputStreamWriter(out, "UTF-8")));
        return out;
    }

    public Appendable format(Object source, Appendable ap) throws IOException {
        OutputSource fs;
        JSONContext context = new JSONContext();
        if (ap instanceof Writer) {
            fs = new WriterOutputSource((Writer) ap);
        } else if (ap instanceof StringBuilder) {
            fs = new StringBuilderOutputSource((StringBuilder) ap);
        } else {
            fs = new AppendableOutputSource(ap);
        }
        context.enter('$', (JSONHint) null);
        Object source2 = context.preformatInternal(source);
        if (context.isPrettyPrint() && context.getInitialIndent() > 0) {
            int indent = context.getInitialIndent();
            for (int j = 0; j < indent; j++) {
                ap.append(context.getIndentText());
            }
        }
        context.formatInternal(source2, fs);
        context.exit();
        fs.flush();
        return ap;
    }

    /* access modifiers changed from: protected */
    public Object preformat(JSONContext context, Object value) throws Exception {
        return value;
    }

    public <T> T parse(CharSequence cs) throws JSONException {
        InputSource is;
        if (cs instanceof String) {
            is = new StringInputSource((String) cs);
        } else if (cs instanceof StringBuilder) {
            is = new StringBuilderInputSource((StringBuilder) cs);
        } else if (cs instanceof StringBuffer) {
            is = new StringBufferInputSource((StringBuffer) cs);
        } else {
            is = new CharSequenceInputSource(cs);
        }
        try {
            JSONReader jreader = new JSONReader(new JSONContext(), is, false, true);
            if (jreader.next() != null) {
                return (T) jreader.getValue();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T parse(CharSequence s, Class<? extends T> cls) throws JSONException {
        return parse(s, (Type) cls);
    }

    public <T> T parse(CharSequence cs, Type type) throws JSONException {
        InputSource is;
        if (cs instanceof String) {
            is = new StringInputSource((String) cs);
        } else if (cs instanceof StringBuilder) {
            is = new StringBuilderInputSource((StringBuilder) cs);
        } else if (cs instanceof StringBuffer) {
            is = new StringBufferInputSource((StringBuffer) cs);
        } else {
            is = new CharSequenceInputSource(cs);
        }
        try {
            JSONContext context = new JSONContext();
            JSONReader jreader = new JSONReader(context, is, false, true);
            return context.convertInternal(jreader.next() != null ? jreader.getValue() : null, type);
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T parse(InputStream in) throws IOException, JSONException {
        JSONReader jreader = new JSONReader(new JSONContext(), new ReaderInputSource(in), false, true);
        if (jreader.next() != null) {
            return (T) jreader.getValue();
        }
        return null;
    }

    public <T> T parse(InputStream in, Class<? extends T> cls) throws IOException, JSONException {
        return parse(in, (Type) cls);
    }

    public <T> T parse(InputStream in, Type type) throws IOException, JSONException {
        JSONContext context = new JSONContext();
        JSONReader jreader = new JSONReader(context, new ReaderInputSource(in), false, true);
        return context.convertInternal(jreader.next() != null ? jreader.getValue() : null, type);
    }

    public <T> T parse(Reader reader) throws IOException, JSONException {
        JSONReader jreader = new JSONReader(new JSONContext(), new ReaderInputSource(reader), false, true);
        if (jreader.next() != null) {
            return (T) jreader.getValue();
        }
        return null;
    }

    public <T> T parse(Reader reader, Class<? extends T> cls) throws IOException, JSONException {
        return parse(reader, (Type) cls);
    }

    public <T> T parse(Reader reader, Type type) throws IOException, JSONException {
        JSONContext context = new JSONContext();
        JSONReader jreader = new JSONReader(context, new ReaderInputSource(reader), false, true);
        return context.convertInternal(jreader.next() != null ? jreader.getValue() : null, type);
    }

    public JSONReader getReader(CharSequence cs) {
        return getReader(cs, true);
    }

    public JSONReader getReader(InputStream in) {
        return getReader(in, true);
    }

    public JSONReader getReader(Reader reader) {
        return getReader(reader, true);
    }

    public JSONReader getReader(CharSequence cs, boolean ignoreWhitespace) {
        InputSource in;
        if (cs instanceof String) {
            in = new StringInputSource((String) cs);
        } else if (cs instanceof StringBuilder) {
            in = new StringBuilderInputSource((StringBuilder) cs);
        } else if (cs instanceof StringBuffer) {
            in = new StringBufferInputSource((StringBuffer) cs);
        } else {
            in = new CharSequenceInputSource(cs);
        }
        return new JSONReader(new JSONContext(), in, true, ignoreWhitespace);
    }

    public JSONReader getReader(InputStream in, boolean ignoreWhitespace) {
        return new JSONReader(new JSONContext(), new ReaderInputSource(in), true, ignoreWhitespace);
    }

    public JSONReader getReader(Reader reader, boolean ignoreWhitespace) {
        return new JSONReader(new JSONContext(), new ReaderInputSource(reader), true, ignoreWhitespace);
    }

    /* access modifiers changed from: package-private */
    public String getMessage(String id, Object... args) {
        return MessageFormat.format(ResourceBundle.getBundle("net.arnx.jsonic.Messages", this.locale).getString(id), args);
    }

    public Object convert(Object value, Type type) throws JSONException {
        return new JSONContext().convertInternal(value, type);
    }

    /* access modifiers changed from: protected */
    public <T> T postparse(JSONContext context, Object value, Class<? extends T> cls, Type type) throws Exception {
        Converter c = null;
        Converter c2 = null;
        if (value != null) {
            JSONHint hint = context.getHint();
            if (hint != null) {
                if (hint.serialized() && hint != context.skipHint) {
                    c2 = FormatConverter.INSTANCE;
                } else if (Serializable.class.equals(hint.type())) {
                    c2 = SerializableConverter.INSTANCE;
                } else if (String.class.equals(hint.type())) {
                    c2 = StringSerializableConverter.INSTANCE;
                }
            }
        } else if (!cls.isPrimitive()) {
            c2 = NullConverter.INSTANCE;
        }
        if (c2 == null) {
            if (value == null || !cls.equals(type) || !cls.isAssignableFrom(value.getClass())) {
                c2 = CONVERT_MAP.get(cls);
            } else {
                c2 = PlainConverter.INSTANCE;
            }
        }
        if (c2 == null && context.memberCache != null) {
            c2 = (Converter) context.memberCache.get(cls);
        }
        if (c == null) {
            if (Properties.class.isAssignableFrom(cls)) {
                c = PropertiesConverter.INSTANCE;
            } else if (Map.class.isAssignableFrom(cls)) {
                c = MapConverter.INSTANCE;
            } else if (Collection.class.isAssignableFrom(cls)) {
                c = CollectionConverter.INSTANCE;
            } else if (cls.isArray()) {
                c = ArrayConverter.INSTANCE;
            } else if (cls.isEnum()) {
                c = EnumConverter.INSTANCE;
            } else if (Date.class.isAssignableFrom(cls)) {
                c = DateConverter.INSTANCE;
            } else if (Calendar.class.isAssignableFrom(cls)) {
                c = CalendarConverter.INSTANCE;
            } else if (CharSequence.class.isAssignableFrom(cls)) {
                c = CharSequenceConverter.INSTANCE;
            } else if (Appendable.class.isAssignableFrom(cls)) {
                c = AppendableConverter.INSTANCE;
            } else if (cls.equals(ClassUtil.findClass("java.net.InetAddress"))) {
                c = InetAddressConverter.INSTANCE;
            } else if (Array.class.isAssignableFrom(cls) || Struct.class.isAssignableFrom(cls)) {
                c = NullConverter.INSTANCE;
            } else {
                c = new ObjectConverter(cls);
            }
            if (context.memberCache == null) {
                context.memberCache = new HashMap();
            }
            context.memberCache.put(cls, c);
        }
        if (c != null) {
            return (T) c.convert(context, value, cls, type);
        }
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public String normalize(String name) {
        return name;
    }

    /* access modifiers changed from: protected */
    public boolean ignore(JSONContext context, Class<?> cls, Member member) {
        if (!Modifier.isTransient(member.getModifiers()) && !member.getDeclaringClass().equals(Object.class)) {
            return false;
        }
        return true;
    }

    protected <T> T create(JSONContext context, Class<? extends T> c) throws Exception {
        Object instance = null;
        JSONHint hint = context.getHint();
        Class<? extends T> cls = c;
        if (hint != null) {
            cls = c;
            if (hint.type() != Object.class) {
                cls = hint.type().asSubclass(c);
            }
        }
        if (cls.isInterface()) {
            if (SortedMap.class.equals(cls)) {
                instance = new TreeMap();
            } else if (Map.class.equals(cls)) {
                instance = new LinkedHashMap();
            } else if (SortedSet.class.equals(cls)) {
                instance = new TreeSet();
            } else if (Set.class.equals(cls)) {
                instance = new LinkedHashSet();
            } else if (List.class.equals(cls)) {
                instance = new ArrayList();
            } else if (Collection.class.equals(cls)) {
                instance = new ArrayList();
            } else if (Appendable.class.equals(cls)) {
                instance = new StringBuilder();
            }
        } else if (Modifier.isAbstract(cls.getModifiers())) {
            if (Calendar.class.equals(cls)) {
                instance = Calendar.getInstance();
            }
        } else if ((cls.isMemberClass() || cls.isAnonymousClass()) && !Modifier.isStatic(cls.getModifiers())) {
            Class<?> eClass = cls.getEnclosingClass();
            Constructor<? extends T> declaredConstructor = cls.getDeclaredConstructor(new Class[]{eClass});
            declaredConstructor.setAccessible(true);
            if (context.contextObject == null || !eClass.isAssignableFrom(context.contextObject.getClass())) {
                instance = declaredConstructor.newInstance(new Object[]{null});
            } else {
                instance = declaredConstructor.newInstance(new Object[]{context.contextObject});
            }
        } else {
            if (Date.class.isAssignableFrom(cls)) {
                try {
                    Constructor<? extends T> declaredConstructor2 = cls.getDeclaredConstructor(new Class[]{Long.TYPE});
                    declaredConstructor2.setAccessible(true);
                    instance = declaredConstructor2.newInstance(new Object[]{0L});
                } catch (NoSuchMethodException e) {
                }
            }
            if (instance == null) {
                Constructor<? extends T> declaredConstructor3 = cls.getDeclaredConstructor(new Class[0]);
                declaredConstructor3.setAccessible(true);
                instance = declaredConstructor3.newInstance(new Object[0]);
            }
        }
        return cls.cast(instance);
    }

    /* access modifiers changed from: private */
    public static boolean isAssignableFrom(Class<?> target, Class<?> cls) {
        return target != null && target.isAssignableFrom(cls);
    }

    public final class JSONContext {
        private final LocalCache cache;
        /* access modifiers changed from: private */
        public final Object contextObject;
        private final String dateFormat;
        private int depth = -1;
        private final NamingStyle enumStyle;
        private final String indentText;
        private final int initialIndent;
        private final Locale locale;
        private final int maxDepth;
        /* access modifiers changed from: private */
        public Map<Class<?>, Object> memberCache;
        private final Mode mode;
        private final String numberFormat;
        private Object[] path;
        private final boolean prettyPrint;
        private final NamingStyle propertyStyle;
        JSONHint skipHint;
        private final boolean suppressNull;
        private final TimeZone timeZone;

        public JSONContext() {
            synchronized (JSON.this) {
                this.locale = JSON.this.locale;
                this.timeZone = JSON.this.timeZone;
                this.contextObject = JSON.this.contextObject;
                this.maxDepth = JSON.this.maxDepth;
                this.prettyPrint = JSON.this.prettyPrint;
                this.initialIndent = JSON.this.initialIndent;
                this.indentText = JSON.this.indentText;
                this.suppressNull = JSON.this.suppressNull;
                this.mode = JSON.this.mode;
                this.numberFormat = JSON.this.numberFormat;
                this.dateFormat = JSON.this.dateFormat;
                this.propertyStyle = JSON.this.propertyStyle;
                this.enumStyle = JSON.this.enumStyle;
                this.cache = new LocalCache("net.arnx.jsonic.Messages", this.locale, this.timeZone);
            }
        }

        private JSONContext(JSONContext context) {
            synchronized (context) {
                this.locale = context.locale;
                this.timeZone = context.timeZone;
                this.contextObject = context.contextObject;
                this.maxDepth = context.maxDepth;
                this.prettyPrint = context.prettyPrint;
                this.initialIndent = context.initialIndent;
                this.indentText = context.indentText;
                this.suppressNull = context.suppressNull;
                this.mode = context.mode;
                this.numberFormat = context.numberFormat;
                this.dateFormat = context.dateFormat;
                this.propertyStyle = context.propertyStyle;
                this.enumStyle = context.enumStyle;
                this.depth = context.depth;
                this.path = (Object[]) context.path.clone();
                this.cache = context.cache;
            }
        }

        /* access modifiers changed from: package-private */
        public JSONContext copy() {
            return new JSONContext(this);
        }

        public Locale getLocale() {
            return this.locale;
        }

        public TimeZone getTimeZone() {
            return this.timeZone;
        }

        public int getMaxDepth() {
            return this.maxDepth;
        }

        public boolean isPrettyPrint() {
            return this.prettyPrint;
        }

        public int getInitialIndent() {
            return this.initialIndent;
        }

        public String getIndentText() {
            return this.indentText;
        }

        public boolean isSuppressNull() {
            return this.suppressNull;
        }

        public Mode getMode() {
            return this.mode;
        }

        public NamingStyle getPropertyStyle() {
            return this.propertyStyle;
        }

        public NamingStyle getEnumStyle() {
            return this.enumStyle;
        }

        public LocalCache getLocalCache() {
            return this.cache;
        }

        @Deprecated
        public int getLevel() {
            return getDepth();
        }

        public int getDepth() {
            return this.depth;
        }

        public Object getKey() {
            return this.path[this.depth * 2];
        }

        public Object getKey(int depth2) {
            if (depth2 < 0) {
                depth2 += getDepth();
            }
            return this.path[depth2 * 2];
        }

        public JSONHint getHint() {
            return (JSONHint) this.path[(this.depth * 2) + 1];
        }

        public <T> T convert(Object key, Object value, Class<? extends T> c) throws Exception {
            enter(key, getHint());
            T o = JSON.this.postparse(this, value, c, c);
            exit();
            boolean isPrimitive = c.isPrimitive();
            Class<?> c2 = c;
            if (isPrimitive) {
                c2 = PlainConverter.getDefaultValue(c).getClass();
            }
            return (T) c2.cast(o);
        }

        public Object convert(Object key, Object value, Type t) throws Exception {
            Class<?> c = ClassUtil.getRawType(t);
            enter(key, getHint());
            Object o = JSON.this.postparse(this, value, c, t);
            exit();
            if (c.isPrimitive()) {
                c = PlainConverter.getDefaultValue(c).getClass();
            }
            return c.cast(o);
        }

        /* access modifiers changed from: package-private */
        public void enter(Object key, JSONHint hint) {
            this.depth++;
            if (this.path == null) {
                this.path = new Object[8];
            }
            if (this.path.length < (this.depth * 2) + 2) {
                Object[] newPath = new Object[Math.max(this.path.length * 2, (this.depth * 2) + 2)];
                System.arraycopy(this.path, 0, newPath, 0, this.path.length);
                this.path = newPath;
            }
            this.path[this.depth * 2] = key;
            this.path[(this.depth * 2) + 1] = hint;
        }

        /* access modifiers changed from: package-private */
        public void enter(Object key) {
            enter(key, getHint());
        }

        /* access modifiers changed from: package-private */
        public void exit() {
            this.depth--;
        }

        /* access modifiers changed from: package-private */
        public NumberFormat getNumberFormat() {
            JSONHint hint = getHint();
            String format = (hint == null || hint.format().length() <= 0) ? this.numberFormat : hint.format();
            if (format != null) {
                return getLocalCache().getNumberFormat(format);
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public DateFormat getDateFormat() {
            JSONHint hint = getHint();
            String format = (hint == null || hint.format().length() <= 0) ? this.dateFormat : hint.format();
            if (format != null) {
                return getLocalCache().getDateFormat(format);
            }
            return null;
        }

        public String toString() {
            StringBuilderOutputSource sb = new StringBuilderOutputSource(new StringBuilder());
            for (int i = 0; i < this.path.length; i += 2) {
                Object key = this.path[i];
                if (key == null) {
                    sb.append("[null]");
                } else if (key instanceof Number) {
                    sb.append('[');
                    sb.append(key.toString());
                    sb.append(']');
                } else if (key instanceof Character) {
                    sb.append(key.toString());
                } else {
                    String str = key.toString();
                    boolean escape = false;
                    for (int j = 0; j < str.length(); j++) {
                        if (j == 0) {
                            escape = !Character.isJavaIdentifierStart(str.charAt(j));
                        } else {
                            escape = !Character.isJavaIdentifierPart(str.charAt(j));
                        }
                        if (escape) {
                            break;
                        }
                    }
                    if (escape) {
                        sb.append('[');
                        try {
                            StringFormatter.serialize(this, str, sb);
                        } catch (Exception e) {
                        }
                        sb.append(']');
                    } else {
                        sb.append('.');
                        sb.append(str);
                    }
                }
            }
            return sb.toString();
        }

        /* access modifiers changed from: package-private */
        public final Object preformatInternal(Object value) {
            if (value == null) {
                return null;
            }
            if (getDepth() > getMaxDepth()) {
                return null;
            }
            if (JSON.this.getClass() == JSON.class) {
                return value;
            }
            try {
                return JSON.this.preformat(this, value);
            } catch (Exception e) {
                throw new JSONException(JSON.this.getMessage("json.format.ConversionError", value, this), JSONException.PREFORMAT_ERROR, e);
            }
        }

        /* access modifiers changed from: package-private */
        public final Formatter formatInternal(Object src, OutputSource ap) throws IOException {
            Object o = src;
            Formatter f = null;
            if (o == null) {
                f = NullFormatter.INSTANCE;
            } else {
                JSONHint hint = getHint();
                if (hint != null) {
                    if (hint.serialized() && hint != this.skipHint) {
                        f = PlainFormatter.INSTANCE;
                    } else if (String.class.equals(hint.type())) {
                        f = StringFormatter.INSTANCE;
                    } else if (Serializable.class.equals(hint.type())) {
                        f = SerializableFormatter.INSTANCE;
                    }
                }
            }
            if (f == null) {
                f = (Formatter) JSON.FORMAT_MAP.get(o.getClass());
            }
            if (f == null && this.memberCache != null) {
                f = (Formatter) this.memberCache.get(o.getClass());
            }
            if (f == null) {
                if (o.getClass().isEnum()) {
                    f = EnumFormatter.INSTANCE;
                } else if (o instanceof Map) {
                    f = MapFormatter.INSTANCE;
                } else if (o instanceof Iterable) {
                    if (!(o instanceof RandomAccess) || !(o instanceof List)) {
                        f = IterableFormatter.INSTANCE;
                    } else {
                        f = ListFormatter.INSTANCE;
                    }
                } else if (o.getClass().isArray()) {
                    f = ObjectArrayFormatter.INSTANCE;
                } else if (o instanceof CharSequence) {
                    f = StringFormatter.INSTANCE;
                } else if (o instanceof Date) {
                    f = DateFormatter.INSTANCE;
                } else if (o instanceof Calendar) {
                    f = CalendarFormatter.INSTANCE;
                } else if (o instanceof Number) {
                    f = NumberFormatter.INSTANCE;
                } else if (o instanceof Iterator) {
                    f = IteratorFormatter.INSTANCE;
                } else if (o instanceof Enumeration) {
                    f = EnumerationFormatter.INSTANCE;
                } else if ((o instanceof Type) || (o instanceof Member) || (o instanceof File)) {
                    f = StringFormatter.INSTANCE;
                } else if (o instanceof TimeZone) {
                    f = TimeZoneFormatter.INSTANCE;
                } else if (o instanceof Charset) {
                    f = CharsetFormatter.INSTANCE;
                } else if (o instanceof Array) {
                    f = SQLArrayFormatter.INSTANCE;
                } else if (o instanceof Struct) {
                    f = StructFormmatter.INSTANCE;
                } else if (o instanceof Node) {
                    if ((o instanceof CharacterData) && !(o instanceof Comment)) {
                        f = CharacterDataFormatter.INSTANCE;
                    } else if (o instanceof Document) {
                        f = DOMDocumentFormatter.INSTANCE;
                    } else if (o instanceof Element) {
                        f = DOMElementFormatter.INSTANCE;
                    }
                } else if (JSON.isAssignableFrom(ClassUtil.findClass("java.sql.RowId"), o.getClass())) {
                    f = SerializableFormatter.INSTANCE;
                } else if (JSON.isAssignableFrom(ClassUtil.findClass("java.net.InetAddress"), o.getClass())) {
                    f = InetAddressFormatter.INSTANCE;
                } else if (JSON.isAssignableFrom(ClassUtil.findClass("org.apache.commons.beanutils.DynaBean"), o.getClass())) {
                    f = DynaBeanFormatter.INSTANCE;
                } else {
                    f = new ObjectFormatter(o.getClass());
                }
                if (this.memberCache == null) {
                    this.memberCache = new HashMap();
                }
                this.memberCache.put(o.getClass(), f);
            }
            try {
                if (f.format(this, src, o, ap) || getDepth() != 0 || getMode() == Mode.SCRIPT) {
                    return f;
                }
                throw new JSONException(JSON.this.getMessage("json.format.IllegalRootTypeError", new Object[0]), 100);
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                JSON json = JSON.this;
                Object[] objArr = new Object[2];
                if (src instanceof CharSequence) {
                    src = "\"" + src + "\"";
                }
                objArr[0] = src;
                objArr[1] = this;
                throw new JSONException(json.getMessage("json.format.ConversionError", objArr), 100, e2);
            }
        }

        /* access modifiers changed from: package-private */
        public <T> T postparseInternal(Object value, Class<? extends T> cls, Type type) throws Exception {
            return JSON.this.postparse(this, value, cls, type);
        }

        /* access modifiers changed from: package-private */
        public <T> T convertInternal(Object value, Type type) throws JSONException {
            String text;
            if (type instanceof TypeReference) {
                type = ((TypeReference) type).getType();
            }
            Class<? extends T> cls = (Class<? extends T>) ClassUtil.getRawType(type);
            try {
                enter('$', (JSONHint) null);
                T result = JSON.this.postparse(this, value, cls, type);
                exit();
                return result;
            } catch (Exception e) {
                if (value instanceof CharSequence) {
                    text = "\"" + value + "\"";
                } else {
                    try {
                        text = value.toString();
                    } catch (Exception e2) {
                        text = value.getClass().toString();
                    }
                }
                throw new JSONException(JSON.this.getMessage("json.parse.ConversionError", text, type, this), JSONException.POSTPARSE_ERROR, e);
            }
        }

        /* access modifiers changed from: package-private */
        public <T> T createInternal(Class<? extends T> c) throws Exception {
            return JSON.this.create(this, c);
        }

        /* access modifiers changed from: package-private */
        public boolean ignoreInternal(Class<?> target, Member member) {
            return JSON.this.ignore(this, target, member);
        }

        /* access modifiers changed from: package-private */
        public String normalizeInternal(String name) {
            return JSON.this.normalize(name);
        }
    }
}
