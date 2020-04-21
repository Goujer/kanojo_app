package com.google.zxing.aztec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.aztec.encoder.Encoder;
import com.google.zxing.common.BitMatrix;
import java.nio.charset.Charset;
import java.util.Map;

public final class AztecWriter implements Writer {
    private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) {
        return encode(contents, format, DEFAULT_CHARSET, 33);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) {
        String charset = (String) hints.get(EncodeHintType.CHARACTER_SET);
        Number eccPercent = (Number) hints.get(EncodeHintType.ERROR_CORRECTION);
        return encode(contents, format, charset == null ? DEFAULT_CHARSET : Charset.forName(charset), eccPercent == null ? 33 : eccPercent.intValue());
    }

    private static BitMatrix encode(String contents, BarcodeFormat format, Charset charset, int eccPercent) {
        if (format == BarcodeFormat.AZTEC) {
            return Encoder.encode(contents.getBytes(charset), eccPercent).getMatrix();
        }
        throw new IllegalArgumentException("Can only encode AZTEC, but got " + format);
    }
}
