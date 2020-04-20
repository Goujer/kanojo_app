package jp.co.cybird.app.android.lib.commons.file.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.InputSource;
import jp.co.cybird.app.android.lib.commons.file.json.parse.JSONParser;
import jp.co.cybird.app.android.lib.commons.file.json.parse.ScriptParser;
import jp.co.cybird.app.android.lib.commons.file.json.parse.TraditionalParser;

public class JSONReader {
    private static /* synthetic */ int[] $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSON$Mode;
    private static /* synthetic */ int[] $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSONEventType;
    private JSON.JSONContext context;
    private JSONParser parser;
    private JSONEventType type;

    static /* synthetic */ int[] $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSON$Mode() {
        int[] iArr = $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSON$Mode;
        if (iArr == null) {
            iArr = new int[JSON.Mode.values().length];
            try {
                iArr[JSON.Mode.SCRIPT.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[JSON.Mode.STRICT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[JSON.Mode.TRADITIONAL.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSON$Mode = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSONEventType() {
        int[] iArr = $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSONEventType;
        if (iArr == null) {
            iArr = new int[JSONEventType.values().length];
            try {
                iArr[JSONEventType.BOOLEAN.ordinal()] = 8;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[JSONEventType.COMMENT.ordinal()] = 11;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[JSONEventType.END_ARRAY.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[JSONEventType.END_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[JSONEventType.NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[JSONEventType.NULL.ordinal()] = 9;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[JSONEventType.NUMBER.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[JSONEventType.START_ARRAY.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[JSONEventType.START_OBJECT.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[JSONEventType.STRING.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[JSONEventType.WHITESPACE.ordinal()] = 10;
            } catch (NoSuchFieldError e11) {
            }
            $SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSONEventType = iArr;
        }
        return iArr;
    }

    JSONReader(JSON.JSONContext context2, InputSource in, boolean multilineMode, boolean ignoreWhitespace) {
        this.context = context2;
        switch ($SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSON$Mode()[context2.getMode().ordinal()]) {
            case 2:
                this.parser = new JSONParser(in, context2.getMaxDepth(), multilineMode, ignoreWhitespace, context2.getLocalCache());
                return;
            case 3:
                this.parser = new ScriptParser(in, context2.getMaxDepth(), multilineMode, ignoreWhitespace, context2.getLocalCache());
                return;
            default:
                this.parser = new TraditionalParser(in, context2.getMaxDepth(), multilineMode, ignoreWhitespace, context2.getLocalCache());
                return;
        }
    }

    public JSONEventType next() throws IOException {
        this.type = this.parser.next();
        return this.type;
    }

    public <T> T getValue(Class<T> cls) throws IOException {
        return this.context.convertInternal(getValue(), cls);
    }

    public Object getValue(Type t) throws IOException {
        return this.context.convertInternal(getValue(), t);
    }

    public Map<?, ?> getMap() throws IOException {
        return (Map) getValue();
    }

    public List<?> getList() throws IOException {
        return (List) getValue();
    }

    public String getString() throws IOException {
        return (String) this.parser.getValue();
    }

    public BigDecimal getNumber() throws IOException {
        return (BigDecimal) this.parser.getValue();
    }

    public Boolean getBoolean() throws IOException {
        return (Boolean) this.parser.getValue();
    }

    /* access modifiers changed from: package-private */
    public Object getValue() throws IOException {
        int i;
        JSONEventType next;
        if (this.type == null) {
            throw new IllegalStateException("you should call next.");
        }
        int ilen = 0;
        int[] istack = new int[8];
        int olen = 0;
        Object[] ostack = new Object[16];
        do {
            switch ($SWITCH_TABLE$jp$co$cybird$app$android$lib$commons$file$json$JSONEventType()[this.type.ordinal()]) {
                case 1:
                case 3:
                    istack = iexpand(istack, ilen + 1);
                    istack[ilen] = olen;
                    ilen++;
                    break;
                case 2:
                    ilen--;
                    int start = istack[ilen];
                    int len = olen - start;
                    if (len < 2) {
                        i = 4;
                    } else if (len < 4) {
                        i = 8;
                    } else if (len < 12) {
                        i = 16;
                    } else {
                        i = ((int) (((float) len) / 0.75f)) + 1;
                    }
                    Map<Object, Object> object = new LinkedHashMap<>(i);
                    for (int i2 = start; i2 < olen; i2 += 2) {
                        object.put(ostack[i2], ostack[i2 + 1]);
                    }
                    int olen2 = start;
                    ostack = oexpand(ostack, olen2 + 1);
                    ostack[olen2] = object;
                    olen = olen2 + 1;
                    break;
                case 4:
                    ilen--;
                    int start2 = istack[ilen];
                    List<Object> array = new ArrayList<>(olen - start2);
                    for (int i3 = start2; i3 < olen; i3++) {
                        array.add(ostack[i3]);
                    }
                    int olen3 = start2;
                    ostack = oexpand(ostack, olen3 + 1);
                    ostack[olen3] = array;
                    olen = olen3 + 1;
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    Object value = this.parser.getValue();
                    ostack = oexpand(ostack, olen + 1);
                    ostack[olen] = value;
                    olen++;
                    break;
            }
            if (!this.parser.isInterpretterMode() || ilen != 0) {
                next = this.parser.next();
                this.type = next;
            }
            return ostack[0];
        } while (next != null);
        //return ostack[0];
    }

    public int getDepth() {
        return this.parser.getDepth();
    }

    private int[] iexpand(int[] array, int min) {
        if (min <= array.length) {
            return array;
        }
        int[] narray = new int[(((array.length * 3) / 2) + 1)];
        System.arraycopy(array, 0, narray, 0, array.length);
        return narray;
    }

    private Object[] oexpand(Object[] array, int min) {
        if (min <= array.length) {
            return array;
        }
        Object[] narray = new Object[(((array.length * 3) / 2) + 1)];
        System.arraycopy(array, 0, narray, 0, array.length);
        return narray;
    }
}
