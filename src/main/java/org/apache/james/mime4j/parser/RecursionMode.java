package org.apache.james.mime4j.parser;

public interface RecursionMode {
    public static final int M_FLAT = 3;
    public static final int M_NO_RECURSE = 1;
    public static final int M_RAW = 2;
    public static final int M_RECURSE = 0;
}
