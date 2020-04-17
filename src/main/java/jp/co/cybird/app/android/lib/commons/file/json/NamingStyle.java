package jp.co.cybird.app.android.lib.commons.file.json;

public abstract class NamingStyle {
    private static final int LOWER = 2;
    public static final NamingStyle LOWER_CAMEL = new NamingStyle("LOWER_CAMEL") {
        public String to(String value) {
            return NamingStyle.toCamelCase(value, false);
        }
    };
    public static final NamingStyle LOWER_CASE = new NamingStyle("LOWER_CASE") {
        public String to(String value) {
            return NamingStyle.toSimpleCase(value, false);
        }
    };
    public static final NamingStyle LOWER_HYPHEN = new NamingStyle("LOWER_HYPHEN") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, false, '-');
        }
    };
    public static final NamingStyle LOWER_SPACE = new NamingStyle("LOWER_SPACE") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, false, ' ');
        }
    };
    public static final NamingStyle LOWER_UNDERSCORE = new NamingStyle("LOWER_UNDERSCORE") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, false, '_');
        }
    };
    private static final int[] MAP = new int[128];
    public static final NamingStyle NOOP = new NamingStyle("NOOP") {
        public String to(String value) {
            return value;
        }
    };
    private static final int NUMBER = 4;
    private static final int OTHER = 9;
    private static final int SEPARATOR = 1;
    private static final int UPPER = 3;
    public static final NamingStyle UPPER_CAMEL = new NamingStyle("UPPER_CAMEL") {
        public String to(String value) {
            return NamingStyle.toCamelCase(value, true);
        }
    };
    public static final NamingStyle UPPER_CASE = new NamingStyle("UPPER_CASE") {
        public String to(String value) {
            return NamingStyle.toSimpleCase(value, true);
        }
    };
    public static final NamingStyle UPPER_HYPHEN = new NamingStyle("UPPER_HYPHEN") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, true, '-');
        }
    };
    public static final NamingStyle UPPER_SPACE = new NamingStyle("UPPER_SPACE") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, true, ' ');
        }
    };
    public static final NamingStyle UPPER_UNDERSCORE = new NamingStyle("UPPER_UNDERSCORE") {
        public String to(String value) {
            return NamingStyle.toSeparatedCase(value, true, '_');
        }
    };
    private String name;

    public abstract String to(String str);

    static {
        for (int i = 0; i < MAP.length; i++) {
            if (i >= 65 && i <= 90) {
                MAP[i] = 3;
            } else if (i >= 97 && i <= 122) {
                MAP[i] = 2;
            } else if (i >= 48 && i <= 57) {
                MAP[i] = 4;
            } else if (i == 32 || i == 43 || i == 44 || i == 45 || i == 46 || i == 95) {
                MAP[i] = 1;
            } else {
                MAP[i] = 9;
            }
        }
    }

    public NamingStyle(String name2) {
        this.name = name2;
    }

    public String toString() {
        return this.name;
    }

    /* access modifiers changed from: private */
    public static String toSimpleCase(String value, boolean upper) {
        if (value == null || value.length() == 0) {
            return value;
        }
        char[] ca = value.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            int type = getType(ca[i]);
            if (upper && type == 2) {
                ca[i] = (char) (ca[i] - ' ');
            } else if (!upper && type == 3) {
                ca[i] = (char) (ca[i] + ' ');
            }
        }
        return String.valueOf(ca);
    }

    /* access modifiers changed from: private */
    public static String toCamelCase(String value, boolean upper) {
        if (value == null || value.length() == 0) {
            return value;
        }
        int start = 0;
        while (start < value.length() && ((type = getType(value.charAt(start))) == 1 || type == 9)) {
            start++;
        }
        int end = 0;
        while (end < value.length() - start && ((type = getType(value.charAt((value.length() - end) - 1))) == 1 || type == 9)) {
            end++;
        }
        int index = -1;
        int i = start;
        while (true) {
            if (i >= value.length() - end) {
                break;
            } else if (getType(value.charAt(i)) == 1) {
                index = i;
                break;
            } else {
                i++;
            }
        }
        if (index == -1) {
            int type = getType(value.charAt(start));
            if (type == 3) {
                char[] ca = value.toCharArray();
                if (!upper) {
                    ca[start] = (char) (ca[start] + ' ');
                }
                int i2 = start + 1;
                while (i2 < ca.length - end && getType(ca[i2]) == 3 && (i2 + 1 >= ca.length - end || (next = getType(ca[i2 + 1])) == 3 || next == 4)) {
                    ca[i2] = (char) (ca[i2] + ' ');
                    i2++;
                }
                return String.valueOf(ca);
            } else if (!upper || type != 2) {
                return value;
            } else {
                char[] ca2 = value.toCharArray();
                ca2[start] = (char) (ca2[start] - ' ');
                return String.valueOf(ca2);
            }
        } else {
            char[] ca3 = value.toCharArray();
            int pos = start;
            for (int i3 = start; i3 < ca3.length - end; i3++) {
                if (getType(ca3[i3]) == 1) {
                    upper = true;
                } else if (upper) {
                    int pos2 = pos + 1;
                    ca3[pos] = getType(ca3[i3]) == 2 ? (char) (ca3[i3] - ' ') : ca3[i3];
                    upper = false;
                    pos = pos2;
                } else {
                    int pos3 = pos + 1;
                    ca3[pos] = getType(ca3[i3]) == 3 ? (char) (ca3[i3] + ' ') : ca3[i3];
                    upper = false;
                    pos = pos3;
                }
            }
            int i4 = 0;
            int pos4 = pos;
            while (i4 < end) {
                ca3[pos4] = ca3[ca3.length - end];
                i4++;
                pos4++;
            }
            return String.valueOf(ca3, 0, pos4);
        }
    }

    /* access modifiers changed from: private */
    public static String toSeparatedCase(String value, boolean upper, char sep) {
        int next;
        if (value == null || value.length() == 0) {
            return value;
        }
        int start = 0;
        while (start < value.length() && ((type = getType(value.charAt(start))) == 1 || type == 9)) {
            start++;
        }
        int end = 0;
        while (end < value.length() - start && ((type = getType(value.charAt((value.length() - end) - 1))) == 1 || type == 9)) {
            end++;
        }
        StringBuilder sb = new StringBuilder((int) (((double) value.length()) * 1.5d));
        if (start > 0) {
            sb.append(value, 0, start);
        }
        int prev = -1;
        for (int i = start; i < value.length() - end; i++) {
            char c = value.charAt(i);
            int type = getType(c);
            if (type == 3 && prev != -1) {
                if (prev != 3 && prev != 1) {
                    sb.append(sep);
                } else if (!(i + 1 >= value.length() - end || (next = getType(value.charAt(i + 1))) == 3 || next == 4 || next == 1)) {
                    sb.append(sep);
                }
            }
            if (type == 1) {
                if (prev != 1) {
                    sb.append(sep);
                }
            } else if (upper && type == 2) {
                sb.append((char) (c - ' '));
            } else if (upper || type != 3) {
                sb.append(c);
            } else {
                sb.append((char) (c + ' '));
            }
            prev = type;
        }
        if (end > 0) {
            sb.append(value, value.length() - end, value.length());
        }
        return sb.toString();
    }

    private static int getType(char c) {
        if (c < MAP.length) {
            return MAP[c];
        }
        return 0;
    }
}
