package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.james.mime4j.codec.CodecUtil;
import org.apache.james.mime4j.field.ContentTypeField;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;
import org.apache.james.mime4j.util.MimeUtil;

public class MessageWriter {
    private static final byte[] CRLF = {13, 10};
    private static final byte[] DASHES = {45, 45};
    public static final MessageWriter DEFAULT = new MessageWriter();

    protected MessageWriter() {
    }

    public void writeBody(Body body, OutputStream out) throws IOException {
        if (body instanceof Message) {
            writeEntity((Message) body, out);
        } else if (body instanceof Multipart) {
            writeMultipart((Multipart) body, out);
        } else if (body instanceof SingleBody) {
            ((SingleBody) body).writeTo(out);
        } else {
            throw new IllegalArgumentException("Unsupported body class");
        }
    }

    public void writeEntity(Entity entity, OutputStream out) throws IOException {
        Header header = entity.getHeader();
        if (header == null) {
            throw new IllegalArgumentException("Missing header");
        }
        writeHeader(header, out);
        Body body = entity.getBody();
        if (body == null) {
            throw new IllegalArgumentException("Missing body");
        }
        OutputStream encOut = encodeStream(out, entity.getContentTransferEncoding(), body instanceof BinaryBody);
        writeBody(body, encOut);
        if (encOut != out) {
            encOut.close();
        }
    }

    public void writeMultipart(Multipart multipart, OutputStream out) throws IOException {
        ByteSequence boundary = getBoundary(getContentType(multipart));
        writeBytes(multipart.getPreambleRaw(), out);
        out.write(CRLF);
        for (BodyPart bodyPart : multipart.getBodyParts()) {
            out.write(DASHES);
            writeBytes(boundary, out);
            out.write(CRLF);
            writeEntity(bodyPart, out);
            out.write(CRLF);
        }
        out.write(DASHES);
        writeBytes(boundary, out);
        out.write(DASHES);
        out.write(CRLF);
        writeBytes(multipart.getEpilogueRaw(), out);
    }

    public void writeHeader(Header header, OutputStream out) throws IOException {
        Iterator i$ = header.iterator();
        while (i$.hasNext()) {
            writeBytes(i$.next().getRaw(), out);
            out.write(CRLF);
        }
        out.write(CRLF);
    }

    /* access modifiers changed from: protected */
    public OutputStream encodeStream(OutputStream out, String encoding, boolean binaryBody) throws IOException {
        if (MimeUtil.isBase64Encoding(encoding)) {
            return CodecUtil.wrapBase64(out);
        }
        if (MimeUtil.isQuotedPrintableEncoded(encoding)) {
            return CodecUtil.wrapQuotedPrintable(out, binaryBody);
        }
        return out;
    }

    private ContentTypeField getContentType(Multipart multipart) {
        Entity parent = multipart.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Missing parent entity in multipart");
        }
        Header header = parent.getHeader();
        if (header == null) {
            throw new IllegalArgumentException("Missing header in parent entity");
        }
        ContentTypeField contentType = (ContentTypeField) header.getField("Content-Type");
        if (contentType != null) {
            return contentType;
        }
        throw new IllegalArgumentException("Content-Type field not specified");
    }

    private ByteSequence getBoundary(ContentTypeField contentType) {
        String boundary = contentType.getBoundary();
        if (boundary != null) {
            return ContentUtil.encode(boundary);
        }
        throw new IllegalArgumentException("Multipart boundary not specified");
    }

    private void writeBytes(ByteSequence byteSequence, OutputStream out) throws IOException {
        if (byteSequence instanceof ByteArrayBuffer) {
            ByteArrayBuffer bab = (ByteArrayBuffer) byteSequence;
            out.write(bab.buffer(), 0, bab.length());
            return;
        }
        out.write(byteSequence.toByteArray());
    }
}
