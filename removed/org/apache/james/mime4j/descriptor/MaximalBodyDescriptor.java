package org.apache.james.mime4j.descriptor;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.apache.james.mime4j.field.datetime.parser.DateTimeParser;
import org.apache.james.mime4j.field.datetime.parser.ParseException;
import org.apache.james.mime4j.field.language.parser.ContentLanguageParser;
import org.apache.james.mime4j.field.mimeversion.parser.MimeVersionParser;
import org.apache.james.mime4j.field.structured.parser.StructuredFieldParser;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.MimeUtil;

public class MaximalBodyDescriptor extends DefaultBodyDescriptor {
    private static final int DEFAULT_MAJOR_VERSION = 1;
    private static final int DEFAULT_MINOR_VERSION = 0;
    private String contentDescription;
    private DateTime contentDispositionCreationDate;
    private MimeException contentDispositionCreationDateParseException;
    private DateTime contentDispositionModificationDate;
    private MimeException contentDispositionModificationDateParseException;
    private Map<String, String> contentDispositionParameters;
    private DateTime contentDispositionReadDate;
    private MimeException contentDispositionReadDateParseException;
    private long contentDispositionSize;
    private MimeException contentDispositionSizeParseException;
    private String contentDispositionType;
    private String contentId;
    private List<String> contentLanguage;
    private MimeException contentLanguageParseException;
    private String contentLocation;
    private MimeException contentLocationParseException;
    private String contentMD5Raw;
    private boolean isContentDescriptionSet;
    private boolean isContentDispositionSet;
    private boolean isContentIdSet;
    private boolean isContentLanguageSet;
    private boolean isContentLocationSet;
    private boolean isContentMD5Set;
    private boolean isMimeVersionSet;
    private int mimeMajorVersion;
    private int mimeMinorVersion;
    private MimeException mimeVersionException;

    protected MaximalBodyDescriptor() {
        this((BodyDescriptor) null);
    }

    public MaximalBodyDescriptor(BodyDescriptor parent) {
        super(parent);
        this.isMimeVersionSet = false;
        this.mimeMajorVersion = 1;
        this.mimeMinorVersion = 0;
        this.contentId = null;
        this.isContentIdSet = false;
        this.contentDescription = null;
        this.isContentDescriptionSet = false;
        this.contentDispositionType = null;
        this.contentDispositionParameters = Collections.emptyMap();
        this.contentDispositionModificationDate = null;
        this.contentDispositionModificationDateParseException = null;
        this.contentDispositionCreationDate = null;
        this.contentDispositionCreationDateParseException = null;
        this.contentDispositionReadDate = null;
        this.contentDispositionReadDateParseException = null;
        this.contentDispositionSize = -1;
        this.contentDispositionSizeParseException = null;
        this.isContentDispositionSet = false;
        this.contentLanguage = null;
        this.contentLanguageParseException = null;
        this.isContentIdSet = false;
        this.contentLocation = null;
        this.contentLocationParseException = null;
        this.isContentLocationSet = false;
        this.contentMD5Raw = null;
        this.isContentMD5Set = false;
    }

    public void addField(Field field) {
        String name = field.getName();
        String value = field.getBody();
        String name2 = name.trim().toLowerCase();
        if (MimeUtil.MIME_HEADER_MIME_VERSION.equals(name2) && !this.isMimeVersionSet) {
            parseMimeVersion(value);
        } else if (MimeUtil.MIME_HEADER_CONTENT_ID.equals(name2) && !this.isContentIdSet) {
            parseContentId(value);
        } else if (MimeUtil.MIME_HEADER_CONTENT_DESCRIPTION.equals(name2) && !this.isContentDescriptionSet) {
            parseContentDescription(value);
        } else if (MimeUtil.MIME_HEADER_CONTENT_DISPOSITION.equals(name2) && !this.isContentDispositionSet) {
            parseContentDisposition(value);
        } else if (MimeUtil.MIME_HEADER_LANGAUGE.equals(name2) && !this.isContentLanguageSet) {
            parseLanguage(value);
        } else if (MimeUtil.MIME_HEADER_LOCATION.equals(name2) && !this.isContentLocationSet) {
            parseLocation(value);
        } else if (!MimeUtil.MIME_HEADER_MD5.equals(name2) || this.isContentMD5Set) {
            super.addField(field);
        } else {
            parseMD5(value);
        }
    }

    private void parseMD5(String value) {
        this.isContentMD5Set = true;
        if (value != null) {
            this.contentMD5Raw = value.trim();
        }
    }

    private void parseLocation(String value) {
        this.isContentLocationSet = true;
        if (value != null) {
            StructuredFieldParser parser = new StructuredFieldParser((Reader) new StringReader(value));
            parser.setFoldingPreserved(false);
            try {
                this.contentLocation = parser.parse();
            } catch (MimeException e) {
                this.contentLocationParseException = e;
            }
        }
    }

    private void parseLanguage(String value) {
        this.isContentLanguageSet = true;
        if (value != null) {
            try {
                this.contentLanguage = new ContentLanguageParser((Reader) new StringReader(value)).parse();
            } catch (MimeException e) {
                this.contentLanguageParseException = e;
            }
        }
    }

    private void parseContentDisposition(String value) {
        this.isContentDispositionSet = true;
        this.contentDispositionParameters = MimeUtil.getHeaderParams(value);
        this.contentDispositionType = this.contentDispositionParameters.get("");
        String contentDispositionModificationDate2 = this.contentDispositionParameters.get("modification-date");
        if (contentDispositionModificationDate2 != null) {
            try {
                this.contentDispositionModificationDate = parseDate(contentDispositionModificationDate2);
            } catch (ParseException e) {
                this.contentDispositionModificationDateParseException = e;
            }
        }
        String contentDispositionCreationDate2 = this.contentDispositionParameters.get("creation-date");
        if (contentDispositionCreationDate2 != null) {
            try {
                this.contentDispositionCreationDate = parseDate(contentDispositionCreationDate2);
            } catch (ParseException e2) {
                this.contentDispositionCreationDateParseException = e2;
            }
        }
        String contentDispositionReadDate2 = this.contentDispositionParameters.get("read-date");
        if (contentDispositionReadDate2 != null) {
            try {
                this.contentDispositionReadDate = parseDate(contentDispositionReadDate2);
            } catch (ParseException e3) {
                this.contentDispositionReadDateParseException = e3;
            }
        }
        String size = this.contentDispositionParameters.get("size");
        if (size != null) {
            try {
                this.contentDispositionSize = Long.parseLong(size);
            } catch (NumberFormatException e4) {
                this.contentDispositionSizeParseException = (MimeException) new MimeException(e4.getMessage(), e4).fillInStackTrace();
            }
        }
        this.contentDispositionParameters.remove("");
    }

    private DateTime parseDate(String date) throws ParseException {
        return new DateTimeParser((Reader) new StringReader(date)).date_time();
    }

    private void parseContentDescription(String value) {
        if (value == null) {
            this.contentDescription = "";
        } else {
            this.contentDescription = value.trim();
        }
        this.isContentDescriptionSet = true;
    }

    private void parseContentId(String value) {
        if (value == null) {
            this.contentId = "";
        } else {
            this.contentId = value.trim();
        }
        this.isContentIdSet = true;
    }

    private void parseMimeVersion(String value) {
        MimeVersionParser parser = new MimeVersionParser((Reader) new StringReader(value));
        try {
            parser.parse();
            int major = parser.getMajorVersion();
            if (major != -1) {
                this.mimeMajorVersion = major;
            }
            int minor = parser.getMinorVersion();
            if (minor != -1) {
                this.mimeMinorVersion = minor;
            }
        } catch (MimeException e) {
            this.mimeVersionException = e;
        }
        this.isMimeVersionSet = true;
    }

    public int getMimeMajorVersion() {
        return this.mimeMajorVersion;
    }

    public int getMimeMinorVersion() {
        return this.mimeMinorVersion;
    }

    public MimeException getMimeVersionParseException() {
        return this.mimeVersionException;
    }

    public String getContentDescription() {
        return this.contentDescription;
    }

    public String getContentId() {
        return this.contentId;
    }

    public String getContentDispositionType() {
        return this.contentDispositionType;
    }

    public Map<String, String> getContentDispositionParameters() {
        return this.contentDispositionParameters;
    }

    public String getContentDispositionFilename() {
        return this.contentDispositionParameters.get("filename");
    }

    public DateTime getContentDispositionModificationDate() {
        return this.contentDispositionModificationDate;
    }

    public MimeException getContentDispositionModificationDateParseException() {
        return this.contentDispositionModificationDateParseException;
    }

    public DateTime getContentDispositionCreationDate() {
        return this.contentDispositionCreationDate;
    }

    public MimeException getContentDispositionCreationDateParseException() {
        return this.contentDispositionCreationDateParseException;
    }

    public DateTime getContentDispositionReadDate() {
        return this.contentDispositionReadDate;
    }

    public MimeException getContentDispositionReadDateParseException() {
        return this.contentDispositionReadDateParseException;
    }

    public long getContentDispositionSize() {
        return this.contentDispositionSize;
    }

    public MimeException getContentDispositionSizeParseException() {
        return this.contentDispositionSizeParseException;
    }

    public List<String> getContentLanguage() {
        return this.contentLanguage;
    }

    public MimeException getContentLanguageParseException() {
        return this.contentLanguageParseException;
    }

    public String getContentLocation() {
        return this.contentLocation;
    }

    public MimeException getContentLocationParseException() {
        return this.contentLocationParseException;
    }

    public String getContentMD5Raw() {
        return this.contentMD5Raw;
    }
}
