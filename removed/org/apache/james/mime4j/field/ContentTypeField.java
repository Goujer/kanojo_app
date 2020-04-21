package org.apache.james.mime4j.field;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.contenttype.parser.ContentTypeParser;
import org.apache.james.mime4j.field.contenttype.parser.ParseException;
import org.apache.james.mime4j.field.contenttype.parser.TokenMgrError;
import org.apache.james.mime4j.util.ByteSequence;

public class ContentTypeField extends AbstractField {
    public static final String PARAM_BOUNDARY = "boundary";
    public static final String PARAM_CHARSET = "charset";
    static final FieldParser PARSER = new FieldParser() {
        public ParsedField parse(String name, String body, ByteSequence raw) {
            return new ContentTypeField(name, body, raw);
        }
    };
    public static final String TYPE_MESSAGE_RFC822 = "message/rfc822";
    public static final String TYPE_MULTIPART_DIGEST = "multipart/digest";
    public static final String TYPE_MULTIPART_PREFIX = "multipart/";
    public static final String TYPE_TEXT_PLAIN = "text/plain";
    private static Log log = LogFactory.getLog(ContentTypeField.class);
    private String mimeType = "";
    private Map<String, String> parameters = new HashMap();
    private ParseException parseException;
    private boolean parsed = false;

    ContentTypeField(String name, String body, ByteSequence raw) {
        super(name, body, raw);
    }

    public ParseException getParseException() {
        if (!this.parsed) {
            parse();
        }
        return this.parseException;
    }

    public String getMimeType() {
        if (!this.parsed) {
            parse();
        }
        return this.mimeType;
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

    public boolean isMimeType(String mimeType2) {
        if (!this.parsed) {
            parse();
        }
        return this.mimeType.equalsIgnoreCase(mimeType2);
    }

    public boolean isMultipart() {
        if (!this.parsed) {
            parse();
        }
        return this.mimeType.startsWith(TYPE_MULTIPART_PREFIX);
    }

    public String getBoundary() {
        return getParameter(PARAM_BOUNDARY);
    }

    public String getCharset() {
        return getParameter(PARAM_CHARSET);
    }

    public static String getMimeType(ContentTypeField child, ContentTypeField parent) {
        if (child != null && child.getMimeType().length() != 0 && (!child.isMultipart() || child.getBoundary() != null)) {
            return child.getMimeType();
        }
        if (parent == null || !parent.isMimeType(TYPE_MULTIPART_DIGEST)) {
            return TYPE_TEXT_PLAIN;
        }
        return TYPE_MESSAGE_RFC822;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r0 = r2.getCharset();
     */
    public static String getCharset(ContentTypeField f) {
        String charset;
        return (f == null || charset == null || charset.length() <= 0) ? "us-ascii" : charset;
    }

    private void parse() {
        String body = getBody();
        ContentTypeParser parser = new ContentTypeParser((Reader) new StringReader(body));
        try {
            parser.parseAll();
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
        String type = parser.getType();
        String subType = parser.getSubType();
        if (!(type == null || subType == null)) {
            this.mimeType = (type + "/" + subType).toLowerCase();
            List<String> paramNames = parser.getParamNames();
            List<String> paramValues = parser.getParamValues();
            if (!(paramNames == null || paramValues == null)) {
                int len = Math.min(paramNames.size(), paramValues.size());
                for (int i = 0; i < len; i++) {
                    this.parameters.put(paramNames.get(i).toLowerCase(), paramValues.get(i));
                }
            }
        }
        this.parsed = true;
    }
}
