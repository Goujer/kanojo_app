package org.apache.james.mime4j.parser;

import java.io.IOException;
import java.util.BitSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.descriptor.DefaultBodyDescriptor;
import org.apache.james.mime4j.descriptor.MaximalBodyDescriptor;
import org.apache.james.mime4j.descriptor.MutableBodyDescriptor;
import org.apache.james.mime4j.io.LineReaderInputStream;
import org.apache.james.mime4j.io.MaxHeaderLimitException;
import org.apache.james.mime4j.io.MaxLineLimitException;
import org.apache.james.mime4j.util.ByteArrayBuffer;

public abstract class AbstractEntity implements EntityStateMachine {
    private static final int T_IN_BODYPART = -2;
    private static final int T_IN_MESSAGE = -3;
    private static final BitSet fieldChars = new BitSet();
    protected final MutableBodyDescriptor body;
    protected final MimeEntityConfig config;
    private boolean endOfHeader;
    protected final int endState;
    private Field field;
    private int headerCount;
    private int lineCount;
    private final ByteArrayBuffer linebuf;
    protected final Log log = LogFactory.getLog(getClass());
    protected final BodyDescriptor parent;
    protected final int startState;
    protected int state;

    /* access modifiers changed from: protected */
    public abstract LineReaderInputStream getDataStream();

    /* access modifiers changed from: protected */
    public abstract int getLineNumber();

    static {
        for (int i = 33; i <= 57; i++) {
            fieldChars.set(i);
        }
        for (int i2 = 59; i2 <= 126; i2++) {
            fieldChars.set(i2);
        }
    }

    AbstractEntity(BodyDescriptor parent2, int startState2, int endState2, MimeEntityConfig config2) {
        this.parent = parent2;
        this.state = startState2;
        this.startState = startState2;
        this.endState = endState2;
        this.config = config2;
        this.body = newBodyDescriptor(parent2);
        this.linebuf = new ByteArrayBuffer(64);
        this.lineCount = 0;
        this.endOfHeader = false;
        this.headerCount = 0;
    }

    public int getState() {
        return this.state;
    }

    /* access modifiers changed from: protected */
    public MutableBodyDescriptor newBodyDescriptor(BodyDescriptor pParent) {
        if (this.config.isMaximalBodyDescriptor()) {
            return new MaximalBodyDescriptor(pParent);
        }
        return new DefaultBodyDescriptor(pParent);
    }

    private ByteArrayBuffer fillFieldBuffer() throws IOException, MimeException {
        int ch;
        if (this.endOfHeader) {
            throw new IllegalStateException();
        }
        int maxLineLen = this.config.getMaxLineLen();
        LineReaderInputStream instream = getDataStream();
        ByteArrayBuffer fieldbuf = new ByteArrayBuffer(64);
        while (true) {
            int len = this.linebuf.length();
            if (maxLineLen <= 0 || fieldbuf.length() + len < maxLineLen) {
                if (len > 0) {
                    fieldbuf.append(this.linebuf.buffer(), 0, len);
                }
                this.linebuf.clear();
                if (instream.readLine(this.linebuf) != -1) {
                    int len2 = this.linebuf.length();
                    if (len2 > 0 && this.linebuf.byteAt(len2 - 1) == 10) {
                        len2--;
                    }
                    if (len2 > 0 && this.linebuf.byteAt(len2 - 1) == 13) {
                        len2--;
                    }
                    if (len2 != 0) {
                        this.lineCount++;
                        if (this.lineCount > 1 && (ch = this.linebuf.byteAt(0)) != 32 && ch != 9) {
                            break;
                        }
                    } else {
                        this.endOfHeader = true;
                        break;
                    }
                } else {
                    monitor(Event.HEADERS_PREMATURE_END);
                    this.endOfHeader = true;
                    break;
                }
            } else {
                throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
        }
        return fieldbuf;
    }

    /* access modifiers changed from: protected */
    public boolean parseField() throws MimeException, IOException {
        int maxHeaderLimit = this.config.getMaxHeaderCount();
        while (!this.endOfHeader) {
            if (this.headerCount >= maxHeaderLimit) {
                throw new MaxHeaderLimitException("Maximum header limit exceeded");
            }
            ByteArrayBuffer fieldbuf = fillFieldBuffer();
            this.headerCount++;
            int len = fieldbuf.length();
            if (len > 0 && fieldbuf.byteAt(len - 1) == 10) {
                len--;
            }
            if (len > 0 && fieldbuf.byteAt(len - 1) == 13) {
                len--;
            }
            fieldbuf.setLength(len);
            boolean valid = true;
            int pos = fieldbuf.indexOf((byte) 58);
            if (pos <= 0) {
                monitor(Event.INALID_HEADER);
                valid = false;
                continue;
            } else {
                int i = 0;
                while (true) {
                    if (i >= pos) {
                        continue;
                        break;
                    } else if (!fieldChars.get(fieldbuf.byteAt(i) & 255)) {
                        monitor(Event.INALID_HEADER);
                        valid = false;
                        continue;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            if (valid) {
                this.field = new RawField(fieldbuf, pos);
                this.body.addField(this.field);
                return true;
            }
        }
        return false;
    }

    public BodyDescriptor getBodyDescriptor() {
        switch (getState()) {
            case -1:
            case 6:
            case 8:
            case 9:
            case 12:
                return this.body;
            default:
                throw new IllegalStateException("Invalid state :" + stateToString(this.state));
        }
    }

    public Field getField() {
        switch (getState()) {
            case 4:
                return this.field;
            default:
                throw new IllegalStateException("Invalid state :" + stateToString(this.state));
        }
    }

    /* access modifiers changed from: protected */
    public void monitor(Event event) throws MimeException, IOException {
        if (this.config.isStrictParsing()) {
            throw new MimeParseEventException(event);
        }
        warn(event);
    }

    /* access modifiers changed from: protected */
    public String message(Event event) {
        String message;
        if (event == null) {
            message = "Event is unexpectedly null.";
        } else {
            message = event.toString();
        }
        int lineNumber = getLineNumber();
        return lineNumber <= 0 ? message : "Line " + lineNumber + ": " + message;
    }

    /* access modifiers changed from: protected */
    public void warn(Event event) {
        if (this.log.isWarnEnabled()) {
            this.log.warn(message(event));
        }
    }

    /* access modifiers changed from: protected */
    public void debug(Event event) {
        if (this.log.isDebugEnabled()) {
            this.log.debug(message(event));
        }
    }

    public String toString() {
        return getClass().getName() + " [" + stateToString(this.state) + "][" + this.body.getMimeType() + "][" + this.body.getBoundary() + "]";
    }

    public static final String stateToString(int state2) {
        switch (state2) {
            case -3:
                return "In message";
            case -2:
                return "Bodypart";
            case -1:
                return "End of stream";
            case 0:
                return "Start message";
            case 1:
                return "End message";
            case 2:
                return "Raw entity";
            case 3:
                return "Start header";
            case 4:
                return "Field";
            case 5:
                return "End header";
            case 6:
                return "Start multipart";
            case 7:
                return "End multipart";
            case 8:
                return "Preamble";
            case 9:
                return "Epilogue";
            case 10:
                return "Start bodypart";
            case 11:
                return "End bodypart";
            case 12:
                return "Body";
            default:
                return "Unknown";
        }
    }
}
