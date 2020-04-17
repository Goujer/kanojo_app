package org.jsoup.parser;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class TokenQueue {
    private static final char ESC = '\\';
    private int pos = 0;
    private String queue;

    public TokenQueue(String data) {
        Validate.notNull(data);
        this.queue = data;
    }

    public boolean isEmpty() {
        return remainingLength() == 0;
    }

    private int remainingLength() {
        return this.queue.length() - this.pos;
    }

    public char peek() {
        if (isEmpty()) {
            return 0;
        }
        return this.queue.charAt(this.pos);
    }

    public void addFirst(Character c) {
        addFirst(c.toString());
    }

    public void addFirst(String seq) {
        this.queue = seq + this.queue.substring(this.pos);
        this.pos = 0;
    }

    public boolean matches(String seq) {
        return this.queue.regionMatches(true, this.pos, seq, 0, seq.length());
    }

    public boolean matchesCS(String seq) {
        return this.queue.startsWith(seq, this.pos);
    }

    public boolean matchesAny(String... seq) {
        for (String s : seq) {
            if (matches(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesAny(char... seq) {
        if (isEmpty()) {
            return false;
        }
        for (char c : seq) {
            if (this.queue.charAt(this.pos) == c) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesStartTag() {
        return remainingLength() >= 2 && this.queue.charAt(this.pos) == '<' && Character.isLetter(this.queue.charAt(this.pos + 1));
    }

    public boolean matchChomp(String seq) {
        if (!matches(seq)) {
            return false;
        }
        this.pos += seq.length();
        return true;
    }

    public boolean matchesWhitespace() {
        return !isEmpty() && StringUtil.isWhitespace(this.queue.charAt(this.pos));
    }

    public boolean matchesWord() {
        return !isEmpty() && Character.isLetterOrDigit(this.queue.charAt(this.pos));
    }

    public void advance() {
        if (!isEmpty()) {
            this.pos++;
        }
    }

    public char consume() {
        String str = this.queue;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    public void consume(String seq) {
        if (!matches(seq)) {
            throw new IllegalStateException("Queue did not match expected sequence");
        }
        int len = seq.length();
        if (len > remainingLength()) {
            throw new IllegalStateException("Queue not long enough to consume sequence");
        }
        this.pos += len;
    }

    public String consumeTo(String seq) {
        int offset = this.queue.indexOf(seq, this.pos);
        if (offset == -1) {
            return remainder();
        }
        String consumed = this.queue.substring(this.pos, offset);
        this.pos += consumed.length();
        return consumed;
    }

    public String consumeToIgnoreCase(String seq) {
        int start = this.pos;
        String first = seq.substring(0, 1);
        boolean canScan = first.toLowerCase().equals(first.toUpperCase());
        while (!isEmpty() && !matches(seq)) {
            if (canScan) {
                int skip = this.queue.indexOf(first, this.pos) - this.pos;
                if (skip == 0) {
                    this.pos++;
                } else if (skip < 0) {
                    this.pos = this.queue.length();
                } else {
                    this.pos += skip;
                }
            } else {
                this.pos++;
            }
        }
        return this.queue.substring(start, this.pos);
    }

    public String consumeToAny(String... seq) {
        int start = this.pos;
        while (!isEmpty() && !matchesAny(seq)) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String chompTo(String seq) {
        String data = consumeTo(seq);
        matchChomp(seq);
        return data;
    }

    public String chompToIgnoreCase(String seq) {
        String data = consumeToIgnoreCase(seq);
        matchChomp(seq);
        return data;
    }

    public String chompBalanced(char open, char close) {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        char last = 0;
        while (!isEmpty()) {
            Character c = Character.valueOf(consume());
            if (last == 0 || last != '\\') {
                if (c.equals(Character.valueOf(open))) {
                    depth++;
                } else if (c.equals(Character.valueOf(close))) {
                    depth--;
                }
            }
            if (depth > 0 && last != 0) {
                accum.append(c);
            }
            last = c.charValue();
            if (depth <= 0) {
                break;
            }
        }
        return accum.toString();
    }

    public static String unescape(String in) {
        StringBuilder out = new StringBuilder();
        char last = 0;
        for (char c : in.toCharArray()) {
            if (c != '\\') {
                out.append(c);
            } else if (last != 0 && last == '\\') {
                out.append(c);
            }
            last = c;
        }
        return out.toString();
    }

    public boolean consumeWhitespace() {
        boolean seen = false;
        while (matchesWhitespace()) {
            this.pos++;
            seen = true;
        }
        return seen;
    }

    public String consumeWord() {
        int start = this.pos;
        while (matchesWord()) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String consumeTagName() {
        int start = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny(':', '_', '-'))) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String consumeElementSelector() {
        int start = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('|', '_', '-'))) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String consumeCssIdentifier() {
        int start = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_'))) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String consumeAttributeKey() {
        int start = this.pos;
        while (!isEmpty() && (matchesWord() || matchesAny('-', '_', ':'))) {
            this.pos++;
        }
        return this.queue.substring(start, this.pos);
    }

    public String remainder() {
        StringBuilder accum = new StringBuilder();
        while (!isEmpty()) {
            accum.append(consume());
        }
        return accum.toString();
    }

    public String toString() {
        return this.queue.substring(this.pos);
    }
}
