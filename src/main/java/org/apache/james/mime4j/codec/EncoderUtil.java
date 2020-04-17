package org.apache.james.mime4j.codec;

import android.support.v4.view.MotionEventCompat;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Locale;
import org.apache.james.mime4j.util.CharsetUtil;

public class EncoderUtil {
    private static final BitSet ATEXT_CHARS = initChars("()<>@.,;:\\\"[]");
    private static final char BASE64_PAD = '=';
    private static final byte[] BASE64_TABLE = Base64OutputStream.BASE64_TABLE;
    private static final int ENCODED_WORD_MAX_LENGTH = 75;
    private static final String ENC_WORD_PREFIX = "=?";
    private static final String ENC_WORD_SUFFIX = "?=";
    private static final int MAX_USED_CHARACTERS = 50;
    private static final BitSet Q_REGULAR_CHARS = initChars("=_?");
    private static final BitSet Q_RESTRICTED_CHARS = initChars("=_?\"#$%&'(),.:;<>@[\\]^`{|}~");
    private static final BitSet TOKEN_CHARS = initChars("()<>@,;:\\\"/[]?=");

    public enum Encoding {
        B,
        Q
    }

    public enum Usage {
        TEXT_TOKEN,
        WORD_ENTITY
    }

    private static BitSet initChars(String specials) {
        BitSet bs = new BitSet(128);
        for (char ch = '!'; ch < 127; ch = (char) (ch + 1)) {
            if (specials.indexOf(ch) == -1) {
                bs.set(ch);
            }
        }
        return bs;
    }

    private EncoderUtil() {
    }

    public static String encodeAddressDisplayName(String displayName) {
        if (isAtomPhrase(displayName)) {
            return displayName;
        }
        if (hasToBeEncoded(displayName, 0)) {
            return encodeEncodedWord(displayName, Usage.WORD_ENTITY);
        }
        return quote(displayName);
    }

    public static String encodeAddressLocalPart(String localPart) {
        return isDotAtomText(localPart) ? localPart : quote(localPart);
    }

    public static String encodeHeaderParameter(String name, String value) {
        String name2 = name.toLowerCase(Locale.US);
        if (isToken(value)) {
            return name2 + "=" + value;
        }
        return name2 + "=" + quote(value);
    }

    public static String encodeIfNecessary(String text, Usage usage, int usedCharacters) {
        if (hasToBeEncoded(text, usedCharacters)) {
            return encodeEncodedWord(text, usage, usedCharacters);
        }
        return text;
    }

    public static boolean hasToBeEncoded(String text, int usedCharacters) {
        if (text == null) {
            throw new IllegalArgumentException();
        } else if (usedCharacters < 0 || usedCharacters > 50) {
            throw new IllegalArgumentException();
        } else {
            int nonWhiteSpaceCount = usedCharacters;
            for (int idx = 0; idx < text.length(); idx++) {
                char ch = text.charAt(idx);
                if (ch == 9 || ch == ' ') {
                    nonWhiteSpaceCount = 0;
                } else {
                    nonWhiteSpaceCount++;
                    if (nonWhiteSpaceCount > 77 || ch < ' ' || ch >= 127) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static String encodeEncodedWord(String text, Usage usage) {
        return encodeEncodedWord(text, usage, 0, (Charset) null, (Encoding) null);
    }

    public static String encodeEncodedWord(String text, Usage usage, int usedCharacters) {
        return encodeEncodedWord(text, usage, usedCharacters, (Charset) null, (Encoding) null);
    }

    public static String encodeEncodedWord(String text, Usage usage, int usedCharacters, Charset charset, Encoding encoding) {
        if (text == null) {
            throw new IllegalArgumentException();
        } else if (usedCharacters < 0 || usedCharacters > 50) {
            throw new IllegalArgumentException();
        } else {
            if (charset == null) {
                charset = determineCharset(text);
            }
            String mimeCharset = CharsetUtil.toMimeCharset(charset.name());
            if (mimeCharset == null) {
                throw new IllegalArgumentException("Unsupported charset");
            }
            byte[] bytes = encode(text, charset);
            if (encoding == null) {
                encoding = determineEncoding(bytes, usage);
            }
            if (encoding == Encoding.B) {
                return encodeB(ENC_WORD_PREFIX + mimeCharset + "?B?", text, usedCharacters, charset, bytes);
            }
            return encodeQ(ENC_WORD_PREFIX + mimeCharset + "?Q?", text, usage, usedCharacters, charset, bytes);
        }
    }

    public static String encodeB(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        int end = bytes.length;
        while (idx < end - 2) {
            int data = ((bytes[idx] & 255) << 16) | ((bytes[idx + 1] & 255) << 8) | (bytes[idx + 2] & MotionEventCompat.ACTION_MASK);
            sb.append((char) BASE64_TABLE[(data >> 18) & 63]);
            sb.append((char) BASE64_TABLE[(data >> 12) & 63]);
            sb.append((char) BASE64_TABLE[(data >> 6) & 63]);
            sb.append((char) BASE64_TABLE[data & 63]);
            idx += 3;
        }
        if (idx == end - 2) {
            int data2 = ((bytes[idx] & 255) << 16) | ((bytes[idx + 1] & 255) << 8);
            sb.append((char) BASE64_TABLE[(data2 >> 18) & 63]);
            sb.append((char) BASE64_TABLE[(data2 >> 12) & 63]);
            sb.append((char) BASE64_TABLE[(data2 >> 6) & 63]);
            sb.append(BASE64_PAD);
        } else if (idx == end - 1) {
            int data3 = (bytes[idx] & 255) << 16;
            sb.append((char) BASE64_TABLE[(data3 >> 18) & 63]);
            sb.append((char) BASE64_TABLE[(data3 >> 12) & 63]);
            sb.append(BASE64_PAD);
            sb.append(BASE64_PAD);
        }
        return sb.toString();
    }

    public static String encodeQ(byte[] bytes, Usage usage) {
        BitSet qChars = usage == Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int v = b & MotionEventCompat.ACTION_MASK;
            if (v == 32) {
                sb.append('_');
            } else if (!qChars.get(v)) {
                sb.append(BASE64_PAD);
                sb.append(hexDigit(v >>> 4));
                sb.append(hexDigit(v & 15));
            } else {
                sb.append((char) v);
            }
        }
        return sb.toString();
    }

    public static boolean isToken(String str) {
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int idx = 0; idx < length; idx++) {
            if (!TOKEN_CHARS.get(str.charAt(idx))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAtomPhrase(String str) {
        boolean containsAText = false;
        int length = str.length();
        for (int idx = 0; idx < length; idx++) {
            char ch = str.charAt(idx);
            if (ATEXT_CHARS.get(ch)) {
                containsAText = true;
            } else if (!CharsetUtil.isWhitespace(ch)) {
                return false;
            }
        }
        return containsAText;
    }

    private static boolean isDotAtomText(String str) {
        char prev = '.';
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int idx = 0; idx < length; idx++) {
            char ch = str.charAt(idx);
            if (ch == '.') {
                if (prev == '.' || idx == length - 1) {
                    return false;
                }
            } else if (!ATEXT_CHARS.get(ch)) {
                return false;
            }
            prev = ch;
        }
        return true;
    }

    private static String quote(String str) {
        return "\"" + str.replaceAll("[\\\\\"]", "\\\\$0") + "\"";
    }

    private static String encodeB(String prefix, String text, int usedCharacters, Charset charset, byte[] bytes) {
        if (prefix.length() + bEncodedLength(bytes) + ENC_WORD_SUFFIX.length() <= 75 - usedCharacters) {
            return prefix + encodeB(bytes) + ENC_WORD_SUFFIX;
        }
        String part1 = text.substring(0, text.length() / 2);
        String word1 = encodeB(prefix, part1, usedCharacters, charset, encode(part1, charset));
        String part2 = text.substring(text.length() / 2);
        return word1 + " " + encodeB(prefix, part2, 0, charset, encode(part2, charset));
    }

    private static int bEncodedLength(byte[] bytes) {
        return ((bytes.length + 2) / 3) * 4;
    }

    private static String encodeQ(String prefix, String text, Usage usage, int usedCharacters, Charset charset, byte[] bytes) {
        if (prefix.length() + qEncodedLength(bytes, usage) + ENC_WORD_SUFFIX.length() <= 75 - usedCharacters) {
            return prefix + encodeQ(bytes, usage) + ENC_WORD_SUFFIX;
        }
        String part1 = text.substring(0, text.length() / 2);
        String word1 = encodeQ(prefix, part1, usage, usedCharacters, charset, encode(part1, charset));
        String part2 = text.substring(text.length() / 2);
        return word1 + " " + encodeQ(prefix, part2, usage, 0, charset, encode(part2, charset));
    }

    private static int qEncodedLength(byte[] bytes, Usage usage) {
        BitSet qChars = usage == Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
        int count = 0;
        for (byte b : bytes) {
            int v = b & MotionEventCompat.ACTION_MASK;
            if (v == 32) {
                count++;
            } else if (!qChars.get(v)) {
                count += 3;
            } else {
                count++;
            }
        }
        return count;
    }

    private static byte[] encode(String text, Charset charset) {
        ByteBuffer buffer = charset.encode(text);
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

    private static Charset determineCharset(String text) {
        boolean ascii = true;
        int len = text.length();
        for (int index = 0; index < len; index++) {
            char ch = text.charAt(index);
            if (ch > 255) {
                return CharsetUtil.UTF_8;
            }
            if (ch > 127) {
                ascii = false;
            }
        }
        return ascii ? CharsetUtil.US_ASCII : CharsetUtil.ISO_8859_1;
    }

    private static Encoding determineEncoding(byte[] bytes, Usage usage) {
        if (bytes.length == 0) {
            return Encoding.Q;
        }
        BitSet qChars = usage == Usage.TEXT_TOKEN ? Q_REGULAR_CHARS : Q_RESTRICTED_CHARS;
        int qEncoded = 0;
        for (byte b : bytes) {
            int v = b & MotionEventCompat.ACTION_MASK;
            if (v != 32 && !qChars.get(v)) {
                qEncoded++;
            }
        }
        return (qEncoded * 100) / bytes.length > 30 ? Encoding.B : Encoding.Q;
    }

    private static char hexDigit(int i) {
        return i < 10 ? (char) (i + 48) : (char) ((i - 10) + 65);
    }
}
