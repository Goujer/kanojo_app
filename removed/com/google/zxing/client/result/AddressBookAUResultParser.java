package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;
import org.apache.james.mime4j.util.CharsetUtil;

public final class AddressBookAUResultParser extends ResultParser {
    public AddressBookParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!rawText.contains("MEMORY") || !rawText.contains(CharsetUtil.CRLF)) {
            return null;
        }
        String name = matchSinglePrefixedField("NAME1:", rawText, 13, true);
        String pronunciation = matchSinglePrefixedField("NAME2:", rawText, 13, true);
        String[] phoneNumbers = matchMultipleValuePrefix("TEL", 3, rawText, true);
        String[] emails = matchMultipleValuePrefix("MAIL", 3, rawText, true);
        String note = matchSinglePrefixedField("MEMORY:", rawText, 13, false);
        String address = matchSinglePrefixedField("ADD:", rawText, 13, true);
        return new AddressBookParsedResult(maybeWrap(name), (String[]) null, pronunciation, phoneNumbers, (String[]) null, emails, (String[]) null, (String) null, note, address == null ? null : new String[]{address}, (String[]) null, (String) null, (String) null, (String) null, (String[]) null, (String[]) null);
    }

    private static String[] matchMultipleValuePrefix(String prefix, int max, String rawText, boolean trim) {
        List<String> values = null;
        for (int i = 1; i <= max; i++) {
            String value = matchSinglePrefixedField(prefix + i + ':', rawText, 13, trim);
            if (value == null) {
                break;
            }
            if (values == null) {
                values = new ArrayList<>(max);
            }
            values.add(value);
        }
        if (values == null) {
            return null;
        }
        return (String[]) values.toArray(new String[values.size()]);
    }
}
