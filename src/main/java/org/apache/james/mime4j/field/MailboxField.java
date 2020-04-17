package org.apache.james.mime4j.field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.address.AddressList;
import org.apache.james.mime4j.field.address.Mailbox;
import org.apache.james.mime4j.field.address.MailboxList;
import org.apache.james.mime4j.field.address.parser.ParseException;
import org.apache.james.mime4j.util.ByteSequence;

public class MailboxField extends AbstractField {
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new MailboxField(name, body, raw);
        }
    };
    private static Log log = LogFactory.getLog(MailboxField.class);
    private Mailbox mailbox;
    private ParseException parseException;
    private boolean parsed = false;

    MailboxField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public Mailbox getMailbox() {
        if (!this.parsed) {
            parse();
        }
        return this.mailbox;
    }

    public ParseException getParseException() {
        if (!this.parsed) {
            parse();
        }
        return this.parseException;
    }

    private void parse() {
        String body = getBody();
        try {
            MailboxList mailboxList = AddressList.parse(body).flatten();
            if (mailboxList.size() > 0) {
                this.mailbox = mailboxList.get(0);
            }
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e.getMessage());
            }
            this.parseException = e;
        }
        this.parsed = true;
    }
}
