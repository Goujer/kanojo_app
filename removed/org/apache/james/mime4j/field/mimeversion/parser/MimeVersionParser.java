package org.apache.james.mime4j.field.mimeversion.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class MimeVersionParser implements MimeVersionParserConstants {
    public static final int INITIAL_VERSION_VALUE = -1;
    private static int[] jj_la1_0;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    public Token jj_nt;
    private int jj_ntk;
    private int major;
    private int minor;
    public Token token;
    public MimeVersionParserTokenManager token_source;

    public int getMinorVersion() {
        return this.minor;
    }

    public int getMajorVersion() {
        return this.major;
    }

    public final void parseLine() throws ParseException {
        parse();
        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
            case 1:
                jj_consume_token(1);
                break;
            default:
                this.jj_la1[0] = this.jj_gen;
                break;
        }
        jj_consume_token(2);
    }

    public final void parseAll() throws ParseException {
        parse();
        jj_consume_token(0);
    }

    public final void parse() throws ParseException {
        Token major2 = jj_consume_token(17);
        jj_consume_token(18);
        Token minor2 = jj_consume_token(17);
        try {
            this.major = Integer.parseInt(major2.image);
            this.minor = Integer.parseInt(minor2.image);
        } catch (NumberFormatException e) {
            throw new ParseException(e.getMessage());
        }
    }

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[]{2};
    }

    public MimeVersionParser(InputStream stream) {
        this(stream, (String) null);
    }

    public MimeVersionParser(InputStream stream, String encoding) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
            this.token_source = new MimeVersionParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 1; i++) {
                this.jj_la1[i] = -1;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReInit(InputStream stream) {
        ReInit(stream, (String) null);
    }

    public void ReInit(InputStream stream, String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 1; i++) {
                this.jj_la1[i] = -1;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public MimeVersionParser(Reader stream) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new MimeVersionParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 1; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 1; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public MimeVersionParser(MimeVersionParserTokenManager tm) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 1; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(MimeVersionParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 1; i++) {
            this.jj_la1[i] = -1;
        }
    }

    private final Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if (oldToken.next != null) {
            this.token = this.token.next;
        } else {
            Token token2 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            this.jj_gen++;
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            Token token2 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        this.jj_gen++;
        return this.token;
    }

    public final Token getToken(int index) {
        Token t;
        int i = 0;
        Token t2 = this.token;
        while (i < index) {
            if (t2.next != null) {
                t = t2.next;
            } else {
                t = this.token_source.getNextToken();
                t2.next = t;
            }
            i++;
            t2 = t;
        }
        return t2;
    }

    private final int jj_ntk() {
        Token token2 = this.token.next;
        this.jj_nt = token2;
        if (token2 == null) {
            Token token3 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token3.next = nextToken;
            int i = nextToken.kind;
            this.jj_ntk = i;
            return i;
        }
        int i2 = this.jj_nt.kind;
        this.jj_ntk = i2;
        return i2;
    }

    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[21];
        for (int i = 0; i < 21; i++) {
            la1tokens[i] = false;
        }
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i2 = 0; i2 < 1; i2++) {
            if (this.jj_la1[i2] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i2] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < 21; i3++) {
            if (la1tokens[i3]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i3;
                this.jj_expentries.addElement(this.jj_expentry);
            }
        }
        int[][] exptokseq = new int[this.jj_expentries.size()][];
        for (int i4 = 0; i4 < this.jj_expentries.size(); i4++) {
            exptokseq[i4] = this.jj_expentries.elementAt(i4);
        }
        return new ParseException(this.token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }
}
