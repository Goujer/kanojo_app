package org.apache.james.mime4j.field.address;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.field.address.parser.AddressListParser;
import org.apache.james.mime4j.field.address.parser.ParseException;

public class Mailbox extends Address {
    private static final DomainList EMPTY_ROUTE_LIST = new DomainList(Collections.emptyList(), true);
    private static final long serialVersionUID = 1;
    private final String domain;
    private final String localPart;
    private final String name;
    private final DomainList route;

    public Mailbox(String localPart2, String domain2) {
        this((String) null, (DomainList) null, localPart2, domain2);
    }

    public Mailbox(DomainList route2, String localPart2, String domain2) {
        this((String) null, route2, localPart2, domain2);
    }

    public Mailbox(String name2, String localPart2, String domain2) {
        this(name2, (DomainList) null, localPart2, domain2);
    }

    public Mailbox(String name2, DomainList route2, String localPart2, String domain2) {
        if (localPart2 == null || localPart2.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.name = (name2 == null || name2.length() == 0) ? null : name2;
        this.route = route2 == null ? EMPTY_ROUTE_LIST : route2;
        this.localPart = localPart2;
        this.domain = (domain2 == null || domain2.length() == 0) ? null : domain2;
    }

    Mailbox(String name2, Mailbox baseMailbox) {
        this(name2, baseMailbox.getRoute(), baseMailbox.getLocalPart(), baseMailbox.getDomain());
    }

    public static Mailbox parse(String rawMailboxString) {
        try {
            return Builder.getInstance().buildMailbox(new AddressListParser((Reader) new StringReader(rawMailboxString)).parseMailbox());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getName() {
        return this.name;
    }

    public DomainList getRoute() {
        return this.route;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        return this.localPart + '@' + this.domain;
    }

    public String getDisplayString(boolean includeRoute) {
        boolean z;
        boolean includeAngleBrackets;
        if (this.route != null) {
            z = true;
        } else {
            z = false;
        }
        boolean includeRoute2 = includeRoute & z;
        if (this.name != null || includeRoute2) {
            includeAngleBrackets = true;
        } else {
            includeAngleBrackets = false;
        }
        StringBuilder sb = new StringBuilder();
        if (this.name != null) {
            sb.append(this.name);
            sb.append(' ');
        }
        if (includeAngleBrackets) {
            sb.append('<');
        }
        if (includeRoute2) {
            sb.append(this.route.toRouteString());
            sb.append(':');
        }
        sb.append(this.localPart);
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (includeAngleBrackets) {
            sb.append('>');
        }
        return sb.toString();
    }

    public String getEncodedString() {
        StringBuilder sb = new StringBuilder();
        if (this.name != null) {
            sb.append(EncoderUtil.encodeAddressDisplayName(this.name));
            sb.append(" <");
        }
        sb.append(EncoderUtil.encodeAddressLocalPart(this.localPart));
        if (this.domain != null) {
            sb.append('@');
            sb.append(this.domain);
        }
        if (this.name != null) {
            sb.append('>');
        }
        return sb.toString();
    }

    public int hashCode() {
        return getCanonicalizedAddress().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Mailbox)) {
            return false;
        }
        return getCanonicalizedAddress().equals(((Mailbox) obj).getCanonicalizedAddress());
    }

    /* access modifiers changed from: protected */
    public final void doAddMailboxesTo(List<Mailbox> results) {
        results.add(this);
    }

    private Object getCanonicalizedAddress() {
        if (this.domain == null) {
            return this.localPart;
        }
        return this.localPart + '@' + this.domain.toLowerCase(Locale.US);
    }
}
