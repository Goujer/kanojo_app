package twitter4j.util;

public final class CharacterUtil {
    private CharacterUtil() {
        throw new AssertionError();
    }

    public static int count(String text) {
        return text.length();
    }

    public static boolean isExceedingLengthLimitation(String text) {
        return count(text) > 140;
    }
}
