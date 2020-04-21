package com.google.zxing.integration.android;

public final class IntentResult {
    private final String contents;
    private final String errorCorrectionLevel;
    private final String formatName;
    private final Integer orientation;
    private final byte[] rawBytes;

    IntentResult() {
        this((String) null, (String) null, (byte[]) null, (Integer) null, (String) null);
    }

    IntentResult(String contents2, String formatName2, byte[] rawBytes2, Integer orientation2, String errorCorrectionLevel2) {
        this.contents = contents2;
        this.formatName = formatName2;
        this.rawBytes = rawBytes2;
        this.orientation = orientation2;
        this.errorCorrectionLevel = errorCorrectionLevel2;
    }

    public String getContents() {
        return this.contents;
    }

    public String getFormatName() {
        return this.formatName;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public Integer getOrientation() {
        return this.orientation;
    }

    public String getErrorCorrectionLevel() {
        return this.errorCorrectionLevel;
    }

    public String toString() {
        StringBuilder dialogText = new StringBuilder(100);
        dialogText.append("Format: ").append(this.formatName).append(10);
        dialogText.append("Contents: ").append(this.contents).append(10);
        dialogText.append("Raw bytes: (").append(this.rawBytes == null ? 0 : this.rawBytes.length).append(" bytes)\n");
        dialogText.append("Orientation: ").append(this.orientation).append(10);
        dialogText.append("EC level: ").append(this.errorCorrectionLevel).append(10);
        return dialogText.toString();
    }
}
