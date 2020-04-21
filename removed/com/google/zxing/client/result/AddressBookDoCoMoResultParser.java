package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
    public AddressBookParsedResult parse(Result result) {
        String[] rawName;
        String rawText = getMassagedText(result);
        if (!rawText.startsWith("MECARD:") || (rawName = matchDoCoMoPrefixedField("N:", rawText, true)) == null) {
            return null;
        }
        String name = parseName(rawName[0]);
        String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawText, true);
        String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawText, true);
        String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawText, true);
        String note = matchSingleDoCoMoPrefixedField("NOTE:", rawText, false);
        String[] addresses = matchDoCoMoPrefixedField("ADR:", rawText, true);
        String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawText, true);
        if (birthday != null && !isStringOfDigits(birthday, 8)) {
            birthday = null;
        }
        String[] urls = matchDoCoMoPrefixedField("URL:", rawText, true);
        return new AddressBookParsedResult(maybeWrap(name), (String[]) null, pronunciation, phoneNumbers, (String[]) null, emails, (String[]) null, (String) null, note, addresses, (String[]) null, matchSingleDoCoMoPrefixedField("ORG:", rawText, true), birthday, (String) null, urls, (String[]) null);
    }

    private static String parseName(String name) {
        int comma = name.indexOf(44);
        if (comma >= 0) {
            return name.substring(comma + 1) + ' ' + name.substring(0, comma);
        }
        return name;
    }
}
