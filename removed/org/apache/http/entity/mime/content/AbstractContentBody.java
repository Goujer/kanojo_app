package org.apache.http.entity.mime.content;

public abstract class AbstractContentBody implements ContentBody {
    private final String mediaType;
    private final String mimeType;
    private final String subType;

    public AbstractContentBody(String mimeType2) {
        if (mimeType2 == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        this.mimeType = mimeType2;
        int i = mimeType2.indexOf(47);
        if (i != -1) {
            this.mediaType = mimeType2.substring(0, i);
            this.subType = mimeType2.substring(i + 1);
            return;
        }
        this.mediaType = mimeType2;
        this.subType = null;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public String getSubType() {
        return this.subType;
    }
}
