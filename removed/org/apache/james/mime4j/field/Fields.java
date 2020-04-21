package org.apache.james.mime4j.field;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.field.address.Address;
import org.apache.james.mime4j.field.address.Mailbox;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.ContentUtil;
import org.apache.james.mime4j.util.MimeUtil;

public class Fields {
    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("[\\x21-\\x39\\x3b-\\x7e]+");

    private Fields() {
    }

    public static ContentTypeField contentType(String contentType) {
        return (ContentTypeField) parse(ContentTypeField.PARSER, "Content-Type", contentType);
    }

    public static ContentTypeField contentType(String mimeType, Map<String, String> parameters) {
        if (!isValidMimeType(mimeType)) {
            throw new IllegalArgumentException();
        } else if (parameters == null || parameters.isEmpty()) {
            return (ContentTypeField) parse(ContentTypeField.PARSER, "Content-Type", mimeType);
        } else {
            StringBuilder sb = new StringBuilder(mimeType);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                sb.append("; ");
                sb.append(EncoderUtil.encodeHeaderParameter(entry.getKey(), entry.getValue()));
            }
            return contentType(sb.toString());
        }
    }

    public static ContentTransferEncodingField contentTransferEncoding(String contentTransferEncoding) {
        return (ContentTransferEncodingField) parse(ContentTransferEncodingField.PARSER, "Content-Transfer-Encoding", contentTransferEncoding);
    }

    public static ContentDispositionField contentDisposition(String contentDisposition) {
        return (ContentDispositionField) parse(ContentDispositionField.PARSER, "Content-Disposition", contentDisposition);
    }

    public static ContentDispositionField contentDisposition(String dispositionType, Map<String, String> parameters) {
        if (!isValidDispositionType(dispositionType)) {
            throw new IllegalArgumentException();
        } else if (parameters == null || parameters.isEmpty()) {
            return (ContentDispositionField) parse(ContentDispositionField.PARSER, "Content-Disposition", dispositionType);
        } else {
            StringBuilder sb = new StringBuilder(dispositionType);
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                sb.append("; ");
                sb.append(EncoderUtil.encodeHeaderParameter(entry.getKey(), entry.getValue()));
            }
            return contentDisposition(sb.toString());
        }
    }

    public static ContentDispositionField contentDisposition(String dispositionType, String filename) {
        return contentDisposition(dispositionType, filename, -1, (Date) null, (Date) null, (Date) null);
    }

    public static ContentDispositionField contentDisposition(String dispositionType, String filename, long size) {
        return contentDisposition(dispositionType, filename, size, (Date) null, (Date) null, (Date) null);
    }

    public static ContentDispositionField contentDisposition(String dispositionType, String filename, long size, Date creationDate, Date modificationDate, Date readDate) {
        Map<String, String> parameters = new HashMap<>();
        if (filename != null) {
            parameters.put("filename", filename);
        }
        if (size >= 0) {
            parameters.put("size", Long.toString(size));
        }
        if (creationDate != null) {
            parameters.put("creation-date", MimeUtil.formatDate(creationDate, (TimeZone) null));
        }
        if (modificationDate != null) {
            parameters.put("modification-date", MimeUtil.formatDate(modificationDate, (TimeZone) null));
        }
        if (readDate != null) {
            parameters.put("read-date", MimeUtil.formatDate(readDate, (TimeZone) null));
        }
        return contentDisposition(dispositionType, parameters);
    }

    public static DateTimeField date(Date date) {
        return date0(FieldName.DATE, date, (TimeZone) null);
    }

    public static DateTimeField date(String fieldName, Date date) {
        checkValidFieldName(fieldName);
        return date0(fieldName, date, (TimeZone) null);
    }

    public static DateTimeField date(String fieldName, Date date, TimeZone zone) {
        checkValidFieldName(fieldName);
        return date0(fieldName, date, zone);
    }

    public static Field messageId(String hostname) {
        return parse(UnstructuredField.PARSER, FieldName.MESSAGE_ID, MimeUtil.createUniqueMessageId(hostname));
    }

    public static UnstructuredField subject(String subject) {
        return (UnstructuredField) parse(UnstructuredField.PARSER, FieldName.SUBJECT, EncoderUtil.encodeIfNecessary(subject, EncoderUtil.Usage.TEXT_TOKEN, FieldName.SUBJECT.length() + 2));
    }

    public static MailboxField sender(Mailbox mailbox) {
        return mailbox0(FieldName.SENDER, mailbox);
    }

    public static MailboxListField from(Mailbox mailbox) {
        return mailboxList0(FieldName.FROM, Collections.singleton(mailbox));
    }

    public static MailboxListField from(Mailbox... mailboxes) {
        return mailboxList0(FieldName.FROM, Arrays.asList(mailboxes));
    }

    public static MailboxListField from(Iterable<Mailbox> mailboxes) {
        return mailboxList0(FieldName.FROM, mailboxes);
    }

    public static AddressListField to(Address address) {
        return addressList0(FieldName.TO, Collections.singleton(address));
    }

    public static AddressListField to(Address... addresses) {
        return addressList0(FieldName.TO, Arrays.asList(addresses));
    }

    public static AddressListField to(Iterable<Address> addresses) {
        return addressList0(FieldName.TO, addresses);
    }

    public static AddressListField cc(Address address) {
        return addressList0(FieldName.CC, Collections.singleton(address));
    }

    public static AddressListField cc(Address... addresses) {
        return addressList0(FieldName.CC, Arrays.asList(addresses));
    }

    public static AddressListField cc(Iterable<Address> addresses) {
        return addressList0(FieldName.CC, addresses);
    }

    public static AddressListField bcc(Address address) {
        return addressList0(FieldName.BCC, Collections.singleton(address));
    }

    public static AddressListField bcc(Address... addresses) {
        return addressList0(FieldName.BCC, Arrays.asList(addresses));
    }

    public static AddressListField bcc(Iterable<Address> addresses) {
        return addressList0(FieldName.BCC, addresses);
    }

    public static AddressListField replyTo(Address address) {
        return addressList0(FieldName.REPLY_TO, Collections.singleton(address));
    }

    public static AddressListField replyTo(Address... addresses) {
        return addressList0(FieldName.REPLY_TO, Arrays.asList(addresses));
    }

    public static AddressListField replyTo(Iterable<Address> addresses) {
        return addressList0(FieldName.REPLY_TO, addresses);
    }

    public static MailboxField mailbox(String fieldName, Mailbox mailbox) {
        checkValidFieldName(fieldName);
        return mailbox0(fieldName, mailbox);
    }

    public static MailboxListField mailboxList(String fieldName, Iterable<Mailbox> mailboxes) {
        checkValidFieldName(fieldName);
        return mailboxList0(fieldName, mailboxes);
    }

    public static AddressListField addressList(String fieldName, Iterable<Address> addresses) {
        checkValidFieldName(fieldName);
        return addressList0(fieldName, addresses);
    }

    private static DateTimeField date0(String fieldName, Date date, TimeZone zone) {
        return (DateTimeField) parse(DateTimeField.PARSER, fieldName, MimeUtil.formatDate(date, zone));
    }

    private static MailboxField mailbox0(String fieldName, Mailbox mailbox) {
        return (MailboxField) parse(MailboxField.PARSER, fieldName, encodeAddresses(Collections.singleton(mailbox)));
    }

    private static MailboxListField mailboxList0(String fieldName, Iterable<Mailbox> mailboxes) {
        return (MailboxListField) parse(MailboxListField.PARSER, fieldName, encodeAddresses(mailboxes));
    }

    private static AddressListField addressList0(String fieldName, Iterable<Address> addresses) {
        return (AddressListField) parse(AddressListField.PARSER, fieldName, encodeAddresses(addresses));
    }

    private static void checkValidFieldName(String fieldName) {
        if (!FIELD_NAME_PATTERN.matcher(fieldName).matches()) {
            throw new IllegalArgumentException("Invalid field name");
        }
    }

    private static boolean isValidMimeType(String mimeType) {
        int idx;
        if (mimeType == null || (idx = mimeType.indexOf(47)) == -1) {
            return false;
        }
        String type = mimeType.substring(0, idx);
        String subType = mimeType.substring(idx + 1);
        if (!EncoderUtil.isToken(type) || !EncoderUtil.isToken(subType)) {
            return false;
        }
        return true;
    }

    private static boolean isValidDispositionType(String dispositionType) {
        if (dispositionType == null) {
            return false;
        }
        return EncoderUtil.isToken(dispositionType);
    }

    private static <F extends Field> F parse(FieldParser parser, String fieldName, String fieldBody) {
        return parser.parse(fieldName, fieldBody, ContentUtil.encode(MimeUtil.fold(fieldName + ": " + fieldBody, 0)));
    }

    private static String encodeAddresses(Iterable<? extends Address> addresses) {
        StringBuilder sb = new StringBuilder();
        for (Address address : addresses) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(address.getEncodedString());
        }
        return sb.toString();
    }
}
