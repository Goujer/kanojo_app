package org.apache.james.mime4j.field;

import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.field.datetime.parser.ParseException;
import org.apache.james.mime4j.field.datetime.parser.TokenMgrError;
import org.apache.james.mime4j.util.ByteSequence;

public class DateTimeField extends AbstractField {
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new DateTimeField(name, body, raw);
        }
    };
    private static Log log = LogFactory.getLog(DateTimeField.class);
    private Date date;
    private ParseException parseException;
    private boolean parsed = false;

    DateTimeField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public Date getDate() {
        if (!this.parsed) {
            parse();
        }
        return this.date;
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
            this.date = new DateTimeParser((Reader) new StringReader(body)).parseAll().getDate();
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e.getMessage());
            }
            this.parseException = e;
        } catch (TokenMgrError e2) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e2.getMessage());
            }
            this.parseException = new ParseException(e2.getMessage());
        }
        this.parsed = true;
    }
}
