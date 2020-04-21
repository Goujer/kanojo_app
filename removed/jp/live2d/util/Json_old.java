package jp.live2d.util;

import java.util.ArrayList;
import java.util.HashMap;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;

public class Json_old {
    byte[] buf;
    int len;
    int line_count = 0;
    Object root;

    public Json_old(byte[] jsonBytes) {
        this.buf = jsonBytes;
        this.len = jsonBytes.length;
    }

    public Object parse() throws Exception {
        try {
            this.root = parseValue(this.buf, this.len, 0, new int[1]);
            return this.root;
        } catch (Throwable e) {
            throw new Exception("JSON error @line:" + this.line_count + " / " + e.getMessage(), e);
        }
    }

    public static Object parseFromBytes(byte[] jsonBytes) throws Exception {
        return new Json_old(jsonBytes).parse();
    }

    public static Object parseFromString(String jsonString) throws Exception {
        return new Json_old(jsonString.getBytes()).parse();
    }

    private static String parseString(byte[] str, int len2, int _pos, int[] ret_endpos) throws Exception {
        int i = _pos;
        StringBuffer sbuf = null;
        int buf_start = _pos;
        while (i < len2) {
            switch ((char) (str[i] & 255)) {
                case '\"':
                    ret_endpos[0] = i + 1;
                    if (sbuf == null) {
                        return new String(str, _pos, i - _pos);
                    }
                    if (i - 1 > buf_start) {
                        sbuf.append(new String(str, buf_start, (i - 1) - buf_start));
                    }
                    return sbuf.toString();
                case '\\':
                    if (sbuf == null) {
                        sbuf = new StringBuffer();
                    }
                    if (i > buf_start) {
                        sbuf.append(new String(str, buf_start, i - buf_start));
                    }
                    i++;
                    if (i < len2) {
                        switch ((char) (str[i] & 255)) {
                            case '\"':
                                sbuf.append('\"');
                                break;
                            case '/':
                                sbuf.append('/');
                                break;
                            case '\\':
                                sbuf.append('\\');
                                break;
                            case 'b':
                                sbuf.append(8);
                                break;
                            case 'f':
                                sbuf.append(12);
                                break;
                            case 'n':
                                sbuf.append(10);
                                break;
                            case 'r':
                                sbuf.append(13);
                                break;
                            case 't':
                                sbuf.append(9);
                                break;
                            case 'u':
                                throw new Exception("parse string/unicode escape not supported");
                        }
                        buf_start = i + 1;
                        break;
                    } else {
                        throw new Exception("parse string/escape error");
                    }
            }
            i++;
        }
        throw new Exception("parse string/illegal end");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
        return r5;
     */
    private Object parseObject(byte[] buf2, int len2, int _pos, int[] ret_endpos) throws Exception {
        HashMap<String, Object> ret = new HashMap<>();
        String key = null;
        int i = _pos;
        int[] local_ret_endpos2 = new int[1];
        boolean ok = false;
        while (i < len2) {
            while (true) {
                if (i < len2) {
                    switch ((char) (buf2[i] & 255)) {
                        case '\"':
                            key = parseString(buf2, len2, i + 1, local_ret_endpos2);
                            i = local_ret_endpos2[0];
                            ok = true;
                            break;
                        case ':':
                            throw new Exception("illegal ':' position");
                        case '}':
                            ret_endpos[0] = i + 1;
                            break;
                        default:
                            i++;
                    }
                }
            }
            if (!ok) {
                throw new Exception("key not found");
            }
            ok = false;
            while (true) {
                if (i < len2) {
                    switch ((char) (buf2[i] & 255)) {
                        case 10:
                            this.line_count++;
                            break;
                        case ':':
                            ok = true;
                            i++;
                            break;
                        case '}':
                            throw new Exception("illegal '}' position");
                    }
                }
                i++;
            }
            if (!ok) {
                throw new Exception("':' not found");
            }
            Object value = parseValue(buf2, len2, i, local_ret_endpos2);
            int i2 = local_ret_endpos2[0];
            ret.put(key, value);
            while (i2 < len2) {
                switch ((char) (buf2[i2] & 255)) {
                    case 10:
                        this.line_count++;
                        break;
                    case ',':
                        break;
                    case '}':
                        ret_endpos[0] = i2 + 1;
                        break;
                }
                i2++;
            }
            i = i2 + 1;
        }
        throw new Exception("illegal end of parseObject");
    }

    private Object parseArray(byte[] buf2, int len2, int _pos, int[] ret_endpos) throws Exception {
        ArrayList<Object> ret = new ArrayList<>();
        int i = _pos;
        int[] local_ret_endpos2 = new int[1];
        while (i < len2) {
            Object value = parseValue(buf2, len2, i, local_ret_endpos2);
            int i2 = local_ret_endpos2[0];
            ret.add(value);
            while (i2 < len2) {
                switch ((char) (buf2[i2] & 255)) {
                    case 10:
                        this.line_count++;
                        break;
                    case ',':
                        break;
                    case ']':
                        ret_endpos[0] = i2 + 1;
                        return ret;
                }
                i2++;
            }
            i = i2 + 1;
        }
        throw new Exception("illegal end of parseObject");
    }

    private Object parseValue(byte[] buf2, int len2, int _pos, int[] ret_endpos2) throws Exception {
        for (int i = _pos; i < len2; i++) {
            switch ((char) (buf2[i] & 255)) {
                case 10:
                    this.line_count++;
                    break;
                case '\"':
                    return parseString(buf2, len2, i + 1, ret_endpos2);
                case ',':
                    throw new Exception("illegal ',' position");
                case '-':
                case '.':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return new Double(UtString.strToDouble(buf2, len2, i, ret_endpos2));
                case '[':
                    return parseArray(buf2, len2, i + 1, ret_endpos2);
                case 'f':
                    if (i + 4 < len2) {
                        return Boolean.FALSE;
                    }
                    throw new Exception("parse false");
                case BaseInterface.RESULT_PRIVACY_OK:
                    if (i + 3 < len2) {
                        return null;
                    }
                    throw new Exception("parse null");
                case 't':
                    if (i + 3 < len2) {
                        return Boolean.TRUE;
                    }
                    throw new Exception("parse true");
                case '{':
                    return parseObject(buf2, len2, i + 1, ret_endpos2);
            }
        }
        throw new Exception("illegal end of value");
    }
}
