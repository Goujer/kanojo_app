package jp.co.cybird.app.android.lib.commons.file.json.parse;

import java.io.IOException;
import jp.co.cybird.app.android.lib.commons.file.json.JSONEventType;
import jp.co.cybird.app.android.lib.commons.file.json.io.InputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.LocalCache;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;

public class TraditionalParser extends JSONParser {
    private boolean emptyRoot = false;
    private InputSource in;
    private long nameLineNumber = Long.MAX_VALUE;

    public TraditionalParser(InputSource in2, int maxDepth, boolean interpretterMode, boolean ignoreWhirespace, LocalCache cache) {
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
                break;
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
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
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
            case 123:
                push(JSONEventType.START_OBJECT);
                return 2;
        }
        if (n != -1) {
            this.in.back();
        }
        this.emptyRoot = true;
        push(JSONEventType.START_OBJECT);
        return 2;
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
            case 44:
                if (isInterpretterMode()) {
                    return 0;
                }
                break;
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 1;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 1;
            case 91:
            case 123:
                break;
        }
        if (isInterpretterMode()) {
            this.in.back();
            return 0;
        }
        throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
    }

    /* access modifiers changed from: package-private */
    public int beforeName() throws IOException {
        String str = null;
        int n = this.in.next();
        switch (n) {
            case -1:
                pop();
                if (getBeginType() == null && this.emptyRoot) {
                    return -1;
                }
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
            case 39:
                this.in.back();
                set(JSONEventType.NAME, parseString(this.in, true), false);
                return 3;
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
                Object num = parseNumber(this.in);
                JSONEventType jSONEventType = JSONEventType.NAME;
                if (num != null) {
                    str = num.toString();
                }
                set(jSONEventType, str, false);
                return 3;
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
                this.in.back();
                String comment = parseComment(this.in);
                if (!isIgnoreWhitespace()) {
                    set(JSONEventType.COMMENT, comment, false);
                }
                return 2;
            case 125:
                if (isFirst()) {
                    pop();
                    if (getBeginType() != null) {
                        this.nameLineNumber = this.in.getLineNumber();
                        return 5;
                    } else if (!this.emptyRoot) {
                        return 1;
                    } else {
                        throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                    }
                } else {
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
            default:
                this.in.back();
                Object literal = parseLiteral(this.in, true);
                JSONEventType jSONEventType2 = JSONEventType.NAME;
                if (literal != null) {
                    str = literal.toString();
                }
                set(jSONEventType2, str, false);
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
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
                this.in.back();
                String comment = parseComment(this.in);
                if (isIgnoreWhitespace()) {
                    return 3;
                }
                set(JSONEventType.COMMENT, comment, false);
                return 3;
            case 58:
                return 4;
            case 91:
            case 123:
                this.in.back();
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
                    this.in.back();
                    set(JSONEventType.NULL, (Object) null, true);
                    return 5;
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
                this.nameLineNumber = this.in.getLineNumber();
                return 5;
            case 44:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    set(JSONEventType.NULL, (Object) null, true);
                    return 2;
                } else if (getBeginType() == JSONEventType.START_ARRAY) {
                    set(JSONEventType.NULL, (Object) null, true);
                    return 4;
                } else {
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
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
                this.nameLineNumber = this.in.getLineNumber();
                return 5;
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
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
                if (getBeginType() != JSONEventType.START_ARRAY) {
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                } else if (isFirst()) {
                    pop();
                    if (getBeginType() == null) {
                        return 1;
                    }
                    this.nameLineNumber = this.in.getLineNumber();
                    return 5;
                } else {
                    set(JSONEventType.NULL, (Object) null, true);
                    this.in.back();
                    return 5;
                }
            case 123:
                push(JSONEventType.START_OBJECT);
                return 2;
            case 125:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    set(JSONEventType.NULL, (Object) null, true);
                    this.in.back();
                    return 5;
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            default:
                this.in.back();
                set(getType(), parseLiteral(this.in, true), true);
                this.nameLineNumber = this.in.getLineNumber();
                return 5;
        }
    }

    /* access modifiers changed from: package-private */
    public int afterValue() throws IOException {
        int n = this.in.next();
        switch (n) {
            case -1:
                if (getBeginType() == JSONEventType.START_OBJECT) {
                    pop();
                    if (getBeginType() == null && this.emptyRoot) {
                        return -1;
                    }
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
            case DateTimeParserConstants.QUOTEDPAIR /*47*/:
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
                    if (getBeginType() != null) {
                        return 5;
                    }
                    if (!this.emptyRoot) {
                        return 1;
                    }
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
            default:
                if (this.in.getLineNumber() > this.nameLineNumber) {
                    this.in.back();
                    this.nameLineNumber = Long.MAX_VALUE;
                    if (getBeginType() == JSONEventType.START_OBJECT) {
                        return 2;
                    }
                    if (getBeginType() == JSONEventType.START_ARRAY) {
                        return 4;
                    }
                    throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
                }
                throw createParseException(this.in, "json.parse.UnexpectedChar", Character.valueOf((char) n));
        }
    }
}
