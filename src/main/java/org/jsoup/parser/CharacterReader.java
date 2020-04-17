package org.jsoup.parser;

import java.util.Locale;
import org.jsoup.helper.Validate;

class CharacterReader {
    static final char EOF = '￿';
    private final char[] input;
    private final int length;
    private int mark = 0;
    private int pos = 0;

    CharacterReader(String input2) {
        Validate.notNull(input2);
        this.input = input2.toCharArray();
        this.length = this.input.length;
    }

    /* access modifiers changed from: package-private */
    public int pos() {
        return this.pos;
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.pos >= this.length;
    }

    /* access modifiers changed from: package-private */
    public char current() {
        return isEmpty() ? EOF : this.input[this.pos];
    }

    /* access modifiers changed from: package-private */
    public char consume() {
        char val = isEmpty() ? EOF : this.input[this.pos];
        this.pos++;
        return val;
    }

    /* access modifiers changed from: package-private */
    public void unconsume() {
        this.pos--;
    }

    /* access modifiers changed from: package-private */
    public void advance() {
        this.pos++;
    }

    /* access modifiers changed from: package-private */
    public void mark() {
        this.mark = this.pos;
    }

    /* access modifiers changed from: package-private */
    public void rewindToMark() {
        this.pos = this.mark;
    }

    /* access modifiers changed from: package-private */
    public String consumeAsString() {
        char[] cArr = this.input;
        int i = this.pos;
        this.pos = i + 1;
        return new String(cArr, i, 1);
    }

    /* access modifiers changed from: package-private */
    public int nextIndexOf(char c) {
        for (int i = this.pos; i < this.length; i++) {
            if (c == this.input[i]) {
                return i - this.pos;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int nextIndexOf(CharSequence seq) {
        char startChar = seq.charAt(0);
        int offset = this.pos;
        while (offset < this.length) {
            if (startChar != this.input[offset]) {
                do {
                    offset++;
                    if (offset >= this.length) {
                        break;
                    }
                } while (startChar == this.input[offset]);
            }
            if (offset < this.length) {
                int i = offset + 1;
                int last = (seq.length() + i) - 1;
                int j = 1;
                while (i < last && seq.charAt(j) == this.input[i]) {
                    i++;
                    j++;
                }
                if (i == last) {
                    return offset - this.pos;
                }
            }
            offset++;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public String consumeTo(char c) {
        int offset = nextIndexOf(c);
        if (offset == -1) {
            return consumeToEnd();
        }
        String consumed = new String(this.input, this.pos, offset);
        this.pos += offset;
        return consumed;
    }

    /* access modifiers changed from: package-private */
    public String consumeTo(String seq) {
        int offset = nextIndexOf((CharSequence) seq);
        if (offset == -1) {
            return consumeToEnd();
        }
        String consumed = new String(this.input, this.pos, offset);
        this.pos += offset;
        return consumed;
    }

    /* access modifiers changed from: package-private */
    public String consumeToAny(char... chars) {
        int start = this.pos;
        loop0:
        while (this.pos < this.length) {
            for (char c : chars) {
                if (this.input[this.pos] == c) {
                    break loop0;
                }
            }
            this.pos++;
        }
        if (this.pos > start) {
            return new String(this.input, start, this.pos - start);
        }
        return "";
    }

    /* access modifiers changed from: package-private */
    public String consumeToEnd() {
        String data = new String(this.input, this.pos, this.length - this.pos);
        this.pos = this.length;
        return data;
    }

    /* access modifiers changed from: package-private */
    public String consumeLetterSequence() {
        int start = this.pos;
        while (this.pos < this.length && (((c = this.input[this.pos]) >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
            this.pos++;
        }
        return new String(this.input, start, this.pos - start);
    }

    /* access modifiers changed from: package-private */
    public String consumeLetterThenDigitSequence() {
        int start = this.pos;
        while (this.pos < this.length && (((c = this.input[this.pos]) >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
            this.pos++;
        }
        while (!isEmpty() && (c = this.input[this.pos]) >= '0' && c <= '9') {
            this.pos++;
        }
        return new String(this.input, start, this.pos - start);
    }

    /* access modifiers changed from: package-private */
    public String consumeHexSequence() {
        int start = this.pos;
        while (this.pos < this.length && (((c = this.input[this.pos]) >= '0' && c <= '9') || ((c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')))) {
            this.pos++;
        }
        return new String(this.input, start, this.pos - start);
    }

    /* access modifiers changed from: package-private */
    public String consumeDigitSequence() {
        int start = this.pos;
        while (this.pos < this.length && (c = this.input[this.pos]) >= '0' && c <= '9') {
            this.pos++;
        }
        return new String(this.input, start, this.pos - start);
    }

    /* access modifiers changed from: package-private */
    public boolean matches(char c) {
        return !isEmpty() && this.input[this.pos] == c;
    }

    /* access modifiers changed from: package-private */
    public boolean matches(String seq) {
        int scanLength = seq.length();
        if (scanLength > this.length - this.pos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; offset++) {
            if (seq.charAt(offset) != this.input[this.pos + offset]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesIgnoreCase(String seq) {
        int scanLength = seq.length();
        if (scanLength > this.length - this.pos) {
            return false;
        }
        for (int offset = 0; offset < scanLength; offset++) {
            if (Character.toUpperCase(seq.charAt(offset)) != Character.toUpperCase(this.input[this.pos + offset])) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesAny(char... seq) {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        for (char seek : seq) {
            if (seek == c) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesLetter() {
        if (isEmpty()) {
            return false;
        }
        char c = this.input[this.pos];
        if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchesDigit() {
        char c;
        if (!isEmpty() && (c = this.input[this.pos]) >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean matchConsume(String seq) {
        if (!matches(seq)) {
            return false;
        }
        this.pos += seq.length();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean matchConsumeIgnoreCase(String seq) {
        if (!matchesIgnoreCase(seq)) {
            return false;
        }
        this.pos += seq.length();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean containsIgnoreCase(String seq) {
        return nextIndexOf((CharSequence) seq.toLowerCase(Locale.ENGLISH)) > -1 || nextIndexOf((CharSequence) seq.toUpperCase(Locale.ENGLISH)) > -1;
    }

    public String toString() {
        return new String(this.input, this.pos, this.length - this.pos);
    }
}
