package org.jsoup.helper;

public final class Validate {
    private Validate() {
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
    }

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isTrue(boolean val) {
        if (!val) {
            throw new IllegalArgumentException("Must be true");
        }
    }

    public static void isTrue(boolean val, String msg) {
        if (!val) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isFalse(boolean val) {
        if (val) {
            throw new IllegalArgumentException("Must be false");
        }
    }

    public static void isFalse(boolean val, String msg) {
        if (val) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void noNullElements(Object[] objects) {
        noNullElements(objects, "Array must not contain any null objects");
    }

    public static void noNullElements(Object[] objects, String msg) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static void notEmpty(String string) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException("String must not be empty");
        }
    }

    public static void notEmpty(String string, String msg) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void fail(String msg) {
        throw new IllegalArgumentException(msg);
    }
}
