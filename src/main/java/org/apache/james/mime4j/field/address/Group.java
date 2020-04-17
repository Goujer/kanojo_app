package org.apache.james.mime4j.field.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.james.mime4j.codec.EncoderUtil;

public class Group extends Address {
    private static final long serialVersionUID = 1;
    private final MailboxList mailboxList;
    private final String name;

    public Group(String name2, Mailbox... mailboxes) {
        this(name2, new MailboxList(Arrays.asList(mailboxes), true));
    }

    public Group(String name2, Collection<Mailbox> mailboxes) {
        this(name2, new MailboxList(new ArrayList(mailboxes), true));
    }

    public Group(String name2, MailboxList mailboxes) {
        if (name2 == null) {
            throw new IllegalArgumentException();
        } else if (mailboxes == null) {
            throw new IllegalArgumentException();
        } else {
            this.name = name2;
            this.mailboxList = mailboxes;
        }
    }

    public static Group parse(String rawGroupString) {
        Address address = Address.parse(rawGroupString);
        if (address instanceof Group) {
            return (Group) address;
        }
        throw new IllegalArgumentException("Not a group address");
    }

    public String getName() {
        return this.name;
    }

    public MailboxList getMailboxes() {
        return this.mailboxList;
    }

    public String getDisplayString(boolean includeRoute) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(':');
        boolean first = true;
        Iterator i$ = this.mailboxList.iterator();
        while (i$.hasNext()) {
            Mailbox mailbox = (Mailbox) i$.next();
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(' ');
            sb.append(mailbox.getDisplayString(includeRoute));
        }
        sb.append(";");
        return sb.toString();
    }

    public String getEncodedString() {
        StringBuilder sb = new StringBuilder();
        sb.append(EncoderUtil.encodeAddressDisplayName(this.name));
        sb.append(':');
        boolean first = true;
        Iterator i$ = this.mailboxList.iterator();
        while (i$.hasNext()) {
            Mailbox mailbox = (Mailbox) i$.next();
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(' ');
            sb.append(mailbox.getEncodedString());
        }
        sb.append(';');
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void doAddMailboxesTo(List<Mailbox> results) {
        Iterator i$ = this.mailboxList.iterator();
        while (i$.hasNext()) {
            results.add((Mailbox) i$.next());
        }
    }
}
