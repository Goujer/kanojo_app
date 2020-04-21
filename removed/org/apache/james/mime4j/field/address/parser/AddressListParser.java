package org.apache.james.mime4j.field.address.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

public class AddressListParser implements AddressListParserTreeConstants, AddressListParserConstants {
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private final JJCalls[] jj_2_rtns;
    private int jj_endpos;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gc;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private int jj_la;
    private final int[] jj_la1;
    private Token jj_lastpos;
    private int[] jj_lasttokens;
    private final LookaheadSuccess jj_ls;
    public Token jj_nt;
    private int jj_ntk;
    private boolean jj_rescan;
    private Token jj_scanpos;
    private boolean jj_semLA;
    protected JJTAddressListParserState jjtree;
    public boolean lookingAhead;
    public Token token;
    public AddressListParserTokenManager token_source;

    public static void main(String[] args) throws ParseException {
        while (true) {
            try {
                AddressListParser parser = new AddressListParser(System.in);
                parser.parseLine();
                ((SimpleNode) parser.jjtree.rootNode()).dump("> ");
            } catch (Exception x) {
                x.printStackTrace();
                return;
            }
        }
    }

    public ASTaddress_list parseAddressList() throws ParseException {
        try {
            parseAddressList0();
            return (ASTaddress_list) this.jjtree.rootNode();
        } catch (TokenMgrError tme) {
            throw new ParseException(tme.getMessage());
        }
    }

    public ASTaddress parseAddress() throws ParseException {
        try {
            parseAddress0();
            return (ASTaddress) this.jjtree.rootNode();
        } catch (TokenMgrError tme) {
            throw new ParseException(tme.getMessage());
        }
    }

    public ASTmailbox parseMailbox() throws ParseException {
        try {
            parseMailbox0();
            return (ASTmailbox) this.jjtree.rootNode();
        } catch (TokenMgrError tme) {
            throw new ParseException(tme.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public void jjtreeOpenNodeScope(Node n) {
        ((SimpleNode) n).firstToken = getToken(1);
    }

    /* access modifiers changed from: package-private */
    public void jjtreeCloseNodeScope(Node n) {
        ((SimpleNode) n).lastToken = getToken(0);
    }

    public final void parseLine() throws ParseException {
        address_list();
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

    public final void parseAddressList0() throws ParseException {
        address_list();
        jj_consume_token(0);
    }

    public final void parseAddress0() throws ParseException {
        address();
        jj_consume_token(0);
    }

    public final void parseMailbox0() throws ParseException {
        mailbox();
        jj_consume_token(0);
    }

    public final void address_list() throws ParseException {
        ASTaddress_list jjtn000 = new ASTaddress_list(1);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 6:
                case 14:
                case AddressListParserConstants.QUOTEDSTRING:
                    address();
                    break;
                default:
                    this.jj_la1[1] = this.jj_gen;
                    break;
            }
            while (true) {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 3:
                        jj_consume_token(3);
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 6:
                            case 14:
                            case AddressListParserConstants.QUOTEDSTRING:
                                address();
                                break;
                            default:
                                this.jj_la1[3] = this.jj_gen;
                                break;
                        }
                    default:
                        this.jj_la1[2] = this.jj_gen;
                        if (1 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                            jjtreeCloseNodeScope(jjtn000);
                            return;
                        }
                        return;
                }
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void address() throws ParseException {
        int i;
        ASTaddress jjtn000 = new ASTaddress(2);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_1(Integer.MAX_VALUE)) {
                addr_spec();
            } else {
                if (this.jj_ntk == -1) {
                    i = jj_ntk();
                } else {
                    i = this.jj_ntk;
                }
                switch (i) {
                    case 6:
                        angle_addr();
                        break;
                    case 14:
                    case AddressListParserConstants.QUOTEDSTRING:
                        phrase();
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 4:
                                group_body();
                                break;
                            case 6:
                                angle_addr();
                                break;
                            default:
                                this.jj_la1[4] = this.jj_gen;
                                jj_consume_token(-1);
                                throw new ParseException();
                        }
                    default:
                        this.jj_la1[5] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void mailbox() throws ParseException {
        ASTmailbox jjtn000 = new ASTmailbox(3);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (jj_2_2(Integer.MAX_VALUE)) {
                addr_spec();
            } else {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 6:
                        angle_addr();
                        break;
                    case 14:
                    case AddressListParserConstants.QUOTEDSTRING:
                        name_addr();
                        break;
                    default:
                        this.jj_la1[6] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
            }
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void name_addr() throws ParseException {
        ASTname_addr jjtn000 = new ASTname_addr(4);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            phrase();
            angle_addr();
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void group_body() throws ParseException {
        ASTgroup_body jjtn000 = new ASTgroup_body(5);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(4);
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 6:
                case 14:
                case AddressListParserConstants.QUOTEDSTRING:
                    mailbox();
                    break;
                default:
                    this.jj_la1[7] = this.jj_gen;
                    break;
            }
            while (true) {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 3:
                        jj_consume_token(3);
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 6:
                            case 14:
                            case AddressListParserConstants.QUOTEDSTRING:
                                mailbox();
                                break;
                            default:
                                this.jj_la1[9] = this.jj_gen;
                                break;
                        }
                    default:
                        this.jj_la1[8] = this.jj_gen;
                        jj_consume_token(5);
                        if (1 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                            jjtreeCloseNodeScope(jjtn000);
                            return;
                        }
                        return;
                }
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void angle_addr() throws ParseException {
        ASTangle_addr jjtn000 = new ASTangle_addr(6);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            jj_consume_token(6);
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 8:
                    route();
                    break;
                default:
                    this.jj_la1[10] = this.jj_gen;
                    break;
            }
            addr_spec();
            jj_consume_token(7);
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003e A[Catch:{ Throwable -> 0x005e, all -> 0x006e }, LOOP:1: B:13:0x0042->B:12:0x003e, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0046 A[Catch:{ Throwable -> 0x005e, all -> 0x006e }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007a A[SYNTHETIC, Splitter:B:30:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x004d A[SYNTHETIC] */
    public final void route() throws org.apache.james.mime4j.field.address.parser.ParseException {
        /*
            r8 = this;
            r7 = 1
            r6 = -1
            org.apache.james.mime4j.field.address.parser.ASTroute r2 = new org.apache.james.mime4j.field.address.parser.ASTroute
            r3 = 7
            r2.<init>(r3)
            r0 = 1
            org.apache.james.mime4j.field.address.parser.JJTAddressListParserState r3 = r8.jjtree
            r3.openNodeScope(r2)
            r8.jjtreeOpenNodeScope(r2)
            r3 = 8
            r8.jj_consume_token(r3)     // Catch:{ Throwable -> 0x005e }
            r8.domain()     // Catch:{ Throwable -> 0x005e }
        L_0x0019:
            int r3 = r8.jj_ntk     // Catch:{ Throwable -> 0x005e }
            if (r3 != r6) goto L_0x003b
            int r3 = r8.jj_ntk()     // Catch:{ Throwable -> 0x005e }
        L_0x0021:
            switch(r3) {
                case 3: goto L_0x0042;
                case 8: goto L_0x0042;
                default: goto L_0x0024;
            }     // Catch:{ Throwable -> 0x005e }
        L_0x0024:
            int[] r3 = r8.jj_la1     // Catch:{ Throwable -> 0x005e }
            r4 = 11
            int r5 = r8.jj_gen     // Catch:{ Throwable -> 0x005e }
            r3[r4] = r5     // Catch:{ Throwable -> 0x005e }
            r3 = 4
            r8.jj_consume_token(r3)     // Catch:{ Throwable -> 0x005e }
            if (r0 == 0) goto L_0x003a
            org.apache.james.mime4j.field.address.parser.JJTAddressListParserState r3 = r8.jjtree
            r3.closeNodeScope((org.apache.james.mime4j.field.address.parser.Node) r2, (boolean) r7)
            r8.jjtreeCloseNodeScope(r2)
        L_0x003a:
            return
        L_0x003b:
            int r3 = r8.jj_ntk     // Catch:{ Throwable -> 0x005e }
            goto L_0x0021
        L_0x003e:
            r3 = 3
            r8.jj_consume_token(r3)     // Catch:{ Throwable -> 0x005e }
        L_0x0042:
            int r3 = r8.jj_ntk     // Catch:{ Throwable -> 0x005e }
            if (r3 != r6) goto L_0x007a
            int r3 = r8.jj_ntk()     // Catch:{ Throwable -> 0x005e }
        L_0x004a:
            switch(r3) {
                case 3: goto L_0x003e;
                default: goto L_0x004d;
            }     // Catch:{ Throwable -> 0x005e }
        L_0x004d:
            int[] r3 = r8.jj_la1     // Catch:{ Throwable -> 0x005e }
            r4 = 12
            int r5 = r8.jj_gen     // Catch:{ Throwable -> 0x005e }
            r3[r4] = r5     // Catch:{ Throwable -> 0x005e }
            r3 = 8
            r8.jj_consume_token(r3)     // Catch:{ Throwable -> 0x005e }
            r8.domain()     // Catch:{ Throwable -> 0x005e }
            goto L_0x0019
        L_0x005e:
            r1 = move-exception
            if (r0 == 0) goto L_0x007d
            org.apache.james.mime4j.field.address.parser.JJTAddressListParserState r3 = r8.jjtree     // Catch:{ all -> 0x006e }
            r3.clearNodeScope(r2)     // Catch:{ all -> 0x006e }
            r0 = 0
        L_0x0067:
            boolean r3 = r1 instanceof java.lang.RuntimeException     // Catch:{ all -> 0x006e }
            if (r3 == 0) goto L_0x0083
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1     // Catch:{ all -> 0x006e }
            throw r1     // Catch:{ all -> 0x006e }
        L_0x006e:
            r3 = move-exception
            if (r0 == 0) goto L_0x0079
            org.apache.james.mime4j.field.address.parser.JJTAddressListParserState r4 = r8.jjtree
            r4.closeNodeScope((org.apache.james.mime4j.field.address.parser.Node) r2, (boolean) r7)
            r8.jjtreeCloseNodeScope(r2)
        L_0x0079:
            throw r3
        L_0x007a:
            int r3 = r8.jj_ntk     // Catch:{ Throwable -> 0x005e }
            goto L_0x004a
        L_0x007d:
            org.apache.james.mime4j.field.address.parser.JJTAddressListParserState r3 = r8.jjtree     // Catch:{ all -> 0x006e }
            r3.popNode()     // Catch:{ all -> 0x006e }
            goto L_0x0067
        L_0x0083:
            boolean r3 = r1 instanceof org.apache.james.mime4j.field.address.parser.ParseException     // Catch:{ all -> 0x006e }
            if (r3 == 0) goto L_0x008a
            org.apache.james.mime4j.field.address.parser.ParseException r1 = (org.apache.james.mime4j.field.address.parser.ParseException) r1     // Catch:{ all -> 0x006e }
            throw r1     // Catch:{ all -> 0x006e }
        L_0x008a:
            java.lang.Error r1 = (java.lang.Error) r1     // Catch:{ all -> 0x006e }
            throw r1     // Catch:{ all -> 0x006e }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.james.mime4j.field.address.parser.AddressListParser.route():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0012 A[LOOP_START, SYNTHETIC, Splitter:B:1:0x0012] */
    public final void phrase() throws ParseException {
        int i;
        ASTphrase jjtn000 = new ASTphrase(8);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        while (true) {
            try {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 14:
                        jj_consume_token(14);
                        break;
                    case AddressListParserConstants.QUOTEDSTRING:
                        jj_consume_token(31);
                        break;
                    default:
                        this.jj_la1[13] = this.jj_gen;
                        jj_consume_token(-1);
                        throw new ParseException();
                }
                if (this.jj_ntk == -1) {
                    i = jj_ntk();
                    continue;
                } else {
                    i = this.jj_ntk;
                    continue;
                }
                switch (i) {
                    case 14:
                    case AddressListParserConstants.QUOTEDSTRING:
                        break;
                }
                this.jj_la1[14] = this.jj_gen;
                if (1 == 0) {
                    return;
                }
                return;
            } finally {
                if (1 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtreeCloseNodeScope(jjtn000);
                }
            }
        }
    }

    public final void addr_spec() throws ParseException {
        ASTaddr_spec jjtn000 = new ASTaddr_spec(9);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            local_part();
            jj_consume_token(8);
            domain();
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
            throw th;
        }
    }

    public final void local_part() throws ParseException {
        Token t;
        Token t2;
        ASTlocal_part jjtn000 = new ASTlocal_part(10);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                case 14:
                    t = jj_consume_token(14);
                    break;
                case AddressListParserConstants.QUOTEDSTRING:
                    t = jj_consume_token(31);
                    break;
                default:
                    this.jj_la1[15] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            while (true) {
                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                    case 9:
                    case 14:
                    case AddressListParserConstants.QUOTEDSTRING:
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 9:
                                t = jj_consume_token(9);
                                break;
                            default:
                                this.jj_la1[17] = this.jj_gen;
                                break;
                        }
                        if (t.kind == 31 || t.image.charAt(t.image.length() - 1) != '.') {
                            break;
                        } else {
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case 14:
                                    t2 = jj_consume_token(14);
                                    break;
                                case AddressListParserConstants.QUOTEDSTRING:
                                    t2 = jj_consume_token(31);
                                    break;
                                default:
                                    this.jj_la1[18] = this.jj_gen;
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                        }
                        break;
                    default:
                        this.jj_la1[16] = this.jj_gen;
                        if (1 == 0) {
                            return;
                        }
                        return;
                }
            }
            throw new ParseException("Words in local part must be separated by '.'");
        } finally {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    public final void domain() throws ParseException {
        int i;
        ASTdomain jjtn000 = new ASTdomain(11);
        this.jjtree.openNodeScope(jjtn000);
        jjtreeOpenNodeScope(jjtn000);
        try {
            if (this.jj_ntk == -1) {
                i = jj_ntk();
            } else {
                i = this.jj_ntk;
            }
            switch (i) {
                case 14:
                    Token t = jj_consume_token(14);
                    while (true) {
                        switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                            case 9:
                            case 14:
                                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                    case 9:
                                        t = jj_consume_token(9);
                                        break;
                                    default:
                                        this.jj_la1[20] = this.jj_gen;
                                        break;
                                }
                                if (t.image.charAt(t.image.length() - 1) != '.') {
                                    throw new ParseException("Atoms in domain names must be separated by '.'");
                                }
                                t = jj_consume_token(14);
                            default:
                                this.jj_la1[19] = this.jj_gen;
                                break;
                        }
                    }
                case 18:
                    jj_consume_token(18);
                    break;
                default:
                    this.jj_la1[21] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        } finally {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                jjtreeCloseNodeScope(jjtn000);
            }
        }
    }

    private final boolean jj_2_1(int xla) {
        boolean z = true;
        this.jj_la = xla;
        Token token2 = this.token;
        this.jj_scanpos = token2;
        this.jj_lastpos = token2;
        try {
            if (jj_3_1()) {
                z = false;
            }
        } catch (LookaheadSuccess e) {
        } finally {
            jj_save(0, xla);
        }
        return z;
    }

    /* JADX INFO: finally extract failed */
    private final boolean jj_2_2(int xla) {
        this.jj_la = xla;
        Token token2 = this.token;
        this.jj_scanpos = token2;
        this.jj_lastpos = token2;
        try {
            boolean z = !jj_3_2();
            jj_save(1, xla);
            return z;
        } catch (LookaheadSuccess e) {
            jj_save(1, xla);
            return true;
        } catch (Throwable th) {
            jj_save(1, xla);
            throw th;
        }
    }

    private final boolean jj_3R_12() {
        Token xsp;
        if (jj_scan_token(14)) {
            return true;
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_13());
        this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_10() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_12()) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(18)) {
                return true;
            }
        }
        return false;
    }

    private final boolean jj_3_2() {
        if (jj_3R_8()) {
            return true;
        }
        return false;
    }

    private final boolean jj_3R_9() {
        Token xsp;
        Token xsp2 = this.jj_scanpos;
        if (jj_scan_token(14)) {
            this.jj_scanpos = xsp2;
            if (jj_scan_token(31)) {
                return true;
            }
        }
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_11());
        this.jj_scanpos = xsp;
        return false;
    }

    private final boolean jj_3R_11() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(9)) {
            this.jj_scanpos = xsp;
        }
        Token xsp2 = this.jj_scanpos;
        if (jj_scan_token(14)) {
            this.jj_scanpos = xsp2;
            if (jj_scan_token(31)) {
                return true;
            }
        }
        return false;
    }

    private final boolean jj_3R_13() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(9)) {
            this.jj_scanpos = xsp;
        }
        if (jj_scan_token(14)) {
            return true;
        }
        return false;
    }

    private final boolean jj_3R_8() {
        if (!jj_3R_9() && !jj_scan_token(8) && !jj_3R_10()) {
            return false;
        }
        return true;
    }

    private final boolean jj_3_1() {
        if (jj_3R_8()) {
            return true;
        }
        return false;
    }

    static {
        jj_la1_0();
        jj_la1_1();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[]{2, -2147467200, 8, -2147467200, 80, -2147467200, -2147467200, -2147467200, 8, -2147467200, 256, 264, 8, -2147467264, -2147467264, -2147467264, -2147466752, 512, -2147467264, 16896, 512, 278528};
    }

    private static void jj_la1_1() {
        jj_la1_1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public AddressListParser(InputStream stream) {
        this(stream, (String) null);
    }

    public AddressListParser(InputStream stream, String encoding) {
        this.jjtree = new JJTAddressListParserState();
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
            this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            for (int i = 0; i < 22; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
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
            this.jjtree.reset();
            this.jj_gen = 0;
            for (int i = 0; i < 22; i++) {
                this.jj_la1[i] = -1;
            }
            for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                this.jj_2_rtns[i2] = new JJCalls();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AddressListParser(Reader stream) {
        this.jjtree = new JJTAddressListParserState();
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 22; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        for (int i = 0; i < 22; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public AddressListParser(AddressListParserTokenManager tm) {
        this.jjtree = new JJTAddressListParserState();
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 22; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(AddressListParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        this.jj_gen = 0;
        for (int i = 0; i < 22; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
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
            int i = this.jj_gc + 1;
            this.jj_gc = i;
            if (i > 100) {
                this.jj_gc = 0;
                for (JJCalls c : this.jj_2_rtns) {
                    while (c != null) {
                        if (c.gen < this.jj_gen) {
                            c.first = null;
                        }
                        c = c.next;
                    }
                }
            }
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    private static final class LookaheadSuccess extends Error {
        private LookaheadSuccess() {
        }
    }

    private final boolean jj_scan_token(int kind) {
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            if (this.jj_scanpos.next == null) {
                Token token2 = this.jj_scanpos;
                Token nextToken = this.token_source.getNextToken();
                token2.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            } else {
                Token token3 = this.jj_scanpos.next;
                this.jj_scanpos = token3;
                this.jj_lastpos = token3;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_rescan) {
            int i = 0;
            Token tok = this.token;
            while (tok != null && tok != this.jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) {
                jj_add_error_token(kind, i);
            }
        }
        if (this.jj_scanpos.kind != kind) {
            return true;
        }
        if (this.jj_la != 0 || this.jj_scanpos != this.jj_lastpos) {
            return false;
        }
        throw this.jj_ls;
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
        Token t2 = this.lookingAhead ? this.jj_scanpos : this.token;
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

    private void jj_add_error_token(int kind, int pos) {
        if (pos < 100) {
            if (pos == this.jj_endpos + 1) {
                int[] iArr = this.jj_lasttokens;
                int i = this.jj_endpos;
                this.jj_endpos = i + 1;
                iArr[i] = kind;
            } else if (this.jj_endpos != 0) {
                this.jj_expentry = new int[this.jj_endpos];
                for (int i2 = 0; i2 < this.jj_endpos; i2++) {
                    this.jj_expentry[i2] = this.jj_lasttokens[i2];
                }
                boolean exists = false;
                Enumeration e = this.jj_expentries.elements();
                while (e.hasMoreElements()) {
                    int[] oldentry = e.nextElement();
                    if (oldentry.length == this.jj_expentry.length) {
                        exists = true;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.jj_expentry.length) {
                                break;
                            } else if (oldentry[i3] != this.jj_expentry[i3]) {
                                exists = false;
                                break;
                            } else {
                                i3++;
                            }
                        }
                        if (exists) {
                            break;
                        }
                    }
                }
                if (!exists) {
                    this.jj_expentries.addElement(this.jj_expentry);
                }
                if (pos != 0) {
                    int[] iArr2 = this.jj_lasttokens;
                    this.jj_endpos = pos;
                    iArr2[pos - 1] = kind;
                }
            }
        }
    }

    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[34];
        for (int i = 0; i < 34; i++) {
            la1tokens[i] = false;
        }
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i2 = 0; i2 < 22; i2++) {
            if (this.jj_la1[i2] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i2] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                    if ((jj_la1_1[i2] & (1 << j)) != 0) {
                        la1tokens[j + 32] = true;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < 34; i3++) {
            if (la1tokens[i3]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i3;
                this.jj_expentries.addElement(this.jj_expentry);
            }
        }
        this.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
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

    private final void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 2; i++) {
            try {
                JJCalls p = this.jj_2_rtns[i];
                do {
                    if (p.gen > this.jj_gen) {
                        this.jj_la = p.arg;
                        Token token2 = p.first;
                        this.jj_scanpos = token2;
                        this.jj_lastpos = token2;
                        switch (i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess e) {
            }
        }
        this.jj_rescan = false;
    }

    private final void jj_save(int index, int xla) {
        JJCalls p = this.jj_2_rtns[index];
        while (true) {
            if (p.gen <= this.jj_gen) {
                break;
            } else if (p.next == null) {
                JJCalls p2 = new JJCalls();
                p.next = p2;
                p = p2;
                break;
            } else {
                p = p.next;
            }
        }
        p.gen = (this.jj_gen + xla) - this.jj_la;
        p.first = this.token;
        p.arg = xla;
    }

    static final class JJCalls {
        int arg;
        Token first;
        int gen;
        JJCalls next;

        JJCalls() {
        }
    }
}
