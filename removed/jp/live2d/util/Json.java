package jp.live2d.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;

public class Json {
    byte[] buf;
    int len;
    int line_count = 0;
    Value root;

    public Json(byte[] jsonBytes) {
        this.buf = jsonBytes;
        this.len = jsonBytes.length;
    }

    public Value parse() throws Exception {
        try {
            this.root = parseValue(this.buf, this.len, 0, new int[1]);
            return this.root;
        } catch (Throwable e) {
            throw new Exception("JSON error @line:" + this.line_count + " / " + e.getMessage(), e);
        }
    }

    public static Value parseFromBytes(byte[] jsonBytes) throws Exception {
        return new Json(jsonBytes).parse();
    }

    public static Value parseFromString(String jsonString) throws Exception {
        return new Json(jsonString.getBytes()).parse();
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
                            case BaseInterface.RESULT_PRIVACY_OK:
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

    private Value parseObject(byte[] buf2, int len2, int _pos, int[] ret_endpos) throws Exception {
        HashMap<String, Value> ret = new HashMap<>();
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
                            return new Value(ret);
                        default:
                            i++;
                    }
                }
            }
            //Unreachable
//            if (!ok) {
//                throw new Exception("key not found");
//            }
//            ok = false;
//            while (true) {
//                if (i < len2) {
//                    switch ((char) (buf2[i] & 255)) {
//                        case 10:
//                            this.line_count++;
//                            break;
//                        case ':':
//                            ok = true;
//                            i++;
//                            break;
//                        case '}':
//                            throw new Exception("illegal '}' position");
//                    }
//                }
//                i++;
//            }
//            if (!ok) {
//                throw new Exception("':' not found");
//            }
//            Value value = parseValue(buf2, len2, i, local_ret_endpos2);
//            int i2 = local_ret_endpos2[0];
//            ret.put(key, value);
//            while (i2 < len2) {
//                switch ((char) (buf2[i2] & 255)) {
//                    case 10:
//                        this.line_count++;
//                        break;
//                    case ',':
//                        break;
//                    case '}':
//                        ret_endpos[0] = i2 + 1;
//                        return new Value(ret);
//                }
//                i2++;
//            }
//            i = i2 + 1;
//        }
//        throw new Exception("illegal end of parseObject");
    }

    private Value parseArray(byte[] buf2, int len2, int _pos, int[] ret_endpos) throws Exception {
        ArrayList<Value> ret = new ArrayList<>();
        int i = _pos;
        int[] local_ret_endpos2 = new int[1];
        while (i < len2) {
            Value value = parseValue(buf2, len2, i, local_ret_endpos2);
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
                        return new Value(ret);
                }
                i2++;
            }
            i = i2 + 1;
        }
        throw new Exception("illegal end of parseObject");
    }

    private Value parseValue(byte[] buf2, int len2, int _pos, int[] ret_endpos2) throws Exception {
        for (int i = _pos; i < len2; i++) {
            switch ((char) (buf2[i] & 255)) {
                case 10:
                    this.line_count++;
                    break;
                case '\"':
                    return new Value(parseString(buf2, len2, i + 1, ret_endpos2));
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
                    return new Value(new Double(UtString.strToDouble(buf2, len2, i, ret_endpos2)));
                case '[':
                    return parseArray(buf2, len2, i + 1, ret_endpos2);
                case 'f':
                    if (i + 4 < len2) {
                        return new Value(Boolean.FALSE);
                    }
                    throw new Exception("parse false");
                case BaseInterface.RESULT_PRIVACY_OK:
                    if (i + 3 < len2) {
                        return null;
                    }
                    throw new Exception("parse null");
                case 't':
                    if (i + 3 < len2) {
                        return new Value(Boolean.TRUE);
                    }
                    throw new Exception("parse true");
                case '{':
                    return parseObject(buf2, len2, i + 1, ret_endpos2);
            }
        }
        throw new Exception("illegal end of value");
    }

    public static final class Value {
        Object o;

        public Value(Object o2) {
            this.o = o2;
        }

        public String toString() {
            return toString("");
        }

        public String toString(String indent) {
            if (this.o instanceof String) {
                return (String) this.o;
            }
            if (this.o instanceof ArrayList) {
                String ret = String.valueOf(indent) + "[\n";
                Iterator it = ((ArrayList) this.o).iterator();
                while (it.hasNext()) {
                    ret = String.valueOf(ret) + indent + "\t" + ((Value) it.next()).toString(String.valueOf(indent) + "\t") + "\n";
                }
                return String.valueOf(ret) + indent + "]\n";
            } else if (!(this.o instanceof Map)) {
                return new StringBuilder().append(this.o).toString();
            } else {
                String ret2 = String.valueOf(indent) + "{\n";
                Map<String, Value> vmap = (Map) this.o;
                for (String key : vmap.keySet()) {
                    ret2 = String.valueOf(ret2) + indent + "\t" + key + " : " + vmap.get(key).toString(String.valueOf(indent) + "\t") + "\n";
                }
                return String.valueOf(ret2) + indent + "}\n";
            }
        }

        public int toInt() {
            return toInt(0);
        }

        public int toInt(int defaultV) {
            if (this.o instanceof Double) {
                return ((Double) this.o).intValue();
            }
            return defaultV;
        }

        public float toFloat() {
            return toFloat(0.0f);
        }

        public float toFloat(float defaultV) {
            if (this.o instanceof Double) {
                return ((Double) this.o).floatValue();
            }
            return defaultV;
        }

        public double toDouble() {
            return toDouble(0.0d);
        }

        public double toDouble(double defaultV) {
            if (this.o instanceof Double) {
                return ((Double) this.o).doubleValue();
            }
            return defaultV;
        }

        public ArrayList<Value> getVector(ArrayList<Value> defalutV) {
            if (this.o instanceof ArrayList) {
                return (ArrayList) this.o;
            }
            return defalutV;
        }

        public Value get(int index) {
            if (this.o instanceof ArrayList) {
                return (Value) ((ArrayList) this.o).get(index);
            }
            return null;
        }

        public Map<String, Value> getMap(Map<String, Value> defalutV) {
            if (this.o instanceof Map) {
                return (Map) this.o;
            }
            return defalutV;
        }

        public Value get(String key) {
            if (this.o instanceof Map) {
                return (Value) ((Map) this.o).get(key);
            }
            return null;
        }

        public Set<String> keySet() {
            if (this.o instanceof Map) {
                return ((Map) this.o).keySet();
            }
            return null;
        }

        public boolean isNull() {
            return this.o == null;
        }

        public boolean isboolean() {
            return this.o instanceof Boolean;
        }

        public boolean isDouble() {
            return this.o instanceof Double;
        }

        public boolean isString() {
            return this.o instanceof String;
        }

        public boolean isArray() {
            return this.o instanceof ArrayList;
        }

        public boolean isMap() {
            return this.o instanceof Map;
        }
    }
}
