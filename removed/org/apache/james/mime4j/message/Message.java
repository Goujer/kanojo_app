package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.field.AddressListField;
import org.apache.james.mime4j.field.DateTimeField;
import org.apache.james.mime4j.field.FieldName;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.field.MailboxField;
import org.apache.james.mime4j.field.MailboxListField;
import org.apache.james.mime4j.field.UnstructuredField;
import org.apache.james.mime4j.field.address.Address;
import org.apache.james.mime4j.field.address.AddressList;
import org.apache.james.mime4j.field.address.Mailbox;
import org.apache.james.mime4j.field.address.MailboxList;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.parser.MimeEntityConfig;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.storage.DefaultStorageProvider;
import org.apache.james.mime4j.storage.StorageProvider;

public class Message extends Entity implements Body {
    public Message() {
    }

    public Message(Message other) {
        super(other);
    }

    public Message(InputStream is) throws IOException, MimeIOException {
        this(is, (MimeEntityConfig) null, DefaultStorageProvider.getInstance());
    }

    public Message(InputStream is, MimeEntityConfig config) throws IOException, MimeIOException {
        this(is, config, DefaultStorageProvider.getInstance());
    }

    public Message(InputStream is, MimeEntityConfig config, StorageProvider storageProvider) throws IOException, MimeIOException {
        try {
            MimeStreamParser parser = new MimeStreamParser(config);
            parser.setContentHandler(new MessageBuilder(this, storageProvider));
            parser.parse(is);
        } catch (MimeException e) {
            throw new MimeIOException(e);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        MessageWriter.DEFAULT.writeEntity(this, out);
    }

    public String getMessageId() {
        Field field = obtainField(FieldName.MESSAGE_ID);
        if (field == null) {
            return null;
        }
        return field.getBody();
    }

    public void createMessageId(String hostname) {
        obtainHeader().setField(Fields.messageId(hostname));
    }

    public String getSubject() {
        UnstructuredField field = (UnstructuredField) obtainField(FieldName.SUBJECT);
        if (field == null) {
            return null;
        }
        return field.getValue();
    }

    public void setSubject(String subject) {
        Header header = obtainHeader();
        if (subject == null) {
            header.removeFields(FieldName.SUBJECT);
        } else {
            header.setField(Fields.subject(subject));
        }
    }

    public Date getDate() {
        DateTimeField dateField = (DateTimeField) obtainField(FieldName.DATE);
        if (dateField == null) {
            return null;
        }
        return dateField.getDate();
    }

    public void setDate(Date date) {
        setDate(date, (TimeZone) null);
    }

    public void setDate(Date date, TimeZone zone) {
        Header header = obtainHeader();
        if (date == null) {
            header.removeFields(FieldName.DATE);
        } else {
            header.setField(Fields.date(FieldName.DATE, date, zone));
        }
    }

    public Mailbox getSender() {
        return getMailbox(FieldName.SENDER);
    }

    public void setSender(Mailbox sender) {
        setMailbox(FieldName.SENDER, sender);
    }

    public MailboxList getFrom() {
        return getMailboxList(FieldName.FROM);
    }

    public void setFrom(Mailbox from) {
        setMailboxList(FieldName.FROM, from);
    }

    public void setFrom(Mailbox... from) {
        setMailboxList(FieldName.FROM, from);
    }

    public void setFrom(Collection<Mailbox> from) {
        setMailboxList(FieldName.FROM, from);
    }

    public AddressList getTo() {
        return getAddressList(FieldName.TO);
    }

    public void setTo(Address to) {
        setAddressList(FieldName.TO, to);
    }

    public void setTo(Address... to) {
        setAddressList(FieldName.TO, to);
    }

    public void setTo(Collection<Address> to) {
        setAddressList(FieldName.TO, to);
    }

    public AddressList getCc() {
        return getAddressList(FieldName.CC);
    }

    public void setCc(Address cc) {
        setAddressList(FieldName.CC, cc);
    }

    public void setCc(Address... cc) {
        setAddressList(FieldName.CC, cc);
    }

    public void setCc(Collection<Address> cc) {
        setAddressList(FieldName.CC, cc);
    }

    public AddressList getBcc() {
        return getAddressList(FieldName.BCC);
    }

    public void setBcc(Address bcc) {
        setAddressList(FieldName.BCC, bcc);
    }

    public void setBcc(Address... bcc) {
        setAddressList(FieldName.BCC, bcc);
    }

    public void setBcc(Collection<Address> bcc) {
        setAddressList(FieldName.BCC, bcc);
    }

    public AddressList getReplyTo() {
        return getAddressList(FieldName.REPLY_TO);
    }

    public void setReplyTo(Address replyTo) {
        setAddressList(FieldName.REPLY_TO, replyTo);
    }

    public void setReplyTo(Address... replyTo) {
        setAddressList(FieldName.REPLY_TO, replyTo);
    }

    public void setReplyTo(Collection<Address> replyTo) {
        setAddressList(FieldName.REPLY_TO, replyTo);
    }

    private Mailbox getMailbox(String fieldName) {
        MailboxField field = (MailboxField) obtainField(fieldName);
        if (field == null) {
            return null;
        }
        return field.getMailbox();
    }

    private void setMailbox(String fieldName, Mailbox mailbox) {
        Header header = obtainHeader();
        if (mailbox == null) {
            header.removeFields(fieldName);
        } else {
            header.setField(Fields.mailbox(fieldName, mailbox));
        }
    }

    private MailboxList getMailboxList(String fieldName) {
        MailboxListField field = (MailboxListField) obtainField(fieldName);
        if (field == null) {
            return null;
        }
        return field.getMailboxList();
    }

    private void setMailboxList(String fieldName, Mailbox mailbox) {
        setMailboxList(fieldName, (Collection<Mailbox>) mailbox == null ? null : Collections.singleton(mailbox));
    }

    private void setMailboxList(String fieldName, Mailbox... mailboxes) {
        setMailboxList(fieldName, (Collection<Mailbox>) mailboxes == null ? null : Arrays.asList(mailboxes));
    }

    private void setMailboxList(String fieldName, Collection<Mailbox> mailboxes) {
        Header header = obtainHeader();
        if (mailboxes == null || mailboxes.isEmpty()) {
            header.removeFields(fieldName);
        } else {
            header.setField(Fields.mailboxList(fieldName, mailboxes));
        }
    }

    private AddressList getAddressList(String fieldName) {
        AddressListField field = (AddressListField) obtainField(fieldName);
        if (field == null) {
            return null;
        }
        return field.getAddressList();
    }

    private void setAddressList(String fieldName, Address address) {
        setAddressList(fieldName, (Collection<Address>) address == null ? null : Collections.singleton(address));
    }

    private void setAddressList(String fieldName, Address... addresses) {
        setAddressList(fieldName, (Collection<Address>) addresses == null ? null : Arrays.asList(addresses));
    }

    private void setAddressList(String fieldName, Collection<Address> addresses) {
        Header header = obtainHeader();
        if (addresses == null || addresses.isEmpty()) {
            header.removeFields(fieldName);
        } else {
            header.setField(Fields.addressList(fieldName, addresses));
        }
    }
}
