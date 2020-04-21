package org.apache.james.mime4j.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ContentUtil {
    private ContentUtil() {
    }

    public static ByteSequence encode(String string) {
        return encode(CharsetUtil.US_ASCII, string);
    }

    public static ByteSequence encode(Charset charset, String string) {
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    public static String decode(ByteSequence byteSequence) {
        return decode(CharsetUtil.US_ASCII, byteSequence, 0, byteSequence.length());
    }

    public static String decode(Charset charset, ByteSequence byteSequence) {
        return decode(charset, byteSequence, 0, byteSequence.length());
    }

    public static String decode(ByteSequence byteSequence, int offset, int length) {
        return decode(CharsetUtil.US_ASCII, byteSequence, offset, length);
    }

    public static String decode(Charset charset, ByteSequence byteSequence, int offset, int length) {
        if (byteSequence instanceof ByteArrayBuffer) {
            return decode(charset, ((ByteArrayBuffer) byteSequence).buffer(), offset, length);
        }
        return decode(charset, byteSequence.toByteArray(), offset, length);
    }

    private static String decode(Charset charset, byte[] buffer, int offset, int length) {
        return charset.decode(ByteBuffer.wrap(buffer, offset, length)).toString();
    }
}
