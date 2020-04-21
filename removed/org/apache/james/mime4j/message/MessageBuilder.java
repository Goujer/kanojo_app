package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.field.AbstractField;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.storage.StorageProvider;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.MimeUtil;

public class MessageBuilder implements ContentHandler {
    private final BodyFactory bodyFactory;
    private final Entity entity;
    private Stack<Object> stack = new Stack<>();

    public MessageBuilder(Entity entity2) {
        this.entity = entity2;
        this.bodyFactory = new BodyFactory();
    }

    public MessageBuilder(Entity entity2, StorageProvider storageProvider) {
        this.entity = entity2;
        this.bodyFactory = new BodyFactory(storageProvider);
    }

    private void expect(Class<?> c) {
        if (!c.isInstance(this.stack.peek())) {
            throw new IllegalStateException("Internal stack error: Expected '" + c.getName() + "' found '" + this.stack.peek().getClass().getName() + "'");
        }
    }

    public void startMessage() throws MimeException {
        if (this.stack.isEmpty()) {
            this.stack.push(this.entity);
            return;
        }
        expect(Entity.class);
        Message m = new Message();
        ((Entity) this.stack.peek()).setBody(m);
        this.stack.push(m);
    }

    public void endMessage() throws MimeException {
        expect(Message.class);
        this.stack.pop();
    }

    public void startHeader() throws MimeException {
        this.stack.push(new Header());
    }

    public void field(Field field) throws MimeException {
        expect(Header.class);
        ((Header) this.stack.peek()).addField(AbstractField.parse(field.getRaw()));
    }

    public void endHeader() throws MimeException {
        expect(Header.class);
        expect(Entity.class);
        ((Entity) this.stack.peek()).setHeader((Header) this.stack.pop());
    }

    public void startMultipart(BodyDescriptor bd) throws MimeException {
        expect(Entity.class);
        Multipart multiPart = new Multipart(bd.getSubType());
        ((Entity) this.stack.peek()).setBody(multiPart);
        this.stack.push(multiPart);
    }

    public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
        InputStream decodedStream;
        Body body;
        expect(Entity.class);
        String enc = bd.getTransferEncoding();
        if (MimeUtil.ENC_BASE64.equals(enc)) {
            decodedStream = new Base64InputStream(is);
        } else if (MimeUtil.ENC_QUOTED_PRINTABLE.equals(enc)) {
            decodedStream = new QuotedPrintableInputStream(is);
        } else {
            decodedStream = is;
        }
        if (bd.getMimeType().startsWith("text/")) {
            body = this.bodyFactory.textBody(decodedStream, bd.getCharset());
        } else {
            body = this.bodyFactory.binaryBody(decodedStream);
        }
        ((Entity) this.stack.peek()).setBody(body);
    }

    public void endMultipart() throws MimeException {
        this.stack.pop();
    }

    public void startBodyPart() throws MimeException {
        expect(Multipart.class);
        BodyPart bodyPart = new BodyPart();
        ((Multipart) this.stack.peek()).addBodyPart(bodyPart);
        this.stack.push(bodyPart);
    }

    public void endBodyPart() throws MimeException {
        expect(BodyPart.class);
        this.stack.pop();
    }

    public void epilogue(InputStream is) throws MimeException, IOException {
        expect(Multipart.class);
        ((Multipart) this.stack.peek()).setEpilogueRaw(loadStream(is));
    }

    public void preamble(InputStream is) throws MimeException, IOException {
        expect(Multipart.class);
        ((Multipart) this.stack.peek()).setPreambleRaw(loadStream(is));
    }

    public void raw(InputStream is) throws MimeException, IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    private static ByteSequence loadStream(InputStream in) throws IOException {
        ByteArrayBuffer bab = new ByteArrayBuffer(64);
        while (true) {
            int b = in.read();
            if (b == -1) {
                return bab;
            }
            bab.append(b);
        }
    }
}
