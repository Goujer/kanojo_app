package org.apache.james.mime4j.parser;

public final class Event {
    public static final Event HEADERS_PREMATURE_END = new Event("Unexpected end of headers detected. Higher level boundary detected or EOF reached.");
    public static final Event INALID_HEADER = new Event("Invalid header encountered");
    public static final Event MIME_BODY_PREMATURE_END = new Event("Body part ended prematurely. Boundary detected in header or EOF reached.");
    private final String code;

    public Event(String code2) {
        if (code2 == null) {
            throw new IllegalArgumentException("Code may not be null");
        }
        this.code = code2;
    }

    public int hashCode() {
        return this.code.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Event) {
            return this.code.equals(((Event) obj).code);
        }
        return false;
    }

    public String toString() {
        return this.code;
    }
}
