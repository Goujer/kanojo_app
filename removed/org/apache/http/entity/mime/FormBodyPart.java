package org.apache.http.entity.mime;

import org.apache.http.entity.mime.content.ContentBody;

public class FormBodyPart {
    private final ContentBody body;
    private final Header header;
    private final String name;

    public FormBodyPart(String name2, ContentBody body2) {
        if (name2 == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (body2 == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = name2;
            this.body = body2;
            this.header = new Header();
            generateContentDisp(body2);
            generateContentType(body2);
            generateTransferEncoding(body2);
        }
    }

    public String getName() {
        return this.name;
    }

    public ContentBody getBody() {
        return this.body;
    }

    public Header getHeader() {
        return this.header;
    }

    public void addField(String name2, String value) {
        if (name2 == null) {
            throw new IllegalArgumentException("Field name may not be null");
        }
        this.header.addField(new MinimalField(name2, value));
    }

    /* access modifiers changed from: protected */
    public void generateContentDisp(ContentBody body2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("form-data; name=\"");
        buffer.append(getName());
        buffer.append("\"");
        if (body2.getFilename() != null) {
            buffer.append("; filename=\"");
            buffer.append(body2.getFilename());
            buffer.append("\"");
        }
        addField("Content-Disposition", buffer.toString());
    }

    /* access modifiers changed from: protected */
    public void generateContentType(ContentBody body2) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(body2.getMimeType());
        if (body2.getCharset() != null) {
            buffer.append("; charset=");
            buffer.append(body2.getCharset());
        }
        addField("Content-Type", buffer.toString());
    }

    /* access modifiers changed from: protected */
    public void generateTransferEncoding(ContentBody body2) {
        addField("Content-Transfer-Encoding", body2.getTransferEncoding());
    }
}
