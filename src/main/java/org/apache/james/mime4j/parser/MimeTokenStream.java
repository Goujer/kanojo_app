package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.io.BufferedLineReaderInputStream;
import org.apache.james.mime4j.io.LineNumberInputStream;
import org.apache.james.mime4j.io.LineNumberSource;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.util.MimeUtil;

public class MimeTokenStream implements EntityStates, RecursionMode {
    private final MimeEntityConfig config;
    private EntityStateMachine currentStateMachine;
    private final LinkedList<EntityStateMachine> entities;
    private BufferedLineReaderInputStream inbuffer;
    private int recursionMode;
    private int state;

    public static final MimeTokenStream createMaximalDescriptorStream() {
        MimeEntityConfig config2 = new MimeEntityConfig();
        config2.setMaximalBodyDescriptor(true);
        return new MimeTokenStream(config2);
    }

    public static final MimeTokenStream createStrictValidationStream() {
        MimeEntityConfig config2 = new MimeEntityConfig();
        config2.setStrictParsing(true);
        return new MimeTokenStream(config2);
    }

    public MimeTokenStream() {
        this(new MimeEntityConfig());
    }

    protected MimeTokenStream(MimeEntityConfig config2) {
        this.entities = new LinkedList<>();
        this.state = -1;
        this.recursionMode = 0;
        this.config = config2;
    }

    public void parse(InputStream stream) {
        doParse(stream, (String) null);
    }

    public void parseHeadless(InputStream stream, String contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("Content type may not be null");
        }
        doParse(stream, contentType);
    }

    private void doParse(InputStream stream, String contentType) {
        this.entities.clear();
        LineNumberSource lineSource = null;
        if (this.config.isCountLineNumbers()) {
            LineNumberSource lineInput = new LineNumberInputStream(stream);
            lineSource = lineInput;
            stream = lineInput;
        }
        this.inbuffer = new BufferedLineReaderInputStream(stream, 4096, this.config.getMaxLineLen());
        switch (this.recursionMode) {
            case 0:
            case 1:
            case 3:
                MimeEntity mimeentity = new MimeEntity(lineSource, this.inbuffer, (BodyDescriptor) null, 0, 1, this.config);
                mimeentity.setRecursionMode(this.recursionMode);
                if (contentType != null) {
                    mimeentity.skipHeader(contentType);
                }
                this.currentStateMachine = mimeentity;
                break;
            case 2:
                this.currentStateMachine = new RawEntity(this.inbuffer);
                break;
        }
        this.entities.add(this.currentStateMachine);
        this.state = this.currentStateMachine.getState();
    }

    public boolean isRaw() {
        return this.recursionMode == 2;
    }

    public int getRecursionMode() {
        return this.recursionMode;
    }

    public void setRecursionMode(int mode) {
        this.recursionMode = mode;
        if (this.currentStateMachine != null) {
            this.currentStateMachine.setRecursionMode(mode);
        }
    }

    public void stop() {
        this.inbuffer.truncate();
    }

    public int getState() {
        return this.state;
    }

    public InputStream getInputStream() {
        return this.currentStateMachine.getContentStream();
    }

    public InputStream getDecodedInputStream() {
        String transferEncoding = getBodyDescriptor().getTransferEncoding();
        InputStream dataStream = this.currentStateMachine.getContentStream();
        if (MimeUtil.isBase64Encoding(transferEncoding)) {
            return new Base64InputStream(dataStream);
        }
        if (MimeUtil.isQuotedPrintableEncoded(transferEncoding)) {
            return new QuotedPrintableInputStream(dataStream);
        }
        return dataStream;
    }

    public Reader getReader() {
        Charset charset;
        String mimeCharset = getBodyDescriptor().getCharset();
        if (mimeCharset == null || "".equals(mimeCharset)) {
            charset = CharsetUtil.US_ASCII;
        } else {
            charset = Charset.forName(mimeCharset);
        }
        return new InputStreamReader(getDecodedInputStream(), charset);
    }

    public BodyDescriptor getBodyDescriptor() {
        return this.currentStateMachine.getBodyDescriptor();
    }

    public Field getField() {
        return this.currentStateMachine.getField();
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 124 */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0043, code lost:
        r4.currentStateMachine = r4.entities.getLast();
        r4.currentStateMachine.setRecursionMode(r4.recursionMode);
     */
    public int next() throws IOException, MimeException {
        int i;
        if (this.state == -1 || this.currentStateMachine == null) {
            throw new IllegalStateException("No more tokens are available.");
        }
        loop0:
        while (true) {
            if (this.currentStateMachine == null) {
                this.state = -1;
                i = this.state;
                break loop0;
            }
            EntityStateMachine next = this.currentStateMachine.advance();
            if (next != null) {
                this.entities.add(next);
                this.currentStateMachine = next;
            }
            this.state = this.currentStateMachine.getState();
            if (this.state == -1) {
                this.entities.removeLast();
                if (!this.entities.isEmpty()) {
                    break;
                }
                this.currentStateMachine = null;
            } else {
                i = this.state;
                break loop0;
            }
        }
        return i;
    }

    public static final String stateToString(int state2) {
        return AbstractEntity.stateToString(state2);
    }
}
