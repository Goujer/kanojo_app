package org.jsoup.parser;

import org.apache.james.mime4j.field.datetime.parser.DateTimeParserConstants;
import org.jsoup.parser.Token;

enum TokeniserState {
    Data {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    t.emit(r.consume());
                    return;
                case DateTimeParserConstants.COMMENT:
                    t.advanceTransition(CharacterReferenceInData);
                    return;
                case '<':
                    t.advanceTransition(TagOpen);
                    return;
                case 65535:
                    t.emit((Token) new Token.EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('&', '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    CharacterReferenceInData {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char[] c = t.consumeCharacterReference((Character) null, false);
            if (c == null) {
                t.emit('&');
            } else {
                t.emit(c);
            }
            t.transition(Data);
        }
    },
    Rcdata {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case DateTimeParserConstants.COMMENT:
                    t.advanceTransition(CharacterReferenceInRcdata);
                    return;
                case '<':
                    t.advanceTransition(RcdataLessthanSign);
                    return;
                case 65535:
                    t.emit((Token) new Token.EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('&', '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    CharacterReferenceInRcdata {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char[] c = t.consumeCharacterReference((Character) null, false);
            if (c == null) {
                t.emit('&');
            } else {
                t.emit(c);
            }
            t.transition(Rcdata);
        }
    },
    Rawtext {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '<':
                    t.advanceTransition(RawtextLessthanSign);
                    return;
                case 65535:
                    t.emit((Token) new Token.EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptData {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '<':
                    t.advanceTransition(ScriptDataLessthanSign);
                    return;
                case 65535:
                    t.emit((Token) new Token.EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    PLAINTEXT {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case 65535:
                    t.emit((Token) new Token.EOF());
                    return;
                default:
                    t.emit(r.consumeTo((char) TokeniserState.nullChar));
                    return;
            }
        }
    },
    TagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '!':
                    t.advanceTransition(MarkupDeclarationOpen);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.advanceTransition(EndTagOpen);
                    return;
                case '?':
                    t.advanceTransition(BogusComment);
                    return;
                default:
                    if (r.matchesLetter()) {
                        t.createTagPending(true);
                        t.transition(TagName);
                        return;
                    }
                    t.error((TokeniserState) this);
                    t.emit('<');
                    t.transition(Data);
                    return;
            }
        }
    },
    EndTagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.emit("</");
                t.transition(Data);
            } else if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(TagName);
            } else if (r.matches('>')) {
                t.error((TokeniserState) this);
                t.advanceTransition(Data);
            } else {
                t.error((TokeniserState) this);
                t.advanceTransition(BogusComment);
            }
        }
    },
    TagName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            t.tagPending.appendTagName(r.consumeToAny(9, 10, 13, 12, ' ', '/', '>', TokeniserState.nullChar).toLowerCase());
            switch (r.consume()) {
                case 0:
                    t.tagPending.appendTagName(TokeniserState.replacementStr);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    RcdataLessthanSign {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matches('/')) {
                t.createTempBuffer();
                t.advanceTransition(RCDATAEndTagOpen);
            } else if (!r.matchesLetter() || r.containsIgnoreCase("</" + t.appropriateEndTagName())) {
                t.emit("<");
                t.transition(Rcdata);
            } else {
                t.tagPending = new Token.EndTag(t.appropriateEndTagName());
                t.emitTagPending();
                r.unconsume();
                t.transition(Data);
            }
        }
    },
    RCDATAEndTagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(Character.toLowerCase(r.current()));
                t.dataBuffer.append(Character.toLowerCase(r.current()));
                t.advanceTransition(RCDATAEndTagName);
                return;
            }
            t.emit("</");
            t.transition(Rcdata);
        }
    },
    RCDATAEndTagName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    if (t.isAppropriateEndTagToken()) {
                        t.transition(BeforeAttributeName);
                        return;
                    } else {
                        anythingElse(t, r);
                        return;
                    }
                case DateTimeParserConstants.QUOTEDPAIR:
                    if (t.isAppropriateEndTagToken()) {
                        t.transition(SelfClosingStartTag);
                        return;
                    } else {
                        anythingElse(t, r);
                        return;
                    }
                case '>':
                    if (t.isAppropriateEndTagToken()) {
                        t.emitTagPending();
                        t.transition(Data);
                        return;
                    }
                    anythingElse(t, r);
                    return;
                default:
                    anythingElse(t, r);
                    return;
            }
        }

        private void anythingElse(Tokeniser t, CharacterReader r) {
            t.emit("</" + t.dataBuffer.toString());
            r.unconsume();
            t.transition(Rcdata);
        }
    },
    RawtextLessthanSign {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matches('/')) {
                t.createTempBuffer();
                t.advanceTransition(RawtextEndTagOpen);
                return;
            }
            t.emit('<');
            t.transition(Rawtext);
        }
    },
    RawtextEndTagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(RawtextEndTagName);
                return;
            }
            t.emit("</");
            t.transition(Rawtext);
        }
    },
    RawtextEndTagName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
            } else if (!t.isAppropriateEndTagToken() || r.isEmpty()) {
                anythingElse(t, r);
            } else {
                char c = r.consume();
                switch (c) {
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                        t.transition(BeforeAttributeName);
                        return;
                    case DateTimeParserConstants.QUOTEDPAIR:
                        t.transition(SelfClosingStartTag);
                        return;
                    case '>':
                        t.emitTagPending();
                        t.transition(Data);
                        return;
                    default:
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                        return;
                }
            }
        }

        private void anythingElse(Tokeniser t, CharacterReader r) {
            t.emit("</" + t.dataBuffer.toString());
            t.transition(Rawtext);
        }
    },
    ScriptDataLessthanSign {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '!':
                    t.emit("<!");
                    t.transition(ScriptDataEscapeStart);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.createTempBuffer();
                    t.transition(ScriptDataEndTagOpen);
                    return;
                default:
                    t.emit("<");
                    r.unconsume();
                    t.transition(ScriptData);
                    return;
            }
        }
    },
    ScriptDataEndTagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(ScriptDataEndTagName);
                return;
            }
            t.emit("</");
            t.transition(ScriptData);
        }
    },
    ScriptDataEndTagName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
            } else if (!t.isAppropriateEndTagToken() || r.isEmpty()) {
                anythingElse(t, r);
            } else {
                char c = r.consume();
                switch (c) {
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                        t.transition(BeforeAttributeName);
                        return;
                    case DateTimeParserConstants.QUOTEDPAIR:
                        t.transition(SelfClosingStartTag);
                        return;
                    case '>':
                        t.emitTagPending();
                        t.transition(Data);
                        return;
                    default:
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                        return;
                }
            }
        }

        private void anythingElse(Tokeniser t, CharacterReader r) {
            t.emit("</" + t.dataBuffer.toString());
            t.transition(ScriptData);
        }
    },
    ScriptDataEscapeStart {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matches('-')) {
                t.emit('-');
                t.advanceTransition(ScriptDataEscapeStartDash);
                return;
            }
            t.transition(ScriptData);
        }
    },
    ScriptDataEscapeStartDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matches('-')) {
                t.emit('-');
                t.advanceTransition(ScriptDataEscapedDashDash);
                return;
            }
            t.transition(ScriptData);
        }
    },
    ScriptDataEscaped {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '-':
                    t.emit('-');
                    t.advanceTransition(ScriptDataEscapedDash);
                    return;
                case '<':
                    t.advanceTransition(ScriptDataEscapedLessthanSign);
                    return;
                default:
                    t.emit(r.consumeToAny('-', '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptDataEscapedDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataEscaped);
                    return;
                case '-':
                    t.emit(c);
                    t.transition(ScriptDataEscapedDashDash);
                    return;
                case '<':
                    t.transition(ScriptDataEscapedLessthanSign);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataEscaped);
                    return;
            }
        }
    },
    ScriptDataEscapedDashDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataEscaped);
                    return;
                case '-':
                    t.emit(c);
                    return;
                case '<':
                    t.transition(ScriptDataEscapedLessthanSign);
                    return;
                case '>':
                    t.emit(c);
                    t.transition(ScriptData);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataEscaped);
                    return;
            }
        }
    },
    ScriptDataEscapedLessthanSign {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTempBuffer();
                t.dataBuffer.append(Character.toLowerCase(r.current()));
                t.emit("<" + r.current());
                t.advanceTransition(ScriptDataDoubleEscapeStart);
            } else if (r.matches('/')) {
                t.createTempBuffer();
                t.advanceTransition(ScriptDataEscapedEndTagOpen);
            } else {
                t.emit('<');
                t.transition(ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedEndTagOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(Character.toLowerCase(r.current()));
                t.dataBuffer.append(r.current());
                t.advanceTransition(ScriptDataEscapedEndTagName);
                return;
            }
            t.emit("</");
            t.transition(ScriptDataEscaped);
        }
    },
    ScriptDataEscapedEndTagName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
            } else if (!t.isAppropriateEndTagToken() || r.isEmpty()) {
                anythingElse(t, r);
            } else {
                char c = r.consume();
                switch (c) {
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                        t.transition(BeforeAttributeName);
                        return;
                    case DateTimeParserConstants.QUOTEDPAIR:
                        t.transition(SelfClosingStartTag);
                        return;
                    case '>':
                        t.emitTagPending();
                        t.transition(Data);
                        return;
                    default:
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                        return;
                }
            }
        }

        private void anythingElse(Tokeniser t, CharacterReader r) {
            t.emit("</" + t.dataBuffer.toString());
            t.transition(ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscapeStart {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.dataBuffer.append(name.toLowerCase());
                t.emit(name);
                return;
            }
            char c = r.consume();
            switch (c) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                case DateTimeParserConstants.QUOTEDPAIR:
                case '>':
                    if (t.dataBuffer.toString().equals("script")) {
                        t.transition(ScriptDataDoubleEscaped);
                    } else {
                        t.transition(ScriptDataEscaped);
                    }
                    t.emit(c);
                    return;
                default:
                    r.unconsume();
                    t.transition(ScriptDataEscaped);
                    return;
            }
        }
    },
    ScriptDataDoubleEscaped {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.current();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '-':
                    t.emit(c);
                    t.advanceTransition(ScriptDataDoubleEscapedDash);
                    return;
                case '<':
                    t.emit(c);
                    t.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(r.consumeToAny('-', '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
                case '-':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedDashDash);
                    return;
                case '<':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedDashDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
                case '-':
                    t.emit(c);
                    return;
                case '<':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case '>':
                    t.emit(c);
                    t.transition(ScriptData);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedLessthanSign {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matches('/')) {
                t.emit('/');
                t.createTempBuffer();
                t.advanceTransition(ScriptDataDoubleEscapeEnd);
                return;
            }
            t.transition(ScriptDataDoubleEscaped);
        }
    },
    ScriptDataDoubleEscapeEnd {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.dataBuffer.append(name.toLowerCase());
                t.emit(name);
                return;
            }
            char c = r.consume();
            switch (c) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                case DateTimeParserConstants.QUOTEDPAIR:
                case '>':
                    if (t.dataBuffer.toString().equals("script")) {
                        t.transition(ScriptDataEscaped);
                    } else {
                        t.transition(ScriptDataDoubleEscaped);
                    }
                    t.emit(c);
                    return;
                default:
                    r.unconsume();
                    t.transition(ScriptDataDoubleEscaped);
                    return;
            }
        }
    },
    BeforeAttributeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(AttributeName);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
            }
        }
    },
    AttributeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            t.tagPending.appendAttributeName(r.consumeToAny(9, 10, 13, 12, ' ', '/', '=', '>', TokeniserState.nullChar, '\"', '\'', '<').toLowerCase());
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(AfterAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName(c);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.transition(SelfClosingStartTag);
                    return;
                case '=':
                    t.transition(BeforeAttributeValue);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    t.transition(AttributeName);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(AttributeName);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.transition(SelfClosingStartTag);
                    return;
                case '=':
                    t.transition(BeforeAttributeValue);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
            }
        }
    },
    BeforeAttributeValue {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    t.transition(AttributeValue_unquoted);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                    t.transition(AttributeValue_doubleQuoted);
                    return;
                case DateTimeParserConstants.COMMENT:
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
                    return;
                case '\'':
                    t.transition(AttributeValue_singleQuoted);
                    return;
                case '<':
                case '=':
                case '`':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue(c);
                    t.transition(AttributeValue_unquoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
                    return;
            }
        }
    },
    AttributeValue_doubleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAny('\"', '&', TokeniserState.nullChar);
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            }
            switch (r.consume()) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterAttributeValue_quoted);
                    return;
                case DateTimeParserConstants.COMMENT:
                    char[] ref = t.consumeCharacterReference('\"', true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AttributeValue_singleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAny('\'', '&', TokeniserState.nullChar);
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            }
            switch (r.consume()) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case DateTimeParserConstants.COMMENT:
                    char[] ref = t.consumeCharacterReference('\'', true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case '\'':
                    t.transition(AfterAttributeValue_quoted);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AttributeValue_unquoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAny(9, 10, 13, 12, ' ', '&', '>', TokeniserState.nullChar, '\"', '\'', '<', '=', '`');
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            }
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                case '`':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue(c);
                    return;
                case DateTimeParserConstants.COMMENT:
                    char[] ref = t.consumeCharacterReference('>', true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeValue_quoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case DateTimeParserConstants.QUOTEDPAIR:
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    r.unconsume();
                    t.transition(BeforeAttributeName);
                    return;
            }
        }
    },
    SelfClosingStartTag {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '>':
                    t.tagPending.selfClosing = true;
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BeforeAttributeName);
                    return;
            }
        }
    },
    BogusComment {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            r.unconsume();
            Token.Comment comment = new Token.Comment();
            comment.bogus = true;
            comment.data.append(r.consumeTo('>'));
            t.emit((Token) comment);
            t.advanceTransition(Data);
        }
    },
    MarkupDeclarationOpen {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchConsume("--")) {
                t.createCommentPending();
                t.transition(CommentStart);
            } else if (r.matchConsumeIgnoreCase("DOCTYPE")) {
                t.transition(Doctype);
            } else if (r.matchConsume("[CDATA[")) {
                t.transition(CdataSection);
            } else {
                t.error((TokeniserState) this);
                t.advanceTransition(BogusComment);
            }
        }
    },
    CommentStart {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentStartDash);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentStartDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentStartDash);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    Comment {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case 0:
                    t.error((TokeniserState) this);
                    r.advance();
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    return;
                case '-':
                    t.advanceTransition(CommentEndDash);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(r.consumeToAny('-', TokeniserState.nullChar));
                    return;
            }
        }
    },
    CommentEndDash {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append('-').append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentEnd);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append('-').append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentEnd {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--").append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '!':
                    t.error((TokeniserState) this);
                    t.transition(CommentEndBang);
                    return;
                case '-':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append('-');
                    return;
                case '>':
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--").append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentEndBang {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--!").append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.commentPending.data.append("--!");
                    t.transition(CommentEndDash);
                    return;
                case '>':
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append("--!").append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    Doctype {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeDoctypeName);
                    return;
                case 65535:
                    t.eofError(this);
                    t.createDoctypePending();
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BeforeDoctypeName);
                    return;
            }
        }
    },
    BeforeDoctypeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createDoctypePending();
                t.transition(DoctypeName);
                return;
            }
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    t.transition(DoctypeName);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case 65535:
                    t.eofError(this);
                    t.createDoctypePending();
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.createDoctypePending();
                    t.doctypePending.name.append(c);
                    t.transition(DoctypeName);
                    return;
            }
        }
    },
    DoctypeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.doctypePending.name.append(r.consumeLetterSequence().toLowerCase());
                return;
            }
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(AfterDoctypeName);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.name.append(c);
                    return;
            }
        }
    },
    AfterDoctypeName {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.doctypePending.forceQuirks = true;
                t.emitDoctypePending();
                t.transition(Data);
            } else if (r.matchesAny(9, 10, 13, 12, ' ')) {
                r.advance();
            } else if (r.matches('>')) {
                t.emitDoctypePending();
                t.advanceTransition(Data);
            } else if (r.matchConsumeIgnoreCase("PUBLIC")) {
                t.transition(AfterDoctypePublicKeyword);
            } else if (r.matchConsumeIgnoreCase("SYSTEM")) {
                t.transition(AfterDoctypeSystemKeyword);
            } else {
                t.error((TokeniserState) this);
                t.doctypePending.forceQuirks = true;
                t.advanceTransition(BogusDoctype);
            }
        }
    },
    AfterDoctypePublicKeyword {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeDoctypePublicIdentifier);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BeforeDoctypePublicIdentifier {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                    t.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypePublicIdentifier_doubleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterDoctypePublicIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.publicIdentifier.append(c);
                    return;
            }
        }
    },
    DoctypePublicIdentifier_singleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\'':
                    t.transition(AfterDoctypePublicIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.publicIdentifier.append(c);
                    return;
            }
        }
    },
    AfterDoctypePublicIdentifier {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BetweenDoctypePublicAndSystemIdentifiers);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BetweenDoctypePublicAndSystemIdentifiers {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    AfterDoctypeSystemKeyword {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    t.transition(BeforeDoctypeSystemIdentifier);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    return;
            }
        }
    },
    BeforeDoctypeSystemIdentifier {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '\"':
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypeSystemIdentifier_doubleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterDoctypeSystemIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.systemIdentifier.append(c);
                    return;
            }
        }
    },
    DoctypeSystemIdentifier_singleQuoted {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case 0:
                    t.error((TokeniserState) this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\'':
                    t.transition(AfterDoctypeSystemIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.systemIdentifier.append(c);
                    return;
            }
        }
    },
    AfterDoctypeSystemIdentifier {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BogusDoctype {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case 65535:
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    CdataSection {
        /* access modifiers changed from: package-private */
        public void read(Tokeniser t, CharacterReader r) {
            t.emit(r.consumeTo("]]>"));
            r.matchConsume("]]>");
            t.transition(Data);
        }
    };
    
    private static final char eof = '￿';
    private static final char nullChar = '\u0000';
    private static final char replacementChar = '�';
    /* access modifiers changed from: private */
    public static final String replacementStr = null;

    /* access modifiers changed from: package-private */
    public abstract void read(Tokeniser tokeniser, CharacterReader characterReader);

    static {
        replacementStr = String.valueOf(replacementChar);
    }
}
