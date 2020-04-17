package jp.co.cybird.app.android.lib.commons.file.json.util;

public class Base64 {
    private static final String BASE64_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }
        char[] buffer = new char[((data.length % 3 == 0 ? 0 : 4) + ((data.length / 3) * 4))];
        int buf = 0;
        for (int i = 0; i < data.length; i++) {
            switch (i % 3) {
                case 0:
                    buffer[(i / 3) * 4] = BASE64_MAP.charAt((data[i] & 252) >> 2);
                    buf = (data[i] & 3) << 4;
                    if (i + 1 != data.length) {
                        break;
                    } else {
                        buffer[((i / 3) * 4) + 1] = BASE64_MAP.charAt(buf);
                        buffer[((i / 3) * 4) + 2] = '=';
                        buffer[((i / 3) * 4) + 3] = '=';
                        break;
                    }
                case 1:
                    buffer[((i / 3) * 4) + 1] = BASE64_MAP.charAt(buf + ((data[i] & 240) >> 4));
                    buf = (data[i] & 15) << 2;
                    if (i + 1 != data.length) {
                        break;
                    } else {
                        buffer[((i / 3) * 4) + 2] = BASE64_MAP.charAt(buf);
                        buffer[((i / 3) * 4) + 3] = '=';
                        break;
                    }
                case 2:
                    buf += (data[i] & 192) >> 6;
                    buffer[((i / 3) * 4) + 2] = BASE64_MAP.charAt(buf);
                    buffer[((i / 3) * 4) + 3] = BASE64_MAP.charAt(data[i] & 63);
                    break;
            }
        }
        return String.valueOf(buffer);
    }

    public static byte[] decode(CharSequence cs) {
        int data;
        int addsize = 0;
        int bufsize = 0;
        int i = 0;
        while (true) {
            if (i >= cs.length()) {
                break;
            }
            char c = cs.charAt(i);
            if ((c >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || ((c >= '0' && c <= '9') || c == '+' || c == '/'))) {
                bufsize++;
            } else if (c == '=') {
                if (i + 1 < cs.length() && cs.charAt(i + 1) == '=' && (bufsize + 2) % 4 == 0) {
                    bufsize += 2;
                    addsize = -2;
                } else if ((bufsize + 1) % 4 != 0) {
                    return null;
                } else {
                    bufsize++;
                    addsize = -1;
                }
            }
            i++;
        }
        byte[] buffer = new byte[(((bufsize / 4) * 3) + addsize)];
        int pos = 0;
        for (int i2 = 0; i2 < cs.length(); i2++) {
            char c2 = cs.charAt(i2);
            if (c2 >= 'A' && c2 <= 'Z') {
                data = c2 - 'A';
            } else if (c2 >= 'a' && c2 <= 'z') {
                data = (c2 - 'a') + 26;
            } else if (c2 >= '0' && c2 <= '9') {
                data = (c2 - '0') + 52;
            } else if (c2 == '+') {
                data = 62;
            } else if (c2 == '/') {
                data = 63;
            } else if (c2 == '=') {
                return buffer;
            }
            switch (pos % 4) {
                case 0:
                    buffer[(pos / 4) * 3] = (byte) (data << 2);
                    break;
                case 1:
                    int i3 = (pos / 4) * 3;
                    buffer[i3] = (byte) (buffer[i3] + ((byte) (data >> 4)));
                    if (((pos / 4) * 3) + 1 < buffer.length) {
                        buffer[((pos / 4) * 3) + 1] = (byte) ((data & 15) << 4);
                        break;
                    }
                    break;
                case 2:
                    int i4 = ((pos / 4) * 3) + 1;
                    buffer[i4] = (byte) (buffer[i4] + ((byte) (data >> 2)));
                    if (((pos / 4) * 3) + 2 < buffer.length) {
                        buffer[((pos / 4) * 3) + 2] = (byte) ((data & 3) << 6);
                        break;
                    }
                    break;
                case 3:
                    int i5 = ((pos / 4) * 3) + 2;
                    buffer[i5] = (byte) (buffer[i5] + ((byte) data));
                    break;
            }
            pos++;
        }
        return buffer;
    }
}
