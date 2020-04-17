package org.jsoup.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.james.mime4j.field.ContentTypeField;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class DataUtil {
    private static final int bufferSize = 131072;
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
    static final String defaultCharset = "UTF-8";

    private DataUtil() {
    }

    public static Document load(File in, String charsetName, String baseUri) throws IOException {
        FileInputStream inStream = null;
        try {
            FileInputStream inStream2 = new FileInputStream(in);
            try {
                Document parseByteData = parseByteData(readToByteBuffer(inStream2), charsetName, baseUri, Parser.htmlParser());
                if (inStream2 != null) {
                    inStream2.close();
                }
                return parseByteData;
            } catch (Throwable th) {
                th = th;
                inStream = inStream2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (inStream != null) {
                inStream.close();
            }
            throw th;
        }
    }

    public static Document load(InputStream in, String charsetName, String baseUri) throws IOException {
        return parseByteData(readToByteBuffer(in), charsetName, baseUri, Parser.htmlParser());
    }

    public static Document load(InputStream in, String charsetName, String baseUri, Parser parser) throws IOException {
        return parseByteData(readToByteBuffer(in), charsetName, baseUri, parser);
    }

    static Document parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser) {
        String docData;
        Document doc = null;
        if (charsetName == null) {
            docData = Charset.forName(defaultCharset).decode(byteData).toString();
            doc = parser.parseInput(docData, baseUri);
            Element meta = doc.select("meta[http-equiv=content-type], meta[charset]").first();
            if (meta != null) {
                String foundCharset = meta.hasAttr("http-equiv") ? getCharsetFromContentType(meta.attr("content")) : meta.attr(ContentTypeField.PARAM_CHARSET);
                if (!(foundCharset == null || foundCharset.length() == 0 || foundCharset.equals(defaultCharset))) {
                    charsetName = foundCharset;
                    byteData.rewind();
                    docData = Charset.forName(foundCharset).decode(byteData).toString();
                    doc = null;
                }
            }
        } else {
            Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
            docData = Charset.forName(charsetName).decode(byteData).toString();
        }
        if (doc != null) {
            return doc;
        }
        if (docData.length() > 0 && docData.charAt(0) == 65279) {
            docData = docData.substring(1);
        }
        Document doc2 = parser.parseInput(docData, baseUri);
        doc2.outputSettings().charset(charsetName);
        return doc2;
    }

    static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException {
        boolean z;
        boolean capped;
        if (maxSize >= 0) {
            z = true;
        } else {
            z = false;
        }
        Validate.isTrue(z, "maxSize must be 0 (unlimited) or larger");
        if (maxSize > 0) {
            capped = true;
        } else {
            capped = false;
        }
        byte[] buffer = new byte[131072];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(131072);
        int remaining = maxSize;
        while (true) {
            int read = inStream.read(buffer);
            if (read == -1) {
                break;
            }
            if (capped) {
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(buffer, 0, read);
        }
        return ByteBuffer.wrap(outStream.toByteArray());
    }

    static ByteBuffer readToByteBuffer(InputStream inStream) throws IOException {
        return readToByteBuffer(inStream, 0);
    }

    static String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        Matcher m = charsetPattern.matcher(contentType);
        if (m.find()) {
            String charset = m.group(1).trim();
            if (Charset.isSupported(charset)) {
                return charset;
            }
            String charset2 = charset.toUpperCase(Locale.ENGLISH);
            if (Charset.isSupported(charset2)) {
                return charset2;
            }
        }
        return null;
    }
}
