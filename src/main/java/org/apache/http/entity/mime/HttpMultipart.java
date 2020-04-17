package org.apache.http.entity.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.CharsetUtil;

public class HttpMultipart {
    private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, CharsetUtil.CRLF);
    private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
    private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
    private final String boundary;
    private final Charset charset;
    private final HttpMultipartMode mode;
    private final List<FormBodyPart> parts;
    private final String subType;

    private static ByteArrayBuffer encode(Charset charset2, String string) {
        ByteBuffer encoded = charset2.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
    }

    private static void writeBytes(String s, Charset charset2, OutputStream out) throws IOException {
        writeBytes(encode(charset2, s), out);
    }

    private static void writeBytes(String s, OutputStream out) throws IOException {
        writeBytes(encode(MIME.DEFAULT_CHARSET, s), out);
    }

    private static void writeField(MinimalField field, OutputStream out) throws IOException {
        writeBytes(field.getName(), out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), out);
        writeBytes(CR_LF, out);
    }

    private static void writeField(MinimalField field, Charset charset2, OutputStream out) throws IOException {
        writeBytes(field.getName(), charset2, out);
        writeBytes(FIELD_SEP, out);
        writeBytes(field.getBody(), charset2, out);
        writeBytes(CR_LF, out);
    }

    public HttpMultipart(String subType2, Charset charset2, String boundary2, HttpMultipartMode mode2) {
        if (subType2 == null) {
            throw new IllegalArgumentException("Multipart subtype may not be null");
        } else if (boundary2 == null) {
            throw new IllegalArgumentException("Multipart boundary may not be null");
        } else {
            this.subType = subType2;
            this.charset = charset2 == null ? MIME.DEFAULT_CHARSET : charset2;
            this.boundary = boundary2;
            this.parts = new ArrayList();
            this.mode = mode2;
        }
    }

    public HttpMultipart(String subType2, Charset charset2, String boundary2) {
        this(subType2, charset2, boundary2, HttpMultipartMode.STRICT);
    }

    public HttpMultipart(String subType2, String boundary2) {
        this(subType2, (Charset) null, boundary2);
    }

    public String getSubType() {
        return this.subType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public HttpMultipartMode getMode() {
        return this.mode;
    }

    public List<FormBodyPart> getBodyParts() {
        return this.parts;
    }

    public void addBodyPart(FormBodyPart part) {
        if (part != null) {
            this.parts.add(part);
        }
    }

    public String getBoundary() {
        return this.boundary;
    }

    private void doWriteTo(HttpMultipartMode mode2, OutputStream out, boolean writeContent) throws IOException {
        ByteArrayBuffer boundary2 = encode(this.charset, getBoundary());
        for (FormBodyPart part : this.parts) {
            writeBytes(TWO_DASHES, out);
            writeBytes(boundary2, out);
            writeBytes(CR_LF, out);
            Header header = part.getHeader();
            switch (mode2) {
                case STRICT:
                    Iterator i$ = header.iterator();
                    while (i$.hasNext()) {
                        writeField(i$.next(), out);
                    }
                    break;
                case BROWSER_COMPATIBLE:
                    writeField(part.getHeader().getField("Content-Disposition"), this.charset, out);
                    if (part.getBody().getFilename() != null) {
                        writeField(part.getHeader().getField("Content-Type"), this.charset, out);
                        break;
                    }
                    break;
            }
            writeBytes(CR_LF, out);
            if (writeContent) {
                part.getBody().writeTo(out);
            }
            writeBytes(CR_LF, out);
        }
        writeBytes(TWO_DASHES, out);
        writeBytes(boundary2, out);
        writeBytes(TWO_DASHES, out);
        writeBytes(CR_LF, out);
    }

    public void writeTo(OutputStream out) throws IOException {
        doWriteTo(this.mode, out, true);
    }

    public long getTotalLength() {
        long contentLen = 0;
        for (FormBodyPart part : this.parts) {
            long len = part.getBody().getContentLength();
            if (len < 0) {
                return -1;
            }
            contentLen += len;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doWriteTo(this.mode, out, false);
            return ((long) out.toByteArray().length) + contentLen;
        } catch (IOException e) {
            return -1;
        }
    }
}
