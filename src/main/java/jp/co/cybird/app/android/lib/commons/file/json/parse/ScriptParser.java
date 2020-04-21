package jp.co.cybird.app.android.lib.commons.file.json.parse;

import java.io.IOException;
import jp.co.cybird.app.android.lib.commons.file.json.JSONEventType;
import jp.co.cybird.app.android.lib.commons.file.json.io.InputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.LocalCache;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;

public class ScriptParser extends JSONParser {
    private InputSource in;

    public ScriptParser(InputSource in2, int maxDepth, boolean interpretterMode, boolean ignoreWhirespace, LocalCache cache) {
        super(in2, maxDepth, interpretterMode, ignoreWhirespace, cache);
        this.in = in2;
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
            case 34:
            case 39:
                this.in.back();
                set(JSONEventType.STRING, parseString(this.in, true), true);
                return 1;
            case 45:
            case 48:
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
                return 1;
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 0;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 0;
            case 91:
                push(JSONEventType.START_ARRAY);
                return 4;
            case 102:
                this.in.back();
                set(JSONEventType.BOOLEAN, parseLiteral(this.in, "false", Boolean.FALSE), true);
                return 1;
            case BaseInterface.RESULT_PRIVACY_OK:
                this.in.back();
                set(JSONEventType.NULL, parseLiteral(this.in, "null", (Object) null), true);
                return 1;
            case 116:
                this.in.back();
                set(JSONEventType.BOOLEAN, parseLiteral(this.in, "true", Boolean.TRUE), true);
                return 1;
            case 123:
                push(JSONEventType.START_OBJECT);
                return 2;
            default:
                throw createParseException(this.in, "json.parse.UnexpectedChar", (char) n);
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
            case 34:
            case 39:
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 91:
            case 102:
            case BaseInterface.RESULT_PRIVACY_OK:
            case 116:
            case 123:
                if (isInterpretterMode()) {
                    this.in.back();
                    return 0;
                }
                break;
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 1;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 1;
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
                if (isIgnoreWhitespace()) {
                    return 2;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 2;
            case 34:
            case 39:
                this.in.back();
                set(JSONEventType.NAME, parseString(this.in, true), false);
                return 3;
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 2;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 2;
            case 48:
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
                Object num = parseNumber(this.in);
                set(JSONEventType.NAME, num != null ? num.toString() : null, false);
                return 3;
            case 125:
                if (isFirst()) {
                    pop();
                    if (getBeginType() == null) {
                        return 1;
                    }
                    return 5;
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            default:
                this.in.back();
                set(JSONEventType.NAME, parseLiteral(this.in, false), false);
                return 3;
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
                if (isIgnoreWhitespace()) {
                    return 3;
                }
                set(JSONEventType.WHITESPACE, ws, false);
                return 3;
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 3;
                }
                set(JSONEventType.COMMENT, comment, false);
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
            case 39:
                this.in.back();
                set(JSONEventType.STRING, parseString(this.in, true), true);
                return 5;
            case 45:
            case 48:
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
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 4;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 4;
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
            case 47:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 5;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 5;
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
}
