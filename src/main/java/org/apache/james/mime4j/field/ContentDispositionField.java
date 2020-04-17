package org.apache.james.mime4j.field;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.contentdisposition.parser.ContentDispositionParser;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.field.datetime.parser.TokenMgrError;
import org.apache.james.mime4j.util.ByteSequence;

public class ContentDispositionField extends AbstractField {
    public static final String DISPOSITION_TYPE_ATTACHMENT = "attachment";
    public static final String DISPOSITION_TYPE_INLINE = "inline";
    public static final String PARAM_CREATION_DATE = "creation-date";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_MODIFICATION_DATE = "modification-date";
    public static final String PARAM_READ_DATE = "read-date";
    public static final String PARAM_SIZE = "size";
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new ContentDispositionField(name, body, raw);
        }
    };
    private static Log log = LogFactory.getLog(ContentDispositionField.class);
    private Date creationDate;
    private boolean creationDateParsed;
    private String dispositionType = "";
    private Date modificationDate;
    private boolean modificationDateParsed;
    private Map<String, String> parameters = new HashMap();
    private ParseException parseException;
    private boolean parsed = false;
    private Date readDate;
    private boolean readDateParsed;

    ContentDispositionField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public ParseException getParseException() {
        if (!this.parsed) {
            parse();
        }
        return this.parseException;
    }

    public String getDispositionType() {
        if (!this.parsed) {
            parse();
        }
        return this.dispositionType;
    }

    public String getParameter(String name) {
        if (!this.parsed) {
            parse();
        }
        return this.parameters.get(name.toLowerCase());
    }

    public Map<String, String> getParameters() {
        if (!this.parsed) {
            parse();
        }
        return Collections.unmodifiableMap(this.parameters);
    }

    public boolean isDispositionType(String dispositionType2) {
        if (!this.parsed) {
            parse();
        }
        return this.dispositionType.equalsIgnoreCase(dispositionType2);
    }

    public boolean isInline() {
        if (!this.parsed) {
            parse();
        }
        return this.dispositionType.equals(DISPOSITION_TYPE_INLINE);
    }

    public boolean isAttachment() {
        if (!this.parsed) {
            parse();
        }
        return this.dispositionType.equals(DISPOSITION_TYPE_ATTACHMENT);
    }

    public String getFilename() {
        return getParameter("filename");
    }

    public Date getCreationDate() {
        if (!this.creationDateParsed) {
            this.creationDate = parseDate("creation-date");
            this.creationDateParsed = true;
        }
        return this.creationDate;
    }

    public Date getModificationDate() {
        if (!this.modificationDateParsed) {
            this.modificationDate = parseDate("modification-date");
            this.modificationDateParsed = true;
        }
        return this.modificationDate;
    }

    public Date getReadDate() {
        if (!this.readDateParsed) {
            this.readDate = parseDate("read-date");
            this.readDateParsed = true;
        }
        return this.readDate;
    }

    public long getSize() {
        String value = getParameter("size");
        if (value == null) {
            return -1;
        }
        try {
            long size = Long.parseLong(value);
            if (size < 0) {
                size = -1;
            }
            return size;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Date parseDate(String paramName) {
        String value = getParameter(paramName);
        if (value != null) {
            try {
                return new DateTimeParser((Reader) new StringReader(value)).parseAll().getDate();
            } catch (ParseException e) {
                if (!log.isDebugEnabled()) {
                    return null;
                }
                log.debug("Parsing " + paramName + " '" + value + "': " + e.getMessage());
                return null;
            } catch (TokenMgrError e2) {
                if (!log.isDebugEnabled()) {
                    return null;
                }
                log.debug("Parsing " + paramName + " '" + value + "': " + e2.getMessage());
                return null;
            }
        } else if (!log.isDebugEnabled()) {
            return null;
        } else {
            log.debug("Parsing " + paramName + " null");
            return null;
        }
    }

    private void parse() {
        String body = getBody();
        ContentDispositionParser parser = new ContentDispositionParser((Reader) new StringReader(body));
        try {
            parser.parseAll();
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e.getMessage());
            }
            this.parseException = e;
        } catch (org.apache.james.mime4j.field.contentdisposition.parser.TokenMgrError e2) {
            if (log.isDebugEnabled()) {
                log.debug("Parsing value '" + body + "': " + e2.getMessage());
            }
            this.parseException = new ParseException(e2.getMessage());
        }
        String dispositionType2 = parser.getDispositionType();
        if (dispositionType2 != null) {
            this.dispositionType = dispositionType2.toLowerCase(Locale.US);
            List<String> paramNames = parser.getParamNames();
            List<String> paramValues = parser.getParamValues();
            if (!(paramNames == null || paramValues == null)) {
                int len = Math.min(paramNames.size(), paramValues.size());
                for (int i = 0; i < len; i++) {
                    this.parameters.put(paramNames.get(i).toLowerCase(Locale.US), paramValues.get(i));
                }
            }
        }
        this.parsed = true;
    }
}
