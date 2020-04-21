package org.apache.james.mime4j.field.address;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import org.apache.james.mime4j.field.address.parser.AddressListParser;
import org.apache.james.mime4j.field.address.parser.ParseException;

public abstract class Address implements Serializable {
    private static final long serialVersionUID = 634090661990433426L;

    /* access modifiers changed from: protected */
    public abstract void doAddMailboxesTo(List<Mailbox> list);

    public abstract String getDisplayString(boolean z);

    public abstract String getEncodedString();

    /* access modifiers changed from: package-private */
    public final void addMailboxesTo(List<Mailbox> results) {
        doAddMailboxesTo(results);
    }

    public final String getDisplayString() {
        return getDisplayString(false);
    }

    public static Address parse(String rawAddressString) {
        try {
            return Builder.getInstance().buildAddress(new AddressListParser((Reader) new StringReader(rawAddressString)).parseAddress());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String toString() {
        return getDisplayString(false);
    }
}
