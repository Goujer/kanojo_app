package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;

public class MimeStreamParser {
    private boolean contentDecoding;
    private ContentHandler handler;
    private final MimeTokenStream mimeTokenStream;

    public MimeStreamParser(MimeEntityConfig config) {
        MimeEntityConfig localConfig;
        this.handler = null;
        if (config != null) {
            localConfig = config.clone();
        } else {
            localConfig = new MimeEntityConfig();
        }
        this.mimeTokenStream = new MimeTokenStream(localConfig);
        this.contentDecoding = false;
    }

    public MimeStreamParser() {
        this((MimeEntityConfig) null);
    }

    public boolean isContentDecoding() {
        return this.contentDecoding;
    }

    public void setContentDecoding(boolean b) {
        this.contentDecoding = b;
    }

    public void parse(InputStream is) throws MimeException, IOException {
        InputStream bodyContent;
        this.mimeTokenStream.parse(is);
        while (true) {
            int state = this.mimeTokenStream.getState();
            switch (state) {
                case -1:
                    return;
                case 0:
                    this.handler.startMessage();
                    break;
                case 1:
                    this.handler.endMessage();
                    break;
                case 2:
                    this.handler.raw(this.mimeTokenStream.getInputStream());
                    break;
                case 3:
                    this.handler.startHeader();
                    break;
                case 4:
                    this.handler.field(this.mimeTokenStream.getField());
                    break;
                case 5:
                    this.handler.endHeader();
                    break;
                case 6:
                    this.handler.startMultipart(this.mimeTokenStream.getBodyDescriptor());
                    break;
                case 7:
                    this.handler.endMultipart();
                    break;
                case 8:
                    this.handler.preamble(this.mimeTokenStream.getInputStream());
                    break;
                case 9:
                    this.handler.epilogue(this.mimeTokenStream.getInputStream());
                    break;
                case 10:
                    this.handler.startBodyPart();
                    break;
                case 11:
                    this.handler.endBodyPart();
                    break;
                case 12:
                    BodyDescriptor desc = this.mimeTokenStream.getBodyDescriptor();
                    if (this.contentDecoding) {
                        bodyContent = this.mimeTokenStream.getDecodedInputStream();
                    } else {
                        bodyContent = this.mimeTokenStream.getInputStream();
                    }
                    this.handler.body(desc, bodyContent);
                    break;
                default:
                    throw new IllegalStateException("Invalid state: " + state);
            }
            this.mimeTokenStream.next();
        }
    }

    public boolean isRaw() {
        return this.mimeTokenStream.isRaw();
    }

    public void setRaw(boolean raw) {
        this.mimeTokenStream.setRecursionMode(2);
    }

    public void stop() {
        this.mimeTokenStream.stop();
    }

    public void setContentHandler(ContentHandler h) {
        this.handler = h;
    }
}
