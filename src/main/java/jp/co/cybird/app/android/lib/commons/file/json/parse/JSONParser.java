package jp.co.cybird.app.android.lib.commons.file.json.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jp.co.cybird.app.android.lib.commons.file.json.JSONEventType;
import jp.co.cybird.app.android.lib.commons.file.json.JSONException;
import jp.co.cybird.app.android.lib.commons.file.json.io.InputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.LocalCache;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public class JSONParser {
    static final int AFTER_NAME = 3;
    static final int AFTER_ROOT = 1;
    static final int AFTER_VALUE = 5;
    static final int BEFORE_NAME = 2;
    static final int BEFORE_ROOT = 0;
    static final int BEFORE_VALUE = 4;
    private static final int[] ESCAPE_CHARS = new int[128];
    private boolean active;
    private LocalCache cache;
    private boolean first;
    private boolean ignoreWhirespace;
    private InputSource in;
    private boolean interpretterMode;
    private int maxDepth;
    private List<JSONEventType> stack = new ArrayList();
    private int state = 0;
    private JSONEventType type;
    private Object value;

    static {
        for (int i = 0; i < 32; i++) {
            ESCAPE_CHARS[i] = 3;
        }
        ESCAPE_CHARS[34] = 1;
        ESCAPE_CHARS[39] = 1;
        ESCAPE_CHARS[92] = 2;
        ESCAPE_CHARS[127] = 3;
    }

    public JSONParser(InputSource in2, int maxDepth2, boolean interpretterMode2, boolean ignoreWhirespace2, LocalCache cache2) {
        boolean z = false;
        this.in = in2;
        this.maxDepth = maxDepth2;
        this.interpretterMode = interpretterMode2;
        this.ignoreWhirespace = ignoreWhirespace2;
        this.cache = cache2;
        this.active = this.stack.size() < maxDepth2 ? true : z;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }

    public boolean isInterpretterMode() {
        return this.interpretterMode;
    }

    public boolean isIgnoreWhitespace() {
        return this.ignoreWhirespace;
    }

    public Object getValue() {
        return this.value;
    }

    public int getDepth() {
        if (this.type == JSONEventType.START_OBJECT || this.type == JSONEventType.START_ARRAY) {
            return this.stack.size();
        }
        return this.stack.size() + 1;
    }

    public JSONEventType next() throws IOException {
        JSONEventType type2 = null;
        do {
            set((JSONEventType) null, (Object) null, false);
            switch (this.state) {
                case 0:
                    this.state = beforeRoot();
                    break;
                case 1:
                    this.state = afterRoot();
                    break;
                case 2:
                    this.state = beforeName();
                    break;
                case 3:
                    this.state = afterName();
                    break;
                case 4:
                    this.state = beforeValue();
                    break;
                case 5:
                    this.state = afterValue();
                    break;
            }
            if (getDepth() <= getMaxDepth()) {
                type2 = getType();
            }
            if (this.state != -1) {
            }
            return type2;
        } while (type2 == null);
        return type2;
    }

    /* access modifiers changed from: package-private */
    public int beforeRoot() throws IOException {
        int n = this.in.next();
        if (n == 65279) {
            n = this.in.next();
        }
        switch (n) {
            case -1:
                if (isInterpretterMode()) {
                    return -1;
                }
                throw createParseException(this.in, "json.parse.EmptyInputError");
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (isIgnoreWhitespace()) {
                    return 0;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 0;
            case 91:
                push(JSONEventType.START_ARRAY);
                return 4;
            case 123:
                push(JSONEventType.START_OBJECT);
                return 2;
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }

    /* access modifiers changed from: package-private */
    public int afterRoot() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                return -1;
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (isIgnoreWhitespace()) {
                    return 1;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 1;
            case 91:
            case 123:
                if (isInterpretterMode()) {
                    this.in.back();
                    return 0;
                }
                break;
        }
        throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
    }

    /* access modifiers changed from: package-private */
    public int beforeName() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                throw createParseException(this.in, "json.parse.ObjectNotClosedError");
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (!isIgnoreWhitespace()) {
                    set(JSONEventType.WHITESPACE, ws, false);
                }
                return 2;
            case 34:
                this.in.back();
                set(JSONEventType.NAME, parseString(this.in, false), false);
                return 3;
            case 125:
                if (!isFirst() || getBeginType() != JSONEventType.START_OBJECT) {
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
                pop();
                if (getBeginType() != null) {
                    return 5;
                }
                return 1;
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }

    /* access modifiers changed from: package-private */
    public int afterName() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                throw createParseException(this.in, "json.parse.ObjectNotClosedError");
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (!isIgnoreWhitespace()) {
                    set(JSONEventType.WHITESPACE, ws, false);
                }
                return 3;
            case 58:
                return 4;
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }

    /* access modifiers changed from: package-private */
    public int beforeValue() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    throw createParseException(this.in, "json.parse.ObjectNotClosedError");
                } else if (getBeginType() == JSONEventType.START_ARRAY) {
                    throw createParseException(this.in, "json.parse.ArrayNotClosedError");
                } else {
                    throw new IllegalStateException();
                }
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (isIgnoreWhitespace()) {
                    return 4;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 4;
            case 34:
                this.in.back();
                set(JSONEventType.STRING, parseString(this.in, false), true);
                return 5;
            case 45:
            case DateTimeParserConstants.ANY /*48*/:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                this.in.back();
                set(JSONEventType.NUMBER, parseNumber(this.in), true);
                return 5;
            case 91:
                push(JSONEventType.START_ARRAY);
                return 4;
            case 93:
                if (!isFirst() || getBeginType() != JSONEventType.START_ARRAY) {
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
                pop();
                if (getBeginType() == null) {
                    return 1;
                }
                return 5;
            case 102:
                this.in.back();
                set(JSONEventType.BOOLEAN, parseLiteral(this.in, "false", Boolean.FALSE), true);
                return 5;
            case BaseInterface.RESULT_PRIVACY_OK:
                this.in.back();
                set(JSONEventType.NULL, parseLiteral(this.in, "null", (Object) null), true);
                return 5;
            case 116:
                this.in.back();
                set(JSONEventType.BOOLEAN, parseLiteral(this.in, "true", Boolean.TRUE), true);
                return 5;
            case 123:
                push(JSONEventType.START_OBJECT);
                return 2;
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }

    /* access modifiers changed from: package-private */
    public int afterValue() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    throw createParseException(this.in, "json.parse.ObjectNotClosedError");
                } else if (getBeginType() == JSONEventType.START_ARRAY) {
                    throw createParseException(this.in, "json.parse.ArrayNotClosedError");
                } else {
                    throw new IllegalStateException();
                }
            case 9:
            case 10:
            case 13:
            case 32:
                this.in.back();
                String ws = parseWhitespace(this.in);
                if (isIgnoreWhitespace()) {
                    return 5;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 5;
            case 44:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    return 2;
                }
                if (getBeginType() == JSONEventType.START_ARRAY) {
                    return 4;
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            case 93:
                if (getBeginType() == JSONEventType.START_ARRAY) {
                    pop();
                    if (getBeginType() == null) {
                        return 1;
                    }
                    return 5;
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            case 125:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    pop();
                    if (getBeginType() == null) {
                        return 1;
                    }
                    return 5;
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }

    /* access modifiers changed from: package-private */
    public void push(JSONEventType type2) {
        boolean z = true;
        this.type = type2;
        this.stack.add(type2);
        this.first = true;
        if (this.stack.size() >= this.maxDepth) {
            z = false;
        }
        this.active = z;
    }

    /* access modifiers changed from: package-private */
    public void set(JSONEventType type2, Object value2, boolean isValue) {
        this.type = type2;
        this.value = value2;
        if (isValue) {
            this.first = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void pop() {
        boolean z = false;
        JSONEventType beginType = this.stack.remove(this.stack.size() - 1);
        if (beginType == JSONEventType.START_OBJECT) {
            this.type = JSONEventType.END_OBJECT;
        } else if (beginType == JSONEventType.START_ARRAY) {
            this.type = JSONEventType.END_ARRAY;
        } else {
            throw new IllegalStateException();
        }
        this.first = false;
        if (this.stack.size() < this.maxDepth) {
            z = true;
        }
        this.active = z;
    }

    /* access modifiers changed from: package-private */
    public JSONEventType getBeginType() {
        if (!this.stack.isEmpty()) {
            return this.stack.get(this.stack.size() - 1);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public JSONEventType getType() {
        return this.type;
    }

    /* access modifiers changed from: package-private */
    public boolean isFirst() {
        return this.first;
    }

    /* access modifiers changed from: package-private */
    public Object parseString(InputSource in2, boolean any) throws IOException {
        StringBuilder sb;
        int n;
        if (this.active) {
            sb = this.cache.getCachedBuffer();
        } else {
            sb = null;
        }
        int start = in2.next();
        int rest = in2.mark();
        int len = 0;
        while (true) {
            n = in2.next();
            if (n == -1) {
                break;
            }
            rest--;
            len++;
            if (n < ESCAPE_CHARS.length) {
                int type2 = ESCAPE_CHARS[n];
                if (type2 == 0) {
                    if (rest == 0 && sb != null) {
                        in2.copy(sb, len);
                    }
                } else if (type2 == 1) {
                    if (n == start) {
                        if (len > 1 && sb != null) {
                            in2.copy(sb, len - 1);
                        }
                    } else if (rest == 0 && sb != null) {
                        in2.copy(sb, len);
                    }
                } else if (type2 == 2) {
                    if (len > 0 && sb != null) {
                        in2.copy(sb, len - 1);
                    }
                    rest = 0;
                    in2.back();
                    char c = parseEscape(in2);
                    if (sb != null) {
                        sb.append(c);
                    }
                } else if (!any) {
                    throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                } else if (rest == 0 && sb != null) {
                    in2.copy(sb, len);
                }
            } else if (rest == 0 && sb != null) {
                in2.copy(sb, len);
            }
            if (rest == 0) {
                rest = in2.mark();
                len = 0;
            }
        }
        if (n != start) {
            throw createParseException(in2, "json.parse.StringNotClosedError");
        } else if (sb != null) {
            return this.cache.getString(sb);
        } else {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public char parseEscape(InputSource in2) throws IOException {
        int hex;
        int point = 1;
        char escape = 0;
        int next = in2.next();
        while (true) {
            int n = in2.next();
            if (n == -1) {
                return escape;
            }
            char c = (char) n;
            if (point == 1) {
                switch (c) {
                    case 'b':
                        return 8;
                    case 'f':
                        return 12;
                    case BaseInterface.RESULT_PRIVACY_OK:
                        return 10;
                    case 'r':
                        return 13;
                    case 't':
                        return 9;
                    case 'u':
                        point = 2;
                        break;
                    default:
                        return c;
                }
            } else {
                if (c >= '0' && c <= '9') {
                    hex = c - '0';
                } else if (c < 'A' || c > 'F') {
                    hex = (c < 'a' || c > 'f') ? -1 : (c - 'a') + 10;
                } else {
                    hex = (c - 'A') + 10;
                }
                if (hex != -1) {
                    escape = (char) ((hex << ((5 - point) * 4)) | escape);
                    if (point == 5) {
                        return escape;
                    }
                    point++;
                } else {
                    throw createParseException(in2, "json.parse.IllegalUnicodeEscape", Character.valueOf(c));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
        if (r4 != 0) goto L_0x0016;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0058, code lost:
        r4 = r13.mark();
        r1 = 0;
     */
    public Object parseNumber(InputSource in2) throws IOException {
        int point = 0;
        StringBuilder sb = this.active ? this.cache.getCachedBuffer() : null;
        int rest = in2.mark();
        int len = 0;
        while (true) {
            int n = in2.next();
            if (n != -1) {
                rest--;
                len++;
                char c = (char) n;
                switch (c) {
                    case '+':
                        if (point == 7) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 8;
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                    case '-':
                        if (point == 0) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 1;
                            break;
                        } else if (point == 7) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 8;
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                    case DateTimeParserConstants.DIGITS /*46*/:
                        if (point == 2 || point == 3) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 4;
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                    case DateTimeParserConstants.ANY /*48*/:
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        if (point == 0 || point == 1) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            if (c != '0') {
                                point = 2;
                                break;
                            } else {
                                point = 3;
                                break;
                            }
                        } else if (point == 2 || point == 5 || point == 9) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                                break;
                            }
                        } else if (point == 4) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 5;
                            break;
                        } else if (point == 7 || point == 8) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 9;
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                        break;
                    case 'E':
                    case 'e':
                        if (point == 2 || point == 3 || point == 5 || point == 6) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 7;
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                    default:
                        if (point == 2 || point == 3 || point == 5 || point == 6 || point == 9) {
                            if (len > 1 && sb != null) {
                                in2.copy(sb, len - 1);
                            }
                            in2.back();
                            break;
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf(c));
                        }
                }
            }
        }
        if (sb != null) {
            return this.cache.getBigDecimal(sb);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Object parseLiteral(InputSource in2, String expected, Object result) throws IOException {
        int pos = 0;
        while (true) {
            int n = in2.next();
            if (n != -1) {
                char c = (char) n;
                if (pos >= expected.length()) {
                    break;
                }
                int pos2 = pos + 1;
                if (c != expected.charAt(pos)) {
                    pos = pos2;
                    break;
                } else if (pos2 != expected.length()) {
                    pos = pos2;
                } else if (this.stack.size() < this.maxDepth) {
                    return result;
                } else {
                    return null;
                }
            } else {
                break;
            }
        }
        throw createParseException(in2, "json.parse.UnrecognizedLiteral", expected.substring(0, pos));
    }

    /* access modifiers changed from: package-private */
    public Object parseLiteral(InputSource in2, boolean asValue) throws IOException {
        StringBuilder sb;
        String str;
        int point = 0;
        if (this.active) {
            sb = this.cache.getCachedBuffer();
        } else {
            sb = null;
        }
        while (true) {
            int n = in2.next();
            if (n == -1) {
                break;
            }
            if (n == 92) {
                in2.back();
                n = parseEscape(in2);
            }
            if (point == 0) {
                if (Character.isJavaIdentifierStart(n)) {
                    if (!this.active && ((n == 110 || n == 116 || n == 102) && sb != null)) {
                        sb = this.cache.getCachedBuffer();
                    }
                    if (sb != null) {
                        sb.append((char) n);
                    }
                    point = 1;
                } else {
                    throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
            } else if (point != 1 || (!Character.isJavaIdentifierPart(n) && n != 46)) {
                in2.back();
            } else {
                if (!this.active && sb != null && sb.length() == 5) {
                    sb = null;
                }
                if (sb != null) {
                    sb.append((char) n);
                }
            }
        }
        in2.back();
        if (sb != null) {
            str = this.cache.getString(sb);
        } else {
            str = null;
        }
        if (asValue && str != null) {
            if ("null".equals(str)) {
                this.type = JSONEventType.NULL;
                return null;
            } else if ("true".equals(str)) {
                this.type = JSONEventType.BOOLEAN;
                if (this.active) {
                    return Boolean.TRUE;
                }
                return null;
            } else if ("false".equals(str)) {
                this.type = JSONEventType.BOOLEAN;
                if (this.active) {
                    return Boolean.FALSE;
                }
                return null;
            }
        }
        this.type = JSONEventType.STRING;
        if (!this.active) {
            str = null;
        }
        return str;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        if (r3 != 0) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        r3 = r12.mark();
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00d9, code lost:
        if (r0 <= 1) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00db, code lost:
        if (r4 == null) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00dd, code lost:
        r12.copy(r4, r0);
     */
    public String parseComment(InputSource in2) throws IOException {
        int point = 0;
        StringBuilder sb = !isIgnoreWhitespace() ? this.cache.getCachedBuffer() : null;
        int rest = in2.mark();
        int len = 0;
        while (true) {
            int n = in2.next();
            if (n != -1) {
                rest--;
                len++;
                switch (n) {
                    case 10:
                    case 13:
                        if (point == 2 || point == 3) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 2;
                            break;
                        } else if (point == 4) {
                            if (n != 13) {
                                break;
                            } else {
                                int n2 = in2.next();
                                in2.back();
                                if (n2 == 10) {
                                    if (rest == 0 && sb != null) {
                                        in2.copy(sb, len);
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                        }
                    case 42:
                        if (point == 1) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 2;
                            break;
                        } else if (point == 2) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 3;
                            break;
                        } else if (point == 3 || point == 4) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                                break;
                            }
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                        }
                    case DateTimeParserConstants.QUOTEDPAIR /*47*/:
                        if (point == 0) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 1;
                            break;
                        } else if (point == 1) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 4;
                            break;
                        } else if (point == 3) {
                            if (len > 1 && sb != null) {
                                in2.copy(sb, len);
                                break;
                            }
                        } else if (point == 2 || point == 4) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                                break;
                            }
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                        }
                    default:
                        if (point == 3) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                            }
                            point = 2;
                            break;
                        } else if (point == 2 || point == 4) {
                            if (rest == 0 && sb != null) {
                                in2.copy(sb, len);
                                break;
                            }
                        } else {
                            throw createParseException(in2, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                        }
                }
            }
        }
        if (sb != null) {
            return this.cache.getString(sb);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public String parseWhitespace(InputSource in2) throws IOException {
        StringBuilder sb;
        if (!isIgnoreWhitespace()) {
            sb = this.cache.getCachedBuffer();
        } else {
            sb = null;
        }
        int rest = in2.mark();
        int len = 0;
        while (true) {
            int n = in2.next();
            if (n == -1) {
                break;
            }
            rest--;
            len++;
            if (n == 32 || n == 9 || n == 13 || n == 10) {
                if (rest == 0 && sb != null) {
                    in2.copy(sb, len);
                }
                if (rest == 0) {
                    rest = in2.mark();
                    len = 0;
                }
            } else {
                if (len > 1 && sb != null) {
                    in2.copy(sb, len - 1);
                }
                in2.back();
            }
        }
        if (sb != null) {
            return this.cache.getString(sb);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public JSONException createParseException(InputSource in2, String id) {
        return createParseException(in2, id, (Object[]) null);
    }

    /* access modifiers changed from: package-private */
    public JSONException createParseException(InputSource in2, String id, Object... args) {
        return new JSONException(in2.getLineNumber() + ": " + this.cache.getMessage(id, args) + "\n" + in2.toString() + " <- ?", 200, in2.getLineNumber(), in2.getColumnNumber(), in2.getOffset());
    }
}
