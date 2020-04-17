package org.apache.james.mime4j.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.mime4j.field.ContentTypeField;

public final class MimeUtil {
    public static final String ENC_7BIT = "7bit";
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BASE64 = "base64";
    public static final String ENC_BINARY = "binary";
    public static final String ENC_QUOTED_PRINTABLE = "quoted-printable";
    public static final String MIME_HEADER_CONTENT_DESCRIPTION = "content-description";
    public static final String MIME_HEADER_CONTENT_DISPOSITION = "content-disposition";
    public static final String MIME_HEADER_CONTENT_ID = "content-id";
    public static final String MIME_HEADER_LANGAUGE = "content-language";
    public static final String MIME_HEADER_LOCATION = "content-location";
    public static final String MIME_HEADER_MD5 = "content-md5";
    public static final String MIME_HEADER_MIME_VERSION = "mime-version";
    public static final String PARAM_CREATION_DATE = "creation-date";
    public static final String PARAM_FILENAME = "filename";
    public static final String PARAM_MODIFICATION_DATE = "modification-date";
    public static final String PARAM_READ_DATE = "read-date";
    public static final String PARAM_SIZE = "size";
    private static final ThreadLocal<DateFormat> RFC822_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new Rfc822DateFormat();
        }
    };
    private static int counter = 0;
    private static final Log log = LogFactory.getLog(MimeUtil.class);
    private static final Random random = new Random();

    private MimeUtil() {
    }

    public static boolean isSameMimeType(String pType1, String pType2) {
        return (pType1 == null || pType2 == null || !pType1.equalsIgnoreCase(pType2)) ? false : true;
    }

    public static boolean isMessage(String pMimeType) {
        return pMimeType != null && pMimeType.equalsIgnoreCase(ContentTypeField.TYPE_MESSAGE_RFC822);
    }

    public static boolean isMultipart(String pMimeType) {
        return pMimeType != null && pMimeType.toLowerCase().startsWith(ContentTypeField.TYPE_MULTIPART_PREFIX);
    }

    public static boolean isBase64Encoding(String pTransferEncoding) {
        return ENC_BASE64.equalsIgnoreCase(pTransferEncoding);
    }

    public static boolean isQuotedPrintableEncoded(String pTransferEncoding) {
        return ENC_QUOTED_PRINTABLE.equalsIgnoreCase(pTransferEncoding);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    public static Map<String, String> getHeaderParams(String pValue) {
        String main;
        String rest;
        String pValue2 = unfold(pValue.trim());
        HashMap hashMap = new HashMap();
        if (pValue2.indexOf(";") == -1) {
            main = pValue2;
            rest = null;
        } else {
            main = pValue2.substring(0, pValue2.indexOf(";"));
            rest = pValue2.substring(main.length() + 1);
        }
        hashMap.put("", main);
        if (rest != null) {
            char[] chars = rest.toCharArray();
            StringBuilder sb = new StringBuilder(64);
            StringBuilder sb2 = new StringBuilder(64);
            byte state = 0;
            boolean escaped = false;
            for (char c : chars) {
                switch (state) {
                    case 0:
                        if (c == '=') {
                            log.error("Expected header param name, got '='");
                            state = 99;
                            break;
                        } else {
                            sb.setLength(0);
                            sb2.setLength(0);
                            state = 1;
                        }
                    case 1:
                        if (c == '=') {
                            if (sb.length() != 0) {
                                state = 2;
                                break;
                            } else {
                                state = 99;
                                break;
                            }
                        } else {
                            sb.append(c);
                            break;
                        }
                    case 2:
                        boolean fallThrough = false;
                        switch (c) {
                            case 9:
                            case ' ':
                                break;
                            case '\"':
                                state = 4;
                                break;
                            default:
                                state = 3;
                                fallThrough = true;
                                break;
                        }
                        if (!fallThrough) {
                            break;
                        }
                    case 3:
                        boolean fallThrough2 = false;
                        switch (c) {
                            case 9:
                            case ' ':
                            case ';':
                                hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString().trim());
                                state = 5;
                                fallThrough2 = true;
                                break;
                            default:
                                sb2.append(c);
                                break;
                        }
                        if (!fallThrough2) {
                            break;
                        }
                    case 4:
                        switch (c) {
                            case '\"':
                                if (escaped) {
                                    escaped = false;
                                    sb2.append(c);
                                    break;
                                } else {
                                    hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString());
                                    state = 5;
                                    break;
                                }
                            case '\\':
                                if (escaped) {
                                    sb2.append('\\');
                                }
                                if (escaped) {
                                    escaped = false;
                                    break;
                                } else {
                                    escaped = true;
                                    break;
                                }
                            default:
                                if (escaped) {
                                    sb2.append('\\');
                                }
                                escaped = false;
                                sb2.append(c);
                                break;
                        }
                    case 5:
                        switch (c) {
                            case 9:
                            case ' ':
                                break;
                            case ';':
                                state = 0;
                                break;
                            default:
                                state = 99;
                                break;
                        }
                    case 99:
                        if (c != ';') {
                            break;
                        } else {
                            state = 0;
                            break;
                        }
                }
            }
            if (state == 3) {
                hashMap.put(sb.toString().trim().toLowerCase(), sb2.toString().trim());
            }
        }
        return hashMap;
    }

    public static String createUniqueBoundary() {
        return "-=Part." + Integer.toHexString(nextCounterValue()) + '.' + Long.toHexString(random.nextLong()) + '.' + Long.toHexString(System.currentTimeMillis()) + '.' + Long.toHexString(random.nextLong()) + "=-";
    }

    public static String createUniqueMessageId(String hostName) {
        StringBuilder sb = new StringBuilder("<Mime4j.");
        sb.append(Integer.toHexString(nextCounterValue()));
        sb.append('.');
        sb.append(Long.toHexString(random.nextLong()));
        sb.append('.');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        if (hostName != null) {
            sb.append('@');
            sb.append(hostName);
        }
        sb.append('>');
        return sb.toString();
    }

    public static String formatDate(Date date, TimeZone zone) {
        DateFormat df = RFC822_DATE_FORMAT.get();
        if (zone == null) {
            df.setTimeZone(TimeZone.getDefault());
        } else {
            df.setTimeZone(zone);
        }
        return df.format(date);
    }

    public static String fold(String s, int usedCharacters) {
        int length = s.length();
        if (usedCharacters + length <= 76) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        int lastLineBreak = -usedCharacters;
        int wspIdx = indexOfWsp(s, 0);
        while (wspIdx != length) {
            int nextWspIdx = indexOfWsp(s, wspIdx + 1);
            if (nextWspIdx - lastLineBreak > 76) {
                sb.append(s.substring(Math.max(0, lastLineBreak), wspIdx));
                sb.append(CharsetUtil.CRLF);
                lastLineBreak = wspIdx;
            }
            wspIdx = nextWspIdx;
        }
        sb.append(s.substring(Math.max(0, lastLineBreak)));
        return sb.toString();
    }

    public static String unfold(String s) {
        int length = s.length();
        for (int idx = 0; idx < length; idx++) {
            char c = s.charAt(idx);
            if (c == 13 || c == 10) {
                return unfold0(s, idx);
            }
        }
        return s;
    }

    private static String unfold0(String s, int crlfIdx) {
        int length = s.length();
        StringBuilder sb = new StringBuilder(length);
        if (crlfIdx > 0) {
            sb.append(s.substring(0, crlfIdx));
        }
        for (int idx = crlfIdx + 1; idx < length; idx++) {
            char c = s.charAt(idx);
            if (!(c == 13 || c == 10)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int indexOfWsp(String s, int fromIndex) {
        int len = s.length();
        int index = fromIndex;
        while (index < len) {
            char c = s.charAt(index);
            if (c == ' ' || c == 9) {
                return index;
            }
            index++;
        }
        return len;
    }

    private static synchronized int nextCounterValue() {
        int i;
        synchronized (MimeUtil.class) {
            i = counter;
            counter = i + 1;
        }
        return i;
    }

    private static final class Rfc822DateFormat extends SimpleDateFormat {
        private static final long serialVersionUID = 1;

        public Rfc822DateFormat() {
            super("EEE, d MMM yyyy HH:mm:ss ", Locale.US);
        }

        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
            StringBuffer sb = super.format(date, toAppendTo, pos);
            int minutes = ((this.calendar.get(15) + this.calendar.get(16)) / 1000) / 60;
            if (minutes < 0) {
                sb.append('-');
                minutes = -minutes;
            } else {
                sb.append('+');
            }
            sb.append(String.format("%02d%02d", new Object[]{Integer.valueOf(minutes / 60), Integer.valueOf(minutes % 60)}));
            return sb;
        }
    }
}
