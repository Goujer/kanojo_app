package org.apache.james.mime4j.field.datetime.parser;

import java.io.IOException;
import java.io.PrintStream;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.core.util.Live2dUtil;
import jp.live2d.Def;
import jp.live2d.motion.Live2DMotion;

public class DateTimeParserTokenManager implements DateTimeParserConstants {
    static int commentNest;
    static final long[] jjbitVec0 = {0, 0, -1, -1};
    public static final int[] jjnewLexState = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1};
    static final int[] jjnextStates = new int[0];
    public static final String[] jjstrLiteralImages = {"", "\r", "\n", ",", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ":", null, "UT", "GMT", "EST", "EDT", "CST", "CDT", "MST", "MDT", "PST", "PDT", null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    static final long[] jjtoMore = {69956427317248L};
    static final long[] jjtoSkip = {343597383680L};
    static final long[] jjtoSpecial = {68719476736L};
    static final long[] jjtoToken = {70437463654399L};
    public static final String[] lexStateNames = {"DEFAULT", "INCOMMENT", "NESTED_COMMENT"};
    protected char curChar;
    int curLexState;
    public PrintStream debugStream;
    int defaultLexState;
    StringBuffer image;
    protected SimpleCharStream input_stream;
    int jjimageLen;
    int jjmatchedKind;
    int jjmatchedPos;
    int jjnewStateCnt;
    int jjround;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    int lengthOfMatch;

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
            case 0:
                if ((34334373872L & active0) != 0) {
                    this.jjmatchedKind = 35;
                    break;
                }
                break;
            case 1:
                if ((34334373872L & active0) != 0 && this.jjmatchedPos == 0) {
                    this.jjmatchedKind = 35;
                    this.jjmatchedPos = 0;
                    break;
                }
        }
        return -1;
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
            return jjMoveNfa_0(state, pos + 1);
        } catch (IOException e) {
            return pos + 1;
        }
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case 10:
                return jjStopAtPos(0, 2);
            case 13:
                return jjStopAtPos(0, 1);
            case '(':
                return jjStopAtPos(0, 37);
            case ',':
                return jjStopAtPos(0, 3);
            case ':':
                return jjStopAtPos(0, 23);
            case Def.PIVOT_TABLE_SIZE:
                return jjMoveStringLiteralDfa1_0(278528);
            case 'C':
                return jjMoveStringLiteralDfa1_0(1610612736);
            case 'D':
                return jjMoveStringLiteralDfa1_0(4194304);
            case 'E':
                return jjMoveStringLiteralDfa1_0(402653184);
            case 'F':
                return jjMoveStringLiteralDfa1_0(4352);
            case 'G':
                return jjMoveStringLiteralDfa1_0(67108864);
            case 'J':
                return jjMoveStringLiteralDfa1_0(198656);
            case 'M':
                return jjMoveStringLiteralDfa1_0(6442491920L);
            case 'N':
                return jjMoveStringLiteralDfa1_0(2097152);
            case 'O':
                return jjMoveStringLiteralDfa1_0(1048576);
            case 'P':
                return jjMoveStringLiteralDfa1_0(25769803776L);
            case 'S':
                return jjMoveStringLiteralDfa1_0(525824);
            case 'T':
                return jjMoveStringLiteralDfa1_0(160);
            case Live2dUtil.DEFAULT_LOVE_GAUGE:
                return jjMoveStringLiteralDfa1_0(33554432);
            case 'W':
                return jjMoveStringLiteralDfa1_0(64);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'D':
                    return jjMoveStringLiteralDfa2_0(active0, 22817013760L);
                case 'M':
                    return jjMoveStringLiteralDfa2_0(active0, 67108864);
                case 'S':
                    return jjMoveStringLiteralDfa2_0(active0, 11408506880L);
                case 'T':
                    if ((33554432 & active0) != 0) {
                        return jjStopAtPos(1, 25);
                    }
                    break;
                case 'a':
                    return jjMoveStringLiteralDfa2_0(active0, 43520);
                case 'c':
                    return jjMoveStringLiteralDfa2_0(active0, 1048576);
                case 'e':
                    return jjMoveStringLiteralDfa2_0(active0, 4722752);
                case Live2DMotion.Motion.MOTION_TYPE_LAYOUT_SCALE_X:
                    return jjMoveStringLiteralDfa2_0(active0, 128);
                case BaseInterface.RESULT_DELETE_ACCOUNT:
                    return jjMoveStringLiteralDfa2_0(active0, 2097168);
                case 'p':
                    return jjMoveStringLiteralDfa2_0(active0, 16384);
                case 'r':
                    return jjMoveStringLiteralDfa2_0(active0, 256);
                case 'u':
                    return jjMoveStringLiteralDfa2_0(active0, 459808);
            }
            return jjStartNfa_0(0, active0);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 'T':
                    if ((67108864 & active02) != 0) {
                        return jjStopAtPos(2, 26);
                    }
                    if ((134217728 & active02) != 0) {
                        return jjStopAtPos(2, 27);
                    }
                    if ((268435456 & active02) != 0) {
                        return jjStopAtPos(2, 28);
                    }
                    if ((536870912 & active02) != 0) {
                        return jjStopAtPos(2, 29);
                    }
                    if ((1073741824 & active02) != 0) {
                        return jjStopAtPos(2, 30);
                    }
                    if ((2147483648L & active02) != 0) {
                        return jjStopAtPos(2, 31);
                    }
                    if ((4294967296L & active02) != 0) {
                        return jjStopAtPos(2, 32);
                    }
                    if ((8589934592L & active02) != 0) {
                        return jjStopAtPos(2, 33);
                    }
                    if ((17179869184L & active02) != 0) {
                        return jjStopAtPos(2, 34);
                    }
                    break;
                case 'b':
                    if ((4096 & active02) != 0) {
                        return jjStopAtPos(2, 12);
                    }
                    break;
                case 'c':
                    if ((4194304 & active02) != 0) {
                        return jjStopAtPos(2, 22);
                    }
                    break;
                case 'd':
                    if ((64 & active02) != 0) {
                        return jjStopAtPos(2, 6);
                    }
                    break;
                case 'e':
                    if ((32 & active02) != 0) {
                        return jjStopAtPos(2, 5);
                    }
                    break;
                case 'g':
                    if ((262144 & active02) != 0) {
                        return jjStopAtPos(2, 18);
                    }
                    break;
                case 'i':
                    if ((256 & active02) != 0) {
                        return jjStopAtPos(2, 8);
                    }
                    break;
                case BaseInterface.RESULT_MODIFIED:
                    if ((131072 & active02) != 0) {
                        return jjStopAtPos(2, 17);
                    }
                    break;
                case BaseInterface.RESULT_PRIVACY_OK:
                    if ((16 & active02) != 0) {
                        return jjStopAtPos(2, 4);
                    }
                    if ((1024 & active02) != 0) {
                        return jjStopAtPos(2, 10);
                    }
                    if ((2048 & active02) != 0) {
                        return jjStopAtPos(2, 11);
                    }
                    if ((65536 & active02) != 0) {
                        return jjStopAtPos(2, 16);
                    }
                    break;
                case 'p':
                    if ((524288 & active02) != 0) {
                        return jjStopAtPos(2, 19);
                    }
                    break;
                case 'r':
                    if ((8192 & active02) != 0) {
                        return jjStopAtPos(2, 13);
                    }
                    if ((16384 & active02) != 0) {
                        return jjStopAtPos(2, 14);
                    }
                    break;
                case 't':
                    if ((512 & active02) != 0) {
                        return jjStopAtPos(2, 9);
                    }
                    if ((1048576 & active02) != 0) {
                        return jjStopAtPos(2, 20);
                    }
                    break;
                case 'u':
                    if ((128 & active02) != 0) {
                        return jjStopAtPos(2, 7);
                    }
                    break;
                case 'v':
                    if ((2097152 & active02) != 0) {
                        return jjStopAtPos(2, 21);
                    }
                    break;
                case 'y':
                    if ((32768 & active02) != 0) {
                        return jjStopAtPos(2, 15);
                    }
                    break;
            }
            return jjStartNfa_0(1, active02);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(1, active02);
            return 2;
        }
    }

    private final void jjCheckNAdd(int state) {
        if (this.jjrounds[state] != this.jjround) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        while (true) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = jjnextStates[start];
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        while (true) {
            jjCheckNAdd(jjnextStates[start]);
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    private final int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 4;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 46) {
                                    kind = 46;
                                }
                                jjCheckNAdd(3);
                                continue;
                            } else if ((4294967808L & l) != 0) {
                                if (kind > 36) {
                                    kind = 36;
                                }
                                jjCheckNAdd(2);
                                continue;
                            } else if ((43980465111040L & l) != 0 && kind > 24) {
                                kind = 24;
                                continue;
                            }
                        case 2:
                            if ((4294967808L & l) != 0) {
                                kind = 36;
                                jjCheckNAdd(2);
                                continue;
                            } else {
                                continue;
                            }
                        case 3:
                            if ((287948901175001088L & l) != 0) {
                                kind = 46;
                                jjCheckNAdd(3);
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((576456345801194494L & l2) != 0) {
                                kind = 35;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else {
                int i3 = (this.curChar & 255) >> 6;
                long j = 1 << (this.curChar & '?');
                do {
                    i--;
                    int i4 = this.jjstateSet[i];
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 4 - startsAt;
            if (i != startsAt) {
                try {
                    this.curChar = this.input_stream.readChar();
                } catch (IOException e) {
                }
            }
            return curPos;
        }
    }

    private final int jjStopStringLiteralDfa_1(int pos, long active0) {
        return -1;
    }

    private final int jjStartNfa_1(int pos, long active0) {
        return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_1(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
            return jjMoveNfa_1(state, pos + 1);
        } catch (IOException e) {
            return pos + 1;
        }
    }

    private final int jjMoveStringLiteralDfa0_1() {
        switch (this.curChar) {
            case '(':
                return jjStopAtPos(0, 40);
            case ')':
                return jjStopAtPos(0, 38);
            default:
                return jjMoveNfa_1(0, 0);
        }
    }

    private final int jjMoveNfa_1(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 3;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long j = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (kind > 41) {
                                kind = 41;
                                continue;
                            } else {
                                continue;
                            }
                        case 1:
                            if (kind > 39) {
                                kind = 39;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long j2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (kind > 41) {
                                kind = 41;
                            }
                            if (this.curChar == '\\') {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 1;
                                continue;
                            } else {
                                continue;
                            }
                        case 1:
                            if (kind > 39) {
                                kind = 39;
                                continue;
                            } else {
                                continue;
                            }
                        case 2:
                            if (kind > 41) {
                                kind = 41;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else {
                int i22 = (this.curChar & 255) >> 6;
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((jjbitVec0[i22] & l2) != 0 && kind > 41) {
                                kind = 41;
                                continue;
                            }
                        case 1:
                            if ((jjbitVec0[i22] & l2) != 0 && kind > 39) {
                                kind = 39;
                                continue;
                            }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 3 - startsAt;
            if (i != startsAt) {
                try {
                    this.curChar = this.input_stream.readChar();
                } catch (IOException e) {
                }
            }
            return curPos;
        }
    }

    private final int jjStopStringLiteralDfa_2(int pos, long active0) {
        return -1;
    }

    private final int jjStartNfa_2(int pos, long active0) {
        return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
    }

    private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
            return jjMoveNfa_2(state, pos + 1);
        } catch (IOException e) {
            return pos + 1;
        }
    }

    private final int jjMoveStringLiteralDfa0_2() {
        switch (this.curChar) {
            case '(':
                return jjStopAtPos(0, 43);
            case ')':
                return jjStopAtPos(0, 44);
            default:
                return jjMoveNfa_2(0, 0);
        }
    }

    private final int jjMoveNfa_2(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 3;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < '@') {
                long j = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (kind > 45) {
                                kind = 45;
                                continue;
                            } else {
                                continue;
                            }
                        case 1:
                            if (kind > 42) {
                                kind = 42;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long j2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if (kind > 45) {
                                kind = 45;
                            }
                            if (this.curChar == '\\') {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 1;
                                continue;
                            } else {
                                continue;
                            }
                        case 1:
                            if (kind > 42) {
                                kind = 42;
                                continue;
                            } else {
                                continue;
                            }
                        case 2:
                            if (kind > 45) {
                                kind = 45;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else {
                int i22 = (this.curChar & 255) >> 6;
                long l2 = 1 << (this.curChar & '?');
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((jjbitVec0[i22] & l2) != 0 && kind > 45) {
                                kind = 45;
                                continue;
                            }
                        case 1:
                            if ((jjbitVec0[i22] & l2) != 0 && kind > 42) {
                                kind = 42;
                                continue;
                            }
                    }
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 3 - startsAt;
            if (i != startsAt) {
                try {
                    this.curChar = this.input_stream.readChar();
                } catch (IOException e) {
                }
            }
            return curPos;
        }
    }

    public DateTimeParserTokenManager(SimpleCharStream stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[4];
        this.jjstateSet = new int[8];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = stream;
    }

    public DateTimeParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        this.jjround = -2147483647;
        int i = 4;
        while (true) {
            int i2 = i;
            i = i2 - 1;
            if (i2 > 0) {
                this.jjrounds[i] = Integer.MIN_VALUE;
            } else {
                return;
            }
        }
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 3 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }

    /* access modifiers changed from: protected */
    public Token jjFillToken() {
        Token t = Token.newToken(this.jjmatchedKind);
        t.kind = this.jjmatchedKind;
        String im = jjstrLiteralImages[this.jjmatchedKind];
        if (im == null) {
            im = this.input_stream.GetImage();
        }
        t.image = im;
        t.beginLine = this.input_stream.getBeginLine();
        t.beginColumn = this.input_stream.getBeginColumn();
        t.endLine = this.input_stream.getEndLine();
        t.endColumn = this.input_stream.getEndColumn();
        return t;
    }

    public Token getNextToken() {
        Token specialToken = null;
        int curPos = 0;
        while (true) {
            try {
                this.curChar = this.input_stream.BeginToken();
                this.image = null;
                this.jjimageLen = 0;
                while (true) {
                    switch (this.curLexState) {
                        case 0:
                            this.jjmatchedKind = Integer.MAX_VALUE;
                            this.jjmatchedPos = 0;
                            curPos = jjMoveStringLiteralDfa0_0();
                            break;
                        case 1:
                            this.jjmatchedKind = Integer.MAX_VALUE;
                            this.jjmatchedPos = 0;
                            curPos = jjMoveStringLiteralDfa0_1();
                            break;
                        case 2:
                            this.jjmatchedKind = Integer.MAX_VALUE;
                            this.jjmatchedPos = 0;
                            curPos = jjMoveStringLiteralDfa0_2();
                            break;
                    }
                    if (this.jjmatchedKind != Integer.MAX_VALUE) {
                        if (this.jjmatchedPos + 1 < curPos) {
                            this.input_stream.backup((curPos - this.jjmatchedPos) - 1);
                        }
                        if ((jjtoToken[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                            Token matchedToken = jjFillToken();
                            matchedToken.specialToken = specialToken;
                            if (jjnewLexState[this.jjmatchedKind] != -1) {
                                this.curLexState = jjnewLexState[this.jjmatchedKind];
                            }
                            return matchedToken;
                        } else if ((jjtoSkip[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                            if ((jjtoSpecial[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                                Token matchedToken2 = jjFillToken();
                                if (specialToken == null) {
                                    specialToken = matchedToken2;
                                } else {
                                    matchedToken2.specialToken = specialToken;
                                    specialToken.next = matchedToken2;
                                    specialToken = matchedToken2;
                                }
                            }
                            if (jjnewLexState[this.jjmatchedKind] != -1) {
                                this.curLexState = jjnewLexState[this.jjmatchedKind];
                            }
                        } else {
                            MoreLexicalActions();
                            if (jjnewLexState[this.jjmatchedKind] != -1) {
                                this.curLexState = jjnewLexState[this.jjmatchedKind];
                            }
                            curPos = 0;
                            this.jjmatchedKind = Integer.MAX_VALUE;
                            try {
                                this.curChar = this.input_stream.readChar();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            } catch (IOException e2) {
                this.jjmatchedKind = 0;
                Token matchedToken3 = jjFillToken();
                matchedToken3.specialToken = specialToken;
                return matchedToken3;
            }
        }
        int error_line = this.input_stream.getEndLine();
        int error_column = this.input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try {
            this.input_stream.readChar();
            this.input_stream.backup(1);
        } catch (IOException e3) {
            EOFSeen = true;
            error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
            if (this.curChar == 10 || this.curChar == 13) {
                error_line++;
                error_column = 0;
            } else {
                error_column++;
            }
        }
        if (!EOFSeen) {
            this.input_stream.backup(1);
            if (curPos <= 1) {
                error_after = "";
            } else {
                error_after = this.input_stream.GetImage();
            }
        }
        throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
    }

    /* access modifiers changed from: package-private */
    public void MoreLexicalActions() {
        int i = this.jjimageLen;
        int i2 = this.jjmatchedPos + 1;
        this.lengthOfMatch = i2;
        this.jjimageLen = i + i2;
        switch (this.jjmatchedKind) {
            case 39:
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.image.deleteCharAt(this.image.length() - 2);
                return;
            case 40:
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                commentNest = 1;
                return;
            case 42:
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.image.deleteCharAt(this.image.length() - 2);
                return;
            case 43:
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                commentNest++;
                return;
            case 44:
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                commentNest--;
                if (commentNest == 0) {
                    SwitchTo(1);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
