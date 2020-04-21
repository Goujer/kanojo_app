package org.apache.james.mime4j.field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.address.AddressList;
import org.apache.james.mime4j.field.address.parser.ParseException;
import org.apache.james.mime4j.util.ByteSequence;

public class AddressListField extends AbstractField {
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new AddressListField(name, body, raw);
        }
    };
    private static Log log = LogFactory.getLog(AddressListField.class);
    private AddressList addressList;
    private ParseException parseException;
    private boolean parsed = false;

    AddressListField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public AddressList getAddressList() {
        if (!this.parsed) {
            parse();
        }
        return this.addressList;
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
            this.addressList = AddressList.parse(body);
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e.getMessage());
            }
            this.parseException = e;
        }
        this.parsed = true;
    }
}
