package com.facebook.internal;

import java.util.Collection;

public final class Validate {
    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument " + name + " cannot be null");
        }
    }

    public static <T> void notEmpty(Collection<T> container, String name) {
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
        }
    }

    public static <T> void containsNoNulls(Collection<T> container, String name) {
        notNull(container, name);
        for (T t : container) {
            if (t == null) {
                throw new NullPointerException("Container '" + name + "' cannot contain null values");
            }
        }
    }

    public static <T> void notEmptyAndContainsNoNulls(Collection<T> container, String name) {
        containsNoNulls(container, name);
        notEmpty(container, name);
    }

    public static void notNullOrEmpty(String arg, String name) {
        if (Utility.isNullOrEmpty(arg)) {
            throw new IllegalArgumentException("Argument " + name + " cannot be null or empty");
        }
    }

    public static void oneOf(Object arg, String name, Object... values) {
        for (Object value : values) {
            if (value != null) {
                if (value.equals(arg)) {
                    return;
                }
            } else if (arg == null) {
                return;
            }
        }
        throw new IllegalArgumentException("Argument " + name + " was not one of the allowed values");
    }
}
