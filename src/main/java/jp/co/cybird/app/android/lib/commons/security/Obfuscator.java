package jp.co.cybird.app.android.lib.commons.security;

public interface Obfuscator {
    String obfuscate(String str, String str2);

    String unobfuscate(String str, String str2) throws ValidationException;
}
