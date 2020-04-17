package org.apache.james.mime4j.field.language.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ContentLanguageParser implements ContentLanguageParserConstants {
    private static int[] jj_la1_0;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    public Token jj_nt;
    private int jj_ntk;
    private List<String> languages;
    public Token token;
    public ContentLanguageParserTokenManager token_source;

    public List<String> parse() throws ParseException {
        try {
            return doParse();
        } catch (TokenMgrError e) {
            throw new ParseException((Throwable) e);
        }
    }

    private final List<String> doParse() throws ParseException {
        language();
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 1:
                    jj_consume_token(1);
                    language();
                default:
                    this.jj_la1[0] = this.jj_gen;
                    return this.languages;
            }
        }
    }

    public final String language() throws ParseException {
        int i;
        StringBuffer languageTag = new StringBuffer();
        languageTag.append(jj_consume_token(18).image);
        while (true) {
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 2:
                case 19:
                    if (this.jj_ntk == -1) {
                        i = jj_ntk();
                    } else {
                        i = this.jj_ntk;
                    }
                    switch (i) {
                        case 2:
                            jj_consume_token(2);
                            Token token2 = jj_consume_token(18);
                            languageTag.append('-');
                            languageTag.append(token2.image);
                            break;
                        case 19:
                            Token token3 = jj_consume_token(19);
                            languageTag.append('-');
                            languageTag.append(token3.image);
                            break;
                        default:
                            this.jj_la1[2] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                default:
                    this.jj_la1[1] = this.jj_gen;
                    String result = languageTag.toString();
                    this.languages.add(result);
                    return result;
            }
        }
    }

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[]{2, 524292, 524292};
    }

    public ContentLanguageParser(InputStream stream) {
        this(stream, (String) null);
    }

    public ContentLanguageParser(InputStream stream, String encoding) {
        this.languages = new ArrayList();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
            this.token_source = new ContentLanguageParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 3; i++) {
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
            for (int i = 0; i < 3; i++) {
                this.jj_la1[i] = -1;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ContentLanguageParser(Reader stream) {
        this.languages = new ArrayList();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new ContentLanguageParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public ContentLanguageParser(ContentLanguageParserTokenManager tm) {
        this.languages = new ArrayList();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 3; i++) {
            this.jj_la1[i] = -1;
        }
    }

    public void ReInit(ContentLanguageParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 3; i++) {
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
        boolean[] la1tokens = new boolean[23];
        for (int i = 0; i < 23; i++) {
            la1tokens[i] = false;
        }
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            if (this.jj_la1[i2] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i2] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < 23; i3++) {
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
